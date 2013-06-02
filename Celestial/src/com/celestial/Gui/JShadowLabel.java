package com.celestial.Gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class JShadowLabel extends JPanel{
	
	private String text;
	private Color color;
	private Font f;

	public JShadowLabel(String text, Color color, Font f)
	{
		this.text = text;
		this.color = color;
		this.f = f.deriveFont(16.0F);
		this.setOpaque(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();		
		
		g2.setPaint(Color.DARK_GRAY);
		g2.setFont(f);
		int stringLen = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int stringTall = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		int startx = this.getWidth()/2 - stringLen/2+2;
		int starty = this.getHeight()/2 + stringTall/4+2;
		g2.drawString(text, startx, starty);
		
		g2.setPaint(color);
		g2.setFont(f);
		stringLen = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		stringTall = (int)
				g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		startx = this.getWidth()/2 - stringLen/2;
		starty = this.getHeight()/2 + stringTall/4;
		g2.drawString(text, startx, starty);
		
		g2.dispose();
		this.setBorder(new EmptyBorder(5, 0, 5, 0));
		super.paintComponent(g);
	}
	
	public void setColor(Color c) {
		this.color = c;
		repaint();
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}
}
