package com.jackdelia.colonialism;



import com.jackdelia.colonialism.music.MusicThread;

public class SwingGameRunner {

	public static void main(String[] args) {
		Game g = Game.create();

		MusicThread.start();

		g.run();
	}

}
