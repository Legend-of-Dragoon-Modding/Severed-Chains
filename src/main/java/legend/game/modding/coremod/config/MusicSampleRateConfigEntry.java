package legend.game.modding.coremod.config;

import legend.core.audio.SampleRate;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicSampleRateConfigEntry extends EnumConfigEntry<SampleRate> {

  public MusicSampleRateConfigEntry() {
    super(SampleRate.class, SampleRate._48000, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final SampleRate oldValue, final SampleRate newValue) {
    AUDIO_THREAD.changeSampleRate(newValue);
  }
}
