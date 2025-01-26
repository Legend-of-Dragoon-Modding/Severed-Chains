package legend.game.modding.events.submap;

public class SubmapGenerateEncounterEvent extends SubmapEvent{
  public int encounterId;
  public int battleStageId;
  public final int submapId;
  public final int sceneId;
  public final int[] scene;

  public SubmapGenerateEncounterEvent(final int encounterId, final int battleStageId, final int submapId, final int sceneId, final int[] scene) {
    this.encounterId = encounterId;
    this.battleStageId = battleStageId;
    this.submapId = submapId;
    this.sceneId = sceneId;
    this.scene = scene;
  }
}
