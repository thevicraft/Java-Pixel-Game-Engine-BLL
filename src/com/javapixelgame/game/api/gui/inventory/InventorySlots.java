package com.javapixelgame.game.api.gui.inventory;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;

import com.javapixelgame.game.gui.AbstractGUI;
import com.javapixelgame.game.handling.GameHandler;

public class InventorySlots extends AbstractGUI{

	private static final long serialVersionUID = 1L;
	private InventoryArea inventoryArea;

	public InventorySlots(int w, InventoryArea area) {
		super(w, 10);
		this.inventoryArea = area;
		
		int width = getWidth() /9;
		
		setComponentAlignment(GridBagConstraints.NONE);
		slots = new Slot[GameHandler.getWorld().getPlayer().getInventory().getMaxSize()];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new Slot(GameHandler.getWorld().getPlayer().getInventory(), getInventoryArea(),width, i);
		}
		
		row = 1;
		column = 1;

		for(int i = 9; i < slots.length; i++) {
			addNext(slots[i]);
		}
		
		addComponent(AbstractGUI.getSeparator(width, width/4), 1, 4);
		
		for(int hotbar = 0; hotbar < 9; hotbar++) {
			addComponent(slots[hotbar], hotbar+1, 5);
		}
		
		setSize(getWidth(), width * slots.length/9 + width/4);
		setPreferredSize(getSize());
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	private int row, column;
	
	private Slot[] slots;

	private void addNext(JComponent c) {
		if (column >= 10) {
			row++;
			column = 1;
		}

		addComponent(c, column, row);

		column++;
	}

	@Override
	protected void init() {}

	public InventoryArea getInventoryArea() {
		return inventoryArea;
	}

}
