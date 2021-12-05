package godswrath.tut.gods;

import godswrath.tut.God;
import godswrath.tut.Main;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Hephaestus implements God {
    public static final float RepairPercent = 0.25f;
    public static final float DisrepairPercent = 0.25f;
    public static final int FlameAspectDurationTicks = 40;
    public static final int AddUnbreakingLevelPercentChance = 25;
    public static final int UnbreakingLevelToAdd = 2;
    public static List<Player> HephaestusBlessedPlayerList = new ArrayList<Player>();
    public static List<Player> HephaestusDamnedPlayerList = new ArrayList<Player>();
    public static List<Player> QuestPlayerList = new ArrayList<Player>();

    @Override
    public void Invoke(Player player) {
        if (player.isOnline()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&4&lHEPHAESTUS&r&c&k+&r &o&8HAS BURNED YOUR DESTINY TO ASHES."));
                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 500.0f, 1.0f);
                player.setFireTicks(Main.GodEventDurationTicks);
                damageAllGear(player);

                HephaestusDamnedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    HephaestusDamnedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HEPHAESTUS GOT EVENTUALLY BORED AFTER SEEING YOU BURN, AND RETIRED TO BLACKSMITHING."));
                }, Main.GodEventDurationTicks);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&k+&r&4&lHEPHAESTUS&r&c&k+&r &o&eHAS REVEALED YOU THE WAY OF THE FLAMES."));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 500.0f, 1.0f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Main.GodEventDurationTicks, 3));
                for(ItemStack armorpiece : player.getEquipment().getArmorContents()) {

//                    int calculatedDurability =  Math.round(getItemDamage(armorpiece) + (getItemMaxDurability(armorpiece) * (1 + RepairPercent)));
//                    setItemDamage(armorpiece, Math.max(getItemMaxDurability(armorpiece), calculatedDurability));
                    if(armorpiece != null) {
                        int calculatedDamage = Math.round(getItemMaxDurability(armorpiece) * RepairPercent);
                        setItemDamage(armorpiece, getItemDamage(armorpiece) - calculatedDamage);

                        if (AddUnbreakingLevelPercentChance >= ThreadLocalRandom.current().nextInt(0, 100 + 1)) {
                            ItemMeta armorMeta = armorpiece.getItemMeta();
                            int unbreakingLevel = UnbreakingLevelToAdd;

                            if (armorMeta.hasEnchant(Enchantment.DURABILITY)) {
                                unbreakingLevel += armorMeta.getEnchantLevel(Enchantment.DURABILITY);
                            }
                            armorMeta.addEnchant(Enchantment.DURABILITY, unbreakingLevel, true);
                            armorpiece.setItemMeta(armorMeta);
                        }
                    }
                }
                HephaestusBlessedPlayerList.add(player);
                getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    HephaestusBlessedPlayerList.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&o&7HEPHAESTUS IS NO LONGER INTERESTED IN YOUR IMMOLATION TOWARDS HIS BLISS."));
                }, Main.GodEventDurationTicks);
            }
        }
    }

    @Override
    public void requestQuest(Player player) {
        Main.requestQuestLabel(player,"&f&oRepair an item for &r&eHephaestus");
        QuestPlayerList.add(player);
    }

    @Override
    public void invokeQuest(Player player, Boolean Completed) {
        Main.invokeQuest(player, Completed,
                () -> player.giveExpLevels(15),
                () -> damageAllItems(player),
                "&eHephaestus&r&f&o enhanced your forging knowledge",
                "&eHephaestus&r&f&o hammered all of your equipment, disfiguring it");
        QuestPlayerList.remove(player);
    }

    public static void damageAllItems(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item != null) {
                int calculatedDamage = Math.round(getItemMaxDurability(item) * DisrepairPercent);
                setItemDamage(item, getItemDamage(item) + calculatedDamage);
            }
        }
        player.playSound(player.getLocation(),Sound.ITEM_SHIELD_BREAK,500f,1f);
    }

    public static void damageAllGear(Player player) {
        for(ItemStack armorpiece : player.getInventory().getArmorContents()) {
            if(armorpiece != null) {
                int calculatedDamage = Math.round(getItemMaxDurability(armorpiece) * DisrepairPercent);
                setItemDamage(armorpiece, getItemDamage(armorpiece) + calculatedDamage);
            }
        }
        player.playSound(player.getLocation(),Sound.ITEM_SHIELD_BREAK,500f,1f);
    }

    public static void setItemDamage(ItemStack item, int damage) {
        if (item != null) {
            if(item.getItemMeta() != null) {
                if (item.getItemMeta() instanceof Damageable damageable) {
                    damageable.setDamage(damage);
                    item.setItemMeta((ItemMeta) damageable);
                }
            }
        }
    }

    public static int getItemDamage(ItemStack item) {
        if (item != null) {
            if(item.getItemMeta() != null)
                if (item.getItemMeta() instanceof  Damageable damageable) {
                    return damageable.getDamage();
                }
        }
        return -1;
    }

    public static int getItemMaxDurability(ItemStack item)
    {
        if(item != null) {
            return item.getType().getMaxDurability();
        }
       return 0;
    }


}
