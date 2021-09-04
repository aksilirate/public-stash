package me.axilirate.publicstash;

import me.axilirate.publicstash.commands.ps;
import me.axilirate.publicstash.tasks.AutoClear;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class PublicStash extends JavaPlugin {

    public DataManager dataManager = new DataManager(this);

    public HashMap<Player,Inventory> playersOpenedStash = new HashMap<>();

    public HashMap<Player,Integer> playersOpenedStashIndex = new HashMap<>();


    public boolean inventoryUpdated = true;

    public int inventorySize = 9;
    public int stashAmount;
    public boolean despawnedItemsToStash = true;
    public String stashItemType = "CHEST";
    public boolean debugModeEnabled = false;
    ArrayList<String> disabledDespawnedItemsToStash = new ArrayList<>();
    ArrayList<String> disabledItems = new ArrayList<>();
    public int autoClearTimeInTicks = 0;


    File translationFile = new File(this.getDataFolder() + "/language.yml");
    YamlConfiguration translationYml = YamlConfiguration.loadConfiguration(translationFile);


    @Override
    public void onEnable() {

        FileConfiguration config = this.getConfig();

        ArrayList<String> defaultDisabledDespawnedItemsToStash = new ArrayList<>();
        defaultDisabledDespawnedItemsToStash.add("BEDROCK");
        defaultDisabledDespawnedItemsToStash.add("BARRIER");

        ArrayList<String> defaultDisabledItems = new ArrayList<>();
        defaultDisabledItems.add("BEDROCK");
        defaultDisabledItems.add("BARRIER");



        config.addDefault("stash-amount", 9);
        config.addDefault("auto-clear-time-in-ticks", 0);
        config.addDefault("despawned-items-to-stash", true);
        config.addDefault("disabled-despawned-items-to-stash", defaultDisabledDespawnedItemsToStash);
        config.addDefault("disabled-items", defaultDisabledItems);
        config.addDefault("stash-item-type", "CHEST");
        config.addDefault("debug-mode-enabled", false);


        config.options().copyDefaults(true);
        saveConfig();



        stashAmount = config.getInt("stash-amount");
        despawnedItemsToStash = config.getBoolean("despawned-items-to-stash");
        stashItemType = config.getString("stash-item-type");
        debugModeEnabled = config.getBoolean("debug-mode-enabled");
        autoClearTimeInTicks = config.getInt("auto-clear-time-in-ticks");


        disabledDespawnedItemsToStash = (ArrayList<String>) config.getList("disabled-despawned-items-to-stash");
        disabledItems = (ArrayList<String>) config.getList("disabled-items");




        if (stashAmount != 0){
            inventorySize = (int) Math.ceil( (float) stashAmount / 9) * 9;
        }

        if (debugModeEnabled){
            System.out.println("Stash amount is set to " + stashAmount);
            System.out.println("Inventory size is set to " + inventorySize);
            System.out.println("Disabled despawned items to stash is set to " + disabledDespawnedItemsToStash);
            System.out.println("Disabled items is set to " + disabledItems);
        }



        translationYml.addDefault("default.public-stash-title", "&rPublic Stash");
        translationYml.addDefault("en_us.public-stash-title", "&rPublic Stash");
        translationYml.addDefault("zh_tw.public-stash-title", "&r公共倉庫");
        translationYml.addDefault("de_de.public-stash-title", "&rÖffentlicher Vorrat");



        translationYml.addDefault("default.stash-name", "&rStash");
        translationYml.addDefault("en_us.stash-name", "&rStash");
        translationYml.addDefault("zh_tw.stash-name", "&r倉庫");
        translationYml.addDefault("de_de.stash-name", "&rVorrat");

        translationYml.options().copyDefaults(true);

        dataManager.saveYamlFile(translationFile, translationYml);



        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("ps").setExecutor(new ps(this));



        if (autoClearTimeInTicks > 0){
            BukkitTask autoClear = new AutoClear(this).runTaskTimer(this, autoClearTimeInTicks, autoClearTimeInTicks);

            if (debugModeEnabled){
                System.out.println("Auto clear enabled");
            }

        }


    }






    public void openPublicStash(Player player){

        String language = player.getLocale();

        String title =  translationYml.getString( language.toLowerCase() + ".public-stash-title" );
        if (title == null){
            title = translationYml.getString("default.public-stash-title");
        }

        title = ChatColor.translateAlternateColorCodes('&', title);

        if (debugModeEnabled){
            System.out.println(player.getName() + " has opened the public stash with the title: " + title);
        }



        Inventory publicStashInventory = Bukkit.createInventory(player, inventorySize, title);


        ItemStack stashItem = new ItemStack(Material.getMaterial(stashItemType));
        ItemMeta stashItemMeta = stashItem.getItemMeta();



        String stashTitle = translationYml.getString(language.toLowerCase() + ".stash-name" );
        if (stashTitle == null){
            stashTitle = translationYml.getString("default.stash-name");
        }

        stashTitle = ChatColor.translateAlternateColorCodes('&', stashTitle);


        if (debugModeEnabled){
            System.out.println("and with stash title of: " + stashTitle);
        }


        for (int i = 0; i < stashAmount; i++){

            stashItemMeta.setDisplayName(stashTitle + " " + (i + 1));
            stashItem.setItemMeta(stashItemMeta);

            publicStashInventory.setItem(i, stashItem);

        }


        player.openInventory(publicStashInventory);
        playersOpenedStash.put(player, publicStashInventory);



    }




    public void updateStashItem(int stashIndex, int itemIndex, ItemStack stashItem){

        for (Player player: playersOpenedStashIndex.keySet()){
            if (stashIndex == playersOpenedStashIndex.get(player)){
                player.getOpenInventory().getTopInventory().setItem(itemIndex, stashItem);
            }
        }




    }


    public void updateStashInventory(int stashIndex){

        for (Player player: playersOpenedStashIndex.keySet()){

            if (stashIndex == playersOpenedStashIndex.get(player)){

                Inventory stashInventory = dataManager.getYamlInventory(player, stashIndex);
                for (int itemIndex = 0; itemIndex < stashInventory.getSize(); itemIndex++){
                    player.getOpenInventory().getTopInventory().setItem(itemIndex, stashInventory.getItem(itemIndex));
                }

            }
        }

        inventoryUpdated = true;

        if (debugModeEnabled){
            System.out.println("Stash " + stashIndex + " was updated");
        }

    }

    @Override
    public void onDisable() {

    }

}
