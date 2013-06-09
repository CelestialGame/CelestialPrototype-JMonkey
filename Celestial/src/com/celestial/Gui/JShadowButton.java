package com.celestial.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

public class JShadowButton extends JButton {

	private String text;

	private Font f;

	private boolean invertColors = false;

	private Color color;

	public JShadowButton() {
		super();
	}

	public JShadowButton(String text, Float size, Font font, Color c) {
		super();
		this.text = text;
		this.f = font.deriveFont(size);
		this.color = c;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();		
		
		g2.setPaint(Color.DARK_GRAY);
		g2.setFont(this.f);
		int stringLen = (int)
				g2.getFontMetrics().getStringBounds(this.text, g2).getWidth();
		int stringTall = (int)
				g2.getFontMetrics().getStringBounds(this.text, g2).getHeight();
		int startx = this.getWidth()/2 - stringLen/2+2;
		int starty = this.getHeight()/2 + stringTall/4+2;
		g2.drawString(this.text, startx, starty);
		
		g2.setPaint(this.color);
		g2.setFont(this.f);
		stringLen = (int)
				g2.getFontMetrics().getStringBounds(this.text, g2).getWidth();
		stringTall = (int)
				g2.getFontMetrics().getStringBounds(this.text, g2).getHeight();
		startx = this.getWidth()/2 - stringLen/2;
		starty = this.getHeight()/2 + stringTall/4;
		g2.drawString(this.text, startx, starty);
		
		g2.dispose();
	}

	public void setColor(Color c) {
		this.color = c;
		repaint();
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}

	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;

}

