package ent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import VERSION.Version;
import blib.game.Entity;
import blib.util.BTools;
import blib.util.Position;
import blib.util.TextBox;
import custom.Achievement;
import custom.GameData;
import custom.MusicManager;
import custom.Settings;
import custom.TitleButton;
import custom.TitleSlider;
import custom.WorldManager;
import trident.Main;
import trident.TridEntity;
import trident.Trident;

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
                new Rectangle(684, 360, 200, 30),
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
            new TitleButton(
                new Rectangle(0, 360, 175, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Achievements",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(Trident.getFrameWidth() - 40, Trident.getFrameHeight() - 40, 33, 33),
                new Color(88, 101, 242),
                Color.white,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(0, 310, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "News",
                TitleButton.LEFT
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
                new Rectangle(684 - 5, 30, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Rich Text: ---",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 60, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Cam Smoothing: ---",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 90, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Aim Help: ---",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 120, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Assist Color",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 150, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Cam Shake: ---",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 180, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Damage Indicator: ---",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 / 2, Trident.getFrameHeight() - 30, 150, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Keybinds",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 - 5, 210, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Toggle Fullscreen",
                TitleButton.RIGHT
            ),
            new TitleButton(
                new Rectangle(684 - 5, 240, 290, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Subtitles: ---",
                TitleButton.RIGHT
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
        { // 6
            new TitleButton(
                new Rectangle(10, 10, 100, 30),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 25),
                "Cancel",
                TitleButton.LEFT
            ),
            new TitleButton(
                new Rectangle(684 / 2, 405, 300, 50),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 40),
                "Create World",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2 - 45, 210, 45, 45),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 40),
                "-",
                TitleButton.CENTER
            ),
            new TitleButton(
                new Rectangle(684 / 2 + 45, 210, 45, 45),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 40),
                "+",
                TitleButton.CENTER
            ),
        },
        { // 7
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
        { // 8
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
        { // 9
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
                new Rectangle(684 / 2, Trident.getFrameHeight() - 30, 150, 25),
                Color.white,
                Color.gray,
                Color.black,
                new Font(GameData.getFont(), Font.PLAIN, 20),
                "Open Link",
                TitleButton.CENTER
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
        buttons[3][9].text = "Damage Indicators: " + Settings.dmgInd;
        buttons[3][12].text = "Subtitles: " + Settings.subtitles;

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

    public int screen = 0;
    int worldSel = 0;
    ImageIcon title = new ImageIcon("data/images/title.png");
    ImageIcon discord = new ImageIcon("data/images/discord.png");
    ImageIcon thankyou = new ImageIcon("data/images/thankyou.gif");
    public static String[] betaTesters = {
        "Mega",
        "Dusti",
        "Spud :)",
        "cg508",
        "Wizard King",
        "Superisity",
        "JoJo",
    };
    double testerPos = 0;
    double testerSpeed = 0.05;
    double colorTime = 0;

    int scroll = 0;

    public boolean inGameMenu = false;

    public static String worldName = "";
    public static int worldDiff = WorldManager.defaultDiff;

    public static ArrayList<String> newsLinks = new ArrayList<String>();
    public static ArrayList<String> newsDates = new ArrayList<String>();
    public static ArrayList<String> newsTitles = new ArrayList<String>();
    public static ArrayList<String> newsTexts = new ArrayList<String>();
    int selNews = 0;

    // Constructor, runs when the entity is created
    public TitleScreen(Position pos){
        super(pos);
        renderType = Entity.TOPPRIORITY;
        BTools.resizeImgIcon(title, 128 * 2, 128 * 2);
        BTools.resizeImgIcon(thankyou, 6 * 3, 134 * 3);
        BTools.resizeImgIcon(discord, 32, 32);
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
        if(screen == 3){
            g.setColor(Color.white);
            TextBox.outlineColor = Color.black;
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 13));
            TextBox.draw("Music volume updates after \nyou leave the settings screen", g, 10, 320, TextBox.LEFT, TextBox.NOMAXWIDTH, 2);
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
            TextBox.draw("Programming, Art, Design\n   Blocky\n\nMusic\n   Blocky\n   Rascal\n\nTutorial Voice\n   Jack Catt\n\nBuilt with Trident Engine", g, 20, 100);
        }
        if(screen == 6){
            g.setColor(Color.white);
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 40));
            TextBox.draw("World Name:\n" + worldName + "_", g, Trident.getFrameWidth() / 2, 50, TextBox.CENTER);

            TextBox.draw("Difficulty:", g, Trident.getFrameWidth() / 2, 140, TextBox.CENTER);
            String diffText = "";
            switch(worldDiff){
            case WorldManager.V_EASY:
                diffText = "Very Easy";
                break;
            case WorldManager.EASY:
                diffText = "Easy";
                break;
            case WorldManager.NORMAL:
                diffText = "Normal";
                break;
            case WorldManager.HARD:
                diffText = "Hard";
                g.setColor(Color.red);
                break;
            case WorldManager.V_HARD:
                diffText = "Very Hard";
                g.setColor(new Color(0.5f, 0f, 0f));
                TextBox.outlineColor = Color.red;
                break;
            }
            TextBox.draw(diffText, g, Trident.getFrameWidth() / 2, 180, TextBox.CENTER, TextBox.NOMAXWIDTH, ((worldDiff == WorldManager.V_HARD) ? 2 : 0));

            String diffDesc = "";
            switch(worldDiff){
            case WorldManager.V_EASY:
                diffDesc = "- 50% entity health\n- Slimes only spawn at night\n- 50% hunger speed\n- Starving deals no damage\n- 200% health\n- 1 enemy cap\n- Enemies move slower";
                break;
            case WorldManager.EASY:
                diffDesc = "- 75% entity health\n- Slimes only spawn at night\n- 50% hunger speed\n- 150% health\n- 2 enemy cap";
                break;
            case WorldManager.NORMAL:
                diffDesc = "- 100% entity health\n- Slimes spawn any time\n- 100% hunger speed\n- 100% health\n- 3 enemy cap";
                break;
            case WorldManager.HARD:
                diffDesc = "- 150% entity health\n- Slimes spawn any time\n- 10 enemy cap\n- 150% hunger speed\n- Enemies move faster\n- 75% health\n- The caves are pitch-black";
                break;
            case WorldManager.V_HARD:
                diffDesc = "- 200% entity health\n- Slimes spawn any time\n- 20 enemy cap\n- 200% hunger speed\n- Enemies move even faster\n- 50% health\n- The caves and night are pitch-black";
                break;
            }
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            TextBox.draw(diffDesc, g, Trident.getFrameWidth() / 2, 270, TextBox.CENTER, TextBox.NOMAXWIDTH, ((worldDiff == WorldManager.V_HARD) ? 2 : 0));
        }
        if(screen == 7){
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRect(0, 0, Trident.getFrameWidth(), Trident.getFrameHeight());

            ArrayList<Integer> list = Achievement.getSorted();
            for(int i = 0; i < list.size(); i++){
                int a = list.get(i);
                // draw icon
                ImageIcon icon = Achievement.getIcon(a);
                icon.paintIcon(panel, g, 20, 50 - scroll + (74 * i));
                // title
                g.setColor(Color.white);
                if(!Achievement.has(a)) g.setColor(Color.lightGray);
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 25));
                TextBox.draw(Achievement.titles[a], g, 94, 75 - scroll + (74 * i));
                // desc
                g.setFont(new Font(GameData.getFont(), Font.ITALIC, 20));
                TextBox.draw(Achievement.getDesc(a), g, 94, 100 - scroll + (74 * i));
            }

            g.setColor(Color.black);
            g.fillRect(Trident.getFrameWidth() / 2 - 150, Trident.getFrameHeight() - 20, 300, 20);
            g.setColor(Color.green);
            double percent = (double)Achievement.numUnlocked() / Achievement.num();
            g.fillRect(Trident.getFrameWidth() / 2 - 150, Trident.getFrameHeight() - 20, (int)(300 * percent), 20);
            g.setColor(Color.white);
            g.drawRect(Trident.getFrameWidth() / 2 - 150, Trident.getFrameHeight() - 20, 300, 20);
            TextBox.draw((int)(percent * 100) + "%", g, Trident.getFrameWidth() / 2 + 150, Trident.getFrameHeight() - 10);
        }
        if(screen == 8){
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRect(0, 0, Trident.getFrameWidth(), Trident.getFrameHeight());

            for(int i = 0; i < newsTitles.size(); i++){
                String title = newsTitles.get(i);
                String date = newsDates.get(i);
                
                // title
                g.setColor(Color.white);
                g.setFont(new Font(GameData.getFont(), Font.PLAIN, 25));
                TextBox.draw(title, g, 94, 75 - scroll + (74 * i));
                // date
                g.setFont(new Font(GameData.getFont(), Font.ITALIC, 20));
                TextBox.draw(date, g, 94, 100 - scroll + (74 * i));
            }
        }
        if(screen == 9){
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRect(0, 0, Trident.getFrameWidth(), Trident.getFrameHeight());

            // Title
            g.setColor(Color.white);
            g.setFont(new Font(GameData.getFont(), Font.BOLD, 25));
            TextBox.draw(newsTitles.get(selNews), g, Trident.getFrameWidth() / 2, 20, TextBox.CENTER);

            // Date
            g.setColor(Color.lightGray);
            g.setFont(new Font(GameData.getFont(), Font.ITALIC, 15));
            TextBox.draw(newsDates.get(selNews), g, Trident.getFrameWidth() / 2, 50, TextBox.CENTER);

            // Text
            g.setColor(Color.white);
            g.setFont(new Font(GameData.getFont(), Font.PLAIN, 20));
            TextBox.draw(newsTexts.get(selNews), g, 70, 100, TextBox.LEFT, (int)(Trident.getFrameHeight()));
        }
        for(TitleButton b: buttons[screen]){
            b.render(g);
        }
        if(screen == 0){
            discord.paintIcon(panel, g, Trident.getFrameWidth() - 39, Trident.getFrameHeight() - 39);
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
        if(inGameMenu) position = Trident.getPlrPos();
    }

    public void mousePressed(int mb, Point mousePos){
        for(int i = 0; i < buttons[screen].length; i++){
            TitleButton b = buttons[screen][i];
            if(b.rect.contains(mousePos)){
                buttonPressed(i);
                return;
            }
        }

        if(screen == 8){
            int y = mousePos.y - 50 + scroll; // relative y to the list
            if(y >= 0 && y <= newsTitles.size() * 74){
                int sel = y / 74;
                selNews = sel;
                screen = 9; 
            }
        }
    }
    public void mouseScrolled(int s){
        if(screen == 7){
            scroll += s * 20;
            scroll = BTools.clamp(scroll, 0, Math.max(0, -(74 * 5) + (74 * Achievement.num())));
        }
        if(screen == 8){
            scroll += s * 20;
            scroll = BTools.clamp(scroll, 0, Math.max(0, -(74 * 5) + (74 * newsTitles.size())));
        }
    }

    public void buttonPressed(int button){
        Settings.playSound("data/sound/button.wav");
        if(screen == 0){
            switch(button){
                case 0:
                    screen = 1;
                    if(WorldManager.getWorlds().size() == 0){
                        screen = 6;
                        worldName = "";
                        worldDiff = WorldManager.defaultDiff;
                    }
                    break;
                case 1:
                    WorldManager.difficulty = WorldManager.NORMAL;
                    Trident.setupScenes();
                    Trident.loadScene("newTut");
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
                case 6:
                    screen = 7;
                    scroll = 0;
                    break;
                case 7:
                    BTools.openWebsite("https://discord.gg/kb7TqaHucu");
                    break;
                case 8:
                    screen = 8;
                    scroll = 0;
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
                screen = 6;
                worldName = "";
                worldDiff = WorldManager.defaultDiff;
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
                if(inGameMenu){
                    GameData.settingsOpen = false;
                    Trident.destroy(this);
                }
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
                Settings.dmgInd = !Settings.dmgInd;
                updateButtonText();
                break;
            case 10:
                screen = 4;
                break;
            case 11:
                Trident.fullscreen = !Trident.fullscreen;
                Main.window = BTools.getWindowFullscreen(Main.window, Trident.fullscreen, Trident.panel);
                break;
            case 12:
                Settings.subtitles = !Settings.subtitles;
                updateButtonText();
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
        }else if(screen == 6){
            switch(button){
            case 0:
                screen = 1;
                if(WorldManager.getWorlds().size() == 0){
                    screen = 0;
                }
                break;
            case 1:
                makeWorld();
                break;
            case 2:
                worldDiff--;
                if(worldDiff < WorldManager.V_EASY) worldDiff = WorldManager.V_EASY;
                break;
            case 3:
                worldDiff++;
                if(worldDiff > WorldManager.V_HARD) worldDiff = WorldManager.V_HARD;
                break;
            }
        }else if(screen == 7){
            switch(button){
                case 0:
                    screen = 0;
                    break;
            }
        }else if(screen == 8){
            switch(button){
                case 0:
                    screen = 0;
                    break;
            }
        }else if(screen == 9){
            switch(button){
                case 0:
                    screen = 8;
                    break;
                case 1:
                    BTools.openWebsite(newsLinks.get(selNews));
                    break;
            }
        }
    }
    private void setKeybind(int keybind){
        GameData.setKeybind = true;
        GameData.keybindSel = keybind;
    }

    public void makeWorld(){
        if(worldName.length() == 0) return;
        
        Trident.resetKeys();
        Trident.setupScenes(); // make sure any previous sessions are removed
        Trident.loadScene("world");
        WorldManager.newWorld(worldName, (byte)worldDiff);
        
    }
}
