package ent;

import blib.game.Entity;
import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
public class Background extends TridEntity {

    public static int bg = 0;
    public static final int SURFACE = 0, MINES = 1;
    public static final int OFFSET = 10500;
    
    ImageIcon img = new ImageIcon("data/images/bg/grass.png");
    ImageIcon stone = new ImageIcon("data/images/bg/stone.png");

    // Constructor, runs when the entity is created
    public Background(Position pos){
        super(pos);
        BTools.resizeImgIcon(img, 5000, 5000);
        renderType = Entity.BOTTOMPRIORITY;
        alwaysRender = true;
    }
    // Registry constructor, used only for adding to the registry
    public Background(){
        super("bg", true, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new Background(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(bg == SURFACE || Trident.getCurrentScene().name.equals("title")){ 
            g.setColor(new Color(0, 255, 255));
            g.fillRect(0, 0, 700, 500);
            img.paintIcon(panel, g, x - 5000, y - 5000);
            img.paintIcon(panel, g, x, y - 5000);
            img.paintIcon(panel, g, x - 5000, y);
            img.paintIcon(panel, g, x, y);
        }else if(bg == MINES){
            g.setColor(new Color(50, 50, 50));
            g.fillRect(0, 0, 700, 500);
            stone.paintIcon(panel, g, x - 5000, y - 5000);
            stone.paintIcon(panel, g, x, y - 5000);
            stone.paintIcon(panel, g, x - 5000, y);
            stone.paintIcon(panel, g, x, y);
        }
        
    }

    public void engineRender(Graphics g, JPanel panel, int x, int y){
        render(g, panel, x, y);
        super.engineRender(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        double x = 5000;
        double y = 5000;
        if(bg == MINES) x += OFFSET;
        if(Trident.getCurrentScene().name.equals("title")){
            x = 0;
            y = 0;
        }

        position = new Position(x, y);
    }

    public static void changeBackground(int b){
        bg = b;
        
        double x = Trident.getPlrPos().x;
        double y = Trident.getPlrPos().y;
        while(x > 10000) x -= OFFSET;
        while(y > 10000) y -= OFFSET;

        if(bg == MINES) x += OFFSET;

        Trident.setPlrPos(new Position(x, y));

        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(e instanceof GameObject){
                if(e.HASCOLLISION && e.getCollision().intersects(Trident.getPlr().getCollision())){
                    ((GameObject)e).damage(((GameObject)e).health);
                }
            }
            
        }
    }

    public static int[] getClampPos(){
        int X1 = 0, X2 = 1, Y1 = 2, Y2 = 3;

        int[] clamps = {16, 10000 - 16, 16, 10000 - 16};

        if(bg == MINES){
            clamps[X1] += OFFSET;
            clamps[X2] += OFFSET;
        }

        return clamps;
    }

    public static boolean isInDimension(TridEntity e){
        // check if it's in the current dimension

        Rectangle rect = new Rectangle(getClampPos()[0], getClampPos()[2], 10000, 10000);
        Point p = e.position.toPoint();

        return rect.contains(p);
    }
    public static boolean isInDimension(Position pos){
        // check if it's in the current dimension

        Rectangle rect = new Rectangle(getClampPos()[0], getClampPos()[2], 10000, 10000);
        Point p = pos.toPoint();

        return rect.contains(p);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){
        
    }
}
