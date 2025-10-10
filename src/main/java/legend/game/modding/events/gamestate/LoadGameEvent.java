package legend.game.modding.events.gamestate;

import legend.game.saves.SavedGame;
import org.legendofdragoon.modloader.events.Event;

public class LoadGameEvent extends Event {
  public final SavedGame save;

  public LoadGameEvent(final SavedGame save) {
    this.save = save;
  }
}
