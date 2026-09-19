package legend.game.wmap.world;

import javax.annotation.Nullable;
import legend.game.combat.environment.BattleStageDefinition;
import legend.lodmod.LodBattleStages;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

/** A combat environment reference and native fallback selected for encounters on a route. */
public record WorldMapBattleStage(int nativeIndex, @Nullable String label, @Nullable RegistryId combatStageId) {
  public WorldMapBattleStage(final int nativeIndex, @Nullable final String label) {
    this(nativeIndex, label, null);
  }

  public WorldMapBattleStage(final int nativeIndex) {
    this(nativeIndex, null);
  }

  public WorldMapBattleStage {
    if(nativeIndex < -1) throw new IllegalArgumentException("WMAP battle stage native index cannot be less than -1");
  }

  /** Missing optional mod stages recover through the authored native fallback. */
  public BattleStageDefinition resolve() {
    if(this.combatStageId != null && REGISTRIES.battleStages.hasEntry(this.combatStageId)) return REGISTRIES.battleStages.getEntry(this.combatStageId).get();
    return LodBattleStages.nativeStage(this.nativeIndex == -1 ? 1 : this.nativeIndex);
  }
}
