package legend.game.modding.events.scripting;

import org.legendofdragoon.modloader.events.Event;
import java.nio.file.Path;

public class DrgnFileEvent extends Event {
  public Path path;

  public DrgnFileEvent(final Path path) {
    this.path = path;
  }
}