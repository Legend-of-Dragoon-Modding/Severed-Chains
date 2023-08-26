package legend.game.combat.environment;

import legend.game.combat.types.BattleObject;

@FunctionalInterface
public interface CameraOctParamCallback {
  void accept(float x, float y, float z, int ticks, int e, float f, int stepType, BattleObject bobj);
}
