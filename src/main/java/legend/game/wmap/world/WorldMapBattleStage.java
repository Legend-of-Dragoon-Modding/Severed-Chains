package legend.game.wmap.world;

import javax.annotation.Nullable;

/** Native battle-stage index selected for encounters on a route. */
public record WorldMapBattleStage(int nativeIndex, @Nullable String label) {
  public WorldMapBattleStage(final int nativeIndex) {
    this(nativeIndex, null);
  }

  public WorldMapBattleStage {
    if(nativeIndex < -1) throw new IllegalArgumentException("WMAP battle stage native index cannot be less than -1");
  }
}
