package com.javapixelgame.game.api.registry;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.AI;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.entity.ProjectileEntity;
import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.api.graphics.ParticleHandler;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.gui.overlay.CarOverlay;
import com.javapixelgame.game.api.gui.overlay.TankOverlay;
import com.javapixelgame.game.api.item.ProjectileAmmo;
import com.javapixelgame.game.api.obstacle.Obstacle;
import com.javapixelgame.game.api.registry.ItemRegistry.ItemID;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.gui.GamePanel;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.resourcehandling.SkinID;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.util.GameUtil;

public final class ObjectiveRegistry {

	private ObjectiveRegistry() {
	}

	public enum NPCID {
		OLD_MAN, SOLDIER, DARK_KNIGHT, GOZILLA, DELOREAN, TANK, DOG
	}

	public static final NPC getNPC(NPCID id, CPoint spawn, World world) {
		switch (id) {
		case DARK_KNIGHT:
			return new DarkKnight(spawn, world);
		case DELOREAN:
			return new Delorean(spawn, world);
		case DOG:
			return new Dog(spawn, world);
		case GOZILLA:
			return new Gozilla(spawn, world);
		case OLD_MAN:
			return new OldMan(spawn, world);
		case SOLDIER:
			return new Soldier(spawn, world);
		case TANK:
			return new Tank(spawn, world);
		}
		return new PlainNPC(1, spawn, new Texture(TextureID.AUTHOR), world, 0, null, 0, false);
	}

	public static final Tree Tree(double sizeInMeter, CPoint spawn, Texture texture, World world) {
		return new Tree(sizeInMeter, spawn, texture, world);
	}

	public static final House House(double sizeInMeter, CPoint spawn, Texture texture, World world) {
		return new House(sizeInMeter, spawn, texture, world);
	}

	public static final Plant Plant(double sizeInMeter, CPoint spawn, World world) {
		return new Plant(sizeInMeter, spawn, world);
	}

	public static final Sign Sign(double sizeInMeter, CPoint spawn, World world) {
		return new Sign(sizeInMeter, spawn, world);
	}

	public static final Flag Flag(double sizeInMeter, CPoint spawn, World world) {
		return new Flag(sizeInMeter, spawn, world);
	}

	public static class Tree extends Obstacle {
		private static final long serialVersionUID = 1L;

		public Tree(double sizeInMeter, CPoint spawn, Texture texture, World world) {
			super(sizeInMeter, spawn, texture, world, "", false);
			setProperties(false, true);
			getRegistry().setRegistryID("tree");
		}

	}

	public static class House extends Obstacle {
		private static final long serialVersionUID = 1L;

		public House(double sizeInMeter, CPoint spawn, Texture texture, World world) {
			super(sizeInMeter, spawn, texture, world, "", false);
			setProperties(false, true);
			getRegistry().setRegistryID("house");
		}

	}

	public static class Plant extends Obstacle implements Variantable {
		private static final long serialVersionUID = 1L;

		public Plant(double sizeInMeter, CPoint spawn, World world) {
			super(sizeInMeter, spawn, new Texture(TextureID.ROSE), world, "", false);
			setProperties(true, false);
			getRegistry().setRegistryID("plant");
		}

		private int variant = 0;
		public static final int ROSE = 0;

		@Override
		public void setVariant(int variant) {
			this.variant = variant;
			switch (this.variant) {
			case ROSE:
				texture = new Texture(TextureID.ROSE);
				update();
				break;

			default:
				texture = new Texture(TextureID.ROSE);
				update();
				break;
			}
		}

		@Override
		public int getVariant() {
			return variant;
		}

		private void update() {
			texture.optimize((int)getBounds().getWidth(), (int)getBounds().getHeight());
			resetSprite();
			updateGraphics();
		}
	}

	public static class Sign extends Obstacle implements Variantable {
		private static final long serialVersionUID = 1L;

		public Sign(double sizeInMeter, CPoint spawn, World world) {
			super(sizeInMeter, spawn, new Texture(TextureID.SIGN_SOCIALISM), world, "", false);
			setProperties(true, false);
			getRegistry().setRegistryID("sign");
		}

		private int variant = 0;
		public static final int SOCIALISM = 0;

		@Override
		public void setVariant(int variant) {
			this.variant = variant;
			switch (this.variant) {
			case SOCIALISM:
				texture = new Texture(TextureID.SIGN_SOCIALISM);
				update();
				break;

			default:
				texture = new Texture(TextureID.SIGN_SOCIALISM);
				update();
				break;
			}
		}

		@Override
		public int getVariant() {
			return variant;
		}

		private void update() {
			texture.optimize((int)getBounds().getWidth(), (int)getBounds().getHeight());
			resetSprite();
			updateGraphics();
		}
	}

	public static class Flag extends Obstacle implements Variantable {
		private static final long serialVersionUID = 1L;

		public Flag(double sizeInMeter, CPoint spawn, World world) {
			super(sizeInMeter, spawn, new Texture(TextureID.FLAG_SIGN_SOCIALISM), world, "", false);
			setProperties(true, false);
			getRegistry().setRegistryID("flag");
		}

		private int variant = 0;
		public static final int RED_KNIGHT = 0;
		public static final int DARK_KNIGHT = 1;
		public static final int SOCIALISM = 2;

		@Override
		public void setVariant(int variant) {
			this.variant = variant;
			switch (this.variant) {
			case RED_KNIGHT:
				texture = new Texture(TextureID.FLAG_SIGN_KNIGHT_RED);
				update();
				break;
			case DARK_KNIGHT:
				texture = new Texture(TextureID.FLAG_SIGN_KNIGHT_DARK);
				update();
				break;
			case SOCIALISM:
				texture = new Texture(TextureID.FLAG_SIGN_SOCIALISM);
				update();
				break;

			default:
				texture = new Texture(TextureID.FLAG_SIGN_SOCIALISM);
				update();
				break;
			}
		}

		@Override
		public int getVariant() {
			return variant;
		}

		private void update() {
			texture.optimize((int)getBounds().getWidth(), (int)getBounds().getHeight());
			resetSprite();
			updateGraphics();
		}
	}

	public static class OldMan extends NPC {

		private static final long serialVersionUID = 1L;

		public OldMan(CPoint spawn, World world) {
			super(1, spawn, new Skin(SkinID.old_man), world, 0.025, "Kung Fu Master", 5000, false);
			addNPCTask(AI.RunRandom());
			getRegistry().setRegistryID("old_man");
			setMiscBarPolicy(SHOW_MISC_BAR_ON_DAMAGE);
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}

	private static class PlainNPC extends NPC {
		private static final long serialVersionUID = 1L;

		public PlainNPC(double sizeInMeter, CPoint spawn, Skin skin, World world, double speedMeterPerTick, String name,
				int lifePoints, boolean pickupItem) {
			super(sizeInMeter, spawn, skin, world, speedMeterPerTick, name, lifePoints, pickupItem);
			Console.error(
					"Attention on instantiating PlainNPC, the constructor spawn CPoint is set on pixel coordinates, not on meter coordinates!!");
		}

		public PlainNPC(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick,
				String name, int lifePoints, boolean pickupItem) {
			super(sizeInMeter, spawn, texture, world, speedMeterPerTick, name, lifePoints, pickupItem);
			Console.error(
					"Attention on instantiating PlainNPC, the constructor spawn CPoint is set on pixel coordinates, not on meter coordinates!!");
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}

	public static class Soldier extends NPC {
		private static final long serialVersionUID = 1L;

		public Soldier(CPoint spawn, World world) {
			super(1.5, spawn, new Skin(SkinID.soldier), world, 0.075, "Soldier", 100, false);
			// TODO Auto-generated constructor stub
			getInventory().add(ItemRegistry.getItem(ItemID.PROJECTILE_AK47));
			getInventory().add(ItemRegistry.getItem(ItemID.ITEM_GOLD_SWORD));
			getInventory().add(ItemRegistry.getItem(ItemID.PROJECTILE_BOW));
			addNPCTask(AI.FollowTracker());
			addNPCTask(AI.AttackNearest());
			addNPCTask(AI.Track("player"));
			setItemHoldHeight(0.6);

			getRegistry().setRegistryID("soldier");
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}

	public static class DarkKnight extends NPC {
		private static final long serialVersionUID = 1L;

		public DarkKnight(CPoint spawn, World world) {
			super(1.5, spawn, new Skin(SkinID.knight_dark), world, 0.075, "Knight", 100, false);
			// TODO Auto-generated constructor stub
			getInventory().add(ItemRegistry.getItem(ItemID.ITEM_STEEL_SWORD));
			addNPCTask(AI.FollowTracker());
			addNPCTask(AI.AttackNearest());
			addNPCTask(AI.Track("player"));
			setItemHoldHeight(0.6);

			getRegistry().setRegistryID("dark_knight");
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}

	public static class Gozilla extends NPC {
		private static final long serialVersionUID = 1L;

		public Gozilla(CPoint spawn, World world) {
			super(5/* 10 */, spawn, new Skin(SkinID.gozilla), world, 0.075, "Gozilla", 10000, false);
			// TODO Auto-generated constructor stub
			setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
			addNPCTask(AI.FollowTracker());
			addNPCTask(AI.AttackNearest());
			addNPCTask(AI.Track("player"));
			setItemHoldHeight(0.6);
			getCollision().setBorders(0.25, 0.1, 0.5, 0.8); // this should be changed according to the texture

			getRegistry().setRegistryID("gozilla");
			setSearchRadius(25);
		}

		@Override
		protected boolean collide(Objective obj) {
			return obj.getCollision().isBlockAll();
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}

	public static class Delorean extends Car {
		private static final long serialVersionUID = 1L;

		public Delorean(CPoint spawn, World world) {
			super(3, spawn, new Skin(SkinID.delorean), world, GameUtil.KMHtoMPS(140) / 20d/* 0.4 */, "Delorean", 1000);
			setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
			getCollision().setBorders(0.33, 0.25, 0.33, 0.5);
			getCollision().update();
			getRegistry().setRegistryID("delorean");
			
			setLeftLightDistance(1.6d);
			setRightLightDistance(1.6d);
			setUpLightDistance(1.5d);
			setDownLightDistance(1d);
		}

		@Override
		protected boolean collide(Objective obj) {
			if (!(obj instanceof Obstacle))
				return false;
			return obj.getCollision().isBlockAll() || !obj.getCollision().isWalkable();
		}

		@Override
		public void onRenderedTick(int tick) {
			
			if (getEngine().isRunning()) {

				float smoke_speed = 0.1f;
				int smoke_time = 4;

				switch (getSkinDirection()) {
				case Skin.UP:
					world.addParticle(
							new CPoint(getPosition().x - getEntityHeight() / 4,
									getPosition().y - (int) (getEntityHeight() * 0.4)),
							Skin.DOWN, smoke_speed, smoke_time, ParticleHandler.dust);

					world.addParticle(
							new CPoint(getPosition().x + getEntityHeight() / 4,
									getPosition().y - (int) (getEntityHeight() * 0.4)),
							Skin.DOWN, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				case Skin.RIGHT:
					world.addParticle(
							new CPoint(getPosition().x - getEntityHeight() / 2,
									getPosition().y - (int) (getEntityHeight() * 0.25)),
							Skin.LEFT, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				case Skin.LEFT:
					world.addParticle(
							new CPoint(getPosition().x + getEntityHeight() / 2,
									getPosition().y - (int) (getEntityHeight() * 0.25)),
							Skin.RIGHT, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				}
			}
		}

	}

	public static class Tank extends Car {
		private static final long serialVersionUID = 1L;

		public Tank(CPoint spawn, World world) {
			super(5, spawn, new Skin(SkinID.tank_sand), world, GameUtil.KMHtoMPS(40) / 20d/* 0.1 */, "Tank", 10000);
			setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
			getCollision().setBorders(0.3, 0.4, 0.4, 0.5);
			getCollision().update();
			getRegistry().setRegistryID("tank");
			getEngine().setMaxRPM(5200);
			
			setLeftLightDistance(2d);
			setRightLightDistance(2d);
			setUpLightDistance(0.75d);
			setDownLightDistance(1.8d);
		}

		@Override
		public void attack() {
			setAttackCooldown(5*20);
			CPoint ammo_outry = new CPoint(0, 0);
			ammo_outry.setLocation(getPosition());
			switch (getSkinDirection()) {
			case Skin.NULL:
				return;
			case Skin.UP:
				ammo_outry.translate(0, (int) (getEntityHeight() * 0.5d));
				break;
			case Skin.DOWN:
				ammo_outry.translate(0, -(int) (getEntityHeight() * 0.4d));
				break;
			case Skin.LEFT:
				ammo_outry.translate(-(int) (getEntityHeight() * 0.7d), (int) (getEntityHeight() * 0.1d));
				break;
			case Skin.RIGHT:
				ammo_outry.translate((int) (getEntityHeight() * 0.7d), (int) (getEntityHeight() * 0.1d));
				break;
			}
			ProjectileEntity e = ((ProjectileAmmo) ItemRegistry.getItem(ItemID.AMMO_TANK)).transfer(ammo_outry,
					getWorld(), getSkinDirection());

			e.setSenderUID(getRegistry());
			e.setLightEmitting(true);
			e.setLuminocity(1);
			e.setLightRange(2);

			getWorld().addObjective(e);

//			dustlength = 5;

			for (int i = 0; i < 10; i++) {
				world.addParticle(new CPoint(ammo_outry.x, ammo_outry.y), getSkinDirection(),
						0.5f + world.getMeterLength(getDrivenVelocity()), 10, ParticleHandler.flame);
				world.addParticle(new CPoint(ammo_outry.x, ammo_outry.y), getSkinDirection(),
						0.3f + world.getMeterLength(getDrivenVelocity()), 20, ParticleHandler.smoke);
			}

		}
		@Override
		public CarOverlay getOverlay() {
			return new TankOverlay(this, (int) (GamePanel.getCompsize()*1.5), GamePanel.getCompsize() * 2);
		}

		@Override
		protected boolean collide(Objective obj) {
			if (!(obj instanceof Obstacle))
				return false;
			return obj.getCollision().isBlockAll() || !obj.getCollision().isWalkable();
		} 

		@Override
		public void onRenderedTick(int tick) {
			if (getEngine().isRunning()) {

				float smoke_speed = 0.1f;
				int smoke_time = 4 + (int) (16d * getEngine().getGas());

				switch (getSkinDirection()) {
				case Skin.UP:
					world.addParticle(
							new CPoint(getPosition().x - (int) (getEntityHeight() * 0.2d),
									getPosition().y - (int) (getEntityHeight() * 0.15)),
							Skin.UP, smoke_speed, smoke_time, ParticleHandler.dust);

					world.addParticle(
							new CPoint(getPosition().x + (int) (getEntityHeight() * 0.2d),
									getPosition().y - (int) (getEntityHeight() * 0.15)),
							Skin.UP, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				case Skin.DOWN:
					world.addParticle(
							new CPoint(getPosition().x - (int) (getEntityHeight() * 0.2d),
									getPosition().y + (int) (getEntityHeight() * 0.3)),
							Skin.UP, smoke_speed, smoke_time, ParticleHandler.dust);

					world.addParticle(
							new CPoint(getPosition().x + (int) (getEntityHeight() * 0.2d),
									getPosition().y + (int) (getEntityHeight() * 0.3)),
							Skin.UP, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				case Skin.RIGHT:
					world.addParticle(
							new CPoint(getPosition().x - (int) (getEntityHeight() * 0.4d),
									getPosition().y + (int) (getEntityHeight() * 0.1)),
							Skin.LEFT, smoke_speed, smoke_time, ParticleHandler.dust);
					world.addParticle(
							new CPoint(getPosition().x - (int) (getEntityHeight() * 0.4d),
									getPosition().y - (int) (getEntityHeight() * 0.1)),
							Skin.LEFT, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				case Skin.LEFT:
					world.addParticle(
							new CPoint(getPosition().x + (int) (getEntityHeight() * 0.4d),
									getPosition().y + (int) (getEntityHeight() * 0.1)),
							Skin.RIGHT, smoke_speed, smoke_time, ParticleHandler.dust);
					world.addParticle(
							new CPoint(getPosition().x + (int) (getEntityHeight() * 0.4d),
									getPosition().y - (int) (getEntityHeight() * 0.1)),
							Skin.RIGHT, smoke_speed, smoke_time, ParticleHandler.dust);
					break;
				}
			}
		}
	}

	public static class Dog extends NPC {
		private static final long serialVersionUID = 1L;

		public Dog(CPoint spawn, World world) {
			super(1, spawn, new Skin(SkinID.dog), world, 0.1, "Dog", 100, false);
			setMiscBarPolicy(SHOW_MISC_BAR_ON_DAMAGE);
			getRegistry().setRegistryID("dog");
			addNPCTask(AI.RunRandom());
			getCollision().setBorders(0.25, 0.25, 0.5, 0.5);
		}

		@Override
		public void onRenderedTick(int tick) {
		}

	}
}
