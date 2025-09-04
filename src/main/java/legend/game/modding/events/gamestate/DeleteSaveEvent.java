package legend.game.modding.events.gamestate;

import legend.game.saves.Campaign;
import org.legendofdragoon.modloader.events.Event;

public class DeleteSaveEvent extends Event {
  public final Campaign campaign;
  public final String fileName;

  public DeleteSaveEvent(final Campaign campaign, final String fileName) {
    this.campaign = campaign;
    this.fileName = fileName;
  }
}
