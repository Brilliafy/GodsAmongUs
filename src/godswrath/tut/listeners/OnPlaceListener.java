package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Zeus;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnPlaceListener implements Listener {
    public OnPlaceListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnPlace(BlockPlaceEvent e) {
        if(!e.isCancelled()) {
            if(Zeus.QuestPlayerList.contains(e.getPlayer()) && e.getBlock().getLocation().getY() > 80 && e.getBlock().getType() == Material.LIGHTNING_ROD) {
                new Zeus().invokeQuest(e.getPlayer(),true);
            }
        }
    }
}
