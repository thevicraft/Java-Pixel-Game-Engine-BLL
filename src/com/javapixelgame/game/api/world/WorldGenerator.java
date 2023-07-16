package com.javapixelgame.game.api.world;

import java.net.URISyntaxException;
import java.util.Random;

import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.obstacle.Fire;
import com.javapixelgame.game.api.registry.ItemRegistry;
import com.javapixelgame.game.api.registry.ObjectiveRegistry;
import com.javapixelgame.game.api.registry.ItemRegistry.ItemID;
import com.javapixelgame.game.api.registry.ObjectiveRegistry.Flag;
import com.javapixelgame.game.api.registry.ObjectiveRegistry.House;
import com.javapixelgame.game.api.registry.ObjectiveRegistry.NPCID;
import com.javapixelgame.game.api.registry.ObjectiveRegistry.Tree;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.resourcehandling.SkinID;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.resourcehandling.world.InvalidWorldPackageException;
import com.javapixelgame.game.resourcehandling.world.WorldPackage;

public class WorldGenerator {

	public static final World generateTestMap(int width, int height) {

		World world = null;
		try {
			world = new World(WorldPackage.loadWorldModel("world_1"),
					Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)), width, height);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidWorldPackageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Player player = new Player(1.75, world.getPixelPosition(12, 36), new Skin(SkinID.knight_red), world, 0.15, // 0.25
				"thevicraft", 200);
		player.setItemHoldHeight(0.6);
		player.setMiscBarPolicy(NPC.SHOW_MISC_BAR_NEVER);

		world.addPlayer(player);

		Item[] weapons = { ItemRegistry.getItem(ItemID.ITEM_TORCH), ItemRegistry.getItem(ItemID.ITEM_DOUBLE_SPEAR),
				ItemRegistry.getItem(ItemID.PROJECTILE_BOW), ItemRegistry.getItem(ItemID.PROJECTILE_COLT),
				ItemRegistry.getItem(ItemID.ITEM_STEEL_SWORD), ItemRegistry.getItem(ItemID.PROJECTILE_AK47) };

		for (Item i : weapons) {
			if (!player.getInventory().isFilled())
				player.getInventory().add(i);
		}
		player.getInventory().nextItem();

		House house = ObjectiveRegistry.House(9, world.getPixelPosition(26, 10), new Texture(TextureID.WOODEN_HOUSE),
				world);
		house.getCollision().setBorders(0.25, 0.25, 0.5, 0.7);
		house.getCollision().update();

		House castle_stone = ObjectiveRegistry.House(14, world.getPixelPosition(12, 62),
				new Texture(TextureID.CASTLE_STONE), world);
		castle_stone.getCollision().setBorders(0, 0.35, 0.95, 0.6);
		castle_stone.getCollision().update();
		world.addObjective(castle_stone);

		// second huge island part

		House castle_sand = ObjectiveRegistry.House(14, world.getPixelPosition(130, 40),
				new Texture(TextureID.CASTLE_SAND), world);
		castle_sand.getCollision().setBorders(0, 0.35, 0.95, 0.6);
		castle_sand.getCollision().update();
		world.addObjective(castle_sand);

		for (int i = 0; i < 30; i++) {
			world.addObjective(ObjectiveRegistry.getNPC(NPCID.DARK_KNIGHT,
					world.getPixelPosition(111 + new Random().nextInt(119 - 111), 28 + new Random().nextInt(51 - 28)),
					world));
		}
		// -----------------------------------------

		for (int i = 0; i < 21; i += 2) {
			Flag f1 = ObjectiveRegistry.Flag(3, world.getPixelPosition(85 + i, 42 + 2), world);
			Flag f2 = ObjectiveRegistry.Flag(3, world.getPixelPosition(85 + i, 36 + 2), world);
			f1.setVariant(Flag.DARK_KNIGHT);
			f2.setVariant(Flag.DARK_KNIGHT);
			world.addObjective(f1);
			world.addObjective(f2);
		}

		world.addObjective(ObjectiveRegistry.getNPC(NPCID.DARK_KNIGHT, world.getPixelPosition(6, 16), world));

		NPC gozilla = ObjectiveRegistry.getNPC(NPCID.GOZILLA, world.getPixelPosition(119, 40), world);
		world.addObjective(gozilla);
//		gozilla.setAi(false);

		world.addObjective(ObjectiveRegistry.getNPC(NPCID.TANK, world.getPixelPosition(20, 16), world));

		world.addObjective(ObjectiveRegistry.Plant(0.5, world.getPixelPosition(5, 5), world));

//		ObjectiveRegistry.Soldier soldier = new ObjectiveRegistry.Soldier(new CPoint(6, 6), worldInstance);

		Tree tree = ObjectiveRegistry.Tree(5, world.getPixelPosition(26, 17), new Texture(TextureID.HUGE_TREE), world);
		tree.getCollision().setBorders(0.4, 0.5, 0.2, 0.5);
		tree.getCollision().update();

		world.addObjective(ObjectiveRegistry.getNPC(NPCID.DOG, world.getPixelPosition(7, 7), world));

		world.addObjective(ObjectiveRegistry.getNPC(NPCID.DELOREAN, world.getPixelPosition(13, 40), world));

		world.addObjective(ObjectiveRegistry.getNPC(NPCID.OLD_MAN, world.getPixelPosition(20, 20), world));

		// ------------------------------------------------------------------------------------------------------------

		// ------------------------------------------------------------------------------------------------------------

		world.addObjective(tree);

		world.addObjective(house);

		world.addObjective(new Fire(world.getPixelPosition(20, 10), world, Fire.verysmall));

		return world;

	}

}
