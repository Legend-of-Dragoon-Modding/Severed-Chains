package legend.game.textures;

import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

public class RegisterAtlasTexturesEvent extends Event {
  private final Map<RegistryId, Image> textures;

  public RegisterAtlasTexturesEvent(final Map<RegistryId, Image> textures) {
    this.textures = textures;
  }

  public void add(final RegistryId id, final Image image) {
    this.textures.put(id, image);
  }
}
