package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class WorldObject210 implements MemoryRef {
  private final Value ref;

  public final Model124 model_00;
  public final Pointer<MrgFile> mrg_124;
  public final ShortRef s_128;
  public final UnsignedShortRef us_12a;
  public final UnsignedShortRef us_12c;
  public final UnsignedShortRef mrgAnimGroup_12e;
  /** The script index of this wobj */
  public final UnsignedShortRef wobjIndex_130;
  public final UnsignedShortRef mrgAnimGroupIndex_132;
  public final ShortRef s_134;

  public final VECTOR vec_138;
  public final IntRef i_144;
  public final VECTOR vec_148;
  public final VECTOR vec_154;
  public final VECTOR vec_160;
  public final IntRef ui_16c;
  public final UnsignedShortRef us_170;
  public final ShortRef s_172;
  public final ShortRef s_174;

  public final ShortRef s_178;

  /** The amount to rotate this wobj by each frame for {@link WorldObject210#rotationFrames_188} frames */
  public final VECTOR rotationAmount_17c;
  /** Number of frames to apply rotation for */
  public final IntRef rotationFrames_188;
  public final UnsignedIntRef ui_18c;
  /**
   * Example flags for the player wobj - 0x120_0001
   *
   * <ul>
   *   <li>0x1 - player</li>
   *   <li>0x10_0000 - player can collide with</li>
   *   <li>0x20_0000 - collision something</li>
   *   <li>0x40_0000 - collision something</li>
   *   <li>0x80_0000 - collision something</li>
   *   <li>0x100_0000 - collision something</li>
   *   <li>0x200_0000 - collision something</li>
   *   <li>0x400_0000 - collision something</li>
   *   <li>0x800_0000 - collision something</li>
   *   <li>0x1000_0000 - collision something</li>
   *   <li>0x2000_0000 - ?</li>
   *   <li>0x4000_0000 - ?</li>
   *   <li>0x8000_0000 - ?</li>
   * </ul>
   */
  public final UnsignedIntRef flags_190;
  /** The yellow &lt;!&gt; */
  public final BoolRef showAlertIndicator_194;
  public final IntRef alertIndicatorOffsetY_198;
  /** The wobj that this wobj is currently collided with (unknown how this differs from _1a8) */
  public final IntRef collidedWithWobjIndex_19c;
  public final IntRef collisionSizeHorizontal_1a0;
  public final IntRef collisionSizeVertical_1a4;
  /** The wobj that this wobj is currently collided with (unknown how this differs from _19c) */
  public final IntRef collidedWithWobjIndex_1a8;
  public final IntRef collisionSizeHorizontal_1ac;
  public final IntRef collisionSizeVertical_1b0;
  /** Not exactly sure what the point of this is... it extends the collision box in the direction the wobj is facing */
  public final IntRef collisionReach_1b4;
  public final IntRef playerCollisionSizeHorizontal_1b8;
  public final IntRef playerCollisionSizeVertical_1bc;
  /** Not exactly sure what the point of this is... it extends the collision box in the direction the wobj is facing */
  public final IntRef playerCollisionReach_1c0;
  public final BoolRef flatLightingEnabled_1c4;
  public final UnsignedByteRef flatLightRed_1c5;
  public final UnsignedByteRef flatLightGreen_1c6;
  public final UnsignedByteRef flatLightBlue_1c7;
  public final BoolRef ambientColourEnabled_1c8;

  public final UnsignedShortRef ambientRed_1ca;
  public final UnsignedShortRef ambientGreen_1cc;
  public final UnsignedShortRef ambientBlue_1ce;
  public final BigSubStruct _1d0;

  public WorldObject210(final Value ref) {
    this.ref = ref;

    this.model_00 = ref.offset(4, 0x000L).cast(Model124::new);
    this.mrg_124 = ref.offset(4, 0x124L).cast(Pointer.deferred(4, MrgFile::new));
    this.s_128 = ref.offset(2, 0x128L).cast(ShortRef::new);
    this.us_12a = ref.offset(2, 0x12aL).cast(UnsignedShortRef::new);
    this.us_12c = ref.offset(2, 0x12cL).cast(UnsignedShortRef::new);
    this.mrgAnimGroup_12e = ref.offset(2, 0x12eL).cast(UnsignedShortRef::new);
    this.wobjIndex_130 = ref.offset(2, 0x130L).cast(UnsignedShortRef::new);
    this.mrgAnimGroupIndex_132 = ref.offset(2, 0x132L).cast(UnsignedShortRef::new);
    this.s_134 = ref.offset(2, 0x134L).cast(ShortRef::new);

    this.vec_138 = ref.offset(4, 0x138L).cast(VECTOR::new);
    this.i_144 = ref.offset(4, 0x144L).cast(IntRef::new);
    this.vec_148 = ref.offset(4, 0x148L).cast(VECTOR::new);
    this.vec_154 = ref.offset(4, 0x154L).cast(VECTOR::new);
    this.vec_160 = ref.offset(4, 0x160L).cast(VECTOR::new);
    this.ui_16c = ref.offset(4, 0x16cL).cast(IntRef::new);
    this.us_170 = ref.offset(2, 0x170L).cast(UnsignedShortRef::new);
    this.s_172 = ref.offset(2, 0x172L).cast(ShortRef::new);
    this.s_174 = ref.offset(2, 0x174L).cast(ShortRef::new);

    this.s_178 = ref.offset(2, 0x178L).cast(ShortRef::new);

    this.rotationAmount_17c = ref.offset(2, 0x17cL).cast(VECTOR::new);
    this.rotationFrames_188 = ref.offset(4, 0x188L).cast(IntRef::new);
    this.ui_18c = ref.offset(4, 0x18cL).cast(UnsignedIntRef::new);
    this.flags_190 = ref.offset(4, 0x190L).cast(UnsignedIntRef::new);
    this.showAlertIndicator_194 = ref.offset(4, 0x194L).cast(BoolRef::new);
    this.alertIndicatorOffsetY_198 = ref.offset(4, 0x198L).cast(IntRef::new);
    this.collidedWithWobjIndex_19c = ref.offset(4, 0x19cL).cast(IntRef::new);
    this.collisionSizeHorizontal_1a0 = ref.offset(4, 0x1a0L).cast(IntRef::new);
    this.collisionSizeVertical_1a4 = ref.offset(4, 0x1a4L).cast(IntRef::new);
    this.collidedWithWobjIndex_1a8 = ref.offset(4, 0x1a8L).cast(IntRef::new);
    this.collisionSizeHorizontal_1ac = ref.offset(4, 0x1acL).cast(IntRef::new);
    this.collisionSizeVertical_1b0 = ref.offset(4, 0x1b0L).cast(IntRef::new);
    this.collisionReach_1b4 = ref.offset(4, 0x1b4L).cast(IntRef::new);
    this.playerCollisionSizeHorizontal_1b8 = ref.offset(4, 0x1b8L).cast(IntRef::new);
    this.playerCollisionSizeVertical_1bc = ref.offset(4, 0x1bcL).cast(IntRef::new);
    this.playerCollisionReach_1c0 = ref.offset(4, 0x1c0L).cast(IntRef::new);
    this.flatLightingEnabled_1c4 = ref.offset(1, 0x1c4L).cast(BoolRef::new);
    this.flatLightRed_1c5 = ref.offset(1, 0x1c5L).cast(UnsignedByteRef::new);
    this.flatLightGreen_1c6 = ref.offset(1, 0x1c6L).cast(UnsignedByteRef::new);
    this.flatLightBlue_1c7 = ref.offset(1, 0x1c7L).cast(UnsignedByteRef::new);
    this.ambientColourEnabled_1c8 = ref.offset(1, 0x1c8L).cast(BoolRef::new);

    this.ambientRed_1ca = ref.offset(2, 0x1caL).cast(UnsignedShortRef::new);
    this.ambientGreen_1cc = ref.offset(2, 0x1ccL).cast(UnsignedShortRef::new);
    this.ambientBlue_1ce = ref.offset(2, 0x1ceL).cast(UnsignedShortRef::new);
    this._1d0 = ref.offset(4, 0x1d0L).cast(BigSubStruct::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
