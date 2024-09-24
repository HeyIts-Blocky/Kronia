package custom;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import blib.bson.BSonList;
import blib.bson.BSonObject;
import blib.bson.BSonParser;
import blib.util.BTools;
import ent.HUD;
import trident.Trident;

public class Achievement {

    public static ImageIcon lockedIcon = new ImageIcon("data/images/achievements/locked.png");
    public static final String[] titles = {
            "Getting your feet... wet?", // 0
            "Of course you would.", // 1
            "Fully gooped", // 2
            "Entrance Exam", // 3
            "Where it all begins", // 4
            "Now you're cooking with coal", // 5
            "Roasted by the fire", // 6
            "Gronk strong.", // 7
            "Breaking ground", // 8
            "Digging deeper", // 9
            "I want to speak to your Supervisor", // 10
            "You're fired", // 11
    };
    public static final String[] descs = {
            "Slay the Apex Slime.", // 0
            "Get sick by eating raw meat.", // 1
            "Slay the Apex Slime in Very Hard.", // 2
            "Complete the tutorial.", // 3
            "Craft a workbench.", // 4
            "Cook meat.", // 5
            "Cook a hotdog.", // 6
            "Craft a stone tool.", // 7
            "Enter the Mines.", // 8
            "Enter the Deep Mines.", // 9
            "Defeat The Supervisor.", // 10
            "Defeat The Supervisor in Very Hard.", // 11
    };
    public static final ImageIcon[] icons = {
            new ImageIcon("data/images/achievements/apexslime.png"), // 0
            new ImageIcon("data/images/achievements/rawmeat.png"), // 1
            new ImageIcon("data/images/achievements/apexvhard.png"), // 2
            new ImageIcon("data/images/achievements/tutorial.png"), // 3
            new ImageIcon("data/images/achievements/workbench.png"), // 4
            new ImageIcon("data/images/achievements/meat.png"), // 5
            new ImageIcon("data/images/achievements/hotdog.png"), // 6
            new ImageIcon("data/images/achievements/stoneTool.png"), // 7
            new ImageIcon("data/images/achievements/mines.png"), // 8
            new ImageIcon("data/images/achievements/deepMines.png"), // 9
            new ImageIcon("data/images/achievements/supervisor.png"), // 10
            new ImageIcon("data/images/achievements/supervisorvhard.png"), // 11
    };
    private static boolean[] hasAchievement = new boolean[titles.length];

    public static final short APEXSLIME = 0, RAWMEAT = 1, APEXVHARD = 2, TUTORIAL = 3, WORKBENCH = 4, MEAT = 5, HOTDOG = 6, STONETOOL = 7, MINES = 8, DEEPMINES = 9, SUPERVISOR = 10, SUPERVISORVHARD = 11;

    public static void resizeIcons(){
        BTools.resizeImgIcon(lockedIcon, 64, 64);
        for(ImageIcon i: icons){
            BTools.resizeImgIcon(i, 64, 64);
        }
    }

    public static ArrayList<Integer> getSorted(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < num(); i++){
            if(has(i)){
                list.add(i);
            }
        }
        for(int i = 0; i < num(); i++){
            if(!has(i)){
                list.add(i);
            }
        }

        return list;
    }

    public static int numUnlocked(){
        int unlocked = 0;
        for(boolean b: hasAchievement){
            if(b) unlocked++;
        }
        return unlocked;
    }

    public static ImageIcon getIcon(int a){
        if(!has(a)) return lockedIcon;
        else return icons[a];
    }

    public static String getDesc(int a){
        if(!has(a)) return "?????";
        else return descs[a];
    }

    public static int num(){
        return titles.length;
    }

    public static boolean has(int a){
        return hasAchievement[a];
    }

    public static void get(short a){
        if(Trident.getCurrentScene().name.equals("tutorial") && a != 3) return;
        if(!hasAchievement[a]){
            hasAchievement[a] = true;
            HUD.addNotif(titles[a], HUD.NOTIF_TRPHY);
            save();
        }
    }

    public static void load(){
        try{
            for(int i = 0; i < hasAchievement.length; i++){
                hasAchievement[i] = false;
            }
            File f = new File("data/achievements.bson");
            if(f.exists()){
                ArrayList<BSonObject> objects = BSonParser.readFile("data/achievements.bson");
                BSonList list = (BSonList)BSonParser.getObject("achievements", objects);
                for(int i = 0; i < list.list.size(); i++){
                    hasAchievement[i] = list.list.get(i).getBoolean();
                }
            }else{
                save();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Something went wrong while trying to load achievements!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public static void save(){
        try{
            File f = new File("data/achievements.bson");
            f.createNewFile();
            PrintWriter writer = new PrintWriter(f);
            writer.println("{ achievements");
            for(boolean b: hasAchievement){
                writer.println("boolean " + b);
            }
            writer.println("}");
            writer.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Something went wrong while trying to save achievements!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
