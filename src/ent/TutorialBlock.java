package ent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import blib.util.Position;
import trident.TridEntity;
import trident.Trident;
public class TutorialBlock extends TridEntity {

    public int id;
    public boolean enabled = true;

    // Constructor, runs when the entity is created
    public TutorialBlock(Position pos, Dimension d, int id){
        super(pos, d);
        this.id = id;
        renderType = UNDER;
    }
    // Registry constructor, used only for adding to the registry
    public TutorialBlock(){
        super("tutblock", true, 1);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new TutorialBlock(pos, collision, data[0]);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(new Color(0f, 1f, 1f, 0.8f));
        Rectangle rect = getCollision();
        g.fillRect(x - rect.width / 2, y - rect.height / 2, rect.width, rect.height);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }

    public Rectangle getCollision(){
        if(enabled) return super.getCollision();
        else return new Rectangle(0, 0, 0, 0);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){
        enabled = true;
    }

    public static void disable(int id){
        for(TridEntity e: Trident.getEntities()){
            if(e instanceof TutorialBlock){
                if(((TutorialBlock)e).id == id) ((TutorialBlock)e).enabled = false;
            }
        }
    }

    public static void enable(int id){
        for(TridEntity e: Trident.getEntities()){
            if(e instanceof TutorialBlock){
                if(((TutorialBlock)e).id == id) ((TutorialBlock)e).enabled = true;
            }
        }
    }
}
