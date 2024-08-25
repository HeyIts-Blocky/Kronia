package ent;

import blib.util.*;
import ent.game.LogWall;
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

            if(GameData.getSelItem().id == Item.LOGWALL){
                // look for snap points
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity e = Trident.getEntities().get(i);
                    if(e instanceof LogWall){
                        LogWall wall = (LogWall)e;
                        if(wall.rotated == GameData.rotateItem){
                            if(wall.rotated){
                                // check up & down
                                double tDist = 999, bDist = 999;
                                Position top = new Position(e.position.x, e.position.y - 32);
                                Position bottom = new Position(e.position.x, e.position.y + 32);
                                if(BTools.getDistance(top, Trident.mouseWorldPos) < 16){
                                    tDist = BTools.getDistance(top, Trident.mouseWorldPos);
                                }
                                if(BTools.getDistance(bottom, Trident.mouseWorldPos) < 16){
                                    bDist = BTools.getDistance(bottom, Trident.mouseWorldPos);
                                }
                                if(Math.min(tDist, bDist) <= 16){
                                    if(tDist < bDist) position = top;
                                    else position = bottom;
                                }
                            }else{
                                // check left & right
                                double lDist = 999, rDist = 999;
                                Position left = new Position(e.position.x - 64, e.position.y);
                                Position right = new Position(e.position.x + 64, e.position.y);
                                if(BTools.getDistance(left, Trident.mouseWorldPos) < 16){
                                    lDist = BTools.getDistance(left, Trident.mouseWorldPos);
                                }
                                if(BTools.getDistance(right, Trident.mouseWorldPos) < 16){
                                    rDist = BTools.getDistance(right, Trident.mouseWorldPos);
                                }
                                if(Math.min(lDist, rDist) <= 16){
                                    if(lDist < rDist) position = left;
                                    else position = right;
                                }
                            }
                        }
                    }
                }
            }
        }else{
            position = new Position(-1000, -1000);
        }
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
