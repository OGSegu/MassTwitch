package twitch;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


class FollowSenderTest {

    @Test
    void getChannelID() {
        assertEquals("403015056", Utils.getChannelID("realsegu"));
    }
}