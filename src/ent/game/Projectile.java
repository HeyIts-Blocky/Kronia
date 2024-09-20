package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import blib.util.BTools;
import blib.util.Position;
import custom.GameData;
import custom.Item;
import ent.GameObject;
import trident.TridEntity;
import trident.Trident;
public class Projectile extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/coalOre.png");
    double dir = 0;
    double speed;
    long life = 2000;
    TridEntity thrower = null;

    // Constructor, runs when the entity is created
    public Projectile(Position pos, int[] data){
        super(pos, 99999, GameObject.DONTSAVE, data, new Dimension(0, 0));
        double ang = data[0] / 10.0;
        dir = Math.toRadians(ang);
        speed = data[2] / 100.0;
    }
    public Projectile(Position pos, int[] data, TridEntity t){
        super(pos, 99999, GameObject.DONTSAVE, data, new Dimension(0, 0));
        double ang = data[0] / 10.0;
        dir = Math.toRadians(ang);
        speed = data[2] / 100.0;
        thrower = t;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        ImageIcon img = Item.getImg((short)data[1]);
        AffineTransform transform = new AffineTransform();
        transform.rotate(dir, 32, 32);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage bimg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        BufferedImage bimg2 = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        img.paintIcon(null, bimg2.getGraphics(), 32, 16);
        bimg = op.filter(bimg2, bimg);

        g.drawImage(bimg, x - 32, y - 32, 64, 64, null);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        Position move = BTools.angleToVector(dir);
        move.x *= speed * elapsedTime;
        move.y *= speed * elapsedTime;
        position.x += move.x;
        position.y += move.y;

        if(thrower != null && BTools.getDistance(Trident.getPlrPos(), position) < 32){
            GameData.damage(data[3]);
            Trident.destroy(this);
            return;
        }

        for(int j = 0; j < Trident.getEntities().size(); j++){ // damage other entities
            TridEntity e = Trident.getEntities().get(j);
            if((e instanceof GameObject) && !e.equals(this)){
                boolean canHit = false;
                if(((GameObject)e).projectileHit != null){
                    if(((GameObject)e).projectileHit.contains(position.toPoint())) canHit = true;
                }else if(BTools.getDistance(e.position, position) < 32) canHit = true;
                if(canHit){
                    if(thrower != null && e.equals(thrower)) continue;
                    boolean canAttack = false;
                    canAttack = ((GameObject)e).weakness == Item.T_SWORD;
                    if(canAttack){
                        boolean fire = false;
                        if(data[4] > 0){
                            Fire.setOnFire((GameObject)e, data[4] * 1000);
                            fire = true;
                        }
                        ((GameObject)e).damage(data[3], fire);
                        Trident.destroy(this);
                        return;
                    }
                }

            }
        }

        Rectangle coll = new Rectangle((int)position.x - 32, (int)position.y - 16, 64, 48);
        for(Rectangle r: Trident.getCollision()){
            if(r.intersects(coll)){
                Trident.destroy(this);
                return;
            }
        }

        life -= elapsedTime;
        if(life <= 0){
            Trident.destroy(this);
        }
    }
    public void dropItems(){
        
    }
    public void tookDamage(int amount){

    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }
}
