package com.javapixelgame.game.api.item;

import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.ProjectileEntity;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.world.World;

public class ProjectileAmmo extends Item {
	private static final long serialVersionUID = 1L;
	private int projectileDamage;
	private int projectileSpeed;

	public ProjectileAmmo(Texture texture, double sizeInMeter, int projectileSpeed,int projectileDamage) {
		super(texture, sizeInMeter, 0.5, 0.5, plainAttackCooldown, plainDamage);
		// TODO Auto-generated constructor stub
		this.projectileSpeed = projectileSpeed;
		this.projectileDamage = projectileDamage;
	}

	public int getProjectileDamage() {
		return projectileDamage;
	}

	public void setProjectileDamage(int projectileDamage) {
		this.projectileDamage = projectileDamage;
	}

	public ProjectileEntity transfer(CPoint spawn,World world,int direction) {
		return new ProjectileEntity(getRelativeSize(), spawn, getTexture(), world, projectileSpeed, 20,direction,
				projectileDamage, null, null);
	}

	public int getProjectileSpeed() {
		return projectileSpeed;
	}

	public void setProjectileSpeed(int projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

}
