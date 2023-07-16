package com.javapixelgame.game.api.entity;

import java.util.List;
import java.util.Random;

import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.item.Projectile;
import com.javapixelgame.game.log.Console;

public final class AI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private AI() {
	}

	/**
	 * Returns an {@link NPCTask} that makes the applied NPC walk in random
	 * directions. After certain amount of time, or after collision, it changes its
	 * direction.
	 * 
	 * @return NPCTask
	 */
	public static final NPCTask RunRandom() {
		return new NPCTask() {
			private static final long serialVersionUID = 1L;
			public NPC npc;
			Random ran = new Random();
			int counter = 0;

			@Override
			public void onTick(int tick) {
				int dir = ran.nextInt(5);
				counter++;
				if (npc.isColliding() || counter >= 20 * npc.getSizeInMeter()) {
					counter = 0;
					switch (dir) {
					case Skin.UP:
						npc.setxVelocity(0);
						npc.setyVelocity(npc.getPixelSpeedPerTick());
						break;
					case Skin.DOWN:
						npc.setxVelocity(0);
						npc.setyVelocity(-npc.getPixelSpeedPerTick());
						break;
					case Skin.LEFT:
						npc.setxVelocity(-npc.getPixelSpeedPerTick());
						npc.setyVelocity(0);
						break;
					case Skin.RIGHT:
						npc.setxVelocity(npc.getPixelSpeedPerTick());
						npc.setyVelocity(0);
						break;
					}
				}
			}

			@Override
			public void onSecond() {
			}

			@Override
			public void onRandomTick() {
			}

			@Override
			public void onInit(NPC npc) {
				this.npc = npc;
			}
		};
	}

	/**
	 * Returns an {@link NPCTask} that makes the applied NPC track another Entity of
	 * the given type. It stores the tracked Entity initially.
	 * 
	 * @return NPCTask
	 */
	public static final NPCTask Track(String type) {
		NPCTask track = new NPCTask() {
			private static final long serialVersionUID = 1L;
			NPC npc;
			double rangeFromTracker = 0;

			@Override
			public void onTick(int tick) {
				
			}

			@Override
			public void onSecond() {

				if (npc.tracker != null)
					rangeFromTracker = npc.tracker.getPosition().distance(npc.getPosition());
				
				if (npc.tracker == null || npc.tracker.isDead()) {

					if (type.equals("player")) {
						npc.tracker = npc.getWorld().getPlayer();
						rangeFromTracker = npc.tracker.getPosition().distance(npc.getPosition());
					} else {

						List<Entity> result = npc.getWorld().getEntityInRadius(npc.getPosition(),
								npc.getSearchRadius());

						for (int i = 0; i < result.size(); i++) {
							if ((/* (result.get(i) instanceof NPC) || */(result.get(i).getRegistry().getRegistryID()
									.equals(type)))
									&& (!result.get(i).getRegistry().getUUID().equals(npc.getRegistry().getUUID()))) {
								npc.tracker = result.get(i);
								rangeFromTracker = npc.tracker.getPosition().distance(npc.getPosition());
//							System.out.println("found as tracker: " + npc.tracker.getRegistry().getName());
								break;
							}
						}
					}
				}
				if (rangeFromTracker > npc.getWorld().getPixelLength(npc.getSearchRadius()) && npc.tracker != null) {
					npc.tracker = null;
//					System.out.println("tracker lost");
				}
				if (npc.tracker != null && npc.tracker.isDead())
					npc.tracker = null;
			}

			@Override
			public void onRandomTick() {
			}

			@Override
			public void onInit(NPC npc) {
				this.npc = npc;
			}

		};
		return track;

	}

	/**
	 * Returns an {@link NPCTask} that makes the applied NPC to work with its
	 * tracked Entity. If the NPC has a tracked Entity, it will walk onto the
	 * tracked Entity.
	 * 
	 * @return NPCTask
	 */
	public static final NPCTask FollowTracker() {
		return new NPCTask() {
			NPC npc;
			private static final long serialVersionUID = 1L;

			@Override
			public void onTick(int tick) {
				// TODO Auto-generated method stub
//				if (npc.tracker != null) {
//
//					npc.walkTowards(npc.tracker.getPosition());
//				} else {
//
//					npc.stopWalking();
//				}
				if (npc.tracker == null) {
					npc.stopPathfind();
					npc.stopWalking();
				}

			}

			@Override
			public void onSecond() {
				if (npc.tracker != null) {
					new Thread(() -> {
						if (npc.tracker == null)
							return;
						npc.stopPathfind();
						npc.pathfind(npc.tracker.getPosition());
					}).start();
					return;
				}
				npc.stopPathfind();
			}

			@Override
			public void onRandomTick() {
			}

			@Override
			public void onInit(NPC npc) {
				this.npc = npc;
			}
		};
	}

	/**
	 * Returns an {@link NPCTask} that makes the applied NPC to work with its
	 * tracked Entity. If the NPC has a tracked Entity and the tracked Entity is in
	 * a certain radius (according to the weapon of the NPC), the NPC will attack
	 * the tracked Entity.
	 * 
	 * @return NPCTask
	 */
	public static final NPCTask AttackNearest() {
		return new NPCTask() {
			private static final long serialVersionUID = 1L;
			NPC npc;
			double rangeFromTracker = 0;
			double attackrange = 0;

			@Override
			public void onTick(int tick) {
				// TODO Auto-generated method stub
				if (npc.tracker != null) {
					rangeFromTracker = npc.tracker.getPosition().distance(npc.getPosition());
//					int abs = Math.abs(npc.getPosition().x - tracker.getPosition().x);
					if (rangeFromTracker <= npc.getWorld().getPixelLength(attackrange)
							+ npc.getWorld().getPixelLength(npc.tracker.getSizeInMeter() / 2)) {
						if (npc.getSkinDirection() == Skin.UP || npc.getSkinDirection() == Skin.DOWN
								&& Math.abs(npc.getPosition().x - npc.tracker.getPosition().x) <= npc.getWorld()
										.getPixelLength(1)) {
							npc.attack();
						} else if (npc.getSkinDirection() == Skin.LEFT
								&& Math.abs(npc.getPosition().y - npc.tracker.getPosition().y) <= npc.getWorld()
										.getPixelLength(1)) {
							npc.attack();
						} else if (npc.getSkinDirection() == Skin.RIGHT
								&& Math.abs(npc.tracker.getPosition().y - npc.getPosition().y) <= npc.getWorld()
										.getPixelLength(1)) {
							npc.attack();
						}
					}
//					npc.attack();
				} else {

				}

			}

			@Override
			public void onSecond() {
				attackrange = npc.getHandItem() instanceof Projectile ? npc.getSearchRadius() : npc.getSizeInMeter();
			}

			@Override
			public void onRandomTick() {
				npc.getInventory().nextItem();
			}

			@Override
			public void onInit(NPC npc) {
				// TODO Auto-generated method stub
				this.npc = npc;
				attackrange = npc.getSizeInMeter();
			}
		};
	}

}
