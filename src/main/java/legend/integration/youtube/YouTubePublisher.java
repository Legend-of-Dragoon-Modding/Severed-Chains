package legend.integration.youtube;

import com.google.gson.Gson;
import legend.integration.core.LoDPlays;
import legend.integration.core.PlayInput;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Flow;

public class YouTubePublisher implements LoDPlays {
    private final String liveChatId;
    private static final String baseUrl = "https://www.googleapis.com/youtube/v3/liveChat/messages";
    List<Flow.Subscriber<? super PlayInput>> subscriptions;
    private final Thread runner = new Thread(this);
    private static final Logger LOGGER = LogManager.getFormatterLogger(YouTubePublisher.class);
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private String pageToken;

    private final static Object monitor = new Object();

    public YouTubePublisher(String liveChatId) {
        this.liveChatId = liveChatId;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super PlayInput> subscriber) {
        subscriptions.add(subscriber);
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (monitor) {
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder().addQueryParameter("liveChatId", this.liveChatId);
                    if(this.pageToken != null) {
                        urlBuilder.addQueryParameter("pageToken", this.pageToken);
                    }

                    Request request = new Request.Builder()
                            .url(urlBuilder.build())
                            .get()
                            .addHeader("Content-Type", "application/json")
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful() || response.body() == null)
                            throw new IOException("Unexpected code " + response);

                        final String json = response.body().string();
                        final LiveChatListResponse liveChat = this.gson.fromJson(json, LiveChatListResponse.class);

                        this.pageToken = liveChat.nextPageToken();

                        for (LiveChatMessage item : liveChat.items()) {
                            for (Flow.Subscriber<? super PlayInput> sub : this.subscriptions) {
                                PlayInput input = PlayInput.parse(item.snippet().textMessageDetails().messageText());
                                sub.onNext(input);
                            }
                        }

                        monitor.wait(liveChat.pollingIntervalMillis());
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e);
                return;
            }
        }
    }

    @Override
    public void play() {
        this.runner.start();
    }
}
