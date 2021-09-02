package ua.ks.hogo.fingerscanner.sound;

import org.springframework.util.ResourceUtils;
import java.io.*;
import javax.sound.sampled.*;

/**
 * Sound component for playing voice notifications.
 * Picked from https://habr.com/ru/post/191422/
 * @author Oleksii Maloivan
 * @version 1.0.0
 */
public class Sound implements AutoCloseable {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;

    public Sound(String name){
        try {
            File f = ResourceUtils.getFile(name);
            if(f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bis = new BufferedInputStream(fis);
                stream = AudioSystem.getAudioInputStream(bis);
                clip = AudioSystem.getClip();
                clip.open(stream);
                clip.addLineListener(new Listener());
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                released = true;
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;
            close();
        }
    }

    /**
     * Flag of sound file is loaded.
     * @return true or false.
     */
    public boolean isReleased() {
        return released;
    }

    /**
     * Flag of sound noe playing.
     * @return true or false
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Do play sound.
     * @param breakOld - flag. If sound now playing and breakOld is true
     * then old sound has stoped before new play.
     * @return this
     */
    public Sound play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
        return this;
    }

    public Sound play() {
       return play(true);
    }

    /**
     * Stop playing.
     * @return this
     */
    public Sound stop() {
        if (playing) {
            clip.stop();
        }
        return this;
    }

    /**
     * Close stream.
     */
    public void close() {
        if (clip != null)
            clip.close();

        if (stream != null)
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

    /**
     * Set volume gain.
     * @param gain - The volume gain value. Mast been between 0 and 100.
     * @return this.
     */
    public Sound setVolume(int gain) {
        if(!released) return this;
        float x = (float) gain/100f;
        if (x<0) x = 0f;
        if (x>1) x = 1f;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue((max-min)*x+min);
        return this;
    }

    /**
     * Get setted volume gain.
     * @return Volume gain between 0 and 100
     */
    public int getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (int)((v-min)/(max-min))*100;
    }

    /**
     * Wait for play will stop.
     */
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing)
                    clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}
