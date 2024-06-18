package ent.game;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
import trident.*;
public class Slime extends GameObject {

    public static int[] cantDamage = {
        GameObject.SLIME,
        GameObject.BABYSLIME,
        GameObject.CAVESLIME,
        GameObject.ITEM,
    };

    AnimImage img = new AnimImage("data/images/ent/slime.png", 4, 4, 32, 32);
    boolean isRight = true;

    Position targetPos = null;
    long moveTime;
    int range = 100;
    double speed = 0.35;

    boolean attacked = false;

    int damage = 5;

    // Constructor, runs when the entity is created
    public Slime(Position pos){
        super(pos, 15, GameObject.SLIME, new Dimension(0, 0));
        img.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(2000, 4000);
    }
    public Slime(Position pos, int hp){
        super(pos, hp, GameObject.SLIME, 15, new Dimension(0, 0));
        img.resize(64, 64);
        weakness = Item.T_SWORD;
        moveTime = BTools.randInt(2000, 4000);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        
        img.paint(panel, g, x - 32, y - 48);

        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(BTools.getDistance(position, Trident.getPlrPos()) > 1000 || !Background.isInDimension(this)){
            Trident.destroy(this);
            return;
        }
        position.x = BTools.clamp(position.x, 10, 10000 - 10);
        position.y = BTools.clamp(position.y, 10, 10000 - 10);
        img.update(elapsedTime);
        if(moveTime < 1000) img.update(elapsedTime); // speed up if about to move
        if(targetPos == null){
            // no target
            moveTime -= elapsedTime;
            if(moveTime <= 0){
                // get new target pos
                moveTime = BTools.randInt(2000, 4000);
                targetPos = BTools.getDirectionTrig(position, Trident.getPlrPos());
                targetPos.x *= range;
                targetPos.y *= range;
                targetPos.x += position.x;
                targetPos.y += position.y;
                clampTarget();
            }
        }else{
            Position startPos = position.copy();
            Position move = BTools.getDirectionTrig(position, targetPos);
            move.x *= speed * elapsedTime;
            move.y *= speed * elapsedTime;
            position.x += move.x;
            position.y += move.y;
            
            GameObject dmgObj = null;
            for(int j = 0; j < Trident.getEntities().size(); j++){ // damage other entities
                TridEntity e = Trident.getEntities().get(j);
                if(!attacked && BTools.getDistance(e.position, position) < 128 && (e instanceof GameObject) && !e.equals(this)){
                    boolean canDamage = true;
                    for(int i: cantDamage){
                        GameObject go = (GameObject)e;
                        if(i == go.id) canDamage = false;
                    }
                    if(canDamage){
                        if(dmgObj == null || BTools.getDistance(position, dmgObj.position) > BTools.getDistance(position, e.position)){
                            dmgObj = (GameObject)e;
                        }
                    }
                }
            }
            if(dmgObj != null && !attacked){
                dmgObj.damage(damage);
                attacked = true;
            }

            Rectangle coll = new Rectangle((int)position.x - 32, (int)position.y - 16, 64, 48);
            for(Rectangle r: Trident.getCollision()){
                if(r.intersects(coll)){
                    position = startPos;
                    targetPos = null;
                    attacked = false;
                    return;
                }
            }

            if(BTools.getDistance(position, Trident.getPlrPos()) < 32){
                GameData.damage(damage);
            }
            
            if(BTools.getDistance(position, targetPos) < 10){
                targetPos = null;
                attacked = false;
            }
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.JELLY, BTools.randInt(2, 6), position);
    }
    public void tookDamage(int amount){
        
    }

    public void clampTarget(){
        // assumes targetPos is not null
        targetPos.x = BTools.clamp(targetPos.x, 15, 10000 - 15);
        targetPos.y = BTools.clamp(targetPos.y, 15, 10000 - 15);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
