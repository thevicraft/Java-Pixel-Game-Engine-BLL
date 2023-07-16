package com.javapixelgame.game.api.registry;

import com.javapixelgame.game.api.graphics.ParticleHandler;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.item.Projectile;
import com.javapixelgame.game.api.item.ProjectileAmmo;
import com.javapixelgame.game.api.item.SelfProjectile;
import com.javapixelgame.game.resourcehandling.AnimatedTextureID;
import com.javapixelgame.game.resourcehandling.TextureID;

public final class ItemRegistry implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private ItemRegistry() {
	}

	public enum ItemID {
		PROJECTILE_AK47, PROJECTILE_COLT, PROJECTILE_BOW,

		ITEM_SOUL_SWORD, ITEM_GOLD_SWORD, ITEM_OLD_SWORD, ITEM_STEEL_SWORD, ITEM_DOUBLE_SPEAR,

		ITEM_TORCH,

		AMMO_ARROW, AMMO_GUN_BULLET, AMMO_AK47_BULLET, AMMO_TANK,
	}

	
	public static final Item getItem(String id) {
		try {
			return getItem(ItemID.valueOf(id.toUpperCase()));			
		} catch (java.lang.IllegalArgumentException e) {
			return null;
		}
	}

	public static final Item getItem(ItemID id) {
		switch (id) {
		case PROJECTILE_AK47:
			return new Projectile(new Texture(TextureID.AK47), 1.5, 0.5, 0.824, 0, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onAttack() {
					super.onAttack();
					if (getAmmo() == null)
						return;
					for (int i = 0; i < 10; i++) {
//						getEntity().getWorld()
//								.addParticle(new ParticleHandler(getEntity().getWorld()).getType(ammoSpawn,
//										getEntity().getSkinDirection(), 0.025, 5,
//										ParticleHandler.flame));
						getEntity().getWorld().addParticle(ammoSpawn, getEntity().getSkinDirection(), 0.025, 5,
								ParticleHandler.flame);

					}
				}
			}.setDisplayName("AK-47");
		case PROJECTILE_COLT:
			return new Projectile(new Texture(TextureID.PISTOL_COLT), 0.65, 0.5, 0.824, 5, 0.1) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onAttack() {
					super.onAttack();
					if (getAmmo() == null)
						return;
					for (int i = 0; i < 5; i++) {
//						getEntity().getWorld()
//						.addParticle(new ParticleHandler(getEntity().getWorld()).getType(ammoSpawn,
//								getEntity().getSkinDirection(), 0.025, 5,
//								ParticleHandler.flame));
						getEntity().getWorld().addParticle(ammoSpawn, getEntity().getSkinDirection(), 0.025, 5,
								ParticleHandler.flame);

					}
				}
			}.setDisplayName("colt");
		case PROJECTILE_BOW:
			return new Projectile(new Texture(TextureID.BOW), 0.4, 0.5, 1.2, 5, 0.5).setDisplayName("bow");
		case AMMO_AK47_BULLET:
			return new ProjectileAmmo(new Texture(TextureID.GUN_PROJECTILE), 0.15, 1, 20).setDisplayName("AK-47 ammo");
		case AMMO_ARROW:
			return new ProjectileAmmo(new Texture(TextureID.ARROW), 1, 1, 10).setDisplayName("arrow");
		case AMMO_GUN_BULLET:
			return new ProjectileAmmo(new Texture(TextureID.GUN_PROJECTILE), 0.15, 1, 5).setDisplayName("colt ammo");
		case AMMO_TANK:
			return new ProjectileAmmo(new Texture(TextureID.GUN_PROJECTILE), 0.25, 2, 1000).setDisplayName("tank ammo"); // tank ammunition
		case ITEM_DOUBLE_SPEAR:
			return new SelfProjectile(new Texture(TextureID.SPEAR_DOUBLE), 3, 0.25, 0.5, 0, 20).setDisplayName("double spear");
		case ITEM_GOLD_SWORD:
			return new Item(new Texture(TextureID.SWORD_GOLD), 1.5, 0.5, 0.824, 0, 20).setDisplayName("gold sword");
		case ITEM_OLD_SWORD:
			return new Item(new Texture(TextureID.SWORD_OLD), 1.5, 0.5, 0.824, 0, 20).setDisplayName("old sword");
		case ITEM_SOUL_SWORD:
			return new Item(new Texture(TextureID.SOUL_SWORD), 1.5, 0.5, 0.824, 0, 20).setDisplayName("soul sword");
		case ITEM_STEEL_SWORD:
			return new Item(new Texture(TextureID.SWORD_STEEL), 1.5, 0.5, 0.824, 0, 20).setDisplayName("steel sword");
		case ITEM_TORCH:
			return new Item(new Texture(AnimatedTextureID.torch), 1.5, 0.5, 0.824, 0, 20) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSelection() {
//					System.out.println("light an");
					getEntity().setLightEmitting(true);
					getEntity().setLuminocity(0.65f);
					getEntity().setLightRange(5d);
				}

				@Override
				public void onUnselection() {
					super.onUnselection();
					if (getEntity() != null) {
//						System.out.println("light aus");
						getEntity().setLightEmitting(false);
						getEntity().setLuminocity(0);
						getEntity().setLightRange(0);
					}
				}
			}.setDisplayName("torch");
		}
		return null;

	}
}
