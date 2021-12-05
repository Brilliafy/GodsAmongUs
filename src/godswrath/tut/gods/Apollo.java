package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Apollo implements God {
    public static List<Player> QuestPlayerList = new ArrayList<Player>();

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&k+&r&a&lApollo&r&2&k+&r&7&o has intoxicated you."));
                intoxicatePlayer(player);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&k+&r&a&lApollo&r&2&k+&r&2&o has gifted you Nectar."));
                awardNectar(player);
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&oConsume suspicious stew in behalf of&r &2&k+&r&a&lApollo&r&2&k+");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
       Main.invokeQuest(player, Completed,
               () -> awardNectar(player),
               () -> intoxicatePlayer(player),
               "&2&k+&r&a&lApollo&r&2&k+&r&e&o has awarded you with Nectar",
               "&2&k+&r&a&lApollo&r&2&k+&r&c&o intoxicated you");
       QuestPlayerList.remove(player);
    }

    public void awardNectar(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Main.GodEventDurationTicks, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Main.GodEventDurationTicks, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Main.GodEventDurationTicks, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.GodEventDurationTicks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Main.GodEventDurationTicks, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GodEventDurationTicks, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Main.GodEventDurationTicks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Main.GodEventDurationTicks, 2));
    }

    public void intoxicatePlayer(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Main.GodEventDurationTicks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Main.GodEventDurationTicks, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Main.GodEventDurationTicks, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Main.GodEventDurationTicks, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.GodEventDurationTicks, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Main.GodEventDurationTicks, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Main.GodEventDurationTicks, 0));
    }

}
