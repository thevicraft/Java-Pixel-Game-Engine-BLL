package com.javapixelgame.game.api.registry;

import java.util.UUID;

/**
 * The {@link Class} that creates a unique identification number to separate
 * each Objective in the game
 * 
 * @author thevicraft
 *
 */
public class UID implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String ID;
	private String name;
	private String stringIdentifier = "unknown";

	public UID(String name) {
		this.setName(name);
		ID = UUID.randomUUID().toString();
	}

	public boolean isSameAs(UID uid) {
		return getUUID().equals(uid.getUUID());
	}

	/**
	 * Checks if the UIDs of the two {@link Objective} are from the same type.
	 * 
	 * @return
	 */
	public boolean isSameTypeAs(UID uid) {
		return getRegistryID().equals(uid.getRegistryID());
	}

	public String getUUID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the Registry Identificator of the UID, it is needed to give the
	 * {@link Objective} a certain type, to be comparable to other instances of
	 * Entities, Obstacles, etc. </p>
	 * Same function as mincraft's {@link String} registry "minecraft:air" / "minecraft:xxx"
	 * 
	 * @return
	 */
	public String getRegistryID() {
		return stringIdentifier;
	}
	
	/**
	 * Sets the Registry Identificator of the UID, it is needed to give the
	 * {@link Objective} a certain type, to be comparable to other instances of
	 * Entities, Obstacles, etc. </p>
	 * Same function as mincraft's {@link String} registry "minecraft:air" / "minecraft:xxx"
	 */
	public void setRegistryID(String id) {
		stringIdentifier = id;
	}
}
