package godswrath.tut.listeners;

import godswrath.tut.gods.*;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import static org.bukkit.Bukkit.getServer;

public class OnAttackListener implements Listener
{
    public OnAttackListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {

        if(!event.isCancelled() && event.getDamager() instanceof Player player) {

                LivingEntity damageReceiver = (LivingEntity) event.getEntity();
            if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {


                if (Hades.HadesBlessedPlayerList.contains(((Player) event.getDamager()))) {  //has player Hares' blessing?
                    player.setHealth(Clamp(player.getHealth() + Hades.drainDuration * 2, 0, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())); //vampire heal player
                    damageReceiver.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 4));

                    int appendedDuration = Hades.drainDuration; //set or append health drain of enemy
                    if (damageReceiver.hasPotionEffect(PotionEffectType.WITHER)) {
                        appendedDuration = damageReceiver.getPotionEffect(PotionEffectType.WITHER).getDuration() + Hades.drainDuration;
                    }
                    damageReceiver.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, appendedDuration, 4));
                } else if (Hades.HadesDamnedPlayerList.contains(((Player) event.getDamager()))) { //is player damned
                    event.setDamage(0);
                    damageReceiver.setHealth(Clamp(damageReceiver.getHealth() + Math.round(Hades.drainDuration / 20) * 2, 0, damageReceiver.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 4));

                    int appendedDuration = Hades.drainDuration; //set or append health drain of enemy
                    if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                        appendedDuration = player.getPotionEffect(PotionEffectType.WITHER).getDuration() + Hades.drainDuration;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, appendedDuration, 4));
                }

                if (Hephaestus.HephaestusBlessedPlayerList.contains(((Player) event.getDamager()))) {
                    damageReceiver.setVisualFire(true);
                    damageReceiver.setMaximumNoDamageTicks(2);
                    damageReceiver.setNoDamageTicks(2);
                    for (int i = 1; i <= Math.round(Hephaestus.FlameAspectDurationTicks / 2f); i++) {

                        getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
//                            Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(damageReceiver, damageReceiver, EntityDamageEvent.DamageCause.FIRE_TICK, 1));
                            damageReceiver.damage(1);
                        }, i * 2);
                    }

                    getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                        damageReceiver.setVisualFire(false);
                        damageReceiver.setMaximumNoDamageTicks(20);
                        damageReceiver.setNoDamageTicks(20);
                    }, 40);
                }

                if (damageReceiver instanceof Animals) {
                    if (Nemesis.invokeChancePercent * Nemesis.passiveMobInvokeMultiplier >= ThreadLocalRandom.current().nextDouble(0, 100)) {
                        new Nemesis().InvokeBadSide(player);
                    }
                } else {
                    if (Nemesis.invokeChancePercent >= ThreadLocalRandom.current().nextDouble(0, 100)) {
                        new Nemesis().Invoke(player);
                    }
                }
            }
        } else if (!event.isCancelled() && event.getEntity() instanceof Player player && player.getHealth() <= Atropos.minimumHealthToInvoke && Atropos.interferancePercentChance >= ThreadLocalRandom.current().nextInt(0,100 + 1)) {
            new Atropos().Invoke(player);
        }
    }

    public static double Clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

}
