package ent.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.game.ParticleEmitter;
import blib.util.BTools;
import blib.util.Position;
import custom.Effect;
import custom.GameData;
import custom.Item;
import ent.GameObject;
import trident.Trident;
public class Mushroom extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/mushroom.png");
    ParticleEmitter emitter;

    // Constructor, runs when the entity is created
    public Mushroom(Position pos){
        super(pos, 20, GameObject.MUSHROOM, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_SWORD;
        emitter = new ParticleEmitter(new Position(0, 0), 65, 2, true);
        emitter.spawnSquares = true;
        emitter.spawnCircles = false;
        emitter.spawnImages = false;
        emitter.speed = 0.25;
        emitter.size = 5;
        emitter.minLife = 2000;
        emitter.maxLife = 5000;
        emitter.fadeOut = true;
        emitter.spawnRect = new Dimension(150, 150);
        ArrayList<Color> squareColors = new ArrayList<Color>();
        // Add your own colors and images here!
        squareColors.add(new Color(103, 58, 183));
        squareColors.add(new Color(81, 45, 168));
        squareColors.add(new Color(49, 27, 146));

        emitter.squareColors = squareColors;

        defense = 3;
    }
    public Mushroom(Position pos, int hp){
        super(pos, hp, GameObject.MUSHROOM, 20, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_SWORD;
        emitter = new ParticleEmitter(new Position(0, 0), 65, 2, true);
        emitter.spawnSquares = true;
        emitter.spawnCircles = false;
        emitter.spawnImages = false;
        emitter.speed = 0.25;
        emitter.size = 5;
        emitter.minLife = 2000;
        emitter.maxLife = 5000;
        emitter.fadeOut = true;
        emitter.spawnRect = new Dimension(150, 150);
        ArrayList<Color> squareColors = new ArrayList<Color>();
        // Add your own colors and images here!
        squareColors.add(new Color(103, 58, 183));
        squareColors.add(new Color(81, 45, 168));
        squareColors.add(new Color(49, 27, 146));

        emitter.squareColors = squareColors;

        defense = 3;
        
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        img.paintIcon(panel, g, x - 32, y - 32);
        emitter.render(g, panel, x, y);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        emitter.update(elapsedTime);
        Rectangle rect = new Rectangle((int)position.x - 75, (int)position.y - 75, 150, 150);
        if(rect.contains(Trident.getPlrPos().toPoint())){
            GameData.addEffect(new Effect(Effect.POISON, 2000));
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.MUSHROOMCHUNK, BTools.randInt(0, 2), position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
