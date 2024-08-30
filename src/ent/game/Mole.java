package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import blib.util.AnimImage;
import blib.util.BTools;
import blib.util.Position;
import custom.Item;
import custom.WorldManager;
import ent.Background;
import ent.GameObject;
import trident.Trident;
public class Mole extends GameObject {

    AnimImage idleR = new AnimImage("data/images/ent/mole/idleR.png", 4, 8, 32, 32);
    AnimImage idleL = new AnimImage("data/images/ent/mole/idleL.png", 4, 8, 32, 32);
    AnimImage walkR = new AnimImage("data/images/ent/mole/moveR.png", 4, 12, 32, 32);
    AnimImage walkL = new AnimImage("data/images/ent/mole/moveL.png", 4, 12, 32, 32);
    AnimImage atkR = new AnimImage("data/images/ent/mole/atkR.png", 4, 12, 32, 32);
    AnimImage atkL = new AnimImage("data/images/ent/mole/atkL.png", 4, 12, 32, 32);
    boolean isRight = true;

    Position targetPos = null;
    long moveTime;
    int range = 150;
    double speed = 0.25;

    // Constructor, runs when the entity is created
    public Mole(Position pos){
        super(pos, 15, GameObject.MOLE, new Dimension(0, 0));
        walkR.resize(64, 64);
        walkL.resize(64, 64);
        idleR.resize(64, 64);
        idleL.resize(64, 64);
        atkR.resize(64, 64);
        atkL.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(750, 2000);
        defense = 3;
    }
    public Mole(Position pos, int hp){
        super(pos, hp, GameObject.MOLE, 15, new Dimension(0, 0));
        walkR.resize(64, 64);
        walkL.resize(64, 64);
        idleR.resize(64, 64);
        idleL.resize(64, 64);
        atkR.resize(64, 64);
        atkL.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(750, 2000);
        defense = 3;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        
        if(targetPos != null){
            isRight = (targetPos.x > position.x);
        }

        if(targetPos == null){
            if(BTools.getDistance(position, Trident.getPlrPos()) < range){
                if(isRight) atkR.paint(panel, g, x - 32, y - 28);
                else atkL.paint(panel, g, x - 32, y - 28);
            }else{
                if(isRight) idleR.paint(panel, g, x - 32, y - 28);
                else idleL.paint(panel, g, x - 32, y - 28);
            }
            
        }else{
            if(isRight) walkR.paint(panel, g, x - 32, y - 28);
            else walkL.paint(panel, g, x - 32, y - 28);
        }

        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(BTools.getDistance(position, Trident.getPlrPos()) > 1000 || !Background.isInDimension(this)){
            Trident.destroy(this);
            return;
        }
        walkR.update(elapsedTime);
        walkL.update(elapsedTime);
        idleR.update(elapsedTime);
        idleL.update(elapsedTime);
        atkR.update(elapsedTime);
        atkL.update(elapsedTime);

        // difficulty modifier
        if(WorldManager.difficulty == WorldManager.V_EASY){
            elapsedTime *= 0.8;
        }
        if(WorldManager.difficulty == WorldManager.HARD){
            elapsedTime *= 1.3;
        }
        if(WorldManager.difficulty == WorldManager.V_HARD){
            elapsedTime *= 1.5;
        }

        if(targetPos == null){
            // no target
            moveTime -= elapsedTime;
            if(moveTime <= 0){
                if(BTools.getDistance(position, Trident.getPlrPos()) < range){
                    moveTime = BTools.randInt(1500, 2000);

                    // Throw Projectile
                    double dir = BTools.getAngle(position, Trident.getPlrPos());
                    int ang = (int)(Math.toDegrees(dir) * 10);
                    int[] data = {ang, Item.PEBBLE, 35, 5, 0};
                    Trident.spawnEntity(new Projectile(position.copy(), data, this));

                    return;
                }
                // get new target pos
                moveTime = BTools.randInt(1500, 2000);
                targetPos = Trident.getPlrPos();
                clampTarget();
            }
        }else{
            Position move = BTools.getDirectionTrig(position, targetPos);
            move.x *= speed * elapsedTime;
            move.y *= speed * elapsedTime;
            position.x += move.x;
            position.y += move.y;
            
            if(BTools.getDistance(position, targetPos) < range){
                targetPos = null;
            }
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.RAWMEAT, BTools.randInt(0, 3), position);
        GameObject.dropNoDelay(Item.STONE, BTools.randInt(3, 6), position);
        int chance = BTools.randInt(0, 20);
        if(chance == 1){
            GameObject.dropNoDelay(Item.ANTENNA, 1, position);
        }
    }
    public void tookDamage(int amount){
        moveTime = 500;
        double dir = BTools.getAngle(Trident.getPlrPos(), position);
        targetPos = BTools.angleToVector(dir);
        targetPos.x *= range * 3;
        targetPos.y *= range * 3;
        targetPos.x += position.x;
        targetPos.y += position.y;
        clampTarget();
    }

    public void clampTarget(){
        // assumes targetPos is not null
        int[] clamps = Background.getClampPos();
        targetPos.x = BTools.clamp(targetPos.x, clamps[0], clamps[1]);
        targetPos.y = BTools.clamp(targetPos.y, clamps[2], clamps[3]);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
