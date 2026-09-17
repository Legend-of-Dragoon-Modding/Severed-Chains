package legend.game.wmap.world;

import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.wmap.WmapStatics;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import javax.annotation.Nullable;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Immutable presentation tables; UV metrics retain their immutable values and apply behavior. */
public record WorldMapPresentationProfile(List<WorldMapPoint> mapPositions, List<String> regions, List<String> services, List<Integer> waterClutYs, List<Integer> playerAvatarVramSlots, List<UvAdjustmentMetrics14> textureAdjustments,
                                          List<WorldMapPresentationElement> namedElements, List<WorldMapPresentationTexture> namedTextures,
                                          @Nullable WorldMapPresentationCapabilities capabilities) {
  public WorldMapPresentationProfile(final List<WorldMapPoint> mapPositions, final List<String> regions, final List<String> services,
                                    final List<Integer> waterClutYs, final List<Integer> playerAvatarVramSlots, final List<UvAdjustmentMetrics14> textureAdjustments) {
    this(mapPositions, regions, services, waterClutYs, playerAvatarVramSlots, textureAdjustments, List.of(), List.of(), null);
  }

  public WorldMapPresentationProfile {
    mapPositions = List.copyOf(mapPositions);
    regions = List.copyOf(regions);
    services = List.copyOf(services);
    waterClutYs = List.copyOf(waterClutYs);
    playerAvatarVramSlots = List.copyOf(playerAvatarVramSlots);
    textureAdjustments = List.copyOf(textureAdjustments);
    namedElements = List.copyOf(namedElements);
    namedTextures = List.copyOf(namedTextures);
    if(capabilities == null && (mapPositions.size() < 8 || regions.size() < 3 || services.size() < 5 || waterClutYs.size() < 14 || playerAvatarVramSlots.size() < 4 || textureAdjustments.size() < 22)) {
      throw new IllegalArgumentException("World map presentation must supply the runtime's bounded UI and avatar slots");
    }
    for(final int slot : playerAvatarVramSlots) {
      if(slot < 0 || slot >= (capabilities == null ? textureAdjustments.size() : Math.max(22, textureAdjustments.size()))) throw new IllegalArgumentException("Unknown world map avatar texture slot " + slot);
    }
    for(final WorldMapPoint point : mapPositions) {
      if(!Float.isFinite(point.x()) || !Float.isFinite(point.y()) || !Float.isFinite(point.z())) throw new IllegalArgumentException("World map presentation positions must be finite");
    }
    final var textureIds = new HashSet<RegistryId>();
    for(final WorldMapPresentationTexture texture : namedTextures) {
      if(!textureIds.add(texture.id())) throw new IllegalArgumentException("Duplicate named presentation texture " + texture.id());
    }
    final var elementIds = new HashSet<RegistryId>();
    for(final WorldMapPresentationElement element : namedElements) {
      if(!elementIds.add(element.id())) throw new IllegalArgumentException("Duplicate named presentation element " + element.id());
      if(element.texture() != null && !textureIds.contains(element.texture())) throw new IllegalArgumentException("Unknown named presentation texture " + element.texture() + " for " + element.id());
    }
  }

  public boolean supportsRetailLabels() { return this.capabilities == null || this.capabilities.retailLabels(); }
  public boolean supportsRetailWater() { return this.capabilities == null || this.capabilities.retailWater(); }
  public boolean supportsRetailAvatars() { return this.capabilities == null || this.capabilities.retailAvatars(); }

  public static WorldMapPresentationProfile authored(final List<WorldMapPresentationElement> elements, final List<WorldMapPresentationTexture> textures, final WorldMapPresentationCapabilities capabilities) {
    return new WorldMapPresentationProfile(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), elements, textures, Objects.requireNonNull(capabilities, "capabilities"));
  }

  public WorldMapPresentationElement element(final RegistryId id) {
    return this.namedElements.stream().filter(element -> element.id().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown presentation element " + id));
  }

  public WorldMapPresentationTexture texture(final RegistryId id) {
    return this.namedTextures.stream().filter(texture -> texture.id().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown presentation texture " + id));
  }

  /** Bounded compatibility tables; the original named declarations remain independent. */
  public WorldMapPresentationProfile legacyLayout() {
    if(this.capabilities == null) return this;
    final WorldMapPresentationProfile fallback = legacy();
    return new WorldMapPresentationProfile(fill(this.mapPositions, fallback.mapPositions), fill(this.regions, fallback.regions),
      fill(this.services, fallback.services), fill(this.waterClutYs, fallback.waterClutYs),
      fill(this.playerAvatarVramSlots, fallback.playerAvatarVramSlots), fill(this.textureAdjustments, fallback.textureAdjustments));
  }

  private static <T> List<T> fill(final List<T> declared, final List<T> fallback) {
    final List<T> result = new ArrayList<>(declared);
    for(int i = declared.size(); i < fallback.size(); i++) result.add(fallback.get(i));
    return result;
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
