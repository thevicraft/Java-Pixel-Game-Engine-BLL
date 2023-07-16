package com.javapixelgame.game.api.gui.overlay;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.javapixelgame.game.api.gui.inventory.InventoryArea;
import com.javapixelgame.game.keyboard.KeyEventHandler;
import com.javapixelgame.game.keyboard.KeyEventHandler.Keys;

@SuppressWarnings("serial")
public class InventoryOverlay extends AbstractOverlay{
	
	private InventoryArea inventoryArea;
	
	public InventoryOverlay(int width, int height) {
		super(width, height);

		addCloseKey(KeyEvent.VK_ESCAPE);
		addCloseKey(KeyEventHandler.getKeyCode(Keys.inventory));
	}

	@Override
	public void onWorldTick(int tick) {}



	@Override
	protected void paintOverlay(Graphics2D g) {}

	@Override
	public void onOpen() {}

	@Override
	public void onClose() {

		inventoryArea.close();
	}

	@Override
	protected void init() {

		int width = (getWidth() -4)/9;
		inventoryArea = new InventoryArea(getWidth()-4, getHeight()-4, width);
		inventoryArea.addInventorySlots(1, 1);
		addComponent(inventoryArea, 1, 1);
	}
}

