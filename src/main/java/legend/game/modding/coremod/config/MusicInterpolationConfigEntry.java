package legend.game.modding.coremod.config;

import legend.core.audio.Interpolation;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicInterpolationConfigEntry extends EnumConfigEntry<Interpolation> {
  public MusicInterpolationConfigEntry() {
    super(Interpolation.class, Interpolation.Four, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Interpolation oldValue, final Interpolation newValue) {
    AUDIO_THREAD.changeInterpolation(newValue);
  }
}
