package ent;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.util.Position;
import custom.GameData;
import custom.Item;
import trident.TridEntity;
import trident.Trident;
public class ItemRenderer extends TridEntity {

    // Constructor, runs when the entity is created
    public ItemRenderer(Position pos){
        super(pos);
        alwaysRender = true;
    }
    // Registry constructor, used only for adding to the registry
    public ItemRenderer(){
        super("itemrend", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new ItemRenderer(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(GameData.inventory[GameData.selHotbar].id == Item.NOTHING || GameData.inventory[GameData.selHotbar].getType() == Item.T_MACE) return;
        if(GameData.atkTimer < GameData.atkTime){
            Point off = Trident.getShakeOffset();

            double dir = GameData.atkDir;
            if(GameData.getSelItem().getType() != Item.T_RANGED 
                && GameData.getSelItem().getType() != Item.T_CONSUMABLE 
                    && GameData.getSelItem().getType() != Item.T_EFFECT) 
                        dir += Math.toRadians((120 * (GameData.atkTimer / (double)GameData.atkTime)) - 50);
            ImageIcon img = Item.getImg(GameData.inventory[GameData.selHotbar].id);
            AffineTransform transform = new AffineTransform();
            transform.rotate(dir, 32, 32);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            BufferedImage bimg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            BufferedImage bimg2 = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            img.paintIcon(null, bimg2.getGraphics(), 32, 16);
            bimg = op.filter(bimg2, bimg);

            Position offset = Trident.getPlr().getOffset();
            boolean smol = (GameData.inventory[GameData.selHotbar].getType() == Item.T_CONSUMABLE 
                            || GameData.inventory[GameData.selHotbar].getType() == Item.T_NULL 
                            || GameData.getSelItem().getType() == Item.T_EFFECT);
            g.drawImage(bimg, Trident.getFrameWidth() / 2 - (32 / (smol ? 2 : 1)) - (int)offset.x - off.x, Trident.getFrameHeight() / 2 - (32 / (smol ? 2 : 1)) - (int)offset.y - off.y, (64 / (smol ? 2 : 1)), (64 / (smol ? 2 : 1)), null); 
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        position = new Position(Trident.getPlrPos().x, Trident.getPlrPos().y + ((GameData.atkDir > 0) ? 17 : -1));
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
