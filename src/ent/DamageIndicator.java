package ent;

import blib.util.*;
import custom.GameData;
import custom.Settings;
import trident.*;
import javax.swing.*;
import java.awt.*;
public class DamageIndicator extends TridEntity {

    private static final double gravity = 0.001;
    private Position vector;
    private int dmg;
    private long life = 400;

    // Constructor, runs when the entity is created
    public DamageIndicator(Position pos, int amount){
        super(pos);
        vector = new Position((BTools.randInt(0, 5) - 2) / 10.0, -0.3);
        dmg = amount;
        renderType = ABOVE;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(Settings.assistColor);
        g.setFont(new Font(GameData.getFont(), Font.PLAIN, 15));
        TextBox.draw(dmg + "", g, x, y, TextBox.CENTER);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        position.x += vector.x * elapsedTime;
        position.y += vector.y * elapsedTime;
        vector.y += gravity * elapsedTime;
        life -= elapsedTime;
        if(life <= 0) Trident.destroy(this);
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
