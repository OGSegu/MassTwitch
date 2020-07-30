package twitch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    private final String VALID_CHANNEL = "testrealsegu";
    private final String INVALID_CHANNEL = "randomchannel_lmao";

    @Test
    void getChannelID() {
        assertEquals("557206308", Utils.getChannelID(VALID_CHANNEL));
        assertEquals("Unknown", Utils.getChannelID(INVALID_CHANNEL));
    }

    @Test
    void channelExists() {
        assertTrue(Utils.channelExists(VALID_CHANNEL));
        assertFalse(Utils.channelExists(INVALID_CHANNEL));
    }
}