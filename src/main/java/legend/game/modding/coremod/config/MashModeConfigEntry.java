package legend.game.modding.coremod.config;

import legend.game.combat.MashMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;

import static legend.core.GameEngine.CONFIG;

public class MashModeConfigEntry extends EnumConfigEntry<MashMode> implements ScriptReadable {
  public MashModeConfigEntry() {
    super(MashMode.class, MashMode.MASH, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public void read(final int index, final Param out) {
    out.set(CONFIG.getConfig(this).ordinal());
  }
}
