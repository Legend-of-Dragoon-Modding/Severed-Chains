package discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

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
    this.core.runCallbacks();
  }

  public void updateActivity() {
    this.core.activityManager().updateActivity(this.activity);
  }

  public void init() {
    final CreateParams params = new CreateParams();
    params.setClientID(1385814687458918400L); //App ID

    final long paramFlags = CreateParams.Flags.toLong(CreateParams.Flags.DEFAULT, CreateParams.Flags.SUPPRESS_EXCEPTIONS);
    params.setFlags(paramFlags);

    this.core = new Core(params);
    this.core.setLogHook(LogLevel.ERROR, (level, string) -> LOGGER.error(string));
    this.core.setLogHook(LogLevel.WARN, (level, string) -> LOGGER.warn(string));
    this.core.setLogHook(LogLevel.INFO, (level, string) -> LOGGER.info(string));
    this.core.setLogHook(LogLevel.DEBUG, (level, string) -> LOGGER.debug(string));
    this.core.setLogHook(LogLevel.VERBOSE, (level, string) -> LOGGER.trace(string));
  }

  public void destroy() {
    this.core.close();
  }
}
