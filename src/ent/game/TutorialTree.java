package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class TutorialTree extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/tree.png");

    // Constructor, runs when the entity is created
    public TutorialTree(Position pos){
        super(pos, 20, GameObject.TUTTREE, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 96, 96);
        weakness = Item.T_AXE;
    }
    public TutorialTree(Position pos, int hp){
        super(pos, hp, GameObject.TUTTREE, 20, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 96, 96);
        weakness = Item.T_AXE;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paintIcon(panel, g, x - (96 / 2), y - 90);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.TUTWOOD, BTools.randInt(4, 14), position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
