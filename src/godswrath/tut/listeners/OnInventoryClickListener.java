package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Hephaestus;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class OnInventoryClickListener implements Listener {

    public OnInventoryClickListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemRepairEvent(InventoryClickEvent e) {
        if(!e.isCancelled()){
            HumanEntity ent = e.getWhoClicked();

// not really necessary
            if(ent instanceof Player){
                Player player = (Player)ent;
                Inventory inv = e.getInventory();

// see if we are talking about an anvil here
                if(inv instanceof AnvilInventory){
                    AnvilInventory anvil = (AnvilInventory)inv;
                    InventoryView view = e.getView();
                    int rawSlot = e.getRawSlot();

// compare raw slot to the inventory view to make sure we are in the upper inventory
                    if(rawSlot == view.convertSlot(rawSlot)){
                        // 2 = result slot
                        if(rawSlot == 2){
                            // all three items in the anvil inventory
                            ItemStack[] items = anvil.getContents();

// item in the left slot
                            ItemStack item1 = items[0];

// item in the right slot
                            ItemStack item2 = items[1];

// I do not know if this is necessary
                            if(item1 != null && item2 != null){
                                Material mat1 = item1.getType();
                                Material mat2 = item2.getType();

// if the player is repairing something the ids will be the same
                                if(mat1 == mat2){
                                    // item in the result slot
                                    ItemStack item3 = e.getCurrentItem();

// check if there is an item in the result slot
                                    if(item3 != null){
                                        ItemMeta meta = item3.getItemMeta();

// meta data could be null
                                        if(meta != null){
                                            // get the repairable interface to obtain the repair cost
                                            if(meta instanceof Repairable){
                                                Repairable repairable = (Repairable)meta;
                                                int repairCost = repairable.getRepairCost();

// can the player afford to repair the item
                                                if(player.getLevel() >= repairCost){
                                                    new Hephaestus().invokeQuest(player,true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
