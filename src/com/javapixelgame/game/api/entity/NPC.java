package com.javapixelgame.game.api.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.javapixelgame.game.GameConstants;
import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.api.graphics.Particle;
import com.javapixelgame.game.api.graphics.ParticleHandler;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.gui.ChatMessage;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.item.Projectile;
import com.javapixelgame.game.api.pathfinding.Node;
import com.javapixelgame.game.api.pathfinding.PathFinder;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.tileentity.Inventory;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.handling.GameHandler;

public abstract class NPC extends Entity {
	private static final long serialVersionUID = 4235749069527974448L;
	private List<NPCTask> tasks = new ArrayList<>();
	private int searchRadius = 10;
	public Entity tracker = null;
	private boolean ai = true;

	protected Inventory inventory = new Inventory(GameConstants.NPC_DEFAULT_INVENTORY_SIZE);
	private boolean pickupItem;
	private double itemHoldHeight = 0.75;
	private int attackAnimLength = 6;
	public boolean itemSwing = false;
	private boolean bloodOnDamage = true;

	public static final int SHOW_MISC_BAR_ON_DAMAGE = 0;
	public static final int SHOW_MISC_BAR_NEVER = 1;
	public static final int SHOW_MISC_BAR_ALWAYS = 2;

	private int miscBarPolicy = SHOW_MISC_BAR_ON_DAMAGE;

	private boolean riding = false;
	private Vehicle vehicle = null;
	private PathFinder pathFinder;

	private boolean onPath = false;

	public NPC(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick, String name,
			int lifePoints, boolean pickupItem) {
		super(sizeInMeter, spawn, texture, world, speedMeterPerTick, name, lifePoints);
		// TODO Auto-generated constructor stub
		this.pickupItem = pickupItem;
		updatePathFinder();

		getCollision().setBorders(0.25, 0.5, 0.5, 0.5);
		getCollision().update();
	}

	public NPC(double sizeInMeter, CPoint spawn, Skin skin, World world, double speedMeterPerTick, String name,
			int lifePoints, boolean pickupItem) {
		super(sizeInMeter, spawn, skin, world, speedMeterPerTick, name, lifePoints);
		// TODO Auto-generated constructor stub
		this.pickupItem = pickupItem;
		updatePathFinder();

		getCollision().setBorders(0.25, 0.5, 0.5, 0.5);
		getCollision().update();
	}

	public void updatePathFinder() {
		pathFinder = new PathFinder(this);
	}

	@Override
	public void constructEntity() {
	}

	@Override
	public void onEntityTick(int tick) {

	}

	@Override
	public final void onWorldTick(int tick) {
		updateCollidingState();
		applyMovement();
		if (tick % 3 == 0)
			pickItem();
		setHandItem();
		if (!isRiding() && (hasSkin) && (tick % 2 == 0)) {
			if (isRendered())
				setSkin();
			if (((xVelocity != 0) || (yVelocity != 0)) && (hasItem)) {
				state = "running";
				if (itemMovement != 5)
					itemMovement = 5;
				else
					itemMovement = 0;
			} else if ((xVelocity != 0) || (yVelocity != 0)) {
				state = "running";
			} else {
				state = "idle";
			}

		}

		if (getAttackCooldown() > -1) {
			setAttackCooldown(getAttackCooldown() - 1);
		}

		if (hasItem && getHandItem().shouldAnimateSwing)
			itemSwingAnimation(itemSwing);

		if (hasItem && (getHandItem() instanceof Tickable)) {
			((Tickable) getHandItem()).onWorldTick(tick);
		}

//		onEntityTick(tick);

		if (isAi()) {
			if (!isDead()) {
				if (tasks.size() > 0) {
					tasks.forEach(t -> {
						t.onTick(tick);
					});
				}
				if (tasks.size() > 0 && tick == 1) {
					tasks.forEach(t -> {
						t.onSecond();
					});
				}
			}

			onPathFindAI();

		}

		onEntityTick(tick);

		if (isRendered()) {
			resetSprite();
			updateGraphics();
			onRenderedTick(tick);
		}
	}

	@Override
	public void applyMovement() {

		if (isRiding() && !(getVehicle() instanceof Car)) {
			getVehicle().setxVelocity(getxVelocity());
			getVehicle().setyVelocity(getyVelocity());
		} else {

			super.applyMovement();

		}
	}

	@Override
	public void updateGraphics() {
		if (getSkinDirection() == Skin.UP) {
			drawItem();
			drawSkin();
		} else {
			drawSkin();
			drawItem();
		}
	}

	private Stack<Node> path = new Stack<>();

	private Node currentNode;

	private void onPathFindAI() {
		if (!isOnPath() || path.empty())
			return;

		Point i = getWorld().getModel().getTilePosition(getWorld().getCoords().getAbsolutePoint(getPosition()),
				Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)));

		if (i.x == currentNode.col && i.y == currentNode.row) {

			currentNode = path.pop();
			if (path.empty()) {
				onPath = false;
				stopWalking();
				return;
			}
		} else {
			Point gotothis = getWorld().getModel().getMapPosition(currentNode.col, currentNode.row,
					Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)));
			walkStraight(getWorld().getCoords().getRelativePoint(gotothis));

		}
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
			test();
	}

	public void pathfind(CPoint pos) {
		Point goal = getWorld().getModel().getTilePosition(getWorld().getCoords().getAbsolutePoint(pos),
				Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)));
		Point start = getWorld().getModel().getTilePosition(getWorld().getCoords().getAbsolutePoint(getPosition()),
				Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)));
		getPathFinder().resetNodes();
		getPathFinder().setNodes(start.x, start.y, goal.x, goal.y, this);
		onPath = getPathFinder().search();
		path = new Stack<>();
		path.addAll(getPathFinder().pathList);
		if (path.empty())
			return;
		Collections.reverse(path);
		currentNode = path.pop();
	}

	private void test() {
		Stack<Node> path = new Stack<>();
		path.addAll(this.path);
		int mapmeter = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale));

		for (Node node : path) {

			Point gotothis = getWorld().getModel().getMapPosition(node.col, node.row,
					Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)));
			CPoint p = getWorld().getCoords().getRelativePoint(gotothis);
			GameHandler.getWorld().addParticle(new CPoint(p.x + mapmeter / 2, p.y), Skin.UP, 0, 1,
					ParticleHandler.smoke);
		}

		Point gotothis = getWorld().getModel().getMapPosition(currentNode.col, currentNode.row, mapmeter);
		CPoint p = getWorld().getCoords().getRelativePoint(gotothis);
		GameHandler.getWorld().addParticle(new CPoint(p.x + mapmeter / 2, p.y), Skin.UP, 0, 1, ParticleHandler.fire);

	}

	public void stopPathfind() {
		onPath = false;
		stopWalking();
	}

	public void walkTowards(CPoint p) {
		double rangeFromTracker = p.distance(getPosition());
		double alpha = Math.toDegrees(Math.acos((p.x - getPosition().x) / rangeFromTracker));
		int xspeed = (int) (Math.cos(Math.toRadians(alpha)) * getPixelSpeedPerTick());
		int yspeed = (int) (Math.cos(Math.toRadians(90 - alpha)) * getPixelSpeedPerTick());
		setxVelocity(xspeed);
		setyVelocity(getPosition().y > p.y ? -yspeed : yspeed);
	}

	public void walkStraight(CPoint p) {
		int pxLength = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale));

		Point dir = getWorld().getModel().getTilePosition(getWorld().getCoords().getAbsolutePoint(p), pxLength);
		Point i = getWorld().getModel().getTilePosition(getWorld().getCoords().getAbsolutePoint(getPosition()),
				pxLength);

		if ((dir.x > i.x)) {
			setxVelocity(getPixelSpeedPerTick());
			setyVelocity(0);
			return;
		}
		if (dir.x < i.x) {
			setxVelocity(-getPixelSpeedPerTick());
			setyVelocity(0);
			return;
		}

		if (dir.y > i.y) {
			setyVelocity(-getPixelSpeedPerTick());
			setxVelocity(0);
			return;
		}
		if (dir.y < i.y) {
			setyVelocity(getPixelSpeedPerTick());
			setxVelocity(0);
			return;
		}

	}

	public void stopWalking() {
		setxVelocity(0);
		setyVelocity(0);
	}

	public void addNPCTask(NPCTask task) {
		tasks.add(task);
		task.onInit(this);
	}

	public void removeNPCTask(NPCTask task) {
		tasks.remove(task);
	}

	@Override
	public void onRandomTick() {
		if (!isDead() && isAi()) {
			if (tasks.size() > 0) {
				tasks.forEach(t -> {
					t.onRandomTick();
				});
			}
		}
	}

	public void throwMainHandItem() {
		if (getInventory().getCurrentItem() != null) {
			world.addObjective(
					new ItemEntity(getInventory().getCurrentItem(), getPosition(), world, getSpeedMeterPerTick() * 2,
							ItemEntity.defaultDuration, skinDirection, ItemEntity.defaultPickupDelay));
			getInventory().removeCurrentItem();
		}
	}

	public void throwItem(Item item) {
		if (item != null) {
			world.addObjective(new ItemEntity(item, getPosition(), world, getSpeedMeterPerTick() * 2,
					ItemEntity.defaultDuration, skinDirection, ItemEntity.defaultPickupDelay));
		}
	}

	public void throwInventory() {
		Random rand = new Random();
		for (int i = 0; i <= getInventory().getMaxSize(); i++) {
			getInventory().nextItem();
			if (getInventory().getCurrentItem() != null) {
				world.addObjective(new ItemEntity(getInventory().getCurrentItem(), getPosition(), world,
						rand.nextDouble() * getSpeedMeterPerTick() * 2, ItemEntity.defaultDuration, rand.nextInt(4),
						ItemEntity.defaultPickupDelay));
				getInventory().removeCurrentItem();
			}
		}
	}

	public void pickItem() {
		if (pickupItem) {
			if (!getInventory().isFilled()) {
				for (Objective e : world.getColliders(getCollision())) {
					if (e.getRegistry().getRegistryID().equals("item") && ((ItemEntity) e).canPickup()) {
						getInventory().add(((ItemEntity) e).transfer());
						break;
					}
				}
			}
		}
	}

	private void setHandItem() {
		if (getInventory().getCurrentItem() != null) {
			if (!getInventory().getCurrentItem().isApplied())
				getInventory().getCurrentItem().apply(this);
			hasItem = true;
		} else {
			hasItem = false;
		}
	}

	private String nametag = "";
	// attack cool down: -> + (getAttackCooldown() == -1 ? " \u2694" : "") <-
	public static final char heart = '\u2764';

	protected void drawSkin() {
		if (getMiscBarPolicy() == SHOW_MISC_BAR_ALWAYS
				|| (getMiscBarPolicy() == SHOW_MISC_BAR_ON_DAMAGE && getHealthPoints() < getMaxHealthPoints())) {
			graphics.setColor(Color.LIGHT_GRAY);
			int xBar = (int) (getBounds().width * 0.5);
			int yBar = (int) (xBar * 0.1);
			graphics.fillRect((getBounds().width - xBar) / 2, 0, xBar, yBar);
			graphics.setColor(Color.red);
			int xLife = (int) (xBar * 0.95);
			int yLife = (int) (yBar * 0.8);
			graphics.fillRect((getBounds().width - xBar) / 2 + (xBar - xLife) / 2, (yBar - yLife) / 2,
					(int) (xLife * ((double) getHealthPoints() / (double) getMaxHealthPoints())), yLife);
			graphics.setColor(Color.blue);

			// draw the name
			nametag = !Main.game.gp.tab_switch ? getRegistry().getName() : Integer.toString(getHealthPoints()) + heart;

			Rectangle2D r = graphics.getFontMetrics().getStringBounds(nametag, graphics);

			graphics.drawString(nametag, (getBounds().width - (int) r.getWidth()) / 2, (int) r.getHeight());
		}

		if (receivedDamage) {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			receivedDamage = false;
		}
		graphics.drawImage(getToDraw(), xBorder, yBorder, world);

		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	public int itemMovement = 0;

	private int itemRotation = 0;
	private int itemXOffset = 0;
	private int itemYOffset = 0;

	protected void drawItem() {
		if (getInventory() == null)
			return;
		Item displayItem = getHandItem();

		if (displayItem instanceof Projectile) {
			switch (getSkinDirection()) {
			case Skin.DOWN:
				itemRotation = 180;
				itemXOffset = -(int) (getEntityWidth() * 0.25);
				itemYOffset = itemSwing ? (int) (getEntityHeight() * 0.1) - ySwing : (int) (getEntityHeight() * 0.1);
				break;
			case Skin.UP:
				itemRotation = 0;
				itemXOffset = (int) (getEntityWidth() * 0.25);
				itemYOffset = itemSwing ? (int) (getEntityHeight() * 0.15) + ySwing : (int) (getEntityHeight() * 0.15);
				break;
			case Skin.LEFT:
				itemRotation = 270;
				itemXOffset = itemSwing ? -xSwing : 0;
				itemYOffset = 0;
				break;
			case Skin.RIGHT:
				itemRotation = 90;
				itemXOffset = itemSwing ? xSwing : 0;
				itemYOffset = 0;
				break;
			}
		} else {

			switch (getSkinDirection()) {
			case Skin.DOWN:
				itemRotation = (itemSwing ? 180 : -45);
				itemXOffset = -(int) (getEntityWidth() * 0.25);
				itemYOffset = itemSwing ? (int) (getEntityHeight() * 0.1) - ySwing : (int) (getEntityHeight() * 0.1);
				break;
			case Skin.UP:
				itemRotation = (itemSwing ? 0 : 45);
				itemXOffset = (int) (getEntityWidth() * 0.25);
				itemYOffset = itemSwing ? (int) (getEntityHeight() * 0.15) + ySwing : (int) (getEntityHeight() * 0.15);
				break;
			case Skin.LEFT:
				itemRotation = (270);
				itemXOffset = itemSwing ? -xSwing : 0;
				itemYOffset = 0;
				break;
			case Skin.RIGHT:
				itemRotation = (90);
				itemXOffset = itemSwing ? xSwing : 0;
				itemYOffset = 0;
				break;
			}

		}
		if (displayItem != null) {

			graphics.rotate(Math.toRadians(itemRotation + itemMovement),
					itemXOffset + displayItem.getItemPivotPoint().x + xBorder,
					displayItem.getItemPivotPoint().y - itemYOffset + yBorder);

			graphics.drawImage(displayItem.getImageOnDirection(), itemXOffset + displayItem.getItemX() + xBorder,
					displayItem.getItemY() - itemYOffset + yBorder, displayItem.getItemWidth(),
					displayItem.getItemHeight(), world);
//			graphics.fillOval(itemXOffset + displayItem.getItemPivotPoint().x + xBorder,
//					displayItem.getItemPivotPoint().y - itemYOffset + yBorder,5,5);
			graphics.rotate(Math.toRadians(360 - itemRotation - itemMovement),
					itemXOffset + displayItem.getItemPivotPoint().x + xBorder,
					displayItem.getItemPivotPoint().y - itemYOffset + yBorder);

		}

	}

	/**
	 * Makes the entity to swing its weapon if it has one
	 */
	public void attack() {
		if (isRiding()) {
			if (getVehicle().getAttackCooldown() == -1) {
				getVehicle().attack();
			}
			return;
		}
		if (hasItem) {
			if (getAttackCooldown() == -1) {
				setAttackCooldown(
						getHandItem().getAttackCooldown() + (getHandItem().shouldAnimateSwing ? attackAnimLength : 1));
				getHandItem().onAttack();
				itemSwing = true;
				if (getHandItem() instanceof Projectile)
					attackAction();
				else {
					animation_variation = new Random().nextInt(2);
					if (animation_variation == 1)
						itemMovement = -60
								* (getSkinDirection() == Skin.DOWN || getSkinDirection() == Skin.LEFT ? -1 : 1);
//				System.out.println("=== attack sequence ==="+(itemMovement+itemRotation));
				}
			}
		}
	}

	private int animation_variation;

	private int attackCooldown = -1;
	private int itemSwingAnimationState = 0;
	public int xSwing = 0;
	public int ySwing = 0;
	private int swingOffset = 0;
	private double swingAlpha = 0;

	private void itemSwingAnimation(boolean itemSwing) {
		if (itemSwing) {
			switch (animation_variation) {
			case 0:
				swingOffset = (int) (height * 0.5 / attackAnimLength);
				if (itemSwingAnimationState <= attackAnimLength / 2) {
					xSwing += swingOffset;
					ySwing += swingOffset;
					itemSwingAnimationState++;
				} else if (itemSwingAnimationState <= attackAnimLength) {
					xSwing -= swingOffset;
					ySwing -= swingOffset;
					itemSwingAnimationState++;
				} else {
					xSwing = 0;
					ySwing = 0;
					this.itemSwing = false;
					itemSwingAnimationState = 0;
				}

				if (itemSwingAnimationState == attackAnimLength / 2) {
					attackAction();
				}
				break;
			case 1:
				int swingAttackLength = attackAnimLength;
				swingOffset = (int) (height * 0.35 / swingAttackLength * 2);
				swingAlpha = (120 / swingAttackLength)
						* (getSkinDirection() == Skin.DOWN || getSkinDirection() == Skin.LEFT ? -1 : 1);

				if (itemSwingAnimationState <= swingAttackLength / 2 && itemMovement > -45) {
					xSwing += swingOffset;
					ySwing += swingOffset;
					// -----------------------------------
					itemSwingAnimationState++;
					itemMovement += swingAlpha;
				} else if (itemSwingAnimationState <= swingAttackLength) {
					xSwing -= swingOffset;
					ySwing -= swingOffset;
					// -----------------------------------
					itemSwingAnimationState++;
					itemMovement += swingAlpha;
				} else {
					xSwing = 0;
					ySwing = 0;
					this.itemSwing = false;
					itemSwingAnimationState = 0;
					itemMovement = 0;
					swingAlpha = 0;
				}
//				System.out.println(itemMovement+itemRotation);

				if (itemSwingAnimationState == swingAttackLength / 2) {
					attackAction();
				}
				break;
			}
		}

	}

	private void attackAction() {
		CPoint attackPoint = getPosition();
		switch (getSkinDirection()) {
		case Skin.DOWN:
			attackPoint = new CPoint(getPosition().x,
					getPosition().y + (int) (getEntityHeight() * (0.5d - getItemHoldHeight()))
							- world.getPixelLength(getHandItem().getRelativeSize()));
			break;
		case Skin.UP:
			attackPoint = new CPoint(getPosition().x,
					getPosition().y + (int) (getEntityHeight() * (0.5d - getItemHoldHeight()))
							+ world.getPixelLength(getHandItem().getRelativeSize()));
			break;
		case Skin.LEFT:
			attackPoint = new CPoint(getPosition().x - world.getPixelLength(getHandItem().getRelativeSize()),
					getPosition().y + (int) (getEntityHeight() * (0.5d - getItemHoldHeight())));
			break;
		case Skin.RIGHT:
			attackPoint = new CPoint(getPosition().x + world.getPixelLength(getHandItem().getRelativeSize()),
					getPosition().y + (int) (getEntityHeight() * (0.5d - getItemHoldHeight())));
			break;
		}
		if (getHandItem() instanceof Projectile)
			return;
		getWorld().getEntityInRadius(/* attackPoint, 0.1 */getPosition(),
				world.getMeterLength((int) getPosition().distance(attackPoint))).forEach(entity -> {
					if (entity != this && getVehicle() != entity) {
						entity.damage(getInventory().getCurrentItem().getDamage(), getRegistry());
					}
				});
	}

	/**
	 * Makes the {@link Entity} to die and remove from the {@link World} and
	 * {@link Tickable}.
	 * 
	 * @param killer - name of the killer who killed the entity, replace it with
	 *               "none", and no message will be shown
	 */
	@Override
	public void die(UID killer) {
		if (killer.getName().equals("none")) {
			// do nothing
		} else if (!killer.getName().equals("")) {
			world.getChat()
					.sendMessage(new ChatMessage("", getRegistry().getName() + " was killed by " + killer.getName()));
		} else {
			world.getChat()
					.sendMessage(new ChatMessage("", getRegistry().getName() + " died through magical circumstances"));
		}
		setDead(true);
		world.removeObjective(this);
		throwInventory();
	}

	@Override
	protected void onDamage(int damage, UID from) {
		super.onDamage(damage, from);
		if (!isBloodOnDamage())
			return;

		for (int i = 0; i < 5; i++) {
			world.addParticle(new CPoint(getPosition().x, getPosition().y), Particle.RANDOM_DIRECTION, 0.1f, 10,
					ParticleHandler.blood);
		}

	}

	@Override
	public void die() {
		setDead(true);
		world.removeObjective(this);
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean isAi() {
		return ai;
	}

	public void setAi(boolean ai) {
		this.ai = ai;
	}

	public double getItemHoldHeight() {
		return itemHoldHeight;
	}

	public void setItemHoldHeight(double itemHoldHeight) {
		this.itemHoldHeight = itemHoldHeight;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Item getHandItem() {
		return getInventory().getCurrentItem();
	}

	public int getAttackCooldown() {
		return attackCooldown;
	}

	public void setAttackCooldown(int attackCooldown) {
		this.attackCooldown = attackCooldown;
	}

	public int getMiscBarPolicy() {
		return miscBarPolicy;
	}

	public void setMiscBarPolicy(int policy) {
		miscBarPolicy = policy;
	}

	public boolean isRiding() {
		return riding;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	protected void setRiding(boolean riding) {
		this.riding = riding;
	}

	protected void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	public boolean isOnPath() {
		return onPath;
	}

	public boolean isBloodOnDamage() {
		return bloodOnDamage;
	}

	public void setBloodOnDamage(boolean bloodOnDamage) {
		this.bloodOnDamage = bloodOnDamage;
	}

}
