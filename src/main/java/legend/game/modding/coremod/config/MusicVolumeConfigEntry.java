package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCollection;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicVolumeConfigEntry extends VolumeConfigEntry {
  @Override
  public void onChange(final ConfigCollection configCollection, final Float oldValue, final Float newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.getSequencer().setVolume(newValue);
  }
}
