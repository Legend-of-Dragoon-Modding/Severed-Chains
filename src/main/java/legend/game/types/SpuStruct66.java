package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct66 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef used_00;
  public final UnsignedShortRef _02;
  public final UnsignedShortRef channel_04;
  public final UnsignedShortRef channelIndex_06;
  public final UnsignedShortRef _08;
  public final UnsignedShortRef _0a;
  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;
  public final UnsignedShortRef _1a;
  public final UnsignedShortRef _1c;
  public final UnsignedShortRef _1e;
  public final UnsignedShortRef _20;
  public final UnsignedShortRef playableSoundIndex_22;
  public final UnsignedShortRef _24;
  public final UnsignedShortRef _26;
  public final UnsignedShortRef _28;
  public final UnsignedShortRef _2a;
  public final UnsignedShortRef _2c;
  public final UnsignedShortRef _2e;
  public final UnsignedShortRef _30;
  public final UnsignedShortRef _32;
  public final UnsignedShortRef _34;
  public final UnsignedShortRef _36;
  public final UnsignedShortRef _38;
  public final UnsignedShortRef _3a;
  public final UnsignedShortRef _3c;
  public final UnsignedShortRef _3e;
  public final UnsignedShortRef _40;
  public final UnsignedShortRef _42;
  public final UnsignedShortRef _44;
  public final UnsignedShortRef _46;
  public final UnsignedShortRef _48;
  public final UnsignedShortRef _4a;
  public final UnsignedShortRef _4c;
  public final UnsignedShortRef _4e;
  public final UnsignedShortRef _50;
  public final UnsignedShortRef _52;
  public final UnsignedShortRef _54;
  public final UnsignedShortRef _56;
  public final UnsignedShortRef _58;
  public final UnsignedShortRef _5a;
  public final UnsignedShortRef _5c;
  public final UnsignedShortRef _5e;
  public final UnsignedShortRef _60;
  public final UnsignedShortRef _62;
  public final UnsignedShortRef _64;

  public SpuStruct66(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.channel_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.channelIndex_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(UnsignedShortRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(UnsignedShortRef::new);
    this.playableSoundIndex_22 = ref.offset(2, 0x22L).cast(UnsignedShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(UnsignedShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(UnsignedShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(UnsignedShortRef::new);
    this._30 = ref.offset(2, 0x30L).cast(UnsignedShortRef::new);
    this._32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this._34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this._38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);
    this._3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this._3c = ref.offset(2, 0x3cL).cast(UnsignedShortRef::new);
    this._3e = ref.offset(2, 0x3eL).cast(UnsignedShortRef::new);
    this._40 = ref.offset(2, 0x40L).cast(UnsignedShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(UnsignedShortRef::new);
    this._44 = ref.offset(2, 0x44L).cast(UnsignedShortRef::new);
    this._46 = ref.offset(2, 0x46L).cast(UnsignedShortRef::new);
    this._48 = ref.offset(2, 0x48L).cast(UnsignedShortRef::new);
    this._4a = ref.offset(2, 0x4aL).cast(UnsignedShortRef::new);
    this._4c = ref.offset(2, 0x4cL).cast(UnsignedShortRef::new);
    this._4e = ref.offset(2, 0x4eL).cast(UnsignedShortRef::new);
    this._50 = ref.offset(2, 0x50L).cast(UnsignedShortRef::new);
    this._52 = ref.offset(2, 0x52L).cast(UnsignedShortRef::new);
    this._54 = ref.offset(2, 0x54L).cast(UnsignedShortRef::new);
    this._56 = ref.offset(2, 0x56L).cast(UnsignedShortRef::new);
    this._58 = ref.offset(2, 0x58L).cast(UnsignedShortRef::new);
    this._5a = ref.offset(2, 0x5aL).cast(UnsignedShortRef::new);
    this._5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this._5e = ref.offset(2, 0x5eL).cast(UnsignedShortRef::new);
    this._60 = ref.offset(2, 0x60L).cast(UnsignedShortRef::new);
    this._62 = ref.offset(2, 0x62L).cast(UnsignedShortRef::new);
    this._64 = ref.offset(2, 0x64L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
