package legend.game.combat.types;

import legend.core.MathHelper;
import legend.core.gte.MATRIX;
import legend.core.gte.USCOLOUR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;

public abstract class BattleObject {
  public static String EM__ = "EM  ";
  public static String BOBJ = "BOBJ";

  public String magic_00;

  public abstract VECTOR getPosition();
  public abstract Vector3f getRotation();
  public abstract Vector3f getScale();
  public abstract USCOLOUR getColour();

  /** Translates a position relative to this BattleObject's local coordinate system */
  @Method(0x801105ccL)
  public void getRelativePosition(final VECTOR in, final VECTOR out) {
    final MATRIX parentRotationMatrix = new MATRIX();
    RotMatrix_Xyz(this.getRotation(), parentRotationMatrix);
    in.mul(parentRotationMatrix, out).add(this.getPosition());
  }

  /** Translates a position relative to this BattleObject's local coordinate system */
  @Method(0x801105ccL)
  public void getRelativePosition(final VECTOR in) {
    this.getRelativePosition(in, in);
  }

  /** Returns the relative position from a parent BattleObject's local coordinate system */
  public VECTOR getRelativePositionTo(final BattleObject child, final VECTOR out) {
    return getRelativePositionBetween(child, this, out);
  }

  /** Returns the relative position from a parent BattleObject's local coordinate system */
  @Method(0x8011035cL)
  public VECTOR getRelativePositionFrom(final BattleObject parent, final VECTOR out) {
    return getRelativePositionBetween(this, parent, out);
  }

  private static VECTOR getRelativePositionBetween(final BattleObject child, final BattleObject parent, final VECTOR out) {
    final VECTOR deltaPosition = new VECTOR().set(child.getPosition()).sub(parent.getPosition());
    final MATRIX parentRotationMatrix = new MATRIX();
    RotMatrix_Xyz(parent.getRotation(), parentRotationMatrix);

    parentRotationMatrix.transpose();
    deltaPosition.mul(parentRotationMatrix, out);
    return out;
  }

  @Method(0x80112474L)
  public Vector3f getRotationDifference(final BattleObject other, final Vector3f outRotation) {
    MathHelper.floorMod(outRotation.set(this.getRotation()).sub(other.getRotation()), MathHelper.TWO_PI);
    return outRotation;
  }

  @Method(0x801137f8L)
  public Vector3f getScaleDifference(final BattleObject other, final Vector3f out) {
    return out.set(this.getScale()).sub(other.getScale());
  }

  /** NOTE: fixed a retail bug using scriptIndex1 instead of scriptIndex2 */
  @Method(0x80113624L)
  public Vector3f getScaleRatio(final BattleObject other, final Vector3f outScale) {
    final Vector3f scale = this.getScale();
    final Vector3f otherScale = other.getScale();

    if(otherScale.x == 0.0f) {
      otherScale.x = Float.MIN_NORMAL;
    }

    if(otherScale.y == 0.0f) {
      otherScale.y = Float.MIN_NORMAL;
    }

    if(otherScale.z == 0.0f) {
      otherScale.z = Float.MIN_NORMAL;
    }

    outScale.x = scale.x / otherScale.x;
    outScale.y = scale.y / otherScale.y;
    outScale.z = scale.z / otherScale.z;

    return outScale;
  }

  @Method(0x8011441cL)
  public USCOLOUR getColourDifference(final BattleObject other, final USCOLOUR outColour) {
    return outColour.set(this.getColour()).sub(other.getColour());
  }
}
