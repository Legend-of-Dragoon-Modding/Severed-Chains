package legend.game.inventory.screens;

import legend.game.inventory.screens.controls.Panel;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

import java.util.Set;

public class BattleOptionsScreen extends OptionsScreen {
  private final Panel panel;

  public BattleOptionsScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final ConfigCategory category, final Runnable unload) {
    final Panel panel = Panel.panel();
    panel.setPos(12, 20);
    panel.setSize(296, 140);
    this.panel = panel;

    super(config, validLocations, category, unload);

    this.addControl(panel);
  }

  @Override
  protected boolean hideNonBattleEntries() {
    return true;
  }

  @Override
  protected void init() {

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
    this.upArrow.setPos((int)(this.getWidth() - 20 * this.getSizeScale()), this.panel.getY() + 6);
    this.downArrow.setPos((int)(this.getWidth() - 20 * this.getSizeScale()), this.panel.getY() + this.panel.getHeight() - 24);
  }
}
