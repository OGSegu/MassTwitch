import twitch.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Config.loadConfig();
        selectOptions();
    }


    private static void selectOptions() throws IOException {
        if (Config.mode.equals("follow")) {
            String channel = chooseChannel();
            int amount = chooseAmount();
            FollowSender followSender = new FollowSender(Config.token_file, channel, amount, Config.threads);
            followSender.start();
        } else if (Config.mode.equals("checker")) {
            Checker checker = new Checker(Config.token_file, Config.threads);
            checker.start();
        }
    }

    private static String chooseChannel() {
        System.out.print("Channel to follow: ");
        String channel = scanner.nextLine();
        if (!Utils.channelExists(channel)) {
            System.out.println("! WRONG CHANNEL ! TRY AGAIN");
            return chooseChannel();
        }
        return channel;
    }

    private static int chooseAmount() {
        System.out.print("Amount of followers (1 < amount < 10000): ");
        int amount = scanner.nextInt();
        if (amount > 10000 || amount < 1) {
            System.out.println("! WRONG AMOUNT ! TRY AGAIN");
            return chooseAmount();
        }
        return amount;
    }

}
