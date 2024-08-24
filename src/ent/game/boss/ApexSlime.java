package ent.game.boss;

import blib.util.*;
import custom.Item;
import custom.Settings;
import ent.*;
import javax.swing.*;
import java.awt.*;
import trident.*;
import ent.game.*;
public class ApexSlime extends Boss{

    AnimImage img = new AnimImage("data/images/ent/boss/apexSlime.png", 6, 6, 64, 64);
    boolean secondPhase = false;
    boolean playedSound = false;
    
    public ApexSlime(Position pos){
        super(pos, GameObject.APEXSLIME, 300, 20, 0);
        img.resize(128, 128);
        mapColor = Color.magenta;
        minMoveTime = 1500;
        maxMoveTime = 3000;
        range = 200;

        projectileHit = new Rectangle(-64, -92, 128, 100);
    }

    public void render(Graphics g, JPanel panel, int x, int y){
        img.paint(panel, g, x - 64, y - 120);
        super.render(g, panel, x, y);
    }

    public void tookDamage(int amount){
        int chance = BTools.randInt(0, 4);
        if(chance == 0){
            Position pos = position.copy();
            pos.x += BTools.randInt(0, 300) - 150;
            pos.y += BTools.randInt(0, 300) - 150;
            Trident.spawnEntity(new BabySlime(pos));
        }
    }

    public void dropItems(){
        GameObject.dropNoDelay(Item.HARDJELLY, BTools.randInt(5, 9), position);
    }

    public void update(long elapsedTime){
        super.update(elapsedTime);
        img.update(elapsedTime);

        projectileHit.x = (int)position.x - 64;
        projectileHit.y = (int)position.y - 92;

        if(targetPos != null && !playedSound){
            Settings.playSound("data/sound/apexAttack.wav");
            playedSound = true;
        }
        if(targetPos == null) playedSound = false;

        if(moveTime < 1000) img.update(elapsedTime);
        if(health < maxHealth / 2 && !secondPhase){
            minMoveTime = 1000;
            maxMoveTime = 2000;
            range = 250;
            damage = 30;
        }
    }
}
