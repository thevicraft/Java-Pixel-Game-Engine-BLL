package com.javapixelgame.game.api.tileentity;

import java.io.Serializable;
import java.util.Arrays;

import com.javapixelgame.game.api.item.Item;

public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;
//	private List<int> items = new ArrayList<>();
	private Item[] items;
	private int maxSize;
	private int cycle = -1;

	public Inventory(int maxSize) {
		this.maxSize = maxSize;
		items = new Item[maxSize];
	}

	public Inventory(Item[] items, int maxSize) {
//		this.items = Arrays.asList(items);
		this.items = items;
		this.maxSize = maxSize >= items.length ? maxSize : items.length;
	}

	public Inventory(Item[] items) {
		this.maxSize = items.length;
//		this.items = Arrays.asList(items);
		this.items = items;
	}

	public void add(Item item) {
		if (!isFilled()) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null) {
					items[i] = item;
					break;
				}
			}
		}
	}

	public boolean isFilled() {
		return size() >= getMaxSize();
	}

	public Item nextItem() {
		if (getCurrentItem() != null)
			getCurrentItem().onUnselection();
		if (size() > 0) {
			cycle++;
			boolean found = false;
			for (int i = cycle; i < 9; i++) {
				if (items[i] != null) {
					cycle = i;
					found = true;
					break;
				}
			}
			if (!found) {
				for (int i = 0; i < cycle; i++) {
					if (items[i] != null) {
						cycle = i;
						break;
					}
				}
			}
//			items[cycle].onSelection();
			return items[cycle];

		}
		return null;
	}

	/**
	 * <b>If IndexOutOfBoundsException is thrown, the first item is returned!
	 */
	public Item getItem(int index) {
		return items[/* cycle == -1 ? 0 : */index];

	}

	/**
	 * <b>If IndexOutOfBoundsException is thrown, nothing happens
	 */
	public void setItem(int index, Item item) {
		if (index == cycle && items[index] != null)
			items[index].onUnselection();
		items[index] = item;

	}

	public void removeCurrentItem() {
		if (getCurrentItem() != null)
			getCurrentItem().onUnselection();
		items[cycle == -1 ? 0 : cycle] = null;
	}

	public Item getCurrentItem() {
		return items[cycle == -1 ? 0 : cycle];
	}

	public void clear() {
		if (getCurrentItem() != null)
			getCurrentItem().onUnselection();
		cycle = -1;
		items = new Item[maxSize];
	}

	public int getCurrentSlot() {
		return cycle == -1 ? 0 : cycle;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int size) {
		this.maxSize = size;
		items = Arrays.copyOf(items, size);
	}

	public int size() {
		int size = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null)
				size++;
		}
		return size;
	}

}
