package ent;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import blib.game.Player;
import blib.util.AnimImage;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import trident.TridEntity;
import trident.Trident;
public class PlayerRenderer extends TridEntity {

    AnimImage[] imgs = {
        new AnimImage("data/images/player/atkN.png", 4, 1, 16, 16),
        new AnimImage("data/images/player/atkS.png", 4, 1, 16, 16),
        new AnimImage("data/images/player/atkE.png", 4, 1, 16, 16),
        new AnimImage("data/images/player/atkW.png", 4, 1, 16, 16),
    };

    // Constructor, runs when the entity is created
    public PlayerRenderer(Position pos){
        super(pos);
        for(AnimImage i: imgs){
            i.resize(32, 32);
        }
    }
    // Registry constructor, used only for adding to the registry
    public PlayerRenderer(){
        super("plrrend", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new PlayerRenderer(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(GameData.spectate) return;
        Point off = Trident.getShakeOffset();
        if(GameData.atkTimer >= GameData.atkTime){
            // not attacking
            Trident.getPlr().render(panel, g, Trident.getFrameWidth() / 2 - off.x, Trident.getFrameHeight() / 2 - off.y);
        }else{
            // attacking
            if(GameData.atkDir >= (-Math.PI / 4) && GameData.atkDir <= (Math.PI / 4)){
                Trident.getPlr().setDirection(Player.EAST);
            }else if(GameData.atkDir >= (Math.PI / 4) && GameData.atkDir <= (Math.PI / 4 * 3)){
                Trident.getPlr().setDirection(Player.SOUTH);
            }else if(GameData.atkDir >= (-Math.PI / 4 * 3) && GameData.atkDir <= (-Math.PI / 4)){
                Trident.getPlr().setDirection(Player.NORTH);
            }else{
                Trident.getPlr().setDirection(Player.WEST);
            }
            AnimImage img = imgs[Trident.getPlr().getDirection()];
            int frame = (int)((GameData.atkTimer / (double)GameData.atkTime) * 4);
            if(GameData.getSelItem().getType() == Item.T_RANGED || GameData.getSelItem().getType() == Item.T_CONSUMABLE) frame = 1;
            img.currentFrame = frame;
            Position offset = Trident.getPlr().getOffset();
            img.paint(panel, g, Trident.getFrameWidth() / 2 - 16 - (int)offset.x - off.x, Trident.getFrameHeight() / 2 - 16 - (int)offset.y - off.y);
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        position = Trident.getPlrPos(); 
        position.y += 16;
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
