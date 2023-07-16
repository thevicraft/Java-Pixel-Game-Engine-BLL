package com.javapixelgame.game.api.gui;

public class ChatMessage {
	
	private String sender;
	private String message;
	public static final int displayDuration = 5000;
	private long sendTime;
	
	public ChatMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
		this.sendTime = System.currentTimeMillis();
	}
	
	
	public boolean shouldDisplay() {
		return System.currentTimeMillis() - sendTime <= displayDuration;
	}


	public String getMessage() {
		return message;
	}


	public String getSender() {
		return sender;
	}
	
	public String getChatOutput() {
		String output = ""+sender+":"+" "+message;
		return output;
		
	}
	
}
