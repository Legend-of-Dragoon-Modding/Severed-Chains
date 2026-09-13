package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapBattleStage;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapBattleStageEntry extends WorldMapDataEntry<WorldMapBattleStage> {
  public WorldMapBattleStageEntry(final Function<RegistryId, WorldMapBattleStage> factory) {
    super(factory);
  }

  public WorldMapBattleStageEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapBattleStage> factory) {
    super(replaces, priority, factory);
  }
}
