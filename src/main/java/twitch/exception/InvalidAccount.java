package twitch.exception;

public class InvalidAccount extends Exception {
    private String token;

    public InvalidAccount(String message, String token) {
        super(message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
