package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Hephaestus;
import godswrath.tut.gods.Neptune;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class OnProjectileListener implements Listener {

    public OnProjectileListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnProjectile(ProjectileHitEvent e) {
        if(!e.isCancelled())
        {
            if(e.getEntity().getShooter() instanceof Player player)
            {
                if(Neptune.QuestPlayerList.contains(player) && e.getEntity() instanceof Trident tridentProjectile) {
                    if(Hephaestus.getItemDamage(tridentProjectile.getItem()) == 248 + 1) {
                        //&& tridentProjectile.getItem().getEnchantments().size() == 0
                        tridentProjectile.remove();
                    }
                    new Neptune().invokeQuest(player,true);
                }
            }
        }
    }
}
