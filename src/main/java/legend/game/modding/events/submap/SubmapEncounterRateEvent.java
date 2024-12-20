package legend.game.modding.events.submap;

public class SubmapEncounterRateEvent extends SubmapEvent {
  public int encounterRate;
  public final int submapId;

  public SubmapEncounterRateEvent(final int encounterRate, final int submapId) {
    this.encounterRate = encounterRate;
    this.submapId = submapId;
  }
}
