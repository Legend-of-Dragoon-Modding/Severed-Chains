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

public class BattleObject27c extends BattleScriptDataBase {
  public final ArrayRef<ShortRef> all_04; // Note: overlaps all the way to _144
  public final UnsignedShortRef level_04;
  public final UnsignedShortRef dlevel_06;
  public final UnsignedShortRef hp_08;
  public final ShortRef sp_0a;
  public final UnsignedShortRef mp_0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef maxHp_10;
  public final UnsignedShortRef maxMp_12;
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
  public final UnsignedShortRef speed_32;
  public final UnsignedShortRef attack_34;
  public final UnsignedShortRef magicAttack_36;
  public final UnsignedShortRef defence_38;
  public final UnsignedShortRef magicDefence_3a;
  public final UnsignedShortRef attackHit_3c;
  public final UnsignedShortRef magicHit_3e;
  public final UnsignedShortRef attackAvoid_40;
  public final UnsignedShortRef magicAvoid_42;
  public final UnsignedShortRef _44;
  public final UnsignedShortRef _46;
  public final UnsignedShortRef _48;
  public final UnsignedShortRef _4a;
  public final ShortRef _4c;
  public final ShortRef _4e;

  public final ShortRef _52;

  public final ShortRef _56;
  public final ShortRef selectedAddition_58;

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

  public final UnsignedShortRef _94;
  public final UnsignedShortRef _96;

  public final ShortRef _9c;

  public final UnsignedShortRef _a0;

  public final UnsignedShortRef _a4;

  public final ShortRef _a8;

  public final UnsignedShortRef dragoonAttack_ac;
  public final UnsignedShortRef dragoonMagic_ae;
  public final UnsignedShortRef dragoonDefence_b0;
  public final UnsignedShortRef dragoonMagicDefence_b2;
  public final ShortRef _b4;
  public final ShortRef _b6;
  public final ShortRef _b8;
  public final ShortRef _ba;

  public final ShortRef _c8;
  public final ShortRef _ca;
  public final ShortRef _cc;
  public final ShortRef _ce;
  public final ShortRef _d0;
  public final ShortRef _d2;
  public final ShortRef _d4;
  public final ShortRef _d6;
  public final ShortRef _d8;
  public final ShortRef _da;
  public final ShortRef _dc;
  public final ShortRef _de;
  public final UnsignedByteRef _e0;

  public final ShortRef _e6;

  public final ShortRef _ea;

  public final UnsignedShortRef _110;
  public final UnsignedShortRef _112;
  public final UnsignedShortRef _114;
  public final UnsignedShortRef _116;
  public final UnsignedShortRef _118;
  public final UnsignedShortRef _11a;
  public final ShortRef _11c;
  public final UnsignedShortRef equipment0_11e;
  public final UnsignedShortRef equipment1_120;
  public final UnsignedShortRef equipment2_122;
  public final UnsignedShortRef equipment3_124;
  public final UnsignedShortRef equipment4_126;
  public final UnsignedShortRef _128;
  public final UnsignedShortRef _12a;
  public final UnsignedShortRef _12c;
  public final UnsignedShortRef _12e;
  public final UnsignedShortRef _130;
  public final UnsignedShortRef _132;
  public final UnsignedShortRef _134;
  public final UnsignedShortRef _136;
  public final UnsignedShortRef _138;
  public final UnsignedShortRef _13a;
  public final UnsignedShortRef _13c;
  public final UnsignedShortRef _13e;

  public final UnsignedShortRef _142;
  public final Pointer<CombatantStruct1a8> combatant_144;
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

  public final ShortRef combatantIndex_26c;
  public final ShortRef animIndex_26e;
  public final ShortRef animIndex_270;
  public final ShortRef charIndex_272;
  public final ShortRef _274;
  public final ShortRef charSlot_276;
  public final UnsignedByteRef _278;

  public BattleObject27c(final Value ref) {
    super(ref);

    this.all_04 = ref.offset(2, 0x04L).cast(ArrayRef.of(ShortRef.class, 0xa0, 2, ShortRef::new));
    this.level_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.dlevel_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.hp_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.sp_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.mp_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.maxHp_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.maxMp_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
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
    this.speed_32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this.attack_34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this.magicAttack_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this.defence_38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);
    this.magicDefence_3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this.attackHit_3c = ref.offset(2, 0x3cL).cast(UnsignedShortRef::new);
    this.magicHit_3e = ref.offset(2, 0x3eL).cast(UnsignedShortRef::new);
    this.attackAvoid_40 = ref.offset(2, 0x40L).cast(UnsignedShortRef::new);
    this.magicAvoid_42 = ref.offset(2, 0x42L).cast(UnsignedShortRef::new);
    this._44 = ref.offset(2, 0x44L).cast(UnsignedShortRef::new);
    this._46 = ref.offset(2, 0x46L).cast(UnsignedShortRef::new);
    this._48 = ref.offset(2, 0x48L).cast(UnsignedShortRef::new);
    this._4a = ref.offset(2, 0x4aL).cast(UnsignedShortRef::new);
    this._4c = ref.offset(2, 0x4cL).cast(ShortRef::new);
    this._4e = ref.offset(2, 0x4eL).cast(ShortRef::new);

    this._52 = ref.offset(2, 0x52L).cast(ShortRef::new);

    this._56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this.selectedAddition_58 = ref.offset(2, 0x58L).cast(ShortRef::new);

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

    this._94 = ref.offset(2, 0x94L).cast(UnsignedShortRef::new);
    this._96 = ref.offset(2, 0x96L).cast(UnsignedShortRef::new);

    this._9c = ref.offset(2, 0x9cL).cast(ShortRef::new);

    this._a0 = ref.offset(2, 0xa0L).cast(UnsignedShortRef::new);

    this._a4 = ref.offset(2, 0xa4L).cast(UnsignedShortRef::new);

    this._a8 = ref.offset(2, 0xa8L).cast(ShortRef::new);

    this.dragoonAttack_ac = ref.offset(2, 0xacL).cast(UnsignedShortRef::new);
    this.dragoonMagic_ae = ref.offset(2, 0xaeL).cast(UnsignedShortRef::new);
    this.dragoonDefence_b0 = ref.offset(2, 0xb0L).cast(UnsignedShortRef::new);
    this.dragoonMagicDefence_b2 = ref.offset(2, 0xb2L).cast(UnsignedShortRef::new);
    this._b4 = ref.offset(2, 0xb4L).cast(ShortRef::new);
    this._b6 = ref.offset(2, 0xb6L).cast(ShortRef::new);
    this._b8 = ref.offset(2, 0xb8L).cast(ShortRef::new);
    this._ba = ref.offset(2, 0xbaL).cast(ShortRef::new);

    this._c8 = ref.offset(2, 0xc8L).cast(ShortRef::new);
    this._ca = ref.offset(2, 0xcaL).cast(ShortRef::new);
    this._cc = ref.offset(2, 0xccL).cast(ShortRef::new);
    this._ce = ref.offset(2, 0xceL).cast(ShortRef::new);
    this._d0 = ref.offset(2, 0xd0L).cast(ShortRef::new);
    this._d2 = ref.offset(2, 0xd2L).cast(ShortRef::new);
    this._d4 = ref.offset(2, 0xd4L).cast(ShortRef::new);
    this._d6 = ref.offset(2, 0xd6L).cast(ShortRef::new);
    this._d8 = ref.offset(2, 0xd8L).cast(ShortRef::new);
    this._da = ref.offset(2, 0xdaL).cast(ShortRef::new);
    this._dc = ref.offset(2, 0xdcL).cast(ShortRef::new);
    this._de = ref.offset(2, 0xdeL).cast(ShortRef::new);
    this._e0 = ref.offset(1, 0xe0L).cast(UnsignedByteRef::new);

    this._e6 = ref.offset(2, 0xe6L).cast(ShortRef::new);

    this._ea = ref.offset(2, 0xeaL).cast(ShortRef::new);

    this._110 = ref.offset(2, 0x110L).cast(UnsignedShortRef::new);
    this._112 = ref.offset(2, 0x112L).cast(UnsignedShortRef::new);
    this._114 = ref.offset(2, 0x114L).cast(UnsignedShortRef::new);
    this._116 = ref.offset(2, 0x116L).cast(UnsignedShortRef::new);
    this._118 = ref.offset(2, 0x118L).cast(UnsignedShortRef::new);
    this._11a = ref.offset(2, 0x11aL).cast(UnsignedShortRef::new);
    this._11c = ref.offset(2, 0x11cL).cast(ShortRef::new);
    this.equipment0_11e = ref.offset(2, 0x11eL).cast(UnsignedShortRef::new);
    this.equipment1_120 = ref.offset(2, 0x120L).cast(UnsignedShortRef::new);
    this.equipment2_122 = ref.offset(2, 0x122L).cast(UnsignedShortRef::new);
    this.equipment3_124 = ref.offset(2, 0x124L).cast(UnsignedShortRef::new);
    this.equipment4_126 = ref.offset(2, 0x126L).cast(UnsignedShortRef::new);
    this._128 = ref.offset(2, 0x128L).cast(UnsignedShortRef::new);
    this._12a = ref.offset(2, 0x12aL).cast(UnsignedShortRef::new);
    this._12c = ref.offset(2, 0x12cL).cast(UnsignedShortRef::new);
    this._12e = ref.offset(2, 0x12eL).cast(UnsignedShortRef::new);
    this._130 = ref.offset(2, 0x130L).cast(UnsignedShortRef::new);
    this._132 = ref.offset(2, 0x132L).cast(UnsignedShortRef::new);
    this._134 = ref.offset(2, 0x134L).cast(UnsignedShortRef::new);
    this._136 = ref.offset(2, 0x136L).cast(UnsignedShortRef::new);
    this._138 = ref.offset(2, 0x138L).cast(UnsignedShortRef::new);
    this._13a = ref.offset(2, 0x13aL).cast(UnsignedShortRef::new);
    this._13c = ref.offset(2, 0x13cL).cast(UnsignedShortRef::new);
    this._13e = ref.offset(2, 0x13eL).cast(UnsignedShortRef::new);

    this._142 = ref.offset(2, 0x142L).cast(UnsignedShortRef::new);
    this.combatant_144 = ref.offset(4, 0x144L).cast(Pointer.deferred(4, CombatantStruct1a8::new));
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

    this.combatantIndex_26c = ref.offset(2, 0x26cL).cast(ShortRef::new);
    this.animIndex_26e = ref.offset(2, 0x26eL).cast(ShortRef::new);
    this.animIndex_270 = ref.offset(2, 0x270L).cast(ShortRef::new);
    this.charIndex_272 = ref.offset(2, 0x272L).cast(ShortRef::new);
    this._274 = ref.offset(2, 0x274L).cast(ShortRef::new);
    this.charSlot_276 = ref.offset(2, 0x276L).cast(ShortRef::new);
    this._278 = ref.offset(1, 0x278L).cast(UnsignedByteRef::new);
  }
}
