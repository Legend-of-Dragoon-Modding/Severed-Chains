package legend.game.modding.coremod.config;

import legend.core.audio.PitchResolution;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;

public class MusicPitchResolutionConfigEntry extends ConfigEntry<PitchResolution> {
  public MusicPitchResolutionConfigEntry() {
    super(PitchResolution.Quadruple, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicPitchResolutionConfigEntry::serializer, MusicPitchResolutionConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, PitchResolution.values()[index]));

      for(final PitchResolution pitchResolution : PitchResolution.values()) {
        dropdown.addOption(pitchResolution.name());

        if(pitchResolution == CONFIG.getConfig(this)) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final PitchResolution oldValue, final PitchResolution newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.changePitchResolution(newValue);
  }

  private static byte[] serializer(final PitchResolution pitchResolution) {
    return new byte[] {(byte)Integer.numberOfTrailingZeros(pitchResolution.value)};
  }

  private static PitchResolution deserializer(final byte[] data) {
    if(data.length == 1) {

      for(final PitchResolution pitchResolution : PitchResolution.values()) {
        if(pitchResolution.value == (1 << data[0])) {
          return pitchResolution;
        }
      }
    }

    return PitchResolution.Quadruple;
  }
}
