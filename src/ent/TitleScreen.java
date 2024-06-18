package ent;

import blib.game.Entity;
import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import custom.*;
import java.io.*;
import VERSION.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class TitleScreen extends TridEntity {

    public static TitleButton[][] buttons = {
        { // 0
            new TitleButton(
                new Rectangle(684 / 2, 150 + 25, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Play",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 200 + 25, 120, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Tutorial",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 250 + 25, 120, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Settings",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 350 + 25, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Quit",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684, 400, 200, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Give Feedback",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 / 2, 300 + 25, 125, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Credits",
                TitleButton.CENTER
            ),
        },
        { // 1
            new TitleButton(
                new Rectangle(684 / 4, 350, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Prev",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 4 * 3, 350, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Next",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 4 + 50, 400, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Play",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 4 * 3 - 30, 400, 150, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "New World",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 430, 100, 30),
                Color.red,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Delete",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(10, 10, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Back",
                TitleButton.LEFT
            ),
        },
        { // 2
            new TitleButton(
                new Rectangle(684 / 4, 350, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Prev",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 4 * 3, 350, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Next",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 400, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Select",
                TitleButton.CENTER
            ),
        },
        { // 3
            new TitleButton(
                new Rectangle(10, 10, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Back",
                TitleButton.LEFT
            ),
            new TitleSlider(new Point(10, 200), "Master Volume", TitleSlider.MASTER),
            new TitleSlider(new Point(10, 240), "SFX Volume", TitleSlider.SFX),
            new TitleSlider(new Point(10, 280), "Music Volume", TitleSlider.MUSIC),
            new TitleButton(
                new Rectangle(684 / 2, 150 + 25, 210, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Rich Text: ---",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 150 + 65, 300, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Cam Smoothing: ---",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 250, 200, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Aim Help: ---",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 285, 200, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Assist Color",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 285 + 35, 200, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Cam Shake: ---",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2, 285 + 70, 200, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Keybinds",
                TitleButton.CENTER
            ),
        },
        { // 4
            new TitleButton(
                new Rectangle(10, 10, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Back",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 50, 270, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Open Inventory: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 85, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Toggle Map: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 85 + 35, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Move Up: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 85 + 70, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Move Down: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 190, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Move Left: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 225, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Move Right: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 260, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Drop Item: ---",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(30, 295, 250, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Rotate Item: ---",
                TitleButton.LEFT
            ),
        },
        { // 5
            new TitleButton(
                new Rectangle(10, 10, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Back",
                TitleButton.LEFT
            ),
        },
    };

    public static void updateButtonFont(){
        for(TitleButton[] arr: buttons){
            for(TitleButton b: arr){
                b.font = new Font(GameData.getFont(), b.font.getStyle(), b.font.getSize());
            }
        }
    }

    public static void updateButtonText(){
        // settings
        buttons[3][4].text = "Rich Text: " + Settings.richText;
        buttons[3][5].text = "Cam Smoothing: " + Settings.camSmooth;
        buttons[3][6].text = "Aim Help: " + Settings.aimHelp;
        buttons[3][7].color = Settings.assistColor;
        buttons[3][8].text = "Cam Shake: " + Settings.camShake;

        // keybinds
        buttons[4][1].text = "Open Inventory: " + KeyEvent.getKeyText(Settings.keybinds[Settings.INVENTORY]);
        buttons[4][2].text = "Toggle Map: " + KeyEvent.getKeyText(Settings.keybinds[Settings.MAP]);
        buttons[4][3].text = "Move Up: " + KeyEvent.getKeyText(Settings.keybinds[Settings.UP]);
        buttons[4][4].text = "Move Down: " + KeyEvent.getKeyText(Settings.keybinds[Settings.DOWN]);
        buttons[4][5].text = "Move Right: " + KeyEvent.getKeyText(Settings.keybinds[Settings.RIGHT]);
        buttons[4][6].text = "Move Left: " + KeyEvent.getKeyText(Settings.keybinds[Settings.LEFT]);
        buttons[4][7].text = "Drop Item: " + KeyEvent.getKeyText(Settings.keybinds[Settings.DROP]);
        buttons[4][8].text = "Rotate Item: " + KeyEvent.getKeyText(Settings.keybinds[Settings.ROTATE]);

        if(GameData.setKeybind){
            int button = 0;
            switch(GameData.keybindSel){
            case Settings.INVENTORY:
                button = 1;
                break;
            case Settings.MAP:
                button = 2;
                break;
            case Settings.UP:
                button = 3;
                break;
            case Settings.DOWN:
                button = 4;
                break;
            case Settings.RIGHT:
                button = 5;
                break;
            case Settings.LEFT:
                button = 6;
                break;
            case Settings.DROP:
                button = 7;
                break;
            case Settings.ROTATE:
                button = 8;
                break;
            }
            
            buttons[4][button].text = "-PRESS KEY-";
        }
    }

    int screen = 0;
    int worldSel = 0;
    ImageIcon title = new ImageIcon("data/images/title.png");
    ImageIcon thankyou = new ImageIcon("data/images/thankyou.gif");
    String[] betaTesters = {
        "Mega",
        "Dusti",
        "Spud :)",
        "cg508",
        "ALYK",
        "Wizard King",
    };
    double testerPos = 0;
    double testerSpeed = 0.05;
    double colorTime = 0;

    // Constructor, runs when the entity is created
    public TitleScreen(Position pos){
        super(pos);
        renderType = Entity.TOPPRIORITY;
        BTools.resizeImgIcon(title, 128 * 2, 128 * 2);
        BTools.resizeImgIcon(thankyou, 6 * 3, 134 * 3);
    }
    // Registry constructor, used only for adding to the registry
    public TitleScreen(){
        super("titlescreen", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new TitleScreen(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        int WIDTH = 684, HEIGHT = 462; // 684 x 462
        if(screen == 0){
            g.setColor(Color.white);
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            TextBox.draw(Version.ver, g, 0, HEIGHT - 20);
            title.paintIcon(panel, g, WIDTH / 2 - (title.getIconWidth() / 2), -20);
        }
        if(screen == 1){
            if(WorldManager.getWorlds().size() != 0){
                g.setColor(Color.white);
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 40));
                TextBox.draw(WorldManager.getWorlds().get(worldSel), g, WIDTH / 2, HEIGHT / 2 - 70, TextBox.CENTER, WIDTH / 2);
                g.setFont(new Font(GameData.getFont(), Font.ITALIC, 20));
                TextBox.draw("(" + (worldSel + 1) + "/" + WorldManager.getWorlds().size() + ")", g, WIDTH / 2, HEIGHT / 2 + 100, TextBox.CENTER);
            }
            
        }
        if(screen == 5){
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRect(0, 0, Trident.getFrameWidth(), Trident.getFrameHeight());

            thankyou.paintIcon(panel, g, Trident.getFrameWidth() - 18, Trident.getFrameHeight() / 2 - 201);

            double rPercent = (Math.sin(colorTime + (Math.PI / 3)) + 1) / 2;
            double gPercent = (Math.sin(colorTime - (Math.PI / 3)) + 1) / 2;
            double bPercent = (Math.sin(colorTime + Math.PI) + 1) / 2;
            int r = (int)(rPercent * 255);
            int gr = (int)(gPercent * 255);
            int b = (int)(bPercent * 255);

            int imgHeight = 20 * betaTesters.length;
            BufferedImage names = new BufferedImage(Trident.getFrameWidth() / 2, imgHeight + 10, BufferedImage.TYPE_INT_ARGB);
            Graphics g2 = names.getGraphics();
            g2.setColor(new Color(r, gr, b));
            g2.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            String text = "";
            for(String s: betaTesters){
                text += s + "\n";
            }
            TextBox.draw(text, g2, Trident.getFrameWidth() / 2, 10, TextBox.RIGHT);

            while(testerPos > imgHeight) testerPos -= imgHeight;
            int reps = Math.max(2, (Trident.getFrameHeight() / imgHeight) + 1);
            for(int i = 0; i <= reps; i++){
                g.drawImage(names, Trident.getFrameWidth() / 2 - 25, (int)(-testerPos + (i * imgHeight)), panel);
            }

            g.setColor(Color.white);
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 30));
            TextBox.draw("Programming, Art, Design\n   Blocky\n\nMusic\n   Sona AI\n\nEpic Gamer\n   You\n\nBuilt with Trident Engine", g, 20, 100);
        }
        for(TitleButton b: buttons[screen]){
            b.render(g);
        }
    }

    public void sceneStart(String scene){
        worldSel = 0;
        screen = 0;
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        if(screen == 3){
            ((TitleSlider)buttons[3][1]).update(elapsedTime);
            ((TitleSlider)buttons[3][2]).update(elapsedTime);
            ((TitleSlider)buttons[3][3]).update(elapsedTime);
        }
        if(screen == 5){
            testerPos += testerSpeed * elapsedTime;
            colorTime += (elapsedTime / 1000.0) * 2;
        }
    }

    public void mousePressed(int mb, Point mousePos){
        for(int i = 0; i < buttons[screen].length; i++){
            TitleButton b = buttons[screen][i];
            if(b.rect.contains(mousePos)){
                buttonPressed(i);
                break;
            }
        }
    }

    public void buttonPressed(int button){
        Settings.playSound("data/sound/button.wav");
        if(screen == 0){
            switch(button){
            case 0:
                screen = 1;
                if(WorldManager.getWorlds().size() == 0){
                    makeWorld();
                }
                break;
            case 1:
                Trident.setupScenes();
                Trident.loadScene("tutorial");
                break;
            case 2:
                screen = 3;
                break;
            case 3:
                System.exit(0);
                break;
            case 4:
                BTools.openWebsite("https://forms.gle/UzAjpGuNGiGEP4up6");
                break;
            case 5:
                screen = 5;
                break;
            }
        }else if(screen == 1){
            switch(button){
            case 0:
                worldSel--;
                if(worldSel < 0){
                    worldSel = WorldManager.getWorlds().size() - 1;
                }
                break;
            case 1:
                worldSel++;
                if(worldSel > WorldManager.getWorlds().size() - 1) worldSel = 0;
                break;
            case 2:
                WorldManager.loadWorld(WorldManager.getWorlds().get(worldSel));
                break;
            case 3:
                makeWorld();
                break;
            case 4:
                int sel = JOptionPane.showConfirmDialog(null, "You are about to delete the world '" + WorldManager.getWorlds().get(worldSel) + "', are you sure? The world will be deleted forever, and that's a pretty long time!", "Delete World?", JOptionPane.YES_NO_OPTION);
                if(sel == 0){
                    File file = new File("data/worlds/" + WorldManager.getWorlds().get(worldSel) + ".bson");
                    file.delete();
                    worldSel = 0;
                    if(WorldManager.getWorlds().size() == 0) screen = 0;
                }
                break;
            case 5:
                screen = 0;
                break;
            }
        }else if(screen == 2){
            switch(button){
            case 2:
                screen = 0;
                break;
            }
        }else if(screen == 3){
            switch(button){
            case 0:
                screen = 0;
                Settings.saveSettings();
                MusicManager.stopSong();
                MusicManager.songTime = 1000;
                break;
            case 4: 
                Settings.richText = !Settings.richText;
                updateButtonFont();
                updateButtonText();
                break;
            case 5:
                Settings.camSmooth = !Settings.camSmooth;
                updateButtonText();
                break;
            case 6:
                Settings.aimHelp = !Settings.aimHelp;
                updateButtonText();
                break;
            case 7:
                Color c = JColorChooser.showDialog(null, "Select new assist color", Settings.assistColor);
                if(c != null) Settings.assistColor = c;
                updateButtonText();
                break;
            case 8:
                Settings.camShake = !Settings.camShake;
                updateButtonText();
                break;
            case 9:
                screen = 4;
                break;
            }
        }else if(screen == 4){
            switch(button){
            case 0:
                GameData.setKeybind = false;
                screen = 3;
                Settings.saveSettings();
                break;
            case 1:
                setKeybind(Settings.INVENTORY);
                break;
            case 2:
                setKeybind(Settings.MAP);
                break;
            case 3:
                setKeybind(Settings.UP);
                break;
            case 4:
                setKeybind(Settings.DOWN);
                break;
            case 5:
                setKeybind(Settings.RIGHT);
                break;
            case 6:
                setKeybind(Settings.LEFT);
                break;
            case 7:
                setKeybind(Settings.DROP);
                break;
            case 8:
                setKeybind(Settings.ROTATE);
                break;
            }
            updateButtonText();
        }else if(screen == 5){
            switch(button){
            case 0:
                screen = 0;
                break;
            }
        }
    }
    private void setKeybind(int keybind){
        GameData.setKeybind = true;
        GameData.keybindSel = keybind;
    }

    public void makeWorld(){
        String input = JOptionPane.showInputDialog(null, "Enter the name for your world", "New World", JOptionPane.QUESTION_MESSAGE);
        if(input == null || input.length() == 0){
            screen = 0;
            return;
        }
        char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
        for(int i = 0; i < input.length(); i++){
            for(int j = 0; j < ILLEGAL_CHARACTERS.length; j++){
                if(input.charAt(i) == ILLEGAL_CHARACTERS[j]){
                    JOptionPane.showMessageDialog(null, "The world name '" + input + "' is invalid!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    screen = 0;
                    return;
                }
            }
        }
        
        Trident.resetKeys();
        Trident.setupScenes(); // make sure any previous sessions are removed
        Trident.loadScene("world");
        WorldManager.newWorld(input);
        
    }
}
