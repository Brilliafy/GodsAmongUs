package godswrath.tut.listeners;

import godswrath.tut.gods.Erebus;
import godswrath.tut.gods.Hephaestus;
import godswrath.tut.Main;
import godswrath.tut.gods.Hermes;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnMovementListener implements Listener {
    public OnMovementListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnMovementListener(PlayerMoveEvent event) {
        if(!event.isCancelled()) {
            if(Hephaestus.HephaestusDamnedPlayerList.contains(event.getPlayer()) && event.getPlayer().isOnGround()) {
                event.getPlayer().getLocation().getBlock().setType(Material.FIRE);
            }

            if(Erebus.QuestPlayerList.contains(event.getPlayer())) {
                final BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
                int light = 0;
                for (BlockFace blockFace: blockFaces) {
                    light = Math.max(light, event.getPlayer().getLocation().getBlock().getRelative(blockFace).getLightLevel());
                }
                if(light == 0) {
                    new Erebus().invokeQuest(event.getPlayer(),true);
                }
            }
            if(Hermes.QuestPlayerList.contains(event.getPlayer())) {
                if(Math.abs(event.getPlayer().getVelocity().getX()) * 20 > Hermes.questVelocityRequirement ||
                   Math.abs(event.getPlayer().getVelocity().getY()) * 20 > Hermes.questVelocityRequirement ||
                   Math.abs(event.getPlayer().getVelocity().getZ()) * 20 > Hermes.questVelocityRequirement) {
                    new Hermes().invokeQuest(event.getPlayer(),true);
                }
            }
        }
    }
}
