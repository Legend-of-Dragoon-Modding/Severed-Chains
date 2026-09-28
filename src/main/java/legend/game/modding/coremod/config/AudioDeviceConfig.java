package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.core.audio.AudioThread;
import legend.core.lang.RawText;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import java.util.List;

import static legend.core.GameEngine.AUDIO_THREAD;

public class AudioDeviceConfig extends ConfigEntry<String> {
  private List<String> oldDevices;

  public AudioDeviceConfig() {
    super("", ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, AudioDeviceConfig::serialize, bytes -> deserialize(bytes, ""));

    this.setEditControl((current, config) -> {
      this.oldDevices = AudioThread.getDevices();

      final Dropdown<String> dropdown = new Dropdown<>((i, s) -> new RawText(s.replace("OpenAL Soft on ", ""))) {
        private long lastUpdate = System.nanoTime();

        @Override
        protected void render(final int x, final int y) {
          if(System.nanoTime() - this.lastUpdate >= 1_000_000_000L) {
            this.lastUpdate = System.nanoTime();
            final List<String> newDevices = AudioThread.getDevices();

            if(!AudioDeviceConfig.this.oldDevices.equals(newDevices)) {
              AudioDeviceConfig.this.updateDropdownOptions(this, newDevices, config);
              AudioDeviceConfig.this.oldDevices = newDevices;
            }
          }

          super.render(x, y);
        }
      };

      dropdown.onSelection(index -> config.setConfig(this, index == 0 ? "" : dropdown.getSelectedOption()));
      this.updateDropdownOptions(dropdown, this.oldDevices, config);

      return dropdown;
    });
  }

  private void updateDropdownOptions(final Dropdown<String> dropdown, final List<String> devices, final ConfigCollection config) {
    dropdown.clearOptions();
    dropdown.addOption("<default>");

    for(final String device : devices) {
      dropdown.addOption(device);

      if(device.equals(config.getConfig(this))) {
        dropdown.setSelectedIndex(dropdown.size() - 1);
      }
    }
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final String oldValue, final String newValue) {
    super.onChange(configCollection, oldValue, newValue);

    if(!oldValue.equals(newValue)) {
      AUDIO_THREAD.reinit();
    }
  }

  private static byte[] serialize(final String val) {
    return IoHelper.stringToBytes(val, 2);
  }

  private static String deserialize(final byte[] bytes, final String defaultValue) {
    return IoHelper.stringFromBytes(bytes, 2, defaultValue);
  }
}
