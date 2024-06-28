package ent;

import blib.util.*;
import trident.*;
import blib.game.*;
public class GlowEffect extends TridEntity {

    public static boolean glow = false;
    Light light = null;
    long pulseTime = 0;

    // Constructor, runs when the entity is created
    public GlowEffect(Position pos){
        super(pos);
        glow = true;
        light = new Light(pos, 200);
        Trident.addLight(light);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        pulseTime += elapsedTime;
        light.radius = 200 + (int)(Math.sin(pulseTime / 1000.0) * 50);

        light.position = Trident.getPlrPos();
        if(!glow){
            Trident.removeLight(light);
            Trident.destroy(this);
        }
    }
}
