package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

/** Source metadata for a configured rule, including the identifiable legacy adapter. */
public record WorldMapRuleAttribution(RegistryId source, int priority, WorldMapRuleComposition composition, boolean legacy) {
  public static final RegistryId LEGACY_SOURCE = new RegistryId("lod", "wmap_legacy_rules");

  public WorldMapRuleAttribution(final RegistryId source, final int priority, final WorldMapRuleComposition composition) {
    this(source == null ? LEGACY_SOURCE : source, priority, composition, source == null);
  }
}
