package com.javapixelgame.game.api;

import java.util.ArrayList;
import java.util.List;

public class StringCommand {

	private String command;
	private List<String> arguments = new ArrayList<>();

	public static final int BYTE = 0;
	public static final int SHORT = 1;
	public static final int INTEGER = 2;
	public static final int LONG = 3;
	public static final int FLOAT = 4;
	public static final int DOUBLE = 5;
	public static final int BOOLEAN = 6;
	public static final int CHARACTER = 7;
	public static final int STRING = 8;

	public StringCommand(String command) {
		String text = command;
		if (text.startsWith("/"))
			text = command.replaceFirst("/", "");

		List<String> args = new ArrayList<>();

		for (String str : text.split(" ")) {
			if (this.command == null)
				this.command = str.trim();
			else if (str.trim().length() > 0)
				arguments.add(str.trim());
		}

	}

	public boolean isArgumentsSameClass(int[] datatypes) {
		boolean sameclass = true;
//		Console.output("control arguments same class");
		for (int i = 0; i < datatypes.length; i++) {
//			Console.output(getArgument(i)+"="+datatypes[i]+" "+isArgumentOfType(i, datatypes[i]));
			if (!isArgumentOfType(i, datatypes[i])) {
				sameclass = false;
			}
		}
		return sameclass;

	}

	public boolean isArgumentOfType(int index, int datatype) {
		String arg = getArgument(index);
		try {
			switch (datatype) {
			case BYTE:
				Byte.parseByte(arg);
				break;
			case SHORT:
				Short.parseShort(arg);
				break;
			case INTEGER:
				Integer.parseInt(arg);
				break;
			case LONG:
				Long.parseLong(arg);
				break;
			case FLOAT:
				Float.parseFloat(arg);
				break;
			case DOUBLE:
				Double.parseDouble(arg);
				break;
			case BOOLEAN:
				Boolean.parseBoolean(arg);
				break;
			case CHARACTER:
				return arg.toCharArray().length == 1;
			case STRING:
				break;
			default:
				return false;
			}
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasArguments() {
		return arguments.size() > 0;
	}

	public boolean hasMinArguments(int size) {
		return arguments.size() >= size;
	}

	public boolean hasMaxArguments(int size) {
		return arguments.size() <= size;
	}

	public boolean hasArguments(int size) {
		return arguments.size() == size;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public String getArgument(int index) {
		if (index > arguments.size() - 1)
			return "null";
		return arguments.get(index);
	}

	public String getCommand() {
		if (command == null)
			return "null";
		return command;
	}
	
	public byte getArgumentByte(int i) {
		return Byte.parseByte(getArgument(i));
	}
	
	public short getArgumentShort(int i) {
		return Short.parseShort(getArgument(i));
	}
	
	public int getArgumentInt(int i) {
		return Integer.parseInt(getArgument(i));
	}
	
	public long getArgumentLong(int i) {
		return Long.parseLong(getArgument(i));
	}
	
	public float getArgumentFloat(int i) {
		return Float.parseFloat(getArgument(i));
	}
	
	public double getArgumentDouble(int i) {
		return Double.parseDouble(getArgument(i));
	}
	
	public boolean getArgumentBoolean(int i) {
		return Boolean.parseBoolean(getArgument(i));
	}
	
	public char getArgumentCharacter(int i) {
		char[] c = getArgument(i).toCharArray();
		return c[0];
	}
	
}
