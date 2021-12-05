package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Tyche;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnPlayerDropListener implements Listener {

    public OnPlayerDropListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnPlayerDropEvent(PlayerDropItemEvent e){
        if(!e.isCancelled()) {
            ItemStack droppedItem = e.getItemDrop().getItemStack();
            if(Tyche.QuestPlayerList.contains(e.getPlayer()) && droppedItem.getType() == Material.GOLD_INGOT) {
                new Tyche().invokeQuest(e.getPlayer(), droppedItem.getAmount() * (100D / Tyche.maxGoldAmount) > ThreadLocalRandom.current().nextDouble(1, 100));
                if(droppedItem.getAmount() > Tyche.maxGoldAmount) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT,droppedItem.getAmount() - Tyche.maxGoldAmount));
                }
                e.getItemDrop().remove();
            }
        }
    }
}
