package legend.game.modding.events.worldmap;

import legend.game.combat.encounters.Encounter;
import legend.game.modding.events.gamestate.EncounterEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.WMap;

public class WorldMapEncounterEvent extends EncounterEvent<WMap> implements WorldMapEvent {
  public final DirectionalPathSegmentData08 directionalPathSegment;

  public WorldMapEncounterEvent(final WMap engineState, final GameState52c gameState, final Encounter encounter, final int battleStageId, final DirectionalPathSegmentData08 directionalPathSegment) {
    super(engineState, gameState, encounter, battleStageId);
    this.directionalPathSegment = directionalPathSegment;
  }
}
