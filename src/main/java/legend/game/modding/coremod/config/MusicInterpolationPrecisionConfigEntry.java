package legend.game.modding.coremod.config;

import legend.core.audio.InterpolationPrecision;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;

public class MusicInterpolationPrecisionConfigEntry extends ConfigEntry<InterpolationPrecision> {

  public MusicInterpolationPrecisionConfigEntry() {
    super(InterpolationPrecision.Double, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicInterpolationPrecisionConfigEntry::serializer, MusicInterpolationPrecisionConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, InterpolationPrecision.values()[index]));

      for(final InterpolationPrecision bitDepth : InterpolationPrecision.values()) {
        dropdown.addOption(bitDepth.name());

        if(bitDepth == CONFIG.getConfig(this)) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final InterpolationPrecision oldValue, final InterpolationPrecision newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.changeInterpolationBitDepth(newValue);
  }

  private static byte[] serializer(final InterpolationPrecision bitDepth) {
    return new byte[] {(byte)bitDepth.value};
  }

  private static InterpolationPrecision deserializer(final byte[] data) {
    if(data.length == 1) {
      for(final InterpolationPrecision bitDepth : InterpolationPrecision.values()) {
        if(bitDepth.value == data[0]) {
          return bitDepth;
        }
      }
    }

    return InterpolationPrecision.Double;
  }
}
