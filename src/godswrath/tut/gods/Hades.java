package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
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

public class Hades implements God {
    public static final int drainDuration = 60;
    public static int minDeamonsCount = 2;
    public static int maxDeamonsCount = 12;
    public static List<Player> HadesBlessedPlayerList = new ArrayList<>();
    public static List<Player> HadesDamnedPlayerList = new ArrayList<>();
    public static List<Player> QuestPlayerList = new ArrayList<Player>();
    public static int ladsCount = 5;
    public static int ladSize = 5;

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            int duration = ThreadLocalRandom.current().nextInt(Main.GodEventDurationTicks, (Main.GodEventDurationTicks * 2) + 1);

            if (ThreadLocalRandom.current().nextBoolean()) {
                player.playSound(player.getLocation(), Sound.ENTITY_DROWNED_AMBIENT, 500.0f, 1.0f);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&o&nHADES&r &0&oBESEECHES FOR YOUR SOUL."));

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration / 6, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, duration / 4, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration / 15, 0));

                for(int i = ThreadLocalRandom.current().nextInt(minDeamonsCount,maxDeamonsCount + 1); 0 < i;i--) {
                    Location mobLocation = player.getLocation();
                    int Yoffset = ThreadLocalRandom.current().nextInt(2,12 + 1);
                    if(player.getWorld().getBlockAt(mobLocation.getBlockX(),mobLocation.getBlockY() + Yoffset,mobLocation.getBlockZ()).getType() == Material.AIR) {
                        mobLocation.setY(mobLocation.getY() + Yoffset);
                    }

                    spawnTerror(mobLocation);
                }

                HadesDamnedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    HadesDamnedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HADES' CONSIDERS YOUR SOUL UNWORTHY TO EVEN BE IN THE UNDERWORLD OF FILTH, AND SPARED YOU."));
                }, duration);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&o&nHADES&r &o&0&oINQUIRIES YOUR FAITH"));
                HadesBlessedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    HadesBlessedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HADES' IS UNSATISFIED IN THE AMOUNT OF SOULS YOU COLLECTED FOR HIM, AND HAS DISCARDED HIS POWER FROM YOU."));
                }, duration);
            }
        }
    }

    public static void spawnTerror(Location location) {
        Phantom phantom = (Phantom)location.getWorld().spawnEntity(location, EntityType.PHANTOM);

        phantom.setSize(ladSize);
        phantom.setMaxHealth(30);
        phantom.setHealth(30);
        phantom.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
        phantom.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(8);
        phantom.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        phantom.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(50);
        phantom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(16);
        phantom.setGlowing(true);


        phantom.setCustomName(ChatColor.translateAlternateColorCodes('&',"&l&n&4DAEMON&r&l&0&o OF THE FALL"));
        phantom.setCustomNameVisible(true);
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&l&4DEFEAT A &r&0&l&o&nTERROR OF THE NIGHT&r&o&4 TO PLEASE&r&l&0 HADES");
        spawnTerror(new Location(player.getWorld(), player.getLocation().getX(),player.getLocation().getY() + 7, player.getLocation().getZ(),0,90));
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> giveDenseBone(player),
                () -> bringLadsOver(player.getLocation()),
                "&o&7A humongous ponderous bone drops from the &l&n&0TERROR&r&o&7 down onto the earth.",
                "&o&7The &l&n&0TERROR&r&o&7 reckoned bringing the lads over for a &4drink");
        QuestPlayerList.remove(player);
    }

    public static void giveDenseBone(Player player) {
        ItemStack denseBone = new ItemStack(Material.BONE);
        ItemMeta boneMeta = denseBone.getItemMeta();
        boneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lDENSE&r &8Bone of the &r&o&n&l&0TERROR&r"));
        boneMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7&oOnly fearless warriors can wield this heavy weapon.")));
        boneMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        boneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        boneMeta.addEnchant(Enchantment.KNOCKBACK,2,true);
        boneMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE.getKey().getKey(),12, AttributeModifier.Operation.ADD_NUMBER));
        boneMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE.getKey().getKey(),0.35, AttributeModifier.Operation.ADD_NUMBER));
        boneMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(),-0.15, AttributeModifier.Operation.ADD_NUMBER));
        denseBone.setItemMeta(boneMeta);

        player.getInventory().addItem(denseBone);
    }

    public static void bringLadsOver(Location location) {
        Location offsettedLocation = location;
        offsettedLocation.setY(offsettedLocation.getY() + 7);

        for(int i = ladsCount; i > 0; i--) {
            spawnTerror(offsettedLocation);
        }
    }
}
