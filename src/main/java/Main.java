import twitch.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static String mode;
    private static File tokenFile;

    public static void main(String[] args) throws IOException {
        Config.loadConfig();
        selectOptions();
    }


    private static void selectOptions() throws IOException {
        if (mode.equals("follow")) {
            String channel = chooseChannel();
            int amount = chooseAmount();
            int threads = chooseThreads();
            FollowSender followSender = new FollowSender(tokenFile, channel, amount, threads);
            followSender.start();
        } else if (mode.equals("checker")) {
            int threads = chooseThreads();
            Checker checker = new Checker(tokenFile, threads);
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

    private static int chooseThreads() {
        System.out.print("Amount of threads (1 < amount < 1000): ");
        int threads = scanner.nextInt();
        if (threads > 1000 || threads < 1) {
            System.out.println("! WRONG AMOUNT ! TRY AGAIN");
            return chooseThreads();
        }
        return threads;
    }
}
