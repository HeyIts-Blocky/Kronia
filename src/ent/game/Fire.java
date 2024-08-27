package ent.game;

import blib.game.ParticleEmitter;
import blib.util.Position;
import custom.Item;
import ent.*;
import trident.TridEntity;
import trident.Trident;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Fire extends GameObject{

    public static int[] canBurn = {
            GameObject.WORKBENCH,
            GameObject.TREE,
            GameObject.COW,
            GameObject.SLIME,
            GameObject.CAVESLIME,
            GameObject.BABYSLIME,
            GameObject.APEXSLIME,
            GameObject.CRATE,
            GameObject.DONTSAVE,
            GameObject.LOGWALL,
            GameObject.SCP999,
    };

    // data: {life, index of entity on fire}
    GameObject obj;
    long cooldown = 0;
    ParticleEmitter emitter;
    long extraLife = 4000;

    // Constructor, runs when the entity is created
    public Fire(Position pos, int[] dat){
        super(pos, 30, GameObject.FIRE, dat, new Dimension(0, 0));
        weakness = Item.T_NULL;
        obj = (GameObject)Trident.getEntities().get(dat[1]);

        emitter = new ParticleEmitter(new Position(0, 0), 73, 2, true);
        emitter.spawnSquares = true;
        emitter.spawnCircles = false;
        emitter.spawnImages = false;
        emitter.speed = 0.25;
        emitter.size = 9;
        emitter.minLife = 1323;
        emitter.maxLife = 3839;
        emitter.fadeOut = true;
        emitter.spawnRect = new Dimension(32, 32);
        ArrayList<Color> squareColors = new ArrayList<Color>();
        squareColors.add(new Color(227, 81, 28));
        squareColors.add(new Color(251, 255, 0));
        squareColors.add(new Color(52, 52, 52));
        emitter.worldBased = true;

        emitter.squareColors = squareColors;

        renderType = ABOVE;

    }

    public static void setOnFire(GameObject ent, int time){
        for(TridEntity e: Trident.getEntities()){
            if(e instanceof Fire){
                Fire f = (Fire)e;
                if(f.obj.equals(ent)){
                    f.data[0] = Math.max(time, f.data[0]);
                    return;
                }
            }
        }
        int[] dat = {time, Trident.getEntities().indexOf(ent)};
        if(dat[1] == -1) return;
        Trident.spawnEntity(new Fire(new Position(), dat));
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        emitter.render(g, panel, x, y - 14);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        boolean burn = false;
        for(int i: canBurn){
            if(i == obj.id) burn = true;
        }
        if(!burn){
            Trident.destroy(this);
            return;
        }

        cooldown -= elapsedTime;
        if(cooldown <= 0){
            if(obj.health > 0){
                obj.damage(1, true);
                if(!emitter.running) emitter.start();
            }else data[0] = 0;
            cooldown = 500;
        }

        position = obj.position;
        emitter.position = position;
        emitter.update(elapsedTime);

        data[1] = Trident.getEntities().indexOf(obj);
        if(data[1] == -1) Trident.destroy(this);
        data[0] -= elapsedTime;
        if(data[0] <= 0){
            extraLife -= elapsedTime;
            emitter.stop();
            if(extraLife <= 0) Trident.destroy(this);
        }
    }
    public void dropItems(){
        GameObject.dropNoDelay(Item.ANVIL, 1, position);
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }

}
