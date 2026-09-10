package legend.game.wmap.world;

import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.wmap.WmapStatics;

import java.util.Arrays;
import java.util.List;

/** Immutable presentation tables; UV metrics retain their immutable values and apply behavior. */
public record WorldMapPresentationProfile(List<WorldMapPoint> mapPositions, List<String> regions, List<String> services, List<Integer> waterClutYs, List<Integer> playerAvatarVramSlots, List<UvAdjustmentMetrics14> textureAdjustments) {
  public WorldMapPresentationProfile {
    mapPositions = List.copyOf(mapPositions);
    regions = List.copyOf(regions);
    services = List.copyOf(services);
    waterClutYs = List.copyOf(waterClutYs);
    playerAvatarVramSlots = List.copyOf(playerAvatarVramSlots);
    textureAdjustments = List.copyOf(textureAdjustments);
  }

  public static WorldMapPresentationProfile legacy() {
    return new WorldMapPresentationProfile(
      Arrays.stream(WmapStatics.mapPositions_800ef1a8).map(point -> new WorldMapPoint(point.x, point.y, point.z)).toList(),
      Arrays.asList(WmapStatics.regions_800f01ec),
      Arrays.asList(WmapStatics.services_800f01cc),
      Arrays.stream(WmapStatics.waterClutYs_800ef348).boxed().toList(),
      Arrays.stream(WmapStatics.playerAvatarVramSlots_800ef694).boxed().toList(),
      Arrays.asList(WmapStatics.tmdUvAdjustmentMetrics_800eee48)
    );
  }
}
