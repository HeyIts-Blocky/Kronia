package ent.game.boss;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import ent.*;
import custom.*;
import trident.*;
public class Boss extends GameObject {

    public static int[] cantDamage = {
        GameObject.SLIME,
        GameObject.BABYSLIME,
        GameObject.CAVESLIME,
    };
    boolean isRight = true;

    public Position targetPos = null;
    public long moveTime;
    public int minMoveTime = 2000, maxMoveTime = 4000;
    public int range = 100;
    double speed = 0.35;

    int damage;

    public Color mapColor = Color.red;

    public static boolean hasBoss(){
        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(e instanceof Boss){
                return true;
            }
        }
        return false;
    }

    // Constructor, runs when the entity is created
    public Boss(Position pos, int id, int maxHP, int dmg, int song){
        super(pos, maxHP, id, new Dimension(0, 0));
        weakness = Item.T_SWORD;
        damage = dmg;
        MusicManager.startBossMusic(song);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        super.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(BTools.getDistance(position, Trident.getPlrPos()) > 1000){
            Trident.destroy(this);
            return;
        }
        position.x = BTools.clamp(position.x, 10, 10000 - 10);
        position.y = BTools.clamp(position.y, 10, 10000 - 10);
        if(targetPos == null){
            // no target
            moveTime -= elapsedTime;
            if(moveTime <= 0){
                // get new target pos
                moveTime = BTools.randInt(minMoveTime, maxMoveTime);
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
            
            for(int j = 0; j < Trident.getEntities().size(); j++){ // damage other entities
                TridEntity e = Trident.getEntities().get(j);
                if(BTools.getDistance(e.position, position) < 128 && (e instanceof GameObject) && !e.equals(this)){
                    boolean canDamage = true;
                    for(int i: cantDamage){
                        GameObject go = (GameObject)e;
                        if(i == go.id) canDamage = false;
                    }
                    if(canDamage) ((GameObject)e).damage(damage);
                }
            }

            Rectangle coll = new Rectangle((int)position.x - 32, (int)position.y - 16, 64, 48);
            for(Rectangle r: Trident.getCollision()){
                if(r.intersects(coll)){
                    position = startPos;
                    targetPos = null;
                    return;
                }
            }

            if(BTools.getDistance(position, Trident.getPlrPos()) < 32){
                GameData.damage(damage);
            }
            
            if(BTools.getDistance(position, targetPos) < 10){
                targetPos = null;
            }
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.JELLY, 1, position);
    }
    public void tookDamage(int amount){
        if(health <= 0){
            Settings.playSound("data/sound/bossDeath.wav");
        }
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
