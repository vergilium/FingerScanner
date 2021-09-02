package ua.ks.hogo.fingerscanner.sound;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;
import ua.ks.hogo.fingerscanner.config.Settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class Player {
    private boolean isEnable = true;

    @Autowired
    private Settings conf;
    @Autowired
    private RemoteConfig remoteConfig;

    private Map<SoundCommand, Sound> playList = Collections.synchronizedMap(new HashMap<>());

    public void init(){
        conf.SOUND_FILES.forEach((cmd, path) -> {
            playList.put(cmd, new Sound("classpath:" + path));
        });
    }

    public void play(SoundCommand soundCommand){
        if(!isEnable) return;
        playList.get(soundCommand).play().join();
    }

    public void setVolume(){
        playList.forEach((cmd, song) -> song.setVolume(remoteConfig.getAudioLevel() != null ? remoteConfig.getAudioLevel() : 50));
    }

    public void enableAudio(){
        isEnable = remoteConfig.getAudioEnable();
    }

}
