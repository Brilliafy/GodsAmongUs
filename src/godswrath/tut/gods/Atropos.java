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

public class Atropos implements God {

    public static int interferancePercentChance = 2;
    public static int minimumHealthToInvoke = 4;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();

    @Override
    public void Invoke(Player player) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&k&8&k+&r&0&lANTROPOS&k&8&k+&r &o&l&0HAS EMBRACED YOUR TRUE FATE: &4DEATH."));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 500.0f, 1.0f);
            player.setHealth(0);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&k&8&k+&r&0&lANTROPOS&k&8&k+&r &o&l&7HAS PERMITTED YOU TO LIVE."));
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 500.0f, 1.0f);
            player.setHealth(player.getMaxHealth());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 4));
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&oMake life (breed two animals) in behalf of&r&k&8&k+&r&0&lANTROPOS&k&8&k+");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING)),
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 20, 1)),
                "&k&8&k+&r&0&lANTROPOS&k&8&k+&r&o has bestowed another life upon you",
                "&k&8&k+&r&0&lANTROPOS&k&8&k+&r&o has drained you in half");
        QuestPlayerList.remove(player);
    }
}
