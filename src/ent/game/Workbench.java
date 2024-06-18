package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class Workbench extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/workbench.png");

    // Constructor, runs when the entity is created
    public Workbench(Position pos){
        super(pos, 20, GameObject.WORKBENCH, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_AXE;
    }
    public Workbench(Position pos, int hp){
        super(pos, hp, GameObject.WORKBENCH, 20, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_AXE;
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
        GameObject.dropNoDelay(Item.WORKBENCH, 1, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
