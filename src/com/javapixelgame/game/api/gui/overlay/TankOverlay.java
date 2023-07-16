package com.javapixelgame.game.api.gui.overlay;

import java.awt.Font;

import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class TankOverlay extends CarOverlay {

	private Button shoot;
	private static final String fire = "FIRE";

	public TankOverlay(Car car, int width, int height) {
		super(car, width, height);
		shoot = new Button(fire, getWidth() - 10);
		
		shoot.setFont(GameFont.getRainyHearts(Font.BOLD, shoot.getFont().getSize2D()));
		
		shoot.addActionListener(a -> {
			car.attack();
		});
		addComponent(getSeparator(getWidth()-10, shoot.getHeight()), 1, 7);
		addComponent(shoot, 1, 8);
	}

	@Override
	public void onWorldTick(int tick) {
		super.onWorldTick(tick);
		if (tick % 5 == 0) {

			shoot.setEnabled(car.getAttackCooldown() == -1);
			if (shoot.isEnabled())
				shoot.setText(">" + fire + "<");
			else
				shoot.setText(fire);
		}
	}

}
