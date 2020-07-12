package xyz.crystallizedprison.destinyenchants.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.crystallizedprison.destinyenchants.DestinyEnchants;
import xyz.crystallizedprison.destinyenchants.Objects.PlayerInfo;

public class etokens implements CommandExecutor {
    DestinyEnchants main;

    public etokens(DestinyEnchants main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            PlayerInfo playerInfo = main.GetPlayerInfo(player);
            if (args.length ==0) {
                player.sendMessage(ChatColor.DARK_AQUA + "You have " +ChatColor.AQUA+ playerInfo.getTokens() + ChatColor.DARK_AQUA+" Tokens");
                return true;
            }else{
                if (args[0].toLowerCase().equals("admin")){
                    if (!player.hasPermission("destiny.admin")){
                        player.sendMessage(ChatColor.RED+"You Don't Have perms for that!");
                        return true;
                    }

                    if (args.length == 1){
                        player.sendMessage(ChatColor.DARK_AQUA+"Please add a sub-command");
                        return true;
                    }

                    if (args[1].toLowerCase().equals("give")){
                        if (args.length < 4){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid format /etokens admin give [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(targetinfo.getTokens()+Integer.valueOf(args[3]));
                        player.sendMessage(ChatColor.DARK_AQUA+"Tokens given!");
                        target.sendMessage(ChatColor.DARK_AQUA + "You have been given "+ args[3]+" Tokens");
                        main.SavePlayerInfo(targetinfo);
                    }else if (args[1].toLowerCase().equals("take")){
                        if (args.length < 4){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid format /etokens admin take [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(targetinfo.getTokens()-Integer.valueOf(args[3]));
                        player.sendMessage(ChatColor.DARK_AQUA+"Tokens taken!");
                        target.sendMessage(ChatColor.DARK_AQUA + "You have lost "+ args[3]+" Tokens");
                        main.SavePlayerInfo(targetinfo);
                    }else if (args[1].toLowerCase().equals("set")){
                        if (args.length < 4){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid format /etokens admin set [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            player.sendMessage(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(Double.valueOf(args[3]));
                        player.sendMessage(ChatColor.DARK_AQUA+"Tokens set!");
                        target.sendMessage(ChatColor.DARK_AQUA + "Your Tokens has been set to "+ args[3]);
                        main.SavePlayerInfo(targetinfo);
                    }else{
                        player.sendMessage(ChatColor.DARK_AQUA+"Invalid command!");
                        return true;
                    }

                }
                else if (args[0].toLowerCase().equals("gift")){
                    if (args.length < 3){
                        player.sendMessage(ChatColor.DARK_AQUA+"Invalid format /etokens gift [Player] [amount]");
                        return true;
                    }

                    if (Bukkit.getPlayer(args[2]) == null){
                        player.sendMessage(ChatColor.DARK_AQUA+"Invalid Player");
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[2]);
                    PlayerInfo targetinfo = main.GetPlayerInfo(target);

                    targetinfo.setTokens(targetinfo.getTokens()+Integer.valueOf(args[3]));
                    player.sendMessage(ChatColor.DARK_AQUA+"Tokens gifted!");
                    target.sendMessage(ChatColor.DARK_AQUA + "You have been gifted "+ args[3]+" Tokens");
                    return true;
                }else if (Bukkit.getPlayer(args[1]) != null){

                    Player target = Bukkit.getPlayer(args[1]);
                    PlayerInfo targetinfo = main.GetPlayerInfo(target);

                    target.sendMessage(ChatColor.DARK_AQUA + args[1] + " Has "+ targetinfo.getTokens()+" Tokens");
                    return true;
                }
            }

        }else{
            if (args.length !=0) {
                return true;
            }
            else{
                if (args[0].toLowerCase().equals("admin")){

                    if (args.length == 1){
                        System.out.println(ChatColor.DARK_AQUA+"Please add a sub-command");
                        return true;
                    }

                    if (args[1].toLowerCase().equals("give")){
                        if (args.length < 4){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid format /etokens admin give [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(targetinfo.getTokens()+Integer.valueOf(args[3]));
                        target.sendMessage(ChatColor.DARK_AQUA + "You have been given "+ args[3]+" Tokens");
                    }else if (args[1].toLowerCase().equals("take")){
                        if (args.length < 4){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid format /etokens admin take [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(targetinfo.getTokens()-Integer.valueOf(args[3]));
                        target.sendMessage(ChatColor.DARK_AQUA + "You have lost "+ args[3]+" Tokens");
                    }else if (args[1].toLowerCase().equals("set")){
                        if (args.length < 4){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid format /etokens admin set [Player] [amount]");
                            return true;
                        }

                        if (Bukkit.getPlayer(args[2]) == null){
                            System.out.println(ChatColor.DARK_AQUA+"Invalid Player");
                            return true;
                        }

                        Player target = Bukkit.getPlayer(args[2]);
                        PlayerInfo targetinfo = main.GetPlayerInfo(target);

                        targetinfo.setTokens(Double.valueOf(args[3]));
                        target.sendMessage(ChatColor.DARK_AQUA + "Your Tokens has been set to "+ args[3]);
                    }else{
                        System.out.println(ChatColor.DARK_AQUA+"Invalid command!");
                        return true;
                    }

                }
            }
        }

        return false;
    }
}
