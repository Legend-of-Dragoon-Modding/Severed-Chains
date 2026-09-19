package legend.game.modding.events.gamestate;

import legend.game.saves.SavedGame;
import org.legendofdragoon.modloader.events.Event;

public class LoadGamestateFileEvent extends Event {
  public final SavedGame save;

  public LoadGamestateFileEvent(final SavedGame save) {
    this.save = save;
  }
}
