package custom;

import java.io.*;
import trident.*;
import ent.*;
import blib.util.*;
import ent.game.*;
import java.util.ArrayList;
import blib.bson.*;
import ent.game.boss.*;
import java.awt.*;
public class WorldManager {

    private static final int DEFAULT_ENTITIES = 2000, MAX_ENTITIES = 8000; // default is per dimension, max is overall
    private static long spawnTimer = 0, spawnTime = 60000;
    private static long enemyTimer = 0, enemyTime = 30000;

    public static int enemyCap = 3;

    public static String worldName = "";
    public static int difficulty = 2;
    public static final int V_EASY = 0, EASY = 1, NORMAL = 2, HARD = 3, V_HARD = 4;
    public static int defaultDiff = NORMAL;

    public static void checkWorld(long elapsedTime){
        if(Trident.getCurrentScene().name.equals("tutorial")) return;
        if(spawnTimer > 0) spawnTimer -= elapsedTime;
        if(spawnTimer <= 0 && Trident.getEntities().size() < MAX_ENTITIES){
            spawnEnt();
            spawnTimer = spawnTime;
        }
        boolean spawnEnemies = true;
        if(WorldManager.difficulty <= WorldManager.EASY && GameData.getDarkness() <= 0.5) spawnEnemies = false; 
        if(enemyTimer < enemyTime) enemyTimer += elapsedTime;
        int enemyCap = 3;
        switch(WorldManager.difficulty){
        case WorldManager.V_EASY:
            enemyCap = 1;
            break;
        case WorldManager.EASY:
            enemyCap = 2;
            break;
        case WorldManager.HARD:
            enemyCap = 10;
            break;
        case WorldManager.V_HARD:
            enemyCap = 20;
            break;
        }
        if(enemyTimer >= enemyTime && numEnemies() < enemyCap){
            enemyTimer = 0;
            // spawn an enemy
            int ang = BTools.randInt(0, 360);
            double dir = Math.toRadians(ang);
            Position vec = BTools.angleToVector(dir);
            vec.x *= 700;
            vec.y *= 700;
            vec.x += Trident.getPlrPos().x;
            vec.y += Trident.getPlrPos().y;

            if(Background.bg == Background.SURFACE){
                int type = BTools.randInt(0, 1);
                switch(type){
                case 0:
                    Trident.spawnEntity(new Slime(vec));
                    break;
                }
            }
            if(Background.bg == Background.MINES){
                int type = BTools.randInt(0, 2);
                switch(type){
                case 0:
                    Trident.spawnEntity(new CaveSlime(vec));
                    break;
                case 1:
                    Trident.spawnEntity(new Mole(vec));
                    break;
                }
            }
            
        }
    }

    public static int numEnemies(){
        int[] enemies = {
            GameObject.SLIME,
            GameObject.CAVESLIME,
            GameObject.MOLE,
            GameObject.BABYSLIME,
        };

        int num = 0;
        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(e instanceof GameObject){
                GameObject go = (GameObject)e;
                for(int j: enemies){
                    if(go.id == j){
                        num++;
                        break;
                    }
                }
            }
        }

        return num;
    }
    
    public static void newWorld(String name, int diff){
        worldName = name;
        GameData.clearInventory();
        difficulty = diff;
        for(int i = 0; i < DEFAULT_ENTITIES; i++){
            spawnEnt();
        }
        GameData.addItem(new Item(Item.W_AXE));
        Position pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
        Trident.setPlrPos(pos);
        Rectangle plrColl = Trident.getPlr().getCollision();
        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(e.HASCOLLISION){
                if(plrColl.intersects(e.getCollision())){
                    Trident.destroy(e);
                    i--;
                }
            }
        }
        GameData.health = 100;
        GameData.maxHealth = 100;
        GameData.hunger = 100;
        GameData.maxHunger = 100;
        GameData.hungerSpeed = 0;
        GameData.hungerTimer = 0;
        spawnTimer = -(spawnTime * 10);
        GameData.time = 0;
        Background.bg = Background.SURFACE;
        saveWorld();
    }

    private static void spawnEnt(){

        // Spawn Surface
        Position pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
        int type = BTools.randInt(0, 5);
        if(type == 0){
            Trident.spawnEntity(new Tree(pos));
        }
        if(type == 1){
            Trident.spawnEntity(new Rock(pos));
        }
        if(type == 2){
            Trident.spawnEntity(new Cow(pos));
        }
        if(type == 3){
            Trident.spawnEntity(new Tree(pos));
            Trident.spawnEntity(new Tree(new Position(pos.x - BTools.randInt(10, 50), pos.y - BTools.randInt(10, 50))));
            Trident.spawnEntity(new Tree(new Position(pos.x + BTools.randInt(10, 50), pos.y - BTools.randInt(10, 50))));
            Trident.spawnEntity(new Tree(new Position(pos.x - BTools.randInt(10, 50), pos.y + BTools.randInt(10, 50))));
            Trident.spawnEntity(new Tree(new Position(pos.x + BTools.randInt(10, 50), pos.y + BTools.randInt(10, 50))));
        }
        if(type == 4){
            Trident.spawnEntity(new CoalOre(pos));
        }

        // Spawn Mines
        pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
        pos.x += Background.OFFSET;
        type = BTools.randInt(0, 3);
        if(type == 0){
            Trident.spawnEntity(new CaveRock(pos));
        }
        if(type == 1){
            Trident.spawnEntity(new IronOre(pos));
        }
        if(type == 2){
            Trident.spawnEntity(new CopperOre(pos));
        }
    }

    public static void saveWorld(){
        if(Trident.getCurrentScene().name.equals("tutorial")) return;
        try{
            File file = new File("data/worlds/" + worldName + ".bson");
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println("double x " + Trident.getPlrPos().x);
            writer.println("double y " + Trident.getPlrPos().y);
            writer.println("int health " + GameData.health);
            writer.println("int maxHP " + GameData.maxHealth);
            writer.println("int hunger " + GameData.hunger);
            writer.println("int maxHunger " + GameData.maxHunger);
            writer.println("double hungerSpeed " + GameData.hungerSpeed);
            writer.println("long hungerTime " + GameData.hungerTime);
            writer.println("long time " + GameData.time);
            writer.println("int dimension " + Background.bg);
            writer.println("int difficulty " + difficulty);
            writer.println("{ entities");
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                if(e instanceof GameObject){
                    if(e instanceof Boss) continue;
                    GameObject go = (GameObject)e;
                    if(go.id == -1) continue;
                    writer.println("int " + go.id); // id
                    writer.println("double " + go.position.x);
                    writer.println("double " + go.position.y);
                    writer.println("int " + go.health);
                    writer.println("int " + go.data.length);
                    for(int j = 0; j < go.data.length; j++){
                        writer.println("int " + go.data[j]);
                    }
                }
            }
            writer.println("}");
            writer.println("{ inventory");
            for(Item i: GameData.inventory){
                writer.println("int " + i.id);
                writer.println("int " + i.amount);
            }
            writer.println("}");
            writer.println("{ effects");
            for(Effect eff: GameData.effects){
                writer.println("int " + eff.id);
                writer.println("long " + eff.time);
                writer.println("long " + eff.maxTime);
                writer.println("long " + eff.coolTimer);
                writer.println("long " + eff.cooldown);
            }
            writer.println("}");
            writer.close();
        }catch(Exception e){
            Trident.printException("Something went wrong while saving world!", e);
            Trident.loadScene("title");
        }
    }

    public static void loadWorld(String name){
        worldName = name;
        Trident.setupScenes();
        Trident.loadScene("world");
        GameData.clearInventory();
        try{
            ArrayList<BSonObject> objects = BSonParser.readFile("data/worlds/" + worldName + ".bson");
            try{
                BSonObject obj = BSonParser.getObject("difficulty", objects);
                difficulty = obj.getInt();
            }catch(Exception e){
                Trident.printExceptionSilent("Error loading difficulty. Likely an old save.", e);
            }
            BSonObject obj = BSonParser.getObject("entities", objects);
            BSonList asList = (BSonList)obj;
            for(int i = 0; i < asList.list.size(); i++){
                int id = asList.list.get(i).getInt();
                i++;
                double x = asList.list.get(i).getDouble();
                i++;
                double y = asList.list.get(i).getDouble();
                i++;
                int hp = asList.list.get(i).getInt();
                i++;
                int numData = asList.list.get(i).getInt();
                i++;
                int[] data = new int[numData];
                for(int j = 0; j < numData; j++){
                    data[j] = asList.list.get(i).getInt();
                    i++;
                }
                i--;

                Trident.spawnEntity(GameObject.mkObj(new Position(x, y), id, hp, data));
            }
            obj = BSonParser.getObject("inventory", objects);
            asList = (BSonList)obj;
            for(int i = 0; i < asList.list.size(); i += 2){
                int id = asList.list.get(i).getInt();
                int amount = asList.list.get(i + 1).getInt();
                GameData.inventory[i / 2] = new Item(id, amount);
            }
            obj = BSonParser.getObject("x", objects);
            double x = obj.getDouble();
            obj = BSonParser.getObject("y", objects);
            double y = obj.getDouble();
            obj = BSonParser.getObject("health", objects);
            GameData.health = obj.getInt();
            obj = BSonParser.getObject("maxHP", objects);
            GameData.maxHealth = obj.getInt();
            obj = BSonParser.getObject("hunger", objects);
            GameData.hunger = obj.getInt();
            obj = BSonParser.getObject("maxHunger", objects);
            GameData.maxHunger = obj.getInt();
            obj = BSonParser.getObject("hungerSpeed", objects);
            GameData.hungerSpeed = obj.getDouble();
            obj = BSonParser.getObject("hungerTime", objects);
            GameData.hungerTime = obj.getLong();
            obj = BSonParser.getObject("time", objects);
            GameData.time = obj.getLong();
            obj = BSonParser.getObject("dimension", objects);
            Background.changeBackground(obj.getInt());
            Trident.setPlrPos(new Position(x, y));
            try{
                GameData.effects = new ArrayList<Effect>();
                obj = BSonParser.getObject("effects", objects);
                asList = (BSonList)obj;
                for(int i = 0; i < asList.list.size(); i += 5){
                    int id = asList.list.get(i).getInt();
                    long time = asList.list.get(i + 1).getLong();
                    long maxTime = asList.list.get(i + 2).getLong();
                    long coolTimer = asList.list.get(i + 3).getLong();
                    long cooldown = asList.list.get(i + 4).getLong();
                    GameData.effects.add(new Effect(id, time, maxTime, coolTimer, cooldown));
                }
            }catch(Exception e){
                Trident.printExceptionSilent("Error loading effects. Likely an old save.", e);
            }
        }catch(Exception e){
            Trident.printException("Error while loading world!", e);
            Trident.loadScene("title");
        }
    }

    public static ArrayList<String> getWorlds(){
        ArrayList<String> list = new ArrayList<String>();
        File dir = new File("data/worlds");
        if(!dir.exists()){
            dir.mkdir();
            return list;
        }
        File[] files = dir.listFiles();
        for(File f: files){
            String name = f.getName();
            list.add(name.substring(0, name.length() - 5));
        }
        return list;
    }
}
