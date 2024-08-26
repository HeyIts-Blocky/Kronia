package ent.game;

import blib.util.BTools;
import blib.util.Position;
import custom.Item;
import ent.*;
import trident.Trident;

import javax.swing.*;
import java.awt.*;

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

    // Constructor, runs when the entity is created
    public Fire(Position pos, int[] dat){
        super(pos, 30, GameObject.FIRE, dat, new Dimension(0, 0));
        weakness = Item.T_NULL;
        obj = (GameObject)Trident.getEntities().get(dat[1]);
    }

    public static void setOnFire(GameObject ent, int time){

    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){

    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){

        cooldown -= elapsedTime;
        if(cooldown <= 0){
            obj.damage(1);
            cooldown = 500;
        }

        data[1] = Trident.getEntities().indexOf(obj);
        data[0] -= elapsedTime;
        if(data[0] <= 0){
            Trident.destroy(this);
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
