package xyz.crystallizedprison.destinyenchants.ConfigFiles;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class etokenConfig {
    private static File file;
    private static FileConfiguration customfile;

    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("DestinyEnchants").getDataFolder(),"etokenConfig.yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                getLogger().log(Level.ALL,"Failed to create File");

            }

        }
        customfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customfile;

    }

    public static void save(){
        try {
            customfile.save(file);
        }catch (IOException e){
            System.out.println("Could'nt Save File");
        }
    }

    public static void reload(){
        customfile = YamlConfiguration.loadConfiguration(file);
    }
}
