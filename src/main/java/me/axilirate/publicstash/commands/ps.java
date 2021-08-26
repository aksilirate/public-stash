package me.axilirate.publicstash.commands;

import me.axilirate.publicstash.PublicStash;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ps implements CommandExecutor {

    public PublicStash publicStash;

    public ps(PublicStash publicStash) {
        this.publicStash = publicStash;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args.length){

                case 0:

                    publicStash.openPublicStash(player);

            }

        }


        return true;
    }

}
