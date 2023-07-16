package com.javapixelgame.game.api;

import javax.swing.SwingWorker;

public class BackgroundWorker extends SwingWorker<Integer, Integer>{
	
	private Runnable r;
	
	public BackgroundWorker(Runnable r) {
		this.r = r;
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		r.run();
		return null;
	}
	
	 @Override
     protected void done() {

	 }
	
	public static final void invokeLater(Runnable r) {
		BackgroundWorker b = new BackgroundWorker(r);
			b.execute();
	}

}
