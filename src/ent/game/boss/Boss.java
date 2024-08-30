package ent.game.boss;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import blib.util.BTools;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import custom.MusicManager;
import custom.Settings;
import custom.WorldManager;
import ent.Background;
import ent.BossVFX;
import ent.GameObject;
import trident.TridEntity;
import trident.Trident;
public class Boss extends GameObject {

    public static boolean canSpawn(int id){
        if(id == GameObject.APEXSLIME){
            if(Background.bg == Background.SURFACE) return true;
        }
        if(id == GameObject.BIGMOLE){
            if(Background.bg == Background.MINES) return true;
        }

        return false;
    }

    public static int[] cantDamage = {
        GameObject.SLIME,
        GameObject.BABYSLIME,
        GameObject.CAVESLIME,
        GameObject.DONTSAVE, // mainly for projectiles, hopefully nothing else uses this id that bosses should hit...
        GameObject.FIRE,
        GameObject.MOLE,
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
            Trident.shakeCam(1);
            Trident.spawnEntity(new BossVFX(position.copy(), id));
        }
    }

    public void clampTarget(){
        // assumes targetPos is not null
        int[] clamp = Background.getClampPos();
        targetPos.x = BTools.clamp(targetPos.x, clamp[0], clamp[1]);
        targetPos.y = BTools.clamp(targetPos.y, clamp[2], clamp[3]);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
