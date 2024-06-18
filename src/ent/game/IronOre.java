package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class IronOre extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/ironOre.png");

    // Constructor, runs when the entity is created
    public IronOre(Position pos){
        super(pos, 30, GameObject.IRONORE, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
        defense = 4;
    }
    public IronOre(Position pos, int hp){
        super(pos, hp, GameObject.IRONORE, 30, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
        defense = 4;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paintIcon(panel, g, x - 32, y - 40);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.IRONORE, BTools.randInt(1, 5), position);
        GameObject.dropNoDelay(Item.STONE, BTools.randInt(1, 4), position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
