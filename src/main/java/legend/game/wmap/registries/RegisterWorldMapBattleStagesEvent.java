package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapBattleStagesEvent extends RegistryEvent.Register<WorldMapBattleStageEntry> {
  public RegisterWorldMapBattleStagesEvent(final MutableRegistry<WorldMapBattleStageEntry> registry) {
    super(registry);
  }
}
