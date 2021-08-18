package me.axilirate.publicstash;

import me.axilirate.publicstash.commands.ps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PublicStash extends JavaPlugin {

    public DataManager dataManager = new DataManager(this);

    public Inventory publicStashInventory = Bukkit.createInventory(null, 9, "Public Stash");

    public List<Inventory> stashList = new ArrayList<>();



    @Override
    public void onEnable() {


        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("ps").setExecutor(new ps(this));




        for (int i = 0; i < 9; i++){

            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta chestMeta = chest.getItemMeta();
            chestMeta.setDisplayName("Stash " + (i + 1));
            chest.setItemMeta(chestMeta);


            publicStashInventory.setItem(i, chest);
            stashList.add(dataManager.getYamlInventory(i));


        }



    }




    @Override
    public void onDisable() {

        for (int i = 0; i < stashList.size(); i++){
            dataManager.setYamlInventory(i, stashList.get(i));
        }


    }
}
