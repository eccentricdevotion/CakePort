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
