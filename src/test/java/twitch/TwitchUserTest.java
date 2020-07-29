package twitch;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TwitchUserTest {
    private final TwitchUser twitchUser = new TwitchUser(Utils.TEST_TOKEN);

    @Test
    @Order(1)
    void getFollowed() {
        assertEquals(1, twitchUser.getFollowed());
    }

    @Test
    @Order(2)
    void unfollow() throws InterruptedException {
        twitchUser.unfollow("realsegu");
        Thread.sleep(60_000);
        assertEquals(0, twitchUser.getFollowed());
    }

    @Test
    @Order(3)
    void follow() throws InterruptedException {
        twitchUser.follow("realsegu");
        Thread.sleep(60_000);
        assertEquals(1, twitchUser.getFollowed());
    }

    @Test
    void isFollowedTo() {
        assertTrue(twitchUser.isFollowedTo(Utils.getChannelID("realsegu")));
        assertFalse(twitchUser.isFollowedTo(Utils.getChannelID("LIRIK")));
    }

    @Test
    void cleanAll() throws InterruptedException {
        if (twitchUser.getFollowed() == 0) {
            twitchUser.follow("realsegu");
            Thread.sleep(60_000);
        }
        twitchUser.cleanAll();
        Thread.sleep(60_000);
        assertEquals(0, twitchUser.getFollowed());
    }

    @Test
    void clean() throws InterruptedException {
        if (twitchUser.getFollowed() == 0) {
            twitchUser.follow("realsegu");
            Thread.sleep(60_000);
        }
        twitchUser.clean(1);
        Thread.sleep(60_000);
        assertEquals(0, twitchUser.getFollowed());
    }

    @Test
    void canFollow() {
        assertTrue(twitchUser.canFollow());
    }
}