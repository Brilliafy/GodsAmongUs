package godswrath.tut.listeners;

import godswrath.tut.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeathListener implements Listener {
    public OnPlayerDeathListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnEntityDeath(PlayerDeathEvent event) {
        event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
    }
}
