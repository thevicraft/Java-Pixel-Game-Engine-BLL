package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.BackgroundWorker;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.gui.Chat;
import com.javapixelgame.game.api.gui.Hotbar;
import com.javapixelgame.game.api.gui.InfoBox;
import com.javapixelgame.game.api.gui.MiniMap;
import com.javapixelgame.game.api.gui.PlayerMiscBar;
import com.javapixelgame.game.api.gui.overlay.AbstractOverlay;
import com.javapixelgame.game.api.gui.overlay.InventoryOverlay;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.handling.KeyActionHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.util.ComponentUtil;

/**
 * GamePanel where the graphical game components lie on
 * 
 * @author thevicraft
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JLayeredPane {

	public double aspect_ratio;

	public JLabel positionLabel, tickCalcLabel, fpsCounter;
	public Button switchReadKey;
	public JLayeredPane main;

	public Chat chat;

	public PlayerMiscBar miscbar;

	public MiniMap minimap;

	public Hotbar hotbar;

	public InfoBox infobox;

	public PauseMenu pausemenu;

	public DeathMenu deathmenu;

	public InventoryOverlay inventory_gui;

	private AbstractOverlay currentGUI;

	private boolean openGUI = false;

	public boolean tab_switch = false;

	private boolean running = false;

	public ActionListener onFrame = new ActionListener() {
		long last = 0;
		long now = 0;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!ticking)
				return;

			now = System.currentTimeMillis();
			FPS = (int) (1000 / (double) (now - last));
			last = now;

			GameHandler.getWorld().repaint();

//			BackgroundWorker.invokeLater(() -> {

				Toolkit.getDefaultToolkit().sync();


//			});
		}
	};

	public Timer framework = new Timer(16, onFrame);

	public int FPS;

	public GamePanel(int width, int height) {

		aspect_ratio = (double) width / (double) height; // aspect ratio of the given width and height

		setLayout(null);
		setSize(width, height);

		setPreferredSize(getSize());

		initMainPanel();
		initSideComponents();

		setBackground(Color.red);

		add(main, new Integer(1));

	}

	/**
	 * Returns the <strong>GLOBAL</strong> instance of this. This method just
	 * returns:
	 * </p>
	 * <b>Main.<i>game</i>.gp</b>
	 * </p>
	 * 
	 * @return global GamePanel
	 */
	public static final GamePanel get() {
		return Main.game.gp;
	}

	private void initMainPanel() {
		main = new JLayeredPane();
		main.setBounds(0, 0, getWidth(), getHeight());
		main.setLayout(null);
		chat = new Chat(main.getWidth() / 2, main.getHeight() / 2, 15);
		chat.setLocation(10, main.getHeight() / 2);
	}

//	public static final boolean load = true;

	private static int compsize;

	public static int getCompsize() {
		return compsize;
	}

	public void openGUI(AbstractOverlay overlay) {
		closeGUI();
		if (overlay == null)
			return;
		currentGUI = overlay;
		switch (currentGUI.getGuiAlignment()) {
		case SwingConstants.CENTER:
			currentGUI.setLocation(ComponentUtil.placeCentered(currentGUI, main));
			break;
		case SwingConstants.LEFT:
			currentGUI.setLocation(ComponentUtil.placeLeft(currentGUI, main));
			break;
		case SwingConstants.RIGHT:
			currentGUI.setLocation(ComponentUtil.placeRight(currentGUI, main));
			break;
		}
		main.add(overlay, new Integer(20));
		openGUI = true;
		currentGUI.open();
	}

	public void closeGUI() {
		if (currentGUI == null)
			return;
		currentGUI.close();
		main.remove(currentGUI);
		currentGUI = null;
		openGUI = false;
	}

	public void openInventory() {
		openGUI(inventory_gui);
	}

	public void updateGUI() {
		// -------------------------------------
		compsize = getWidth() / (13 - Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.gui_scale)));
		// -------------------------------------

		main.removeAll();
		currentGUI = null;

		inventory_gui = new InventoryOverlay(compsize * 3, compsize);

//		miscbar = new PlayerMiscBar(getWidth() - compsize - (compsize / 10/* border */), (compsize / 10/* border */),
//				compsize, compsize / 4, GameHandler.getWorld());
		miscbar = new PlayerMiscBar(compsize / 10/* border */,
				(getHeight() - (compsize / 4/* width */) - (compsize / 10/* border */)), compsize, compsize / 4,
				GameHandler.getWorld());

		hotbar = new Hotbar(GameHandler.getWorld().getPlayer().getInventory(), compsize * 3/* 500 */, getWidth() / 2,
				getHeight());
		hotbar.setLocation(hotbar.getLocation().x, getHeight() - hotbar.getHeight() - compsize / 10);

		if (ConfigHandler.getConfig(ConfigHandler.minimap).equals("true")) {
			minimap = new MiniMap(0, 0, 10, compsize, compsize);
			main.add(minimap, new Integer(3));
			GameHandler.getWorld().getTick().addTickingComponent(minimap);
		}

		infobox = new InfoBox(compsize * 4, compsize * 2, compsize * 4, compsize,
				(7 - Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.gui_scale))) * 2 + 4);

		main.add(GameHandler.getWorld(), new Integer(1));
		main.add(chat, new Integer(3));

		main.add(hotbar, new Integer(2));
		main.add(miscbar, new Integer(3));
		main.add(infobox, new Integer(4));

		Main.game.createBufferStrategy(2);

		BackgroundWorker.invokeLater(() -> {

			GameHandler.getWorld().getTick().addTickingComponent(hotbar);
			GameHandler.getWorld().getTick().addTickingComponent(chat);
			GameHandler.getWorld().getTick().addTickingComponent(miscbar);
			GameHandler.getWorld().getTick().addTickingComponent(infobox);
			GameHandler.getWorld().getTick().addTickingComponent(new KeyActionHandler());

			GameHandler.getWorld().getTick().addTickingComponent(new Tickable() {

				@Override
				public void onWorldTick(int tick) {
					// TODO Auto-generated method stub
					if (getCurrentGUI() != null)
						getCurrentGUI().onWorldTick(tick);

					if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true") && tick == 1) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								String s = Main.getAllocatedMegaBytes() + "MB";
								positionLabel.setText(s);
								Console.error(s + " ram usage");

							}
						}).start();
					} else if (!ConfigHandler.getConfig(ConfigHandler.debug).equals("true")) {

						positionLabel.setText(
								"x: " + Integer.toString(GameHandler.getWorld().getPlayer().getPosition().x) + " "
										+ "y: " + Integer.toString(GameHandler.getWorld().getPlayer().getPosition().y));
					}
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					double calc = GameHandler.getWorld().getTick().TickCalculationLength;
					tickCalcLabel.setText(df.format(calc) + " ms");
					Color stage = Color.white;
					if (calc > 10)
						stage = Color.red;
					else if (calc > 8)
						stage = Color.yellow;
					else if (calc > 0)
						stage = Color.green;
					tickCalcLabel.setForeground(stage);

					if (tick == 1) {
						Color fpsColor = null;
						if (FPS > 40)
							fpsColor = Color.green;
						else if (FPS > 30)
							fpsColor = Color.yellow;
						else {
							fpsColor = Color.red;

						}
						fpsCounter.setForeground(fpsColor);

						fpsCounter.setText(FPS + " fps");
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							Runtime.getRuntime().gc();
						}
					}).start();
				}

				@Override
				public void onRandomTick() {
				}
			});

			framework = new Timer(1000 / Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.fps)), onFrame);

		});
	}

	public void stopGame() {
		running = false;
		if (GameHandler.getWorld() != null)
			GameHandler.getWorld().setTicking(false);
		framework.stop();
	}

	public void startGame() {
		running = true;
		GameHandler.getWorld().setTicking(true);
		framework.start();

		if (isKilledState())
			setKilledState();

	}

	private boolean killedState;

	public void setKilledState() {
		killedState = true;
		stopGame();
		deathmenu.setVisible(true);
	}

	public void respawn() {
		killedState = false;
		startGame();
		deathmenu.setVisible(false);
		GameHandler.getWorld().getPlayer().respawn();
	}

	public boolean ticking = false;

	public void toggleGameState() {
		if (isKilledState())
			return;
		ticking = !ticking;
		pausemenu.setVisible(!ticking);
		GameHandler.getWorld().setTicking(ticking);
		running = ticking;
		if (ticking)
			framework.start();
		else
			framework.stop();

	}

	public void pauseGame() {
		if (isKilledState() || !isRunning())
			return;
		ticking = false;
		pausemenu.setVisible(true);
		GameHandler.getWorld().setTicking(false);
		running = false;
		framework.stop();
	}

	public void continueGame() {
		if (isKilledState() || isRunning())
			return;
		ticking = true;
		pausemenu.setVisible(false);
		GameHandler.getWorld().setTicking(true);
		running = true;
		framework.start();

	}

	private void initSideComponents() {
		positionLabel = new JLabel();
		tickCalcLabel = new JLabel();
		fpsCounter = new JLabel();

		switchReadKey = new Button("Debug", 200);
		switchReadKey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GameHandler.getWorld().renderHitboxes(!GameHandler.getWorld().areHitboxesRendered());
				switch (ConfigHandler.getConfig(ConfigHandler.debug)) {
				case "true":
					ConfigHandler.setConfig(ConfigHandler.debug, "false");
					break;
				case "false":
					ConfigHandler.setConfig(ConfigHandler.debug, "true");
					break;
				}
				positionLabel.setForeground(
						ConfigHandler.getConfig(ConfigHandler.debug).equals("true") ? Color.green : Color.white);

			}
		});
		switchReadKey.setLocation(10, 220);

		positionLabel.setBounds(10, 150, 120, 20);
		tickCalcLabel.setBounds(10, 180, 80, 20);
		fpsCounter.setBounds(10, 200, 80, 20);

		tickCalcLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		fpsCounter.setHorizontalAlignment(SwingUtilities.RIGHT);
		positionLabel.setHorizontalAlignment(SwingUtilities.RIGHT);

		add(positionLabel, new Integer(2));
		add(tickCalcLabel, new Integer(2));
		add(fpsCounter, new Integer(2));
		positionLabel.setOpaque(true);
		tickCalcLabel.setOpaque(true);
		fpsCounter.setOpaque(true);
		positionLabel.setBackground(Color.black);
		tickCalcLabel.setBackground(Color.black);
		positionLabel.setForeground(Color.white);
		tickCalcLabel.setForeground(Color.white);
		fpsCounter.setForeground(Color.white);
		fpsCounter.setBackground(Color.black);

		add(switchReadKey, new Integer(2));

		pausemenu = new PauseMenu(getWidth(), getHeight());

		pausemenu.setVisible(false);

		pausemenu.setLocation(0, 0);

		add(pausemenu, new Integer(4));

		deathmenu = new DeathMenu(getWidth(), getHeight());

		deathmenu.setVisible(false);

		deathmenu.setLocation(0, 0);

		add(deathmenu, new Integer(3));

	}

	public boolean isKilledState() {
		return killedState;
	}

	public boolean isRunning() {
		return running;
	}

	public AbstractOverlay getCurrentGUI() {
		return currentGUI;
	}

	public boolean isOpenGUI() {
		return openGUI;
	}
}
