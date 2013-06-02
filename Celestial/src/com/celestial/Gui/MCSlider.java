package com.celestial.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MCSlider extends JPanel implements ChangeListener{

	private JSlider slider;
	private JShadowLabel lbl;
	private MCSliderUI mcui;
	private Image button;
	private Image button2;
	private Font f;
	private static ArrayList registeredListeners = new ArrayList();

	/**
	 * Creates a Slider in the style of Minecraft
	 * 
	 * @param init_text Initial text to be displayed
	 * @param orientation Orientation of the Slider (Use JSlider.*)
	 * @param min Minimum ticks
	 * @param max Maximum ticks
	 * @param init Initial tick selected
	 * @param minor Minor Spacing
	 * @param major Major Spacing
	 * @param label Label above slider
	 */
	public MCSlider(String init_text, int orientation, int min, int max, int init, int minor, int major, String label)
	{
		InputStream isf = this.getClass().getResourceAsStream("/fonts/Minecraftia.ttf");
		InputStream isb1 = this.getClass().getResourceAsStream("/images/button.png");
		InputStream isb2 = this.getClass().getResourceAsStream("/images/button_clicked.png");
		try {
			button = ImageIO.read(isb1).getScaledInstance(400, 40, Image.SCALE_DEFAULT);
			button2 = ImageIO.read(isb2).getScaledInstance(400, 40, Image.SCALE_DEFAULT);
			f = Font.createFont(Font.TRUETYPE_FONT, isf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		slider = new JSlider(orientation,
				min, max, init);

		slider.setOpaque(false);

		lbl = new JShadowLabel(label, Color.white, f);
		
		mcui = new MCSliderUI(slider, StretchedImage.stretch(button, new Insets(4,4,4,4), new Dimension(15,40), BufferedImage.TYPE_INT_ARGB), init_text, f);

		slider.setMajorTickSpacing(major);
		slider.setMinorTickSpacing(minor);
		slider.setSnapToTicks(true);
		slider.setUI(mcui);
		slider.setPreferredSize(new Dimension(300, 40));
		slider.setFocusable(false);
		slider.setBorder(new EmptyBorder(0, 2, 0, 2));
		
		slider.addChangeListener(this);

		JTiledPanel bg = new JTiledPanel(StretchedImage.stretch(button2, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));
		bg.setPreferredSize(new Dimension(300, 40));
		bg.setLayout(new BorderLayout());
		bg.setOpaque(false);

		bg.add(slider, BorderLayout.CENTER);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));	
		this.lbl.setMaximumSize(new Dimension(600, 40));
		this.add(lbl);
		bg.setMaximumSize(new Dimension(300, 40));
		this.add(bg);
		this.setOpaque(false);
	}

	public void setText(String text)
	{
		mcui.setText(text);
		repaint();
	}

	public void setLabelText(String text)
	{
		lbl.setText(text);
	}
	
	public void addChangeListener(MCSliderListener listener)
	{
		if (listener != null) {
			registeredListeners.add(listener);
		}
	}

	public void removeChangeListener(MCSliderListener listener)
	{
		if (listener != null) {
			registeredListeners.remove(listener);
		}
	}

	public int getValue()
	{
		return slider.getValue();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		int count = registeredListeners.size();
		for (int i = 0; i < count; i++) {
			( (MCSliderListener) registeredListeners.get(i)).stateChanged(arg0);
		}
	}
}
