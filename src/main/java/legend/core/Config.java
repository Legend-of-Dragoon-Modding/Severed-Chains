package legend.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public final class Config {
  private Config() { }

  private static final Path path = Paths.get(".", "config.conf");
  private static final Properties properties = new Properties();

  static {
    properties.setProperty("window_width", "320");
    properties.setProperty("window_height", "240");
    properties.setProperty("controller_config", "false");
    properties.setProperty("controller_guid", "");
    properties.setProperty("controller_deadzone", "0.3");
  }

  public static int windowWidth() {
    return readInt("window_width", 320, 1, Integer.MAX_VALUE);
  }

  public static int windowHeight() {
    return readInt("window_height", 240, 1, Integer.MAX_VALUE);
  }

  public static boolean controllerConfig() {
    return readBool("controller_config", false);
  }

  public static void controllerConfig(final boolean config) {
    properties.setProperty("controller_config", String.valueOf(config));
  }

  public static String controllerGuid() {
    return properties.getProperty("controller_guid", "");
  }

  public static void controllerGuid(final String guid) {
    properties.setProperty("controller_guid", guid);
  }

  public static float controllerDeadzone() {
    return readFloat("controller_deadzone", 0.3f, 0.0f, 1.0f);
  }

  private static int readInt(final String key, final int defaultVal, final int min, final int max) {
    int val;
    try {
      val = Integer.parseInt(properties.getProperty(key, String.valueOf(defaultVal)));
    } catch(final NumberFormatException e) {
      val = defaultVal;
    }

    return legend.core.MathHelper.clamp(val, min, max);
  }

  private static float readFloat(final String key, final float defaultVal, final float min, final float max) {
    float val;
    try {
      val = Float.parseFloat(properties.getProperty(key, String.valueOf(defaultVal)));
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
  }

  public static void save() throws IOException {
    properties.store(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), "");
  }
}
