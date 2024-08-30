package custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import blib.util.TextBox;
public class TitleButton {

    public Rectangle rect;
    public Color color, outline, textColor;
    public Font font;
    public String text;
    public int alignment;

    public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
    
    public TitleButton(Rectangle r, Color c, Color out, Color tCol, Font f, String t, int align){
        rect = r;
        color = c;
        outline = out;
        textColor = tCol;
        font = f;
        text = t;
        alignment = align;

        if(alignment == CENTER){
            rect.x -= rect.width / 2;
        }
        if(alignment == RIGHT){
            rect.x -= rect.width;
        }
    }

    public void render(Graphics g){
        g.setColor(color);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(outline);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(textColor);
        font = new Font(GameData.getFont(), font.getStyle(), font.getSize());
        g.setFont(font);
        TextBox.draw(text, g, rect.x + rect.width / 2, rect.y + rect.height / 2, TextBox.CENTER);
    }
}
