package ua.ks.hogo.fingerscanner.sound;

import lombok.Getter;

import java.io.FileNotFoundException;

@Getter
public class PlayListItem {
    private final Sound sound;
    private int volume;

    public PlayListItem(String path, int volume) throws FileNotFoundException {
        sound = new Sound("classpath:" + path);
        setVolume(volume);
    }

    public PlayListItem(String path) throws FileNotFoundException {
        this(path, 100);
    }

    public boolean setVolume(int value){
        if(value >= 0 && value<=100){
            volume = value;
            return true;
        }
        return false;
    }

}
