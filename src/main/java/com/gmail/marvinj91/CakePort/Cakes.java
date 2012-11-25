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
