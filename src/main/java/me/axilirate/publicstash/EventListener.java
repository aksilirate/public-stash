package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    private final PublicStash publicStash;

    public EventListener(PublicStash publicStash) {
        this.publicStash = publicStash;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory openedInventory = player.getOpenInventory().getTopInventory();

        if (event.getClickedInventory() == null) {return;}

        if (event.getCurrentItem() == null) {return;}


        if (openedInventory.equals(publicStash.publicStashInventory)) {

            event.setCancelled(true);

            if (event.getClickedInventory().equals(publicStash.publicStashInventory)) {
                player.openInventory(publicStash.stashList.get(event.getSlot()));

            }

        }

        if (publicStash.stashList.contains(openedInventory)) {

            if (event.getCurrentItem().equals(Back.getItem())) {
                player.openInventory(publicStash.publicStashInventory);
            }

        }





    }


    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();

        for (int i = 0; i < publicStash.stashList.size(); i++) {


            for (int slotIndex = 0; slotIndex < publicStash.stashList.get(i).getSize(); slotIndex++) {

                if (publicStash.stashList.get(i).getItem(slotIndex) == null || publicStash.stashList.get(i).getItem(slotIndex).equals(itemStack)) {
                    publicStash.stashList.get(i).addItem(itemStack);
                    publicStash.saveAllStash();
                    return;
                }

            }

        }

    }





    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();


        if (publicStash.stashList.contains(event.getInventory())) {
            publicStash.saveAllStash();
        }

    }


}


