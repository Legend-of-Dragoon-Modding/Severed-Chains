package legend.game.modding.events.scripting;

import org.legendofdragoon.modloader.events.Event;

public class DrgnFileEvent extends Event {
  public String location;

  public DrgnFileEvent(final String location) {
    this.location = location;
  }
}
