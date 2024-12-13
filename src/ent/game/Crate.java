package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.util.BTools;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import ent.GameObject;
import trident.TridEntity;
public class Crate extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/crate.png");

    // data[] = inventory
    /*
     * 0-29 = item id
     * 30-59 = item amount
     */

    // Constructor, runs when the entity is created
    public Crate(Position pos, int[] dat){
        super(pos, 20, GameObject.CRATE, dat, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_AXE;
        if(dat.length == 1) clear();
    }
    public Crate(Position pos, int hp, int[] dat){
        super(pos, hp, GameObject.CRATE, 20, dat, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_AXE;
        if(dat.length == 1) clear();
    }
    public Crate(Position pos){
        super(pos, 20, GameObject.CRATE, new int[60], new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_AXE;
        data[0] = Item.I_SWORD;
        data[30] = 1;
    }

    public Crate(){
        super("tutcrate", 0);
    }
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new Crate(pos); 
    }


    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paintIcon(panel, g, x - 32, y - 50);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.CRATE, 1, position);

        for(int i = 0; i < 30; i++){
            GameObject.dropNoDelay(getSlot(i), position);
        }

        if(GameData.openCrate != null && GameData.openCrate.equals(this)){
            GameData.openCrate = null;
        }
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }

    public Item getSlot(int slot){
        if(slot < 0 || slot >= 30){
            return null;
        }

        int id = data[slot];
        int amount = data[slot + 30];
        return new Item((short)id, amount);
    }
    public void setSlot(int slot, Item item){
        if(slot < 0 || slot >= 30) return;

        data[slot] = item.id;
        data[slot + 30] = item.amount;
    }
    public void clear(){
        data = new int[60];
    }
}
