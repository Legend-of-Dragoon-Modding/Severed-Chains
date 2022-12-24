package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.combat.types.BattleStructEF4.AdditionExtra04;
import legend.game.combat.types.BattleStructEF4.SpecialEffects20;
import legend.game.combat.types.BattleStructEF4.Status04;
import legend.game.types.PartyPermutation08;

public class BattleStructEf4 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<SpecialEffects20> specialEffect_00;

  public final ArrayRef<IntRef> _180;

  /**
   * Number of times pressed during the Dragoon addition.
   */
  public final UnsignedByteRef dAttackValue_280;

  /**
   * <ul>
   *   <li>0x00 Dart</li>
   *   <li>0x01 Lavitz</li>
   *   <li>0x02 Shana</li>
   *   <li>0x03 Rose</li>
   *   <li>0x04 Haschel</li>
   *   <li>0x05 Albert</li>
   *   <li>0x06 Meru</li>
   *   <li>0x07 Kongol</li>
   *   <li>0x08 Miranda</li>
   * <ul>
   */
  public final UnsignedByteRef specialFlag_2b0;

  public final ArrayRef<Status04> status_384;

  public final ArrayRef<AdditionExtra04> additionExtra_474;

  public final BoolRef dragonBlockStaff_550;

  public final ArrayRef<BttlStruct08> _580;
  public final ArrayRef<IntRef> bobjIndices_d80;
  public final ArrayRef<BattleStructEf4Sub08> _d8c;
  public final ArrayRef<IntRef> bobjIndices_e0c;

  public final ArrayRef<IntRef> charBobjIndices_e40;
  public final ArrayRef<IntRef> bobjIndices_e50;

  public final ArrayRef<IntRef> bobjIndices_e78;

  public final ArrayRef<IntRef> bobjIndices_eac;
  public final ArrayRef<IntRef> enemyBobjIndices_ebc;

  public final ByteRef morphMode_ee4;

  /** Note: nodart code no longer uses this */
  public final Pointer<PartyPermutation08> partyPermutation_ee8;
  public final IntRef stageProgression_eec;
  /** Used by script engine */
  public final IntRef _ef0;

  public BattleStructEf4(final Value ref) {
    this.ref = ref;

    this.specialEffect_00 = ref.offset(4, 0x00L).cast(ArrayRef.of(SpecialEffects20.class, 10, 0x20, SpecialEffects20::new));

    this._180 = ref.offset(4, 0x180L).cast(ArrayRef.of(IntRef.class, 0x100, 4, IntRef::new));

    this.dAttackValue_280 = ref.offset(1, 0x280L).cast(UnsignedByteRef::new);

    this.specialFlag_2b0 = ref.offset(1, 0x2b0L).cast(UnsignedByteRef::new);

    this.status_384 = ref.offset(4, 0x384L).cast(ArrayRef.of(Status04.class, 8, 0x4, Status04::new));

    this.additionExtra_474 = ref.offset(4, 0x474L).cast(ArrayRef.of(AdditionExtra04.class, 8, 0x4, AdditionExtra04::new));

    this.dragonBlockStaff_550 = ref.offset(1, 0x550L).cast(BoolRef::new);

    this._580 = ref.offset(4, 0x580L).cast(ArrayRef.of(BttlStruct08.class, 0x100, 0x8, BttlStruct08::new));
    this.bobjIndices_d80 = ref.offset(4, 0xd80L).cast(ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
    this._d8c = ref.offset(4, 0xd8cL).cast(ArrayRef.of(BattleStructEf4Sub08.class, 16, 8, BattleStructEf4Sub08::new));
    this.bobjIndices_e0c = ref.offset(4, 0xe0cL).cast(ArrayRef.of(IntRef.class, 12, 4, IntRef::new));

    this.charBobjIndices_e40 = ref.offset(4, 0xe40L).cast(ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
    this.bobjIndices_e50 = ref.offset(4, 0xe50L).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));
    this.bobjIndices_e78 = ref.offset(4, 0xe78L).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));

    this.bobjIndices_eac = ref.offset(4, 0xeacL).cast(ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
    this.enemyBobjIndices_ebc = ref.offset(4, 0xebcL).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));

    this.morphMode_ee4 = ref.offset(1, 0xee4L).cast(ByteRef::new);

    this.partyPermutation_ee8 = ref.offset(4, 0xee8L).cast(Pointer.deferred(4, PartyPermutation08::new));
    this.stageProgression_eec = ref.offset(4, 0xeecL).cast(IntRef::new);
    this._ef0 = ref.offset(4, 0xef0L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
