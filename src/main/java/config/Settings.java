package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class Settings {
    private static Settings instance;
    public String DB_PATH;

    private Settings(){
        Config config = ConfigFactory.load();
        /* Validating configuration */
        config.checkValid(ConfigFactory.defaultReference(), "DB");
        DB_PATH = config.getString("DB.Path");
    }


    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
}
