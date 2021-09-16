package ua.ks.hogo.fingerscanner.sound;

import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;


@Component
@SuppressWarnings("unused")
public class Player {
    private boolean isEnable = true;

    private final RemoteConfig remoteConfig;
    private final PlayList playList;

    public Player(RemoteConfig remoteConfig, PlayList playList) {
        this.remoteConfig = remoteConfig;
        this.playList = playList;
    }

    public void play(SoundCommand soundCommand){
        if(!isEnable) return;
        playList.get(soundCommand).getSound().play().join();
    }

    public void setVolume(){
        playList.forEach((cmd, song) -> song.setVolume(remoteConfig.getAudioLevel() != null ? remoteConfig.getAudioLevel() : 50));
    }

    public void enableAudio(){
        isEnable = true;//remoteConfig.getAudioEnable();
    }

}
