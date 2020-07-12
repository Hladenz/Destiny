package xyz.crystallizedprison.destinyenchants.Enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.crystallizedprison.destinyenchants.EnchantmentUtil;

import java.util.SplittableRandom;

/**
 * A simple custom enchantment.
 * It will be called "Blast" and only applies to pickaxes, spawning TNT when players mine blocks.
 * We will do our event handling in this class.
 */
public final class Speed extends Enchantment implements Listener
{

    private static int MAX_LEVEL = 0;
    private static final String ENCHANTMENT_NAME = "Speed";
    /**
     * This {@link SplittableRandom} will be used for determining probability for "procs".
     * The use of a final field means we don't have to create a new Random every time (which is a heavy operation).
     */
    private final Plugin plugin;

    /**
     * Main constructor for the enchantment.
     * This provides the {@link NamespacedKey} that Bukkit requires.
     *
     * @param plugin the plugin in charge of registering this enchantment
     */
    public Speed(final JavaPlugin plugin)
    {
        super(new NamespacedKey(plugin, ENCHANTMENT_NAME));
        this.plugin = plugin;

        this.MAX_LEVEL = plugin.getConfig().getInt("EnchantSettings."+ENCHANTMENT_NAME+".max");
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
    public final void onBlockBreak( final PlayerItemHeldEvent event)
    {
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null){
            event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            return;
        }

        int level = event.getPlayer().getInventory().getItem(event.getNewSlot()).getEnchantmentLevel(this);

        //Eliminate unwanted conditions as soon as possible.
        if (level == 0)
        {
            event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            return;
        }

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,level),true );
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
