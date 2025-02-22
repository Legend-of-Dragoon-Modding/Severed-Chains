package legend.game.modding.coremod.config;

import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCollection;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.SPU;

public class MasterVolumeConfigEntry extends VolumeConfigEntry {
  @Override
  public void onChange(final ConfigCollection configCollection, final Float oldValue, final Float newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.getSequencer().setVolume(newValue * CONFIG.getConfig(CoreMod.MUSIC_VOLUME_CONFIG.get()));
    SPU.setPlayerVolume(newValue * CONFIG.getConfig(CoreMod.SFX_VOLUME_CONFIG.get()));
  }
}
