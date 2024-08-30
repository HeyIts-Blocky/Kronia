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
public class Torch extends GameObject {

    AnimImage img = new AnimImage("data/images/ent/torch.png", 6, 12, 32, 32);
    Light light = null;

    // Constructor, runs when the entity is created
    public Torch(Position pos){
        super(pos, 1, GameObject.TORCH, new Dimension(10, 5));
        img.resize(64, 64);
        weakness = Item.T_AXE;
    }
    public Torch(Position pos, int hp){
        super(pos, hp, GameObject.TORCH, 1, new Dimension(10, 5));
        img.resize(64, 64);
        weakness = Item.T_AXE;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paint(panel, g, x - 32, y - 48);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        img.update(elapsedTime);
        if(light == null){
            light = new Light(position, 150, new Color(236, 115, 0, 100));
            Trident.addLight(light);
        }
    }
    public void dropItems(){
        Trident.removeLight(light);
        GameObject.dropNoDelay(Item.TORCH, 1, position);
    }
    public void tookDamage(int amount){
        
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
