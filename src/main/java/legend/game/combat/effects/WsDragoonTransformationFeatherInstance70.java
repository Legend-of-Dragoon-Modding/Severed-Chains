package legend.game.combat.effects;

import org.joml.Vector3f;

public class WsDragoonTransformationFeatherInstance70 {
  public final int index;

  /** ubyte */
  public boolean renderFeather_00;

  /** ushorts */
  public int callbackIndex_02;
  public int currentFrame_04;

  public final Vector3f translation_08 = new Vector3f();

  public float velocityTranslationMagnitudeY_1c;

  /** ubytes */
  public int r_38;
  public int g_39;
  public int b_3a;

  /** Magnitude of x and z components of translation vector around center of effect */
  public float translationMagnitudeXz_3c;
  public float velocityTranslationMagnitudeXz_40;
  public float accelerationTranslationMagnitudeXz_44;
  public float xOffset_48;
  /** short */
  public int countCallback1and3Frames_4c;

  /** Magnitude of y component of translation vector around center of effect */
  public float translationMagnitudeY_50;
  public float yOrigin_54;
  public float angle_58;
  public float angleNoiseXz_5c;
  public float angleStep_60;
  /** short */
  public int countCallback0Frames_64;
  /** ushorts */
  public int unused_66;
  public int unused_68;
  public int unused_6a;
  public int unused_6c;
  /** short */
  public float spriteAngle_6e;

  public WsDragoonTransformationFeatherInstance70(final int index) {
    this.index = index;
  }
}
