package legend.game.modding.events.utility;

public class UtilitySimpleRandEvent extends UtilityEvent {
  public final int simpleRandResult;
  public final int previousPseudoSeed;
  public final int newPseudoSeed;

  public UtilitySimpleRandEvent(final int simpleRandResult, final int previousPseudoSeed, final int newPseudoSeed) {
    this.simpleRandResult = simpleRandResult;
    this.previousPseudoSeed = previousPseudoSeed;
    this.newPseudoSeed = newPseudoSeed;
  }
}
