package ent;

import blib.anim.*;
import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import custom.*;
import java.util.ArrayList;
import java.awt.image.*;
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
        try{
            ArrayList<Animation> anims = new ArrayList<Animation>();
            anims.add(new Animation("data/animations/notif"));
            anims.add(new Animation("data/animations/notifHidden"));

            notifAnim = new Animator(notifPos, anims);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading notif animation", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
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
            if(GameData.openCrate != null){
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
            if(GameData.openCrate != null){
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
                if(slot == 40 && recipes.size() > 0){ 
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

        if(Trident.getCurrentScene().name.equals("tutorial")){
            if(GameData.tutorialTriggers[3]){
                // slime
                g.setColor(Settings.assistColor);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                TextBox.draw("Kill the slime.\nOnce it has fallen, you can enjoy Kronia!\n\nTIP: If you're having trouble hitting it,\nyou can enable an aim helper in the settings.", g, Trident.getFrameWidth() / 2, 10, TextBox.CENTER);
            }else if(GameData.tutorialTriggers[2]){
                // starving
                g.setColor(Settings.assistColor);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                TextBox.draw("Look at your hunger in the\ntop left. You're starving!\n\nKill cows with the sword provided and craft\ncooked food at the furnace in the inventory.\nClick with the food selected to eat it.", g, Trident.getFrameWidth() / 2, 10, TextBox.CENTER);
            }else if(GameData.tutorialTriggers[1]){
                // got axe
                g.setColor(Settings.assistColor);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                TextBox.draw("Welcome to Kronia!\n\nYou have been given an axe.\nClick to chop trees when the axe is selected.\nPress [TAB] to open the inventory to craft a pickaxe\nwhen close to the crafting table.\nScroll with the scroll wheel to select items", g, Trident.getFrameWidth() / 2, 10, TextBox.CENTER);
            }
        }
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        
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
    public static void setNotif(String text, int type){

    }
}
