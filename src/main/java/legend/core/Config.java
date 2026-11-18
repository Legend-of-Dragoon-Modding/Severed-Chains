package legend.core;

import legend.game.modding.coremod.CoreMod;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;

import java.io.IOException;
import java.io.InputStream;
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

  static {
    properties.setProperty("low_memory_unpacker", "false");
    properties.setProperty("last_config_preset", "");
    properties.setProperty("window_width", "960");
    properties.setProperty("window_height", "720");
    properties.setProperty("game_speed_multiplier", "1");
    properties.setProperty("combat_stage", "false");
    properties.setProperty("combat_stage_id", "0");
  }

  private static int gameSpeedMultiplier = 1;
  private static int loadedGameSpeedMultiplier = 1;

  public static boolean lowMemoryUnpacker() {
    return readBool("low_memory_unpacker", false);
  }

  public static void enableLowMemoryUnpacker() {
    properties.setProperty("low_memory_unpacker", "true");
  }

  public static String getLastConfigPreset() {
    return properties.getProperty("last_config_preset");
  }

  public static void setLastConfigPreset(final String preset) {
    properties.setProperty("last_config_preset", preset);
  }

  public static int windowWidth() {
    return readInt("window_width", 640, 1, Integer.MAX_VALUE);
  }

  public static int windowHeight() {
    return readInt("window_height", 480, 1, Integer.MAX_VALUE);
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

  public static void switchFullScreen() {
    final BoolConfigEntry fullScreenConfigEntry = CoreMod.FULLSCREEN_CONFIG.get();
    final boolean isFullScreen = CONFIG.getConfig(fullScreenConfigEntry);
    CONFIG.setConfig(fullScreenConfigEntry, !isFullScreen);
    ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
  }

  private static int readInt(final String key, final int defaultVal, final int min, final int max) {
    int val;
    try {
      val = Integer.parseInt(properties.getProperty(key, String.valueOf(defaultVal)));
    } catch(final NumberFormatException e) {
      val = defaultVal;
    }

    return Math.clamp(val, min, max);
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
    try(final InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
      properties.load(stream);
    }

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
