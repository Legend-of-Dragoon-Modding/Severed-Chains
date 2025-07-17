package legend.game.modding.events.scripting;

import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.events.Event;

import java.util.function.Consumer;

public class DrgnFileEvent extends Event {
  public String location;
  public boolean overrideLoad = false;
  public final Consumer<FileData> onCompletion;

  public DrgnFileEvent(final String location, final Consumer<FileData> onCompletion) {
    this.location = location;
    this.onCompletion = onCompletion;
  }
}
