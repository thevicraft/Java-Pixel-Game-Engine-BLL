package com.javapixelgame.game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.javapixelgame.game.api.BackgroundWorker;
import com.javapixelgame.game.gui.Game;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.resourcehandling.GameFont;
import com.javapixelgame.game.test.GLTest;
import com.javapixelgame.game.test.TestFrame;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;


public class Main {

	public static Game game;

	public static GraphicsEnvironment environment;
	public static GraphicsDevice device;
	public static Rectangle monitor;

	public static void main(String[] args) {
		List<String> arguments = Arrays.asList(args);
		if (args.length > 0) {
			if (arguments.contains("test")) {
				// does nothing
				return;
			} else if (arguments.contains("sound")) {
				soundTest();
				return;
			}
		}
		
		System.setProperty("-Dsun.java2d.opengl", "true");
		GameFont.load();
		start();
				
//		TestFrame.mainTest();
//		GLTest.mainTest();
	}

	public static void restart() {
		close(false);
		
		sleep(1000);
		Console.output("Program restarting...");
		sleep(1000);
		start();
	}
	
	public static void start() {
		if(game != null)return;
		
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();

		monitor = device.getDefaultConfiguration().getBounds();
		
		game = new Game();
		Console.output("Program starting up... ["+getAllocatedMegaBytes()+" MB]",Thread.currentThread());
	}

	public static void close(boolean exitJVM) {
		if (game.isDisplayable())
			game.dispose();
		game = null;
		System.gc();
		System.runFinalization();
		System.gc();
        System.gc();
        System.gc();
        System.gc();
		
		Console.output("Program shutdown ... ["+getAllocatedMegaBytes()+" MB]",Thread.currentThread());
		Console.output("<======================================================>");
		
		if(exitJVM)
			System.exit(0);
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Console.error("Thread could not be slept.");
			e.printStackTrace();
		}
	}
	
	public static long getAllocatedRAM() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
	public static int getAllocatedMegaBytes() {
		Runtime runtime = Runtime.getRuntime();
		return (int)((runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L));
	}
	
	public synchronized static void crash(String message, int exitCode) {
		try{
			close(true);
		}catch(Exception e) {};
		Console.errorWin("🖕🖕🖕"+" "+message, "Game crashed... Exit Code: "+exitCode);
		System.exit(exitCode);
	}
	

	public static void soundTest() {
		try {
//			SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class );
//			SoundSystemConfig.addLibrary( LibraryJOAL.class );
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.err.println("error linking with the plug-ins");
		}
		SoundSystem s = new SoundSystem();

//		boolean priority = false;
//		String sourcename = "gas";
//		String filename = "gas.wav";
//		boolean loop = true;
//		float x = 0;
//		float y = 0;
//		float z = 0;
		int aModel = SoundSystemConfig.ATTENUATION_ROLLOFF;
		float rFactor = SoundSystemConfig.getDefaultRolloff();
//		s.newSource(priority, sourcename, filename, loop, x, y, z, aModel, rFactor);

//		s.newSource(true, "title_theme", "main_title_theme.ogg",false,0,0,0,aModel,rFactor);
//		
//		s.play("title_theme");

//		s.quickPlay( false, "engineStart.wav", false,
//				0, 0, 0,
//				SoundSystemConfig.ATTENUATION_ROLLOFF,
//				SoundSystemConfig.getDefaultRolloff()
//				);

		s.newSource(true, "idle", "idle.wav", true, 0, 0, 0, aModel, rFactor);

		s.newStreamingSource(true, "engineStart", "engineStart.wav", false, 0, 0, 0, aModel, rFactor);

		s.newStreamingSource(true, "engineStop", "engineStop.wav", false, 0, 0, 0, aModel, rFactor);

//		s.play("engineStart");
//
//		s.play("idle");
//
//		s.setVolume("idle", 0.25f);

		try {
//			for (int i = 0; i < 2; i++) {
//
//				Thread.sleep(new Random().nextInt(5000)+1000);
//				Console.output("Manually applying gas on engine...");
//				s.setPitch(s.quickPlay( false, "gas.wav", false,
//						0, 0, 0,
//						SoundSystemConfig.ATTENUATION_ROLLOFF,
//						SoundSystemConfig.getDefaultRolloff()
//						), 2f);
//				
//				Thread.sleep(new Random().nextInt(1000)+1000);
//
//			}

			s.setPitch("engineStart", 0.5f);

			String task = "[start, stop, g]";

			System.out.println(task);
			Scanner in = new Scanner(System.in);
			boolean scanning = true;
			boolean engineRunning = false;
			while (scanning && in.hasNext()) {
				System.out.println(task);
				s.newStreamingSource(true, "engineStart", "engineStart.wav", false, 0, 0, 0, aModel, rFactor);

				s.newStreamingSource(true, "engineStop", "engineStop.wav", false, 0, 0, 0, aModel, rFactor);
				switch (in.nextLine()) {
				case "g":
					if (engineRunning) {
						s.setPitch(s.quickPlay(false, "gas.wav", false, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF,
								SoundSystemConfig.getDefaultRolloff()), 2f);
						Console.error("Manually applying gas on engine...");
//						s.stop("idle");
//						s.setPitch("idle", s.getPitch("idle")+0.1f);
//						s.play("idle");
//						System.out.println(s.getPitch("idle"));

					}
					break;
				case "start":
					if (!engineRunning) {
						engineRunning = true;
						Console.error("Starting Engine...");
						s.play("engineStart");
						s.play("idle");
						s.setVolume("idle", 0.25f);
					}
					break;
				case "stop":
					if (engineRunning) {
						engineRunning = false;
						if (s.playing("engineStart"))
							s.stop("engineStart");
						s.stop("idle");
						s.play("engineStop");
						Console.error("Shutting down engine...");
					}
					break;
				case "shutdown":
					scanning = false;
					in.close();
					break;

				}
			}
			Thread.sleep(2000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s.cleanup();
	}
}
