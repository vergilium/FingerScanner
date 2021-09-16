package ua.ks.hogo.fingerscanner.sound;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ResourceUtils;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Sound component for playing voice notifications.
 * Picked from https://habr.com/ru/post/191422/
 * @author Oleksii Maloivan
 * @version 1.0.0
 */
@Log4j2
@SuppressWarnings("unused")
public class Sound implements AutoCloseable {
    private boolean released = false;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;
    private File f;

    public Sound(String file){
        try {
            f = ResourceUtils.getFile(file);
            log.debug("Audio file path: " + f.getAbsolutePath() + " loading...");
            if(f.exists()) {
                log.debug("File " + f.getName() + " is exist.");
                AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
                AudioFormat af = aff.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, af);
                log.debug("Audio file " + f.getName() + " information: " + af.toString());
                if (!AudioSystem.isLineSupported(info)){
                    log.debug("The audio file " + f.getAbsolutePath() + " format is not suported!!!");
                    return;
                }
                clip = (Clip)AudioSystem.getLine(info);
                released = true;
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            log.error(ex.getMessage());
            log.debug(ex);
            released = false;
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
     * @return this
     */
    public Sound play() {
        log.debug("Play sound start.");
        try{
            if (released) {
                clip.open(AudioSystem.getAudioInputStream(f));
                clip.addLineListener(new Listener());
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(100);
                clip.setFramePosition(0);
                log.debug("Audio INFO:\nClip length: " + clip.getMicrosecondLength() / 1000 + " ms" +
                        "\nClip Level: " + clip.getLevel() +
                        "\nVolume Level: " + volumeControl.getValue() + " " + volumeControl.getUnits());
                clip.start();
                playing = true;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            released = false;
            close();
        }
        return this;
    }


    /**
     * Stop playing.
     * @return this
     */
    public Sound stop() {
        if (playing) {
            clip.stop();
            clip.drain();
        }
        return this;
    }

    /**
     * Close stream.
     */
    public void close() {
        if (clip != null)
            clip.close();
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
                do {
                    Thread.sleep(500);
                    clip.wait();
                }while (clip.isRunning());
            } catch (InterruptedException exc) {}
        }
    }

    private class Listener implements LineListener {
        @SneakyThrows
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                    clip.close();
                }
            }
        }
    }
}
