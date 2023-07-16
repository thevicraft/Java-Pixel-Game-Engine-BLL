package com.javapixelgame.game.api;

/**
 * Interface with {@link onWorldTick} and {@link onRandomTick}
 * 
 * @author thevicraft
 *
 */
public interface Tickable {
	/**
	 * This method is executed each 50 milliseconds if the ticking timer is active,
	 * each tick is symbolized as a game loop
	 * 
	 * @param tick
	 * @see {@link WorldTick}
	 */
	public abstract void onWorldTick(int tick);

	/**
	 * This method is executed randomly according to the value in
	 * {@link WorldTick.getRandomTick()}
	 * 
	 * @see {@link WorldTick}
	 */
	public abstract void onRandomTick();
}
