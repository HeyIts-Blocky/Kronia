package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
import trident.*;
public class Cow extends GameObject {

    ImageIcon idleR = new ImageIcon("data/images/ent/cow/idleR.png");
    ImageIcon idleL = new ImageIcon("data/images/ent/cow/idleL.png");
    AnimImage walkR = new AnimImage("data/images/ent/cow/walkR.png", 4, 6, 32, 32);
    AnimImage walkL = new AnimImage("data/images/ent/cow/walkL.png", 4, 6, 32, 32);
    boolean isRight = true;

    Position targetPos = null;
    long moveTime;
    int range = 100;
    double speed = 0.1;

    // Constructor, runs when the entity is created
    public Cow(Position pos){
        super(pos, 20, GameObject.COW, new Dimension(0, 0));
        BTools.resizeImgIcon(idleR, 64, 64);
        BTools.resizeImgIcon(idleL, 64, 64);
        walkR.resize(64, 64);
        walkL.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(750, 2000);
    }
    public Cow(Position pos, int hp){
        super(pos, hp, GameObject.COW, 20, new Dimension(0, 0));
        BTools.resizeImgIcon(idleR, 64, 64);
        BTools.resizeImgIcon(idleL, 64, 64);
        walkR.resize(64, 64);
        walkL.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(750, 2000);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        
        if(targetPos != null){
            isRight = (targetPos.x > position.x);
        }

        if(targetPos == null){
            if(isRight) idleR.paintIcon(panel, g, x - 32, y - 47);
            else idleL.paintIcon(panel, g, x - 32, y - 47);
        }else{
            if(isRight) walkR.paint(panel, g, x - 32, y - 47);
            else walkL.paint(panel, g, x - 32, y - 47);
        }

        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(BTools.getDistance(position, Trident.getPlrPos()) > 1000 || !Background.isInDimension(this)) return;
        walkR.update(elapsedTime);
        walkL.update(elapsedTime);
        if(targetPos == null){
            // no target
            moveTime -= elapsedTime;
            if(moveTime <= 0){
                // get new target pos
                moveTime = BTools.randInt(750, 2000);
                int x = BTools.randInt((int)(position.x - range), (int)(position.x + range));
                int y = BTools.randInt((int)(position.y - range), (int)(position.y + range));
                targetPos = new Position(x, y);
                clampTarget();
            }
        }else{
            Position startPos = position.copy();
            Position move = BTools.getDirectionTrig(position, targetPos);
            move.x *= speed * elapsedTime;
            move.y *= speed * elapsedTime;
            position.x += move.x;
            position.y += move.y;

            Rectangle coll = new Rectangle((int)position.x - 32, (int)position.y - 16, 64, 48);
            for(Rectangle r: Trident.getCollision()){
                if(r.intersects(coll)){
                    position = startPos;
                    targetPos = null;
                    return;
                }
            }
            
            if(BTools.getDistance(position, targetPos) < 10){
                targetPos = null;
            }
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.RAWMEAT, BTools.randInt(2, 6), position);
    }
    public void tookDamage(int amount){
        moveTime = BTools.randInt(750, 2000);
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
