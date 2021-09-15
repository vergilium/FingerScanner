package ua.ks.hogo.fingerscanner.sound;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.Settings;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class PlayList extends HashMap<SoundCommand, PlayListItem> {

    public PlayList(@Value("#{${sounds}}") Map<String, String> playlist){
        super();
        playlist.forEach((cmd, path) -> {
            try {
                put(SoundCommand.fromString(cmd), new PlayListItem(path));
            } catch (Exception ex) {
                log.error(ex.getMessage());
                log.debug(ex);
            }
        });
    }
}
