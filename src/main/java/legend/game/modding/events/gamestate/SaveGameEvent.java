package legend.game.modding.events.gamestate;

import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.events.Event;

public class SaveGameEvent extends Event {
  public final String fileName;
  public final String saveName;
  public final GameState52c state;

  public SaveGameEvent(final String fileName, final String saveName, final GameState52c state) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.state = state;
  }
}
