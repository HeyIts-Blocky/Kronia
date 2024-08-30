package ent.game.boss;

import blib.game.Light;
import blib.util.*;
import custom.Achievement;
import custom.GameData;
import custom.Item;
import custom.Settings;
import custom.WorldManager;
import ent.*;
import javax.swing.*;
import java.awt.*;
import trident.*;
import ent.game.*;
public class BigMole extends Boss{

    boolean faceRight = false;
    AnimImage[] imgL = {
        new AnimImage("data/images/ent/boss/moleBoss/idleL.png", 4, 8, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/readyL.png", 4, 12, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/atkL.png", 4, 12, 64, 64),
    };
    AnimImage[] imgR = {
        new AnimImage("data/images/ent/boss/moleBoss/idleR.png", 4, 8, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/readyR.png", 4, 12, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/atkR.png", 4, 12, 64, 64),
    };
    int status = 0;

    boolean secondPhase = false;
    boolean playedSound = false;

    Light light = new Light(new Position(), 250);
    
    public BigMole(Position pos){
        super(pos, GameObject.BIGMOLE, 1000, 25, 1);
        for(AnimImage i: imgL) i.resize(128, 128);
        for(AnimImage i: imgR) i.resize(128, 128);
        imgL[1].loop = false;
        imgR[1].loop = false;
        mapColor = Color.white;
        minMoveTime = 1000;
        maxMoveTime = 2500;
        range = 250;
        projectileHit = new Rectangle(-64, -92, 128, 100);
        Trident.addLight(light);
        defense = 3;
    }

    public void render(Graphics g, JPanel panel, int x, int y){
        if(faceRight) imgR[status].paint(panel, g, x - 64, y - 120);
        else imgL[status].paint(panel, g, x - 64, y - 120);
        super.render(g, panel, x, y);
    }

    public void tookDamage(int amount){
        super.tookDamage(amount);

        if(health <= 0){
            Trident.removeLight(light);

            Trident.printError("mole achievement WIP");
        }
    }

    public void dropItems(){
        GameObject.dropNoDelay(Item.DRILLHEAD, 1, position);
        GameObject.dropNoDelay(Item.RAWMEAT, BTools.randInt(9, 14), position);
        GameObject.dropNoDelay(Item.STONE, BTools.randInt(24, 32), position);
        GameObject.dropNoDelay(Item.IRONORE, BTools.randInt(13, 16), position);
    }

    public void update(long elapsedTime){
        // ** update images **
        imgL[0].update(elapsedTime);
        imgR[0].update(elapsedTime);
        // ***

        if(BTools.getDistance(position, Trident.getPlrPos()) > 1500){
            Trident.destroy(this);
            return;
        }
        int[] clamp = Background.getClampPos();
        position.x = BTools.clamp(position.x, clamp[0], clamp[1]);
        position.y = BTools.clamp(position.y, clamp[2], clamp[3]);

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
            faceRight = (Trident.getPlrPos().x > position.x);
            // no target
            moveTime -= elapsedTime;
            if(moveTime <= 0){
                if(status == 0){
                    // still in idle
                    imgL[1].currentFrame = 0;
                    imgL[1].timer = 0;
                    imgR[1].currentFrame = 0;
                    imgR[1].timer = 0;

                    status = 1;
                }
                if(status == 1){
                    if(imgL[1].currentFrame < imgL[1].frameCount - 1){
                        imgL[1].update(elapsedTime);
                        imgR[1].update(elapsedTime);
                    }else{
                        // get new target pos
                        moveTime = BTools.randInt(minMoveTime, maxMoveTime);
                        targetPos = BTools.getDirectionTrig(position, Trident.getPlrPos());
                        targetPos.x *= range;
                        targetPos.y *= range;
                        targetPos.x += position.x;
                        targetPos.y += position.y;
                        clampTarget();
                        status = 2;
                    }
                    
                }
                
            }
        }else{
            imgL[2].update(elapsedTime);
            imgR[2].update(elapsedTime);

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
                    if(canDamage){
                        GameObject dmgObj = (GameObject)e;
                        dmgObj.damage(damage);
                    }
                }
            }

            Rectangle coll = new Rectangle((int)position.x - 32, (int)position.y - 16, 64, 48);
            for(Rectangle r: Trident.getCollision()){
                if(r.intersects(coll)){
                    position = startPos;
                    targetPos = null;
                    status = 0;
                    return;
                }
            }

            if(BTools.getDistance(position, Trident.getPlrPos()) < 32){
                GameData.damage(damage);
            }
            
            if(BTools.getDistance(position, targetPos) < 10){
                targetPos = null;
                status = 0;
            }
        }

        projectileHit.x = (int)position.x - 64;
        projectileHit.y = (int)position.y - 92;

        if(targetPos != null && !playedSound){
            Settings.playSound("data/sound/moleBossAtk.wav");
            playedSound = true;
        }
        if(targetPos == null) playedSound = false;

        
        if(health < maxHealth / 2 && !secondPhase){
            minMoveTime = 750;
            maxMoveTime = 1500;
            range = 400;
            damage = 35;
        }
    }
}
