package twitch;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TwitchUserTest {

    private final static TwitchUser twitchUser = new TwitchUser(Utils.TEST_TOKEN);

    private final static String VALID_CHANNEL = "realsegu";
    private final static String INVALID_CHANNEL = "randomchannel_lmao";



    @Order(0)
    @Test
    void getFollowed() {
        assertEquals(0, new TwitchUser("g7zp9qrh7921au52ae0jsc0db0xnyb").getFollowed());
    }

    @Order(1)
    @Test
    void unfollow() throws InterruptedException {
        assertFalse(twitchUser.unfollow(INVALID_CHANNEL));
        twitchUser.unfollow(VALID_CHANNEL);
        Thread.sleep(60_000);
        assertEquals(0, twitchUser.getFollowed());
    }

    @Order(2)
    @Test
    void follow() throws InterruptedException {
        twitchUser.follow(VALID_CHANNEL);
        Thread.sleep(60_000);
        assertEquals(1, twitchUser.getFollowed());
    }


    @Order(3)
    @Test
    void isFollowedTo() {
        assertTrue(twitchUser.isFollowedTo(Utils.getChannelID(VALID_CHANNEL)));
        assertFalse(twitchUser.isFollowedTo(Utils.getChannelID("LIRIK")));
    }

    @Disabled
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

    @Order(4)
    @Test
    void clean() throws InterruptedException {
        twitchUser.clean(1);
        Thread.sleep(60_000);
        assertEquals(0, twitchUser.getFollowed());
    }


    @Test
    void canFollow() {
        assertTrue(twitchUser.canFollow());
    }

    @AfterAll
    public static void afterFollow() {
        twitchUser.follow(VALID_CHANNEL);
    }
}