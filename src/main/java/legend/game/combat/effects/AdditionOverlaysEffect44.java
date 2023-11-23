package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import org.joml.Vector3f;

public class AdditionOverlaysEffect44 implements Effect {
  public int attackerScriptIndex_00;
  public int targetScriptIndex_04;

  public final Vector3f attackerStartingPosition_10 = new Vector3f();
  public final Vector3f distancePerFrame_20 = new Vector3f();

  /** ubyte */
  public int count_30;
  /** ubyte; 0 = renders and ticks, 1 = skips render and tick, 2 = renders only; set by scriptAlterAdditionContinuationState */
  public int pauseTickerAndRenderer_31;
  /** byte */
  public int additionComplete_32;

  /** short */
  public int currentFrame_34;
  /** ushort */
  public int unused_36;
  /** ubyte */
  public int numFramesToRenderCenterSquare_38;
  /** ubyte */
  public int lastCompletedHit_39;
  /** ubyte; 0 = no auto complete, 2 = WC and UW auto-complete */
  public int autoCompleteType_3a;

  // Not needed anymore, just reference hit overlay array at index lastCompletedHit_39
  // public final Pointer<AdditionOverlaysHit20> lastCompletedHitOverlay_3c;
  public AdditionOverlaysHit20[] hitOverlays_40;

  public Obj reticleBorderShadow;
  public final MV transforms = new MV();
}
