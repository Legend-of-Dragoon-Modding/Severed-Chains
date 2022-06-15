package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.BigStruct;

public class BtldScriptData27c extends BattleScriptDataBase {
  public final ArrayRef<ShortRef> _04; // Note: overlaps all the way to _144
  public final UnsignedShortRef _08;
  public final ShortRef _0a;
  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;
  public final UnsignedShortRef _1a;
  public final ShortRef _1c;
  public final UnsignedShortRef _1e;
  public final UnsignedShortRef _20;
  public final UnsignedShortRef _22;
  public final UnsignedShortRef _24;
  public final UnsignedShortRef _26;
  public final UnsignedShortRef _28;
  public final UnsignedShortRef _2a;
  public final UnsignedShortRef _2c;
  public final UnsignedShortRef _2e;
  public final UnsignedShortRef _30;
  public final ShortRef _32;
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
  public final ShortRef _4c;

  public final ShortRef _58;

  public final UnsignedShortRef _5c;
  public final UnsignedShortRef _5e;
  public final UnsignedShortRef _60;
  public final UnsignedShortRef _62;
  public final UnsignedShortRef _64;
  public final UnsignedShortRef _66;
  public final UnsignedShortRef _68;
  public final UnsignedShortRef _6a;
  public final UnsignedShortRef _6c;
  public final UnsignedShortRef _6e;
  public final UnsignedShortRef _70;
  public final UnsignedShortRef _72;
  public final UnsignedShortRef _74;
  public final UnsignedShortRef _76;
  public final SVECTOR _78;
  public final UnsignedShortRef _7e;
  public final UnsignedShortRef _80;
  public final UnsignedShortRef _82;
  public final ShortRef _84;
  public final ShortRef _86;
  public final ShortRef _88;
  public final ShortRef _8a;

  public final UnsignedShortRef _a4;

  public final ShortRef _b8;

  public final UnsignedShortRef _110;
  public final UnsignedShortRef _112;

  public final Pointer<BattleStruct1a8> _144;
  public final BigStruct _148;

  //TODO maybe part of previous struct
  public final UnsignedByteRef _1e4;
  public final UnsignedByteRef _1e5;
  public final ShortRef _1e6;

  public final UnsignedByteRef _1ea;

  public final UnsignedByteRef _214;
  public final UnsignedByteRef _215;

  public final VECTOR _244;

  public final UnsignedIntRef _254;

  public final UnsignedIntRef _25c;

  public final ShortRef _26c;
  public final ShortRef _26e;
  public final ShortRef _270;
  public final ShortRef _272;
  public final ShortRef _274;
  public final ShortRef _276;
  public final UnsignedByteRef _278;

  public BtldScriptData27c(final Value ref) {
    super(ref);

    this._04 = ref.offset(2, 0x04L).cast(ArrayRef.of(ShortRef.class, 0xa0, 2, ShortRef::new));
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(UnsignedShortRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(UnsignedShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(UnsignedShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(UnsignedShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(UnsignedShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(UnsignedShortRef::new);
    this._30 = ref.offset(2, 0x30L).cast(UnsignedShortRef::new);
    this._32 = ref.offset(2, 0x32L).cast(ShortRef::new);
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
    this._4c = ref.offset(2, 0x4cL).cast(ShortRef::new);

    this._58 = ref.offset(2, 0x58L).cast(ShortRef::new);

    this._5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this._5e = ref.offset(2, 0x5eL).cast(UnsignedShortRef::new);
    this._60 = ref.offset(2, 0x60L).cast(UnsignedShortRef::new);
    this._62 = ref.offset(2, 0x62L).cast(UnsignedShortRef::new);
    this._64 = ref.offset(2, 0x64L).cast(UnsignedShortRef::new);
    this._66 = ref.offset(2, 0x66L).cast(UnsignedShortRef::new);
    this._68 = ref.offset(2, 0x68L).cast(UnsignedShortRef::new);
    this._6a = ref.offset(2, 0x6aL).cast(UnsignedShortRef::new);
    this._6c = ref.offset(2, 0x6cL).cast(UnsignedShortRef::new);
    this._6e = ref.offset(2, 0x6eL).cast(UnsignedShortRef::new);
    this._70 = ref.offset(2, 0x70L).cast(UnsignedShortRef::new);
    this._72 = ref.offset(2, 0x72L).cast(UnsignedShortRef::new);
    this._74 = ref.offset(2, 0x74L).cast(UnsignedShortRef::new);
    this._76 = ref.offset(2, 0x76L).cast(UnsignedShortRef::new);
    this._78 = ref.offset(2, 0x78L).cast(SVECTOR::new);
    this._7e = ref.offset(2, 0x7eL).cast(UnsignedShortRef::new);
    this._80 = ref.offset(2, 0x80L).cast(UnsignedShortRef::new);
    this._82 = ref.offset(2, 0x82L).cast(UnsignedShortRef::new);
    this._84 = ref.offset(2, 0x84L).cast(ShortRef::new);
    this._86 = ref.offset(2, 0x86L).cast(ShortRef::new);
    this._88 = ref.offset(2, 0x88L).cast(ShortRef::new);
    this._8a = ref.offset(2, 0x8aL).cast(ShortRef::new);

    this._a4 = ref.offset(2, 0xa4L).cast(UnsignedShortRef::new);

    this._b8 = ref.offset(2, 0xb8L).cast(ShortRef::new);

    this._110 = ref.offset(2, 0x110L).cast(UnsignedShortRef::new);
    this._112 = ref.offset(2, 0x112L).cast(UnsignedShortRef::new);

    this._144 = ref.offset(4, 0x144L).cast(Pointer.deferred(4, BattleStruct1a8::new));
    this._148 = ref.offset(4, 0x148L).cast(BigStruct::new);

    this._1e4 = ref.offset(1, 0x1e4L).cast(UnsignedByteRef::new);
    this._1e5 = ref.offset(1, 0x1e5L).cast(UnsignedByteRef::new);
    this._1e6 = ref.offset(2, 0x1e6L).cast(ShortRef::new);

    this._1ea = ref.offset(1, 0x1eaL).cast(UnsignedByteRef::new);

    this._214 = ref.offset(1, 0x214L).cast(UnsignedByteRef::new);
    this._215 = ref.offset(1, 0x215L).cast(UnsignedByteRef::new);

    this._244 = ref.offset(4, 0x244L).cast(VECTOR::new);

    this._254 = ref.offset(4, 0x254L).cast(UnsignedIntRef::new);

    this._25c = ref.offset(4, 0x25cL).cast(UnsignedIntRef::new);

    this._26c = ref.offset(2, 0x26cL).cast(ShortRef::new);
    this._26e = ref.offset(2, 0x26eL).cast(ShortRef::new);
    this._270 = ref.offset(2, 0x270L).cast(ShortRef::new);
    this._272 = ref.offset(2, 0x272L).cast(ShortRef::new);
    this._274 = ref.offset(2, 0x274L).cast(ShortRef::new);
    this._276 = ref.offset(2, 0x276L).cast(ShortRef::new);
    this._278 = ref.offset(1, 0x278L).cast(UnsignedByteRef::new);
  }
}
