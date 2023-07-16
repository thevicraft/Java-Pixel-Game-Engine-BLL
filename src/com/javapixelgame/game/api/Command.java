package com.javapixelgame.game.api;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.api.gui.overlay.TestOverlay;
import com.javapixelgame.game.api.registry.ItemRegistry;
import com.javapixelgame.game.api.registry.ObjectiveRegistry;
import com.javapixelgame.game.api.registry.ObjectiveRegistry.NPCID;
import com.javapixelgame.game.gui.GamePanel;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.util.PerformanceUtil;

public class Command {

	public static final String crash = "crash";
	public static final String spawn = "spawn";
	public static final String dialog = "dialog";
	public static final String path = "path";
	public static final String time = "time";
	public static final String give = "give";
	public static final String position = "position";

	public static void execute(String cmd) {

		if (cmd.length() <= 1)
			return;

		StringCommand command = new StringCommand(cmd);

//		Console.output("<" + command.getCommand() + ">");
//		command.getArguments().forEach(e -> Console.output("arg" + "<" + e + ">"));

		switch (command.getCommand()) {
// --------------------------------------------------------------------------------------------------------------------------------
		case crash:
			Main.crash("System has been crashed from Terminal", 1);
			break;
// --------------------------------------------------------------------------------------------------------------------------------
		case spawn:
			int x = 0;
			int y = 0;
			if (command.hasArguments(2)) {

				if (command.isArgumentsSameClass(new int[] { StringCommand.INTEGER, StringCommand.INTEGER })) {
					x = command.getArgumentInt(0);
					y = command.getArgumentInt(1);
				}

			} else {
				x = GameHandler.getWorld().getPlayer().getPosition().x;
				y = GameHandler.getWorld().getPlayer().getPosition().y;
			}
//			getWorld().addObjective(new ObjectiveRegistry.Flag(3, p, TextureLoader.get(TextureID.FLAG_SIGN_SOCIALISM), world));
			GameHandler.getWorld().addObjective(
					ObjectiveRegistry.getNPC(NPCID.DARK_KNIGHT, new CPoint(x, y), GameHandler.getWorld()));
			break;
		// --------------------------------------------------------------------------------------------------------------------------------
		case dialog:
//			GamePanel.get().chat
//					.sendRawTest(Console.input(command.hasArguments() ? command.getArgument(0) : "Are you Gay"));
			Thread.getAllStackTraces().keySet().forEach((t) -> System.out
					.println(t.getName() + "\nIs Daemon " + t.isDaemon() + "\nIs Alive " + t.isAlive()));
			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case path:
			if (command.hasArguments(2)
					&& command.isArgumentsSameClass(new int[] { StringCommand.INTEGER, StringCommand.INTEGER })) {
				new Thread(() -> {
					GameHandler.getWorld().getPlayer().stopPathfind();

					CPoint goal = new CPoint(command.getArgumentInt(0), command.getArgumentInt(1));
					GameHandler.getWorld().getPlayer().pathfind(goal);
				}).start();
			}
			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case time:
			if (command.hasArguments(2)
					&& command.isArgumentsSameClass(new int[] { StringCommand.STRING, StringCommand.INTEGER })) {
				switch (command.getArgument(0)) {
				case "set":
					GameHandler.getWorld().getTime().setTickTime(command.getArgumentInt(1));
					break;
				case "add":
					GameHandler.getWorld().getTime()
							.setTickTime(command.getArgumentInt(1) + GameHandler.getWorld().getTime().getTickTime());
					break;
				}
			} else if (command.hasArguments()) {
				if (command.getArgument(0).equals("get"))
					GamePanel.get().chat.sendRawTest("Game Time: " + GameHandler.getWorld().getTime().getTickTime());
			}
			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case "engine":
			if (!GameHandler.getWorld().getPlayer().isRiding()
					|| !(GameHandler.getWorld().getPlayer().getVehicle() instanceof Car))
				break;
			if (command.hasMinArguments(1)) {
				switch (command.getArgument(0)) {
				case "start":
					((Car) GameHandler.getWorld().getPlayer().getVehicle()).getEngine().start();
					break;
				case "stop":
					((Car) GameHandler.getWorld().getPlayer().getVehicle()).getEngine().stop();
					break;
				case "rpm":
					Console.output(((Car) GameHandler.getWorld().getPlayer().getVehicle()).getEngine().getRPM());
					break;
				case "gas":
					if (command.isArgumentOfType(1, StringCommand.FLOAT)) {
						((Car) GameHandler.getWorld().getPlayer().getVehicle()).getEngine()
								.setGas(command.getArgumentFloat(1));
					}
					Console.output(((Car) GameHandler.getWorld().getPlayer().getVehicle()).getEngine().getGas());
					break;
				}
			}
			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case give:
			if (command.hasArguments()) {
				int count = command.hasArguments(2) && command.isArgumentOfType(1, StringCommand.INTEGER)
						? Integer.parseInt(command.getArgument(1))
						: 1;
//				Console.output(command.hasArguments(2) && command.isArgumentOfType(1, StringCommand.INTEGER));

				for (int i = 0; i < count; i++) {
					GameHandler.getWorld().getPlayer().getInventory().add(ItemRegistry.getItem(command.getArgument(0)));
				}
			}

			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case position:
			if (!command.hasMinArguments(2))
				break;

			if (command.getArgument(0).equals("get")) {
				CPoint pos = GameHandler.getWorld().getPlayer().getPosition();
				if (command.getArgument(1).equals("meter")) {
					GamePanel.get().chat.sendRawTest("(" + GameHandler.getWorld().getMeterLength(pos.x) + "|"
							+ GameHandler.getWorld().getMeterLength(pos.y) + ")");
					break;
				}
				if (command.getArgument(1).equals("pixel")) {
					GamePanel.get().chat.sendRawTest("(" + pos.x + "|" + pos.y + ")");
					break;
				}
				break;
			}

			if (!command.hasArguments(3))
				break;

			if (command.isArgumentsSameClass(
					new int[] { StringCommand.STRING, StringCommand.INTEGER, StringCommand.INTEGER })) {
				GameHandler.getWorld().getPlayer()
						.teleport(new CPoint(command.getArgumentInt(1), command.getArgumentInt(2)));
				break;
			}

			break;
//--------------------------------------------------------------------------------------------------------------------------------
		case "radius":
			if (!command.hasArguments(1) || !command.isArgumentOfType(0, StringCommand.DOUBLE))
				return;
			PerformanceUtil.start();
			GameHandler.getWorld()
					.getEntityInRadius(GameHandler.getWorld().getPlayer().getPosition(), command.getArgumentDouble(0))
					.forEach(e -> Console.output(e.getRegistry().getName()));
			PerformanceUtil.stop();
			break;
		}
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true")) {
			Console.output("(Successfully) executed command: " + cmd);
		}
	}

}
