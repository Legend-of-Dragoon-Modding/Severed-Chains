package legend.game.combat.effects;

import java.util.Arrays;

public class ProjectileHitEffect14 implements BttlScriptData6cSubBase1 {
  public final int count_00;

  public final ProjectileHitEffect14Sub48[] _08;

  public ProjectileHitEffect14(final int count) {
    this.count_00 = count;

    this._08 = new ProjectileHitEffect14Sub48[count];
    Arrays.setAll(this._08, i -> new ProjectileHitEffect14Sub48());
  }
}
