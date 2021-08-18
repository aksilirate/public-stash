package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class EventListener implements Listener {

    private final PublicStash publicStash;

    public EventListener(PublicStash publicStash) {
        this.publicStash = publicStash;
    }





    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory openedInventory = player.getOpenInventory().getTopInventory();

        if (event.getClickedInventory() != null) {
            if (openedInventory.equals(publicStash.publicStashInventory)){

                event.setCancelled(true);

                if (event.getClickedInventory().equals(publicStash.publicStashInventory)){
                    player.openInventory(publicStash.stashList.get(event.getSlot()));

                }

            }

            if (publicStash.stashList.contains(openedInventory)){

                if (event.getCurrentItem().equals(Back.getItem())){
                    player.openInventory(publicStash.publicStashInventory);
                }

            }



        }


    }


}
