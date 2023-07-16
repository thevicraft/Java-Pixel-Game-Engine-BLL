package com.javapixelgame.game.gui.settings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.AbstractGUI;
import com.javapixelgame.game.gui.Game;
import com.javapixelgame.game.gui.SettingsMenu;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class GeneralSettingsGUI extends AbstractGUI {

	private Button restart;

	private JLabel attention;

	public GeneralSettingsGUI(int width, int height) {
		super(width, height);
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
	}

	@Override
	protected void init() {
		int width = getColumnScaledWidth(getWidth(),3);
		Button done = new Button("Done", width);
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.game.setState(Game.main_menu);
			}
		});

//		Slider test = new Slider("test", width);
//		test.setMaximum(1920);
//		test.setValueVariant(Slider.PERCENTAGE_VALUE);
//		test.setValue(50);

		ActionListener ChangeNotifier = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				notifyChange();
			}
		};
		ConfigComponent.SwitchButton c_focus = new ConfigComponent.SwitchButton(ConfigHandler.pause_unfocused, width,
				new String[] { "false", "true" }, null);
		c_focus.setToolTipText("Pauses the game when the window is unfocused.");

		ConfigComponent.SwitchButton c_debug = new ConfigComponent.SwitchButton(ConfigHandler.debug, width,
				new String[] { "false", "true" }, null);
		ConfigComponent.SwitchButton c_fullscreen = new ConfigComponent.SwitchButton(ConfigHandler.fullscreen, width,
				new String[] { "false", "true" }, ChangeNotifier);
		ConfigComponent.ConfigSlider c_width = new ConfigComponent.ConfigSlider(ConfigHandler.width, Game.MIN_WIDTH,
				Main.monitor.width, width, ChangeNotifier);
		c_width.setUnit("px");
		ConfigComponent.ConfigSlider c_height = new ConfigComponent.ConfigSlider(ConfigHandler.height, Game.MIN_WIDTH,
				Main.monitor.height, width, ChangeNotifier);
		c_height.setUnit("px");
		ConfigComponent.ConfigSlider c_fps = new ConfigComponent.ConfigSlider(ConfigHandler.fps, 1, 250, width, null);
		ConfigComponent.ConfigSlider c_gui_scale = new ConfigComponent.ConfigSlider(ConfigHandler.gui_scale, 1, 6,
				width, null);
		c_gui_scale.setToolTipText("Needs restart to affect all game GUI.");

		ConfigComponent.ConfigSlider c_map_scale = new ConfigComponent.ConfigSlider(ConfigHandler.map_scale, 10, 100,
				width, null);
		c_map_scale.setToolTipText("Affects game map zoom. Unit in display px per meter. The higher this value, the laggier the game gets!");

		ConfigComponent.SwitchButton c_minimap = new ConfigComponent.SwitchButton(ConfigHandler.minimap, width,
				new String[] { "false", "true" }, null);
		c_minimap.setToolTipText("Attention: Minimap is work-in-progress and sucks.");
		
		ConfigComponent.SwitchButton c_particles = new ConfigComponent.SwitchButton(ConfigHandler.particles, width,
				new String[] { "false", "true" }, null);

		ConfigComponent.ActionButton c_apply_on_current_size = new ConfigComponent.ActionButton(
				"apply size on state", width, ar -> {
					c_width.setValue(Main.game.getWidth());
					c_height.setValue(Main.game.getHeight());
					c_width.onChange();
					c_height.onChange();
				}, ChangeNotifier);
		c_apply_on_current_size.setToolTipText("Applies the current window size on settings.");
		
		ConfigComponent.ActionButton controls_menu = new ConfigComponent.ActionButton(
				"Controls...", width, ar -> {
					Main.game.settingmenu.setSelectedTab(SettingsMenu.controls);
				}, null);
		
		ConfigComponent.SwitchButton c_animate_gauges = new ConfigComponent.SwitchButton(ConfigHandler.animate_gauges, width,
				new String[] { "false", "true" }, null);
		c_animate_gauges.setToolTipText("Affects tachometer/gauge value animations.");
			

		attention = new JLabel("Settings are applied after restart.");
		attention.setOpaque(false);
		attention.setForeground(Color.red);
		attention.setPreferredSize(done.getPreferredSize());
		attention.setFont(GameFont.getRainyHearts(Font.PLAIN, (float) attention.getPreferredSize().getHeight() * 0.7f));
		attention.setVisible(false);

		restart = new Button("Restart", width);
		restart.addActionListener(a -> {
			Main.restart();
		});
		restart.setVisible(false);

		addComponent(c_focus, 1, 1);
		addComponent(c_debug, 1, 2);
		addComponent(c_fullscreen, 1, 3);
		addComponent(c_gui_scale, 1, 4);
		addComponent(c_minimap, 1, 5);
		addComponent(c_particles, 1, 6);
		addComponent(c_animate_gauges, 1, 7);

		addComponent(c_width, 3, 1);
		addComponent(c_height, 3, 2);
		addComponent(c_apply_on_current_size, 3, 3);
		addComponent(c_fps, 3, 4);
		addComponent(c_map_scale, 3, 5);
		addComponent(controls_menu, 3, 6);

		addComponent(attention, 2, 3);
		addComponent(restart, 2, 4);
		
		addComponent(done, 2, 7);
	}

	public void notifyChange() {
		attention.setVisible(true);
		restart.setVisible(true);
	}
}