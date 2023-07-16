package com.javapixelgame.game.log;

import javax.swing.JOptionPane;
import java.time.format.DateTimeFormatter;
import java.awt.Component;
import java.time.LocalDateTime;

public class Console {

	
//	private static boolean timeOutput = true;
//	private static boolean threadOutput = true;
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final String seperator = "   ";
	
	public static String getLocalTime() {
		return dtf.format(LocalDateTime.now());
	}
	/**{@link public static void output(Object text) } </p>
	 * Makes a simple console output. It makes a time prefix. Time output can be set through <b>setTimeOutput(boolean time)</b>
	 * @param text - message text to output in console
	 */
	public static void output(Object text) {
//		System.out.println((isTimeOutput() ? "[" +getLocalTime() + (isThreadOutput() ? "|"+ Thread.currentThread().getName().toUpperCase():"")+ "]   ": "") + text);
		System.out.println("[" +getLocalTime()+"|"+Thread.currentThread().getName().toUpperCase()+"]"+seperator+text);
	}
	/**{@link public static void output(Object text, Thread thread) } </p>
	 * Makes a simple console output. It makes a time prefix. Time output can be set through <b>setTimeOutput(boolean time)</b> 
	 * </p>
	 * <b>In addition it gives the thread name to control multi-threading</b>
	 * @param text - message text to output in console
	 * @param thread - optimally the {@link Thread} where this method is executed.
	 */
	public static void output(Object text, Thread thread) {
		System.out.println(
				"[" +getLocalTime()+"|"+thread.getName().toUpperCase() + "]"+seperator 
				+ text);
	}
	/**{@link public static void error(Object text) } </p>
	 * Makes a simple console <b>error</b> output. It makes a time prefix. Time output can be set through <b>setTimeOutput(boolean time)</b>
	 * @param text - <b>error</b> message text to output in console
	 */
	public static void error(Object text) {
//		System.err.println((isTimeOutput() ? "[" +getLocalTime() + "]"+seperator: "") + text);
		System.err.println("[" +getLocalTime()+"|"+Thread.currentThread().getName().toUpperCase()+"]"+seperator+text);
//		System.err.println((isTimeOutput() ? "[" +getLocalTime() + (isThreadOutput() ? "|"+ Thread.currentThread().getName().toUpperCase():"")+ "]   ": "") + text);
	}
	/**{@link public static void errorWin(String error,String log)} </p>
	 * Shows <b>JOptionPane message dialogue</b> with the given <b>error</b> and displays in console as <b>error</b> message.
	 * @param error
	 * @param log
	 */
	public static void errorWin(String error, String log) {
		Console.output(log);
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
	/**{@link public static void errorWin(String error) } </p>
	 * Shows <b>JOptionPane message dialogue</b> with the given <b>error</b>.
	 * @param error
	 */
	public static void errorWin(String error) {
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
	/**{@link public static String input(String message) } </p>
	 * Asks for input from <b>JOptionPane input dialogue</b> with the given <b>message</b>.
	 * @param message
	 * @return
	 */
	public static String input(String message) {
		return JOptionPane.showInputDialog(null, message);
	}
	/**{@link public static int confirm(String message, int confirmOption) } </p>
	 * Asks for confirm from <b>JOptionPane confirm dialogue</b> with the given <b>message</b>.
	 * @param message
	 * @param confirmOption - confirm option from <b>JOptionPane static variables</b> <i>(e.g. JOptionPane.YES_NO_OPTION)</i>
	 * @return
	 */
	public static int confirm(String message, int confirmOption) {
		int option = JOptionPane.showConfirmDialog(null, message, "Confirm Dialogue", confirmOption, JOptionPane.WARNING_MESSAGE);
		logConfirmation(option);
		return option;
	}
	/**{@link public static int confirm(String message, int confirmOption, Component parent) } </p>
	 * Asks for confirm from <b>JOptionPane confirm dialogue</b> with the given <b>message</b>.
	 * @param message
	 * @param confirmOption - confirm option from <b>JOptionPane static variables</b> <i>(e.g. JOptionPane.<b>YES_NO_OPTION</b>)</i>
	 * @param parent - parent component to this dialogue frame
	 * @return
	 */
	public static int confirm(String message, int confirmOption, Component parent) {
		int option = JOptionPane.showConfirmDialog(parent, message, "Confirm Dialogue", confirmOption, JOptionPane.WARNING_MESSAGE);
		logConfirmation(option);
		return option;
	}
	
	private static void logConfirmation(int option) {
		switch(option) {
		case JOptionPane.YES_OPTION:
			Console.output("Accepted confirm dialogue.");
			break;
		case JOptionPane.NO_OPTION:
			Console.output("Declined confirm dialogue.");
			break;
		case JOptionPane.CANCEL_OPTION:
			Console.output("Cancelled confirm dialogue.");
			break;
		case JOptionPane.CLOSED_OPTION:
			Console.output("Closed confirm dialogue.");
			break;
		}
	}

//	public static boolean isTimeOutput() {
//		return timeOutput;
//	}
//
//	public static void setTimeOutput(boolean output) {
//		timeOutput = output;
//	}
//	public static boolean isThreadOutput() {
//		return threadOutput;
//	}
//	public static void setThreadOutput(boolean threadOutput) {
//		Console.threadOutput = threadOutput;
//	}
}
