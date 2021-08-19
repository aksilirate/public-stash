package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

public class DataManager {


    public PublicStash publicStash;

    public DataManager(PublicStash publicStash) {
        this.publicStash = publicStash;
    }



    public void setYamlInventory(int index, Inventory inventory){
        File file = new File(publicStash.getDataFolder() + "data.yml");
        YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);

        yaml_file.set(Integer.toString(index), inventory);

        saveYamlFile(file, yaml_file);
    }



    public Inventory getYamlInventory(int index){
        File file = new File(publicStash.getDataFolder() + "data.yml");
        YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);

        Inventory inventory = (Inventory) yaml_file.get(Integer.toString(index));



        if (inventory == null){
            inventory = Bukkit.createInventory(null, 54, "Stash " + (index + 1) );
            inventory.setItem(45, Back.getItem());
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
