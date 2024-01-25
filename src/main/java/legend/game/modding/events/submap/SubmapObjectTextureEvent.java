package legend.game.modding.events.submap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.opengl.Texture;

import java.util.function.Consumer;

public class SubmapObjectTextureEvent extends SubmapEvent {
  public final int disk;
  public final int submapCut;
  public final Int2ObjectMap<Consumer<Texture.Builder>> textures = new Int2ObjectOpenHashMap<>();

  public SubmapObjectTextureEvent(final int disk, final int submapCut) {
    this.disk = disk;
    this.submapCut = submapCut;
  }
}
