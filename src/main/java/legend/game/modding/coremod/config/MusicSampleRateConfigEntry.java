package legend.game.modding.coremod.config;

import legend.core.audio.SampleRate;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import java.nio.ByteBuffer;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;

public class MusicSampleRateConfigEntry extends ConfigEntry<SampleRate> {

  public MusicSampleRateConfigEntry() {
    super(SampleRate._48000, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicSampleRateConfigEntry::serializer, MusicSampleRateConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, SampleRate.values()[index]));

      for(final SampleRate sampleRate : SampleRate.values()) {
        dropdown.addOption(sampleRate.description);

        if(sampleRate == CONFIG.getConfig(this)) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final SampleRate oldValue, final SampleRate newValue) {
    super.onChange(configCollection, oldValue, newValue);

    AUDIO_THREAD.changeSampleRate(newValue);
  }

  private static byte[] serializer(final SampleRate sampleRate) {
    return ByteBuffer.allocate(4).putInt(sampleRate.value).array();
  }

  private static SampleRate deserializer(final byte[] data) {
    if(data.length == 4) {
      final int value = ByteBuffer.wrap(data).getInt();

      for(final SampleRate sampleRate : SampleRate.values()) {
        if(sampleRate.value == value) {
          return sampleRate;
        }
      }
    }

    return SampleRate._48000;
  }
}
