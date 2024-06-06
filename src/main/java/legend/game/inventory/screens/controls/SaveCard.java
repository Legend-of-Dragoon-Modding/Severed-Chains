package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.saves.SavedGame;
import legend.game.saves.types.SaveDisplay;

import javax.annotation.Nullable;

public abstract class SaveCard<Display extends SaveDisplay> extends Control {
  public abstract void setSaveData(@Nullable final SavedGame<Display> saveData);
}
