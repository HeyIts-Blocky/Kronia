package custom;

import java.util.ArrayList;
import java.awt.*;
import ent.*;
import trident.*;
import ent.game.*;
import blib.util.*;
public class GameData {
    
    public static Item[] inventory = new Item[40]; // first 10 is hotbar
    public static int selHotbar = 0;
    public static long atkTimer = 250, atkTime = 250;
    public static double atkDir = 0;
    public static boolean attacked = true;
    public static boolean invOpen = false;
    public static ArrayList<Rectangle> invBoxes = null;
    public static ArrayList<Rectangle> crateBoxes = null; 
    public static Item cursorItem = null;
    public static boolean minimapEnabled = true;
    public static int selCraft = 0;
    public static boolean settingsOpen = false;

    public static int health = 100, maxHealth = 100;
    public static long hurtTime = 0;
    public static long healTime = 0;
    public static int hunger = 100, maxHunger = 100;
    public static long hungerTimer = 0, hungerTime = 10000;
    public static double hungerSpeed = 0;

    public static long craftTimer = 0, craftTime = 50;

    public static long time = 0, maxTime = 600000;
    public static double maxDarkness = 0.9;

    public static boolean spectate = false;
    public static long deadTime = 0;

    public static boolean[] tutorialTriggers = new boolean[4];

    public static boolean setKeybind = false;
    public static int keybindSel = 0;

    public static Crate openCrate = null;

    public static Position lastDeath = null;

    public static boolean drawHud = true;

    public static ArrayList<Effect> effects = new ArrayList<Effect>();
    public static void addEffect(Effect e){
        for(Effect eff: effects){
            if(eff.id == e.id){
                if(eff.maxTime < e.maxTime || eff.time > e.time){
                    eff.time = e.time;
                    eff.maxTime = e.maxTime;
                    return;
                }
            }
        }

        effects.add(e);
    }
    public static boolean hasEffect(int id){
        for(Effect eff: effects){
            if(eff.id == id){
                return true;
            }
        }
        return false;
    }

    public static boolean rotateItem = false;
    public static final int[] canRotate = {
        Item.LOGWALL,
    };
    public static boolean canRotateCurrent(){
        for(int i: canRotate) if(i == inventory[selHotbar].id) return true;
        return false;
    }

    public static void damage(int amount){
        if(hurtTime > 0 || (Trident.getCurrentScene().name.equals("tutorial") && !tutorialTriggers[3]) || health <= 0) return;
        health -= amount;
        hurtTime = 1000;
        healTime += 2000;
        if(Settings.camShake) Trident.shakeCam(0.5);
        Settings.playSound("data/sound/damage.wav");
    }
    public static void forceDamage(int amount){
        health -= amount;
        hurtTime = 1000;
        healTime += 2000;
        Settings.playSound("data/sound/damage.wav");
    }

    public static String getFont(){
        return Settings.richText ? "Arial" : "Purple Smile";
    }

    public static void clearInventory(){
        inventory = new Item[40];
        for(int i = 0; i < inventory.length; i++){
            inventory[i] = new Item(Item.NOTHING, 0);
        }
    }

    public static void checkInventory(){
        for(Item i: inventory){
            if(i.amount <= 0){
                i.amount = 0;
                i.id = Item.NOTHING;
            }else if(i.id == Item.NOTHING){
                i.amount = 0;
            }else if(i.amount > 999){
                int diff = i.amount - 999;
                i.amount = 999;
                addItem(new Item(i.id, diff));
            }
        }
    }

    public static void addItem(Item item){
        if(item.id == Item.NOTHING) return;
        if(!canAdd(item)){
            // drop item
            GameObject.drop(item, Trident.getPlrPos());
            return;
        }
        for(int i = 0; i < inventory.length; i++){
            Item it = inventory[i];
            if(it.id == item.id && it.amount < 999){
                it.amount += item.amount;
                if(it.amount > 999){
                    int diff = it.amount - 999;
                    it.amount = 999;
                    addItem(new Item(item.id, diff));
                }
                return;
            }
        }
        for(int i = 0; i < inventory.length; i++){
            Item it = inventory[i];
            if(it.id == Item.NOTHING){
                inventory[i] = item;
                return;
            }
        }
    }
    public static void addToCrate(Item item){
        if(item.id == Item.NOTHING || openCrate == null) return;
        if(!canAddCrate(item)){
            // drop item
            GameObject.drop(item, Trident.getPlrPos());
            return;
        }
        for(int i = 0; i < 30; i++){
            Item it = openCrate.getSlot(i);
            if(it.id == item.id && it.amount < 999){
                it.amount += item.amount;
                if(it.amount > 999){
                    int diff = it.amount - 999;
                    it.amount = 999;
                    openCrate.setSlot(i, it);
                    addToCrate(new Item(item.id, diff));
                }else openCrate.setSlot(i, it);
                return;
            }
        }
        for(int i = 0; i < 30; i++){
            Item it = openCrate.getSlot(i);
            if(it.id == Item.NOTHING){
                openCrate.setSlot(i, item);
                return;
            }
        }
    }

    public static boolean canAdd(Item item){
        if(item.id == Item.NOTHING) return false;
        for(int i = 0; i < inventory.length; i++){
            Item it = inventory[i];
            if(it.id == Item.NOTHING){
                return true;
            }else if(it.id == item.id && it.amount < 999){
                return true;
            }
        }
        return false;
    }
    public static boolean canAddCrate(Item item){
        if(item.id == Item.NOTHING || openCrate == null) return false;
        for(int i = 0; i < 30; i++){
            Item it = openCrate.getSlot(i);
            if(it.id == Item.NOTHING){
                return true;
            }else if(it.id == item.id && it.amount < 999){
                return true;
            }
        }
        return false;
    }

    public static Item getSelItem(){
        return inventory[selHotbar];
    }

    public static double getDarkness(){
		double darkness = 0;
		double timePercent = (double)time / maxTime;
		if(timePercent >= 0 && timePercent < 0.25){
			darkness = 0;
		}
		if(timePercent >= 0.25 && timePercent < 0.5){
			darkness = ((1/.25) * timePercent - 1) * maxDarkness;
		}
		if(timePercent >= 0.5 && timePercent < 0.75){
			darkness = maxDarkness;
		}
		if(timePercent >= 0.75 && timePercent < 1){
			darkness = (-(1/0.25) * timePercent + 4) * maxDarkness;
		}
		return darkness;
	}

}
