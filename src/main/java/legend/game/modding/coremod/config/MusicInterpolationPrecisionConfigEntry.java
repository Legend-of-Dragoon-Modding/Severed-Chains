package legend.game.modding.coremod.config;

import legend.core.audio.InterpolationPrecision;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicInterpolationPrecisionConfigEntry extends EnumConfigEntry<InterpolationPrecision> {

  public MusicInterpolationPrecisionConfigEntry() {
    super(InterpolationPrecision.class, InterpolationPrecision.Double, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final InterpolationPrecision oldValue, final InterpolationPrecision newValue) {
    AUDIO_THREAD.changeInterpolationBitDepth(newValue);
  }
}
