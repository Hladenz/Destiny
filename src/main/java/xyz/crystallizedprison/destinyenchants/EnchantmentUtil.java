package xyz.crystallizedprison.destinyenchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.crystallizedprison.destinyenchants.Enchants.Efficiency;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class EnchantmentUtil
{

    private static final Set<Material> PICKAXE_TYPES = EnumSet.of(
            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE);

    private EnchantmentUtil()
    {

    }

    public static boolean isPickaxe( final ItemStack item)
    {
        return PICKAXE_TYPES.contains(item.getType());
    }

    public static String GetName(final Enchantment enchantment){
        if(enchantment.equals(Enchantment.DIG_SPEED)){
            return "Efficiency";
        }else if (enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)){
            return "Fortune";

        }else {
            return enchantment.getName();
        }
    }

	/**
	 * Because these are custom enchantments, the Minecraft client does not add them to the lore automatically.
	 * As such, we have to add it ourselves.
	 * <p>
	 * This method uses {@link ItemMeta} and a few String operations to add the lore manually, removing old versioned lore.
	 *
	 * @param enchantment the enchantment to add.
	 * @param itemStack   the item to add the enchantment to.
	 * @param level       the level of the enchantment to add.
	 * @throws IllegalArgumentException if the enchantment is unsafe (more than the maximum level).
	 * @author Alex Wood
	 */

    public static void applyEnchantment( final Enchantment enchantment,  final ItemStack itemStack, final int level) throws IllegalArgumentException
    {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return; //the item type is probably AIR so can't be enchanted.

        List<String> lore = new ArrayList<>(); //a nice little micro-optimization since we're only adding 1 element to the list

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemMeta.addEnchant(enchantment, level,true);


        lore.add(ChatColor.DARK_AQUA + "Enchantments:");

        int Index =0;
        for (Enchantment ench:itemMeta.getEnchants().keySet()) {
            if (Index == 0) {
                //⬜
                lore.add(ChatColor.AQUA + "╔ " + GetName(ench) + " " + ChatColor.DARK_AQUA + itemMeta.getEnchants().get(ench));
            } else if (Index == itemMeta.getEnchants().keySet().size()-1) {
                lore.add(ChatColor.AQUA + "╚ " +  GetName(ench) + " " + ChatColor.DARK_AQUA + itemMeta.getEnchants().get(ench));
            } else {
                lore.add(ChatColor.AQUA + "║ " +  GetName(ench) + " " + ChatColor.DARK_AQUA + itemMeta.getEnchants().get(ench));
            }
            Index++;
        }

        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

    }
}
