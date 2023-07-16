package com.javapixelgame.game.api.gui.inventory;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPopupMenu;
import javax.swing.JToolTip;

import com.javapixelgame.game.api.gui.ItemHUD;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.tileentity.Inventory;
import com.javapixelgame.game.gui.component.ConfigComponent.ToolTip;
import com.javapixelgame.game.log.Console;

public class Slot extends ItemHUD {
	

	private static final long serialVersionUID = 1L;
	private MouseListener focus = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {
			setSelected(false);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setSelected(true);
			Slot.this.updateToolTip();
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if(e.getButton() == MouseEvent.BUTTON3)
				return;

			if(getInventory().getItem(getSlotNumber()) != null && getInventoryArea().getSelectedMouseItem().getItem() != null) {
				Item temp = getInventory().getItem(getSlotNumber());
				getInventory().setItem(getSlotNumber(), getInventoryArea().getSelectedMouseItem().getItem());
				getInventoryArea().getSelectedMouseItem().setItem(temp);
				return;
			}
			
			
			if(getInventoryArea().getSelectedMouseItem().getItem() == null) {
				// takes the item from the slot and places it in the placeholder
				getInventoryArea().getSelectedMouseItem().setItem(getCorrespondingItem());
				getInventory().setItem(getSlotNumber(), null);
				return;
			} 
			if(getInventory().getItem(getSlotNumber()) == null){
				// takes the item from the placeholder and places it in the slot
				getInventory().setItem(getSlotNumber(), getInventoryArea().getSelectedMouseItem().getItem());
				getInventoryArea().getSelectedMouseItem().setItem(null);

				return;
			}
			updateToolTip();
		}
	};
	private InventoryArea inventoryArea;

	public Slot(Inventory inventory, InventoryArea area, int size, int slot) {
		super(inventory, size, slot);
		this.inventoryArea = area;
		setSelectedBorderColor(Color.LIGHT_GRAY);
		addMouseListener(focus);
		if (area == null)
			return;
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Slot.this.inventoryArea.dispatchEvent(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {}
		});
	}
	
	@Override
	public JToolTip createToolTip() {
		return new ItemToolTip(this, Color.DARK_GRAY, Color.cyan, getHeight()/2);
	}
	
	private void updateToolTip() {
		if(getInventory().getItem(getSlotNumber()) == null) {
			setToolTipText("");
			return;
		}
		setToolTipText(getInventory().getItem(getSlotNumber()).getDisplayName());
	}


	public InventoryArea getInventoryArea() {
		return inventoryArea;
	}

}
