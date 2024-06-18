package custom;

import javax.swing.*;

import blib.util.BTools;
public class Effect {
    
    // effect IDs and data
    public static final int NONE = -1, POISON = 0, FOODPOISON = 1;
    private static long[] defaultTime = {
        5000, // 0
        10000, // 1
    };
    private static long[] coolTimes = {
        1000, // 0
        1000, // 1
    };
    private static String[] names = {
        "Poison", // 0
        "Food Poisoning", // 1
    };
    private static ImageIcon[] imgs = {
        new ImageIcon("data/images/effects/poison.png"), // 0
        new ImageIcon("data/images/effects/foodPoisoning.png"), // 1
    };
    public static ImageIcon slot = new ImageIcon("data/images/effects/effectSlot.png");


    public int id;
    public long time, maxTime; // lifetime
    public long coolTimer, cooldown; // update frequency


    public Effect(int id){
        this.id = id;
        maxTime = defaultTime[id];
        cooldown = coolTimes[id];
    }
    public Effect(int id, long life){
        this.id = id;
        maxTime = life;
        cooldown = coolTimes[id];
    }
    public Effect(int id, long t, long mt, long ctimer, long cool){
        this.id = id;
        time = t;
        maxTime = mt;
        coolTimer = ctimer;
        cooldown = cool;
    }

    public void update(long elapsedTime){
        time += elapsedTime;
        coolTimer += elapsedTime;

        if(coolTimer >= cooldown){
            runEffect();
            coolTimer = 0;
        }

        if(time >= maxTime){
            effectEnd();
        }
    }

    public void runEffect(){
        switch(id){
        case POISON:
            GameData.forceDamage(5);
            break;
        case FOODPOISON:
            GameData.hunger -= 5;
            GameData.hungerSpeed = 2;
            break;
        }
    }

    public void effectEnd(){
        switch(id){
        
        }
    }

    public String getName(){
        return names[id];
    }

    public ImageIcon getImg(){
        return imgs[id];
    }

    public static void resizeImgs(){
        for(ImageIcon img: imgs){
            BTools.resizeImgIcon(img, 20, 20);
        }
        BTools.resizeImgIcon(slot, 28, 28);
    }
}
