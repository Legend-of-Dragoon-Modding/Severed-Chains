package legend.game.combat.environment;

@FunctionalInterface
public interface CameraSeptParamCallback {
  void accept(float x, float y, float z, int ticks, int e, int f, int scriptIndex);
}
