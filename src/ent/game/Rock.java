package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class Rock extends GameObject {

    ImageIcon img;

    // Constructor, runs when the entity is created
    public Rock(Position pos){
        super(pos, 40, GameObject.ROCK, new Dimension(20, 15));
        int sel = BTools.randInt(0, 5);
        img = new ImageIcon("data/images/ent/rock/" + sel + ".png");
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
    }
    public Rock(Position pos, int hp){
        super(pos, hp, GameObject.ROCK, 40, new Dimension(20, 15));
        int sel = BTools.randInt(0, 5);
        img = new ImageIcon("data/images/ent/rock/" + sel + ".png");
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paintIcon(panel, g, x - 32, y - 43);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.STONE, BTools.randInt(2, 6), position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
