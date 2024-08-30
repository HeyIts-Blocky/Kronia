package update;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import blib.util.BTools;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import custom.MusicManager;
import custom.Recipe;
import custom.Settings;
import custom.WorldManager;
import ent.GameObject;
import ent.HUD;
import ent.TitleScreen;
import ent.game.MaceBall;
import trident.TridEntity;
import trident.Trident;
public class Inputs {
    
    public static void keyPressed(int key){
        if((Trident.getCurrentScene().name.equals("title") || GameData.settingsOpen) && GameData.setKeybind){
            GameData.setKeybind = false;
            Settings.keybinds[GameData.keybindSel] = key;
            TitleScreen.updateButtonText();
        }
        if(Trident.getCurrentScene().name.equals("title")){
            // world name
            if(TitleScreen.worldName.length() < 20){
                if(key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z){
                    char c = KeyEvent.getKeyText(key).charAt(0);
                    if(!Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                        c = Character.toLowerCase(c);
                    }
                    TitleScreen.worldName += c;
                }
                if(key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9){
                    char c = KeyEvent.getKeyText(key).charAt(0);
                    TitleScreen.worldName += c;
                }
                if(key == KeyEvent.VK_SPACE){
                    TitleScreen.worldName += " ";
                }
                if(key == KeyEvent.VK_MINUS){
                    TitleScreen.worldName += "-";
                }
            }
            if(key == KeyEvent.VK_BACK_SPACE){
                if(TitleScreen.worldName.length() > 0) TitleScreen.worldName = TitleScreen.worldName.substring(0, TitleScreen.worldName.length() - 1);
            }
        }
        if(Trident.getCurrentScene().name.equals("world") || (Trident.getCurrentScene().name.equals("tutorial") && GameData.tutorialTriggers[0])){
            if(GameData.spectate) return;
            // in world
            if(!MaceBall.maceOut()){
                if(key == KeyEvent.VK_1){
                    GameData.selHotbar = 0;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_2){
                    GameData.selHotbar = 1;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_3){
                    GameData.selHotbar = 2;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_4){
                    GameData.selHotbar = 3;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_5){
                    GameData.selHotbar = 4;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_6){
                    GameData.selHotbar = 5;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_7){
                    GameData.selHotbar = 6;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_8){
                    GameData.selHotbar = 7;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_9){
                    GameData.selHotbar = 8;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
                if(key == KeyEvent.VK_0){
                    GameData.selHotbar = 9;
                    Settings.playSound("data/sound/hotbarSel.wav");
                }
            }
            

            if(key == Settings.keybinds[Settings.INVENTORY]){
                GameData.invOpen = !GameData.invOpen;
                if(GameData.cursorItem != null){
                    GameData.addItem(GameData.cursorItem);
                    GameData.cursorItem = null;
                }
                GameData.selCraft = 0;
            }

            if(key == Settings.keybinds[Settings.DROP] && GameData.getSelItem().id != Item.NOTHING){
                GameObject.drop(GameData.getSelItem(), Trident.getPlrPos());
                GameData.getSelItem().amount = 0;
            }
            if(key == Settings.keybinds[Settings.MAP]){
                GameData.minimapEnabled = !GameData.minimapEnabled;
            }
            
            if(key == Settings.keybinds[Settings.ROTATE]){
                if(GameData.canRotateCurrent()){
                    GameData.rotateItem = !GameData.rotateItem;
                }
            }
        }
    }

    public static void mousePressed(int mb, Point mousePos, Position worldPos){
        if(Trident.getCurrentScene().name.equals("world") || (Trident.getCurrentScene().name.equals("tutorial") && GameData.tutorialTriggers[0])){
            if(GameData.spectate) return;
            if(GameData.invOpen){
                int slot = -1;
                for(int i = 0; i < GameData.invBoxes.size(); i++){
                    Rectangle r = GameData.invBoxes.get(i);
                    if(r.contains(mousePos)){
                        slot = i;
                        break;
                    }
                }
                if(slot != -1){
                    if(slot == 40){
                        if(Recipe.getRecipes().size() == 0) return;
                        if(GameData.cursorItem == null || (GameData.cursorItem.id == Recipe.getRecipes().get(GameData.selCraft).output.id && GameData.cursorItem.amount + Recipe.getRecipes().get(GameData.selCraft).output.amount < 999)){
                            int initialSize = Recipe.getRecipes().size();
                            Recipe recipe = Recipe.getRecipes().get(GameData.selCraft);
                            Item item = recipe.craft();
                            if(item != null){
                                if(GameData.cursorItem == null) GameData.cursorItem = item;
                                else GameData.cursorItem.amount += item.amount;
                            }
                            if(Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                while(recipe.canCraft() && GameData.cursorItem.amount + recipe.output.amount < 999){
                                    item = recipe.craft();
                                    GameData.cursorItem.amount += item.amount;
                                }
                            }

                            if(Recipe.getRecipes().size() != initialSize){
                                // recipes have moved, check if the old recipe is still there
                                for(int i = 0; i < Recipe.getRecipes().size(); i++){
                                    if(Recipe.getRecipes().get(i).equals(recipe)){
                                        // found the recipe
                                        GameData.selCraft = i;
                                        break;
                                    }
                                }
                            }
                        }
                        return;
                    }
                    if(slot == 41){
                        WorldManager.saveWorld();
                        HUD.addNotif("Game saved.", HUD.NOTIF_SAVE);
                        Trident.resetKeys();
                        GameData.invOpen = false;
                        return;
                    }
                    if(slot == 42){
                        WorldManager.saveWorld();
                        GameData.invOpen = false;
                        Trident.loadScene("title");
                        MusicManager.stopSong();
                        MusicManager.songTime = 0;
                        Trident.stopShake();
                        return;
                    }
                    if(slot == 43){
                        GameData.selCraft--;
                        return;
                    }
                    if(slot == 44){
                        GameData.selCraft++;
                        return;
                    }
                    if(slot == 45){
                        GameData.settingsOpen = true;
                        TitleScreen menu = new TitleScreen(new Position());
                        menu.inGameMenu = true;
                        menu.screen = 3;
                        Trident.spawnEntity(menu);
                        return;
                    }
                    if(GameData.cursorItem == null){
                        if(GameData.inventory[slot].id != Item.NOTHING){
                            if(mb == 1){
                                if(GameData.openCrate != null && Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                    GameData.addToCrate(GameData.inventory[slot]);
                                }else GameData.cursorItem = GameData.inventory[slot];
                                GameData.inventory[slot] = new Item();
                            }
                            if(mb == 3){
                                if(GameData.inventory[slot].amount == 1){
                                    GameData.cursorItem = GameData.inventory[slot];
                                    GameData.inventory[slot] = new Item();
                                }else{
                                    int diff = GameData.inventory[slot].amount - (GameData.inventory[slot].amount / 2);
                                    GameData.inventory[slot].amount /= 2;
                                    GameData.cursorItem = new Item(GameData.inventory[slot].id, diff);
                                }
                            }
                        }
                    }else{
                        if(GameData.inventory[slot].id == Item.NOTHING){
                            if(mb == 1){
                                GameData.inventory[slot] = GameData.cursorItem;
                                GameData.cursorItem = null;
                            }
                            if(mb == 3){
                                GameData.inventory[slot] = new Item(GameData.cursorItem.id);
                                GameData.cursorItem.amount--;
                                if(GameData.cursorItem.amount <= 0){
                                    GameData.cursorItem = null;
                                }
                            }
                        }else if(GameData.inventory[slot].id == GameData.cursorItem.id && GameData.inventory[slot].amount < 999){
                            if(mb == 1){
                                if(GameData.openCrate != null && Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                    GameData.addToCrate(GameData.inventory[slot]);
                                }else{
                                    int total = GameData.cursorItem.amount + GameData.inventory[slot].amount;
                                    if(total <= 999){
                                        GameData.inventory[slot].amount = total;
                                        GameData.cursorItem = null;
                                    }else{
                                        int diff = total - 999;
                                        GameData.inventory[slot].amount = 999;
                                        GameData.cursorItem.amount = diff;
                                    }
                                }
                                
                            }
                            if(mb == 3){
                                GameData.inventory[slot].amount++;
                                GameData.cursorItem.amount--;
                                if(GameData.cursorItem.amount <= 0){
                                    GameData.cursorItem = null;
                                }
                            }
                        }else{
                            if(mb == 1){
                                if(GameData.openCrate != null && Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                    GameData.addToCrate(GameData.inventory[slot]);
                                }else{
                                    Item temp = GameData.cursorItem;
                                    GameData.cursorItem = GameData.inventory[slot];
                                    GameData.inventory[slot] = temp;
                                }
                                
                            }
                        }
                    }
                }else if(GameData.openCrate != null){
                    
                    slot = -1;
                    for(int i = 0; i < GameData.crateBoxes.size(); i++){
                        Rectangle r = GameData.crateBoxes.get(i);
                        if(r.contains(mousePos)){
                            slot = i - 10;
                            break;
                        }
                    }

                    if(slot != -1){
                        if(GameData.cursorItem == null){
                            if(GameData.openCrate.getSlot(slot).id != Item.NOTHING){
                                if(mb == 1){
                                    if(Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                        GameData.addItem(GameData.openCrate.getSlot(slot));
                                    }else GameData.cursorItem = GameData.openCrate.getSlot(slot);
                                    GameData.openCrate.setSlot(slot, new Item());
                                }
                                if(mb == 3){
                                    if(GameData.openCrate.getSlot(slot).amount == 1){
                                        GameData.cursorItem = GameData.openCrate.getSlot(slot);
                                        GameData.openCrate.setSlot(slot, new Item());
                                    }else{
                                        int diff = GameData.openCrate.getSlot(slot).amount - (GameData.openCrate.getSlot(slot).amount / 2);
                                        GameData.openCrate.setSlot(slot, new Item(GameData.openCrate.getSlot(slot).id, GameData.openCrate.getSlot(slot).amount / 2));
                                        GameData.cursorItem = new Item(GameData.openCrate.getSlot(slot).id, diff);
                                    }
                                }
                            }
                        }else{
                            if(GameData.openCrate.getSlot(slot).id == Item.NOTHING){
                                if(mb == 1){
                                    GameData.openCrate.setSlot(slot, GameData.cursorItem);
                                    GameData.cursorItem = null;
                                }
                                if(mb == 3){
                                    GameData.openCrate.setSlot(slot, new Item(GameData.cursorItem.id));
                                    GameData.cursorItem.amount--;
                                    if(GameData.cursorItem.amount <= 0){
                                        GameData.cursorItem = null;
                                    }
                                }
                            }else if(GameData.openCrate.getSlot(slot).id == GameData.cursorItem.id && GameData.openCrate.getSlot(slot).amount < 999){
                                if(mb == 1){
                                    if(Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                        GameData.addItem(GameData.openCrate.getSlot(slot));
                                    }else{
                                        int total = GameData.cursorItem.amount + GameData.openCrate.getSlot(slot).amount;
                                        if(total <= 999){
                                            int id = GameData.openCrate.getSlot(slot).id;
                                            GameData.openCrate.setSlot(slot, new Item(id, total));
                                            GameData.cursorItem = null;
                                        }else{
                                            int diff = total - 999;
                                            int id = GameData.openCrate.getSlot(slot).id;
                                            GameData.openCrate.setSlot(slot, new Item(id, 999));
                                            GameData.cursorItem.amount = diff;
                                        }
                                    }
                                    
                                }
                                if(mb == 3){
                                    int id = GameData.openCrate.getSlot(slot).id;
                                    int amount = GameData.openCrate.getSlot(slot).amount;
                                    GameData.openCrate.setSlot(slot, new Item(id, amount + 1));
                                    GameData.cursorItem.amount--;
                                    if(GameData.cursorItem.amount <= 0){
                                        GameData.cursorItem = null;
                                    }
                                }
                            }else{
                                if(mb == 1){
                                    if(Trident.getKeyDown(KeyEvent.VK_SHIFT)){
                                        GameData.addItem(GameData.openCrate.getSlot(slot));
                                    }else{
                                        Item temp = GameData.cursorItem;
                                        GameData.cursorItem = GameData.openCrate.getSlot(slot);
                                        GameData.openCrate.setSlot(slot, temp);
                                    }
                                    
                                }
                            }
                        }
                    }else if(GameData.cursorItem != null && !HUD.noDropRect.contains(mousePos)){
                        if(mb == 1){
                            GameObject.drop(GameData.cursorItem, Trident.getPlrPos());
                            GameData.cursorItem = null;
                        }
                        if(mb == 3){
                            GameObject.drop(GameData.cursorItem.id, 1, Trident.getPlrPos());
                            GameData.cursorItem.amount--;
                            if(GameData.cursorItem.amount <= 0){
                                GameData.cursorItem = null;
                            }
                        }
                    }

                }else if(GameData.cursorItem != null && !HUD.noDropRect.contains(mousePos)){
                    if(mb == 1){
                        GameObject.drop(GameData.cursorItem, Trident.getPlrPos());
                        GameData.cursorItem = null;
                    }
                    if(mb == 3){
                        GameObject.drop(GameData.cursorItem.id, 1, Trident.getPlrPos());
                        GameData.cursorItem.amount--;
                        if(GameData.cursorItem.amount <= 0){
                            GameData.cursorItem = null;
                        }
                    }
                }
            }else{
                if(!MaceBall.maceOut()){
                    int slot = -1;
                    for(int i = 0; i < GameData.invBoxes.size(); i++){
                        Rectangle r = GameData.invBoxes.get(i);
                        if(r.contains(mousePos)){
                            slot = i;
                            break;
                        }
                    }
                    if(slot >= 0 && slot <= 9){
                        GameData.selHotbar = slot;
                        
                    }
                }
                
            }
        }
        if(Trident.getCurrentScene().name.equals("title") || GameData.settingsOpen){
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                if(e instanceof TitleScreen){
                    TitleScreen t = (TitleScreen)e;
                    t.mousePressed(mb, mousePos);
                }
            }
        }
        
    }

    public static void onScroll(int scroll){
        if(MaceBall.maceOut()) return;
        if(Trident.getCurrentScene().name.equals("world") || (Trident.getCurrentScene().name.equals("tutorial") && GameData.tutorialTriggers[0])){
            if(GameData.invOpen){
                GameData.selCraft += scroll;
                GameData.selCraft = BTools.clamp(GameData.selCraft, 0, Recipe.getRecipes().size() - 1);
                Settings.playSound("data/sound/hotbarSel.wav");
            }else{
                GameData.selHotbar += scroll;
                if(GameData.selHotbar > 9) GameData.selHotbar = 0;
                if(GameData.selHotbar < 0) GameData.selHotbar = 9;
                Settings.playSound("data/sound/hotbarSel.wav");
            }
            
        }
        if(Trident.getCurrentScene().name.equals("title")){
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                if(e instanceof TitleScreen){
                    TitleScreen t = (TitleScreen)e;
                    t.mouseScrolled(scroll);
                }
            }
        }
    }
}
