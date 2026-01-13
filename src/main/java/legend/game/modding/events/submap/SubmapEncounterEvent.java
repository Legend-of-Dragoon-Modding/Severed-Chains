package legend.game.modding.events.submap;

import legend.game.combat.encounters.Encounter;
import legend.game.modding.events.gamestate.EncounterEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

public class SubmapEncounterEvent extends EncounterEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public final int submapId;
  public final int sceneId;
  public final int[] scene;

  public SubmapEncounterEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final Encounter encounter, final int battleStageId, final int submapId, final int sceneId, final int[] scene) {
    super(engineState, gameState, encounter, battleStageId);
    this.submap = submap;
    this.submapId = submapId;
    this.sceneId = sceneId;
    this.scene = scene;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
