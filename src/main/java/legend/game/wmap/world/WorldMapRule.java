package legend.game.wmap.world;

/** Must be deterministic and side-effect free for a given progression snapshot. */
@FunctionalInterface
public interface WorldMapRule {
  WorldMapAccess evaluate(WorldMapPortal portal, WorldMapAction action, WorldMapProgression progression);
}
