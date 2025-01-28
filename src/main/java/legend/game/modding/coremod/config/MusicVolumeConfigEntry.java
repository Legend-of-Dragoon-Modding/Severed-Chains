package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.AUDIO_THREAD;

public class MusicVolumeConfigEntry extends ConfigEntry<Float> {
  public MusicVolumeConfigEntry() {
    super(1.0f, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicVolumeConfigEntry::serializer, MusicVolumeConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Float> spinner = NumberSpinner.percentSpinner(number, 0.05f, 0.25f, 0.0f, 1.0f);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Float oldValue, final Float newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.getSequencer().setVolume(newValue);
  }

  private static byte[] serializer(final float val) {
    return new byte[] {(byte)(Math.round(val * 100.0f))};
  }

  private static float deserializer(final byte[] data) {
    if(data.length == 1) {
      return IoHelper.readUByte(data, 0) / 100.0f;
    }

    return 1.0f;
  }
}
