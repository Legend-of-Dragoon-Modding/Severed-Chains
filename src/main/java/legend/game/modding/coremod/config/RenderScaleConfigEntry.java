package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.GPU;

public class RenderScaleConfigEntry extends ConfigEntry<Integer> {
  public static final int MAX = 5;

  public RenderScaleConfigEntry() {
    super(1, ConfigStorageLocation.GLOBAL, RenderScaleConfigEntry::serializer, RenderScaleConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Integer> spinner = NumberSpinner.intSpinner(number, 1, MAX);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  @Override
  public void onChange(final Integer oldValue, final Integer newValue) {
    super.onChange(oldValue, newValue);
    GPU.rescale(newValue);
  }

  private static byte[] serializer(final int val) {
    return new byte[] {(byte)val};
  }

  private static int deserializer(final byte[] data) {
    if(data.length == 1) {
      return IoHelper.readUByte(data, 0);
    }

    return 1;
  }
}
