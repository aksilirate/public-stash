package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {


    public PublicStash publicStash;

    public DataManager(PublicStash publicStash) {
        this.publicStash = publicStash;
    }



    public void setYamlInventory(int index, Inventory inventory){
        File file = new File(publicStash.getDataFolder() + "/data.yml");
        YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);


        yaml_file.set(Integer.toString(index), inventory.getStorageContents());


        saveYamlFile(file, yaml_file);
    }



    public Inventory getYamlInventory(int index){
        File file = new File(publicStash.getDataFolder() + "/data.yml");
        YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);

        ArrayList<ItemStack> itemStackArrayList = (ArrayList<ItemStack>) yaml_file.get(Integer.toString(index));

        Inventory inventory = Bukkit.createInventory(null, 54, "Stash " + (index + 1) );


        for(int i = 0; i < itemStackArrayList.size(); i++){
            inventory.setItem(i, itemStackArrayList.get(i));
        }


        inventory.setItem(45, Back.getItem());

        return inventory;

    }



    public void saveYamlFile(File file, YamlConfiguration yaml_file) {
        try {
            yaml_file.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
