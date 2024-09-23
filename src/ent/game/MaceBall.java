package ent.game;

import java.awt.Dimension;
import java.awt.Graphics;
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
public class MaceBall extends GameObject {

    ImageIcon img = new ImageIcon("data/images/ent/maceBall.png");
    double dir = 0;
    double speed;
    long life = 100000;
    TridEntity thrower = null;
    long atkCooldown = 0;
    boolean returning = false;

    ImageIcon chainImg = new ImageIcon("data/images/ent/maceChain.png");

    // Constructor, runs when the entity is created
    public MaceBall(Position pos, int[] data){
        super(pos, 99999, GameObject.DONTSAVE, data, new Dimension(0, 0));
        double ang = data[0] / 10.0;
        dir = Math.toRadians(ang);
        speed = data[2] / 100.0;
        BTools.resizeImgIcon(img, 32, 32);
        BTools.resizeImgIcon(chainImg, 25, 8);
        renderType = ABOVE;
    }
    public MaceBall(Position pos, int[] data, TridEntity t){
        super(pos, 99999, GameObject.DONTSAVE, data, new Dimension(0, 0));
        double ang = data[0] / 10.0;
        dir = Math.toRadians(ang);
        speed = data[2] / 100.0;
        thrower = t;
        BTools.resizeImgIcon(img, 32, 32);
        BTools.resizeImgIcon(chainImg, 25, 8);
        renderType = ABOVE;
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        AffineTransform chainTransform = new AffineTransform();
        chainTransform.rotate(BTools.getAngle(Trident.getPlrPos(), position), Trident.getFrameWidth() / 2, Trident.getFrameHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(chainTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        Position throwPos = Trident.getPlrPos();
        if(thrower != null) throwPos = thrower.position;
        int throwDist = (int)BTools.getDistance(position, throwPos);
        BufferedImage chain = new BufferedImage(throwDist, 8, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < throwDist / 8 + 1; i++){
            chainImg.paintIcon(null, chain.getGraphics(), i * 25, 0);
        }
        BufferedImage rotatedImg = new BufferedImage((int)(Trident.getFrameWidth() * 1.5), Trident.getFrameHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage positionedImg = new BufferedImage((int)(Trident.getFrameWidth() * 1.5), Trident.getFrameHeight(), BufferedImage.TYPE_INT_ARGB);
        positionedImg.getGraphics().drawImage(chain, Trident.getFrameWidth() / 2, Trident.getFrameHeight() / 2 - 4, null);
        rotatedImg = op.filter(positionedImg, rotatedImg);
        g.drawImage(rotatedImg, -Trident.getShakeOffset().x - (int)Trident.getPlr().getOffset().x, -Trident.getShakeOffset().y - (int)Trident.getPlr().getOffset().y, null);



        AffineTransform transform = new AffineTransform();
        transform.rotate(dir, 32, 32);
        op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
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

        if(atkCooldown > 0) atkCooldown -= elapsedTime;
        if(atkCooldown <= 0){
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
                            ((GameObject)e).damage(returning ? 1 : data[3]);
                            atkCooldown = (returning ? 500 : 100);
                        }
                    }

                }
            }
        }


        Position throwPos = Trident.getPlrPos();
        if(thrower != null) throwPos = thrower.position;
        if(BTools.getDistance(position, throwPos) > data[1]){
            returning = true;
        }
        if(returning) {
            dir = BTools.getAngle(position, throwPos);
            if(BTools.getDistance(position, throwPos) < 16){
                Trident.destroy(this);
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


    public static boolean maceOut(){
        for(int i = 0; i < Trident.getEntities().size(); i++){
            if(Trident.getEntities().get(i) instanceof MaceBall && ((MaceBall)Trident.getEntities().get(i)).thrower == null) return true;
        }
        return false;
    }
}
