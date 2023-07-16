package com.javapixelgame.game.api.gui.inventory;

import com.javapixelgame.game.api.gui.ItemHUD;
import com.javapixelgame.game.api.item.Item;

public class TakeableItem extends ItemHUD{
	
	private static final long serialVersionUID = 1L;
	private Item item;
	private InventoryArea inventoryArea;
	public TakeableItem(int size, InventoryArea area) {
		super(null, size, 0);
		this.inventoryArea = area;
		setBackgroundVisible(false);
		setOpaque(false);
	}
	
	
	protected Item getCorrespondingItem() {
		return getItem();
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
		setVisible(item != null);
		
	}
	public InventoryArea getInventoryArea() {
		return inventoryArea;
	}
}
