package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Apollo;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class OnConsumeListener implements Listener {
    public OnConsumeListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnConsume(PlayerItemConsumeEvent event) {
        if(Apollo.QuestPlayerList.contains(event.getPlayer())) {
            if(event.getItem().getType() == Material.SUSPICIOUS_STEW) {
                new Apollo().invokeQuest(event.getPlayer(),true);
            }
        }
    }
}
