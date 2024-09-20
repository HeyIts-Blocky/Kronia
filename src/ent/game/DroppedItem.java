package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import blib.util.Audio;
import blib.util.BTools;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import ent.GameObject;
import trident.Trident;
public class DroppedItem extends GameObject {

    public long pickupDelay = 3000;
    long life = 300000;

    // Constructor, runs when the entity is created
    public DroppedItem(Position pos, int[] dat){
        super(pos, 1, GameObject.ITEM, dat, new Dimension(0, 0));
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(data[1] > 1){
            g.drawImage(Item.getImg((short)data[0]).getImage(), x - 6, y - 6, 16, 16, null);
        }
        if(data[1] > 10){
            g.drawImage(Item.getImg((short)data[0]).getImage(), x - 10, y - 10, 16, 16, null);
        }
        g.drawImage(Item.getImg((short)data[0]).getImage(), x - 8, y - 8, 16, 16, null);
        
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        life -= elapsedTime;
        if(life <= 0){
            Trident.destroy(this);
            return;
        }
        if(pickupDelay > 0) pickupDelay -= elapsedTime;
        if(GameData.spectate) return;
        if(BTools.getDistance(position, Trident.getPlrPos()) > 40){
            pickupDelay = 0;
        }
        if(pickupDelay <= 0){
            if(BTools.getDistance(position, Trident.getPlrPos()) < 32 && GameData.canAdd(getItem())){
                GameData.addItem(getItem());
                Trident.destroy(this);
                Audio.play("data/sound/pickup.wav");
            }
        }
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }

    public int getID(){
        return data[0];
    }
    public int getAmount(){
        return data[1];
    }
    public Item getItem(){
        return new Item((short)data[0], data[1]);
    }




    // ID LIST
    public static final int TREE = 0;
}
