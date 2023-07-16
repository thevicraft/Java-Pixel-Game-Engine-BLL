package com.javapixelgame.game.api.obstacle;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.Entity;
import com.javapixelgame.game.api.graphics.ParticleHandler;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.resourcehandling.AnimatedTextureID;

public class Fire extends Obstacle implements Tickable {
	private static final long serialVersionUID = 1L;
	public static final int verysmall = 2;
	public static final int small = 5;
	public static final int middle = 15;
	public static final int tall = 30;

	public static final int maxFireDamage = 50;

	private int stage;

	public Fire(CPoint spawn, World world, final int stage) {
		super(1, spawn, new Texture(AnimatedTextureID.fire), world, "hot fire", false);
		// TODO Auto-generated constructor stub
		setProperties(true, false);
		this.stage = stage;
		getRegistry().setRegistryID("fire");
		getCollision().setBorders(0.2, 0.5, 0.6, 0.5);
		getCollision().update();
		setLightEmitting(true);
		updateLight();

		resize(3d * stage / (double) tall);
	}

	@Override
	public void onWorldTick(int tick) {


		if (getWorld().isRendered(this))
			makeParticles();

		getWorld().getColliders(getCollision()).forEach(dam -> {
			if (dam instanceof Entity && tick == 1) {
				((Entity) dam).damage(maxFireDamage * stage / tall, getRegistry());
			}
		});

	}

	@Override
	public void onRandomTick() {

		if (stage < tall) {
			stage += 3;
			updateLight();
			resize(3d * stage / (double) tall);
		}


	}

	public void updateLight() {
		setLightRange(sizeInMeter * (stage / (double) tall) * 10);
		setLuminocity(stage / (float) tall);
	}

	public void resize(double sizeInMeter) {
		this.sizeInMeter = sizeInMeter;
		this.height = world.getPixelLength(sizeInMeter);

		this.width = texture.getWidthOnRatio(height);

		xBorder = (int) (width * borderFactor);
		yBorder = (int) (height * borderFactor);

		setPosition(getPosition());

		collision.update();

		this.texture.optimize(width, height);

		resetSprite();
		updateGraphics();
	}

	private void makeParticles() {

		world.addParticle(new CPoint(getPosition().x, getPosition().y + getObstacleHeight() / 2), Skin.UP, 0.1,
				stage * 3, ParticleHandler.smoke);
	}

}
