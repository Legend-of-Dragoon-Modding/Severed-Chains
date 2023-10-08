package legend.game.combat.environment;

import legend.game.combat.types.BattleObject;

@FunctionalInterface
public interface CameraSeptParamCallback {
  void accept(float x, float y, float z, int ticks, int stepType, int f, BattleObject bobj);
}
