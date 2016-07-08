import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GameRunner {

	public static void main(String[] args) throws IOException {
		Game g = new Game();
		InputStream in = new FileInputStream("df.wav");

		// Create an AudioStream object from the input stream.
		AudioStream as = new AudioStream(in);         

		// Use the static class member "player" from class AudioPlayer to play
		// clip.
		AudioPlayer.player.start(as);
		g.run();

	}

}
