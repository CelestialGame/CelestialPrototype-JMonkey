package com.celestial.Gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.celestial.Celestial;

/**
 * Main Menu GUI JFrame
 *
 * @author kevint.
 *         Created Jun 8, 2013.
 */
public class MainMenu extends JFrame implements ActionListener, MouseListener{
	
	private Celestial parent;
	private JPanel mainPane;
	private Font font;
	private BufferedImage img_gt;
	private BufferedImage img_50;
	private BufferedImage img_64;
	private ImageIcon icon;
	private JButton spplay;
	private JButton options;
	private JButton quit;
	public JPanel topcardPane;
	private GuiOptions optionsPane;
	private JPanel gameLoadPane;
	private JPanel gamePane;
	
	public final static String MAIN = "main";
	public final static String OPTIONS = "options";
	public final static String GAME = "game";
	public final static String GAMELOAD = "gameload";

	public MainMenu(Celestial celestial) {
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
	
	public void close() {
		this.dispose();
		System.exit(0);
	}
	
	public void init() {
		
		InputStream isf = this.getClass().getResourceAsStream("/assets/fonts/PTS55F.ttf");
		InputStream isgt = this.getClass().getResourceAsStream("/assets/images/glasstile.png");
		InputStream is50 = this.getClass().getResourceAsStream("/assets/images/Logo_Icon_50px.png");
		InputStream is64 = this.getClass().getResourceAsStream("/assets/images/Logo_Icon_64px.png");
		try {
			this.img_gt = ImageIO.read(isgt);
			this.img_50 = ImageIO.read(is50);
			this.img_64 = ImageIO.read(is64);
			font = Font.createFont(Font.TRUETYPE_FONT, isf);
		}  catch (IOException exception) {
			exception.printStackTrace();
		} catch (FontFormatException exception) {
			exception.printStackTrace();
			this.font = Font.getFont("Arial");
		}
		
		this.icon = new ImageIcon(this.img_50);
		this.setIconImage(this.img_64);
		
		this.topcardPane = new JPanel(new CardLayout());
		
		this.mainPane = new JPanel();
		this.mainPane.setOpaque(false);
		this.mainPane.setLayout(new BorderLayout(0, 0));
		
		JLabel mainlabel = 
				new JLabel("<html><div style='margin-top:20px;background-color:black;'><img src='"+MainMenu.class.getResource("/assets/images/Logo_119px.png")+"'></div></html>", SwingConstants.CENTER);
		this.mainPane.add(mainlabel, BorderLayout.NORTH);
		
		/*JMEBackground.jmebg = new JMEBackground();
		JMEBackground.createNewCanvas();
		this.mainPane.add(JMEBackground.canvas, BorderLayout.CENTER);*/
		
		JPanel glassbg = new JPanel();
		this.spplay = new JButton("SinglePlayer");
		this.spplay.setFont(this.font);
		//this.spplay.setFocusPainted(false);
		//this.spplay.setContentAreaFilled(false);
		//this.spplay.setBorder(BorderFactory.createRaisedBevelBorder());
		glassbg.add(this.spplay);
		this.options = new JButton("Options");
		this.options.setFont(this.font);
		//this.options.setFocusPainted(false);
		//this.options.setContentAreaFilled(false);
		//this.options.setBorder(BorderFactory.createRaisedBevelBorder());
		glassbg.add(this.options);
		this.quit = new JButton("Quit");
		this.quit.setFont(this.font);
		//this.quit.setFocusPainted(false);
		//this.quit.setContentAreaFilled(false);
		//this.quit.setBorder(BorderFactory.createRaisedBevelBorder());
		glassbg.add(this.quit);
		glassbg.setMinimumSize(new Dimension(1500,1500));
		
		this.mainPane.add(glassbg, BorderLayout.SOUTH);
		
		this.spplay.addActionListener(this);
		this.options.addActionListener(this);
		this.quit.addActionListener(this);
		this.spplay.addMouseListener(this);
		this.options.addMouseListener(this);
		this.quit.addMouseListener(this);
		
		this.topcardPane.add(this.mainPane, MAIN);
		
		/* OPTIONS */

		//this.optionsPane = new GuiOptions(this);
		//optionsPane.initGui();
		//topcardPane.add(optionsPane, OPTIONS);
		
		/* GAME LOADING */
		
		this.gameLoadPane = new JPanel();
		gameLoadPane.setLayout(new BorderLayout());
		
		topcardPane.add(gameLoadPane, GAMELOAD);
		
		JShadowLabel loading = new JShadowLabel("Loading Game... Please Wait", Color.white, font);
		
		gameLoadPane.add(loading, BorderLayout.CENTER);
		
		/* GAME */
		
		this.gamePane = new JPanel();
		gamePane.setLayout(new BorderLayout());
		
		topcardPane.add(gamePane, GAME);
		
		/* FINALIZE */
		
		setContentPane(this.topcardPane);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.spplay))
		{
			Celestial.createNewCanvas(Celestial.SINGLEPLAYER);
			this.gamePane.add(Celestial.canvas, BorderLayout.CENTER);
			Celestial.app.startCanvas(true);
		}
		else if(e.getSource().equals(this.quit))
		{
			this.close();
		}
		else if(e.getSource().equals(this.options))
		{
			//TODO
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub.
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.equals(this.spplay)) {
			this.spplay.setBackground(new Color(0,255,0));
		} else if(e.equals(this.options)) {
			this.options.setBackground(new Color(0,255,0));
		} else if(e.equals(this.quit)) {
			this.quit.setBackground(new Color(0,255,0));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.equals(this.spplay)) {
			this.spplay.setBackground(new Color(255,255,255));
		} else if(e.equals(this.options)) {
			this.options.setBackground(new Color(255,255,255));
		} else if(e.equals(this.quit)) {
			this.quit.setBackground(new Color(255,255,255));
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub.
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub.
		
	}
	public void changeCard(String card)
	{
		CardLayout cl = (CardLayout)(this.topcardPane.getLayout());
		cl.show(this.topcardPane, card);
	}

}
