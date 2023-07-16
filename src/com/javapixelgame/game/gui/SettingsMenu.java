package com.javapixelgame.game.gui;

import com.javapixelgame.game.gui.component.TabbedPane;
import com.javapixelgame.game.gui.settings.ControlSettingsGUI;
import com.javapixelgame.game.gui.settings.GeneralSettingsGUI;

@SuppressWarnings("serial")
public class SettingsMenu extends TabbedPane{
	
	public static final int general = 0;
	public static final int controls = 1;
	
	public SettingsMenu(int width, int height, int tabs) {
		super(width, height, tabs);
		// TODO Auto-generated constructor stub
		setTab(new GeneralSettingsGUI(width, height), general);
		setTab(new ControlSettingsGUI(width, height), controls);
		
		setSelectedTab(general);
	}

}
