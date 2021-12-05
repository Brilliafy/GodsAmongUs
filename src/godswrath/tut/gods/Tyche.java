package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Tyche implements God {
    public static final int itemsMultipliedMax = 3;
    public static final int discardItemPercentChance = 70;
    public static List<Player> TycheBlessedPlayerList = new ArrayList<Player>();
    public static List<Player> TycheDamnedPlayerList = new ArrayList<Player>();
    public static List<Player> QuestPlayerList = new ArrayList<Player>();
    public static int maxGoldAmount = 20;
    public static int maxReward = 20;
    public static int minReward = 1;
    public static int landmineChancePerthousand = 5;

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&k+&r&l&aTYCHE&r&2&k+&r &4&oBESTOWS BAD FORTUNE UPON YOU."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, Main.GodEventDurationTicks * 4, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, Main.GodEventDurationTicks * 4, 0));

                TycheDamnedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    TycheDamnedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7TYCHE HAS RESTORED YOUR FORTUNE, AND WENT TO RUIN SOME OTHER'S MORTAL FATE."));

                }, Main.GodEventDurationTicks * 2);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&k+&r&l&aTYCHE&r&2&k+&r &e&oBESTOWS GOOD FORTUNE UPON YOU."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Main.GodEventDurationTicks * 4, 0));
                TycheBlessedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    TycheBlessedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7TYCHE'S GOOD WILL IN YOU NEVER PAID OFF. TYCHE'S FORTUNE IS NO LONGER BESTOWED UPON YOU."));
                }, Main.GodEventDurationTicks * 2);
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&a&lTyche:&r&o&f Pssf- drop me some gold to gamble it");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.getInventory().addItem(new ItemStack(Material.DIAMOND,ThreadLocalRandom.current().nextInt(minReward,maxReward + 1))),
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK,Main.GodEventDurationTicks,4)),
                "&a&lTyche:&r&o&f Well played. &l&2You won",
                "&a&lTyche:&r&o&f Sorry mate, no refunds. &l&4You lost");
        QuestPlayerList.remove(player);
    }
}
