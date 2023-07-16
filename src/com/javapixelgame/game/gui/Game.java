package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.javapixelgame.game.GameConstants;
import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.keyboard.KeyEventHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.resourcehandling.Images;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.resourcehandling.TextureLoader;

/**
 * Game Class, undefined due to lack of brain
 * 
 * @author thevicraft
 *
 */
@SuppressWarnings("serial")
public class Game extends JFrame {

	public int WINDOW_WIDTH;
	public int WINDOW_HEIGHT;

	public static final int loading = -1;
	public static final int main_menu = 0;
	public static final int in_game = 1;
	public static final int settings = 2;
	public static final int world_load_selection = 3;
	public static final int world_create_menu = 4;

	public static final int MIN_WIDTH = 250;

	public GamePanel gp;

	public MainMenu mainmenu;

	public SettingsMenu settingmenu;

	public LoadingMenu loadingmenu;
	
	public WorldLoadMenu worldloadmenu;
	
	public WorldCreationMenu worldcreatemenu;

	public int state;
	
	private boolean keyInput = true;

	public Game() {
		ConfigHandler.loadConfig();
		if (ConfigHandler.getConfig(ConfigHandler.fullscreen).equals("true") && Main.device.isFullScreenSupported()) {
			Main.device.setFullScreenWindow(this);
			WINDOW_WIDTH = getWidth();
			WINDOW_HEIGHT = getHeight();
		} else {
			WINDOW_WIDTH = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.width));
			WINDOW_HEIGHT = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.height));
		}

		double aspect_ratio = (double) WINDOW_WIDTH / (double) WINDOW_HEIGHT; // aspect ratio of the given width and
																				// height
		setTitle(GameConstants.title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_WIDTH / aspect_ratio)));
		setResizable(true);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.black);
		setLayout(new FlowLayout());

		Console.output("Game is starting up...");
		setVisible(true);

		loadingmenu = new LoadingMenu(getContentPane().getWidth(), getContentPane().getHeight());
		add(loadingmenu);
		loadingmenu.updateUI();
		Toolkit.getDefaultToolkit().sync();

		// -----------------STARTUP LOADING MENU
		onStartup();
		// -----------------STARTUP LOADING MENU ENDING

		setState(main_menu);
		Console.output("Game successfully started up!");
		Console.error("You are running version <" + GameConstants.VERSION + ">");
		Console.error(
				"This version is a snapshot/test release, so keep attention to errors and report them. Thanks in advance...");

		loadingmenu.setVisible(false);
		loadingmenu.stop();
		remove(loadingmenu);

		Toolkit.getDefaultToolkit().sync();
		mainmenu.updateUI();
	}

	/**
	 * Executed on <strong>startup</strong> of the program in the loading menu.
	 * <strong>Startup tasks</strong> should be done here.
	 */
	public void onStartup() {
		
		KeyEventHandler.loadConfig();

		KeyEventHandler.init(this);

		addKeyListener(KeyEventHandler.game_pause);
		
		addFocusListener(new Focus());

		if (!Images.isLoaded()) {
			Images.initImages();
		}
		TextureLoader.loadAll();

		initComponents();
		setIconImage(Images.getPicture(TextureID.ICON).getImage());
	}

	/**
	 * Executed on <strong>program exit</strong>. <strong>Shutdown tasks</strong>
	 * should be done here.
	 */
	public void onExit() {
		gp.stopGame();
		
		GameHandler.disposeWorld();
		
		ConfigHandler.saveConfig();
		KeyEventHandler.saveConfig();

		Images.unloadAll();
		TextureLoader.unloadAll();
	}

	private JComponent[] gui;

	private void initComponents() {
		gp = new GamePanel(getContentPane().getWidth(), getContentPane().getHeight());
		addGUIComponent(gp);

		mainmenu = new MainMenu(getContentPane().getWidth(), getContentPane().getHeight(),
				new Texture(TextureID.main_menu_background_character));
		addGUIComponent(mainmenu);

		settingmenu = new SettingsMenu(getContentPane().getWidth(), getContentPane().getHeight(),2);
		addGUIComponent(settingmenu);
		
		worldloadmenu = new WorldLoadMenu(getContentPane().getWidth(), getContentPane().getHeight());
		addGUIComponent(worldloadmenu);
		
		worldcreatemenu = new WorldCreationMenu(getContentPane().getWidth(), getContentPane().getHeight());
		addGUIComponent(worldcreatemenu);

		gui = new JComponent[] { gp, mainmenu, settingmenu, worldloadmenu, worldcreatemenu };
	}

	private void addGUIComponent(JComponent gui) {
		add(gui);
		gui.updateUI();
		gui.setVisible(false);
	}

	public void setState(int state) {
		this.state = state;
		switch (state) {
		case main_menu:
			showOnly(mainmenu);
			break;
		case in_game:
			showOnly(gp);
			gp.toggleGameState();
			break;
		case settings:
			showOnly(settingmenu);
			break;
		case world_load_selection:
			showOnly(worldloadmenu);
			break;
		case world_create_menu:
			showOnly(worldcreatemenu);
			break;

		default:
			new Exception("Illegal state access!").printStackTrace();
		}
	}

	private void showOnly(JComponent here) {
		int index = 0;
		for (int i = 0; i < gui.length; i++) {
			if (gui[i].equals(here)) {
				gui[i].setVisible(true);
				index = i;
			} else {
				gui[i].setVisible(false);
			}
		}
		gui[index].updateUI();
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void dispose() {
		if (gp.isRunning())
			gp.stopGame();
		onExit();

		super.dispose();
	}
	
	public boolean isKeyInput() {
		return keyInput;
	}
	
	public void enableKeyInput() {
		keyInput = true;
	}
	
	public void disableKeyInput() {
		keyInput = false;
	}
	
	public void setKeyInput(boolean b) {
		keyInput = b;
	}
	
	private class Focus implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			if(!ConfigHandler.getConfig(ConfigHandler.pause_unfocused).equals("true"))return;
			
			if(state != Game.in_game || gp.chat.isOpen())return;
			
			
			if(gp.isOpenGUI() && gp.getCurrentGUI().isCloseOnKey())return;
			
			gp.pauseGame();
		}
		
	}
}
