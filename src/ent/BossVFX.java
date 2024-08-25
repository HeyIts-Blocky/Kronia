package ent;

import blib.game.ParticleEmitter;
import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BossVFX extends TridEntity {

    ParticleEmitter emitter;
    long life = 1750;

    // Constructor, runs when the entity is created
    public BossVFX(Position pos){
        super(pos);

        emitter = new ParticleEmitter(new Position(0, 0), 16, 0, true);
        emitter.spawnSquares = false;
        emitter.spawnCircles = true;
        emitter.spawnImages = false;
        emitter.speed = 0.81;
        emitter.size = 27;
        emitter.minLife = 118;
        emitter.maxLife = 613;
        emitter.fadeOut = true;
        ArrayList<Color> circleColors = new ArrayList<Color>();
        circleColors.add(new Color(141, 0, 240));
        circleColors.add(new Color(63, 0, 171));
        circleColors.add(new Color(209, 11, 221));
        emitter.circleColors = circleColors;

    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        emitter.render(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        emitter.update(elapsedTime);
        life -= elapsedTime;
        if(life <= 1000) emitter.stop();
        if(life <= 0) Trident.destroy(this);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
