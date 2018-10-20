package com.jackdelia.colonialism.music

import sun.audio.AudioPlayer
import sun.audio.AudioStream

/**
 * Handler for the Thread that Plays the Game's Audio
 */
class MusicThread {

    static Thread threadReference

    /**
     * Starts the Music Thread
     */
    static void start() {
        threadReference = Thread.start {
            InputStream inputStream
            AudioStream audioStream

            try {
                inputStream = new FileInputStream("df.wav")
                audioStream = new AudioStream(inputStream)

                AudioPlayer.player.start(audioStream)

            } catch (Exception e) {
                e.printStackTrace()
            }

            long start = System.currentTimeMillis()

            while(true) {
                if(System.currentTimeMillis() - start >= 492000){
                    run()
                    break
                }
            }
        }
    }

    /**
     * Stops the thread
     */
    static void stop() {
        if(threadReference?.alive) {
            threadReference?.interrupt()
        }
    }

}
