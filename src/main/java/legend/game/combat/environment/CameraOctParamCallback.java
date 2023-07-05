package legend.game.combat.environment;

@FunctionalInterface
public interface CameraOctParamCallback {
  void accept(int x, int y, int z, int d, int e, int f, int g, int scriptIndex);
}
