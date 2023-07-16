package com.javapixelgame.game.api.gui.inventory;


import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLayeredPane;

import com.javapixelgame.game.api.tileentity.Inventory;
import com.javapixelgame.game.handling.GameHandler;

public class InventoryArea extends JLayeredPane{
	
	private TakeableItem selectedMouseItem;
	
	private static final long serialVersionUID = 1L;
	
	private InventorySlots inv_slots;
	private Inventory inventory;
	private int slotSize;
	
	private MouseMotionListener m = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {

			InventoryArea.this.getParent().repaint();
			
			if(e.getComponent() == InventoryArea.this) {
				getSelectedMouseItem().setLocation(e.getX()-getSelectedMouseItem().getWidth()/2, e.getY()-getSelectedMouseItem().getHeight()/2);
				return;
			}
			int xoff = 0;
			int yoff = 0;

			// gives the correct position of the mouse over the possible child components: slot, inventoryslots
			if(e.getComponent() instanceof Slot) {
				if(e.getComponent().getParent() == InventoryArea.this) {
					xoff = e.getComponent().getX();
					yoff = e.getComponent().getY();
				} else if(e.getComponent().getParent() == InventoryArea.this.inv_slots) {
					xoff = e.getComponent().getX() + InventoryArea.this.inv_slots.getX();
					yoff = e.getComponent().getY() + InventoryArea.this.inv_slots.getY();
				}
			} else if(e.getComponent() instanceof InventorySlots) {
				xoff = InventoryArea.this.inv_slots.getX();
				yoff = InventoryArea.this.inv_slots.getY();
			}
			
			getSelectedMouseItem().setLocation(xoff + e.getX()-getSelectedMouseItem().getWidth()/2, yoff + e.getY()-getSelectedMouseItem().getHeight()/2);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
	};
	public static final MouseMotionListener forward = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			e.getComponent().getParent().dispatchEvent(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
	};
	
	
	public InventoryArea(int width, int height, int slotSize) {
		setSize(width, height);

		setPreferredSize(getSize());

		setOpaque(false);
		this.slotSize = slotSize;
		selectedMouseItem = new TakeableItem(slotSize, this);
		inventory = GameHandler.getWorld().getPlayer().getInventory();
		inv_slots = new InventorySlots(slotSize *9,this);
		inv_slots.addMouseMotionListener(forward);
		setLayout(null);
		addMouseMotionListener(m);

		add(getSelectedMouseItem(), new Integer(2));
	}
	
	public void close() {
		if(getSelectedMouseItem().getItem() == null)
			return;
		
		if(getInventory().isFilled())
			GameHandler.getWorld().getPlayer().throwItem(getSelectedMouseItem().getItem());
		else
			getInventory().add(getSelectedMouseItem().getItem());
		
		
		getSelectedMouseItem().setItem(null);
	}
	
	public void addInventorySlots(int x, int y) {
		inv_slots.setLocation(x, y);
		add(inv_slots, new Integer(1));
	}
	
	public TakeableItem getSelectedMouseItem() {
		return selectedMouseItem;
	}



	public Inventory getInventory() {
		return inventory;
	}


}
