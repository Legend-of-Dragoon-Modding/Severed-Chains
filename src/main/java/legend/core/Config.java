package legend.core;

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

import static legend.game.Scus94491BpeSegment_800b.inputDpadButtonsActionState_800bee90;

public final class Config {
  private Config() { }

  private static final Path path = Paths.get(".", "config.conf");
  private static final SortedStoreProperties properties = new SortedStoreProperties();

  static {
    properties.setProperty("window_width", "320");
    properties.setProperty("window_height", "240");
    properties.setProperty("controller_config", "false");
    properties.setProperty("controller_guid", "");
    properties.setProperty("controller_deadzone", "0.3");
    properties.setProperty("inventory_size", "32");
    properties.setProperty("unlock_party", "false");
    properties.setProperty("battle_ui_colour_change", "false");
    properties.setProperty("battle_ui_r", "0");
    properties.setProperty("battle_ui_g", "41");
    properties.setProperty("battle_ui_b", "159");
    properties.setProperty("save_anywhere", "false");
    properties.setProperty("auto_addition", "false");
    properties.setProperty("auto_dragoon_meter", "false");
    properties.setProperty("combat_stage", "false");
    properties.setProperty("combat_stage_id", "0");
    properties.setProperty("fast_text_speed", "false");
    properties.setProperty("auto_advance_text", "false");
    properties.setProperty("auto_charm_potion", "false");
    properties.setProperty("auto_run", "false");
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

  public static int inventorySize() {
    return readInt("inventory_size", 32, 1, 64);
  }

  public static boolean unlockParty() {
    return readBool("unlock_party", false);
  }

  public static boolean changeBattleRGB() {
    return readBool("battle_ui_colour_change", false);
  }

  public static void toggleBattleUIColour() {
    properties.setProperty("battle_ui_colour_change", String.valueOf(!changeBattleRGB()));
  }

  public static boolean saveAnywhere() {
    return readBool("save_anywhere", false);
  }

  public static void toggleSaveAnywhere() {
    properties.setProperty("save_anywhere", String.valueOf(!saveAnywhere()));
  }

  public static boolean autoAddition() {
    return readBool("auto_addition", false);
  }

  public static void toggleAutoAddition() {
    properties.setProperty("auto_addition", String.valueOf(!autoAddition()));
  }

  public static boolean autoDragoonMeter() {
    return readBool("auto_dragoon_meter", false);
  }

  public static void toggleAutoDragoonMeter() {
    properties.setProperty("auto_dragoon_meter", String.valueOf(!autoDragoonMeter()));
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

  public static void setCombatStage(int id) {
    properties.setProperty("combat_stage_id", String.valueOf(id));
  }

  public static  boolean fastTextSpeed() {
    return readBool("fast_text_speed", false);
  }

  public static void toggleFastText() {
    properties.setProperty("fast_text_speed", String.valueOf(!fastTextSpeed()));
  }

  public static boolean autoAdvanceText() {
    return readBool("auto_advance_text", false);
  }

  public static void toggleAutoAdvanceText() {
    properties.setProperty("auto_advance_text", String.valueOf(!autoAdvanceText()));
  }

  public static boolean autoCharmPotion() {
    return readBool("auto_charm_potion", false);
  }

  public static void toggleAutoCharmPotion() {
    properties.setProperty("auto_charm_potion", String.valueOf(!autoCharmPotion()));
  }

  public static boolean autoRun() {
    return readBool("auto_run", false);
  }

  public static void toggleAutoRun() {
    properties.setProperty("auto_run", String.valueOf(!autoRun()));
  }

  public static void autoRunHandleInput() {
    if(autoRun()) {
      inputDpadButtonsActionState_800bee90.or(0x40);
    }
  }

  public static int getBattleRGB() {
    int[] rgbArray = new int[] {
      readInt("battle_ui_r", 0, 0, 255),
      readInt("battle_ui_g", 0, 0, 255),
      readInt("battle_ui_b", 0, 0, 255),
      0x00,
    };

    return (
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8  |
        (0xff & rgbArray[0]) << 0
    );
  }

  public static void setBattleRGB(int rgb) {
    int[] rgbArray = new int[] {
      ((rgb >> 24) & 0xff),
      ((rgb >> 16) & 0xff),
      ((rgb >> 8)  & 0xff),
      ((rgb >> 0)  & 0xff)
    };

    properties.setProperty("battle_ui_r", String.valueOf(rgbArray[3]));
    properties.setProperty("battle_ui_g", String.valueOf(rgbArray[2]));
    properties.setProperty("battle_ui_b", String.valueOf(rgbArray[1]));
    properties.setProperty("battle_ui_colour_change", "true");
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


  static class SortedStoreProperties extends Properties {

    @Override
    public void store(final OutputStream out, final String comments) throws IOException {
      Properties sortedProps = new Properties() {
        @Override
        public Set<Map.Entry<Object, Object>> entrySet() {
          Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<Map.Entry<Object, Object>>(new Comparator<Map.Entry<Object, Object>>() {
            @Override
            public int compare(final Map.Entry<Object, Object> o1, final Map.Entry<Object, Object> o2) {
              return o1.getKey().toString().compareTo(o2.getKey().toString());
            }
          }
          );
          sortedSet.addAll(super.entrySet());
          return sortedSet;
        }

        @Override
        public Set<Object> keySet() {
          return new TreeSet<Object>(super.keySet());
        }

        @Override
        public synchronized Enumeration<Object> keys() {
          return Collections.enumeration(new TreeSet<Object>(super.keySet()));
        }

      };
      sortedProps.putAll(this);
      sortedProps.store(out, comments);
    }
  }
}
