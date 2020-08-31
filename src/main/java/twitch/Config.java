package twitch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static final Properties properties = new Properties();

    static {
        loadConfig();
    }

    public static void loadConfig() {
        FileInputStream in;
        try {
            in = new FileInputStream("config.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
