package xyz.crystallizedprison.destinyenchants.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import xyz.crystallizedprison.destinyenchants.DestinyEnchants;
import xyz.crystallizedprison.destinyenchants.EnchantmentUtil;
import xyz.crystallizedprison.destinyenchants.Objects.PlayerInfo;

public class destiny implements CommandExecutor {

    DestinyEnchants main;

    public destiny(DestinyEnchants main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p =((Player) sender).getPlayer();
            PlayerInfo playerInfo = main.GetPlayerInfo(p);

            if (args.length != 0){
                if (args[0].toLowerCase().equals("admin")){
                    if (!p.hasPermission("destiny.admin")){
                        p.sendMessage(ChatColor.DARK_RED+"HAAHHAHAHAHH GOOD TRY");
                        return true;
                    }

                    if (args.length < 2){
                        p.sendMessage(ChatColor.DARK_AQUA+"Please add a sub-command");
                        return true;
                    }

                    if (args[1].toLowerCase().equals("reload")){
                        main.reloadConfig();
                        p.sendMessage(ChatColor.DARK_AQUA + "config.yml Reloaded");
                    }
                }else if(args[0].toLowerCase().equals("enchant")){

                    if (args.length > 3){
                        p.sendMessage(ChatColor.DARK_AQUA+"Invalid format /destiny enchant [Enchant] [Level]");
                        return true;
                    }

                    if (!EnchantmentUtil.isPickaxe(p.getInventory().getItemInMainHand())){
                        p.sendMessage(ChatColor.DARK_AQUA+"You Can't Enchant this item");
                        return true;
                    }

                    if (!main.getEm().getCustomEnchantments().keySet().contains(args[1].toLowerCase())){
                        p.sendMessage(ChatColor.DARK_AQUA+"Invalid Enchant");
                        return true;
                    }

                    Enchantment enchantment = main.getEm().getCustomEnchantments().get(args[1].toLowerCase());
                    int level = p.getItemInHand().getItemMeta().getEnchantLevel(enchantment);
                    int increase = Integer.valueOf(args[2]);

                    if (level == enchantment.getMaxLevel()){
                        p.sendMessage(ChatColor.DARK_AQUA+"You are at max Level!");
                        return true;
                    }

                    if ((level+increase) > enchantment.getMaxLevel()){
                        increase = enchantment.getMaxLevel() - level;
                    }

                    int cost = main.getEth().GetCost(enchantment,level,increase);
                    if (playerInfo.getTokens() < cost){
                        p.sendMessage(ChatColor.DARK_AQUA+"You need " + ChatColor.AQUA + cost + ChatColor.DARK_AQUA + " Tokens to Enchant this!");
                        return true;
                    }

                    EnchantmentUtil.applyEnchantment(enchantment,p.getInventory().getItemInMainHand(),level+increase);

                    playerInfo.setTokens(playerInfo.getTokens()-cost);

                    main.SavePlayerInfo(playerInfo);

                    p.sendMessage(ChatColor.DARK_AQUA+"You Have just Bought " + ChatColor.AQUA+increase+"x"+ChatColor.DARK_AQUA+" Levels of " +enchantment.getName() );
                }
            }
        }

        return false;
    }
}
