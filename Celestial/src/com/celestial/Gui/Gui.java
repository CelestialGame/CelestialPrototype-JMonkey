/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.Gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import net.miginfocom.swing.MigLayout;

import com.celestial.Celestial;
import com.celestial.Gui.Gui;
import com.celestial.Gui.GuiOptions;
import com.celestial.Gui.JShadowButton;
import com.celestial.Gui.JTiledPanel;
import com.celestial.Gui.StretchedImage;

import de.lessvoid.nifty.Nifty;

public class Gui extends JFrame implements ActionListener, MouseListener {

	private Celestial parent;
	private Nifty nifty;
	private JPanel cardpane;
	private BufferedImage img_50;
	private BufferedImage img_64;
	Image img_bg_dirt;
	Image img_bg_stone;
	Image img_button;
	Image img_button_hover;
	Image img_button_disabled;
	Font font;
	ImageIcon icon;
	private JTiledPanel mainPane;
	private JShadowButton btnPlay;
	private JShadowButton btnOptions;
	private JShadowButton btnQuit;
	private JShadowButton btnBack;
	private JTiledPanel btnPlayBg;
	private JTiledPanel btnOptionsBg;
	private JTiledPanel btnQuitBg;
	private JTiledPanel btnBackBg;
	private GuiOptions optionsPane;
	public JTiledPanel gamePane;
	private JTiledPanel gameLoadPane;
	public final static String MAIN = "main";
	public final static String OPTIONS = "options";
	public final static String GAME = "game";
	public final static String GAMELOAD = "gameload";

	public Gui(Celestial celestial)
	{
		this.parent = celestial;
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(Celestial.width, Celestial.height);
		setMinimumSize(new Dimension(Celestial.width, Celestial.height));
		setResizable(false);
		setLocationRelativeTo(null);
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", Celestial.title);
		setTitle(Celestial.title);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}
	
	public void initGui() {

        cardpane = new JPanel(new CardLayout());

		InputStream isf = this.getClass().getResourceAsStream("/assets/fonts/Minecraftia.ttf");
		InputStream is50 = this.getClass().getResourceAsStream("/assets/images/Logo_Icon_50px.png");
		InputStream is64 = this.getClass().getResourceAsStream("/assets/images/Logo_Icon_64px.png");
		InputStream isbg = this.getClass().getResourceAsStream("/assets/images/background.png");
		InputStream isbg2 = this.getClass().getResourceAsStream("/assets/images/background2.png");
		InputStream isb1 = this.getClass().getResourceAsStream("/assets/images/button.png");
		InputStream isb2 = this.getClass().getResourceAsStream("/assets/images/button_hover.png");
		InputStream isb3 = this.getClass().getResourceAsStream("/assets/images/button_clicked.png");
		try {
			img_50 = ImageIO.read(is50);
			img_64 = ImageIO.read(is64);
			img_bg_dirt = ImageIO.read(isbg).getScaledInstance(64, 64, Image.SCALE_DEFAULT);
			img_bg_stone = ImageIO.read(isbg2).getScaledInstance(64, 64, Image.SCALE_DEFAULT);
			img_button = ImageIO.read(isb1).getScaledInstance(400, 40, Image.SCALE_DEFAULT);
			img_button_hover = ImageIO.read(isb2).getScaledInstance(400, 40, Image.SCALE_DEFAULT);
			img_button_disabled = ImageIO.read(isb3).getScaledInstance(400, 40, Image.SCALE_DEFAULT);
			font = Font.createFont(Font.TRUETYPE_FONT, isf);
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
			font = Font.getFont("Arial");
		}
		icon = new ImageIcon(img_50);

		this.setIconImage(img_64);

		/* MAIN PANE */

		this.mainPane = new JTiledPanel(img_bg_stone);
		this.mainPane.setLayout(new BorderLayout(0, 0));

		JLabel mainlabel = new JLabel("<html><div style='margin-top:20px;'><img src='"+Gui.class.getResource("/assets/images/Logo_119px.png")+"'></div></html>", SwingConstants.CENTER);
		mainPane.add(mainlabel, BorderLayout.NORTH);

		JTiledPanel buttonsbg = new JTiledPanel(img_bg_dirt);
		buttonsbg.setPreferredSize(new Dimension(mainPane.getWidth(), 116));
		mainPane.add(buttonsbg, BorderLayout.SOUTH);
		JPanel buttons = new JPanel();
		buttons.setLayout(new MigLayout("filly, insets 0 117 0 117"));
		buttons.setOpaque(false);
		buttons.setBorder(new EmptyBorder(8, 0, 8, 0));
		buttonsbg.setLayout(new BorderLayout());
		buttonsbg.add(buttons, BorderLayout.CENTER);
		buttonsbg.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));

		btnPlay = new JShadowButton("Play", 16.0F, font, Color.white);
		btnPlay.setFocusPainted(false);
		btnPlay.setContentAreaFilled(false);
		btnPlay.setBorder(new EmptyBorder(0,0,0,0));
		btnOptions = new JShadowButton("Options", 16.0F, font, Color.white);
		btnOptions.setFocusPainted(false);
		btnOptions.setContentAreaFilled(false);
		btnOptions.setBorder(new EmptyBorder(0,0,0,0));
		btnQuit = new JShadowButton("Quit", 16.0F, font, Color.white);
		btnQuit.setFocusPainted(false);
		btnQuit.setContentAreaFilled(false);
		btnQuit.setBorder(new EmptyBorder(0,0,0,0));

		btnPlayBg = new JTiledPanel(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
		btnPlayBg.setLayout(new BorderLayout());
		btnPlayBg.add(btnPlay, BorderLayout.CENTER);
		btnOptionsBg = new JTiledPanel(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));
		btnOptionsBg.setLayout(new BorderLayout());
		btnOptionsBg.add(btnOptions, BorderLayout.CENTER);
		btnQuitBg = new JTiledPanel(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));
		btnQuitBg.setLayout(new BorderLayout());
		btnQuitBg.add(btnQuit, BorderLayout.CENTER);

		buttons.add(btnPlayBg, "width :610:, height :40:, hmax 40, alignx center, span");
		buttons.add(btnQuitBg, "width :300:, height :40:, hmax 40, alignx left");
		buttons.add(btnOptionsBg, "width :300:, height :40:, hmax 40, alignx right");

		btnPlay.addActionListener(this);
		btnQuit.addActionListener(this);
		btnOptions.addActionListener(this);
		btnPlay.addMouseListener(this);
		btnQuit.addMouseListener(this);
		btnOptions.addMouseListener(this);
		
		cardpane.add(mainPane, MAIN);

		/* OPTIONS */

		this.optionsPane = new GuiOptions(this);
		optionsPane.initGui();

		cardpane.add(optionsPane, OPTIONS);
		
		/* GAME LOADING */
		
		this.gameLoadPane = new JTiledPanel(img_bg_dirt);
		gameLoadPane.setLayout(new BorderLayout());
		
		cardpane.add(gameLoadPane, GAMELOAD);
		
		JShadowLabel loading = new JShadowLabel("Loading Game... Please Wait", Color.white, font);
		
		gameLoadPane.add(loading, BorderLayout.CENTER);
		
		/* GAME */
		
		this.gamePane = new JTiledPanel(img_bg_dirt);
		gamePane.setLayout(new BorderLayout());
		
		cardpane.add(gamePane, GAME);
		
		/* FINALIZE */
		
		setContentPane(this.cardpane);
	}	
	
	public void close() {
		this.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(this.btnPlay))
		{
			this.changeCard(GAMELOAD);
			Celestial.createNewCanvas(Celestial.SINGLEPLAYER);
			gamePane.add(Celestial.canvas, BorderLayout.CENTER);
			Celestial.app.startCanvas(true);
		}
		else if(arg0.getSource().equals(btnQuit))
		{
			this.close();
		}
		else if(arg0.getSource().equals(btnOptions))
		{
			this.changeCard(OPTIONS);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//nothing
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if(arg0.getSource().equals(btnPlay))
		{
			btnPlayBg.setTextureImage(StretchedImage.stretch(img_button_hover, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnPlay.setColor(new Color(255, 255, 160));
		}
		else if(arg0.getSource().equals(btnBack))
		{
			btnBackBg.setTextureImage(StretchedImage.stretch(img_button_hover, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnBack.setColor(new Color(255, 255, 160));
		}
		else if(arg0.getSource().equals(btnQuit))
		{
			btnQuitBg.setTextureImage(StretchedImage.stretch(img_button_hover, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));	
			btnQuit.setColor(new Color(255, 255, 160));
		}
		else if(arg0.getSource().equals(btnOptions))
		{
			btnOptionsBg.setTextureImage(StretchedImage.stretch(img_button_hover, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));	
			btnOptions.setColor(new Color(255, 255, 160));
		}

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if(arg0.getSource().equals(btnPlay))
		{
			btnPlayBg.setTextureImage(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnPlay.setColor(new Color(225, 225, 225));
		}
		else if(arg0.getSource().equals(btnBack))
		{
			btnBackBg.setTextureImage(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(610,40), BufferedImage.TYPE_INT_ARGB));
			btnBack.setColor(new Color(225, 225, 225));
		}
		else if(arg0.getSource().equals(btnQuit))
		{
			btnQuitBg.setTextureImage(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));	
			btnQuit.setColor(new Color(225, 225, 225));
		}
		else if(arg0.getSource().equals(btnOptions))
		{
			btnOptionsBg.setTextureImage(StretchedImage.stretch(img_button, new Insets(4,4,4,4), new Dimension(300,40), BufferedImage.TYPE_INT_ARGB));	
			btnOptions.setColor(new Color(225, 225, 225));
		}
	}

	public void playSound(String soundName)
	{
		try 
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
			Clip clip = AudioSystem.getClip( );
			clip.open(audioInputStream);
			clip.start( );
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace( );
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//nothing
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//nothing	
	}

	public void changeCard(String card)
	{
		CardLayout cl = (CardLayout)(cardpane.getLayout());
		cl.show(cardpane, card);
	}
}
