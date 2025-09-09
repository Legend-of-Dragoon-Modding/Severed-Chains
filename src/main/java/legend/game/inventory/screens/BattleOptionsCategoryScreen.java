package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.inventory.screens.controls.Panel;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

import java.util.Set;

public class BattleOptionsCategoryScreen extends OptionsCategoryScreen {
  private Panel panel;

  public BattleOptionsCategoryScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final Runnable unload) {
    super(config, validLocations, unload);
  }

  @Override
  protected void init() {
    this.panel = this.addControl(Panel.panel());
    this.panel.setPos(12, 20);
    this.panel.setSize(296, 140);
  }

  @Override
  protected MenuScreen createOptionsScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final ConfigCategory category) {
    return new BattleOptionsScreen(config, validLocations, category, SItem.menuStack::popScreen);
  }

  @Override
  public int getWidth() {
    return 320;
  }

  @Override
  protected int maxVisibleEntries() {
    return (this.panel.getHeight() - 14) / 14;
  }

  @Override
  protected void updateArrowPositions() {
    super.updateArrowPositions();
  }
}
