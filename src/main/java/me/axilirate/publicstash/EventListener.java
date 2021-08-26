package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class EventListener implements Listener {

    private final PublicStash publicStash;


    boolean itemsTaken = false;


    public EventListener(PublicStash publicStash) {
        this.publicStash = publicStash;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory openedInventory = player.getOpenInventory().getTopInventory();

        if (event.getClickedInventory() == null) {
            return;
        }


        if (publicStash.playersOpenedStashIndex.containsKey(player)) {

            if (itemsTaken){
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().equals(Back.getItem())) {
                    event.setCancelled(true);
                    publicStash.openPublicStash(player);
                    return;
                }
            }

            itemsTaken = true;
            Bukkit.getScheduler().runTaskLater(publicStash, () -> {

                if (!publicStash.playersOpenedStashIndex.containsKey(player)) {
                    return;
                }

                int stashIndex = publicStash.playersOpenedStashIndex.get(player);

                publicStash.dataManager.setYamlInventory(stashIndex, player.getOpenInventory().getTopInventory());

                Bukkit.getScheduler().runTaskLater(publicStash, () -> {itemsTaken = false;}, 20 );

            }, 1);




        }


        if (event.getCurrentItem() == null) {
            return;
        }

        if (!publicStash.playersOpenedStash.containsKey(player)) {
            return;
        }


        event.setCancelled(true);

        if (event.getClickedInventory().equals(openedInventory)) {
            Inventory stashInventory = publicStash.dataManager.getYamlInventory(player, event.getSlot());


            player.openInventory(stashInventory);
            publicStash.playersOpenedStashIndex.put(player, event.getSlot());


        }


    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (publicStash.playersOpenedStashIndex.containsKey(player)) {

            if (itemsTaken){
                event.setCancelled(true);
                return;
            }

            itemsTaken = true;
            Bukkit.getScheduler().runTaskLater(publicStash, () -> {

                if (!publicStash.playersOpenedStashIndex.containsKey(player)) {
                    return;
                }

                int stashIndex = publicStash.playersOpenedStashIndex.get(player);

                publicStash.dataManager.setYamlInventory(stashIndex, player.getOpenInventory().getTopInventory());

                Bukkit.getScheduler().runTaskLater(publicStash, () -> {itemsTaken = false;}, 20 );

            }, 1);





        }

    }


    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {

        if (!publicStash.despawnedItemsToStash) {
            return;
        }


        ItemStack itemStack = event.getEntity().getItemStack();

        for (int stashIndex = 0; stashIndex < publicStash.stashAmount; stashIndex++) {
            Inventory stashInventory = publicStash.dataManager.getYamlInventory(null, stashIndex);

            for (int itemIndex = 0; itemIndex < 54; itemIndex++) {
                if (stashInventory.getItem(itemIndex) == null) {
                    continue;
                }

                if (stashInventory.getItem(itemIndex).getType().equals(itemStack.getType()) && stashInventory.getItem(itemIndex).getAmount() + itemStack.getAmount() > 64) {
                    continue;
                }

                stashInventory.addItem(itemStack);
                publicStash.dataManager.setYamlItemStack(stashIndex, itemIndex, itemStack);
                return;

            }

        }

    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();


        if (publicStash.playersOpenedStash.containsKey(player)) {
            publicStash.playersOpenedStash.remove(player);
        }


        if (publicStash.playersOpenedStashIndex.containsKey(player)) {
            publicStash.playersOpenedStashIndex.remove(player);

            player.setItemOnCursor(null);
        }


    }


}


