package twitch;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ViewBot extends Checkable {
    public final HttpClient client = HttpClient.newHttpClient();
    private final String target;

    private Queue<String> proxyList = new ArrayBlockingQueue<>(10000);
    private final Queue<String> fullProxyList = new ArrayBlockingQueue<>(10000);

    //private final File proxyFile;
    private final String clientId = "kimne78kx3ncx6brgo4mv6wki5h1ko";

    public ViewBot(File fileIn, int threads, String target) {
        super(fileIn, threads);
        this.target = target;
        loadProxy();
    }

    private void loadProxy() {
        try (Scanner sc = new Scanner(super.fileIn, "UTF-8")) {
            while (sc.hasNext()) {
                fullProxyList.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File can not be found: " + e);
        }
        proxyList = fullProxyList;
    }

    public void start() {
        while (!super.executor.isTerminated()) {
            super.executor.execute(() -> {
                if (proxyList.size() == 0) proxyList = fullProxyList;
                String[] proxy = proxyList.poll().split(":");
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .proxy(ProxySelector.of(new InetSocketAddress(proxy[0], Integer.parseInt(proxy[1]))))
                        .build();
                String[] info = getInfo(client);
                if (info.length == 0) {
                    System.out.println("Bad Proxy. Continuing...");
                    return;
                }
                String token = info[0];
                String sig = info[1];
                System.out.println("token: " + token + "\n" + "sig: " + sig);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendView(client, token, sig);
            });
        }
    }

    private boolean sendView(HttpClient client, String token, String sig) {
        String url = String.format("https://usher.ttvnw.net/api/channel/hls/%s.m3u8?allow_source=true&segment_preference=4&sig=%s&token=%s", target, sig, token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response == null) return false;
        System.out.println("\nresponse: " + response.body());
        return true;
    }

    private String[] getInfo(HttpClient client) {
        String[] resultArray = new String[2];
        String url = String.format("https://api.twitch.tv/api/channels/" +
                "%s/access_token?need_https=true&oauth_token=&" +
                "platform=web&player_backend=mediaplayer&player_type=site&client_id=%s", target, clientId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                .setHeader("Accept", "application/vnd.twitchtv.v5+json; charset=UTF-8")
                .setHeader("Content-Type","application/json; charset=UTF-8")
                .setHeader("Accept-Language", "en-us")
                .setHeader("Referer", "https://www.twitch.tv/" + target)
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new String[0];
        }
        if (response == null) return new String[0];
        JSONObject jsonObject = new JSONObject(response.body());
        try {
            resultArray[0] = URLEncoder.encode(jsonObject.getString("token").replace("\\", ""), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resultArray[1] = jsonObject.getString("sig").replace("\\", "");
        return resultArray;
    }
}
