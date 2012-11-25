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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCakeCommand implements CommandExecutor {

    private final CakePort plugin;

    public LinkCakeCommand(CakePort plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {
        Player player = (Player) sender;
        if ((arg.length == 2) && (!plugin.anonymousCheck(sender)) && player.hasPermission("cakeport.link")) {
            String fCake = arg[0];
            String sCake = arg[1];
            boolean doesExist1 = Files.containskey(fCake, CakePort.blocks);
            boolean doesExist2 = Files.containskey(sCake, CakePort.blocks);
            boolean isFirstLinked = Files.containskey(fCake, CakePort.links);
            boolean isSecondLinked = Files.containskey(sCake, CakePort.links);
            if (!doesExist1 || !doesExist2) {
                if (!doesExist1) {
                    player.sendMessage(ChatColor.AQUA + fCake + ChatColor.RED + " does not exist.");
                }
                if (!doesExist2) {
                    player.sendMessage(ChatColor.AQUA + sCake + ChatColor.RED + " does not exist.");
                }
                return false;
            }
            if (isFirstLinked || isSecondLinked) {
                if (isFirstLinked) {
                    player.sendMessage(ChatColor.AQUA + fCake + ChatColor.RED + " is already linked.");
                }
                if (isSecondLinked) {
                    player.sendMessage(ChatColor.AQUA + sCake + ChatColor.RED + " is already linked.");
                }
                return false;
            }
            Files.linkCakes(fCake, sCake, CakePort.links);
            player.sendMessage(ChatColor.GREEN + "Cakes linked.");
        }
        return false;
    }
}
