package xyz.crystallizedprison.destinyenchants.Enchants;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.clip.autosell.events.DropsToInventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crystallizedprison.destinyenchants.EnchantmentUtil;
import xyz.crystallizedprison.destinyenchants.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SplittableRandom;

/**
 * A simple custom enchantment.
 * It will be called "Blast" and only applies to pickaxes, spawning TNT when players mine blocks.
 * We will do our event handling in this class.
 */
public final class Explosive extends Enchantment implements Listener
{

    private static int MAX_LEVEL = 0;
    private static final String ENCHANTMENT_NAME = "Explosive";
    private static int size = 4;
    private static double chance = 2;
    /**
     * This {@link SplittableRandom} will be used for determining probability for "procs".
     * The use of a final field means we don't have to create a new Random every time (which is a heavy operation).
     */
    private final SplittableRandom random;
    private final Plugin plugin;

    /**
     * Main constructor for the enchantment.
     * This provides the {@link NamespacedKey} that Bukkit requires.
     *
     * @param plugin the plugin in charge of registering this enchantment
     */
    public Explosive( final JavaPlugin plugin)
    {
        super(new NamespacedKey(plugin, ENCHANTMENT_NAME));
        this.random = new SplittableRandom();
        this.plugin = plugin;

        this.MAX_LEVEL = plugin.getConfig().getInt("EnchantSettings."+ENCHANTMENT_NAME+".max");
        this.size = plugin.getConfig().getInt("EnchantSettings."+ENCHANTMENT_NAME+".radius");
        this.chance = plugin.getConfig().getDouble("EnchantSettings."+ENCHANTMENT_NAME+".chance");
    }

    /**
     * @return the name of the enchantment.
     */
    @Override

    public final String getName()
    {
        return ENCHANTMENT_NAME;
    }

    /**
     * Get the maximum level of the enchantment.
     * Any level above this will require {@link ItemStack#addUnsafeEnchantment(Enchantment, int)}
     *
     * @return the maximum level
     */
    @Override
    public final int getMaxLevel()
    {
        return MAX_LEVEL;
    }

    /**
     * Get the level this enchantment should start at.
     * 99% of the time this should be 0
     *
     * @return the level this enchantment should start at
     */
    @Override
    public final int getStartLevel()
    {
        return 0;
    }

    /**
     * Get the target for this enchantment.
     * This represents the types of items that can receive this enchantment.
     * In this case it's {@link EnchantmentTarget#TOOL}, as there is nothing specific to pickaxes.
     *
     * @return the enchantment's target
     */
    @Override

    public final EnchantmentTarget getItemTarget()
    {
        return EnchantmentTarget.TOOL;
    }

    /**
     * Get if this enchantment is treasure - meaning it can only be obtained from trading, fishing, etc.
     * This will **NOT** add the enchantment to loot tables! It serves only as a marker.
     *
     * @return if this enchantment is treasure.
     */
    @Override
    public final boolean isTreasure()
    {
        return false;
    }

    /**
     * Get if this enchantment is cursed. This method should not be used.
     *
     * @return if this enchantment is cursed.
     */
    @Override
    public final boolean isCursed()
    {
        return false;
    }

    /**
     * Get if this enchantment conflicts with another.
     * For example, sharpness conflicts with smite.
     *
     * @param other the other enchantment to compare
     * @return if this enchantment conflicts with {other}
     */
    @Override
    public final boolean conflictsWith( final Enchantment other)
    {
        return false;
    }

    /**
     * If a given item can receive this enchantment.
     * Here we check that the item is a pickaxe using {@link EnchantmentUtil#isPickaxe(ItemStack)}
     *
     * @param item the item to check
     * @return if the given item can receive this enchantment
     */
    @Override
    public final boolean canEnchantItem( final ItemStack item)
    {
        return EnchantmentUtil.isPickaxe(item);
    }


    /**
     * The event handler of our enchantment.
     * This will listen to {@link BlockBreakEvent} and process it accordingly.
     *
     * @param event the event.
     */


    @EventHandler
    public final void onBlockBreak( final BlockBreakEvent event)
    {
        int level = event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(this);

        //Eliminate unwanted conditions as soon as possible.
        if (level == 0)
        {
            return;
        }

        if (this.plugin.getConfig().getStringList("EnchantSettings."+ENCHANTMENT_NAME+".blacklisted-worlds").contains(event.getBlock().getLocation().getWorld().getName())){
            return;
        }

        //We will do a 0.5% chance per level.
        double percentageChance = level / 2.0;



        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();


        List<ItemStack> drops = new ArrayList<>();
        for (int x = -size; x < size; x++) { 		  //Loop through X coordinates
            for (int y = -size; y < size; y++) {	  //Loop through Y coordinates
                for (int z = -size; z < size; z++) {  //Loop through Z coordinates

                    Block block = event.getBlock().getRelative(x, y, z); //Block to be removed, relative to e.getBlock()
                    if(block.getType() == Material.BEDROCK || block.getType() == Material.AIR) continue;


                    if (!Functions.getChance(chance*level))
                    {
                        //The enchantment hasn't triggered
                        continue;
                    }
                    if (!query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_BREAK)) {
                        continue;
                    }
                    drops.addAll(block.getDrops(event.getPlayer().getItemInHand()));//Break block & Drop item, if item used is correct.
                    block.setType(Material.AIR);

                }
            }
        }
        DropsToInventoryEvent var14 = new DropsToInventoryEvent(event.getPlayer(), drops, event.getBlock());
        Bukkit.getPluginManager().callEvent(var14);

        if (var14.isCancelled()){
            return;
        }

        for (ItemStack drop:var14.getDrops()){
            event.getPlayer().getInventory().addItem(drop);
        }


    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Enchantment)) return false;
        return ((Enchantment) other).getKey().equals(this.getKey());
    }

    @Override
    public int hashCode()
    {
        return getKey().hashCode();
    }
}
