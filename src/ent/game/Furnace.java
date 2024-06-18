package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
public class Furnace extends GameObject {

    AnimImage img = new AnimImage("data/images/ent/furnace.png", 5, 6, 32, 32);

    // Constructor, runs when the entity is created
    public Furnace(Position pos){
        super(pos, 30, GameObject.FURNACE, new Dimension(20, 10));
        img.resize(64, 64);
        weakness = Item.T_PICK;
    }
    public Furnace(Position pos, int hp){
        super(pos, hp, GameObject.FURNACE, 30, new Dimension(20, 10));
        img.resize(64, 64);
        weakness = Item.T_PICK;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paint(panel, g, x - 32, y - 50);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        img.update(elapsedTime);
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.FURNACE, 1, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
