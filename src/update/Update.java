package update;

import trident.*;
import ent.*;
import java.util.ArrayList;

import blib.game.Player;
import blib.util.*;
import custom.*;
import ent.game.*;
import javax.swing.*;
import java.awt.*;
import ent.game.boss.*;
public class Update {

    public static void setup(){
        // Add custom entities to the registry here. Required in order to load them properly
        Trident.addCustomEntity(new ExampleEntity()); // Use the empty constructor
        Trident.addCustomEntity(new Background());
        Trident.addCustomEntity(new Minimap());
        Trident.addCustomEntity(new Hotbar());
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

        Trident.splash = new ImageIcon("data/images/intro.gif");
        Trident.drawPlayer = false;

        Settings.loadSettings();
        TitleScreen.updateButtonText();
    }

    public static void sceneStart(String scene){
        MusicManager.sceneChange(scene);
        Trident.resetKeys();
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

            if(Background.bg == Background.SURFACE) Trident.setDefaultLight(BTools.flip((int)(GameData.getDarkness() * 255), 255));
            if(Background.bg == Background.MINES) Trident.setDefaultLight(25);
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
                        if(go.weakness != Item.T_NULL && (go.weakness == GameData.getSelItem().getType() || go.weakness == Item.T_SWORD)){
                            double dir = BTools.getAngle(Trident.getPlrPos(), e.position);
                            double angDist = Math.abs(BTools.angleDiffRad(GameData.atkDir, dir));
                            if(angDist < Math.toRadians(45) && BTools.getDistance(Trident.getPlrPos(), e.position) < 64){
                                int dmg = GameData.getSelItem().getData()[0];
                                if(go.weakness == Item.T_SWORD && GameData.getSelItem().getType() != Item.T_SWORD) dmg /= 2;
                                go.damage(dmg);
                            }
                            
                        }
                    }
                }
            }
            if(!GameData.spectate && Trident.getMouseDown(1) && GameData.atkTimer >= GameData.atkTime + 50 && GameData.inventory[GameData.selHotbar].id != Item.NOTHING && !GameData.invOpen){
                GameData.atkTimer = 0;
                GameData.atkDir = BTools.getAngle(Trident.getPlrPos(), Trident.mouseWorldPos);
                GameData.attacked = false;
                Settings.playSound("data/sound/attack.wav");

                GameData.hungerSpeed += 0.05;

                // Can be used?
                if(GameData.getSelItem().getType() == Item.T_PLACEABLE && BTools.getDistance(Trident.getPlrPos(), Trident.mouseWorldPos) < 128){
                    GameObject obj = GameObject.placeObj(Trident.mouseWorldPos, GameData.getSelItem().getData()[0], new int[]{(GameData.rotateItem ? 1 : 0)});
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
                                if(e instanceof TutorialBlock){
                                    Trident.destroy(e);
                                    break;
                                }
                            }
                        }
                    }
                    
                }
                if(GameData.getSelItem().getType() == Item.T_BOSS_SPAWN && !Boss.hasBoss()){
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
                        Trident.spawnEntity(new Projectile(Trident.getPlrPos(), new int[]{ang, item.id, item.getData()[2], item.getData()[3]}));
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
                Trident.loadScene("title");
            }

            GameData.hungerTimer += (long)(elapsedTime * GameData.hungerSpeed);
            if(GameData.hungerTimer >= GameData.hungerTime){
                GameData.hungerTimer = 0;
                GameData.hunger--;
                if(GameData.hunger <= 0){
                    GameData.damage(5);
                }
            }
            if(GameData.hunger < 0) GameData.hunger = 0;

            if(GameData.hungerSpeed > 2) GameData.hungerSpeed = 2;

            WorldManager.checkWorld(elapsedTime);
        }
        if(Trident.getCurrentScene().name.equals("title")){
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
            Trident.spawnEntity(new Hotbar(new Position()));
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


        // items
        if(id == 1 && Background.bg == Background.SURFACE){
            Background.changeBackground(Background.MINES);
        }
        if(id == 2 && Background.bg == Background.MINES){
            GameData.getSelItem().amount--;
            Background.changeBackground(Background.SURFACE);
        }
    }

    public static int command(ArrayList<String> cmdParts){ // cmdParts.get(0) is the command, while the rest are arguments for the command.
        switch(cmdParts.get(0)){
        case "helloWorld":
            System.out.println("Hello, World!");
            return 0;
        case "ping":
            System.out.println("pong");
            return 0;
        case "newWorld":
            WorldManager.newWorld(cmdParts.get(1));
            return 0;
        case "loadWorld":
            WorldManager.loadWorld(cmdParts.get(1));
            return 0;
        case "saveWorld":
            WorldManager.saveWorld();
            return 0;
        case "clear":
            GameData.clearInventory();
            return 0;
        case "give":
            GameData.addItem(new Item(Integer.parseInt(cmdParts.get(1)), Integer.parseInt(cmdParts.get(2))));
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
        case "song":
            JOptionPane.showMessageDialog(null, "The current song is called \"" + MusicManager.lastName + "\"", "Kronia", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        return 1; // return 1 if command is not recognized
    }
}
