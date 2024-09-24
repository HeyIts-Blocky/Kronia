package update;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

import blib.game.Player;
import blib.util.BTools;
import blib.util.Position;
import custom.Achievement;
import custom.Effect;
import custom.GameData;
import custom.Item;
import custom.MusicManager;
import custom.Recipe;
import custom.Settings;
import custom.WorldManager;
import ent.Background;
import ent.ExampleEntity;
import ent.GameObject;
import ent.HUD;
import ent.ItemRenderer;
import ent.Minimap;
import ent.PlayerRenderer;
import ent.PreviewRenderer;
import ent.TitleScreen;
import ent.TutorialBlock;
import ent.game.Crate;
import ent.game.Fire;
import ent.game.LogWall;
import ent.game.MaceBall;
import ent.game.Projectile;
import ent.game.Slime;
import ent.game.boss.Boss;
import trident.TridEntity;
import trident.Trident;
public class Update {

    public static void setup(){
        // Add custom entities to the registry here. Required in order to load them properly
        Trident.addCustomEntity(new ExampleEntity()); // Use the empty constructor
        Trident.addCustomEntity(new Background());
        Trident.addCustomEntity(new Minimap());
        Trident.addCustomEntity(new HUD());
        Trident.addCustomEntity(new ItemRenderer());
        Trident.addCustomEntity(new TitleScreen());
        Trident.addCustomEntity(new PreviewRenderer());
        Trident.addCustomEntity(new GameObject());
        Trident.addCustomEntity(new PlayerRenderer());
        Trident.addCustomEntity(new TutorialBlock());

        // Set settings
        Trident.setPlrSpeed(0.2);
        Trident.setShortCollision(true);
        Trident.setDefaultScene("title");
        Trident.setShakeLoss(0.00065);
        Trident.setShakeStrength(10);

        // Post Processing
        Trident.setBloom(0.2);
        Trident.setExposure(1);
        Trident.enableBloom = false;
        Trident.enableExposure = false;
        Trident.setLightBlur(1);

        GameData.clearInventory();
        GameData.inventory[9] = new Item(Item.WOOD, 999);

        Item.resizeImgs();
        Achievement.resizeIcons();
        Achievement.load();

        Trident.splash = new ImageIcon("data/images/intro.gif");
        Trident.drawPlayer = false;

        Settings.loadSettings();
        TitleScreen.updateButtonText();

        Trident.runCommand("credits");
        Trident.printConsole("Type 'help' to get a list of commands,");
        Trident.printConsole("or type 'customHelp' for a list of game-specific commands.");

        int chance = BTools.randInt(0, 1000);
        if(chance == 1){
            Trident.setWindowTitle("クロニア");
        }

        try {
            URL targetUrl = new URL ("https://raw.githubusercontent.com/HeyIts-Blocky/kronianews/refs/heads/main/news.txt"); 
            
            BufferedReader read = new BufferedReader(new InputStreamReader(targetUrl.openStream()));

            String input = read.readLine();
            while(input != null){

                URL news = new URL("https://raw.githubusercontent.com/HeyIts-Blocky/kronianews/refs/heads/main/" + input);
                BufferedReader read2 = new BufferedReader(new InputStreamReader(news.openStream()));

                String in2 = read2.readLine();
                TitleScreen.newsLinks.add(in2);

                in2 = read2.readLine();
                TitleScreen.newsDates.add(in2);

                in2 = read2.readLine();
                TitleScreen.newsTitles.add(in2);

                in2 = read2.readLine();
                String newsText = "";
                while(in2 != null){
                    newsText += in2 + "\n";
                    in2 = read2.readLine();
                }
                TitleScreen.newsTexts.add(newsText);

                read2.close();

                input = read.readLine();
            }
            
            read.close();
            
        } catch (MalformedURLException ex) {
            Trident.printException("Something went wrong while connecting to Github!", ex);
            ex.printStackTrace();
        }
        catch (IOException ex) {
            Trident.printException("Something went wrong while connecting to Github!", ex);
            ex.printStackTrace();
        }

        Collections.reverse(TitleScreen.newsDates);
        Collections.reverse(TitleScreen.newsTitles);
        Collections.reverse(TitleScreen.newsTexts);
    }

    public static void sceneStart(String scene){
        MusicManager.sceneChange(scene);
        Trident.resetKeys();
        HUD.clearNotif();
        if(Trident.getCurrentScene().name.equals("title")){
            int light = BTools.randInt((int)(0.1 * 255), 255);
            Trident.setDefaultLight(light);
        }
        for(int i = 0; i < GameData.tutorialTriggers.length; i++){
            GameData.tutorialTriggers[i] = false;
        }
    }
    
    public static void update(long elapsedTime){
        MusicManager.update(elapsedTime);
        if(Trident.getCurrentScene().name.equals("world") || (Trident.getCurrentScene().name.equals("tutorial") && GameData.tutorialTriggers[0])){
            Settings.applyKeybinds();
            Trident.drawPlayer = false;

            if(!GameData.canRotateCurrent()){
                GameData.rotateItem = false;
            }

            if(WorldManager.difficulty == WorldManager.V_HARD) GameData.maxDarkness = 1;
            else GameData.maxDarkness = 0.9;

            if(Background.bg == Background.SURFACE) Trident.setDefaultLight(BTools.flip((int)(GameData.getDarkness() * 255), 255));
            if(Background.bg == Background.MINES) Trident.setDefaultLight((WorldManager.difficulty >= WorldManager.HARD) ? 0 : 25);
            if(Background.bg == Background.DEEPMINES) Trident.setDefaultLight(0);
            GameData.time += elapsedTime;
            if(GameData.time > GameData.maxTime || Trident.getCurrentScene().name.equals("tutorial")) GameData.time = 0;

            if(GameData.getSelItem().getData().length >= 2){
                GameData.atkTime = GameData.getSelItem().getData()[1];
            }else{
                GameData.atkTime = 250;
            }

            int[] clamps = Background.getClampPos();
            Trident.getPlr().clampPos(clamps[0], clamps[1], clamps[2], clamps[3]);

            if(GameData.atkTimer < GameData.atkTime + 50){
                GameData.atkTimer += elapsedTime;
            }
            if(GameData.atkTimer > GameData.atkTime / 2 && !GameData.attacked){
                GameData.hungerSpeed += 0.05;
                GameData.attacked = true;
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity e = Trident.getEntities().get(i);
                    if(e instanceof GameObject){
                        GameObject go = (GameObject)e;
                        if(go.weakness != Item.T_NULL && (go.weakness == GameData.getSelItem().getType() || (go.weakness == Item.T_SWORD && (GameData.getSelItem().getType() == Item.T_PICK || GameData.getSelItem().getType() == Item.T_AXE)))){
                            double dir = BTools.getAngle(Trident.getPlrPos(), e.position);
                            double angDist = Math.abs(BTools.angleDiffRad(GameData.atkDir, dir));
                            if((angDist < Math.toRadians(90) && BTools.getDistance(Trident.getPlrPos(), e.position) < 80) || BTools.getDistance(Trident.getPlrPos(), e.position) < 32){
                                int dmg = GameData.getSelItem().getData()[0];
                                if(go.weakness == Item.T_SWORD && GameData.getSelItem().getType() != Item.T_SWORD) dmg /= 2;
                                if(GameData.hasEffect(Effect.STRENGTH) && go.weakness == Item.T_SWORD) dmg *= 2;
                                go.damage(dmg);
                            }
                            
                        }
                    }
                }
            }
            if(!MaceBall.maceOut() && !GameData.spectate && Trident.getMouseDown(1) && GameData.atkTimer >= GameData.atkTime + 50 && GameData.inventory[GameData.selHotbar].id != Item.NOTHING && !GameData.invOpen){
                int hotbarSlot = -1;
                for(int i = 0; i < GameData.invBoxes.size(); i++){
                    Rectangle r = GameData.invBoxes.get(i);
                    if(r.contains(Trident.mousePos)){
                        hotbarSlot = i;
                        break;
                    }
                }
                if(!(hotbarSlot >= 0 && hotbarSlot <= 9)){
                    GameData.atkTimer = 0;
                    GameData.atkDir = BTools.getAngle(Trident.getPlrPos(), Trident.mouseWorldPos);
                    GameData.attacked = false;
                    Settings.playSound("data/sound/attack.wav");
        
                    GameData.hungerSpeed += 0.05;
        
                    // Can be used?
                    try{
                        if(GameData.getSelItem().getType() == Item.T_PLACEABLE && BTools.getDistance(Trident.getPlrPos(), Trident.mouseWorldPos) < 128){
                            Position position = Trident.mouseWorldPos;

                            if(GameData.getSelItem().id == Item.LOGWALL){
                                // look for snap points
                                for(int i = 0; i < Trident.getEntities().size(); i++){
                                    TridEntity e = Trident.getEntities().get(i);
                                    if(e instanceof LogWall){
                                        LogWall wall = (LogWall)e;
                                        if(wall.rotated == GameData.rotateItem){
                                            if(wall.rotated){
                                                // check up & down
                                                double tDist = 999, bDist = 999;
                                                Position top = new Position(e.position.x, e.position.y - 32);
                                                Position bottom = new Position(e.position.x, e.position.y + 32);
                                                if(BTools.getDistance(top, Trident.mouseWorldPos) < 16){
                                                    tDist = BTools.getDistance(top, Trident.mouseWorldPos);
                                                }
                                                if(BTools.getDistance(bottom, Trident.mouseWorldPos) < 16){
                                                    bDist = BTools.getDistance(bottom, Trident.mouseWorldPos);
                                                }
                                                if(Math.min(tDist, bDist) <= 16){
                                                    if(tDist < bDist) position = top;
                                                    else position = bottom;
                                                }
                                            }else{
                                                // check left & right
                                                double lDist = 999, rDist = 999;
                                                Position left = new Position(e.position.x - 64, e.position.y);
                                                Position right = new Position(e.position.x + 64, e.position.y);
                                                if(BTools.getDistance(left, Trident.mouseWorldPos) < 16){
                                                    lDist = BTools.getDistance(left, Trident.mouseWorldPos);
                                                }
                                                if(BTools.getDistance(right, Trident.mouseWorldPos) < 16){
                                                    rDist = BTools.getDistance(right, Trident.mouseWorldPos);
                                                }
                                                if(Math.min(lDist, rDist) <= 16){
                                                    if(lDist < rDist) position = left;
                                                    else position = right;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            GameObject obj = GameObject.placeObj(position, GameData.getSelItem().getData()[0], new int[]{(GameData.rotateItem ? 1 : 0)});
                            if(!obj.getCollision().intersects(Trident.getPlr().getCollision())){
                                for(Rectangle r: Trident.getCollision()){
                                    if(r.intersects(obj.getCollision())) return;
                                }
                                GameData.getSelItem().amount--;
                                Trident.spawnEntity(obj);
                            }
                            GameData.hungerSpeed += 0.05;
                        }
                        if(GameData.getSelItem().getType() == Item.T_CONSUMABLE && GameData.hunger < GameData.maxHunger){
                            if(!(Trident.getCurrentScene().name.equals("tutorial") && GameData.getSelItem().id == Item.RAWMEAT)){
                                if(GameData.getSelItem().id == Item.RAWMEAT){
                                    Achievement.get(Achievement.RAWMEAT);
                                }
                                GameData.getSelItem().amount--;
                                int[] data = GameData.getSelItem().getData();
                                GameData.hunger += data[0];
                                if(data.length >= 3){
                                    GameData.health += data[2];
                                }
                                if(data.length >= 4){
                                    if(data[3] != Effect.NONE){
                                        if(data.length >= 5) GameData.addEffect(new Effect(data[3], data[4] * 100));
                                        else GameData.addEffect(new Effect(data[3]));
                                    }
                                    
                                }
                                if(GameData.hunger >= GameData.maxHunger){
                                    GameData.hunger = GameData.maxHunger;
                                }
                                GameData.hungerSpeed = 0;
            
                                if(Trident.getCurrentScene().name.equals("tutorial")){
                                    for(int i = 0; i < Trident.getEntities().size(); i++){
                                        TridEntity e = Trident.getEntities().get(i);
                                        if(e instanceof TutorialBlock && ((TutorialBlock)e).id == 0){
                                            Trident.destroy(e);
                                            break;
                                        }
                                    }
                                }
                            }
                            
                        }
                        if(GameData.getSelItem().getType() == Item.T_BOSS_SPAWN && !Boss.hasBoss() && Boss.canSpawn(GameData.getSelItem().getData()[0])){
                            GameData.getSelItem().amount--;
                            int ang = BTools.randInt(0, 360);
                            double dir = Math.toRadians(ang);
                            Position pos = BTools.angleToVector(dir);
                            pos.x *= 800;
                            pos.y *= 800;
                            pos.x += Trident.getPlrPos().x;
                            pos.y += Trident.getPlrPos().y;
                            Trident.spawnEntity(GameObject.placeObj(pos, GameData.getSelItem().getData()[0], null));
                            Settings.playSound("data/sound/bossSpawn.wav");
                            if(Settings.camShake) Trident.shakeCam(1);
                        }
                        if(GameData.getSelItem().getType() == Item.T_RANGED){
                            int ammoType = GameData.getSelItem().getData()[0];
                            int slot = -1;
                            for(int i = 0; i < GameData.inventory.length; i++){
                                Item item = GameData.inventory[i];
                                if(item.getType() == Item.T_AMMO && item.getData()[0] == ammoType){
                                    // found it!
                                    slot = i;
                                    break;
                                }
                            }
                            if(slot != -1){
                                GameData.inventory[slot].amount--;
                                Item item = GameData.inventory[slot];
                                int ang = (int)(Math.toDegrees(GameData.atkDir) * 10);
                                Trident.spawnEntity(new Projectile(Trident.getPlrPos(), new int[]{ang, item.id, item.getData()[2], item.getData()[3], item.getData()[4]}));
                            }
                        }
                        if(GameData.getSelItem().getType() == Item.T_TRIGGER){
                            trigger(GameData.getSelItem().getData()[0]);
                        }
                        if(GameData.getSelItem().getType() == Item.T_EFFECT){
                            GameData.getSelItem().amount--;
                            int id = GameData.getSelItem().getData()[0];
                            long length = -1;
                            if(GameData.getSelItem().getData().length >= 3){
                                length = GameData.getSelItem().getData()[2] * 100;
                            }
                            if(length == -1) GameData.addEffect(new Effect(id));
                            else GameData.addEffect(new Effect(id, length));
                        }
                        if(GameData.getSelItem().getType() == Item.T_MACE){
                            int ang = (int)(Math.toDegrees(GameData.atkDir) * 10);
                            Trident.spawnEntity(new MaceBall(Trident.getPlrPos(), new int[]{ang, GameData.getSelItem().getData()[0], GameData.getSelItem().getData()[2], GameData.getSelItem().getData()[3]}));
                        }
                    }catch(Exception e){
                        Trident.printException("Problem while using item!", e);
                    }
                    
                    
                }
            }
            
            GameData.selCraft = BTools.clamp(GameData.selCraft, 0, Recipe.getRecipes().size() - 1);
            GameData.checkInventory();

            for(int i = 0; i < GameData.effects.size(); i++){
                Effect eff = GameData.effects.get(i);
                eff.update(elapsedTime);
                if(eff.time >= eff.maxTime){
                    GameData.effects.remove(i);
                    i--;
                }
            }

            if(GameData.invOpen){
                // get the closest crate that's in range
                Crate crate = null;
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity ent = Trident.getEntities().get(i);
                    if(ent instanceof Crate){
                        if(BTools.getDistance(Trident.getPlrPos(), ent.position) < 200){
                            if(crate == null) crate = (Crate)ent;
                            else{
                                if(BTools.getDistance(Trident.getPlrPos(), ent.position) < BTools.getDistance(Trident.getPlrPos(), crate.position)){
                                    crate = (Crate)ent;
                                }
                            }
                        }
                    }
                }

                GameData.openCrate = crate;

                // check right click (craft fast)
                if(Trident.getMouseDown(3)){
                    GameData.craftTimer -= elapsedTime;
                    if(GameData.craftTimer <= 0){
                        GameData.craftTimer = GameData.craftTime;

                        // check mouse is in crafting slot, do crafting logic
                        int slot = -1;
                        for(int i = 0; i < GameData.invBoxes.size(); i++){
                            Rectangle r = GameData.invBoxes.get(i);
                            if(r.contains(Trident.mousePos)){
                                slot = i;
                                break;
                            }
                        }
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
                        }
                    }
                }
            }

            if(GameData.hurtTime > 0) GameData.hurtTime -= elapsedTime;
            if(GameData.health < GameData.maxHealth && GameData.health > 0){
                GameData.healTime -= elapsedTime;
                if(GameData.healTime <= 0){
                    GameData.healTime = 750;
                    if(GameData.hunger > 25) GameData.health++;
                    GameData.hunger--;
                    GameData.hungerSpeed += 0.05;
                }
            }
            if(GameData.health > GameData.maxHealth) GameData.health = GameData.maxHealth;
            if(GameData.hunger > GameData.maxHunger) GameData.hunger = GameData.maxHunger;
            if(GameData.health <= 0){
                if(Trident.getCurrentScene().name.equals("tutorial")){
                    Trident.loadScene("title");
                    return;
                }
                for(Item i: GameData.inventory){
                    if(i.id != Item.NOTHING) GameObject.dropNoDelay(i, Trident.getPlrPos());
                }
                GameData.clearInventory();
                GameData.effects = new ArrayList<Effect>();

                if(!GameData.spectate){
                    GameData.spectate = true;
                    GameData.deadTime = 10000;
                    GameData.atkTimer = GameData.atkTime + 50;
                    GameData.invOpen = false;
                    GameData.lastDeath = Trident.getPlrPos();
                    Trident.setPlrSpeed(0);
                    Settings.playSound("data/sound/death.wav");
                }

                GameData.deadTime -= elapsedTime;

                if(GameData.deadTime <= 0){
                    GameData.spectate = false;
                    Trident.setPlrSpeed(0.2);

                    Background.changeBackground(Background.SURFACE);
                    Position pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
                    Trident.setPlrPos(pos);
                    for(int i = 0; i < Trident.getEntities().size(); i++){
                        TridEntity e = Trident.getEntities().get(i);
                        if(e instanceof GameObject){
                            if(e.HASCOLLISION && e.getCollision().intersects(Trident.getPlr().getCollision())){
                                ((GameObject)e).damage(((GameObject)e).health);
                            }
                        }
                    }
                    GameData.health = 50;
                    GameData.hunger = 100;
                    GameData.hungerSpeed = 0;

                    GameData.addItem(new Item(Item.W_AXE));
                }
            }

            if(Trident.getCurrentScene().name.equals("tutorial") && GameData.tutorialTriggers[3] && WorldManager.numEnemies() == 0){
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity e = Trident.getEntities().get(i);
                    if(e instanceof TutorialBlock && ((TutorialBlock)e).id == 1){
                        Trident.destroy(e);
                        break;
                    }
                }
            }

            double hungerMult = 1;
            switch(WorldManager.difficulty){
            case WorldManager.V_EASY:
                hungerMult = 0.5;
                break;
            case WorldManager.EASY:
                hungerMult = 0.75;
                break;
            case WorldManager.HARD:
                hungerMult = 1.5;
                break;
            case WorldManager.V_HARD:
                hungerMult = 2;
                break;
            }
            GameData.hungerTimer += (long)(elapsedTime * GameData.hungerSpeed * hungerMult);
            if(GameData.hungerTimer >= GameData.hungerTime){
                GameData.hungerTimer = 0;
                GameData.hunger--;
                if(GameData.hunger <= 0 && WorldManager.difficulty != WorldManager.V_EASY){
                    GameData.damage(5);
                }
            }
            if(GameData.hunger < 0) GameData.hunger = 0;

            if(GameData.hungerSpeed > 2) GameData.hungerSpeed = 2;

            WorldManager.checkWorld(elapsedTime);
        }
        if(Trident.getCurrentScene().name.equals("title")){
            GameData.settingsOpen = false;
            Trident.setPlrSpeed(0);
            Trident.getPlr().smoothType = Player.NOSMOOTH;
        }else{
            if(Settings.camSmooth) Trident.getPlr().smoothType = Player.BASICSMOOTH;
            else Trident.getPlr().smoothType = Player.NOSMOOTH;
            if(GameData.health > 0) Trident.setPlrSpeed(0.2);
        }
    }

    public static void trigger(int id){
        // tutorial
        if(id == -1 && !GameData.tutorialTriggers[0]){
            GameData.clearInventory();
            Trident.spawnEntity(new HUD(new Position()));
            Trident.spawnEntity(new PreviewRenderer(new Position()));
            Trident.spawnEntity(new ItemRenderer(new Position()));

            GameData.tutorialTriggers[0] = true;
        }
        if(id == -2 && !GameData.tutorialTriggers[1]){
            GameData.addItem(new Item(Item.I_AXE));

            GameData.tutorialTriggers[1] = true;
        }
        if(id == -3 && !GameData.tutorialTriggers[2]){
            GameData.hunger = 0;
            GameData.addItem(new Item(Item.W_SWORD));

            GameData.tutorialTriggers[2] = true;
        }
        if(id == -4 && !GameData.tutorialTriggers[3]){
            GameData.health = 100;
            GameData.hungerSpeed = 0;
            Trident.spawnEntity(new Slime(new Position(960, 1960)));

            GameData.tutorialTriggers[3] = true;
        }
        if(id == -5){
            Achievement.get(Achievement.TUTORIAL);
            Trident.loadScene("title");
        }


        // items
        if(id == 1 && Background.bg == Background.SURFACE){
            Background.changeBackground(Background.MINES);
            Achievement.get(Achievement.MINES);
        }
        if(id == 2 && Background.bg == Background.MINES){
            GameData.getSelItem().amount--;
            Background.changeBackground(Background.SURFACE);
        }
        if(id == 3 && GameData.health < GameData.maxHealth){
            GameData.getSelItem().amount--;
            GameData.health = Math.min(GameData.maxHealth, GameData.health + 5);
        }
        if(id == 4 && Background.bg == Background.MINES){
            Background.changeBackground(Background.DEEPMINES);
            Achievement.get(Achievement.DEEPMINES);
        }
    }

    public static int command(ArrayList<String> cmdParts){ // cmdParts.get(0) is the command, while the rest are arguments for the command.
        switch(cmdParts.get(0)){
            case "helloWorld":
                Trident.printConsole("Hello, World!");
                return 0;
            case "ping":
                Trident.printConsole("pong");
                return 0;
            case "newWorld":
                WorldManager.newWorld(cmdParts.get(1), WorldManager.defaultDiff);
                return 0;
            case "loadWorld":
                WorldManager.loadWorld(cmdParts.get(1));
                return 0;
            case "saveWorld":
                WorldManager.saveWorld();
                return 0;
            case "clearInv":
                GameData.clearInventory();
                return 0;
            case "give":
                if(Integer.parseInt(cmdParts.get(1)) >= Item.names.length || Integer.parseInt(cmdParts.get(1)) < 1){
                    Trident.printConsole("Item ID " + Integer.parseInt(cmdParts.get(1)) + " is out of bounds!");
                    return 0;
                }
                GameData.addItem(new Item((short)Integer.parseInt(cmdParts.get(1)), Integer.parseInt(cmdParts.get(2))));
                return 0;
            case "fillMeWithWood":
                while(GameData.canAdd(new Item(Item.WOOD, 999))){
                    GameData.addItem(new Item(Item.WOOD, 999));
                }
                return 0;
            case "damage":
                GameData.damage(Integer.parseInt(cmdParts.get(1)));
                return 0;
            case "setHealth":
                GameData.health = Integer.parseInt(cmdParts.get(1));
                return 0;
            case "setTime":
                GameData.time = Long.parseLong(cmdParts.get(1));
                return 0;
            case "itemList":
                int p = 1;
                if(cmdParts.size() > 1) p = Integer.parseInt(cmdParts.get(1));
                printItems(p);
                return 0;
            case "searchItem":
                if(cmdParts.size() > 1){
                    String search = cmdParts.get(1).toUpperCase();
                    ArrayList<Integer> results = new ArrayList<Integer>();
                    for(int i = 0; i < Item.names.length; i++){
                        if(results.size() >= 10) break;
                        String s = Item.names[i];
                        if(s.toUpperCase().contains(search)){
                            results.add(i);
                        }
                    }

                    Trident.printConsole("Search results for: \"" + cmdParts.get(1) + "\"");
                    Trident.printConsole(" --- ");
                    Trident.printConsole("");

                    if(results.size() == 0){
                        Trident.printConsole(" -- NO RESULTS --");
                    }else{
                        for(int i: results){
                            Trident.printConsole("} " + Item.names[i] + " - " + i);
                        }
                    }

                    Trident.printConsole("");
                }
                return 0;
            case "song":
                Trident.printConsole("The current song is called \"" + MusicManager.lastName + "\"");
                return 0;
            case "difficulty":
                if(cmdParts.size() == 1){
                    String diff = "UNKNOWN";
                    switch(WorldManager.difficulty){
                    case WorldManager.V_EASY:
                        diff = "veasy";
                        break;
                    case WorldManager.EASY:
                        diff = "easy";
                        break;
                    case WorldManager.NORMAL:
                        diff = "normal";
                        break;
                    case WorldManager.HARD:
                        diff = "hard";
                        break;
                    case WorldManager.V_HARD:
                        diff = "vhard";
                        break;
                    }

                    Trident.printConsole("Current difficulty: " + diff);
                }else{
                    if(cmdParts.get(1).equals("veasy")) WorldManager.difficulty = WorldManager.V_EASY;
                    else if(cmdParts.get(1).equals("easy")) WorldManager.difficulty = WorldManager.EASY;
                    else if(cmdParts.get(1).equals("normal")) WorldManager.difficulty = WorldManager.NORMAL;
                    else if(cmdParts.get(1).equals("hard")) WorldManager.difficulty = WorldManager.HARD;
                    else if(cmdParts.get(1).equals("vhard")) WorldManager.difficulty = WorldManager.V_HARD;
                    else{
                        Trident.printConsole("Unknown Difficulty: " + cmdParts.get(1));
                        return 0;
                    }
                    Trident.printConsole("Set difficulty to " + cmdParts.get(1));
                }
                return 0;
            case "defaultDiff":
                if(cmdParts.size() == 1){
                    String diff = "UNKNOWN";
                    switch(WorldManager.defaultDiff){
                    case WorldManager.V_EASY:
                        diff = "veasy";
                        break;
                    case WorldManager.EASY:
                        diff = "easy";
                        break;
                    case WorldManager.NORMAL:
                        diff = "normal";
                        break;
                    case WorldManager.HARD:
                        diff = "hard";
                        break;
                    case WorldManager.V_HARD:
                        diff = "vhard";
                        break;
                    }

                    Trident.printConsole("Current default difficulty: " + diff);
                }else{
                    if(cmdParts.get(1).equals("veasy")) WorldManager.defaultDiff = WorldManager.V_EASY;
                    else if(cmdParts.get(1).equals("easy")) WorldManager.defaultDiff = WorldManager.EASY;
                    else if(cmdParts.get(1).equals("normal")) WorldManager.defaultDiff = WorldManager.NORMAL;
                    else if(cmdParts.get(1).equals("hard")) WorldManager.defaultDiff = WorldManager.HARD;
                    else if(cmdParts.get(1).equals("vhard")) WorldManager.defaultDiff = WorldManager.V_HARD;
                    else{
                        Trident.printConsole("Unknown Difficulty: " + cmdParts.get(1));
                        return 0;
                    }
                    Trident.printConsole("Set default difficulty to " + cmdParts.get(1));
                }
                return 0;
            case "toggleHud":
                GameData.drawHud = !GameData.drawHud;
                return 0;
            case "notifTest":
                int type = Integer.parseInt(cmdParts.get(1));
                HUD.addNotif("Test notification", type);
                Trident.consoleOpen = false;
                return 0;
            case "lightFire":
                GameObject obj = null;
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity ent = Trident.getEntities().get(i);
                    if(ent instanceof GameObject){
                        if(obj == null || BTools.getDistance(obj.position, Trident.getPlrPos()) > BTools.getDistance(ent.position, Trident.getPlrPos())){
                            obj = (GameObject)ent;
                        }
                    }
                }
                if(obj != null){
                    Fire.setOnFire(obj, 10000);
                }
                return 0;
            case "numEnt":
                Trident.printConsole("Entities: " + Trident.getEntities().size());
                return 0;
            case "ramUsage":
                Runtime runtime = Runtime.getRuntime();

                NumberFormat format = NumberFormat.getInstance();

                long maxMemory = runtime.maxMemory();
                long allocatedMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();

                Trident.printConsole(" -------- ");
                Trident.printConsole("free memory: " + format.format(freeMemory / 1024 / 1024) + " MB\n");
                Trident.printConsole("allocated memory: " + format.format(allocatedMemory / 1024 / 1024) + " MB  <--- This is seen in the task manager\n");
                Trident.printConsole("used memory: " + format.format((allocatedMemory - freeMemory) / 1024 / 1024) + " MB  <--- This is what's being used by objects and the such");
                Trident.printConsole("max memory: " + format.format(maxMemory / 1024 / 1024) + " MB\n");
                Trident.printConsole("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024) + " MB\n");
                Trident.printConsole(" -------- ");
                return 0;
            case "cleanRam":
                System.gc();
                
                Trident.runCommand("ramUsage");
                return 0;
        }
        return 1; // return 1 if command is not recognized
    }
    public static String[] commands = { // Fill this with the format for all custom commands
        "helloWorld",
        "ping",
        "newWorld",
        "loadWorld <worldName>",
        "saveWorld",
        "clearInv",
        "give <id> <amount>",
        "fillMeWithWood",
        "damage <amount>",
        "setHealth <health>",
        "setTime <time>",
        "song",
        "itemList [page]",
        "searchItem <name>",
        "difficulty [veasy/easy/normal/hard/vhard]",
        "defaultDiff [veasy/easy/normal/hard/vhard]",
        "toggleHud",
        "notifTest <notifType>",
        "lightFire",
        "numEnt",
        "ramUsage",
        "cleanRam", 
    };

    public static void printItems(int page){
        int startIndex = (page - 1) * 10;
        if(startIndex > Item.names.length - 1){
            Trident.printConsole("page beyond bounds: " + page);
            Trident.printConsole("number of pages: " + (Item.names.length / 10 + ((Item.names.length % 10 != 0) ? 1 : 0)));
            return;
        }
        
        Trident.printConsole("-- ITEMS --");
        Trident.printConsole("Page " + page + " of " + (Item.names.length / 10 + ((Item.names.length % 10 != 0) ? 1 : 0)));
        Trident.printConsole("");
        for(int i = startIndex; (i < Item.names.length && i < startIndex + 10); i++){
            Trident.printConsole("} " + Item.names[i] + " - " + i);
        }
        Trident.printConsole("");
    }
}
