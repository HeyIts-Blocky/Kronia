package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.util.BTools;
import blib.util.Position;
import custom.Item;
import ent.GameObject;
public class CopperOre extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/copperOre.png");

    // Constructor, runs when the entity is created
    public CopperOre(Position pos){
        super(pos, 25, GameObject.COPPERORE, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
        defense = 2;
    }
    public CopperOre(Position pos, int hp){
        super(pos, hp, GameObject.COPPERORE, 25, new Dimension(20, 10));
        BTools.resizeImgIcon(img, 64, 64);
        weakness = Item.T_PICK;
        defense = 2;
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
        GameObject.dropNoDelay(Item.COPPERORE, BTools.randInt(1, 5), position);
        GameObject.dropNoDelay(Item.STONE, BTools.randInt(1, 4), position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
