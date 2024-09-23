package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;

import static legend.core.GameEngine.CONFIG;

public class InventorySizeConfigEntry extends ConfigEntry<Integer> implements ScriptReadable {
  public InventorySizeConfigEntry() {
    super(32, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, InventorySizeConfigEntry::serializer, InventorySizeConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Integer> spinner = NumberSpinner.intSpinner(number, 1, 9999);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  @Override
  public void read(final int index, final Param out) {
    out.set(CONFIG.getConfig(this));
  }

  private static byte[] serializer(final int val) {
    final byte[] data = new byte[4];
    MathHelper.set(data, 0, 4, val);
    return data;
  }

  private static int deserializer(final byte[] data) {
    if(data.length == 4) {
      return IoHelper.readInt(data, 0);
    }

    return 32;
  }
}
