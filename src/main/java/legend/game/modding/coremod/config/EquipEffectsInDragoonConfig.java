package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;

import static legend.core.GameEngine.CONFIG;

public class EquipEffectsInDragoonConfig extends BoolConfigEntry implements ScriptReadable {
  public EquipEffectsInDragoonConfig() {
    super(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }


  @Override
  public void read(final int index, final Param out) {
    out.set(CONFIG.getConfig(this));
  }
}