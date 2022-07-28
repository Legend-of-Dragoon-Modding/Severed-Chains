package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.ExtendedTmd;
import legend.game.types.MrgFile;

/** Data related to a combatant (player or enemy) */
public class CombatantStruct1a8 implements MemoryRef {
  private final Value ref;

  public final Pointer<MrgFile> mrg_00;
  /** TODO ptr */
  public final UnsignedIntRef _04;
  public final Pointer<ExtendedTmd> tmd_08;

  /** This seems to be either a script or an extended TMD */
  public final UnsignedIntRef filePtr_10;
  public final ArrayRef<BattleStruct1a8_c> _14;
  public final UnsignedIntRef _194;
  public final UnsignedIntRef _198;
  public final ShortRef charSlot_19c;
  /**
   * 0x1 - used?
   * 0x4 - player (not NPC)
   */
  public final UnsignedShortRef flags_19e;
  public final ShortRef _1a0;
  /** Maybe not strictly char index? */
  public final ShortRef charIndex_1a2;
  public final ShortRef _1a4;
  public final ShortRef _1a6;

  public CombatantStruct1a8(final Value ref) {
    this.ref = ref;

    this.mrg_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, MrgFile::new));
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.tmd_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, ExtendedTmd::new));

    this.filePtr_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(BattleStruct1a8_c.class, 32, 0xc, BattleStruct1a8_c::new));
    this._194 = ref.offset(4, 0x194L).cast(UnsignedIntRef::new);
    this._198 = ref.offset(4, 0x198L).cast(UnsignedIntRef::new);
    this.charSlot_19c = ref.offset(2, 0x19cL).cast(ShortRef::new);
    this.flags_19e = ref.offset(2, 0x19eL).cast(UnsignedShortRef::new);
    this._1a0 = ref.offset(2, 0x1a0L).cast(ShortRef::new);
    this.charIndex_1a2 = ref.offset(2, 0x1a2L).cast(ShortRef::new);
    this._1a4 = ref.offset(2, 0x1a4L).cast(ShortRef::new);
    this._1a6 = ref.offset(2, 0x1a6L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
