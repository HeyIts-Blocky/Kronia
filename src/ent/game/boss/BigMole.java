package ent.game.boss;

import blib.game.Light;
import blib.util.*;
import custom.Achievement;
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
        new AnimImage("data/images/ent/boss/moleBoss/readyL.png", 4, 8, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/atkL.png", 4, 8, 64, 64),
    };
    AnimImage[] imgR = {
        new AnimImage("data/images/ent/boss/moleBoss/idleR.png", 4, 8, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/readyR.png", 4, 8, 64, 64),
        new AnimImage("data/images/ent/boss/moleBoss/atkR.png", 4, 8, 64, 64),
    };

    boolean secondPhase = false;
    boolean playedSound = false;

    Light light = new Light(new Position(), 250);
    
    public BigMole(Position pos){
        super(pos, GameObject.APEXSLIME, 300, 20, 0);
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
        damage = 25;
    }

    public void render(Graphics g, JPanel panel, int x, int y){
        // img.paint(panel, g, x - 64, y - 120);
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
        GameObject.dropNoDelay(Item.HARDJELLY, BTools.randInt(5, 9), position);
    }

    public void update(long elapsedTime){
        super.update(elapsedTime);

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
