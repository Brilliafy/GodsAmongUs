package godswrath.tut.listeners;

import godswrath.tut.gods.Ares;
import godswrath.tut.Main;
import godswrath.tut.gods.Demeter;
import godswrath.tut.gods.Hades;
import godswrath.tut.gods.Nemesis;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class OnEntityDeathListener implements Listener {

    public OnEntityDeathListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnEntityDeath(EntityDeathEvent event) {
        for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
            if(Nemesis.QuestPlayerList.contains(onlinePlayer)) {
                if(event.getEntity() instanceof Mob) {
                    if(event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                        new Nemesis().invokeQuest(onlinePlayer, true);
                    } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                    {
                        if(event.getEntity().getLocation().clone().subtract(0, 1, 0).getBlock().getType() == Material.FIRE) {
                            new Nemesis().invokeQuest(onlinePlayer, true);
                        }
                    }
                }
            }
        }

        if(event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();

            if (Ares.AresBlessedPlayerList.contains(player)) {
                double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                if (Ares.maxHealthCap > playerMaxHealth) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + ThreadLocalRandom.current().nextDouble(Ares.minimumHealthIncrease, Ares.maxHealthIncrease));
                }
            }

            if(event.getEntity() instanceof Animals) {
                if (Demeter.DemeterBlessedPlayerList.contains(player)) {
                    for (ItemStack item : event.getDrops()) {
                        item.setAmount(item.getAmount() * ThreadLocalRandom.current().nextInt(Demeter.minDropMultiplier, Demeter.maxDropMultiplier + 1));
                    }
                } else if (Demeter.DemeterDamnedPlayerList.contains(player)) {
                    event.getDrops().removeIf(item -> ThreadLocalRandom.current().nextDouble(0, 100) <= Demeter.discardItemChancePercent);
                }
            }

            if(Ares.QuestPlayerList.contains(player)) {
                if (event.getEntity() instanceof IronGolem) {
                    new Ares().invokeQuest(player,true);
                }
            }

            if(Hades.QuestPlayerList.contains(player)) {
                if(event.getEntity() instanceof Phantom daemon && daemon.getSize() == Hades.ladSize) {
                    new Hades().invokeQuest(player,true);
                }
            }
        }
    }
}
