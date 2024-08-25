package custom;

import javax.swing.*;
import blib.util.*;
import ent.GameObject;
public class Item {

    public int id, amount;
    
    public Item(int id, int a){
        this.id = id;
        amount = a;
    }
    public Item(int id){
        this.id = id;
        amount = 1;
    }
    public Item(){
        id = 0;
        amount = 0;
    }

    public Item copy(){
        return new Item(id, amount);
    }
    
    public ImageIcon getImg(){
        return getImg(id);
    }
    public int getType(){
        return getType(id);
    }
    public int[] getData(){
        return getData(id);
    }
    public String getName(){
        return getName(id);
    }
    public String getDescription(){
        return getDescription(id);
    }

    public static void resizeImgs(){
        for(ImageIcon i: imgs){
            if(i != null){
                BTools.resizeImgIcon(i, 32, 32);
            }
        }
    }


    public static ImageIcon getImg(int id){
        return imgs[id];
    }
    public static int getType(int id){
        return types[id];
    }
    public static int[] getData(int id){
        return data[id];
    }
    public static String getName(int id){
        return names[id];
    }
    public static String getDescription(int id){
        return descriptions[id];
    }

    // Item Types
    public static final int T_NULL = 0, T_SWORD = 1, T_PICK = 2, T_AXE = 3, T_CONSUMABLE = 4, T_PLACEABLE = 5, T_BOSS_SPAWN = 6, T_RANGED = 7, T_AMMO = 8, T_TRIGGER = 9, T_EFFECT = 10, T_MACE = 11;
    // Ammo Types
    public static final int A_ARROW = 0, A_PEBBLE = 1;
    /* Item IDs */
    public static final int NOTHING = 0, WOOD = 1, WORKBENCH = 2, W_SWORD = 3, W_PICK = 4, W_AXE = 5, STONE = 6, FURNACE = 7, RAWMEAT = 8, COOKEDMEAT = 9, JELLY = 10, LOGWALL = 11, TORCH = 12, COAL = 13, SCP999 = 14, S_SWORD = 15, S_PICK = 16, S_AXE = 17;
    public static final int APEXSPAWN = 18, HARDJELLY = 19, BOW = 20, ARROW = 21, J_SWORD = 22, J_PICK = 23, J_AXE = 24, J_SHOVEL = 25, EMERGENCYPOGO = 26, STONELADDER = 27, IRONORE = 28, IRONINGOT = 29;
    public static final int I_SWORD = 30, I_PICK = 31, I_AXE = 32, COPPERORE = 33, COPPERINGOT = 34, C_SWORD = 35, C_PICK = 36, C_AXE = 37, ANVIL = 38, PEBBLE = 39, I_ARROW = 40, J_ARROW = 41;
    public static final int HJ_ARROW = 42, TUTWOOD = 43, CAMPFIRE = 44, HOTDOG = 45, CRATE = 46, FLASK = 47, STR_FLASK = 48, HP_FLASK = 49, REGEN_FLASK = 50, GLOW_FLASK = 51, MACE = 52;
    /*****/

    private static ImageIcon[] imgs = {
            null, // 0
            new ImageIcon("data/images/items/wood.png"), // 1
            new ImageIcon("data/images/items/workbench.png"), // 2
            new ImageIcon("data/images/items/woodSword.png"), // 3
            new ImageIcon("data/images/items/woodPick.png"), // 4
            new ImageIcon("data/images/items/woodAxe.png"), // 5
            new ImageIcon("data/images/items/stone.png"), // 6
            new ImageIcon("data/images/items/furnace.png"), // 7
            new ImageIcon("data/images/items/rawMeat.png"), // 8
            new ImageIcon("data/images/items/cookedMeat.png"), // 9
            new ImageIcon("data/images/items/jelly.png"), // 10
            new ImageIcon("data/images/items/logWall.png"), // 11
            new ImageIcon("data/images/items/torch.png"), // 12
            new ImageIcon("data/images/items/coal.png"), // 13
            new ImageIcon("data/images/items/999spawn.png"), // 14
            new ImageIcon("data/images/items/spikedSword.png"), // 15
            new ImageIcon("data/images/items/stonePick.png"), // 16
            new ImageIcon("data/images/items/stoneAxe.png"), // 17
            new ImageIcon("data/images/items/apexSlimeSpawn.png"), // 18
            new ImageIcon("data/images/items/hardJelly.png"), // 19
            new ImageIcon("data/images/items/bow.png"), // 20
            new ImageIcon("data/images/items/arrow.png"), // 21
            new ImageIcon("data/images/items/jellySword.png"), // 22
            new ImageIcon("data/images/items/jellyPick.png"), // 23
            new ImageIcon("data/images/items/jellyAxe.png"), // 24
            new ImageIcon("data/images/items/jellyShovel.png"), // 25
            new ImageIcon("data/images/items/emergencyPogo.png"), // 26
            new ImageIcon("data/images/items/stoneLadder.png"), // 27
            new ImageIcon("data/images/items/ironOre.png"), // 28
            new ImageIcon("data/images/items/ironIngot.png"), // 29
            new ImageIcon("data/images/items/ironSword.png"), // 30
            new ImageIcon("data/images/items/ironPick.png"), // 31
            new ImageIcon("data/images/items/ironAxe.png"), // 32
            new ImageIcon("data/images/items/copperOre.png"), // 33
            new ImageIcon("data/images/items/copperIngot.png"), // 34
            new ImageIcon("data/images/items/copperSword.png"), // 35
            new ImageIcon("data/images/items/copperPick.png"), // 36
            new ImageIcon("data/images/items/copperAxe.png"), // 37
            new ImageIcon("data/images/items/anvil.png"), // 38
            new ImageIcon("data/images/items/pebble.png"), // 39
            new ImageIcon("data/images/items/ironArrow.png"), // 40
            new ImageIcon("data/images/items/jellyArrow.png"), // 41
            new ImageIcon("data/images/items/hardJellyArrow.png"), // 42
            new ImageIcon("data/images/items/wood.png"), // 43
            new ImageIcon("data/images/items/campfire.png"), // 44
            new ImageIcon("data/images/items/hotdog.png"), // 45
            new ImageIcon("data/images/items/crate.png"), // 46
            new ImageIcon("data/images/items/slimeFlask.png"), // 47
            new ImageIcon("data/images/items/strengthFlask.png"), // 48
            new ImageIcon("data/images/items/healthFlask.png"), // 49
            new ImageIcon("data/images/items/regenFlask.png"), // 50
            new ImageIcon("data/images/items/glowFlask.png"), // 51
            new ImageIcon("data/images/items/mace.png"), // 52
    };
    private static int[] types = {
            T_NULL, // 0
            T_NULL, // 1
            T_PLACEABLE, // 2
            T_SWORD, // 3
            T_PICK, // 4
            T_AXE, // 5
            T_NULL, // 6
            T_PLACEABLE, // 7
            T_CONSUMABLE, // 8
            T_CONSUMABLE, // 9
            T_EFFECT, // 10
            T_PLACEABLE, // 11
            T_PLACEABLE, // 12
            T_NULL, // 13
            T_PLACEABLE, // 14
            T_SWORD, // 15
            T_PICK, // 16
            T_AXE, // 17
            T_BOSS_SPAWN, // 18
            T_NULL, // 19
            T_RANGED, // 20
            T_AMMO, // 21
            T_SWORD, // 22
            T_PICK, // 23
            T_AXE, // 24
            T_TRIGGER, // 25
            T_TRIGGER, // 26
            T_TRIGGER, // 27
            T_NULL, // 28
            T_NULL, // 29
            T_SWORD, // 30
            T_PICK, // 31
            T_AXE, // 32
            T_NULL, // 33
            T_NULL, // 34
            T_SWORD, // 35
            T_PICK, // 36
            T_AXE, // 37
            T_PLACEABLE, // 38
            T_AMMO, // 39
            T_AMMO, // 40
            T_AMMO, // 41
            T_AMMO, // 42
            T_NULL, // 43
            T_PLACEABLE, // 44
            T_CONSUMABLE, // 45
            T_PLACEABLE, // 46
            T_NULL, // 47
            T_EFFECT, // 48
            T_TRIGGER, // 49
            T_EFFECT, // 50
            T_EFFECT, // 51
            T_MACE, // 52
    };
    private static int[][] data = { // atk, def, stuff like that
            {}, // 0
            {}, // 1
            {GameObject.WORKBENCH}, // 2
            {2, 300}, // 3
            {2, 250}, // 4
            {2, 250}, // 5
            {}, // 6
            {GameObject.FURNACE}, // 7
            {1, 250, 0, Effect.FOODPOISON}, // 8
            {15, 250, 5}, // 9
            {Effect.POISON}, // 10
            {GameObject.LOGWALL}, // 11
            {GameObject.TORCH}, // 12
            {}, // 13
            {GameObject.SCP999}, // 14
            {5, 350}, // 15
            {4}, // 16
            {4}, // 17
            {GameObject.APEXSLIME}, // 18
            {}, // 19
            {A_ARROW}, // 20
            {A_ARROW, 250, 40, 2}, // 21
            {7, 200}, // 22
            {7, 200}, // 23
            {7, 200}, // 24
            {1}, // 25
            {2}, // 26
            {2}, // 27
            {}, // 28
            {}, // 29
            {9}, // 30
            {9}, // 31
            {9}, // 32
            {}, // 33
            {}, // 34
            {6, 200}, // 35
            {6}, // 36
            {6}, // 37
            {GameObject.ANVIL}, // 38
            {A_PEBBLE, 250, 35, 1}, // 39
            {A_ARROW, 250, 40, 5}, // 40
            {A_ARROW, 250, 50, 1}, // 41
            {A_ARROW, 250, 50, 4}, // 42
            {}, // 43
            {GameObject.CAMPFIRE}, // 44
            {7, 250, 2}, // 45
            {GameObject.CRATE}, // 46
            {}, // 47
            {Effect.STRENGTH, 250, 750}, // 48
            {3}, // 49
            {Effect.REGEN, 250, 50}, // 50
            {Effect.GLOW, 250, 750}, // 51
            {200, 250, 40, 4}, // 52
    };
    public static String[] names = {
            "", // 0
            "Wood", // 1
            "Workbench", // 2
            "Wood Sword", // 3
            "Wood Pick", // 4
            "Wood Axe", // 5
            "Stone", // 6
            "Furnace", // 7
            "Raw Meat", // 8
            "Cooked Meat", // 9
            "Jelly", // 10
            "Log Wall", // 11
            "Torch", // 12
            "Coal", // 13
            "Strange Orange Jelly", // 14
            "Spiked Wood Sword", // 15
            "Stone Pick", // 16
            "Stone Axe", // 17
            "Condensed Jelly", // 18
            "Hardened Jelly", // 19
            "Bow", // 20
            "Arrow", // 21
            "Jelly Sword", // 22
            "Jelly Pick", // 23
            "Jelly Axe", // 24
            "Jelly Shovel", // 25
            "Emergency Pogo", // 26
            "Stone Ladder", // 27
            "Iron Ore", // 28
            "Iron Ingot", // 29
            "Iron Sword", // 30
            "Iron Pick", // 31
            "Iron Axe", // 32
            "Copper Ore", // 33
            "Copper Ingot", // 34
            "Copper Sword", // 35
            "Copper Pick", // 36
            "Copper Axe", // 37
            "\"Anvil\"", // 38
            "Pebble", // 39
            "Iron Arrow", // 40
            "Jelly Arrow", // 41
            "Hard Jelly Arrow", // 42
            "Tutorial Wood", // 43
            "Campfire Set", // 44
            "Hotdog", // 45
            "Crate", // 46
            "Empty Flask", // 47
            "Flask of Strength", // 48
            "Flask of Healing", // 49
            "Flask of Regeneration", // 50
            "Flask of Illumination", // 51
            "Mace", // 52
    };
    private static String[] descriptions = {
            "", // 0
            "It's wood.", // 1
            "You can make all sorts of stuff on this", // 2
            "Swish, swish", // 3
            "Mining away", // 4
            "We all have to start somewhere", // 5
            "Rock and stone.", // 6
            "Hot, hot, hot!", // 7
            "You... you're gonna eat that, aren't you?", // 8
            "A delicious, savory steak.. needs seasoning though.", // 9
            "It's really sticky, but could probably burn for a while", // 10
            "A big wall that should keep the monsters out", // 11
            "You light up my world", // 12
            "It's like a diamond... but not as dense", // 13
            "Just touching it makes you filled with joy", // 14
            "Gronk make stick hurt more by put rock on stick. Stick hurt big.", // 15
            "Rock the rock's world", // 16
            "Those trees are gonna regret being in your way", // 17
            "You hear a distant yet large squelch...", // 18
            "It's somehow light, goopy, and strong at the same time", // 19
            "Try not to shoot your eye out", // 20
            "Pointy stick go zoom", // 21
            "Now you can slay your enemies quick, and leave them feeling goopy.", // 22
            "It can cut through stone easily, but there's probably better underground.", // 23
            "You're getting goop all over my wood.", // 24
            "Lifetime pass to the underground! You're on your own to get back, though.", // 25
            "You can bounce to the surface, but a shovel isn't really meant to do that...", // 26
            "Lets you climb to the surface from the mines.", // 27
            "Only useful once refined.", // 28
            "Cold, hard steel", // 29
            "A sharp, heavy blade. It's reliable.", // 30
            "Ever stronger.", // 31
            "This can cut through trees with ease.", // 32
            "It's rusted over a little", // 33
            "You've smelted the rust off, for now", // 34
            "A bit dull, but it's light", // 35
            "It'll work for a bit", // 36
            "Well... it's better than stone.", // 37
            "What? It's still a surface to make tools on!", // 38
            "Pebble", // 39
            "Now the stick's even sharper!", // 40
            "It's light but won't hit as hard", // 41
            "Not the sharpest, but it'll fly quick", // 42
            "It comes from the endangered Tutorial Tree, you monster.", // 43
            "Cooking on the go", // 44
            "its got that cow in 'em", // 45
            "You can put your stuff in here for safe keeping", // 46
            "It's not big, but you can do some simple alchemy with this", // 47
            "Your blade feels particularly sharp for a short while", // 48
            "Instantly heal some of your health", // 49
            "Regenerate your health for a short time", // 50
            "Cause your skin to glow, lighting up the surrounding area", // 51
            "A ball of death. That's metal as hell.", // 52
    };
}
