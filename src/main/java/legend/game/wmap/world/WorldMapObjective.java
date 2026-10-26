package legend.game.wmap.world;

import legend.game.types.Flags;
import legend.game.wmap.WMapDestinationMarker2c;
import legend.game.wmap.WmapStatics;

import javax.annotation.Nullable;

/** World map objective presentation, independent of portal availability. */
public record WorldMapObjective(int x, int y, @Nullable String label) {
  @Nullable
  public static WorldMapObjective legacy(final Flags story, final WorldMapDefinition definition) {
    final int index = WorldMapStory.select(story, WmapStatics.wmapDestinationMarkers_800f5a6c);
    if(index <= 0) {
      return null;
    }

    final WMapDestinationMarker2c marker = WmapStatics.wmapDestinationMarkers_800f5a6c[index];
    return new WorldMapObjective(marker.x_24, marker.y_26, definition.place(marker.placeIndex_28).name());
  }
}
