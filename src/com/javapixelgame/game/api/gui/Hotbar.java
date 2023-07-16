package com.javapixelgame.game.api.gui;

import javax.swing.JPanel;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.tileentity.Inventory;

@SuppressWarnings("serial")
public class Hotbar extends JPanel implements Tickable {

	private Inventory inventory;
	private ItemHUD[] huds;
	private int border = 5;
	
	public static final int slotDisplay = 9;

	public Hotbar(Inventory inventory, int width, int xcenter, int ycenter) {
		this.inventory = inventory;
		int height = (width - slotDisplay * border) / slotDisplay;
		setSize(width, height);
		setOpaque(false);
		setLayout(null);
		setLocation(xcenter - getWidth() / 2, ycenter - getHeight() / 2);
		huds = new ItemHUD[slotDisplay];
		for (int i = 0; i < huds.length; i++) {
			huds[i] = new ItemHUD(inventory, width > height ? height : width, i);
			huds[i].setLocation((int) (border + width * (i / (double) (slotDisplay))), 0);
			add(huds[i]);
		}
	}

	@Override
	public void onWorldTick(int tick) {
		// TODO Auto-generated method stub
		if (tick % 2 == 0)
			for (int i = 0; i < huds.length; i++) {
				huds[i].setSelected(i == inventory.getCurrentSlot());
				huds[i].repaint();
			}
	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub

	}
}
