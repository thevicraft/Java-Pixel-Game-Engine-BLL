package com.javapixelgame.game.handling;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.gui.overlay.CarOverlay;
import com.javapixelgame.game.gui.GamePanel;
import com.javapixelgame.game.keyboard.KeyEventHandler;
import com.javapixelgame.game.keyboard.KeyEventHandler.Keys;

public class KeyActionHandler implements Tickable {

	public KeyActionHandler() {

	}

	private int xvelocity = 0;
	private int xvelocity2 = 0;
	private int yvelocity = 0;
	private int yvelocity2 = 0;

	@Override
	public void onWorldTick(int tick) {

		if (!Main.game.isKeyInput()) {
			if (!GameHandler.getWorld().getPlayer().isOnPath()) {
				GameHandler.getWorld().getPlayer().setxVelocity(0);
				GameHandler.getWorld().getPlayer().setyVelocity(0);
			}
			return;
		}

		int speed = GameHandler.getWorld().getPlayer().getPixelSpeedPerTick();
		if (KeyEventHandler.isPressed(Keys.down)) {

			if (GameHandler.getWorld().getPlayer().isRiding()
					&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car && GamePanel.get().getCurrentGUI() instanceof CarOverlay)
				CarOverlay.parse(GamePanel.get().getCurrentGUI()).applyGas(-0.02);
			
			
			yvelocity = -speed;
		} else {
			yvelocity = 0;
		}
		if (KeyEventHandler.isPressed(Keys.up)) {
			
			if (GameHandler.getWorld().getPlayer().isRiding()
					&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car && GamePanel.get().getCurrentGUI() instanceof CarOverlay)
				CarOverlay.parse(GamePanel.get().getCurrentGUI()).applyGas(0.02);
			
			yvelocity2 = speed;
		} else {
			yvelocity2 = 0;
		}

		if (KeyEventHandler.isPressed(Keys.left)) {
			xvelocity = -speed;
			if (GameHandler.getWorld().getPlayer().isRiding()
					&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car)
				((Car) GameHandler.getWorld().getPlayer().getVehicle()).setSkinDirection(
						Skin.turnAntiClockwise(GameHandler.getWorld().getPlayer().getVehicle().getSkinDirection()));
		} else {
			xvelocity = 0;
		}

		if (KeyEventHandler.isPressed(Keys.right)) {
			if (GameHandler.getWorld().getPlayer().isRiding()
					&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car)
				((Car) GameHandler.getWorld().getPlayer().getVehicle()).setSkinDirection(
						Skin.turnClockwise(GameHandler.getWorld().getPlayer().getVehicle().getSkinDirection()));
			xvelocity2 = speed;
		} else {
			xvelocity2 = 0;
		}

		if (KeyEventHandler.isPressed(Keys.attack)) {
			GameHandler.getWorld().getPlayer().attack();
		}

		if (KeyEventHandler.isTyped(Keys.swap_item)) {
			GameHandler.getWorld().getPlayer().getInventory().nextItem();
		}

		if (KeyEventHandler.isTyped(Keys.throw_item)) {
			GameHandler.getWorld().getPlayer().throwMainHandItem();
		}

		if (KeyEventHandler.isTyped(Keys.minimap_zoom_in)
				&& ConfigHandler.getConfig(ConfigHandler.minimap).equals("true")) {
			Main.game.gp.minimap.zoomIn(2);
		}

		if (KeyEventHandler.isTyped(Keys.minimap_zoom_out)
				&& ConfigHandler.getConfig(ConfigHandler.minimap).equals("true")) {
			Main.game.gp.minimap.zoomOut(2);
		}

		if (KeyEventHandler.isTyped(Keys.open_chat)) {
			if (!Main.game.gp.chat.isOpen())
				Main.game.gp.chat.open();
		}

		if (KeyEventHandler.isTyped(Keys.interact)) {
			GameHandler.getWorld().getPlayer().interact();
		}

		if (KeyEventHandler.isTyped(Keys.inventory)) {
			GamePanel.get().openInventory();
		}

		int xv = xvelocity + xvelocity2;
		int yv = yvelocity + yvelocity2;
		if ((xv != 0) && (yv != 0)) {
			int dif = (int) Math.sqrt(Math.abs(2 * xv));
			if (xv > 0) {
				xv -= dif;
			} else {
				xv += dif;
			}
			if (yv > 0) {
				yv -= dif;
			} else {
				yv += dif;
			}
		}
		if (GameHandler.getWorld().getPlayer().isOnPath())
			return;
		if (GameHandler.getWorld().getPlayer().isRiding()
				&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car)
			return;
		GameHandler.getWorld().getPlayer().setxVelocity(xv);
		GameHandler.getWorld().getPlayer().setyVelocity(yv);

//		}
	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub

	}

}
