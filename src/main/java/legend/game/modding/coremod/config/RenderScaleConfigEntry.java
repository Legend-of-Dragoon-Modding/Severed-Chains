package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigEntry;

import static legend.core.GameEngine.GPU;

public class RenderScaleConfigEntry extends ConfigEntry<Integer> {
  public static final int MAX = 10;

  public RenderScaleConfigEntry() {
    super(2, RenderScaleConfigEntry::validator, RenderScaleConfigEntry::serializer, RenderScaleConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner spinner = new NumberSpinner(number);
      spinner.setMin(1);
      spinner.setMax(MAX);
      spinner.onChange(val -> {
        gameState.setConfig(this, val);
        GPU.rescale(val);
      });
      return spinner;
    });
  }

  private static boolean validator(final int val) {
    return val > 0 && val <= MAX;
  }

  private static byte[] serializer(final int val) {
    return new byte[] {(byte)val};
  }

  private static int deserializer(final byte[] data) {
    if(data.length == 1) {
      return IoHelper.readUByte(data, 0);
    }

    return 2;
  }
}
