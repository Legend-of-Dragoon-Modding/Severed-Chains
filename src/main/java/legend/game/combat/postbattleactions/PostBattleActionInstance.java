package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;

public abstract class PostBattleActionInstance<T extends PostBattleAction<U, T>, U extends PostBattleActionInstance<T, U>> {
  public final T action;

  public PostBattleActionInstance(final T action) {
    this.action = action;
  }

  public int getTotalDuration(final Battle battle) {
    return this.action.getTotalDuration(battle, (U)this);
  }

  public int getFadeDuration(final Battle battle) {
    return this.action.getFadeDuration(battle, (U)this);
  }

  public void onCameraFadeoutStart(final Battle battle) {
    this.action.onCameraFadeoutStart(battle, (U)this);
  }

  public void onCameraFadeoutFinish(final Battle battle) {
    this.action.onCameraFadeoutFinish(battle, (U)this);
  }

  public void performAction(final Battle battle) {
    this.action.performAction(battle, (U)this);
  }
}
