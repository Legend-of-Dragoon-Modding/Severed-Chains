package legend.game.modding.events.scripting;

import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.events.Event;

import java.nio.file.Path;
import java.util.function.Consumer;

public class DrgnFileEvent extends Event {
  public String location;
  public boolean overrideLoad = false;
  public final Consumer<FileData> onCompletion;
  public final boolean loadDeff;
  public byte[] fileData;
  public Path deffPath;

  public DrgnFileEvent(final String location, final Consumer<FileData> onCompletion) {
    this.location = location;
    this.onCompletion = onCompletion;
    this.loadDeff = false;
  }

  public DrgnFileEvent(final Path deffPath, final byte[] fileData) {
    this.deffPath = deffPath;
    this.fileData = fileData;
    this.loadDeff = true;
    this.onCompletion = null;
  }
}
