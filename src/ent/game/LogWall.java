package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.util.BTools;
import blib.util.Position;
import custom.Item;
import ent.GameObject;
public class LogWall extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/logWall.png"), rotImg = new ImageIcon("data/images/ent/logWallRot.png");

    public boolean rotated;

    // Constructor, runs when the entity is created
    public LogWall(Position pos, boolean rot){
        super(pos, 50, GameObject.LOGWALL, new int[]{(rot ? 1 : 0)}, (rot ? new Dimension(8, 32) : new Dimension(64, 16)));
        BTools.resizeImgIcon(img, 64, 64);
        BTools.resizeImgIcon(rotImg, 64, 64);
        weakness = Item.T_AXE;
        rotated = rot;
    }
    public LogWall(Position pos, int hp, boolean rot){
        super(pos, hp, GameObject.LOGWALL, 50, new int[]{(rot ? 1 : 0)}, (rot ? new Dimension(8, 32) : new Dimension(64, 16)));
        BTools.resizeImgIcon(img, 64, 64);
        BTools.resizeImgIcon(rotImg, 64, 64);
        weakness = Item.T_AXE;
        rotated = rot;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(!rotated) img.paintIcon(panel, g, x - 32, y - 56);
        else rotImg.paintIcon(panel, g, x - 32, y - 56);
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.WOOD, 5, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
