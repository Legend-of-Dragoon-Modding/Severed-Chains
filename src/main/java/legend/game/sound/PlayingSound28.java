package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class PlayingSound28 implements MemoryRef {
  private final Value ref;

  /**
   * <ol start="0">
   *   <li>unused</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>sounds?</li>
   *   <li>?</li>
   * </ul>
   */
  public final UnsignedByteRef type_00;

  public final IntRef bobjIndex_04;
  public final IntRef soundFileIndex_08;
  public final IntRef soundIndex_0c;
  /** I think if this has flag 0x80 set it enables reverb? I don't think it's ever used? */
  public final ShortRef playableSoundIndex_10;
  public final ShortRef patchIndex_12;
  public final ShortRef sequenceIndex_14;
  public final ShortRef pitchShiftVolRight_16;
  public final ShortRef pitchShiftVolLeft_18;
  public final ShortRef pitch_1a;
  public final UnsignedIntRef _1c;
  public final ShortRef _20;
  public final ShortRef _22;
  public final ShortRef _24;

  public PlayingSound28(final Value ref) {
    this.ref = ref;

    this.type_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.bobjIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.soundFileIndex_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.soundIndex_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.playableSoundIndex_10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this.patchIndex_12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this.sequenceIndex_14 = ref.offset(2, 0x14L).cast(ShortRef::new);
    this.pitchShiftVolRight_16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this.pitchShiftVolLeft_18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this.pitch_1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
