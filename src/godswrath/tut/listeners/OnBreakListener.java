package godswrath.tut.listeners;

import godswrath.tut.Main;
import godswrath.tut.gods.Demeter;
import godswrath.tut.gods.Tyche;
import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class OnBreakListener implements Listener {

    public OnBreakListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event) {
       if(!event.isCancelled() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {

           if(event.isDropItems()) {
               Block block = event.getBlock();

               if (!block.getType().isInteractable()) {  //ignore or dismiss furnaces, chests, etc
                   if (Tyche.TycheBlessedPlayerList.contains(event.getPlayer())) {
                       event.setDropItems(false);

                       for (ItemStack item : block.getDrops()) {
                           item.setAmount(Clamp(item.getAmount() + ThreadLocalRandom.current().nextInt(1, Tyche.itemsMultipliedMax + 1), 1, item.getMaxStackSize())); //lucky multiply drop
                           block.getWorld().dropItemNaturally(block.getLocation(), item);
                       }
                   } else if (Tyche.TycheDamnedPlayerList.contains(event.getPlayer()) && Tyche.discardItemPercentChance >= ThreadLocalRandom.current().nextInt(0, 100)) {
                       event.setDropItems(false);
                   }
               }


               if(event.getBlock().getBlockData() instanceof org.bukkit.block.data.Ageable ageable && ageable.getAge() == ageable.getMaximumAge()) {
                   if(Demeter.DemeterBlessedPlayerList.contains(event.getPlayer())) {
                       for(ItemStack item : event.getBlock().getDrops())
                       {
                           item.setAmount(item.getAmount() * ThreadLocalRandom.current().nextInt(Demeter.minDropMultiplier,Demeter.maxDropMultiplier + 1));
                       }
                   }

                   if(Demeter.QuestPlayerList.contains(event.getPlayer())) {
                       new Demeter().invokeQuest(event.getPlayer(),true);
                   }
               }


               if(Demeter.DemeterDamnedPlayerList.contains(event.getPlayer())) {
                   event.getBlock().getDrops().removeIf(item -> ThreadLocalRandom.current().nextDouble(0, 100) <= Demeter.discardItemChancePercent);
               }

           }

           if(ThreadLocalRandom.current().nextInt(1,1000 + 1) < Tyche.landmineChancePerthousand) {
               Location targetLocation = event.getPlayer().getLocation();
               event.getPlayer().getWorld().spawn(event.getPlayer().getTargetBlock(null, 50).getLocation().add(0, 1, 0), TNTPrimed.class).setFuseTicks(100);
               getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                   event.getPlayer().getWorld().createExplosion(targetLocation,3,true,false);
               }, 100);
           }
       }
    }

    public static int Clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
