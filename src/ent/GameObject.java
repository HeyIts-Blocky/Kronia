package ent;

import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import ent.game.*;
import custom.*;
import ent.game.boss.*;
public class GameObject extends TridEntity {

    public int maxHealth, health, id;
    public int[] data;
    public int weakness = Item.T_NULL;
    public int defense = 0;

    // Constructor, runs when the entity is created
    public GameObject(Position pos, int hp, int id, Dimension coll){
        super(pos, coll);
        maxHealth = hp;
        health = hp;
        this.id = id;
        data = new int[0];
    }
    public GameObject(Position pos, int hp, int id, int maxHP, Dimension coll){
        super(pos, coll);
        maxHealth = maxHP;
        health = hp;
        this.id = id;
        data = new int[0];
    }
    public GameObject(Position pos, int hp, int id, int[] dat, Dimension coll){
        super(pos, coll);
        maxHealth = hp;
        health = hp;
        this.id = id;
        data = dat;
    }
    public GameObject(Position pos, int hp, int id, int maxHP, int[] dat, Dimension coll){
        super(pos, coll);
        maxHealth = maxHP;
        health = hp;
        this.id = id;
        data = dat;
    }

    public GameObject(){
        super("gameObj", false, 1);
    }

    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return placeObj(pos, data[0], new int[0]);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(health != maxHealth){
            g.setColor(Color.black);
            g.fillRect(x - 20, y + 10, 40, 5);
            double hpPercent = (double)health / maxHealth;
            g.setColor(Color.red);
            g.fillRect(x - 20, y + 10, (int)(40 * hpPercent), 5);
        }
        if(Settings.aimHelp){
            g.setColor(Settings.assistColor);
            g.drawLine(x - 2, y - 2, x + 2, y + 2);
            g.drawLine(x + 2, y - 2, x - 2, y + 2);
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }

    public final void damage(int amount){
        Settings.playSound("data/sound/hit.wav");
        if(amount < defense) return;
        health -= amount - defense;
        tookDamage(amount - defense);
        if(health <= 0){
            Trident.destroy(this);
            dropItems();
        }
    }
    public void tookDamage(int amount){}
    public void dropItems(){}

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }

    public static GameObject mkObj(Position pos, int id, int hp, int[] data){
        switch(id){
        case TREE:
            return new Tree(pos, hp);
        case ROCK:
            return new Rock(pos, hp);
        case CAVEROCK:
            return new CaveRock(pos, hp);
        case WORKBENCH:
            return new Workbench(pos, hp);
        case FURNACE:
            return new Furnace(pos, hp);
        case COW:
            return new Cow(pos, hp);
        case ITEM:
            return new DroppedItem(pos, data);
        case SLIME:
            return new Slime(pos, hp);
        case LOGWALL:
            if(data.length == 0) return new LogWall(pos, hp, false);
            return new LogWall(pos, hp, data[0] == 1);
        case TORCH:
            return new Torch(pos, hp);
        case COALORE:
            return new CoalOre(pos, hp);
        case SCP999:
            return new SCP999(pos, hp);
        case CAVESLIME:
            return new CaveSlime(pos, hp);
        case IRONORE:
            return new IronOre(pos, hp);
        case ANVIL:
            return new Anvil(pos, hp);
        case COPPERORE:
            return new CopperOre(pos, hp);
        case MOLE:
            return new Mole(pos, hp);
        case TUTTREE:
            return new TutorialTree(pos, hp);
        case CAMPFIRE:
            return new Campfire(pos, hp);
        case CRATE:
            return new Crate(pos, hp, data);
        default:
            System.out.println("Unkown id: " + id);
            return new Tree(pos);
        }
    }

    public static GameObject placeObj(Position pos, int id, int[] data){
        switch(id){
        case TREE:
            return new Tree(pos);
        case ROCK:
            return new Rock(pos);
        case CAVEROCK:
            return new CaveRock(pos);
        case WORKBENCH:
            return new Workbench(pos);
        case FURNACE:
            return new Furnace(pos);
        case COW:
            return new Cow(pos);
        case ITEM:
            return new DroppedItem(pos, data);
        case SLIME:
            return new Slime(pos);
        case LOGWALL:
            if(data.length == 0) return new LogWall(pos, false);
            return new LogWall(pos, data[0] == 1);
        case TORCH:
            return new Torch(pos);
        case COALORE:
            return new CoalOre(pos);
        case SCP999:
            return new SCP999(pos);
        case APEXSLIME:
            return new ApexSlime(pos);
        case CAVESLIME:
            return new CaveSlime(pos);
        case IRONORE:
            return new IronOre(pos);
        case ANVIL:
            return new Anvil(pos);
        case COPPERORE:
            return new CopperOre(pos);
        case MOLE:
            return new Mole(pos);
        case TUTTREE:
            return new TutorialTree(pos);
        case CAMPFIRE:
            return new Campfire(pos);
        case CRATE:
            return new Crate(pos, data);
        default:
            System.out.println("Unkown id: " + id);
            return new Tree(pos);
        }
    }

    public static void drop(Item item, Position pos){
        drop(item.id, item.amount, pos);
    }
    public static void drop(int id, int amount, Position pos){
        if(id == Item.NOTHING) return;
        pos = pos.copy();
        pos.x += BTools.randInt(0, 64) - 32;
        pos.y += BTools.randInt(0, 64) - 32;
        Trident.spawnEntity(new DroppedItem(pos, new int[]{id, amount}));
    }
    public static void dropNoDelay(Item item, Position pos){
        dropNoDelay(item.id, item.amount, pos);
    }
    public static void dropNoDelay(int id, int amount, Position pos){
        if(id == Item.NOTHING) return;
        pos = pos.copy();
        pos.x += BTools.randInt(0, 64) - 32;
        pos.y += BTools.randInt(0, 64) - 32;
        DroppedItem dIt = new DroppedItem(pos, new int[]{id, amount});
        dIt.pickupDelay = 0;
        Trident.spawnEntity(dIt);
    }




    // ID LIST
    public static final int DONTSAVE = -1;
    public static final int TREE = 0, ITEM = 1, WORKBENCH = 2, ROCK = 3, FURNACE = 4, COW = 5, SLIME = 6, LOGWALL = 7, TORCH = 8, COALORE = 9, SCP999 = 10, APEXSLIME = 11, BABYSLIME = 12, CAVEROCK = 13;
    public static final int CAVESLIME = 14, IRONORE = 15, ANVIL = 16, COPPERORE = 17, MOLE = 18, TUTTREE = 19, CAMPFIRE = 20, CRATE = 21; 
}