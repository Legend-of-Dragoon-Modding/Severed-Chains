package legend.game;

import legend.core.GameEngine;
import legend.core.Version;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SaveFailedException;
import legend.game.saves.SavedGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.SAVES;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.lastSavableEngineState;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCutForSave_800cb450;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public final class Main {
  public static final Locale ORIGINAL_LOCALE = Locale.getDefault();

  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");

    Locale.setDefault(Locale.US);
  }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Main.class);

  private Main() { }

  public static void main(final String[] args) {
    try {
      GameEngine.start();
    } catch(final Throwable e) {
      boolean generatedCrashSave = false;

      if(gameState_800babc8 != null && CONFIG.getConfig(CoreMod.CREATE_CRASH_SAVE_CONFIG.get())) {
        final List<SavedGame> saves = gameState_800babc8.campaign.loadAllSaves();
        final String name = SAVES.generateSaveName(saves, "Crash Recovery");

        gameState_800babc8.submapScene_a4 = collidedPrimitiveIndex_80052c38;
        gameState_800babc8.submapCut_a8 = submapCutForSave_800cb450;
        engineState_8004dd20 = lastSavableEngineState;
        gameState_800babc8.isOnWorldMap_4e4 = engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08;

        try {
          SAVES.newSave(name, gameState_800babc8, stats_800be5f8);
          generatedCrashSave = true;
        } catch(final SaveFailedException ex) {
          LOGGER.error("Failed to generate crash recovery save :(", ex);
        }
      }

      final List<String> messages = new ArrayList<>();
      getExceptionMessages(e, messages);

      LOGGER.error("----------------------------------------------------------------------------");
      LOGGER.error("Crash detected");
      LOGGER.error("Severed Chains %s commit %s built %s", Version.FULL_VERSION, Version.HASH, Version.TIMESTAMP);

      if(generatedCrashSave) {
        LOGGER.error("We have attempted to generate a recovery save. You can load it next time you run Severed Chains.");
      }

      LOGGER.error("Please copy this crash log and send it to us in the Player Help channel in the Legend of Dragoon Discord server.");
      LOGGER.error("https://discord.gg/legendofdragoon");

      for(final String message : messages) {
        LOGGER.error("Message: %s", message);
      }

      LOGGER.error("----------------------------------------------------------------------------");
      LOGGER.error("Stack trace:", e);

      LOGGER.error("Please copy this crash log and send it to us in the Player Help channel in the Legend of Dragoon Discord server.");
      LOGGER.error("https://discord.gg/legendofdragoon");

      System.exit(1);
    }
  }

  private static void getExceptionMessages(final Throwable e, final List<String> messages) {
    if(e.getMessage() != null) {
      messages.add(e.getMessage());
    }

    if(e.getCause() != null) {
      getExceptionMessages(e.getCause(), messages);
    }
  }
}
