package com.javapixelgame.game.util;

import com.javapixelgame.game.log.Console;

public class PerformanceUtil {
	
	private static long start;
	private static double stop;
	
	public synchronized static void start() {
		start = System.nanoTime();
	}
	
	public synchronized static void count() {
		stop = (System.nanoTime()-start)*1e-6;
		start = 0;
	}
	
	public synchronized static void stop() {
		count();
		System.out.println(stop+" ms");
	}
	
	public synchronized static void stop(double min_border, double max_border) {
		count();
		if(stop >= min_border && stop <= max_border)return;
		System.err.println(stop+" ms");
	}
	
	public static void getThread() {
		Console.output(Thread.currentThread().getName());
	}
	
}
