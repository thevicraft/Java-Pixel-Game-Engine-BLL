package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import com.javapixelgame.game.resourcehandling.Images;

/**
 * Loader with loading Bar to show a loading sequence / working process
 * 
 * @author thevicraft
 * @category JFrame
 * @see Game
 * 
 */
@SuppressWarnings("serial")
public class Loader extends JFrame {

	public JProgressBar loadBar;

	public JPanel loadCircle;

//	public Thread anim;

	private int count;
	private Timer timer;
	private Graphics2D g2d;

//	private Color backbround = new Color(207, 207, 207);
	private Color background = new Color(63, 66, 66);
	
	private int diameter = 50;
	private int procBarHeight = 15;
	
	Image icon = null;

	/**
	 * Constructor for Loader
	 * 
	 * @param typeLoading - title of the loader
	 * @param max_value   - maximum of loading value steps (e.g.: the loader needs
	 *                    to show how 78 images are loaded, so this number is set to
	 *                    78)
	 * @author thevicraft
	 * @see Loader
	 * 
	 */
	public Loader(String typeLoading, int max_value) {
		setTitle("Loading " + typeLoading + "...");
		setSize(310, diameter + procBarHeight + 50); // 310,175
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setBackground(background);

		loadBar = new JProgressBar(0, max_value);
		loadBar.setPreferredSize(new Dimension(300, procBarHeight));
		loadBar.setStringPainted(true);
		loadBar.setForeground(new Color(0, 230, 27));
		loadBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		Image icon = Images.imageFromFileName("loading.png").getImage();// .getScaledInstance(64, 64, 0);

		
		loadCircle = new JPanel() {

			@Override
			public void paintComponent(Graphics g) {
//				super.paintComponent(g);
				g2d = (Graphics2D) g;

				g2d.rotate(count / 180.0 * Math.PI, diameter / 2, diameter / 2);
				// the length of the panel (100) minus the height of the image (64) equals 36,
				// to positionate it correctly it has to be the half, so 18
				g2d.drawImage(icon, 0, 0, diameter, diameter, this);
			}
		};

		timer = new Timer(5, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				count += 2;
				if (count > 360)
					count -= 360;

				loadBar.updateUI();
				loadCircle.updateUI();
				loadCircle.repaint();
				loadCircle.updateUI();
				loadBar.updateUI();
			}
		});
		loadCircle.setPreferredSize(new Dimension(diameter, diameter));
		loadCircle.setBackground(background);
		add(loadBar);
		add(loadCircle);
		loadBar.setValue(0);
		setIconImage(icon);
		setVisible(true);
		loadBar.updateUI();
		timer.start();
	}

	@Override
	public void dispose() {
		super.dispose();
		try {
			timer.stop();
		} catch (Exception e) {
		}
	}

	/**
	 * Method to iterate the loader after a certain loading step
	 * 
	 * @author thevicraft
	 * @see Loader
	 * 
	 */
	public void iterate() {
		if (loadBar.getMaximum() >= loadBar.getValue())
			loadBar.setValue(loadBar.getValue() + 1);
	}

}
