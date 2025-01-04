package legend.core;

import legend.game.modding.coremod.CoreMod;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static legend.core.GameEngine.CONFIG;

public final class Config {
  private Config() {
  }

  private static final Path path = Paths.get(".", "config.conf");
  private static final SortedStoreProperties properties = new SortedStoreProperties();

  private static final Vector3f uiColour = new Vector3f();
  public static final Vector3f defaultUiColour = new Vector3f(0.0f, 0x29 / 255.0f, 0x9f / 255.0f);

  static {
    properties.setProperty("low_memory_unpacker", "false");
    properties.setProperty("window_width", "960");
    properties.setProperty("window_height", "720");
    properties.setProperty("battle_ui_colour_change", "false");
    properties.setProperty("battle_ui_r", "0");
    properties.setProperty("battle_ui_g", "41");
    properties.setProperty("battle_ui_b", "159");
    properties.setProperty("addition_overlay_colour_change", "false");
    properties.setProperty("addition_overlay_r", "72");
    properties.setProperty("addition_overlay_g", "96");
    properties.setProperty("addition_overlay_b", "255");
    properties.setProperty("counter_overlay_r", "216");
    properties.setProperty("counter_overlay_g", "96");
    properties.setProperty("counter_overlay_b", "32");
    properties.setProperty("game_speed_multiplier", "1");
    properties.setProperty("combat_stage", "false");
    properties.setProperty("combat_stage_id", "0");
    properties.setProperty("textbox_colour", "false");
    properties.setProperty("textbox_colour_mode", "0");
    properties.setProperty("textbox_colour1_r", "0");
    properties.setProperty("textbox_colour1_g", "0");
    properties.setProperty("textbox_colour1_b", "0");
    properties.setProperty("textbox_colour2_r", "0");
    properties.setProperty("textbox_colour2_g", "0");
    properties.setProperty("textbox_colour2_b", "0");
    properties.setProperty("textbox_colour3_r", "0");
    properties.setProperty("textbox_colour3_g", "0");
    properties.setProperty("textbox_colour3_b", "0");
    properties.setProperty("textbox_colour4_r", "0");
    properties.setProperty("textbox_colour4_g", "0");
    properties.setProperty("textbox_colour4_b", "0");
    properties.setProperty("textbox_colour5_r", "0");
    properties.setProperty("textbox_colour5_g", "0");
    properties.setProperty("textbox_colour5_b", "0");
    properties.setProperty("textbox_colour6_r", "0");
    properties.setProperty("textbox_colour6_g", "0");
    properties.setProperty("textbox_colour6_b", "0");
    properties.setProperty("textbox_colour7_r", "0");
    properties.setProperty("textbox_colour7_g", "0");
    properties.setProperty("textbox_colour7_b", "0");
    properties.setProperty("textbox_colour8_r", "0");
    properties.setProperty("textbox_colour8_g", "0");
    properties.setProperty("textbox_colour8_b", "0");
    properties.setProperty("textbox_transparency_mode", "0");
  }

  private static int gameSpeedMultiplier = 1;
  private static int loadedGameSpeedMultiplier = 1;

  public static boolean lowMemoryUnpacker() {
    return readBool("low_memory_unpacker", false);
  }

  public static void enableLowMemoryUnpacker() {
    properties.setProperty("low_memory_unpacker", "true");
  }

  public static int windowWidth() {
    return readInt("window_width", 640, 1, Integer.MAX_VALUE);
  }

  public static int windowHeight() {
    return readInt("window_height", 480, 1, Integer.MAX_VALUE);
  }

  public static boolean changeBattleRgb() {
    return readBool("battle_ui_colour_change", false);
  }

  public static void toggleBattleUiColour() {
    properties.setProperty("battle_ui_colour_change", String.valueOf(!changeBattleRgb()));
  }

  public static boolean changeAdditionOverlayRgb() {
    return readBool("addition_overlay_colour_change", false);
  }

  public static void toggleAdditionOverlayColour() {
    properties.setProperty("addition_overlay_colour_change", String.valueOf(!changeAdditionOverlayRgb()));
  }

  public static boolean combatStage() {
    return readBool("combat_stage", false);
  }

  public static void toggleCombatStage() {
    properties.setProperty("combat_stage", String.valueOf(!combatStage()));
  }

  public static int getCombatStage() {
    return readInt("combat_stage_id", 0, 1, 127);
  }

  public static void setCombatStage(final int id) {
    properties.setProperty("combat_stage_id", String.valueOf(id));
  }

  public static int getGameSpeedMultiplier() {
    return gameSpeedMultiplier;
  }

  public static void setGameSpeedMultiplier(final int multiplier) {
    gameSpeedMultiplier = multiplier;
    properties.setProperty("game_speed_multiplier", String.valueOf(multiplier));
  }

  public static int getLoadedGameSpeedMultiplier() {
    return loadedGameSpeedMultiplier;
  }

  public static void setLoadedGameSpeedMultiplier(final int multiplier) {
    loadedGameSpeedMultiplier = multiplier;
  }

  public static Vector3f getBattleRgb() {
    final int r = readInt("battle_ui_r", 0, 0, 255);
    final int g = readInt("battle_ui_g", 0, 0, 255);
    final int b = readInt("battle_ui_b", 0, 0, 255);

    return uiColour.set(r / 255.0f, g / 255.0f, b / 255.0f);
  }

  public static void setBattleRgb(final int rgb) {
    final int[] rgbArray = {
      ((rgb >> 16) & 0xff),
      ((rgb >> 8) & 0xff),
      (rgb & 0xff)
    };

    properties.setProperty("battle_ui_r", String.valueOf(rgbArray[2]));
    properties.setProperty("battle_ui_g", String.valueOf(rgbArray[1]));
    properties.setProperty("battle_ui_b", String.valueOf(rgbArray[0]));
    properties.setProperty("battle_ui_colour_change", "true");
  }

  public static int getAdditionOverlayRgb() {
    final int[] rgbArray = {
      readInt("addition_overlay_r", 72, 0, 255),
      readInt("addition_overlay_g", 96, 0, 255),
      readInt("addition_overlay_b", 255, 0, 255),
      0x00,
    };

    return (
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0]
    );
  }

  public static void setAdditionOverlayRgb(final int rgb) {
    final int[] rgbArray = {
      ((rgb >> 24) & 0xff),
      ((rgb >> 16) & 0xff),
      ((rgb >> 8) & 0xff),
      (rgb & 0xff)
    };

    properties.setProperty("addition_overlay_r", String.valueOf(rgbArray[3]));
    properties.setProperty("addition_overlay_g", String.valueOf(rgbArray[2]));
    properties.setProperty("addition_overlay_b", String.valueOf(rgbArray[1]));
    properties.setProperty("addition_overlay_colour_change", "true");
  }

  public static int getCounterOverlayRgb() {
    final int[] rgbArray = {
      readInt("counter_overlay_r", 216, 0, 255),
      readInt("counter_overlay_g", 96, 0, 255),
      readInt("counter_overlay_b", 32, 0, 255),
      0x00,
    };

    return (
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0]
    );
  }

  public static void setCounterOverlayRgb(final int rgb) {
    final int[] rgbArray = {
      ((rgb >> 24) & 0xff),
      ((rgb >> 16) & 0xff),
      ((rgb >> 8) & 0xff),
      (rgb & 0xff)
    };

    properties.setProperty("counter_overlay_r", String.valueOf(rgbArray[3]));
    properties.setProperty("counter_overlay_g", String.valueOf(rgbArray[2]));
    properties.setProperty("counter_overlay_b", String.valueOf(rgbArray[1]));
    properties.setProperty("addition_overlay_colour_change", "true");
  }

  public static boolean textBoxColour() {
    return readBool("textbox_colour", false);
  }

  public static void toggleTextBoxColour() {
    properties.setProperty("textbox_colour", String.valueOf(!textBoxColour()));
  }

  public static int getTextBoxColourMode() {
    return readInt("textbox_colour_mode", 0, 0, 2);
  }

  public static void setTextBoxColourMode(final int value) {
    properties.setProperty("textbox_colour_mode", String.valueOf(value));
  }

  public static void switchFullScreen() {
    final BoolConfigEntry fullScreenConfigEntry = CoreMod.FULLSCREEN_CONFIG.get();
    final boolean isFullScreen = CONFIG.getConfig(fullScreenConfigEntry);
    CONFIG.setConfig(fullScreenConfigEntry, !isFullScreen);
    ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
  }

  public static int getTextBoxRgb(final int textbox) {
    final int[] rgbArray = {
      readInt("textbox_colour" + textbox + "_r", 0, 0, 255),
      readInt("textbox_colour" + textbox + "_g", 0, 0, 255),
      readInt("textbox_colour" + textbox + "_b", 0, 0, 255),
      0x00,
    };

    return (
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8  |
        0xff & rgbArray[0]
    );
  }

  public static void setTextBoxRgb(final int textbox, final int rgb) {
    final int[] rgbArray = {
      ((rgb >> 24) & 0xff),
      ((rgb >> 16) & 0xff),
      ((rgb >> 8) & 0xff),
      (rgb & 0xff)
    };

    properties.setProperty("textbox_colour" + textbox + "_r", String.valueOf(rgbArray[3]));
    properties.setProperty("textbox_colour" + textbox + "_g", String.valueOf(rgbArray[2]));
    properties.setProperty("textbox_colour" + textbox + "_b", String.valueOf(rgbArray[1]));
  }

  public static int getTextBoxTransparencyMode() {
    return readInt("textbox_transparency_mode", 0, 0, 10);
  }

  public static void setTextBoxTransparencyMode(final int value) {
    properties.setProperty("textbox_transparency_mode", String.valueOf(value));
  }

  private static int readInt(final String key, final int defaultVal, final int min, final int max) {
    int val;
    try {
      val = Integer.parseInt(properties.getProperty(key, String.valueOf(defaultVal)));
    } catch(final NumberFormatException e) {
      val = defaultVal;
    }

    return MathHelper.clamp(val, min, max);
  }

  private static boolean readBool(final String key, final boolean defaultVal) {
    if("true".equals(properties.getProperty(key))) {
      return true;
    }

    if("false".equals(properties.getProperty(key))) {
      return false;
    }

    return defaultVal;
  }

  public static boolean exists() {
    return Files.exists(path);
  }

  public static void load() throws IOException {
    properties.load(Files.newInputStream(path, StandardOpenOption.READ));
    gameSpeedMultiplier = readInt("game_speed_multiplier", 1, 1, 16);
    loadedGameSpeedMultiplier = gameSpeedMultiplier;
  }

  public static void save() throws IOException {
    properties.store(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), "");
  }

  private static class SortedStoreProperties extends Properties {
    @Override
    public void store(final OutputStream out, final String comments) throws IOException {
      final Properties sortedProps = new Properties() {
        @Override
        public Set<Map.Entry<Object, Object>> entrySet() {
          final Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<>(Comparator.comparing(o -> o.getKey().toString()));
          sortedSet.addAll(super.entrySet());
          return sortedSet;
        }

        @Override
        public Set<Object> keySet() {
          return new TreeSet<>(super.keySet());
        }

        @Override
        public synchronized Enumeration<Object> keys() {
          return Collections.enumeration(new TreeSet<>(super.keySet()));
        }
      };

      sortedProps.putAll(this);
      sortedProps.store(out, comments);
    }
  }
}
