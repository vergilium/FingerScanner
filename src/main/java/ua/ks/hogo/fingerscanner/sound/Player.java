package ua.ks.hogo.fingerscanner.sound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;


@Component
public class Player {
    private boolean isEnable = true;

    @Autowired
    private RemoteConfig remoteConfig;
    @Autowired
    private PlayList playList;

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
