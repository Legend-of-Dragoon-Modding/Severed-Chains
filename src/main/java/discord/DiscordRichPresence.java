package discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import legend.core.GameEngine;
import legend.game.EngineStateEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;

public final class DiscordRichPresence {

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);
  private static Thread thread;
  private static CreateParams params;
  private static Core core;
  private static Activity activity;

  private DiscordRichPresence() {
  }

  public static void start() {

    thread = new Thread(() -> {
      params = new CreateParams();
      params.setClientID(1383897032212611112L);
      params.setFlags(CreateParams.getDefaultFlags());

      try {
        core = new Core(params);
        core.setLogHook(LogLevel.VERBOSE, DiscordRichPresence::LogCallback);

        startActivity();

        try {
          while(true) {
            updateActivity();
            core.runCallbacks();
            Thread.sleep(1000 * 60); //Refreshes every minute
          }
        } catch (final InterruptedException ex) {
          LOGGER.info("Terminating Discord thread...");
        }
      } catch(final Exception ex) {
        LOGGER.error(ex);
      }
    });

    thread.start();
  }

  public static void stop() {
    if (thread != null) {
      try{
        thread.interrupt();
      } catch(final Exception ex) {
        LOGGER.error(ex);
      }
    }
  }

  private static void LogCallback(final LogLevel level, final String message) {
    if(level == LogLevel.ERROR) {
      LOGGER.error(message);
    } else if(level == LogLevel.WARN) {
      LOGGER.warn(message);
    } else if(level == LogLevel.INFO) {
      LOGGER.info(message);
    }
  }

  private static void startActivity() {
    activity = new Activity();
    activity.timestamps().setStart(Instant.now());
    activity.assets().setLargeImage("https://assets-prd.ignimgs.com/2022/03/02/the-legend-of-dragoon-1646184664725.jpg");
  }

  public static void updateActivity() {
    if(gameState_800babc8 != null && engineState_8004dd20 != EngineStateEnum.TITLE_02) {
      int partyCount = 0;
      partyCount += gameState_800babc8.charIds_88[0] != -1 ? 1 : 0;
      partyCount += gameState_800babc8.charIds_88[1] != -1 ? 1 : 0;
      partyCount += gameState_800babc8.charIds_88[2] != -1 ? 1 : 0;

      activity.party().size().setCurrentSize(partyCount);
      activity.party().size().setMaxSize(3);
      activity.setDetails(chapterNames_80114248[gameState_800babc8.chapterIndex_98]);
      activity.setState(engineState_8004dd20 == EngineStateEnum.SUBMAP_05 ? submapNames_8011c108[submapId_800bd808] : worldMapNames_8011c1ec[continentIndex_800bf0b0]);
    } else {
      activity.setDetails("Main Menu");
      activity.party().size().setCurrentSize(0);
      activity.party().size().setMaxSize(0);
    }

    core.activityManager().updateActivity(activity);
  }
}
