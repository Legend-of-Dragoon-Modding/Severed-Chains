package legend.game.combat.environment;

@FunctionalInterface
public interface CameraOctParamCallback {
  void accept(float x, float y, float z, int ticks, int e, int f, int stepType, int scriptIndex);
}
