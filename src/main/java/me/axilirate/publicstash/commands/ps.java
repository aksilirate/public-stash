package me.axilirate.publicstash.commands;

import me.axilirate.publicstash.PublicStash;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    player.openInventory(publicStash.publicStashInventory);


            }

        }


        return true;
    }

}
