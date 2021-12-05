package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Ares implements God {
    public static List<Player> AresBlessedPlayerList = new ArrayList<Player>();
    public static double maxHealthCap = 40;
    public static double minimumHealthIncrease = 0;
    public static double maxHealthIncrease = 0.5;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();


    @Override
    public void Invoke(Player player) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&4&lARÉS&r&c&k+&r&o&4 HAS DECLARED WAR ON THY LAND."));
            player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 500.0f, 1.0f);
            player.teleport(new Location(Bukkit.getWorlds().get(0), player.getLocation().getX(), player.getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getZ()));

            spawnTank(new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 5,180,0));
            spawnGladiator(new Location(player.getWorld(),player.getLocation().getX() + 3, player.getLocation().getY(), player.getLocation().getZ() - 3,180,0));
            spawnGladiator(new Location(player.getWorld(),player.getLocation().getX() - 3, player.getLocation().getY(), player.getLocation().getZ() - 3,180,0));
            spawnArcher(new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 8,0,0));
            spawnArcher(new Location(player.getWorld(),player.getLocation().getX() + 3, player.getLocation().getY(), player.getLocation().getZ() + 8,0,0));
            spawnArcher(new Location(player.getWorld(),player.getLocation().getX() - 3, player.getLocation().getY(), player.getLocation().getZ() + 8,0,0));

                for(int arrowSpawnDelay = 5; arrowSpawnDelay <= Main.GodEventDurationTicks; arrowSpawnDelay += 5) {
                    Main.getPlugin(Main.class).getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                        Location randomizedLocation = new Location(player.getWorld(),player.getLocation().getX() + ThreadLocalRandom.current().nextInt(-1,1 + 1), player.getLocation().getY() + 8, player.getLocation().getZ() + ThreadLocalRandom.current().nextInt(-1,1 + 1),0,90);
                        player.getWorld().spawnArrow(randomizedLocation,new Vector(0,-90,0),2f,0f);
                    }, arrowSpawnDelay);
                }

        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&4&lARÉS&r&c&k+&r&o&6 DEMANDS BLOOD."));
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 500.0f, 1.0f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GodEventDurationTicks, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Main.GodEventDurationTicks, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.GodEventDurationTicks, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Main.GodEventDurationTicks, 2));
            AresBlessedPlayerList.add(player);
            getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                AresBlessedPlayerList.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7ARÉS IS DISPLEASED WITH THE MASSACRE YOU CAUSED, AND HIRED A NEW SLAYER."));
            }, Main.GodEventDurationTicks);
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&oDefeat an Iron Golem in behalf of&r &4&lAres&r&o&7");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Main.GodEventDurationTicks, 2)),
                () -> spawnTank(player.getLocation().add(2,1,2)),
                "&4&lAres&r&o&7&r&o has granted you almighty power",
                "&4&lAres&r&o&7&r&o has requested for your head");
        QuestPlayerList.remove(player);
    }


    public void spawnGladiator(Location location) {
            Skeleton skeleton = (Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON);

            ItemStack boots = new ItemStack(Material.IRON_BOOTS);
            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
            ItemStack iteminhand = new ItemStack(Material.IRON_SWORD);

            skeleton.getEquipment().setBoots(boots);
            skeleton.getEquipment().setChestplate(chestplate);
            skeleton.getEquipment().setLeggings(legs);
            skeleton.getEquipment().setItemInHand(iteminhand);

            skeleton.setMaxHealth(50);
            skeleton.setHealth(50);
            skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(16);

            skeleton.setCustomName(ChatColor.translateAlternateColorCodes('&',"&4GLADIATOR&r&o&0 OF THE DREAD"));
            skeleton.setCustomNameVisible(true);

            skeleton.getEquipment().setBootsDropChance(0);
            skeleton.getEquipment().setHelmetDropChance(0);
            skeleton.getEquipment().setChestplateDropChance(0);
            skeleton.getEquipment().setLeggingsDropChance(0);
            skeleton.getEquipment().setItemInHandDropChance(0);

        }

    public void spawnArcher(Location location) {
        Pillager pillager = (Pillager)location.getWorld().spawnEntity(location, EntityType.PILLAGER);

        ItemStack iteminhand = new ItemStack(Material.CROSSBOW);

        ItemMeta meta = iteminhand.getItemMeta();
        meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true);
        meta.addEnchant(Enchantment.PIERCING, 4, true);
        iteminhand.setItemMeta(meta);

        pillager.getEquipment().setItemInHand(iteminhand);

        pillager.setMaxHealth(40);
        pillager.setHealth(40);
        pillager.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
        pillager.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(8);

        pillager.setCustomName(ChatColor.translateAlternateColorCodes('&',"&4ARCHER&r&o&0 OF THE DREAD"));
        pillager.setCustomNameVisible(true);

        pillager.getEquipment().setItemInHandDropChance(0);

    }

    public void spawnTank(Location location) {
        WitherSkeleton skeleton = (WitherSkeleton)location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        Ravager ravager = (Ravager)location.getWorld().spawnEntity(location, EntityType.RAVAGER);

        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack iteminhand = new ItemStack(Material.IRON_AXE);

        skeleton.getEquipment().setBoots(boots);
        skeleton.getEquipment().setChestplate(chestplate);
        skeleton.getEquipment().setLeggings(legs);
        skeleton.getEquipment().setItemInHand(iteminhand);

        skeleton.setMaxHealth(65);
        skeleton.setHealth(65);
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(18);
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);

        skeleton.setCustomName(ChatColor.translateAlternateColorCodes('&',"&4TANK&r&o&0 OF THE DREAD"));
        skeleton.setCustomNameVisible(true);

        ravager.setCustomName(ChatColor.translateAlternateColorCodes('&',"&4BEAST&r&o&0 OF THE DREAD"));
        ravager.setCustomNameVisible(true);

        skeleton.getEquipment().setBootsDropChance(0);
        skeleton.getEquipment().setHelmetDropChance(0);
        skeleton.getEquipment().setChestplateDropChance(0);
        skeleton.getEquipment().setLeggingsDropChance(0);
        skeleton.getEquipment().setItemInHandDropChance(0);

        ravager.setPassenger(skeleton);
    }

}
