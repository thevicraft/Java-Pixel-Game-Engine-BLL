package com.javapixelgame.game.api.entity.car;

import com.javapixelgame.game.api.BackgroundWorker;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.entity.Vehicle;
import com.javapixelgame.game.api.graphics.Light;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.gui.overlay.CarOverlay;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.gui.GamePanel;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.log.Console;

public abstract class Car extends Vehicle {

	private Engine engine;

	public static final int default_max_rpm = 8000;
	private double maxspeedmeterpertick;

	private float maxLuminocity;
	public static final double default_light_range = 15;

	public static final int LIGHTS_OFF = 0;
	public static final int LIGHTS_NORMAL = 1;
	public static final int LIGHTS_FAR = 2;

	private int lightState = LIGHTS_OFF;

	public Car(double sizeInMeter, CPoint spawn, Skin skin, World world, double speedMeterPerTick, String name,
			int lifePoints) {
		super(sizeInMeter, spawn, skin, world, speedMeterPerTick, name, lifePoints);
		setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
		engine = new Engine(default_max_rpm);
		maxspeedmeterpertick = speedMeterPerTick;

		setBloodOnDamage(false);
		setLightProperty(Light.DIRECTIONAL);
	}

	public Car(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick, String name,
			int lifePoints) {
		super(sizeInMeter, spawn, texture, world, speedMeterPerTick, name, lifePoints);
		setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
		engine = new Engine(default_max_rpm);
		maxspeedmeterpertick = speedMeterPerTick;

		setBloodOnDamage(false);
		setLightProperty(Light.DIRECTIONAL);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void onRidingTick(int tick) {

		if (!getRider().getRegistry().isSameAs(GameHandler.getWorld().getPlayer().getRegistry())
				|| GamePanel.get().isOpenGUI())
			return;
		GamePanel.get().openGUI(getOverlay());
	}

//	@Override
//	public void onRenderedTick(int tick) {
//		// TODO Auto-generated method stub
//		
//	}

	private boolean blockAllStore;
	private boolean walkableStore;
	private boolean invulnerableStore;

	@Override
	public void onStartRiding(NPC rider) {
		rider.setVisible(false);
		blockAllStore = rider.getCollision().isBlockAll();
		walkableStore = rider.getCollision().isWalkable();
		rider.getCollision().setBlockAll(false);
		rider.getCollision().setWalkable(true);

		invulnerableStore = rider.isInvulnerable();
		rider.setInvulnerable(true);

		if (!rider.getRegistry().isSameAs(GameHandler.getWorld().getPlayer().getRegistry()))
			return;

		BackgroundWorker.invokeLater(() -> {
			GamePanel.get().openGUI(getOverlay());

		});

//			GamePanel.get().openGUI(getOverlay());

	}

	public CarOverlay getOverlay() {
		return new CarOverlay(this, (int) (GamePanel.getCompsize() * 1.5), GamePanel.getCompsize() * 2);
	}

	@Override
	public void onStopRiding(NPC rider) {
		rider.setVisible(true);
		rider.getCollision().setBlockAll(blockAllStore);
		rider.getCollision().setWalkable(walkableStore);
		rider.setInvulnerable(invulnerableStore);
		if (!rider.getRegistry().isSameAs(GameHandler.getWorld().getPlayer().getRegistry()))
			return;
		GamePanel.get().closeGUI();
	}

	private int velocity = 0;
	private CPoint last = new CPoint(0, 0);

	protected void onTick(int tick) {
		setSpeedMeterPerTick(getEngine().getSpeedMultiplier() * getMaxSpeedMeterPerTick());

		switch (getSkinDirection()) {
		case Skin.UP:
			setxVelocity(0);
			setyVelocity(getPixelSpeedPerTick());
			break;
		case Skin.DOWN:
			setxVelocity(0);
			setyVelocity(-getPixelSpeedPerTick());
			break;
		case Skin.RIGHT:
			setxVelocity(getPixelSpeedPerTick());
			setyVelocity(0);
			break;
		case Skin.LEFT:
			setxVelocity(-getPixelSpeedPerTick());
			setyVelocity(0);
			break;
		}

		velocity = (int) getPosition().distance(last);

		last = getPosition();
		
		if(turnCooldown > -1)
			turnCooldown -= 1;

	}

	public int getDrivenVelocity() {
		return velocity;
	}

	@Override
	protected void setSkin() {
		if (isMoving())
			toDraw = skin.getSkinImage(getSkinDirection());

	}
	
	private int turnCooldown = -1;

	public void setSkinDirection(int direction) {
		if (!isMoving() || turnCooldown > -1)
			return;

		turnCooldown = 4;
//		if(Skin.getOppositeDirection(direction) == getSkinDirection())return;
		skinDirection = direction;
	}

	public Engine getEngine() {
		return engine;
	}

	public double getMaxSpeedMeterPerTick() {
		return maxspeedmeterpertick;
	}

	public float getMaxLuminocity() {
		return maxLuminocity;
	}

	public void setMaxLuminocity(float maxLuminocity) {
		this.maxLuminocity = maxLuminocity;
	}

	public int getLightState() {
		return lightState;
	}

	public void setLightState(int lightState) {
		this.lightState = lightState;
		switch (lightState) {
		case LIGHTS_OFF:
			setLightEmitting(false);
			break;
		case LIGHTS_NORMAL:
			setLightRange(default_light_range);
			setLuminocity(0.75f);
			setLightEmitting(true);
			break;
		case LIGHTS_FAR:
			setLightRange(default_light_range * 1.5d);
			setLuminocity(0.9f);
			setLightEmitting(true);
			break;
		}
	}

}
