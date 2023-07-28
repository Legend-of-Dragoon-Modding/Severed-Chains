package legend.game.combat.environment;

@FunctionalInterface
public interface CameraOctParamCallback {
  void accept(float x, float y, float z, int d, int e, int f, int g, int scriptIndex);
}
