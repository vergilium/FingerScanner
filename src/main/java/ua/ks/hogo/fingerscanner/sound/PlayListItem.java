package ua.ks.hogo.fingerscanner.sound;

import lombok.Getter;

@Getter
@SuppressWarnings("{unused}")
public class PlayListItem {
    private final Sound sound;
    private int volume;

    public PlayListItem(String path, int volume) {
        sound = new Sound("classpath:" + path);
        setVolume(volume);
    }

    public PlayListItem(String path) {
        this(path, 100);
    }

    public void setVolume(int value){
        if(value >= 0 && value<=100){
            volume = value;
        }
    }

}
