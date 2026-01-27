package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.scripting.RunningScript;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class PostBattleAction<T extends PostBattleActionInstance<U, T>, U extends PostBattleAction<T, U>> extends RegistryEntry {
  /** The total duration of the action (e.g. victory dance and fade out) */
  protected abstract int getTotalDuration(final Battle battle, final T inst);
  /** The duration of the fade out */
  protected abstract int getFadeDuration(final Battle battle, final T inst);
  /** Run when the fade out starts */
  protected abstract void onCameraFadeoutStart(final Battle battle, final T inst);
  /** Run when the fade out finishes */
  protected abstract void onCameraFadeoutFinish(final Battle battle, final T inst);
  /** Run when the game is ready to transition away from battle */
  protected abstract void performAction(final Battle battle, final T inst);
  /** Create an instance of this {@link PostBattleAction} */
  public abstract T inst(final RunningScript<?> script);
}
