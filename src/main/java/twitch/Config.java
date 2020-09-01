package twitch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static final Properties properties = new Properties();
    public static String mode;
    public static int threads;
    public static File token_file;

    public static void loadConfig() {
        FileInputStream in;
        try {
            in = new FileInputStream("config.properties");
            properties.load(in);
            mode = properties.getProperty("mode");
            token_file = new File(properties.getProperty("token_file"));
            threads = Integer.parseInt(properties.getProperty("threads"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
