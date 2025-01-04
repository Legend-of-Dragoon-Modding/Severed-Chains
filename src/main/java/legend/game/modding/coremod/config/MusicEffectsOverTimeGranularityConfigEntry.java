package legend.game.modding.coremod.config;

import legend.core.audio.EffectsOverTimeGranularity;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicEffectsOverTimeGranularityConfigEntry extends EnumConfigEntry<EffectsOverTimeGranularity> {
  public MusicEffectsOverTimeGranularityConfigEntry() {
    super(EffectsOverTimeGranularity.class, EffectsOverTimeGranularity.Finer, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final EffectsOverTimeGranularity oldValue, final EffectsOverTimeGranularity newValue) {
    AUDIO_THREAD.changeEffectsOverTimeGranularity(newValue);
  }
}
