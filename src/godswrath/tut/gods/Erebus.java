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

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;


public class Erebus implements God {
    public static double invokeOnBedEnterChancePercent = 10;
    public static int maxSpawnDemonCount = 12;
    public static int minSpawnDemonCount = 2;
    public static int maxSpawnDistanceFromPlayer = 20;
    public static List<Player> ErebusCursedPlayerList = new ArrayList<Player>();
    public static List<Player> QuestPlayerList = new ArrayList<Player>();

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&lErebus&r&o&7 has brought&r &l&8Darkness&r&o&7 upon thy land."));
                long time = Bukkit.getWorlds().get(0).getTime();
                Bukkit.getWorlds().get(0).setTime(14000);
                player.playSound(player.getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 500.0f, 1.0f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.GodEventDurationTicks, 0));
                player.teleport(new Location(Bukkit.getWorlds().get(0),player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ()));

                for(int i = ThreadLocalRandom.current().nextInt(minSpawnDemonCount,maxSpawnDemonCount + 1); i > 0; i--) {
                    double X = ThreadLocalRandom.current().nextDouble(player.getLocation().getX(),player.getLocation().getX() + maxSpawnDistanceFromPlayer);
                    double Z = ThreadLocalRandom.current().nextDouble(player.getLocation().getZ(),player.getLocation().getZ() + maxSpawnDistanceFromPlayer);
                    double Y = player.getWorld().getHighestBlockYAt((int)X,(int)Z);
                    float yaw = (float)ThreadLocalRandom.current().nextDouble(-180,180 + 1);
                    float pitch = (float)ThreadLocalRandom.current().nextDouble(-90,90 + 1);

                    Location spawnLocation = new Location(player.getWorld(),X,Y,Z,yaw,pitch);
                    spawnDemon(spawnLocation);
                }
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    Bukkit.getWorlds().get(0).setTime(time);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&8The&r&l&0 Darkness&r&o&8 is fading away."));
                }, Main.GodEventDurationTicks);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&lErebus&r&o&7 blinded all of thine enemies"));
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 500.0f, 1.0f);
               for(LivingEntity entity : player.getWorld().getLivingEntities()) {
                   if(entity instanceof Player possiblePlayer && possiblePlayer != player) {
                       entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.GodEventDurationTicks, 0));
                   }
               }
            }
        }
    }



    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&oEnter an area with no light to please&r &0&lErebus&r&o&7");
        getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            QuestPlayerList.add(player);
        }, 120);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Main.GodEventDurationTicks, 0)),
                () -> invokeInSleep(player),
                "&0&lErebus&r&o&7&r&e&o has granted you the Vision of the Night",
                "&0&lErebus&r&o&7&r&c&o surrounded you with Darkness");
        QuestPlayerList.remove(player);
    }

    public static void invokeInSleep(Player player)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&0EREBUS&r&o&0 AGREED WITH EPIALES AND HYPNOS TO BRING TERRORS IN YOUR SLEEP."));
        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 500.0f, 1.0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.GodEventDurationTicks, 0));
        for(int i = ThreadLocalRandom.current().nextInt(Hades.minDeamonsCount,Hades.maxDeamonsCount + 1); 0 < i;i--) {
            Hades.spawnTerror(player.getLocation());
            spawnDemon(player.getLocation());
        }

        ErebusCursedPlayerList.add(player);
        getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            ErebusCursedPlayerList.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&8EPIALES&r&o&7 finally retires from terrorising your soul."));
        }, Main.GodEventDurationTicks);
    }

    public static void spawnDemon(Location location) {
        WitherSkeleton skeleton = (WitherSkeleton)location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack helmet = new ItemStack(Material.ENDER_EYE);
        ItemStack iteminhand = new ItemStack(Material.NETHERITE_SWORD);

        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&l&o&4&nEye of Cathulu"));
        helmet.setItemMeta(helmetMeta);

        LeatherArmorMeta bootsMeta = (LeatherArmorMeta)boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(0,0,0));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true);
        boots.setItemMeta(bootsMeta);

        LeatherArmorMeta chesplateMeta = (LeatherArmorMeta)boots.getItemMeta();
        chesplateMeta.setColor(Color.fromRGB(0,0,0));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true);
        chestplate.setItemMeta(chesplateMeta);

        LeatherArmorMeta legsMeta = (LeatherArmorMeta)boots.getItemMeta();
        legsMeta.setColor(Color.fromRGB(0,0,0));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true);
        legs.setItemMeta(legsMeta);

        skeleton.getEquipment().setBoots(boots);
        skeleton.getEquipment().setHelmet(helmet);
        skeleton.getEquipment().setChestplate(chestplate);
        skeleton.getEquipment().setLeggings(legs);
        skeleton.getEquipment().setItemInMainHand(iteminhand);


        skeleton.getEquipment().setBootsDropChance(0);
        skeleton.getEquipment().setHelmetDropChance(100);
        skeleton.getEquipment().setChestplateDropChance(0);
        skeleton.getEquipment().setLeggingsDropChance(0);
        skeleton.getEquipment().setItemInHandDropChance(0);

        skeleton.setMaxHealth(70);
        skeleton.setHealth(70);
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(18);

        skeleton.setCustomName(ChatColor.translateAlternateColorCodes('&',"&l&0TERROR&r&o&8 of the&r&0&l NIGHT"));
        skeleton.setCustomNameVisible(true);
    }

}
