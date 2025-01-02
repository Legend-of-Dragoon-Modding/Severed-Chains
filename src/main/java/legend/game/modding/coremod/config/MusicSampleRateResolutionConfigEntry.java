package legend.game.modding.coremod.config;

import legend.core.audio.SampleRateResolution;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;

public class MusicSampleRateResolutionConfigEntry extends ConfigEntry<SampleRateResolution> {
  public MusicSampleRateResolutionConfigEntry() {
    super(SampleRateResolution.Quadruple, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicSampleRateResolutionConfigEntry::serializer, MusicSampleRateResolutionConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, SampleRateResolution.values()[index]));

      for(final SampleRateResolution sampleRateResolution : SampleRateResolution.values()) {
        dropdown.addOption(sampleRateResolution.name());

        if(sampleRateResolution == CONFIG.getConfig(this)) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final SampleRateResolution oldValue, final SampleRateResolution newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.changeSampleRateResolution(newValue);
  }

  private static byte[] serializer(final SampleRateResolution sampleRateResolution) {
    return new byte[] {(byte)Integer.numberOfTrailingZeros(sampleRateResolution.value)};
  }

  private static SampleRateResolution deserializer(final byte[] data) {
    if(data.length == 1) {

      for(final SampleRateResolution sampleRateResolution : SampleRateResolution.values()) {
        if(sampleRateResolution.value == (1 << data[0])) {
          return sampleRateResolution;
        }
      }
    }

    return SampleRateResolution.Quadruple;
  }
}
