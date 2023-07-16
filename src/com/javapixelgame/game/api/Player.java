package com.javapixelgame.game.api;

import com.javapixelgame.game.GameConstants;
import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.Collision;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.entity.Vehicle;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.gui.ChatMessage;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.world.World;

/**
 * {@link Player} the Class provides various methods for controlling the Player,
 * it is {@linkplain Tickable}, it extends {@link NPC}, so it acts like one and
 * has nearly the same methods
 * 
 * @author thevicraft
 * @see {@link World} {@link Game} {@link Tickable}
 */
public class Player extends NPC {

	private static final long serialVersionUID = 8688176121437528634L;

	public Player(double sizeInMeter, CPoint spawn, Texture skin, World world, double speed_m_per_s, String name,
			int lifePoints) {
		super(sizeInMeter, spawn, skin, world, speed_m_per_s, name, lifePoints, true);
		// TODO Auto-generated constructor stub
		getInventory().setMaxSize(GameConstants.PLAYER_INVENTORY_SIZE);
	}

	public Player(double sizeInMeter, CPoint spawn, Skin skin, World world, double speed_m_per_s, String name,
			int lifePoints) {
		super(sizeInMeter, spawn, skin, world, speed_m_per_s, name, lifePoints, true);
		// TODO Auto-generated constructor stub
		getInventory().setMaxSize(GameConstants.PLAYER_INVENTORY_SIZE);
	}

	@Override
	public void constructEntity() {
		collision = new Collision(this);
		super.constructEntity();
		setMiscBarPolicy(SHOW_MISC_BAR_NEVER);
		getRegistry().setRegistryID("player");
	}

	@Override
	public void onEntityTick(int tick) {
		super.onEntityTick(tick);
//		System.out.println(isColliding() && checkObjectiveColliding());
	}

	public void interact() {

//		String t = "Press "+Main.game.keyhandler.getKeyName(Keys.interact).toLowerCase()+" to enter a vehicle.";
//		Main.game.gp.infobox.display(t, 20);

		if (!isRiding())
			getWorld().getEntityInRadius(getPosition(), 1).forEach(e -> {
				if (e instanceof Vehicle) {
					((Vehicle) e).setRider(this);
				}
			});
		else
			getVehicle().eject();
	}

	@Override
	public void die(UID uid) {
		if (uid.getName().equals("none")) {
			// do nothing
		} else if (!uid.getName().equals("")) {
			world.getChat()
					.sendMessage(new ChatMessage("", getRegistry().getName() + " was killed by " + uid.getName()));
		} else {
			world.getChat()
					.sendMessage(new ChatMessage("", getRegistry().getName() + " died through magical circumstances"));
		}
		if (isRiding())
			getVehicle().eject();
		setDead(true);
		throwInventory();
		Main.game.gp.setKilledState();
	}

	public void respawn() {
		setPosition(getSpawn());
		setDead(false);
		regenerate();
	}

	@Override
	public void onRenderedTick(int tick) {
		// TODO Auto-generated method stub

	}
}
