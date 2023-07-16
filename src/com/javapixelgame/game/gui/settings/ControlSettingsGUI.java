package com.javapixelgame.game.gui.settings;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.AbstractGUI;
import com.javapixelgame.game.gui.SettingsMenu;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.keyboard.KeyConfig;

public class ControlSettingsGUI extends AbstractGUI{

	public ControlSettingsGUI(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
	private int row, column;

	@Override
	protected void init() {
		// TODO Auto-generated method stub
//		int width = (int) (getWidth() * 0.33);
		int width = getColumnScaledWidth(getWidth(),3);
		
		row = 1;
		column = 1;
		
		KeyConfig.KeyButton[] buttons = KeyConfig.getConfigButtons(width);
		
		for(int i = 0; i < buttons.length; i++) {
			addNext(buttons[i]);
		}
		
		ConfigComponent.ActionButton done = new ConfigComponent.ActionButton(
				"done", width, ar -> {
					Main.game.settingmenu.setSelectedTab(SettingsMenu.general);
				}, null);
		row++;
		column = 2;
		addComponent(done, column, row);
		
	}
	
	private void addNext(JComponent c) {
		if(column >=4) {
			row++;
			column = 1;
		}
		
		addComponent(c, column, row);
	
		column ++;
	}

}
