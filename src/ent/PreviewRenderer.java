package ent;

import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import custom.*;
import java.awt.image.*;
public class PreviewRenderer extends TridEntity {

    // Constructor, runs when the entity is created
    public PreviewRenderer(Position pos){
        super(pos);
        alwaysRender = true;
    }
    // Registry constructor, used only for adding to the registry
    public PreviewRenderer(){
        super("previewrend", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new PreviewRenderer(pos);
    }

    // Render while in game
    public void render(Graphics gra, JPanel panel, int x, int y){
        if(GameData.getSelItem().getType() == Item.T_PLACEABLE){
            BufferedImage img = new BufferedImage(Trident.getFrameWidth(), Trident.getFrameHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D)img.getGraphics();
            GameObject ob = GameObject.placeObj(new Position(), GameData.getSelItem().getData()[0], new int[]{(GameData.rotateItem ? 1 : 0)});
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            ob.render(g, panel, x, y);
            gra.drawImage(img, 0, 0, null);
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(GameData.getSelItem().getType() == Item.T_PLACEABLE){
            position = Trident.mouseWorldPos;
        }else{
            position = new Position(-1000, -1000);
        }
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
