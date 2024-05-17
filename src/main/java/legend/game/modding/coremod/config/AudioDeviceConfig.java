package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.core.audio.AudioThread;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.CONFIG;

public class AudioDeviceConfig extends ConfigEntry<String> {
  public AudioDeviceConfig() {
    super("", ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, AudioDeviceConfig::serialize, bytes -> deserialize(bytes, ""));

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, dropdown.getSelectedOption()));
      dropdown.addOption("<default>");

      for(final String device : AudioThread.getDevices()) {
        dropdown.addOption(device);

        if(device.equals(CONFIG.getConfig(this))) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  private static byte[] serialize(final String val) {
    return IoHelper.stringToBytes(val, 2);
  }

  private static String deserialize(final byte[] bytes, final String defaultValue) {
    return IoHelper.stringFromBytes(bytes, 2, defaultValue);
  }
}
