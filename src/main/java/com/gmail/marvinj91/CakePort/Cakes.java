package com.gmail.marvinj91.CakePort;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Cakes {

    public static boolean isCakeBlock(Block block) {
        Properties pro = new Properties();
        Location loc = block.getLocation();
        World world = block.getWorld();

        String worldName = world.getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        try {
            FileInputStream in = new FileInputStream(CakePort.blocks);
            pro.load(in);
            if (pro.contains(worldName + "," + x + "," + y + "," + z)) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static String getName(Block block) {
        String cakeName = null;
        Map<String, Block> map = Files.CakeBlock;
        for (Map.Entry<String, Block> entry : map.entrySet()) {
            Block cBlock = entry.getValue();
            if (cBlock.equals(block)) {
                cakeName = entry.getKey();
            }
        }
        return cakeName;
    }

    public static Location getDest(String cakeName, Block block) {
        Location loc;
        String destCake;

        if (Files.CakeBlock.containsKey(cakeName)) {
            destCake = (String) Files.CakeLinks.get(cakeName);
            Block destBlock = Files.CakeBlock.get(destCake);
            loc = destBlock.getLocation();

            //y variable adjusts for Cake, full or half blocks (fooling around)
            loc.add(.5, 1, .5);
            if (block.getType() == Material.CAKE_BLOCK) {
                loc.subtract(0, .5625, 0);
            } else if (block.getType() == Material.STEP) {
                loc.subtract(0, .5, 0);
            }
            return loc;
        }
        return null;
    }
}
