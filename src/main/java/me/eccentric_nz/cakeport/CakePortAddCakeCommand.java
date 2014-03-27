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
package me.eccentric_nz.cakeport;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CakePortAddCakeCommand implements CommandExecutor {

    private final CakePort plugin;

    public CakePortAddCakeCommand(CakePort plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {
        Player player = (Player) sender;
        if ((arg.length == 1) && (!plugin.anonymousCheck(sender)) && player.hasPermission("cakeport.add")) {
            String name = arg[0];
            Block block = CakePortPlayerListener.SelectBlock.get(player.getName());
            if (block == null) {
                player.sendMessage(ChatColor.RED + "Cake has not been selected.");
                return false;
            }
            boolean cakeExist = CakePortFiles.containskey(name, CakePort.blocks);
            if (!cakeExist) {
                if (CakePortFiles.write(name, block, CakePort.blocks)) {
                    CakePortFiles.CakeBlock.put(name, block);
                    player.sendMessage(ChatColor.GREEN + "Cake " + ChatColor.AQUA + name + ChatColor.GREEN + " has been added.");
                    CakePortPlayerListener.SelectBlock.put(player.getName(), null);
                } else {
                    player.sendMessage("Could not save CakePort.");
                }
            } else {
                player.sendMessage(ChatColor.AQUA + name + ChatColor.RED + " already exists.");
            }
        }
        return false;
    }
}
