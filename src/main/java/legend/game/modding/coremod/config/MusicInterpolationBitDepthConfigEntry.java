package legend.game.modding.coremod.config;

import legend.core.audio.InterpolationBitDepth;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;

public class MusicInterpolationBitDepthConfigEntry  extends ConfigEntry<InterpolationBitDepth> {

  public MusicInterpolationBitDepthConfigEntry() {
    super(InterpolationBitDepth.Double, ConfigStorageLocation.GLOBAL, ConfigCategory.AUDIO, MusicInterpolationBitDepthConfigEntry::serializer, MusicInterpolationBitDepthConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(this, InterpolationBitDepth.values()[index]));

      for(final InterpolationBitDepth bitDepth : InterpolationBitDepth.values()) {
        dropdown.addOption(bitDepth.name());

        if(bitDepth == CONFIG.getConfig(this)) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final InterpolationBitDepth oldValue, final InterpolationBitDepth newValue) {
    super.onChange(configCollection, oldValue, newValue);
    AUDIO_THREAD.changeInterpolationBitDepth(newValue);
  }

  private static byte[] serializer(final InterpolationBitDepth bitDepth) {
    return new byte[] {(byte)bitDepth.value};
  }

  private static InterpolationBitDepth deserializer(final byte[] data) {
    if(data.length == 1) {
      for(final InterpolationBitDepth bitDepth : InterpolationBitDepth.values()) {
        if(bitDepth.value == data[0]) {
          return bitDepth;
        }
      }
    }

    return InterpolationBitDepth.Double;
  }
}
