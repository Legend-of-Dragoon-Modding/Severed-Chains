package legend.game;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.game.combat.types.BattleStructEf4;
import legend.game.combat.types.BttlStruct08;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8006 {
  private Scus94491BpeSegment_8006() { }

  //TODO all of this is part of this struct
  public static final BattleStructEf4 _8006e398 = MEMORY.ref(4, 0x8006e398L, BattleStructEf4::new);

  public static final ArrayRef<BttlStruct08> _8006e918 = MEMORY.ref(4, 0x8006e918L, ArrayRef.of(BttlStruct08.class, 0x100, 0x8, BttlStruct08::new));

  public static final Value _8006f1a4 = MEMORY.ref(4, 0x8006f1a4L);

  public static final Value _8006f1d8 = MEMORY.ref(4, 0x8006f1d8L);

  public static final Value _8006f1e8 = MEMORY.ref(4, 0x8006f1e8L);

  /** The same type as {@link Scus94491BpeSegment_8006#_8006f1d8} */
  public static final Value _8006f210 = MEMORY.ref(4, 0x8006f210L);

  public static final Value _8006f244 = MEMORY.ref(4, 0x8006f244L);

  /** The same type as {@link Scus94491BpeSegment_8006#_8006f1d8} */
  public static final Value _8006f254 = MEMORY.ref(4, 0x8006f254L);
}
