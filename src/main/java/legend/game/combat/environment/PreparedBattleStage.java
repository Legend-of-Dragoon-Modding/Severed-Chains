package legend.game.combat.environment;

import legend.game.combat.Battle;

/**
 * Exclusively owned prepared resources. Preparation must not touch engine or GPU state.
 * Adoption runs once on the render thread and transfers installed resources to Battle.
 * Close releases any resources not transferred, including when the request is obsolete.
 * Close must be safe on a loader thread, and must not throw.
 */
public interface PreparedBattleStage extends AutoCloseable {
  void adopt(Battle battle);

  @Override
  default void close() { }
}
