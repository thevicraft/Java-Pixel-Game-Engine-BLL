package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.GameConstants;
import com.javapixelgame.game.gui.component.ProgressBar;
import com.javapixelgame.game.resourcehandling.GameFont;
/**
 * The LoadingMenu Container.
 * @author thevicraft
 *
 */
@SuppressWarnings("serial")
public class LoadingMenu extends AbstractGUI {
	
	private ProgressBar loader;
	private JLabel title;
	private JLabel subtitle;
	/**
	 * The LoadingMenu Container.
	 */
	public LoadingMenu(int width, int height) {
		super(width, height);
	}
	/**
	 * <b>Stops</b> the progress bar from updating. This should be done when {@link LoadingMenu} is turned invisible.
	 */
	public void stop() {
		loader.stop();
	}
	/**
	 * <b>Starts</b> the progress bar updating loop. This should <b>only</b> be done after {@link LoadingMenu.stop()}.
	 * </p>
	 * <b>This is already executed on init!</b>
	 */
	public void start() {
		loader.start();
	}

	/**
	 * Method to iterate the loader after a certain loading step
	 * 
	 * @author thevicraft
	 */
	@Deprecated
	public void iterate() {
//		if (loader.getMaximum() >= loader.getValue())
//			loader.setValue(loader.getValue() + 1);
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
	}

	@Override
	protected void init() {
		setOpaque(true);
		setBackground(Color.red);

		title = new JLabel(GameConstants.GameProducer);
		title.setFont(GameFont.getKarmaticArcade(Font.BOLD, getHeight() / 10));
		title.setHorizontalAlignment(SwingUtilities.CENTER);

		loader = new ProgressBar(getWidth() - 50, 15, Color.white, Color.red);
		loader.start();

		subtitle = new JLabel(GameConstants.GameDedication);
		subtitle.setFont(GameFont.getKarmaticArcade(Font.BOLD, getHeight() / 20));
		subtitle.setHorizontalAlignment(SwingUtilities.CENTER);

		addComponent(title, 1, 1);
		addComponent(loader, 1, 2);
		addComponent(subtitle, 1, 3);

		updateUI();

	}
}
