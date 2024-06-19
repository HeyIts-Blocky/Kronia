package custom;

import javax.swing.*;
import java.awt.*;
import blib.util.*;
import trident.*;
public class TitleSlider extends TitleButton{

    public static int MASTER = 0, SFX = 1, MUSIC = 2;

    ImageIcon bar = new ImageIcon("data/images/bar.png"), notch = new ImageIcon("data/images/notch.png");
    int minAudio = 0, maxAudio = 100;
    int type;
    
    public TitleSlider(Point p, String text, int t){
        super(new Rectangle(p.x, p.y, 140, 30), null, null, null, new Font(GameData.getFont(), Font.PLAIN, 15), text, TitleButton.LEFT);
        type = t;
    }

    public void update(long elapsedTime){
        if(Trident.getMouseDown(1)){
            Point mousePos = Trident.mousePos;
            if(rect.contains(mousePos)){
                int x = mousePos.x - rect.x;
                double percent = x / (double)rect.width;
                if(type == MASTER){
                    int diff = maxAudio - minAudio;
                    int vol = minAudio + (int)(diff * percent);
                    Audio.volume = vol;
                }
                if(type == SFX){
                    Settings.sfxVolume = percent;
                }
                if(type == MUSIC){
                    MusicManager.volume = percent;
                }
                
            }
        }
    }

    public void render(Graphics g){
        bar.paintIcon(null, g, rect.x - 5, rect.y + 4);
        int diff = maxAudio - minAudio;
        int volDiff = maxAudio - Audio.volume;
        double percent = (double)volDiff / diff;
        if(type == SFX) percent = -Settings.sfxVolume + 1;
        if(type == MUSIC) percent = -MusicManager.volume + 1;
        notch.paintIcon(null, g, (rect.x + rect.width) - (int)((rect.width) * percent) - 5, rect.y - 2); 
        g.setFont(font);
        TextBox.draw(text, g, rect.x, rect.y - 10);
    }
}
