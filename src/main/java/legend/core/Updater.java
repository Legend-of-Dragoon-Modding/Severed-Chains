package legend.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Updater {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Updater.class);

  private static final String UPDATE_URL = "https://api.github.com/repos/Legend-of-Dragoon-Modding/Severed-Chains/releases";

  private AsyncHttpClient client;

  private ListenableFuture<Response> activeCheck;

  public void delete() {
    try {
      if(this.client != null) {
        this.client.close();
      }
    } catch(final IOException e) {
      LOGGER.warn("Failed to shut down updater", e);
    }
  }

  public void check(final Consumer<Release> onComplete) {
    synchronized(this) {
      if(this.client == null) {
        try {
          this.client = asyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setConnectTimeout(Duration.ofSeconds(10)).setReadTimeout(Duration.ofSeconds(10)).build());
        } catch(final Throwable r) {
          LOGGER.error("Failed to initialize updater");
        }

        if(this.client == null) {
          onComplete.accept(null);
          return;
        }
      }

      if(this.activeCheck != null) {
        return;
      }

      //noinspection ConstantValue
      if(Version.TIMESTAMP == null) {
        // If the build timestamp is null, we're running in the IDE or using a custom build that was compiled manually
        LOGGER.info("Custom build, skipping update check");
        onComplete.accept(null);
        return;
      }

      LOGGER.info("Checking for updates...");
      this.activeCheck = this.get(UPDATE_URL, response -> this.onCheckComplete(response, onComplete));
    }
  }

  private void onCheckComplete(final Response response, final Consumer<Release> onComplete) {
    final Release release = this.parseReleases(new JSONArray(response.getResponseBody()))
      .stream()
      .filter(r -> r.prerelease == Version.PRERELEASE)
      .sorted()
      .filter(r -> r.timestamp.isAfter(Version.TIMESTAMP))
      .findFirst()
      .orElse(null);

    synchronized(this) {
      if(release != null) {
        LOGGER.info("Found new release %s", release);
        onComplete.accept(release);
      } else {
        LOGGER.info("No updates found");
        onComplete.accept(null);
      }

      this.activeCheck = null;
    }
  }

  private List<Release> parseReleases(final JSONArray releasesJson) {
    final List<Release> releases = new ArrayList<>();

    for(int releaseIndex = 0; releaseIndex < releasesJson.length(); releaseIndex++) {
      final JSONObject release = releasesJson.getJSONObject(releaseIndex);
      releases.add(new Release(release.getString("tag_name"), release.getString("html_url"), ZonedDateTime.parse(release.getString("published_at")), release.getBoolean("prerelease")));
    }

    return releases;
  }

  private ListenableFuture<Response> get(final String url, final Consumer<Response> listener) {
    return this.listen(this.client.prepareGet(url).execute(), listener);
  }

  private ListenableFuture<Response> listen(final ListenableFuture<Response> future, final Consumer<Response> listener) {
    future.addListener(() -> this.callResponse(this.getFuture(future), listener), null);
    return future;
  }

  private void callResponse(@Nullable final Response response, final Consumer<Response> listener) {
    if(response != null) {
      if(response.getStatusCode() / 100 != 2) {
        LOGGER.warn("Request to %s failed (%d): %s", response.getUri(), response.getStatusCode(), response.getStatusText());

        synchronized(this) {
          this.activeCheck = null;
        }

        return;
      }

      try {
        listener.accept(response);
      } catch(final Throwable t) {
        LOGGER.warn("Failed to check for updates", t);

        synchronized(this) {
          this.activeCheck = null;
        }
      }
    }
  }

  private <T> T getFuture(final Future<T> future) {
    try {
      return future.get();
    } catch(final InterruptedException | ExecutionException e) {
      LOGGER.warn("Failed to check for updates", e);

      synchronized(this) {
        this.activeCheck = null;
      }
    }

    return null;
  }

  public static class Release implements Comparable<Release> {
    public final String tag;
    public final String uri;
    public final ZonedDateTime timestamp;
    public final boolean prerelease;

    private Release(final String tag, final String uri, final ZonedDateTime timestamp, final boolean prerelease) {
      this.tag = tag;
      this.uri = uri;
      this.timestamp = timestamp;
      this.prerelease = prerelease;
    }

    @Override
    public int compareTo(@NotNull final Updater.Release o) {
      return -this.timestamp.compareTo(o.timestamp);
    }

    @Override
    public String toString() {
      return this.tag + ' ' + this.timestamp + (this.prerelease ? " (prerelease)" : "");
    }
  }
}
