package legend.game.combat.effects;

import org.joml.Vector3f;

import java.util.Arrays;

public class ProjectileHitEffectInstance48 {
  public boolean used_00 = true;

  public final Vector3f[] _04 = new Vector3f[16];
  public final Vector3f[] _24 = new Vector3f[8];
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

  public ProjectileHitEffectInstance48() {
    Arrays.setAll(this._04, i -> new Vector3f());
    Arrays.setAll(this._24, i -> new Vector3f());
  }
}
