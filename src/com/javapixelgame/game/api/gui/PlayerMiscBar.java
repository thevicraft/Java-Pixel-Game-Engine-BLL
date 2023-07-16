package com.javapixelgame.game.api.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class PlayerMiscBar extends JPanel implements Tickable {
	private World world;
	private JLabel name;
	private HealthBar bar;

	private GridBagConstraints cgb;

	public PlayerMiscBar(int x, int y,int width, int height, World world) {
		this.world = world;
		setLocation(x, y);
		setSize(width, height);
		setPreferredSize(getSize());
		setFocusable(false);
		setOpaque(false);
//		setBackground(Color.yellow);

		cgb = new GridBagConstraints();

		cgb.fill = GridBagConstraints.HORIZONTAL;

		setLayout(new GridBagLayout());

		name = new JLabel();
		name.setSize(width, height / 2);
		name.setPreferredSize(name.getSize());
		name.setOpaque(false);
		name.setHorizontalAlignment(SwingUtilities.CENTER);
//		name.setFont(GameFont.getKarmaticArcade(Font.PLAIN, name.getFont().getSize2D()));
		name.setFont(GameFont.getKarmaticArcade(Font.PLAIN, name.getHeight()*0.8f));
		name.setForeground(Color.blue);
		cgb.gridy += 1;
		add(name, cgb);

		bar = new HealthBar(width, height / 2);
		bar.setBackground(Color.gray);
		bar.setForeground(Color.red);
		cgb.gridy += 1;
		add(bar, cgb);

	}


	@Override
	public void onWorldTick(int tick) {
		// TODO Auto-generated method stub
		bar.setMaxLife(world.getPlayer().getMaxHealthPoints());
		bar.updateLife(world.getPlayer().getHealthPoints());
		name.setText(world.getPlayer().getRegistry().getName()/* + (world.getPlayer().getAttackCooldown() == -1 ? " +++" : "")*/);
	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub

	}

	public static class HealthBar extends JLabel {
		private double percentage = 1;
		private int borderSize = 3;
		private int max = 0;
		private boolean attributesVisible = true;

		public HealthBar(int width, int height) {
			setSize(width, height);
			setPreferredSize(getSize());
			setFocusable(false);
			setBackground(Color.gray);
			setForeground(Color.red);
			setFont(GameFont.getRainyHearts(Font.BOLD, getHeight()-2*borderSize));
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics = (Graphics2D) g;
			graphics.setColor(getBackground());
			graphics.fillRect(0, 0, getWidth(), getHeight());

			graphics.setColor(Color.DARK_GRAY);
			graphics.fillRect(borderSize, borderSize, getWidth() - 2 * borderSize, getHeight() - 2 * borderSize);

			graphics.setColor(getForeground());
			graphics.fillRect(borderSize, borderSize,
					(int) ((double) (getWidth() - 2 * borderSize) * (double) percentage), getHeight() - 2 * borderSize);
			graphics.setFont(getFont());
			graphics.setColor(Color.white);
			graphics.drawString(getText(),
					getWidth() / 2
							- graphics.getFontMetrics().charsWidth(getText().toCharArray(), 0, getText().length()) / 2,
					getHeight() - borderSize * 2);
			graphics.dispose();
		}

		public double getPercentage() {
			return percentage;
		}

		public void updateLife(int life) {
			this.percentage = life / (double) getMaxLife();
			if (isAttributesVisible()) {
				setText("-"+life+"-");
			}
			repaint();
		}

		public int getBorderSize() {
			return borderSize;
		}

		public void setBorderSize(int borderSize) {
			this.borderSize = borderSize;
		}

		public int getMaxLife() {
			return max;
		}

		public void setMaxLife(int max) {
			this.max = max;
		}

		public boolean isAttributesVisible() {
			return attributesVisible;
		}

		public void setAttributesVisible(boolean attributesVisible) {
			this.attributesVisible = attributesVisible;
			if(!isAttributesVisible())
				setText("");
		}
	}
}
