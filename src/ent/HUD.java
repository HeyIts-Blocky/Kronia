package ent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blib.anim.Animation;
import blib.anim.Animator;
import blib.util.BTools;
import blib.util.Position;
import blib.util.TextBox;
import custom.Dialog;
import custom.Effect;
import custom.GameData;
import custom.Item;
import custom.Recipe;
import custom.Settings;
import trident.TridEntity;
import trident.Trident;
public class HUD extends TridEntity {

    ImageIcon img = new ImageIcon("data/images/hotbar.png");
    ImageIcon invImg = new ImageIcon("data/images/inventory.png");
    ImageIcon crateImg = new ImageIcon("data/images/inventory chest.png");
    BufferedImage healthMask, hungerMask;
    ImageIcon healthOutline = new ImageIcon("data/images/healthOutline.png");
    ImageIcon hungerOutline = new ImageIcon("data/images/hungerOutline.png");
    Rectangle healthRect = new Rectangle(10, 10, 32, 32);
    Rectangle hungerRect = new Rectangle(10, 42, 32, 32);
    ImageIcon selImg = new ImageIcon("data/images/hotbarSel.png");
    public static Rectangle noDropRect = new Rectangle(15, 237, 520, Trident.getFrameHeight());

    private static int notifType = 0;
    private static String notifText = "";
    private static Animator notifAnim;
    private static Position notifPos = new Position();
    private static ImageIcon[] notifImgs = {
            new ImageIcon("data/images/notif/save.png"),
            new ImageIcon("data/images/notif/autosave.png"),
            new ImageIcon("data/images/notif/exc mark.png"),
            new ImageIcon("data/images/notif/hint.png"),
            new ImageIcon("data/images/notif/music.png"),
            new ImageIcon("data/images/notif/trophy.png"),
    };
    private static ArrayList<Notif> notifs = new ArrayList<Notif>();

    // Constructor, runs when the entity is created
    public HUD(Position pos){
        super(pos);
        renderType = TOPPRIORITY;
        alwaysRender = true;
        BTools.resizeImgIcon(img, Trident.getFrameWidth(), Trident.getFrameHeight());
        BTools.resizeImgIcon(invImg, Trident.getFrameWidth(), Trident.getFrameHeight());
        BTools.resizeImgIcon(crateImg, Trident.getFrameWidth(), Trident.getFrameHeight());
        BTools.resizeImgIcon(healthOutline, 32, 32);
        BTools.resizeImgIcon(hungerOutline, 32, 32);
        BTools.resizeImgIcon(selImg, 32, 8);

        // animator
        notifPos = new Position();
        try{
            ArrayList<Animation> anims = new ArrayList<Animation>();
            anims.add(new Animation("data/animations/notif"));
            anims.add(new Animation("data/animations/notifHidden"));

            notifAnim = new Animator(notifPos, anims);
            notifAnim.play("notifHidden");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading notif animation", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        for(ImageIcon i: notifImgs){
            BTools.resizeImgIcon(i, 16, 16);
        }

        // set up masks
        ImageIcon img = new ImageIcon("data/images/masks/healthMask.png");
        healthMask = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = healthMask.getGraphics();
        g.drawImage(img.getImage(), 0, 0, 32, 32, null);
        img = new ImageIcon("data/images/masks/hungerMask.png");
        hungerMask = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        g = hungerMask.getGraphics();
        g.drawImage(img.getImage(), 0, 0, 32, 32, null);

        Effect.resizeImgs();

        // set up slot rects

        GameData.invBoxes = new ArrayList<Rectangle>();
        GameData.crateBoxes = new ArrayList<Rectangle>();

        for(int i = 0; i < 10; i++){ // hotbar
            GameData.invBoxes.add(new Rectangle(20 + (i * 52), 412, 32, 32));
            GameData.crateBoxes.add(new Rectangle(20 + (i * 52), 412, 32, 32));
        }
        // crate
        for(int i = 0; i < 10; i++){
            GameData.crateBoxes.add(new Rectangle(20 + ((i) * 52), 184, 32, 32));
        }
        for(int i = 10; i < 20; i++){
            GameData.crateBoxes.add(new Rectangle(20 + ((i - 10) * 52), 129, 32, 32));
        }
        for(int i = 20; i < 30; i++){
            GameData.crateBoxes.add(new Rectangle(20 + ((i - 20) * 52), 81, 32, 32));
        }
        // inventory
        for(int i = 10; i < 20; i++){
            GameData.invBoxes.add(new Rectangle(20 + ((i - 10) * 52), 340, 32, 32));
        }
        for(int i = 20; i < 30; i++){
            GameData.invBoxes.add(new Rectangle(20 + ((i - 20) * 52), 289, 32, 32));
        }
        for(int i = 30; i < 40; i++){
            GameData.invBoxes.add(new Rectangle(20 + ((i - 30) * 52), 237, 32, 32));
        }
        // other buttons
        GameData.invBoxes.add(new Rectangle(636, 84, 32, 32));
        GameData.invBoxes.add(new Rectangle(552, 304, 48, 48));
        GameData.invBoxes.add(new Rectangle(604, 304, 48, 48));
        GameData.invBoxes.add(new Rectangle(636, 84 - 52, 32, 32));
        GameData.invBoxes.add(new Rectangle(636, 84 + 52, 32, 32));
        GameData.invBoxes.add(new Rectangle(604, 304 - 52, 48, 48));
    }
    // Registry constructor, used only for adding to the registry
    public HUD(){
        super("hotbar", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new HUD(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        if(GameData.settingsOpen || !GameData.drawHud) return;
        if(GameData.spectate){
            g.setColor(Color.red);
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 50));
            TextBox.draw("You Died!\n\n" + (GameData.deadTime / 1000 + 1), g, Trident.getFrameWidth() / 2, 100, TextBox.CENTER);
            return;
        }

        g.setColor(new Color(1f, 0f, 0f, Math.max((GameData.hurtTime / 4000f), 0f)));
        g.fillRect(0, 0, 700, 500);

        if(!GameData.invOpen) img.paintIcon(panel, g, 0, 0);
        else{
            if(GameData.openCrate != null || GameData.bigCraft){
                crateImg.paintIcon(panel, g, 0, 0);
            }else{
                invImg.paintIcon(panel, g, 0, 0);
            }
        }

        for(int i = 0; i < 10; i++){
            Item it = GameData.inventory[i];
            if(it.id != Item.NOTHING){
                Item.getImg(it.id).paintIcon(panel, g, 20 + (i * 52), 412);
            }
            if(it.amount != 1 && it.id != Item.NOTHING){
                g.setColor(Color.white);
                TextBox.outlineColor = Color.black;
                g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                TextBox.draw(it.amount + "", g, 20 + (i * 52) + 35, 412 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
            }
            if(i == GameData.selHotbar){
                g.setColor(Color.black);
                selImg.paintIcon(panel, g, 20 + (i * 52), 412 + 40);
            }
        }

        if(GameData.invOpen){
            if(GameData.bigCraft){
                ArrayList<Recipe> recipes = Recipe.getRecipes();
                for(int i = 0; i < 10; i++){
                    if(i + GameData.selCraft >= recipes.size()) break;
                    Item it = recipes.get(i + GameData.selCraft).output;
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i) * 52), 184);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i) * 52) + 35, 184 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                    if(!recipes.get(i + GameData.selCraft).canCraft()){
                        g.setColor(new Color(1f, 0f, 0f, 0.5f));
                        g.fillRect(20 + ((i) * 52), 184, 32, 32);
                    }
                }
                for(int i = 10; i < 20; i++){
                    if(i + GameData.selCraft >= recipes.size()) break;
                    Item it = recipes.get(i + GameData.selCraft).output;
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 10) * 52), 129);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i - 10) * 52) + 35, 129 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                    if(!recipes.get(i + GameData.selCraft).canCraft()){
                        g.setColor(new Color(1f, 0f, 0f, 0.5f));
                        g.fillRect(20 + ((i - 10) * 52), 129, 32, 32);
                    }
                }
                for(int i = 20; i < 30; i++){
                    if(i + GameData.selCraft >= recipes.size()) break;
                    Item it = recipes.get(i + GameData.selCraft).output;
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 20) * 52), 81);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i - 20) * 52) + 35, 81 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                    if(!recipes.get(i + GameData.selCraft).canCraft()){
                        g.setColor(new Color(1f, 0f, 0f, 0.5f));
                        g.fillRect(20 + ((i - 20) * 52), 81, 32, 32);
                    }
                }
            }else if(GameData.openCrate != null){
                for(int i = 0; i < 10; i++){
                    Item it = GameData.openCrate.getSlot(i);
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i) * 52), 184);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i) * 52) + 35, 184 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                }
                for(int i = 10; i < 20; i++){
                    Item it = GameData.openCrate.getSlot(i);
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 10) * 52), 129);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i - 10) * 52) + 35, 129 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                }
                for(int i = 20; i < 30; i++){
                    Item it = GameData.openCrate.getSlot(i);
                    if(it.id != Item.NOTHING){
                        Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 20) * 52), 81);
                    }
                    if(it.amount != 1 && it.id != Item.NOTHING){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(it.amount + "", g, 20 + ((i - 20) * 52) + 35, 81 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                }
            }

            for(int i = 10; i < 20; i++){
                Item it = GameData.inventory[i];
                if(it.id != Item.NOTHING){
                    Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 10) * 52), 340);
                }
                if(it.amount != 1 && it.id != Item.NOTHING){
                    g.setColor(Color.white);
                    TextBox.outlineColor = Color.black;
                    g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                    TextBox.draw(it.amount + "", g, 20 + ((i - 10) * 52) + 35, 340 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                }
            }
            for(int i = 20; i < 30; i++){
                Item it = GameData.inventory[i];
                if(it.id != Item.NOTHING){
                    Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 20) * 52), 289);
                }
                if(it.amount != 1 && it.id != Item.NOTHING){
                    g.setColor(Color.white);
                    TextBox.outlineColor = Color.black;
                    g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                    TextBox.draw(it.amount + "", g, 20 + ((i - 20) * 52) + 35, 289 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                }
            }
            for(int i = 30; i < 40; i++){
                Item it = GameData.inventory[i];
                if(it.id != Item.NOTHING){
                    Item.getImg(it.id).paintIcon(panel, g, 20 + ((i - 30) * 52), 237);
                }
                if(it.amount != 1 && it.id != Item.NOTHING){
                    g.setColor(Color.white);
                    TextBox.outlineColor = Color.black;
                    g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                    TextBox.draw(it.amount + "", g, 20 + ((i - 30) * 52) + 35, 237 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                }
            }
            
            ArrayList<Recipe> recipes = Recipe.getRecipes();
            if(!GameData.bigCraft){
                
                if(recipes.size() > 0){
                    if(GameData.selCraft > 0){
                        recipes.get(GameData.selCraft - 1).output.getImg().paintIcon(panel, g, 636, 32);
                        if(!recipes.get(GameData.selCraft - 1).canCraft()){
                            g.setColor(new Color(1f, 0f, 0f, 0.5f));
                            g.fillRect(636, 32, 32, 32);
                        }
                    }
                    recipes.get(GameData.selCraft).output.getImg().paintIcon(panel, g, 636, 84);
                    if(!recipes.get(GameData.selCraft).canCraft()){
                        g.setColor(new Color(1f, 0f, 0f, 0.5f));
                        g.fillRect(636, 84, 32, 32);
                    }
                    if(recipes.get(GameData.selCraft).output.amount > 1){
                        g.setColor(Color.white);
                        TextBox.outlineColor = Color.black;
                        g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                        TextBox.draw(recipes.get(GameData.selCraft).output.amount + "", g, 636 + 32, 84 + 32, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                    }
                    Image[] imgs = new Image[recipes.get(GameData.selCraft).ingredients.length];
                    for(int i = 0; i < imgs.length; i++){
                        imgs[i] = recipes.get(GameData.selCraft).ingredients[i].getImg().getImage();
                    }
                    for(int i = 0; i < imgs.length; i++){
                        g.drawImage(imgs[i], 609 - (i * 18), 91, 16, 16, null);
                        if(recipes.get(GameData.selCraft).ingredients[i].amount > 1){
                            g.setColor(Color.white);
                            TextBox.outlineColor = Color.black;
                            g.setFont(new Font(GameData.getFont(), Font.ITALIC, 7));
                            TextBox.draw(recipes.get(GameData.selCraft).ingredients[i].amount + "", g, 609 - (i * 18) + 16, 91 + 16, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                        }
                    }
                    if(GameData.selCraft < recipes.size() - 1){
                        recipes.get(GameData.selCraft + 1).output.getImg().paintIcon(panel, g, 636, 136);
                        if(!recipes.get(GameData.selCraft + 1).canCraft()){
                            g.setColor(new Color(1f, 0f, 0f, 0.5f));
                            g.fillRect(636, 136, 32, 32);
                        }
                    }
                }
            }
            

            if(GameData.cursorItem != null){
                Point offset = new Point();
                if(Trident.mousePos.x > Trident.getFrameWidth() - 100) offset.x -= 32;
                if(Trident.mousePos.y > Trident.getFrameHeight() - 100) offset.y -= 32;

                Item.getImg(GameData.cursorItem.id).paintIcon(panel, g, Trident.mousePos.x + offset.x, Trident.mousePos.y + offset.y);
                if(GameData.cursorItem.amount != 1){
                    g.setColor(Color.white);
                    TextBox.outlineColor = Color.black;
                    g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
                    TextBox.draw(GameData.cursorItem.amount + "", g, Trident.mousePos.x + 35 + offset.x, Trident.mousePos.y + 32 + offset.y, TextBox.RIGHT, TextBox.NOMAXWIDTH, 1);
                }
                
            }



            int slot = -1;
            for(int i = 0; i < GameData.invBoxes.size(); i++){
                if(GameData.invBoxes.get(i).contains(Trident.mousePos)){
                    slot = i;
                    break;
                }
            }
            String label = "";
            String desc = "";
            if(slot != -1){
                if(slot == 40 && recipes.size() > 0 && !GameData.bigCraft){ 
                    // craft
                    label = recipes.get(GameData.selCraft).output.getName();
                    desc = recipes.get(GameData.selCraft).output.getDescription();
                }else if(slot == 41){
                    // save
                    label = "Save Game";
                    desc = "---------";
                }else if(slot == 42){
                    // save & exit
                    label = "Save & Exit";
                    desc = "---------";
                }else if(slot == 45){
                    // settings
                    label = "Settings";
                    desc = "---------";
                }else if(slot < 40){
                    // inventory
                    label = GameData.inventory[slot].getName();
                    desc = GameData.inventory[slot].getDescription();
                }

                boolean longDesc = false;
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                int titleLength = g.getFontMetrics().stringWidth(label);
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 15));
                int descLength = g.getFontMetrics().stringWidth(desc);
                if(descLength >= Trident.getFrameWidth() / 2 - 20){
                    longDesc = true;
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 12));
                    descLength = g.getFontMetrics().stringWidth(desc);
                }

                int length = Math.max(titleLength, descLength);

                if(Trident.mousePos.x < Trident.getFrameWidth() / 2){
                    g.setColor(new Color(0f, 0f, 0f, 0.3f));
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                    g.fillRect(Trident.mousePos.x, Trident.mousePos.y - 45, length, 45);
                    g.setColor(Color.white);
                    TextBox.draw(label, g, Trident.mousePos.x, Trident.mousePos.y - 35);
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 15));
                    if(longDesc) g.setFont(new Font(GameData.getFont(), Font.PLAIN, 12));
                    TextBox.draw(desc, g, Trident.mousePos.x, Trident.mousePos.y - 15);
                }else{
                    g.setColor(new Color(0f, 0f, 0f, 0.3f));
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                    g.fillRect(Trident.mousePos.x - length, Trident.mousePos.y - 45, length, 45);
                    g.setColor(Color.white);
                    TextBox.draw(label, g, Trident.mousePos.x, Trident.mousePos.y - 35, TextBox.RIGHT);
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 15));
                    if(longDesc) g.setFont(new Font(GameData.getFont(), Font.PLAIN, 10));
                    TextBox.draw(desc, g, Trident.mousePos.x, Trident.mousePos.y - 15, TextBox.RIGHT);

                }
                
            }
        }
        

        BufferedImage healthBar = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = healthBar.getGraphics();
        g2.setColor(Color.gray);
        g2.fillRect(0, 0, 32, 32);
        g2.setColor(Color.red);
        double healthPercent = (double)GameData.health / GameData.maxHealth;
        g2.fillRect(0, (int)(-32 * healthPercent + 32), 32, (int)(32 * healthPercent));
        applyGrayscaleMaskToAlpha(healthBar, healthMask);
        g.drawImage(healthBar, 10, 10, null);
        healthOutline.paintIcon(panel, g, 10, 10);

        BufferedImage hungerBar = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        g2 = hungerBar.getGraphics();
        g2.setColor(Color.gray);
        g2.fillRect(0, 0, 32, 32);
        g2.setColor(new Color(148, 60, 2));
        double hungerPercent = (double)GameData.hunger / GameData.maxHunger;
        g2.fillRect(0, (int)(-32 * hungerPercent + 32), 32, (int)(32 * hungerPercent));
        applyGrayscaleMaskToAlpha(hungerBar, hungerMask);
        g.drawImage(hungerBar, 10, 40, null);
        hungerOutline.paintIcon(panel, g, 10, 40);

        if(healthRect.contains(Trident.mousePos)){
            String label = GameData.health + "/" + GameData.maxHealth;
            g.setColor(new Color(0f, 0f, 0f, 0.3f));
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            int length = g.getFontMetrics().stringWidth(label);
            g.fillRect(Trident.mousePos.x, Trident.mousePos.y, length, 30);
            g.setColor(Color.white);
            TextBox.draw(label, g, Trident.mousePos.x, Trident.mousePos.y + 15);
        }
        if(hungerRect.contains(Trident.mousePos)){
            String label = GameData.hunger + "/" + GameData.maxHunger;
            g.setColor(new Color(0f, 0f, 0f, 0.3f));
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            int length = g.getFontMetrics().stringWidth(label);
            g.fillRect(Trident.mousePos.x, Trident.mousePos.y, length, 30);
            g.setColor(Color.white);
            TextBox.draw(label, g, Trident.mousePos.x, Trident.mousePos.y + 15);
        }
    
        Effect eff = null;
        for(int i = 0; i < GameData.effects.size(); i++){
            int y2 = i % 5;
            int x2 = i / 5;

            Effect.slot.paintIcon(panel, g, Trident.getFrameWidth() - 33 - (x2 * 30), 5 + (y2 * 30));
            GameData.effects.get(i).getImg().paintIcon(panel, g, Trident.getFrameWidth() - 33 - (x2 * 30) + 4, 5 + (y2 * 30) + 4);

            Rectangle rect = new Rectangle(Trident.getFrameWidth() - 33 - (x2 * 30), 5 + (y2 * 30), 56, 56);
            if(rect.contains(Trident.mousePos)) eff = GameData.effects.get(i);
        }
        if(eff != null){
            String label = eff.getName() + ": " + ((int)BTools.flip(eff.time, eff.maxTime) / 1000 + 1) + "/" + (eff.maxTime / 1000);
            g.setColor(new Color(0f, 0f, 0f, 0.3f));
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            int length = g.getFontMetrics().stringWidth(label);
            g.fillRect(Trident.mousePos.x - length, Trident.mousePos.y, length, 30);
            g.setColor(Color.white);
            TextBox.draw(label, g, Trident.mousePos.x, Trident.mousePos.y + 15, TextBox.RIGHT);
        }

        g.setFont(new Font(GameData.getFont(), Font.PLAIN, 16));
        int txtWidth = g.getFontMetrics().stringWidth(notifText);
        g.setColor(new Color(0f, 0f, 0f, (float)Math.max(notifPos.x / 2, 0)));
        g.fillRect(Trident.getFrameWidth() - txtWidth - ((notifType == NOTIF_BLANK) ? 0 : 20), 0, txtWidth + 30, 20);
        g.setColor(Color.white);
        TextBox.draw(notifText, g, Trident.getFrameWidth() - ((notifType == NOTIF_BLANK) ? 0 : 20), -12 + (int)notifPos.y, TextBox.RIGHT);
        if(notifType != NOTIF_BLANK){
            notifImgs[notifType - 1].paintIcon(panel, g, Trident.getFrameWidth() - 16, -20 + (int)notifPos.y);
        }

        if(Trident.getCurrentScene().name.equals("newTut")){
            switch(GameData.tutProgress){
            case 2:
                g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                int strWidth = g.getFontMetrics().stringWidth("Left Mouse to attack");
                g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, 10, strWidth, 25);
                g.setColor(Color.white);
                TextBox.draw("Left Mouse to attack", g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);
                break;
            case 3:
                if(!GameData.invOpen){
                    g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                    strWidth = g.getFontMetrics().stringWidth(KeyEvent.getKeyText(Settings.keybinds[Settings.INVENTORY]) + " to open inventory");
                    g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, 10, strWidth, 25);
                    g.setColor(Color.white);
                    TextBox.draw(KeyEvent.getKeyText(Settings.keybinds[Settings.INVENTORY]) + " to open inventory", g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);
                    break;
                }
                break;
            case 4:
            case 5:
            case 9:
                if(!GameData.invOpen){
                    g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                    strWidth = g.getFontMetrics().stringWidth(KeyEvent.getKeyText(Settings.keybinds[Settings.INVENTORY]) + " to open inventory");
                    g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, 10, strWidth, 25);
                    g.setColor(Color.white);
                    TextBox.draw(KeyEvent.getKeyText(Settings.keybinds[Settings.INVENTORY]) + " to open inventory", g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);
                    break;
                }else{
                    g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
                    g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                    strWidth = g.getFontMetrics().stringWidth("Craft items with the crafting slots in the top right");
                    g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, 10, strWidth, 25);
                    g.setColor(Color.white);
                    TextBox.draw("Craft items with the crafting slots in the top right", g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);
                    break;
                }
            case 10:
                g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
                strWidth = g.getFontMetrics().stringWidth("Open inventory near crate to open it");
                g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, 10, strWidth, 25);
                g.setColor(Color.white);
                TextBox.draw("Open inventory near crate to open it", g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);
                break;
            }
        }

        if(Dialog.dialog != -1 && Settings.subtitles){
            g.setColor(new Color(0f, 0.5f, 0.5f, 0.75f));
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 15));
            int strWidth = TextBox.textWidth(Dialog.dialogSubs[Dialog.dialog], g, Trident.getFrameHeight());
            int strHeight = TextBox.numLines(Dialog.dialogSubs[Dialog.dialog], g, Trident.getFrameHeight()) * 15;
            g.fillRect(Trident.getFrameWidth() / 2 - strWidth / 2, Trident.getFrameHeight() - strHeight - 10, strWidth, strHeight + 5);
            g.setColor(Color.white);
            TextBox.draw(Dialog.dialogSubs[Dialog.dialog], g, Trident.getFrameWidth() / 2, Trident.getFrameHeight() - strHeight - 3, TextBox.CENTER, Trident.getFrameHeight());
        }

        if(Trident.getCurrentScene().name.equals("newTut") && GameData.tutProgress == 17){
            double percent = BTools.clamp(((double)BTools.flip(GameData.tutTimer, 5000) / 4000), 0, 1);

            g.setColor(new Color(0f, 0f, 0f, (float)percent));
            g.fillRect(0, 0, Trident.getFrameWidth(), Trident.getFrameHeight());
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        notifAnim.update(elapsedTime);

        if(!notifAnim.isPlaying() && notifs.size() > 0){
            notifAnim.play("notif");
            notifText = notifs.get(0).text;
            notifType = notifs.get(0).type;
            notifs.remove(0);
        }
    }

    // Runs at the beginning of the scene
    public void sceneStart(String scene){

    }

    public void applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask){
        int width = image.getWidth();
        int height = image.getHeight();

        int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < imagePixels.length; i++)
        {
            int color = imagePixels[i] & 0x00ffffff; // Mask preexisting alpha
            int alpha = maskPixels[i] << 24; // Shift blue to alpha
            imagePixels[i] = color | alpha;
        }

        image.setRGB(0, 0, width, height, imagePixels, 0, width);
    }


    public static final int NOTIF_BLANK = 0, NOTIF_SAVE = 1, NOTIF_AUTOSAVE = 2, NOTIF_EXCMK = 3, NOTIF_QSTMK = 4, NOTIF_MUSIC = 5, NOTIF_TRPHY = 6;
    public static void addNotif(String text, int type){
        if(type > 6) type = NOTIF_BLANK;
        notifs.add(new Notif(text, type));
    }
    public static void clearNotif(){
        notifAnim.play("notifHidden");
        notifs = new ArrayList<Notif>();
    }

    private static class Notif {
        public String text;
        public int type;

        public Notif(String t, int ty){
            text = t;
            type = ty;
        }
    }
}
