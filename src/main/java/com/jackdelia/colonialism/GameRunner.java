package com.jackdelia.colonialism;

import java.io.InputStream;

import sun.audio.*;


public class GameRunner {

	public static void main(String[] args) {
		Game gameManager = new Game();
		
		Thread musicThread = new Thread(new Runnable() {

		    public void run() {
		    	InputStream in;
		    	AudioStream as = null;
				try {
					in = GameRunner.class.getResourceAsStream("/audio/df.wav");
					as = new AudioStream(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				AudioPlayer.player.start(as);
				long start = System.currentTimeMillis();
				while(true){
					if(System.currentTimeMillis() - start >= 492000){
						run();
						break;
					}
				}
				
		    }
		            
		});
		        
		musicThread.start();
		
		
		gameManager.run();
	}

}
