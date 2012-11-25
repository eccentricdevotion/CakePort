/*
 * Copyright 2011 marvinj91. All rights reserved.
 *
 * Development and maintenance taken over by Rob Rate 2012.
 *
 * Licensed under The GNU General Public License v3.0, a copy of the licence is included in the JAR file.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.gmail.marvinj91.CakePort;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CakePortPlayerListener implements Listener {

    private final CakePort plugin;
    public static Map<String, Boolean> hasTeleported = new HashMap<String, Boolean>();
    public static Map<String, Block> SelectBlock = new HashMap<String, Block>();

    public CakePortPlayerListener(CakePort plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            boolean isCakePort = Files.CakeBlock.containsValue(block);

            //Allow block / Item in hand here - May make config for any block
            if (player.getItemInHand().getType() == Material.BONE && block.getType() == Material.CAKE_BLOCK) {
                if (player.hasPermission("cakeport.add")) {
                    SelectBlock.put(player.getName(), block);
                    player.sendMessage(ChatColor.DARK_GREEN + "Cake selected.");
                }
            }

            //Cake is a lie
            if (isCakePort && block.getType() == Material.CAKE_BLOCK) {
                int health = player.getHealth();
                if (health < 20) {
                    event.setCancelled(true);
                    block.setData((byte) 0x0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        World world = event.getTo().getWorld();
        Player player = event.getPlayer();
        String cakeName;
        Location destLoc;
        Location pLoc = player.getLocation();
        //getY()-.2 = at what point below the player to scan
        Location loc = new Location(world, event.getTo().getX(), event.getTo().getY() - .2, event.getTo().getZ());
        Block blockin = world.getBlockAt(loc);

        boolean isCakePort = Files.CakeBlock.containsValue(blockin);

        if (!isCakePort) {
            hasTeleported.put(player.getName(), false);
        }
        if (player.hasPermission("cakeport.warp")) {
            if (isCakePort && !hasTeleported.get(player.getName())) {
                cakeName = Cakes.getName(blockin);
                boolean isLinked = Files.CakeLinks.containsKey(cakeName);
                if (isLinked) {
                    destLoc = Cakes.getDest(cakeName, blockin);
                    destLoc.setPitch(player.getLocation().getPitch());
                    destLoc.setYaw(player.getLocation().getYaw());
                    event.setTo(destLoc);
                    if (player.teleport(destLoc)) {
                        hasTeleported.put(player.getName(), true);
                    }
                }
            }
        }
    }
}