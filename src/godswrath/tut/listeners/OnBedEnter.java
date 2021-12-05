package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Erebus;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class OnBedEnter implements Listener {
    public OnBedEnter(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if(Erebus.ErebusCursedPlayerList.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oYou are too afraid to sleep, since &r&8&lEpiales&r&7&o left you traumatised."));
            event.getPlayer().leaveVehicle();
            event.setUseBed(Event.Result.DENY);
            event.setCancelled(true);

        } else if (ThreadLocalRandom.current().nextDouble(0, 100) <= Erebus.invokeOnBedEnterChancePercent) {
            Erebus.invokeInSleep(event.getPlayer());
            event.getPlayer().leaveVehicle();
            event.setUseBed(Event.Result.DENY);
            event.setCancelled(true);
        }
    }
}
