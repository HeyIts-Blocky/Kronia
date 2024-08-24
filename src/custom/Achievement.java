package custom;

import blib.bson.*;
import blib.util.BTools;
import ent.HUD;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Achievement {

    public static ImageIcon lockedIcon = new ImageIcon("data/images/achievements/locked.png");
    private static final String[] titles = {
            "Getting your feet... wet?", // 0
            "Of course you would.", // 1
            "Fully gooped", // 2
    };
    private static final String[] descs = {
            "Slay the Apex Slime.", // 0
            "Get sick by eating raw meat.", // 1
            "Slay the Apex Slime in Very Hard.", // 2
    };
    private static final ImageIcon[] icons = {
            new ImageIcon("data/images/achievements/apexslime.png"), // 0
            new ImageIcon("data/images/achievements/rawmeat.png"), // 1
            new ImageIcon("data/images/achievements/apexvhard.png"), // 2
    };
    private static boolean[] hasAchievement = new boolean[titles.length];

    public static final int APEXSLIME = 0, RAWMEAT = 1, APEXVHARD = 2;

    public static void resizeIcons(){
        BTools.resizeImgIcon(lockedIcon, 64, 64);
        for(ImageIcon i: icons){
            BTools.resizeImgIcon(i, 64, 64);
        }
    }

    public static boolean has(int a){
        return hasAchievement[a];
    }

    public static void get(int a){
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
