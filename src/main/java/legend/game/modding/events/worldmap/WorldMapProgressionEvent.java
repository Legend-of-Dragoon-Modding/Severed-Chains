package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapProgression;
import legend.game.wmap.world.WorldMapDefinition;
import org.legendofdragoon.modloader.registries.RegistryId;
import javax.annotation.Nullable;

/**
 * Add named facts to a fresh snapshot. Do not mutate game state or reenter WMap queries here.
 * Fired when legacy flags change or a mod calls WMap.invalidateWorldMap() for its own saved state.
 */
public class WorldMapProgressionEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapProgression.Builder progression;
  public final WorldMapDefinition definition;
  @Nullable public final RegistryId presetId;
  public final boolean candidate;

  public WorldMapProgressionEvent(final WMap engineState, final GameState52c gameState, final WorldMapProgression.Builder progression) {
    this(engineState, gameState, progression, engineState.getWorldMapDefinition(), null, false);
  }

  public WorldMapProgressionEvent(final WMap engineState, final GameState52c gameState, final WorldMapProgression.Builder progression, final WorldMapDefinition definition, @Nullable final RegistryId presetId, final boolean candidate) {
    super(engineState, gameState);
    this.progression = progression;
    this.definition = definition;
    this.presetId = presetId;
    this.candidate = candidate;
  }
}
