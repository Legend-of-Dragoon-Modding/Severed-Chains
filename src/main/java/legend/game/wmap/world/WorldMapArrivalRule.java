package legend.game.wmap.world;

/** Pure transport-entry decision over the final configured world and its progression. */
@FunctionalInterface
public interface WorldMapArrivalRule {
  WorldMapTravel.Arrival evaluate(SubmapEndpoint origin, WorldMapProgression progression, WorldMapDefinition definition);
}
