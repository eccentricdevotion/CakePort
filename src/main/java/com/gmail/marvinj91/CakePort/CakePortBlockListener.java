package com.gmail.marvinj91.CakePort;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CakePortBlockListener implements Listener {

    private final CakePort plugin;

    public CakePortBlockListener(CakePort instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        String cakeName;

        if (Cakes.isCakeBlock(block)) {
            if (player.hasPermission("cakeport.remove")) {
                cakeName = Cakes.getName(block);
                Files.deleteCake(cakeName);
                player.sendMessage(ChatColor.RED + "Cake " + ChatColor.AQUA + cakeName + ChatColor.RED + " has been removed.");
            } else {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to remove CakePorts.");
                event.setCancelled(true);
            }
        }
    }
}
