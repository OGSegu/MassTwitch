package twitch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwitchUserTest {
    private final TwitchUser twitchUser = new TwitchUser(Utils.TEST_TOKEN);

    @Test
    void getFollowed() {
        assertEquals(1, twitchUser.getFollowed());
    }

    @Test
    void canFollow() {
        assertTrue(twitchUser.canFollow());
    }

    @Test
    void isFollowedTo() {
        assertTrue(twitchUser.isFollowedTo(Utils.getChannelID("realsegu")));
        assertFalse(twitchUser.isFollowedTo(Utils.getChannelID("LIRIK")));
    }

    @Test
    void follow() {
        twitchUser.follow("realsegu");
        assertEquals(1, twitchUser.getFollowed());
    }

    @Test
    void unfollow() {
        twitchUser.unfollow("realsegu");
        assertEquals(0, twitchUser.getFollowed());
    }

    @Test
    void cleanAll() {
        if (twitchUser.getFollowed() == 0) twitchUser.follow("realsegu");
        twitchUser.cleanAll();
        assertEquals(0, twitchUser.getFollowed());
    }

    @Test
    void clean() {
        if (twitchUser.getFollowed() == 0) twitchUser.follow("realsegu");
        twitchUser.clean(1);
        assertEquals(0, twitchUser.getFollowed());
    }
}