package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Neptune implements God {
//    public static final int waterRadius = 20;
public static final float swimBoostMultiplier = 1.5f;
public static final float speedLimit = 2f;
    public static List<Player> QuestPlayerList = new ArrayList<Player>();


    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&b&k+&r&3&lNEPTUNE&r&o&b&k+&r &1&oURGES TO SPAWN SEISMIC WAVES UPON YOUR HABITAT."));
                player.playSound(player.getLocation(), Sound.ENTITY_DROWNED_AMBIENT_WATER, 500.0f, 1.0f);
                player.teleport(closestBiomeLocation(Biome.OCEAN,new Location(Bukkit.getWorlds().get(0),ThreadLocalRandom.current().nextDouble(-3000000,3000001), 63,ThreadLocalRandom.current().nextDouble(-3000000,3000001))));

                player.setVelocity(new Vector(0,-100,0));
                player.setRemainingAir(0);

                spawnSeaProtector(new Location(player.getWorld(),player.getLocation().getX() + 5, player.getLocation().getY(), player.getLocation().getZ(),-90,0));
                spawnSeaProtector(new Location(player.getWorld(),player.getLocation().getX() - 5, player.getLocation().getY(), player.getLocation().getZ(),90,0));
                spawnSeaProtector(new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 5,0,0));
                spawnSeaProtector(new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 5,180,0));


            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&b&k+&r&3&lNEPTUNE&r&o&b&k+&r &1&oIS IN FAVOUR OF YOUR AQUATIC  SPRIGHTLINESS"));
                player.playSound(player.getLocation(), Sound.ENTITY_DROWNED_SWIM, 500.0f, 1.0f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Main.GodEventDurationTicks, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Main.GodEventDurationTicks, 2));
            }
        }
    }
    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&1&lNeptune&r&o&f expects you to throw and hit");
        giveOneshotTrident(player);
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> giveOldSpyglass(player),
                () -> spawnSeaProtector(player.getLocation()),
                "&1&lNepture&r&o&f awards you with something random off his drawer",
                "&1&lNeptune&r&o&f was feeling disgraced, and hired a hitman");
        QuestPlayerList.remove(player);
    }

    public Location closestBiomeLocation(Biome biomeToLocate, Location originLocation) {
        double range = 5000;
        double pointOffset = 16 * 8; //minimum block distance to switch biome
        Location possibleLocation = originLocation;
        // TODO: Make change Main.getPlugin(Main.class) to a greek key pattern for infinite lookout

        for (double x = originLocation.getX() - (range / 2); x <= originLocation.getX() + ((range / 2) + 1); x += pointOffset) {
            for (double z = originLocation.getZ() - (range / 2); z <= originLocation.getZ() + ((range / 2) + 1); z += pointOffset) {
                possibleLocation.setX(x);
                possibleLocation.setZ(z);
                if(possibleLocation.getBlock().getBiome() == biomeToLocate) {
                    possibleLocation.setYaw(0);
                    possibleLocation.setPitch(0);
                    return possibleLocation;
                }
            }
        }
        return originLocation;
    }

    public static void giveOneshotTrident(Player player) {
        ItemStack oneshotTrident = new ItemStack(Material.TRIDENT);
        ItemMeta tridentMeta = oneshotTrident.getItemMeta();

        Repairable tridentRepairableInstance = (Repairable)tridentMeta;
        tridentRepairableInstance.setRepairCost(1000);
        oneshotTrident.setItemMeta((ItemMeta) tridentRepairableInstance);
        Hephaestus.setItemDamage(oneshotTrident,Hephaestus.getItemMaxDurability(oneshotTrident) - 2);


        player.getInventory().addItem(oneshotTrident);
    }

    public static void giveOldSpyglass(Player player) {
        ItemStack spyglass = new ItemStack(Material.SPYGLASS);
        ItemMeta spyglassMeta = spyglass.getItemMeta();
        spyglassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&fOl' Spyglass of Capt' Usopp"));
        spyglassMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&o&7Legends says he spied on his neighbours with it.")));
        spyglass.setItemMeta(spyglassMeta);
        player.getInventory().addItem(spyglass);
    }

    public void spawnSeaProtector(Location location) {
        Drowned drowned = (Drowned)location.getWorld().spawnEntity(location, EntityType.DROWNED);
        Guardian guardian = (Guardian)location.getWorld().spawnEntity(location, EntityType.GUARDIAN);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack iteminhand = new ItemStack(Material.TRIDENT);

        ItemMeta meta = iteminhand.getItemMeta();
        meta.addEnchant(Enchantment.IMPALING, 3, true);
        iteminhand.setItemMeta(meta);

        LeatherArmorMeta bootsMeta = (LeatherArmorMeta)boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(0,157,196));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
        boots.setItemMeta(bootsMeta);

        LeatherArmorMeta chesplateMeta = (LeatherArmorMeta)boots.getItemMeta();
        chesplateMeta.setColor(Color.fromRGB(0,157,196));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
        chestplate.setItemMeta(chesplateMeta);

        LeatherArmorMeta legsMeta = (LeatherArmorMeta)boots.getItemMeta();
        legsMeta.setColor(Color.fromRGB(0,157,196));
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
        legs.setItemMeta(legsMeta);


        drowned.getEquipment().setBoots(boots);
        drowned.getEquipment().setChestplate(chestplate);
        drowned.getEquipment().setLeggings(legs);
        drowned.getEquipment().setItemInHand(iteminhand);
        drowned.setMaxHealth(60);
        drowned.setHealth(60);

        drowned.setCustomName(ChatColor.translateAlternateColorCodes('&',"&l&o&1SERVANT OF&r &o&b&k+&r&3&lNEPTUNE&r&o&b&k+"));
        drowned.setCustomNameVisible(true);

        guardian.setCustomName(ChatColor.translateAlternateColorCodes('&',"&l&o&1CREATURE OF THE&b OCEAN"));
        guardian.setCustomNameVisible(true);

        drowned.getEquipment().setBootsDropChance(0);
        drowned.getEquipment().setHelmetDropChance(0);
        drowned.getEquipment().setChestplateDropChance(0);
        drowned.getEquipment().setLeggingsDropChance(0);
        drowned.getEquipment().setItemInHandDropChance(0);

        guardian.setPassenger(drowned);
    }
}
