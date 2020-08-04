import twitch.Utils;
import twitch.io.FileCreator;
import twitch.Checker;
import twitch.FollowSender;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Checker checker;
    private static FollowSender followSender;
    private static Scanner scanner = new Scanner(System.in);

    private static String tokenFile;
    private static String channel;
    private static int amount;
    private static int threads;

    public static void main(String[] args) throws IOException {
        checkArguments(args);
    }


    private static void checkArguments(String[] args) throws IOException {
        if (args.length > 1) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        if (args[0].toLowerCase().equals("follow")) {
            tokenFile = chooseTokenFile();
            channel = chooseChannel();
            amount = chooseAmount();
            threads = chooseThreads();
            followSender = new FollowSender(channel, amount, threads);
            followSender.start();
        } else if (args[0].toLowerCase().equals("check")) {
            tokenFile = chooseTokenFile();
            checker = new Checker();
            checker.start();
        }
    }

    private static String chooseTokenFile() {
        System.out.print("Token file: ");
        String fileURL = scanner.nextLine();
        if (!FileCreator.create(fileURL)) {
            System.out.println("! WRONG FILE ! TRY AGAIN");
            chooseTokenFile();
        }
        return fileURL;
    }

    private static String chooseChannel() {
        System.out.print("Channel to follow: ");
        String channel = scanner.nextLine();
        if (!Utils.channelExists(channel)) {
            System.out.println("! WRONG CHANNEL ! TRY AGAIN");
            chooseChannel();
        }
        return channel;
    }

    private static int chooseAmount() {
        System.out.print("Amount of followers (1 < amount < 10000): ");
        int amount = scanner.nextInt();
        if (amount > 10000 || amount < 1) {
            System.out.println("! WRONG AMOUNT ! TRY AGAIN");
            chooseAmount();
        }
        return amount;
    }

    private static int chooseThreads() {
        System.out.print("Amount of threads (1 < amount < 1000): ");
        int threads = scanner.nextInt();
        if (threads > 1000 || threads < 1) {
            System.out.println("! WRONG AMOUNT ! TRY AGAIN");
            chooseThreads();
        }
        return threads;
    }
}
