package com.javapixelgame.game.gui.component;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import com.javapixelgame.game.gui.AbstractGUI;

@SuppressWarnings("serial")
public abstract class TabbedPane extends AbstractGUI {

	private AbstractGUI[] tab;

	private int selectedTab = 0;

	public TabbedPane(int width, int height, int tabs) {
		super(width, height);
		setLayout(new FlowLayout());
		tab = new AbstractGUI[tabs];
	}

	public void setTab(AbstractGUI component, int index) {
		if (tab[index] != null)
			return;

		tab[index] = component;
		add(tab[index]);
		updateTabs();
	}

	public void updateTabs() {
		if (tab.length == 0)
			return;
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] != null) {
				if (i == getSelectedTab()) {
					tab[i].setVisible(true);
					tab[i].updateUI();
				} else
					tab[i].setVisible(false);
			}
		}
		updateUI();
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
		updateTabs();
	}

}
