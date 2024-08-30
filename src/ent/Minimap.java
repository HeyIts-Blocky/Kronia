package ent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import blib.util.Position;
import custom.GameData;
import ent.game.LogWall;
import ent.game.boss.Boss;
import trident.TridEntity;
import trident.Trident;
public class Minimap extends TridEntity {

    // Constructor, runs when the entity is created
    public Minimap(Position pos){
        super(pos);
        renderType = TOPPRIORITY;
        alwaysRender = true;
    }
    // Registry constructor, used only for adding to the registry
    public Minimap(){
        super("minimap", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new Minimap(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(!GameData.minimapEnabled || GameData.spectate) return;
        g.setColor(Color.blue);
        g.fillRect(Trident.getFrameWidth() - 150, Trident.getFrameHeight() - 100, 150, 100);
        g.setColor(Color.white);
        g.drawRect(Trident.getFrameWidth() - 150, Trident.getFrameHeight() - 100, 150, 100);
        
        if(Background.bg == Background.SURFACE){
            g.setColor(Color.green);
            g.fillRect(Trident.getFrameWidth() - 75 - 47, Trident.getFrameHeight() - 100 + 3, 94, 94);
        }
        if(Background.bg == Background.MINES){
            g.setColor(Color.gray);
            g.fillRect(Trident.getFrameWidth() - 75 - 47, Trident.getFrameHeight() - 100 + 3, 94, 94);
        }

        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(!Background.isInDimension(e)) continue;
            if(e instanceof LogWall){
                g.setColor(new Color(116, 51, 0));
                Point p = new Point();
                p.x = (int)(((e.position.x - Background.getClampPos()[0]) / 10000) * 94);
                p.y = (int)(((e.position.y - Background.getClampPos()[2]) / 10000) * 94);
                g.fillRect(Trident.getFrameWidth() - 75 - 47 + p.x, Trident.getFrameHeight() - 100 + 3 + p.y, 1, 1);
            }
            if(e instanceof Boss){
                Boss boss = (Boss)e;
                g.setColor(boss.mapColor);
                Point p = new Point();
                p.x = (int)(((e.position.x - Background.getClampPos()[0]) / 10000) * 94);
                p.y = (int)(((e.position.y - Background.getClampPos()[2]) / 10000) * 94);
                g.fillRect(Trident.getFrameWidth() - 75 - 47 + p.x - 2, Trident.getFrameHeight() - 100 + 3 + p.y - 2, 4, 4);
            }
        }

        if(GameData.lastDeath != null && Background.isInDimension(GameData.lastDeath)){
            g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
            Point p = new Point();
            p.x = (int)(((GameData.lastDeath.x - Background.getClampPos()[0]) / 10000) * 94);
            p.y = (int)(((GameData.lastDeath.y - Background.getClampPos()[2]) / 10000) * 94);
            g.fillRect(Trident.getFrameWidth() - 75 - 47 - 1 + p.x, Trident.getFrameHeight() - 100 + 3 - 1 + p.y, 3, 3);
        }

        g.setColor(Color.red);
        Point p = new Point();
        p.x = (int)(((Trident.getPlrPos().x - Background.getClampPos()[0]) / 10000) * 94);
        p.y = (int)(((Trident.getPlrPos().y - Background.getClampPos()[2]) / 10000) * 94);
        g.fillRect(Trident.getFrameWidth() - 75 - 47 - 1 + p.x, Trident.getFrameHeight() - 100 + 3 - 1 + p.y, 3, 3);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
