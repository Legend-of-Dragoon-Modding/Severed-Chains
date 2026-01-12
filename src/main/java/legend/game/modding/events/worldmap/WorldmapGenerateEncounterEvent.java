package legend.game.modding.events.worldmap;

import legend.game.wmap.DirectionalPathSegmentData08;

public class WorldmapGenerateEncounterEvent extends WorldmapEvent{
  public int encounterId;
  public int battleStageId;
  final public DirectionalPathSegmentData08 directionalPathSegment;

  public WorldmapGenerateEncounterEvent(final int encounterId, final int battleStageId, final DirectionalPathSegmentData08 directionalPathSegment) {
    this.encounterId = encounterId;
    this.battleStageId = battleStageId;
    this.directionalPathSegment = directionalPathSegment;
  }
}
