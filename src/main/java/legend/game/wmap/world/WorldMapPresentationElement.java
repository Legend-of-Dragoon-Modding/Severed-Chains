package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;
import javax.annotation.Nullable;
import java.util.Objects;

/** Named declaration interpreted by the selected presentation controller. */
public record WorldMapPresentationElement(RegistryId id, WorldMapPoint position, @Nullable String label, @Nullable RegistryId texture) {
  public WorldMapPresentationElement {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(position, "position");
    if(!Float.isFinite(position.x()) || !Float.isFinite(position.y()) || !Float.isFinite(position.z())) throw new IllegalArgumentException("Presentation element position must be finite: " + id);
  }
}
