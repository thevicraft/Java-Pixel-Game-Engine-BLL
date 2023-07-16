package com.javapixelgame.game.api.item;

import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.entity.ProjectileEntity;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;

public class Projectile extends Item {
	private static final long serialVersionUID = 1L;
	protected int[] xOffsets = new int[4];
	protected int[] yOffsets = new int[4];

	private ProjectileAmmo ammo;
	private double shootingX;

	public Projectile(Texture texture, double sizeInMeter, double holdingX, double holdingY, int attackCooldown,
			double shootingX) {
		super(texture, sizeInMeter, holdingX, holdingY, attackCooldown, 0);
		// TODO Auto-generated constructor stub
		shouldAnimateSwing = false;
		this.shootingX = shootingX;
	}

	@Override
	public void apply(NPC entity) {
		super.apply(entity);
		xOffsets[Skin.DOWN] = (int) (getEntity().getEntityWidth() * (-0.25));
		xOffsets[Skin.UP] = (int) (getEntity().getEntityWidth() * 0.25);
		xOffsets[Skin.LEFT] = (int) -(getEntity().getEntityWidth() / 2
				+ (getItemWidth() /*- getEntity().getEntityWidth() / 2*/));
		xOffsets[Skin.RIGHT] = (int) (getEntity().getEntityWidth() / 2
				+ (getItemWidth()/* - getEntity().getEntityWidth() / 2 */));

		yOffsets[Skin.DOWN] = (int) -(getEntity().getEntityHeight() / 2
				+ (getItemHeight() - getEntity().getEntityHeight() / 2));
		yOffsets[Skin.UP] = (int) (getEntity().getEntityHeight() / 2
				+ (getItemHeight() - getEntity().getEntityHeight() / 2));
//		yOffsets[Skin.LEFT] = (int) -(this.getItemWidth() / 2);
//		yOffsets[Skin.RIGHT] = (int) -(this.getItemWidth() / 2);
		yOffsets[Skin.LEFT] = (int) ((getItemY() - getItemWidth()) * shootingX);
		yOffsets[Skin.RIGHT] = (int) ((getItemY() - getItemWidth()) * shootingX);
	}

	protected CPoint ammoSpawn;

	@Override
	public void onAttack() {
		if (getAmmo() != null) {
			CPoint bullet = new CPoint(getEntity().getPosition().x + xOffsets[getEntity().getSkinDirection()],
					getEntity().getPosition().y + yOffsets[getEntity().getSkinDirection()]
							+ (int) (getEntity().getWorld().getPixelLength(getAmmo().getRelativeSize() / 2)));

//			CPoint bullet = new CPoint(getEntity().getPosition().x,
//					getEntity().getPosition().y	+ (int) (getEntity().getWorld().getPixelLength(getAmmo().getRelativeSize() / 2)));
			ammoSpawn = bullet;

			ProjectileEntity e = getAmmo().transfer(bullet, getEntity().getWorld(), getEntity().getSkinDirection());
			e.setSenderUID(getEntity().getRegistry());
			getEntity().getWorld().addObjective(e);
		}
	}

	public ProjectileAmmo getAmmo() {
		return ammo;
	}

	public void setAmmo(ProjectileAmmo ammo) {
		this.ammo = ammo;
	}

}
