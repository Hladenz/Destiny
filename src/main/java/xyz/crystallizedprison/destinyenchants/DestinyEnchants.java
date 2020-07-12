package xyz.crystallizedprison.destinyenchants;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crystallizedprison.destinyenchants.Commands.destiny;
import xyz.crystallizedprison.destinyenchants.Commands.etokens;
import xyz.crystallizedprison.destinyenchants.ConfigFiles.etokenConfig;
import xyz.crystallizedprison.destinyenchants.Intergrations.PAPI;
import xyz.crystallizedprison.destinyenchants.Objects.PlayerInfo;
import xyz.crystallizedprison.destinyenchants.handlers.ETokenHandler;

public final class DestinyEnchants extends JavaPlugin {

    private final EnchantmentManager em = new EnchantmentManager(this);
    private final xyz.crystallizedprison.destinyenchants.ConfigFiles.etokenConfig etokenConfig = new etokenConfig();
    private final ETokenHandler eth = new ETokenHandler(this);


    public EnchantmentManager getEm() {
        return em;
    }

    public ETokenHandler getEth() {
        return eth;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("destiny").setExecutor(new destiny(this));
        getCommand("etokens").setExecutor(new etokens(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        etokenConfig.setup();

        new PAPI(this).register();

        em.registerAllCustomEnchantments();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        em.unregisterAllCustomEnchantments();
    }

    public void SavePlayerInfo(PlayerInfo playerInfo){
        ConfigurationSection section;
        if (etokenConfig.get().contains("data."+playerInfo.getPlayer().getUniqueId().toString())){
            section= etokenConfig.get().getConfigurationSection("data."+playerInfo.getPlayer().getUniqueId().toString());
        }else{
            section= etokenConfig.get().createSection("data."+playerInfo.getPlayer().getUniqueId().toString());
        }

        section.set("tokens",playerInfo.getTokens());

        etokenConfig.save();
    }

    public PlayerInfo GetPlayerInfo(Player player){
        ConfigurationSection section;
        String uuid = player.getUniqueId().toString();
        if (etokenConfig.get().contains("data."+uuid)){
            section= etokenConfig.get().getConfigurationSection("data."+uuid);
        }else{
            return new PlayerInfo(player,0);
        }

        double Tokens = section.getDouble("tokens");

        return new PlayerInfo(player,Tokens);
    }
}
