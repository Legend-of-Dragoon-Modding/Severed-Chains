package legend.game.combat.environment;

@FunctionalInterface
public interface CameraQuadParamCallback {
  void accept(int x, int y, int z, int scriptIndex);
}
