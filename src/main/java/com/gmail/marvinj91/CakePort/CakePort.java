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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CakePort extends JavaPlugin {

    public static final String s = File.separator;
    static String maindirectory = "plugins" + s + "CakePort" + s;
    static File blocks = null;
    static File links = null;
    private final CakePortPlayerListener playerListener = new CakePortPlayerListener(this);
    private final CakePortBlockListener blockListener = new CakePortBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static Server server;

    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
        } catch (Exception e) {
            System.out.println("Cakeport 0.6 could not create directory!");
        }

        getDataFolder().setWritable(true);
        getDataFolder().setExecutable(true);

        blocks = new File(getDataFolder(), "blocks.data");
        if (!blocks.exists()) {
            try {
                blocks.createNewFile();
            } catch (IOException e) {
                System.out.println("Cakeport 0.6 could not create locs.data file!");
            }
        }

        links = new File(getDataFolder(), "links.data");
        if (!links.exists()) {
            try {
                links.createNewFile();
            } catch (IOException e) {
                System.out.println("Cakeport 0.6 could not create links.data file!");
            }
        }

        server = this.getServer();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);

        getCommand("addcake").setExecutor(new AddCakeCommand(this));
        getCommand("linkcakes").setExecutor(new LinkCakeCommand(this));

        if (Files.loadBlocks()) {
            System.out.println("CakePort blocks loaded.");
        }
        if (Files.loadLinks()) {
            System.out.println("CakePort links loaded");
        }
    }

    @Override
    public void onDisable() {
    }

    public boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
    }
}
