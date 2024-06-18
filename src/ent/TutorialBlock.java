package ent;

import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
public class TutorialBlock extends TridEntity {

    // Constructor, runs when the entity is created
    public TutorialBlock(Position pos, Dimension d){
        super(pos, d);
    }
    // Registry constructor, used only for adding to the registry
    public TutorialBlock(){
        super("tutblock", true, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new TutorialBlock(pos, collision);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(Color.black);
        Rectangle rect = getCollision();
        g.fillRect(x - rect.width / 2, y - rect.height / 2, rect.width, rect.height);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
