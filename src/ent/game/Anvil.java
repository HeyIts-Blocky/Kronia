package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class Anvil extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/anvil.png");

    // Constructor, runs when the entity is created
    public Anvil(Position pos){
        super(pos, 30, GameObject.ANVIL, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
    }
    public Anvil(Position pos, int hp){
        super(pos, hp, GameObject.ANVIL, 30, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
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
        GameObject.dropNoDelay(Item.ANVIL, 1, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
