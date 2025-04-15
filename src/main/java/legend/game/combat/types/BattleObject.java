package legend.game.combat.types;

import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.scripting.ScriptedObject;
import org.joml.Vector3f;
import org.joml.Vector3i;

public abstract class BattleObject implements ScriptedObject {
  public static String EM__ = "EM  ";
  public static String BOBJ = "BOBJ";

  public final String magic_00;

  protected BattleObject(final String magic) {
    this.magic_00 = magic;
  }

  @Override
  public abstract Vector3f getPosition();
  public abstract Vector3f getRotation();
  public abstract Vector3f getScale();
  @Override
  public abstract Vector3i getColour();

  /** Translates a position relative to this BattleObject's local coordinate system */
  @Method(0x801105ccL)
  public void getRelativePosition(final Vector3f in, final Vector3f out) {
    final MV parentRotationMatrix = new MV();
    parentRotationMatrix.rotationXYZ(this.getRotation());
    in.mul(parentRotationMatrix, out).add(this.getPosition());
  }

  /** Translates a position relative to this BattleObject's local coordinate system */
  @Method(0x801105ccL)
  public void getRelativePosition(final Vector3f inOut) {
    this.getRelativePosition(inOut, inOut);
  }

  /** Returns the relative position from a parent BattleObject's local coordinate system */
  public Vector3f getRelativePositionTo(final BattleObject child, final Vector3f out) {
    return getRelativePositionBetween(child, this, out);
  }

  /** Returns the relative position from a parent BattleObject's local coordinate system */
  @Method(0x8011035cL)
  public Vector3f getRelativePositionFrom(final BattleObject parent, final Vector3f out) {
    return getRelativePositionBetween(this, parent, out);
  }

  private static Vector3f getRelativePositionBetween(final BattleObject child, final BattleObject parent, final Vector3f out) {
    final Vector3f deltaPosition = new Vector3f().set(child.getPosition()).sub(parent.getPosition());
    final MV parentRotationMatrix = new MV();
    parentRotationMatrix.rotationXYZ(parent.getRotation());

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
  public Vector3i getColourDifference(final BattleObject other, final Vector3i outColour) {
    return outColour.set(this.getColour()).sub(other.getColour());
  }
}
