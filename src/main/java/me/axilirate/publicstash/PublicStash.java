package me.axilirate.publicstash;

import me.axilirate.publicstash.commands.ps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class PublicStash extends JavaPlugin {

    public DataManager dataManager = new DataManager(this);

    public HashMap<Player,Inventory> playersOpenedStash = new HashMap<>();

    public HashMap<Player,Integer> playersOpenedStashIndex = new HashMap<>();


    public int inventorySize = 9;
    public int stashAmount;
    public boolean despawnedItemsToStash = true;
    public String stashItemType = "CHEST";


    File translationFile = new File(this.getDataFolder() + "/language.yml");
    YamlConfiguration translationYml = YamlConfiguration.loadConfiguration(translationFile);


    @Override
    public void onEnable() {

        FileConfiguration config = this.getConfig();

        config.addDefault("stash-amount", 9);
        config.addDefault("despawned-items-to-stash", true);
        config.addDefault("stash-item-type", "CHEST");


        config.options().copyDefaults(true);
        saveConfig();



        stashAmount = config.getInt("stash-amount");
        despawnedItemsToStash = config.getBoolean("despawned-items-to-stash");
        stashItemType = config.getString("stash-item-type");


        if (stashAmount != 0){
            inventorySize = (int) Math.ceil( (float) stashAmount / 9) * 9;
        }


        if (translationYml.get("default.public-stash-title") == null){
            translationYml.set("default.public-stash-title", "Public Stash");
        }

        if (translationYml.get("en_us.public-stash-title") == null){
            translationYml.set("en_us.public-stash-title", "Public Stash");
        }



        if (translationYml.get("default.stash-name") == null){
            translationYml.set("default.stash-name", "Stash");
        }


        if (translationYml.get("en_us.stash-name") == null){
            translationYml.set("en_us.stash-name", "Stash");
        }



        dataManager.saveYamlFile(translationFile, translationYml);



        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("ps").setExecutor(new ps(this));




    }






    public void openPublicStash(Player player){

        String language = player.getLocale();

        String title = translationYml.getString(language + ".public-stash-title" );

        if (title == null){
            title = translationYml.getString("default.public-stash-title");
        }


        Inventory publicStashInventory = Bukkit.createInventory(player, inventorySize, title);


        ItemStack stashItem = new ItemStack(Material.getMaterial(stashItemType));
        ItemMeta stashItemMeta = stashItem.getItemMeta();


        String stashTitle = translationYml.getString(language + ".stash-name" );

        if (stashTitle == null){
            stashTitle = translationYml.getString("default.stash-name");
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
    }

    @Override
    public void onDisable() {

    }

}
