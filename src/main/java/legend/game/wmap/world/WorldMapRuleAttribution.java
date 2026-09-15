package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

/** Source metadata for a configured world-map rule; legacy callers have a null source. */
public record WorldMapRuleAttribution(RegistryId source, int priority, WorldMapRuleComposition composition) { }
