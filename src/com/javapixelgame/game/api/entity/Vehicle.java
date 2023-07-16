package com.javapixelgame.game.api.entity;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.obstacle.Obstacle;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.world.World;

public abstract class Vehicle extends NPC implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private NPC rider;
	private boolean ridden = false;

	public Vehicle(double sizeInMeter, CPoint spawn, Skin skin, World world, double speedMeterPerTick, String name,
			int lifePoints) {
		super(sizeInMeter, spawn, skin, world, speedMeterPerTick, name, lifePoints, false);

	}

	public Vehicle(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick,
			String name, int lifePoints) {
		super(sizeInMeter, spawn, texture, world, speedMeterPerTick, name, lifePoints,false);

	}

	// according to current functionalities the speed of the rider NPC should be adjusted and the position of the rider changed to this one here
	@Override
	public final void onEntityTick(int tick) {
		if(isRidden()) {
			getRider().teleport(getPosition());
			onRidingTick(tick);
		}
		onTick(tick);
	}
	
	protected void onTick(int tick) {
		
	}
	
	@Override
	protected final boolean compareCollision(Objective obj) {
		return (isRidden() ? obj != getRider() : true) && obj instanceof Obstacle;
	}
	
	public abstract void onRidingTick(int tick);
	
	public abstract void onStartRiding(NPC rider);

	public abstract void onStopRiding(NPC rider);
	
	private double speedStore = 0;
	
	public void setRider(NPC rider) {
		eject();
		this.rider = rider;
		this.rider.setRiding(true);
		this.rider.setVehicle(this);
		this.speedStore = this.rider.getSpeedMeterPerTick();
		this.rider.setSpeedMeterPerTick(this.getSpeedMeterPerTick());
		ridden = true;
		onStartRiding(this.rider);
	}

	public void eject() {
		if (ridden && rider != null) {
			onStopRiding(rider);
			rider.setRiding(false);
			rider.setVehicle(null);
			this.rider.setSpeedMeterPerTick(speedStore);
			rider = null;
			ridden = false;
		}
	}
	
	@Override
	public void die(UID killer) {
		eject();
		setDead(true);
		world.removeObjective(this);
		throwInventory();
	}

	@Override
	public void die() {
		eject();
		setDead(true);
		world.removeObjective(this);
	}

	public NPC getRider() {
		return rider;
	}

	public boolean isRidden() {
		return ridden;
	}

}
