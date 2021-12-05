package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Demeter implements God {
    public static List<Player> DemeterBlessedPlayerList = new ArrayList<Player>();
    public static List<Player> DemeterDamnedPlayerList = new ArrayList<Player>();
    public static int maxDropMultiplier = 4;
    public static int minDropMultiplier = 1;
    public static int discardItemChancePercent = 75;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();
    public static int minRewardAmount = 12;
    public static int maxRewardAmount = 32;
    public static float bootsDurabilityMultiplierDamager = 0.7f;

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.playSound(player.getLocation(), Sound.ENTITY_BEE_DEATH, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&a&lDemeter&r&c&k+&r&o&4 has damned all of your crops and animals."));
                player.setFoodLevel(0);
                DemeterDamnedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    DemeterDamnedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&a&lDemeter&r&c&k+&r&o&4 was told off by the other Gods, so he had to restore your fate."));

                }, Main.GodEventDurationTicks * 2);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_AZALEA_LEAVES_BREAK, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&a&lDemeter&r&c&k+&r&o&f has sanctified your crops and animals."));
                player.setFoodLevel(20);
                DemeterBlessedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    DemeterBlessedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&a&lDemeter&r&c&k+&r&o&4 realised he made a mistake, and took back his blessings."));
                }, Main.GodEventDurationTicks * 2);
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&oHarvest a full-grown crop in honour of &r&l&2Demeter");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.getInventory().addItem(new ItemStack(Material.BONE_MEAL, ThreadLocalRandom.current().nextInt(minRewardAmount, maxRewardAmount + 1))),
                () -> mudBoots(player),
                "&a&2Demeter&r&o&f has awarded you with dung as fertilizer",
                "&o&fYou hear your boots fracture after &r&a&2Demeter&r&o&f (accidentally) pushed you down in mud");
        QuestPlayerList.remove(player);
    }

    public static void mudBoots(Player player) {
        if (player.getInventory().getBoots() != null) {
            ItemStack boots = player.getInventory().getBoots();
            ItemMeta bootsMeta = boots.getItemMeta();
            bootsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l&oMudded boots"));
            boots.setItemMeta(bootsMeta);

            int calculatedDamage = Math.round(Hephaestus.getItemMaxDurability(boots) * bootsDurabilityMultiplierDamager);
            Hephaestus.setItemDamage(boots,Hephaestus.getItemDamage(boots) + calculatedDamage);
            player.playSound(player.getLocation(),Sound.ITEM_SHIELD_BREAK,500f,1f);
        }
    }
}
