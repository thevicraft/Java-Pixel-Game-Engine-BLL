package com.javapixelgame.game.api.entity.car;

import java.io.Serializable;

import com.javapixelgame.game.log.Console;

public class Engine implements Serializable{

	private static final long serialVersionUID = 1L;
	private int rpm = 0;
	private int maxRPM;
	private float gas = 0f;
	protected static final int neutral_rpm = 800;
	
	public static final int RPM_OFF = -1;
	public static final int RPM_NEUTRAL = 0;
	public static final int RPM_NORMAL = 1;
	public static final int RPM_HIGH = 2;
	public static final int RPM_TOO_HIGH = 3;
	
	private boolean running = false;
	
	public Engine(int maxRPM) {
		this.maxRPM = maxRPM;
		
	}

	public void setGas(float gas) {
		if(!isRunning()) {
			this.gas = gas;
			rpm = 0;
			return;
		}
		this.gas = gas;
		rpm = (int) ((maxRPM - neutral_rpm) * gas) + neutral_rpm;
	}
	
	public void start() {
		if(getGas() > 0)return;
		running = true;
		setGas(0f);
	}
	
	public void stop() {
		running = false;
		rpm = 0;
	}
	
	public int getRPMState() {
		if(!isRunning())
			return RPM_OFF;
		if(getRPM() > getMaxRPM() * 0.75f)
			return RPM_TOO_HIGH;
		if(getRPM() > getMaxRPM() * 0.5f)
			return RPM_HIGH;
		if(getRPM() > neutral_rpm)
			return RPM_NORMAL;
		if(getRPM() == neutral_rpm)
			return RPM_NEUTRAL;
//		if(getRPM() < neutral_rpm)
//			return RPM_OFF;
		return RPM_OFF;
	}

	public int getMaxRPM() {
		return maxRPM;
	}

	public void setMaxRPM(int maxRPM) {
		this.maxRPM = maxRPM;
	}
	
	public int getRPM() {
		if(rpm > getMaxRPM()) {
			Console.error("Engine RPM higher than allowed! Correcting...");
			return getMaxRPM();
		}
		return rpm;
	}
	
	public double getSpeedMultiplier() {
		if(!isRunning())return 0;
		return (double)(getRPM()- neutral_rpm) / (double)getMaxRPM();
	}

	public float getGas() {
		return gas;
	}

	public boolean isRunning() {
		return running;
	}

}
