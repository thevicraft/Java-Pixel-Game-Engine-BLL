package com.javapixelgame.game.api.graphics;

import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.resourcehandling.TextureID;

public class ParticleHandler {
	public static final int flame = 0;
	public static final int smoke = 1;
	public static final int fire = 2;
	public static final int dust = 3;
	public static final int blood = 4;
	
	private Texture[] t = {
			new Texture(TextureID.P_FLAME),
			new Texture(TextureID.P_SMOKE),
			new Texture(TextureID.P_FLAME),
			new Texture(TextureID.P_DUST),
			new Texture(TextureID.P_BLOOD),
	};

	private World world;

	public ParticleHandler(World world) {
		this.world = world;
	}

	public Particle getType(CPoint spawn, int direction, double spreadInMeter, int length, final int type) {
		int spread = world.getPixelLength(spreadInMeter);
		switch (type) {
		case flame:
			return new Particle(t[type], world.getPixelLength(0.25), (short) length, spawn,
					spread, direction);
		case smoke:
			return new Particle(t[type], world.getPixelLength(0.25), (short) length, spawn,
					spread, direction);
		case fire:
			return new Particle(t[type], world.getPixelLength(0.35), (short) length, spawn,
					spread, direction);
		case dust:
			return new Particle(t[type], world.getPixelLength(0.1), (short) length, spawn,
					spread, direction);
		case blood:
			return new Particle(t[type], world.getPixelLength(0.1), (short) length, spawn,
					spread, direction);
		}
		return null;
	}
}
