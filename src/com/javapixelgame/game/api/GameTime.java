package com.javapixelgame.game.api;

public class GameTime {
	private float lightLevel = 0;
	private int tickTime = 12000;

	public void setTickTime(int time) {
		if (time < 0)
			tickTime = 0;
		else if (time > 24000)
			tickTime = 0;
		else
			tickTime = time;
		updateLightLevel();
	}

	public void nextTick() {
		setTickTime(tickTime + 2);
		updateLightLevel();
	}

	public void updateLightLevel() {
		if (isBetween(0, 6000)) {
			lightLevel = 0.8f;
		} else if (isBetween(6000, 7000)) {
			lightLevel = 0.7f;
		} else if (isBetween(7000, 8000)) {
			lightLevel = 0.6f;
		} else if (isBetween(8000, 9000)) {
			lightLevel = 0.5f;
		} else if (isBetween(9000, 10000)) {
			lightLevel = 0.4f;
		} else if (isBetween(10000, 11000)) {
			lightLevel = 0.3f;
		} else if (isBetween(11000, 16000)) {
			lightLevel = 0f;
		} else if (isBetween(16000, 17000)) {
			lightLevel = 0.3f;
		} else if (isBetween(17000, 19000)) {
			lightLevel = 0.4f;
		} else if (isBetween(19000, 20000)) {
			lightLevel = 0.5f;
		} else if (isBetween(20000, 21000)) {
			lightLevel = 0.6f;
		} else if (isBetween(21000, 22000)) {
			lightLevel = 0.7f;
		} else if (isBetween(22000, 24000)) {
			lightLevel = 0.8f;
		}
	}

	public float getLightLevel() {
		return lightLevel;
	}

	public int getTickTime() {
		return tickTime;
	}

	private boolean isBetween(int arg0, int arg1) {
		return (arg0 <= tickTime) && (tickTime < arg1);
	}
}
