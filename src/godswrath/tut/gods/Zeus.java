package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;


public class Zeus implements God {
    public List<String> filteredEffects = new ArrayList<String>(Arrays.asList("CONFUSION","HEAL", "HARM", "HUNGER","POISON", "SLOW_DIGGING", "SLOW", "WEAKNESS", "WITHER", "BLINDNESS", "NAUSEA", "LEVITATION", "BAD_OMEN", "UNLUCK"));
    public static List<Player> QuestPlayerList = new ArrayList<Player>();
    public static int immortalityDuration = 120;

    
    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                Bukkit.getWorlds().get(0).setThunderDuration(Main.GodEventDurationTicks);
                Bukkit.getWorlds().get(0).setStorm(true);

                    player.getWorld().strikeLightning(player.getLocation());
                    player.setVelocity(new Vector(0, -10, 0));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&oYOU HAVE FELT THE WRATH OF &r&0&k+&r&l&eZEUS&r&0&k+"));
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 500.0f, 1.0f);
                   summonTargetLightingstorm(player);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&k+&r&l&eZEUS&r&3&k+&r &lis pleased."));
                Bukkit.getWorlds().get(0).setThundering(false);
                Bukkit.getWorlds().get(0).setStorm(false);

                for(PotionEffectType potion : PotionEffectType.values()) {
                    if(!filteredEffects.contains(potion.getName())) {
                        player.addPotionEffect(new PotionEffect(potion, Main.GodEventDurationTicks, 0));
                    }
                }
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&o&fPlace a lighting rod somewhere high to counteract &r&e&lZeus");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,immortalityDuration * 20, 4,true ,false)),
                () -> summonTargetLightingstorm(player),
                "&e&lZeus&r&o&f granted you temporary immortality for trying",
                "&e&lZeus&r&o&f feels condemned to have you alive");
        QuestPlayerList.remove(player);
    }

    public static void summonTargetLightingstorm(Player player)
    {
        getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            for (int lightingCount = ThreadLocalRandom.current().nextInt(1, Math.round((Main.GodEventDurationTicks / 20) / 5) + 1); lightingCount >= 1; lightingCount--) {
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class) , () -> {
                    player.getWorld().strikeLightning(player.getLocation());
                    player.setVelocity(new Vector(0, -10, 0));
                }, lightingCount * 100L);
            }
       }, 200);
    }
}
