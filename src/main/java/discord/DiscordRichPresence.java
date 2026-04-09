package discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import legend.core.Async;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class DiscordRichPresence {
  private static final Logger LOGGER = LogManager.getFormatterLogger(DiscordRichPresence.class);

  private Core core;
  public final Activity activity;

  public DiscordRichPresence() {
    this.activity = new Activity();
    this.activity.timestamps().setStart(Instant.now());
    this.activity.assets().setLargeImage("https://legendofdragoon.org/discord-int/disco.png");
  }

  public void tick() {
    synchronized(this) {
      if(this.core != null) {
        try {
          this.core.runCallbacks();
        } catch(final GameSDKException ignored) { }
      }
    }
  }

  public void updateActivity() {
    synchronized(this) {
      if(this.core != null) {
        this.core.activityManager().updateActivity(this.activity);
      }
    }
  }

  public void init() {
    final CreateParams params = new CreateParams();
    params.setClientID(1385814687458918400L); //App ID

    final long paramFlags = CreateParams.Flags.toLong(CreateParams.Flags.NO_REQUIRE_DISCORD, CreateParams.Flags.SUPPRESS_EXCEPTIONS);
    params.setFlags(paramFlags);

    Async.run(() -> {
      final Core core = new Core(params);
      core.setLogHook(LogLevel.ERROR, (level, string) -> LOGGER.error(string));
      core.setLogHook(LogLevel.WARN, (level, string) -> LOGGER.warn(string));
      core.setLogHook(LogLevel.INFO, (level, string) -> LOGGER.info(string));
      core.setLogHook(LogLevel.DEBUG, (level, string) -> LOGGER.debug(string));
      core.setLogHook(LogLevel.VERBOSE, (level, string) -> LOGGER.trace(string));

      synchronized(this) {
        this.core = core;
      }
    })
      .orTimeout(10, TimeUnit.SECONDS)
      .exceptionally(t -> {
        LOGGER.warn("Failed to initialize Discord rich presence", t);
        return null;
      });
  }

  public void destroy() {
    synchronized(this) {
      if(this.core != null) {
        this.core.close();
        this.core = null;
      }
    }
  }
}
