package legend.game.combat.environment;

import legend.game.combat.types.BattleObject;

@FunctionalInterface
public interface CameraQuadParamCallback {
  void accept(float x, float y, float z, BattleObject bobj);
}
