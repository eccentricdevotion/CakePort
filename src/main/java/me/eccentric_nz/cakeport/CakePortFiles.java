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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CakePortFiles {

    public static Map<String, Block> CakeBlock = new HashMap<String, Block>();
    public static Map<String, String> CakeLinks = new HashMap<String, String>();

    public static boolean loadBlocks() {
        Properties pro = new Properties();

        try {
            FileInputStream in = new FileInputStream(CakePort.blocks);
            pro.load(in);
            Iterator keys = pro.keySet().iterator();
            while (keys.hasNext()) {
                String CakeName = (String) keys.next();
                String string = (String) pro.getProperty(CakeName);
                String[] arg = string.split(",");
                String WorldName = arg[0];
                int x = Integer.parseInt(arg[1]);
                int y = Integer.parseInt(arg[2]);
                int z = Integer.parseInt(arg[3]);
                World world = CakePort.server.getWorld(WorldName);
                Block block = world.getBlockAt(x, y, z);
                CakeBlock.put(CakeName, block);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean loadLinks() {
        Properties pro = new Properties();
        try {
            FileInputStream in = new FileInputStream(CakePort.links);
            pro.load(in);

            Iterator keys = pro.keySet().iterator();
            while (keys.hasNext()) {
                String FirstCake = (String) keys.next();
                String SecondCake = (String) pro.getProperty(FirstCake);
                CakeLinks.put(FirstCake, SecondCake);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean containskey(String name, File file) {
        Properties pro = new Properties();
        try {
            FileInputStream in = new FileInputStream(file);
            pro.load(in);
            if (pro.containsKey(name)) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean write(String name, Block block, File blocks) {
        Properties pro = new Properties();
        World world = block.getWorld();
        String worldName = world.getName();
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();
        try {
            FileInputStream in = new FileInputStream(blocks);
            pro.load(in);

            pro.setProperty(name, worldName + "," + x + "," + y + "," + z);
            pro.store(new FileOutputStream(blocks), null);
        } catch (IOException e) {
            System.out.println("Could not write to file " + blocks.getName());
            return false;
        }
        return true;
    }

    public static boolean linkCakes(String fCake, String sCake, File links) {
        Properties pro = new Properties();
        try {
            CakeLinks.put(fCake, sCake);
            CakeLinks.put(sCake, fCake);
            FileInputStream in = new FileInputStream(links);
            pro.load(in);
            pro.setProperty(fCake, sCake);
            pro.setProperty(sCake, fCake);
            pro.store(new FileOutputStream(links), null);
        } catch (IOException e) {
            System.out.println("Could not write to file " + links.getName());
            return false;
        }
        return true;
    }

    public static boolean deleteCake(String cakeName) {
        Properties pro = new Properties();
        Properties pro2 = new Properties();
        boolean isLinked = CakeLinks.containsKey(cakeName);
        try {
            FileInputStream in = new FileInputStream(CakePort.links);
            FileInputStream in2 = new FileInputStream(CakePort.blocks);
            pro.load(in);
            pro2.load(in2);
            if (isLinked) {
                String link = (String) pro.getProperty(cakeName);
                CakeLinks.remove(cakeName);
                CakeLinks.remove(link);
                pro.remove(cakeName);
                pro.remove(link);
                pro.store(new FileOutputStream(CakePort.links), null);
            }
            CakeBlock.remove(cakeName);
            pro2.remove(cakeName);
            pro2.store(new FileOutputStream(CakePort.blocks), null);
        } catch (IOException e) {
            System.out.println("Could not write to file " + CakePort.links.getName());
            return false;
        }
        return true;
    }
}
