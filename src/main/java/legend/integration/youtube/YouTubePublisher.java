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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class YouTubePublisher implements LoDPlays {
  private static final Logger LOGGER = LogManager.getFormatterLogger(YouTubePublisher.class);

  private static final String baseUrl = "https://www.googleapis.com/youtube/v3/liveChat/messages";
  private final static Object monitor = new Object();

  private final String liveChatId;
  private final String apiKey;
  private final List<Flow.Subscriber<? super PlayInput>> subscriptions = new ArrayList<>();
  private final Thread runner = new Thread(this);
  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  private String pageToken;

  public YouTubePublisher(final String liveChatId, final String apiKey) {
    this.liveChatId = liveChatId;
    this.apiKey = apiKey;
  }

  @Override
  public void subscribe(final Flow.Subscriber<? super PlayInput> subscriber) {
    subscriptions.add(subscriber);
  }

  @Override
  public void run() {
    while(true) {
      try {
        synchronized(monitor) {
          final HttpUrl.Builder urlBuilder = HttpUrl
            .parse(baseUrl)
            .newBuilder()
            .addQueryParameter("liveChatId", this.liveChatId)
            .addQueryParameter("key", this.apiKey);

          if(this.pageToken != null) {
            urlBuilder.addQueryParameter("pageToken", this.pageToken);
          }

          final Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .addHeader("Content-Type", "application/json")
            .build();

          try(final Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful() || response.body() == null) {
              throw new IOException("Unexpected code " + response);
            }

            final String json = response.body().string();
            final LiveChatListResponse liveChat = this.gson.fromJson(json, LiveChatListResponse.class);

            this.pageToken = liveChat.nextPageToken();

            for(final LiveChatMessage item : liveChat.items()) {
              for(final Flow.Subscriber<? super PlayInput> sub : this.subscriptions) {
                final PlayInput input = PlayInput.parse(item.snippet().textMessageDetails().messageText());
                sub.onNext(input);
              }
            }

            monitor.wait(liveChat.pollingIntervalMillis());
          }
        }
      } catch(final Exception e) {
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
