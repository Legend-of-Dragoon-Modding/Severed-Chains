package legend.game;

import legend.core.GameEngine;
import legend.core.Version;
import legend.core.platform.input.InputCodepoints;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SaveFailedException;
import legend.game.saves.SavedGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.SAVES;
import static legend.game.Scus94491BpeSegment_8002.charWidth;
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

  private static JSONObject glyph(final char c, final boolean colour) {
    final int i = switch(c) {
      case InputCodepoints.DPAD_UP -> 224;
      case InputCodepoints.DPAD_DOWN -> 225;
      case InputCodepoints.DPAD_LEFT -> 226;
      case InputCodepoints.DPAD_RIGHT -> 227;
      case InputCodepoints.SELECT-> 245;
      case InputCodepoints.START -> 244;
      case InputCodepoints.LEFT_BUMPER -> 230;
      case InputCodepoints.LEFT_TRIGGER -> 231;
      case InputCodepoints.LEFT_STICK -> 232;
      case InputCodepoints.RIGHT_BUMPER -> 233;
      case InputCodepoints.RIGHT_TRIGGER -> 234;
      case InputCodepoints.RIGHT_STICK -> 235;
      case InputCodepoints.LEFT_AXIS_X -> 241;
      case InputCodepoints.LEFT_AXIS_Y -> 240;
      case InputCodepoints.RIGHT_AXIS_X -> 243;
      case InputCodepoints.RIGHT_AXIS_Y -> 242;
      case InputCodepoints.A -> 236;
      case InputCodepoints.B -> 237;
      case InputCodepoints.X -> 238;
      case InputCodepoints.Y -> 239;
      case InputCodepoints.XBOX_BUTTON_BACK -> 245; //TODO uses select
      case InputCodepoints.XBOX_BUTTON_MENU -> 228;
      case InputCodepoints.XBOX_BUTTON_VIEW -> 229;
      case InputCodepoints.PS_BUTTON_CROSS -> 252;
      case InputCodepoints.PS_BUTTON_CIRCLE -> 253;
      case InputCodepoints.PS_BUTTON_SQUARE -> 254;
      case InputCodepoints.PS_BUTTON_TRIANGLE -> 255;
      default -> c - '!';
    };

    final int texU = i % 16 * 16;
    final int texV = i / 16 * 16;
    final int texW = charWidth(c);
    final int texH = 16;
    final int w = charWidth(c);
    final int h = 16;

    final JSONObject glyph = new JSONObject();
    glyph.put("texU", texU);
    glyph.put("texV", texV);
    glyph.put("texW", texW);
    glyph.put("texH", texH);
    glyph.put("x", 0);
    glyph.put("y", 0);
    glyph.put("w", w);
    glyph.put("h", h);
    glyph.put("colour", colour);

    return glyph;
  }

  public static void main(final String[] args) {
    final JSONObject glyphs = new JSONObject();

    final JSONObject space = new JSONObject();
    space.put("texU", 0);
    space.put("texV", 0);
    space.put("texW", 8);
    space.put("texH", 0);
    space.put("x", 0);
    space.put("y", 0);
    space.put("w", 8);
    space.put("h", 16);
    space.put("colour", false);

    glyphs.put(" ", space);

    for(char c = '!'; c <= '~'; c++) {
      glyphs.put(Character.toString(c), glyph(c, true));
    }

    glyphs.put("×", glyph('×', true));
    glyphs.put("·", glyph('·', true));

    for(char c = InputCodepoints.DPAD_UP; c <= InputCodepoints.RIGHT_AXIS_Y; c++) {
      glyphs.put("%#x".formatted((int)c), glyph(c, false));
    }

    glyphs.put("0xe100", glyph(InputCodepoints.XBOX_BUTTON_BACK, false));
    glyphs.put("0xe101", glyph(InputCodepoints.XBOX_BUTTON_MENU, false));
    glyphs.put("0xe102", glyph(InputCodepoints.XBOX_BUTTON_VIEW, false));

    glyphs.put("0xe200", glyph(InputCodepoints.PS_BUTTON_CROSS, false));
    glyphs.put("0xe201", glyph(InputCodepoints.PS_BUTTON_CIRCLE, false));
    glyphs.put("0xe202", glyph(InputCodepoints.PS_BUTTON_SQUARE, false));
    glyphs.put("0xe203", glyph(InputCodepoints.PS_BUTTON_TRIANGLE, false));

    final JSONObject font = new JSONObject();
    font.put("name", "Smooth");
    font.put("glyphs", glyphs);

    try {
      final Path path = Path.of("./gfx/fonts/default.json");
      Files.deleteIfExists(path);
      Files.writeString(path, font.toString(2));
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    System.exit(0);

    try {
      LOGGER.info("Initialising LWJGL version %s", org.lwjgl.Version.getVersion());
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

      LogManager.shutdown();
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
