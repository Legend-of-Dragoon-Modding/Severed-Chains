package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapRules;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

/** Configure this WMap instance after resolving registered/preset data and before resolving locations. */
public class WorldMapConfigureEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapDefinition.Builder definition;
  public final WorldMapRules.Builder rules;
  /** Selected authored preset, or null when compiling registered defaults. */
  @Nullable public final RegistryId presetId;

  public WorldMapConfigureEvent(final WMap engineState, final GameState52c gameState, final WorldMapDefinition.Builder definition, final WorldMapRules.Builder rules) {
    this(engineState, gameState, definition, rules, null);
  }

  public WorldMapConfigureEvent(final WMap engineState, final GameState52c gameState, final WorldMapDefinition.Builder definition, final WorldMapRules.Builder rules, @Nullable final RegistryId presetId) {
    super(engineState, gameState);
    this.definition = definition;
    this.rules = rules;
    this.presetId = presetId;
  }
}
