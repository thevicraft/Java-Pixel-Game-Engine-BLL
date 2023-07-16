package com.javapixelgame.game.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import com.javapixelgame.game.api.entity.Entity;
import com.javapixelgame.game.api.world.World;

import java.util.Timer;

/**
 * Tick Class that manages the ticking with a {@link Timer} for {@link Tickable}
 * objects
 * 
 * @author thevicraft
 * @see {@link World} {@link Game} {@link Tickable}
 */
public class WorldTick {
	
	private float randomTick;

	private byte currentTick = 0;

	private java.util.Timer tick;

	private TimerTask task;

	private boolean running = false;

	private List<Tickable> tickObjects = new ArrayList<Tickable>();
	private List<Tickable> remove1 = new ArrayList<Tickable>();
	private List<Tickable> remove2 = new ArrayList<Tickable>();
	private List<Tickable> add1 = new ArrayList<Tickable>();
	private List<Tickable> add2 = new ArrayList<Tickable>();

	public double TickCalculationLength;

	private World world;

	public WorldTick(World world, float randomTick) {
		this.randomTick = randomTick;

		this.world = world;

	}

	private void resetTimer() {
		tick = new java.util.Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				tickExecution();
			}
		};
	}

	private Random rand = new Random();

	private void tickExecution() {
		long start = System.nanoTime();

		currentTick = (byte) (currentTick + 1);
		if (currentTick > 20) {
			currentTick = 1;
		}
		if (remove1.size() > 0) {
			tickObjects.removeAll(remove1);
			remove1 = new ArrayList<>();
		}
		if (remove2.size() > 0) {
			tickObjects.removeAll(remove2);
			remove2 = new ArrayList<>();
		}
		if (add1.size() > 0) {
			tickObjects.addAll(add1);
			add1 = new ArrayList<>();
		}
		if (add2.size() > 0) {
			tickObjects.addAll(add2);
			add2 = new ArrayList<>();
		}
		WorldTick.this.world.onWorldTick(currentTick);

		WorldTick.this.world.getGameObjectives().forEach(obj -> {
			if (obj instanceof Tickable) {
				((Tickable) obj).onWorldTick(currentTick);
				if ((WorldTick.this.randomTick >= rand.nextFloat()) && (currentTick == 1)) {
					((Tickable) obj).onRandomTick();
				}

			}

		});

		WorldTick.this.world.getCamera().update();

//		BackgroundWorker.invokeLater(()->WorldTick.this.world.getPainter().updateBackground());

		tickObjects.forEach(iterator -> {
			iterator.onWorldTick(currentTick);

			if ((WorldTick.this.randomTick >= rand.nextFloat()) && (currentTick == 1)) {
				iterator.onRandomTick();

			}
		});
		TickCalculationLength = (System.nanoTime() - start) * 1e-6;
	}

	/**
	 * Adds a {@link Tickable} component to the tick list, for now the
	 * {@link onWorldTick()} and the {@link onRandomTick(} methods of the object
	 * will be called
	 * 
	 * @param t - {@link Tickable} object that will be triggered each random and
	 *          regular tick
	 * @see {@link WorldTick}
	 */
	public void addTickingComponent(Tickable t) {
		add1.add(t);
	}

	/**
	 * Adds a {@link Tickable} {@linkplain Array} of components to the tick list,
	 * for now the {@link onWorldTick()} and the {@link onRandomTick(} methods of
	 * the object in that array will be called
	 * 
	 * @param t - {@link Tickable} object {@linkplain Array} that will be triggered
	 *          each random and regular tick
	 * @see {@link WorldTick}
	 */
	public void addTickingComponents(Tickable[] t) {
		add2.addAll(Arrays.asList(t));
	}

	/**
	 * Removes a {@link Tickable} component from the tick list, for now the
	 * {@link onWorldTick()} and the {@link onRandomTick(} methods of the object are
	 * no longer triggered
	 * 
	 * @param t - {@link Tickable} object that will be removed from the tick list
	 *          and no longer be triggered
	 * @see {@link WorldTick}
	 */
	public void removeTickingComponent(Tickable t) {
		remove1 = Arrays.asList(t);
	}

	/**
	 * Removes a {@link Tickable} {@linkplain Array} from the tick list, for now the
	 * {@link onWorldTick()} and the {@link onRandomTick(} methods of the object in
	 * that array are no longer triggered
	 * 
	 * @param t - {@link Tickable} object {@linkplain Array} that will be removed
	 *          from the tick list and no longer be triggered
	 * @see {@link WorldTick}
	 */
	public void removeTickingComponents(Tickable[] t) {
		remove2 = Arrays.asList(t);
	}

	/**
	 * Starts the ticking process, it will trigger the {@link onWorldTick()} and the
	 * {@link onRandomTick(} methods of all the {@linkplain Tickable} objects in the
	 * tick list
	 * </p>
	 * can be added through {@link addTickingComponents(Tickable[] t)} and
	 * {@link addTickingComponent(Tickable t)}
	 * </p>
	 * can be removed through {@link removeTickingComponents(Tickable[] t)} and
	 * {@link removeTickingComponent(Tickable t)} )
	 * 
	 * @see {@link World} {@link Entity} {@link Game}
	 */
	public void start() {
		if (isRunning())
			return;
		running = true;
		resetTimer();
		tick.scheduleAtFixedRate(task, 0, 50);
	}

	/**
	 * Stops the ticking process, it will no longer trigger the
	 * {@link onWorldTick()} and the {@link onRandomTick(} methods of all the
	 * {@linkplain Tickable} objects in the tick list
	 * </p>
	 * can be added through {@link addTickingComponents(Tickable[] t)} and
	 * {@link addTickingComponent(Tickable t)}
	 * </p>
	 * can be removed through {@link removeTickingComponents(Tickable[] t)} and
	 * {@link removeTickingComponent(Tickable t)} )
	 * 
	 * @see {@link World} {@link Entity} {@link Game}
	 */
	public void stop() {
		running = false;
		if(tick == null)return;
		tick.cancel();
		tick.purge();
		tick = null;
		task = null;
		System.gc();
	}

	/**
	 * Returns the Random Tick Value, it is the percentage that a random tick will
	 * be executed each second
	 * 
	 * @return randomTick
	 */
	public float getRandomTick() {
		return randomTick;
	}

	/**
	 * Sets the Random Tick Value, it is the percentage that a random tick will be
	 * executed each second
	 * 
	 * @param randomTick - new value for Random Tick
	 */
	public void setRandomTick(float randomTick) {
		this.randomTick = randomTick;
	}

	/**
	 * Returns the current tick number from 1 to 20.
	 * 
	 * @return
	 */
	public byte getCurrentTick() {
		return currentTick;
	}

	public boolean isRunning() {
		return running;
	}
}
