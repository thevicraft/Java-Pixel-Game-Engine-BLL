package com.javapixelgame.game.api.item;

import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.ProjectileEntity;
import com.javapixelgame.game.api.graphics.Texture;

public class SelfProjectile extends Projectile{
	private static final long serialVersionUID = 1L;
	public SelfProjectile(Texture texture, double sizeInMeter, double holdingX, double holdingY, int attackCooldown,
			int damage) {
		super(texture, sizeInMeter, holdingX, holdingY, attackCooldown,0.5);
		// TODO Auto-generated constructor stub
		setDamage(damage);
	}
	
	
	@Override
	public void onAttack() {
		CPoint bullet = new CPoint(getEntity().getPosition().x + xOffsets[getEntity().getSkinDirection()],
				getEntity().getPosition().y + yOffsets[getEntity().getSkinDirection()]);

		getEntity().getWorld().addObjective(new ProjectileEntity(getRelativeSize(), bullet, getTexture(),
				getEntity().getWorld(), 1, 20, getEntity().getSkinDirection(),  getDamage(), getEntity().getRegistry(),this));
		
		getEntity().getInventory().removeCurrentItem();
	}

}
