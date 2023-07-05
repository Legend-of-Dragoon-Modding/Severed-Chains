package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;

import java.util.Arrays;

public class ProjectileHitEffect14Sub48 {
  public boolean used_00;

  public final VECTOR[] _04 = new VECTOR[16];
  public final SVECTOR[] _24 = new SVECTOR[8];
  public int r_34;
  public int g_36;
  public int b_38;
  /** Amount to fade R each frame */
  public int fadeR_3a;
  /** Amount to fade G each frame */
  public int fadeG_3c;
  /** Amount to fade B each frame */
  public int fadeB_3e;

  public int _40;
  /** Number of frames to decrease the colour over */
  public int frames_44;

  public ProjectileHitEffect14Sub48() {
    Arrays.setAll(this._04, i -> new VECTOR());
    Arrays.setAll(this._24, i -> new SVECTOR());
  }
}
