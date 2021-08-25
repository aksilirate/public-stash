package me.axilirate.publicstash;

import me.axilirate.publicstash.commands.ps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PublicStash extends JavaPlugin {

    public DataManager dataManager = new DataManager(this);

    public Inventory publicStashInventory;

    public List<Inventory> stashList = new ArrayList<>();

    public int inventorySize = 9;
    public boolean despawnedItemsToStash = true;

    @Override
    public void onEnable() {

        FileConfiguration config = this.getConfig();


        int stashAmount = config.getInt("stash-amount");

        if (stashAmount != 0){
            inventorySize = (int) Math.ceil( (float) stashAmount / 9) * 9;
        }

        publicStashInventory = Bukkit.createInventory(null, inventorySize, "Public Stash");


        config.addDefault("stash-amount", 9);
        config.addDefault("despawned-items-to-stash", true);
        config.options().copyDefaults(true);
        saveConfig();




        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdirs()) {
                getLogger().info("Data dir was created.");
            }
        }


        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("ps").setExecutor(new ps(this));




        for (int i = 0; i < stashAmount; i++){

            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta chestMeta = chest.getItemMeta();
            chestMeta.setDisplayName("Stash " + (i + 1));
            chest.setItemMeta(chestMeta);


            publicStashInventory.setItem(i, chest);
            stashList.add(dataManager.getYamlInventory(i));


        }



    }


    public void saveAllStash(){
        for (int i = 0; i < stashList.size(); i++){
            dataManager.setYamlInventory(i, stashList.get(i));
        }
    }


    @Override
    public void onDisable() {

        saveAllStash();


    }
}
