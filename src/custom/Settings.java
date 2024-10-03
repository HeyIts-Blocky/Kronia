package custom;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import blib.bson.BSonObject;
import blib.bson.BSonParser;
import blib.util.Audio;
import trident.Trident;
public class Settings {

    public static double sfxVolume = 1;
    public static boolean richText = false; // true = font is arial, false = font is whatever i changed it to i forgor
    public static boolean camSmooth = true;
    public static boolean aimHelp = false;
    public static Color assistColor = Color.white;
    public static boolean camShake = true;
    public static boolean dmgInd = true;
    public static boolean subtitles = true;

    public static int[] keybinds = {
        KeyEvent.VK_TAB, // inventory
        KeyEvent.VK_W, // up
        KeyEvent.VK_S, // down
        KeyEvent.VK_A, // left
        KeyEvent.VK_D, // right
        KeyEvent.VK_M, // map
        KeyEvent.VK_T, // drop
        KeyEvent.VK_R, // rotate
    };
    public static final int INVENTORY = 0, UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4, MAP = 5, DROP = 6, ROTATE = 7;

    public static void playSound(String file){
        Audio.play(file, sfxVolume);
    }
    public static void playSound(String file, double vol){
        Audio.play(file, sfxVolume * vol);
    }
    
    public static void saveSettings(){
        try{
            File file = new File("data/settings.bson");
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println("int volume " + Audio.volume);
            writer.println("double sfx " + sfxVolume);
            writer.println("double music " + MusicManager.volume);
            writer.println("boolean richText " + richText);
            writer.println("boolean camSmooth " + camSmooth);
            writer.println("boolean aimHelp " + aimHelp);
            writer.println("int assistR " + assistColor.getRed());
            writer.println("int assistG " + assistColor.getGreen());
            writer.println("int assistB " + assistColor.getBlue());
            writer.println("int assistA " + assistColor.getAlpha());
            writer.println("int keyinv " + keybinds[INVENTORY]);
            writer.println("int keyup " + keybinds[UP]);
            writer.println("int keydown " + keybinds[DOWN]);
            writer.println("int keyleft " + keybinds[LEFT]);
            writer.println("int keyright " + keybinds[RIGHT]);
            writer.println("int keymap " + keybinds[MAP]);
            writer.println("int keydrop " + keybinds[DROP]);
            writer.println("int keyrotate " + keybinds[ROTATE]);
            writer.println("boolean camShake " + camShake);
            writer.println("boolean dmgInd " + dmgInd);
            writer.println("boolean dev " + Trident.consoleEnabled);
            writer.println("boolean subtitles " + subtitles);
            writer.close();
        }catch(Exception e){
            Trident.printException("Problem while saving settings!", e);
        }
    }

    public static void loadSettings(){
        if(!(new File("data/settings.bson")).exists()){
            saveSettings();
            return;
        }
        try{
            ArrayList<BSonObject> objects = BSonParser.readFile("data/settings.bson");
            BSonObject obj = BSonParser.getObject("volume", objects);
            Audio.volume = obj.getInt();
            obj = BSonParser.getObject("sfx", objects);
            sfxVolume = obj.getDouble();
            obj = BSonParser.getObject("music", objects);
            MusicManager.volume = obj.getDouble();
            obj = BSonParser.getObject("richText", objects);
            richText = obj.getBoolean();
            obj = BSonParser.getObject("camSmooth", objects);
            camSmooth = obj.getBoolean();
            obj = BSonParser.getObject("aimHelp", objects);
            aimHelp = obj.getBoolean();
            obj = BSonParser.getObject("assistR", objects);
            int assistR = obj.getInt();
            obj = BSonParser.getObject("assistG", objects);
            int assistG = obj.getInt();
            obj = BSonParser.getObject("assistB", objects);
            int assistB = obj.getInt();
            obj = BSonParser.getObject("assistA", objects);
            int assistA = obj.getInt();
            assistColor = new Color(assistR, assistG, assistB, assistA);
            obj = BSonParser.getObject("keyinv", objects);
            keybinds[INVENTORY] = obj.getInt();
            obj = BSonParser.getObject("keyup", objects);
            keybinds[UP] = obj.getInt();
            obj = BSonParser.getObject("keydown", objects);
            keybinds[DOWN] = obj.getInt();
            obj = BSonParser.getObject("keyleft", objects);
            keybinds[LEFT] = obj.getInt();
            obj = BSonParser.getObject("keyright", objects);
            keybinds[RIGHT] = obj.getInt();
            obj = BSonParser.getObject("keymap", objects);
            keybinds[MAP] = obj.getInt();
            obj = BSonParser.getObject("keydrop", objects);
            keybinds[DROP] = obj.getInt();
            obj = BSonParser.getObject("keyrotate", objects);
            keybinds[ROTATE] = obj.getInt();
            obj = BSonParser.getObject("camShake", objects);
            camShake = obj.getBoolean();
            obj = BSonParser.getObject("dmgInd", objects);
            dmgInd = obj.getBoolean();

            try{ // v0.7
                obj = BSonParser.getObject("dev", objects);
                Trident.consoleEnabled = obj.getBoolean();
                obj = BSonParser.getObject("subtitles", objects);
                subtitles = obj.getBoolean();
            }catch(Exception e){
                Trident.consoleEnabled = false;
                subtitles = true;
            }
            applyKeybinds();
        }catch(Exception e){
            Trident.printException("Problem while loading settings!", e);
        }
    }

    public static void applyKeybinds(){ // apply keybinds to player
        Trident.getPlr().up = keybinds[UP];
        Trident.getPlr().down = keybinds[DOWN];
        Trident.getPlr().left = keybinds[LEFT];
        Trident.getPlr().right = keybinds[RIGHT];
    }
}
