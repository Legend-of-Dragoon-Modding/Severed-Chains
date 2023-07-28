package legend.game.combat.environment;

@FunctionalInterface
public interface CameraQuadParamCallback {
  void accept(float x, float y, float z, int scriptIndex);
}
