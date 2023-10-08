package legend.core.memory.types;

import legend.game.combat.types.BattleObject;
import org.joml.Vector3f;

@FunctionalInterface
public interface ComponentFunction {
  float apply(int component, BattleObject bobj, Vector3f point);
}
