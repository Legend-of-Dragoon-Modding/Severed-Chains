package legend.game.combat.environment;

@FunctionalInterface
public interface CameraSeptParamCallback {
  void accept(int x, int y, int z, int d, int e, int f, int scriptIndex);
}
