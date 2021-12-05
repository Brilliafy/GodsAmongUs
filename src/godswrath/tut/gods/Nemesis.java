package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.TNT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Nemesis implements God {

    public static double invokeChancePercent = 0.8;
    public static double passiveMobInvokeMultiplier = 20;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();


    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                InvokeBadSide(player);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lNEMESIS&r&o&e has balanced your miserable life's integrity."));
                player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_CELEBRATE, 500.0f, 1.0f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&cBURN AN ENEMY INSIDE THE FLAMES FOR&l&0 NEMESISs");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> giveDagger(player),
                () -> nukePlayer(player),
                "&0&lNEMESIS&r&f&o forged you a special dagger",
                "&0&lNEMESIS&r&4&o DROPPED YOU A PACK OF C4");
        QuestPlayerList.remove(player);
    }


    public void InvokeBadSide(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lNEMESIS&r&o&c has balanced the scales by righting your injustices."));
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 500.0f, 1.0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0));
    }

    public static void giveDagger(Player player) {
        ItemStack dagger = new ItemStack(Material.IRON_SWORD);
        ItemMeta daggerMeta = dagger.getItemMeta();
        daggerMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&fBackstabbing Dagger of &4&lBetrayal"));
        daggerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        daggerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        daggerMeta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        daggerMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        daggerMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),0.065, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),2000, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),4.5, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),-8, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),-6, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),-1, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),-0.5, AttributeModifier.Operation.ADD_NUMBER));
        daggerMeta.addAttributeModifier(Attribute.GENERIC_FOLLOW_RANGE, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),100, AttributeModifier.Operation.ADD_NUMBER));
        dagger.setItemMeta(daggerMeta);
        player.getInventory().addItem(dagger);
    }

    public void nukePlayer(Player player) {
        getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            Location targetLocation = player.getLocation();
            player.getWorld().spawn(player.getTargetBlock(null, 50).getLocation().add(0, 1, 0), TNTPrimed.class).setFuseTicks(60);
            player.getWorld().createExplosion(targetLocation,0,false,false);
            player.setVelocity(new Vector(0,200f,0));
            player.setFallDistance(-190f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,120,4));
            getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                player.getWorld().createExplosion(targetLocation,80,true,true);
            }, 60);
        }, 60);
    }
}
