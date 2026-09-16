package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;

import static legend.core.GameEngine.CONFIG;

public class QuickTextModeConfigEntry extends EnumConfigEntry<QuickTextMode> implements ScriptReadable {
  public QuickTextModeConfigEntry() {
    super(QuickTextMode.class, QuickTextMode.ALWAYS, ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  @Override
  public void read(final int index, final Param out) {
    out.set(CONFIG.getConfig(this).scriptValue);
  }
}
