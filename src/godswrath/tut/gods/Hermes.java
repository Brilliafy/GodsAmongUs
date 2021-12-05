package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Hermes implements God {
    public static float speedBoostMultiplier = 2.5f;
    public static int DepthStriderAddPercentChance = 50;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();
    public static float questVelocityRequirement = 50f;

    @Override
    public void Invoke(Player player) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&n&o&3_&r&l&n&bHERMES&r&n&o&3_&r&o&8&l ENJOYS WATCHING YOU FALL TO YOUR DOOM."));
            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 500.0f, 1.0f);
            if(!player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getWorld().getHighestBlockYAt(player.getLocation()),player.getLocation().getZ()));
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 33));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0));
//            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() / speedBoostMultiplier);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.GodEventDurationTicks, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Main.GodEventDurationTicks, 1));

            getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
//                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * speedBoostMultiplier);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HERMES FLIES INTO THE CLOUDS, WITH A GRIN IN HIS FACE."));
            }, Main.GodEventDurationTicks);

        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&n&o&3_&r&l&n&bHERMES&r&n&o&3_&r&o&f&l HAS GRANTED YOU GODLY HASTE."));
            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 500.0f, 1.0f);
//            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * speedBoostMultiplier);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.GodEventDurationTicks, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Main.GodEventDurationTicks, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Main.GodEventDurationTicks, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 20));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Main.GodEventDurationTicks, 1));

            if(player.getEquipment().getBoots() != null) {
                ItemMeta bootsMeta = player.getEquipment().getBoots().getItemMeta();

                bootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                player.getEquipment().getBoots().setItemMeta((bootsMeta));
            }

            getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
//                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() / speedBoostMultiplier);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HERMES' SPEED SLOWLY FADES AWAY…"));
            }, Main.GodEventDurationTicks);
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,String.format("&o&fReach a velocity of over %sm/s in honor of&r&l&6 Hermes",questVelocityRequirement));
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> giveRunnerAward(player),
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Main.GodEventDurationTicks,2)),
                "&l&6Hermes&r&o&f was delighted to give you an &eaward",
                "&o&fObesity caught up to you and you're slower");
        QuestPlayerList.remove(player);
    }

    public static void giveRunnerAward(Player player) {
        ItemStack award = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta awardMeta = award.getItemMeta();
        awardMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&n&e30m Sprint Gold Award"));
        awardMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&o&7We all know it's fake, but it helps you run faster")));
        awardMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        awardMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),0.085, AttributeModifier.Operation.ADD_NUMBER));
        award.setItemMeta(awardMeta);
        player.getInventory().addItem(award);
    }
}
