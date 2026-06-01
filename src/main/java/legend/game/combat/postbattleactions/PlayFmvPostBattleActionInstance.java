package legend.game.combat.postbattleactions;

public class PlayFmvPostBattleActionInstance extends PostBattleActionInstance<PlayFmvPostBattleAction, PlayFmvPostBattleActionInstance> {
  public final int fmv;

  public PlayFmvPostBattleActionInstance(final PlayFmvPostBattleAction action, final int fmv) {
    super(action);
    this.fmv = fmv;
  }
}
