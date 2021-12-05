package godswrath.tut.listeners;

import godswrath.tut.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import godswrath.tut.gods.Atropos;


public class OnAnimalsBreed implements Listener {
    public OnAnimalsBreed(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnBreed(EntityBreedEvent event) {
        if(event.getBreeder() instanceof Player player) {
            if(Atropos.QuestPlayerList.contains(player)) {
                new Atropos().invokeQuest(player,true);
            }
        }
    }
}
