package com.celestial.Gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MCSliderUI extends BasicSliderUI {

    private static float[] fracs = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
    private LinearGradientPaint p;
	private Image img;
	private Font font;
	private String text;

    public MCSliderUI(JSlider slider, Image img, String text, Font font) {
        super(slider);
        this.img = img;
        this.font = font.deriveFont(16.0F);
        this.text = text;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
    	Rectangle t = new Rectangle(300,40);
    	Graphics2D g2 = (Graphics2D) g.create();		
		
		g2.setPaint(Color.DARK_GRAY);
		g2.setFont(font);
		int stringLen = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int stringTall = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		int startx = t.width/2 - stringLen/2+2;
		int starty = t.height/2 + stringTall/4+2;
		g2.drawString(text, startx, starty);
		
		g2.setPaint(Color.WHITE);
		g2.setFont(font);
		stringLen = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		stringTall = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		startx = t.width/2 - stringLen/2;
		starty = t.height/2 + stringTall/4;
		g2.drawString(text, startx, starty);
		
		g2.dispose();
    }
    
    @Override
    public void paintTrack(Graphics g)
    {
    	
    }
    
    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle t = thumbRect;
        t.setSize(15, 40);
        t.setLocation(t.x, 0);
        g2d.drawImage(img, t.x, 0, 15, 40, null);
    }

	public void setText(String text2) {
		this.text = text2;
	}
}