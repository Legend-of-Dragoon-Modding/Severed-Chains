package legend.game.modding.coremod.config;

import legend.core.audio.PitchResolution;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicPitchResolutionConfigEntry extends EnumConfigEntry<PitchResolution> {
  public MusicPitchResolutionConfigEntry() {
    super(PitchResolution.class, PitchResolution.Quadruple, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final PitchResolution oldValue, final PitchResolution newValue) {
    AUDIO_THREAD.changePitchResolution(newValue);
  }
}
