package twitch;

import org.junit.jupiter.api.Test;

import java.io.File;

class ViewBotTest {

    ViewBot viewBot = new ViewBot(new File("proxy.txt"), 10, "fe1ho");

    @Test
    void start() {
        viewBot.start();
    }
}