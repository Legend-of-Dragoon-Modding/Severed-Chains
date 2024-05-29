package legend.game.combat.environment;

import legend.game.combat.types.BattleObject;

@FunctionalInterface
public interface CameraStepParamCallback {
  void accept(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj);
}
