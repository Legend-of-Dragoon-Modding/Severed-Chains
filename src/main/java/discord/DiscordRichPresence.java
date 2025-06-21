package discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import legend.game.EngineStateEnum;
import legend.game.fmv.Fmv;
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

  private static final Logger LOGGER = LogManager.getFormatterLogger(DiscordRichPresence.class);
  private static Thread thread;
  private static Core core;
  private static Activity activity;

  private DiscordRichPresence() {
  }

  public static void start() {
    thread = new Thread(() -> {
      try {
        final CreateParams params = new CreateParams();
        params.setClientID(1385814687458918400L); //App ID
        params.setFlags(CreateParams.getDefaultFlags());

        core = new Core(params);
        core.setLogHook(LogLevel.INFO, DiscordRichPresence::LogCallback);
        core.setLogHook(LogLevel.WARN, DiscordRichPresence::LogCallback);
        core.setLogHook(LogLevel.ERROR, DiscordRichPresence::LogCallback);
        core.setLogHook(LogLevel.VERBOSE, DiscordRichPresence::LogCallback);

        startActivity();
      } catch(final Exception ex) {
        LOGGER.error(ex);
        return;
      }

      try {
        while(true) {
          updateActivity();
          core.runCallbacks();
          Thread.sleep(1000 * 30); //Refreshes every 30 seconds
        }
      } catch(final InterruptedException ex) {
        LOGGER.info("Terminating Discord thread...");
      } catch(final Exception ex) {
        LOGGER.error(ex);
      }
    });

    thread.start();
  }

  public static void stop() {
    if(thread != null) {
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
    activity.assets().setLargeImage("https://legendofdragoon.org/discord-int/disco.png");
  }

  public static void updateActivity() {
    if(gameState_800babc8 != null) {
      activity.setDetails(getChapter() + " - " + getSubmap());
      activity.setState(getStatus());
      activity.party().size().setCurrentSize(getParty());
      activity.party().size().setMaxSize(3);
    } else if(engineState_8004dd20 == EngineStateEnum.CREDITS_04) {
      activity.setDetails("Credits");
      activity.setState(getStatus());
      activity.party().size().setCurrentSize(getParty());
      activity.party().size().setMaxSize(activity.party().size().getCurrentSize() > 0 ? 3 : 0);
    }
    else if(engineState_8004dd20 == EngineStateEnum.GAME_OVER_07) {
      activity.setDetails("Game Over");
      activity.setState(getStatus());
      activity.party().size().setCurrentSize(getParty());
      activity.party().size().setMaxSize(activity.party().size().getCurrentSize() > 0 ? 3 : 0);
    }
    else {
      activity.setDetails("Main Menu");
      activity.setState(getStatus());
      activity.party().size().setCurrentSize(0);
      activity.party().size().setMaxSize(0);
    }

    core.activityManager().updateActivity(activity);
  }

  private static String getStatus() {
    if(Fmv.isPlaying || engineState_8004dd20 == EngineStateEnum.FMV_09 || engineState_8004dd20 == EngineStateEnum.FINAL_FMV_11) {
      return "Cutscene";
    }
    if(engineState_8004dd20 == EngineStateEnum.PRELOAD_00 || engineState_8004dd20 == EngineStateEnum.TITLE_02 || engineState_8004dd20 == EngineStateEnum.CREDITS_04 || engineState_8004dd20 == EngineStateEnum.GAME_OVER_07) {
      return null;
    }
    if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
      return "Combat";
    }
    return "Exploring";
  }

  private static String getChapter() {
    if(gameState_800babc8 != null && gameState_800babc8.chapterIndex_98 > -1 && gameState_800babc8.chapterIndex_98 < chapterNames_80114248.length) {
      return chapterNames_80114248[gameState_800babc8.chapterIndex_98];
    }
    return "Unknown Chapter";
  }

  private static String getSubmap() {
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05 && submapId_800bd808 > -1 && submapId_800bd808 < submapNames_8011c108.length) {
      return submapNames_8011c108[submapId_800bd808];
    }
    if(continentIndex_800bf0b0 > -1 && continentIndex_800bf0b0 < worldMapNames_8011c1ec.length) {
      return worldMapNames_8011c1ec[continentIndex_800bf0b0];
    }
    return "Unknown Location";
  }

  private static int getParty() {
    int partyCount = 0;
    if(gameState_800babc8 != null) {
      partyCount += gameState_800babc8.charIds_88[0] != -1 ? 1 : 0;
      partyCount += gameState_800babc8.charIds_88[1] != -1 ? 1 : 0;
      partyCount += gameState_800babc8.charIds_88[2] != -1 ? 1 : 0;
      if(partyCount > 0 && gameState_800babc8.charIds_88[0] == gameState_800babc8.charIds_88[1] && gameState_800babc8.charIds_88[1] == gameState_800babc8.charIds_88[2]) {
        partyCount = 1; //When starting a new game all slots are assigned 0 (Dart) for some reason
      }
    }
    return partyCount;
  }
}
