package xyz.crystallizedprison.destinyenchants;


import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crystallizedprison.destinyenchants.Enchants.*;
import xyz.crystallizedprison.destinyenchants.reflect.MemberType;
import xyz.crystallizedprison.destinyenchants.reflect.ReflectionCache;
import xyz.crystallizedprison.destinyenchants.reflect.ReflectionDefinition;

import java.lang.reflect.Field;
import java.util.*;

/**
 * In charge of holding and managing the custom enchantments.
 * This should be an effective singleton (1 instance per Plugin).
 */
public final class EnchantmentManager
{

    private static final ReflectionDefinition ACCEPTING_NEW_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "acceptingNew");
    private static final ReflectionDefinition BY_KEY_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "byKey");
    private static final ReflectionDefinition BY_NAME_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "byName");

    private final JavaPlugin plugin;

    private final HashMap<String,Enchantment> customEnchantments;
    private final HashMap<Enchantment,Integer> enchantCost;

    private final Explosive explosive;
    private final Efficiency efficiency;
    private final Fortune fortune;
    private final Haste haste;
    private final Speed speed;

    /**
     * Create a new EnchantmentManager.
     *
     * @param plugin the plugin managing the enchantments.
     */
    public EnchantmentManager(JavaPlugin plugin)
    {
        this.plugin = plugin;

        this.customEnchantments = new HashMap<>();
        this.enchantCost = new HashMap<>();

        this.explosive = new Explosive(plugin);
        this.efficiency = new Efficiency(plugin);
        this.fortune = new Fortune(plugin);
        this.haste = new Haste(plugin);
        this.speed = new Speed(plugin);

        customEnchantments.put(explosive.getName().toLowerCase(),explosive);
        customEnchantments.put(efficiency.getName().toLowerCase(),efficiency);
        customEnchantments.put(fortune.getName().toLowerCase(),fortune);
        customEnchantments.put(haste.getName().toLowerCase(),haste);
        customEnchantments.put(speed.getName().toLowerCase(),speed);

        enchantCost.put(explosive,plugin.getConfig().getInt("EnchantSettings."+explosive.getName()+".cost"));
        enchantCost.put(efficiency,plugin.getConfig().getInt("EnchantSettings."+efficiency.getName()+".cost"));
        enchantCost.put(fortune,plugin.getConfig().getInt("EnchantSettings."+fortune.getName()+".cost"));
        enchantCost.put(haste,plugin.getConfig().getInt("EnchantSettings."+haste.getName()+".cost"));
        enchantCost.put(speed,plugin.getConfig().getInt("EnchantSettings."+speed.getName()+".cost"));

    }

    /**
     * Register all custom enchantments with Bukkit.
     * This uses reflection to hook into Bukkit's "acceptingNew" field to allow new Enchantments to be registered.
     * This method also registers any enchantments that are event handlers.
     * This method should only be called once per server startup.
     */
    public void registerAllCustomEnchantments()
    {
        try
        {
            //Reflection to allow registering new enchants. It's locked by default.
            Field acceptingField = (Field) ReflectionCache.get(ACCEPTING_NEW_FIELD_DEF);
            acceptingField.set(null, true);

            //Register all enchants here
            for (Enchantment enchantment : customEnchantments.values())
            {
                if (!enchantment.equals(efficiency) && !enchantment.equals(fortune)) {
                    Enchantment.registerEnchantment(enchantment);
                    if (enchantment instanceof Listener) {
                        Bukkit.getPluginManager().registerEvents((Listener) enchantment, plugin);
                    }
                }

            }
        }
        catch (Exception ex)
        {
            System.out.println("Could not register custom enchantments:"+ex);
        }
        finally
        {
            Enchantment.stopAcceptingRegistrations();
        }
    }

    /**
     * Reflectively unregister all custom enchantments from Bukkit, and their event handlers.
     */
    public void unregisterAllCustomEnchantments()
    {
        try
        {
            Field byKeyField = (Field) ReflectionCache.get(BY_KEY_FIELD_DEF);
            Field byNameField = (Field) ReflectionCache.get(BY_NAME_FIELD_DEF);

            @SuppressWarnings("unchecked") Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            @SuppressWarnings("unchecked") Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            for (Enchantment enchantment : customEnchantments.values())
            {
                byKey.remove(enchantment.getKey());
                //noinspection deprecation
                byName.remove(enchantment.getName());

                if (enchantment instanceof Listener)
                {
                    HandlerList.unregisterAll((Listener) enchantment);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Could not unregister custom enchantments:"+e);
        }
    }

    public Explosive getexplosive()
    {
        return explosive;
    }

    public HashMap<String,Enchantment> getCustomEnchantments()
    {
        return this.customEnchantments;
    }
}
