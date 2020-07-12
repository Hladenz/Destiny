package xyz.crystallizedprison.destinyenchants.handlers;

import org.bukkit.enchantments.Enchantment;
import xyz.crystallizedprison.destinyenchants.ConfigFiles.etokenConfig;
import xyz.crystallizedprison.destinyenchants.DestinyEnchants;

public class ETokenHandler {

    DestinyEnchants main;

    public ETokenHandler(DestinyEnchants main) {
        this.main = main;
    }


    public int GetCost(Enchantment enchantment,int Level,int levelincrease){
        int Enchantcost = main.getConfig().getInt("EnchantSettings."+enchantment.getName()+".cost");
        int cost =0;
        for (int i = 1;i<=levelincrease;i++){
            cost+=Enchantcost*(Level+i);
        }
        return cost;
    }




}
