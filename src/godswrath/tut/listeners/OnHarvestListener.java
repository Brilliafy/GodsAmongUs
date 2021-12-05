package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Demeter;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

public class OnHarvestListener implements Listener {


    public OnHarvestListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnHarvest(PlayerHarvestBlockEvent event) {
        if(!event.isCancelled() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {

            if(Demeter.DemeterBlessedPlayerList.contains(event.getPlayer())) {
                for(ItemStack item : event.getItemsHarvested())
                {
                    item.setAmount(item.getAmount() * ThreadLocalRandom.current().nextInt(Demeter.minDropMultiplier,Demeter.maxDropMultiplier + 1));
                }
            } else if(Demeter.DemeterDamnedPlayerList.contains(event.getPlayer())) {
                event.getItemsHarvested().removeIf(item -> ThreadLocalRandom.current().nextDouble(0, 100) <= Demeter.discardItemChancePercent);
            }
            if(Demeter.QuestPlayerList.contains(event.getPlayer())) {
                new Demeter().invokeQuest(event.getPlayer(),true);
            }

        }
    }
}
