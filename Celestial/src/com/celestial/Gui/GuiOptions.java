package com.celestial.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import net.miginfocom.swing.MigLayout;

public class GuiOptions extends JTiledPanel implements ActionListener, MouseListener{

	private Gui gui;
	private MCSlider jvmHeap;
	private JShadowButton btnBack;
	private JTiledPanel btnBackBg;

	public GuiOptions(Gui gui)
	{
		this.gui = gui;
		this.setTextureImage(gui.img_bg_stone);
	}
	
	public void initGui()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setOpaque(false);
		top.setBorder(new EmptyBorder(10, 5, 5, 5));
		JShadowLabel title = new JShadowLabel("Nothing to see here....", Color.white, gui.font);
		
		top.add(title, BorderLayout.NORTH);
		add(top, BorderLayout.CENTER);

		JTiledPanel buttonsoptbg = new JTiledPanel(gui.img_bg_dirt);
		buttonsoptbg.setPreferredSize(new Dimension(getWidth(), 116));
		add(buttonsoptbg, BorderLayout.SOUTH);
		JPanel buttonsopt = new JPanel();
		buttonsopt.setLayout(new MigLayout("filly, insets 0 117 0 117"));
		buttonsopt.setOpaque(false);
		buttonsopt.setBorder(new EmptyBorder(8, 0, 8, 0));
		buttonsoptbg.setLayout(new BorderLayout());
		buttonsoptbg.add(buttonsopt, BorderLayout.CENTER);
		buttonsoptbg.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));

		btnBack = new JShadowButton("Go Back", 16.0F, gui.font, Color.white);
		btnBack.setFocusPainted(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorder(new EmptyBorder(0,0,0,0));

		btnBackBg = new JTiledPanel(StretchedImage.stretch(gui.img_button, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
		btnBackBg.setLayout(new BorderLayout());
		btnBackBg.add(btnBack, BorderLayout.CENTER);

		buttonsopt.add(btnBackBg, "width :610:, height :40:, hmax 40, alignx center, span");


		btnBack.addActionListener(this);
		btnBack.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if(arg0.getSource().equals(btnBack))
		{
			btnBackBg.setTextureImage(StretchedImage.stretch(gui.img_button_hover, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnBack.setColor(new Color(255, 255, 160));
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if(arg0.getSource().equals(btnBack))
		{
			btnBackBg.setTextureImage(StretchedImage.stretch(gui.img_button, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnBack.setColor(new Color(225, 225, 225));
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(btnBack))
		{
			gui.changeCard(Gui.MAIN);
		}
	}
	
}
