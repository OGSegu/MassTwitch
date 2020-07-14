import twitch.io.FileCreator;
import twitch.Checker;
import twitch.FollowSender;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Checker checker;
        FollowSender followSender;

        if (args.length > 4 || args.length == 0) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        if (args[0].toLowerCase().equals("follow")) {
            if (args[1] == null) {
                FileCreator.create();
            } else {
                FileCreator.create(args[1]);
            }
            if (args[2] == null) {
                throw new IllegalArgumentException("Example: follow \"file.txt\" \"channel\" \"amount\"");
            }
            if (args[2].toLowerCase().isEmpty()) {
                throw new IllegalArgumentException("Example: follow \"file.txt\" \"channel\" \"amount\"");
            }
            if (args[3] == null) {
                throw new IllegalArgumentException("Example: follow \"file.txt\" \"channel\" \"amount\"");
            }
            followSender = new FollowSender(args[2], Integer.parseInt(args[3]));
            followSender.start();
        } else if (args[0].toLowerCase().equals("check")) {
            if (args[1] == null) {
                FileCreator.create();
            } else {
                FileCreator.create(args[1]);
            }
            checker = new Checker();
            checker.start();
        }
    }
}
