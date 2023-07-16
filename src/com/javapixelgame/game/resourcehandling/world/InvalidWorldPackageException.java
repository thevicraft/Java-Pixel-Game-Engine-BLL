package com.javapixelgame.game.resourcehandling.world;

@SuppressWarnings("serial")
public class InvalidWorldPackageException extends Exception{
	public InvalidWorldPackageException(String packageName) {
		super("Unable to init "+packageName+". Invalid package version or incomplete files.");
	}
	
	public InvalidWorldPackageException(String packageName, String message) {
		super("Unable to init "+packageName+". Invalid package version or incomplete files. "+message);
	}
}
