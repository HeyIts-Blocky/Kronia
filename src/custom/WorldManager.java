package custom;

import java.awt.Rectangle;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import blib.bson.BSonList;
import blib.bson.BSonObject;
import blib.bson.BSonParser;
import blib.util.BTools;
import blib.util.Position;
import ent.Background;
import ent.GameObject;
import ent.HUD;
import ent.game.CaveRock;
import ent.game.CaveSlime;
import ent.game.CoalOre;
import ent.game.CopperOre;
import ent.game.Cow;
import ent.game.IronOre;
import ent.game.Mole;
import ent.game.Mushroom;
import ent.game.MushroomSlime;
import ent.game.Rock;
import ent.game.Slime;
import ent.game.Tree;
import ent.game.boss.Boss;
import trident.TridEntity;
import trident.Trident;
public class WorldManager {

    private static final int DEFAULT_ENTITIES = 2000, MAX_ENTITIES = 5000; // per dimension
    private static int spawnTimer = 0, spawnTime = 60000;
    private static int enemyTimer = 0, enemyTime = 30000;

    public static byte enemyCap = 3;

    public static String worldName = "";
    public static byte difficulty = 2;
    public static final byte V_EASY = 0, EASY = 1, NORMAL = 2, HARD = 3, V_HARD = 4;
    public static byte defaultDiff = NORMAL;

    public static final byte WORLD_SAVE_VERSION = 1; // INCREASE THIS NUMBER EVERY TIME THE WAY WORLDS ARE SAVED GETS CHANGED

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
        if(spawnEnemies && enemyTimer >= enemyTime && numEnemies() < enemyCap){
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
            if(Background.bg == Background.DEEPMINES){
                int type = BTools.randInt(0, 2);
                switch(type){
                case 0:
                    Trident.spawnEntity(new CaveSlime(vec));
                    break;
                case 1:
                    Trident.spawnEntity(new MushroomSlime(vec));
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
    
    public static void newWorld(String name, byte diff){
        worldName = name;
        GameData.clearInventory();
        difficulty = diff;
        Background.bg = Background.SURFACE;
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
        GameData.maxHealth = 100;
        switch(diff){
            case V_EASY:
                GameData.maxHealth = 200;
                break;
            case EASY:
                GameData.maxHealth = 150;
                break;
            case HARD:
                GameData.maxHealth = 75;
                break;
            case V_HARD:
                GameData.maxHealth = 50;
                break;
        }
        GameData.health = GameData.maxHealth;
        GameData.hunger = 100;
        GameData.maxHunger = 100;
        GameData.hungerSpeed = 0;
        GameData.hungerTimer = 0;
        spawnTimer = -(spawnTime * 10);
        GameData.time = 0;
        saveWorld();
    }

    private static void spawnEnt(){

        if(Background.bg == Background.SURFACE){
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
        }
        
        if(Background.bg == Background.MINES){
            // Spawn Mines
            Position pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
            pos.x += Background.OFFSET;
            int type = BTools.randInt(0, 3);
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
        
        if(Background.bg == Background.DEEPMINES){
            // Spawn Deep Mines
            Position pos = new Position(BTools.randInt(10, 10000 - 10), BTools.randInt(10, 10000 - 10));
            pos.y += Background.OFFSET;
            int type = BTools.randInt(0, 5);
            if(type == 0){
                Trident.spawnEntity(new CaveRock(pos));
            }
            if(type >= 1 && type <= 3){
                Trident.spawnEntity(new IronOre(pos));
            }
            if(type == 4){
                Trident.spawnEntity(new Mushroom(pos));
            }
        }
        
    }

    public static void loadEntities(int dimension){
        // save game
        saveWorld();
        HUD.addNotif("World Saved", HUD.NOTIF_AUTOSAVE);

        loadEntitiesNoSave(dimension);
    }

    public static void loadEntitiesNoSave(int dimension){
        Position pos = Trident.getPlrPos();
        Trident.setupScenes();
        Trident.loadScene("world");
        Trident.setPlrPos(pos);
        ArrayList<BSonObject> objects = BSonParser.readFile("data/worlds/" + worldName + ".bson");
        BSonList l;
        switch(dimension){
        case Background.SURFACE:
            l = (BSonList)BSonParser.getObject("surfaceEnt", objects);
            break;
        case Background.MINES:
            l = (BSonList)BSonParser.getObject("minesEnt", objects);
            break;
        case Background.DEEPMINES:
            l = (BSonList)BSonParser.getObject("deepMinesEnt", objects);
            break;
        default:
            l = (BSonList)BSonParser.getObject("surfaceEnt", objects);
        }

        for(int i = 0; i < l.list.size(); i++){
            int id = l.list.get(i).getInt();
            i++;
            double x = l.list.get(i).getDouble();
            i++;
            double y = l.list.get(i).getDouble();
            i++;
            int hp = l.list.get(i).getInt();
            i++;
            int numData = l.list.get(i).getInt();
            i++;
            int[] data = new int[numData];
            for(int j = 0; j < numData; j++){
                data[j] = l.list.get(i).getInt();
                i++;
            }
            i--;

            Trident.spawnEntity(GameObject.mkObj(new Position(x, y), id, hp, data));
        }

        if(l.list.size() == 0){
            // first time in dimension, probably
            for(int i = 0; i < DEFAULT_ENTITIES; i++){
                spawnEnt();
            }
        }
    }

    public static void saveWorld(){
        if(Trident.getCurrentScene().name.equals("tutorial")) return;
        try{
            File file = new File("data/worlds/" + worldName + ".bson");
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println("int version " + WORLD_SAVE_VERSION);
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
            writer.println("{ surfaceEnt");
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                Rectangle bounds = new Rectangle(-16, -16, 10032, 10032);
                if(bounds.contains(e.position.toPoint())){ // check to make sure it's in the surface
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
            }
            writer.println("}");
            writer.println("{ minesEnt");
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                Rectangle bounds = new Rectangle(-16, -16, 10032, 10032);
                bounds.x += Background.OFFSET;
                if(bounds.contains(e.position.toPoint())){ // check to make sure it's in the surface
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
            }
            writer.println("}");
            writer.println("{ deepMinesEnt");
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                Rectangle bounds = new Rectangle(-16, -16, 10032, 10032);
                bounds.y += Background.OFFSET;
                if(bounds.contains(e.position.toPoint())){ // check to make sure it's in the surface
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
            BSonObject obj = BSonParser.getObject("version", objects);
            int saveVersion = 0;
            if(obj != null) saveVersion = obj.getInt();

            // World data
            if(saveVersion == 0){ // Old entities
                obj = BSonParser.getObject("entities", objects);
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
            }
            
            obj = BSonParser.getObject("inventory", objects);
            BSonList asList = (BSonList)obj;
            for(int i = 0; i < asList.list.size(); i += 2){
                int id = asList.list.get(i).getInt();
                int amount = asList.list.get(i + 1).getInt();
                GameData.inventory[i / 2] = new Item((short)id, (short)amount);
            }
            obj = BSonParser.getObject("difficulty", objects);
            difficulty = (byte)obj.getInt();

            // Player data
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

            // Hunger data
            obj = BSonParser.getObject("hungerSpeed", objects);
            GameData.hungerSpeed = obj.getDouble();
            obj = BSonParser.getObject("hungerTime", objects);
            GameData.hungerTime = obj.getLong();

            // Time
            obj = BSonParser.getObject("time", objects);
            GameData.time = obj.getLong();

            // Dimension
            obj = BSonParser.getObject("dimension", objects);
            Background.bg = ((byte)obj.getInt());
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                if(e instanceof GameObject){
                    if(e.HASCOLLISION && e.getCollision().intersects(Trident.getPlr().getCollision())){
                        Trident.destroy(e);
                    }
                }
                
            }
            Trident.setPlrPos(new Position(x, y));

            // Effects
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

            if(saveVersion >= 1){ // load entities v1
                loadEntitiesNoSave(Background.bg);
            }

            if(saveVersion == 0){ // makes sure entities are optimized
                saveWorld();
                loadWorld(name);
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
