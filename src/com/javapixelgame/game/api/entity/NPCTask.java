package com.javapixelgame.game.api.entity;
/**
 * Interface that supplies with methods for simple AI tasks on an NPC. Needs to be instantiated and added through;
 * </p>
 *  <b>NPC.addNPCTask(NPCTask task);</b>
 *  </p>
 * @author thevicraft
 *
 */
public interface NPCTask extends java.io.Serializable{
	static final long serialVersionUID = 1L;
	/**
	 * This function is executed each Tick of your NPC task
	 * @param tick - the current tick number
	 */
	public void onTick(int tick);
	
	/**
	 * This function is executed <b>once</b> when the NPCTask is added to the NPC's AI.
	 * @param npc - the NPC that the NPCTask is applied to
	 */
	public void onInit(NPC npc);
	
	/**
	 * This function is executed each second.
	 */
	public void onSecond();
	/**
	 * This function is executed each World RandomTick.
	 */
	public void onRandomTick();
}
