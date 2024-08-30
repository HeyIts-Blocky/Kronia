package ent.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import blib.game.Light;
import blib.util.AnimImage;
import blib.util.Position;
import custom.Item;
import ent.GameObject;
import trident.Trident;
public class Campfire extends GameObject {

    AnimImage img = new AnimImage("data/images/ent/campfire.png", 4, 12, 32, 32);
    Light light = null;

    // Constructor, runs when the entity is created
    public Campfire(Position pos){
        super(pos, 30, GameObject.CAMPFIRE, new Dimension(20, 10));
        img.resize(64, 64);
        weakness = Item.T_AXE;
    }
    public Campfire(Position pos, int hp){
        super(pos, hp, GameObject.CAMPFIRE, 30, new Dimension(20, 10));
        img.resize(64, 64);
        weakness = Item.T_AXE;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paint(panel, g, x - 32, y - 50);
        super.render(g, panel, x, y);
        if(light == null){
            light = new Light(position, 250, new Color(236, 115, 0, 100));
            Trident.addLight(light);
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        img.update(elapsedTime);
    }
    public void dropItems(){
        Trident.removeLight(light);
        GameObject.dropNoDelay(Item.COAL, 3, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
