package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class ProgressBar extends JPanel{
	
	private Rectangle paintArea;
	private Rectangle bar;
	private int border = 2;
	private int speed = 5;
	private Color foreG;
	
	public ProgressBar(int width, int height, Color background, Color foreground) {
		foreG = foreground;
		setSize(width,height);
		setPreferredSize(getSize());
		setBackground(background);
		paintArea = new Rectangle(border,border,width - border, height-border);
		bar = new Rectangle();
		bar.setLocation(paintArea.getLocation());
		bar.setSize((int)(paintArea.width*0.125), paintArea.height-border);
		setDoubleBuffered(true);
		setLayout(null);
	}
	
	private Timer timer = new Timer(17, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {

			if(bar.x <= paintArea.x)
				setSpeed(Math.abs(getSpeed()));
			if(bar.x+bar.width >= paintArea.x+paintArea.width)
				setSpeed(-Math.abs(getSpeed()));
			
			bar.setLocation(bar.x+getSpeed(), bar.y);
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {

					repaint();
					Toolkit.getDefaultToolkit().sync();
				}
			});
		}
	});
	
	public void start() {
		timer.start();
	}
	public void stop() {
		timer.stop();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		synchronized(graphics) {
		graphics.setColor(getBackground());
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setColor(foreG);
		graphics.fillRect(bar.x, bar.y, bar.width, bar.height);
	}}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
