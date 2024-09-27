package trident;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
public class Main{
    public static JFrame window = new JFrame("Kronia");
    public static void main(String[] args){
        window.setSize(700, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(700, 500));
        window.setIconImage(new ImageIcon("data/icon.png").getImage());
        // panel
        MainPanel panel = new MainPanel();
        window.add(panel);
        //
        window.setVisible(true);
    }
}
