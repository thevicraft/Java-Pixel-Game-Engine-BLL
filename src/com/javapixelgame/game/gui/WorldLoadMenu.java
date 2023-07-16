package com.javapixelgame.game.gui;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.gui.component.ScrollPane;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.resourcehandling.world.WorldInstance;

@SuppressWarnings("serial")
public class WorldLoadMenu extends AbstractGUI {

	private AbstractGUI pane;

	public WorldLoadMenu(int width, int height) {
		super(width, height);
		setLayout(new FlowLayout(FlowLayout.CENTER));
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
	}

	@Override
	protected void init() {
		int width = getColumnScaledWidth(getWidth(), 3);
		ConfigComponent.ActionButton back = new ConfigComponent.ActionButton("back", width, ar -> {
			Main.game.setState(Game.main_menu);
		}, null);

		ConfigComponent.ActionButton refresh = new ConfigComponent.ActionButton("refresh", width, ar -> {
			update();
		}, null);

		int length = 1000;

		ScrollPane scroll = new ScrollPane(getWidth(), getHeight() - 2 * back.getHeight(), length);

		pane = scroll.getContentPane();

		add(scroll);
		add(refresh);
		add(back);
	}

	public void update() {
		pane.removeAll();
		int cycle = 1;
		int buttonheight = 0;
		for (File file : getAvailableWorlds()) {

			ConfigComponent.ActionButton add = getWorldLoadButton(file);

			pane.addComponent(add, 1, cycle);
			cycle++;
			buttonheight = add.getHeight();

		}

		pane.setSize(pane.getSize().width, buttonheight * cycle);
		pane.setPreferredSize(pane.getSize());

	}

	public File[] getAvailableWorlds() {
		List<File> f = new ArrayList<>();

		Arrays.asList(WorldInstance.getSaveDirectory().listFiles()).forEach(w -> {
			if (WorldInstance.proofWorldSaveValidity(w.getName()))
				f.add(w);
		});
		return f.toArray(new File[0]);
	}

	private int getButtonWidth() {
		return pane.getWidth() / 2;
	}

	private ConfigComponent.ActionButton getWorldLoadButton(File worldFile) {

		ConfigComponent.ActionButton back = new ConfigComponent.ActionButton(worldFile.getName(), getButtonWidth(),
				ar -> {
					if (WorldInstance.proofWorldSaveValidity(worldFile.getName())) {
						GameHandler.loadInstance(worldFile.getName());
						Main.game.gp.updateGUI();
						Main.game.setState(Game.in_game);
					} else {
						((ConfigComponent.ActionButton)ar.getSource()).setEnabled(false);
					}
				}, null);

		return back;
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			update();
		}
		super.setVisible(b);
	}

}
