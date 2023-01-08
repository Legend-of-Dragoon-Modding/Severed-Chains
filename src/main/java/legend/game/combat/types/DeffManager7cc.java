package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.game.types.MrgFile;

public class DeffManager7cc {
  public Struct08 _00 = new Struct08();
  public final Struct04[] _08 = {new Struct04(), new Struct04(), new Struct04(), new Struct04(), new Struct04()};
  public int scriptIndex_1c;
  public int _20;
  // TODO this was just set to a pointer to _28, but _28 wasn't used
//  public final UnsignedIntRef ptr_24;
  // TODO unused?
//  public final Value _28;
  //TODO sub-structs from here down?
  public MrgFile mrg_2c;
  public long ptr_30;
  public long ptr_34;
  public DeffFile deff_38;

  public final BattleStruct4c _4c = new BattleStruct4c();
  public final BattleStruct4c[] _98 = {new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c(), new BattleStruct4c()};
  public final TmdObjTable[] tmds_2f8 = new TmdObjTable[38];
  public final DeffPart[] _390 = new DeffPart[3];
  public final SpriteMetrics08[] spriteMetrics_39c = {new SpriteMetrics08(), new SpriteMetrics08(), new SpriteMetrics08(), new SpriteMetrics08()};
  public int u_3ac;
  public int v_3ae;

  public int clut_3b2;

  public MrgFile deffPackage_5a8;
  public DeffFile deff_5ac;

  public BattleStruct24_2 _5b8 = new BattleStruct24_2();
  public BattleLightStruct64 _5dc = new BattleLightStruct64();
  public BttlLightStruct84[] _640 = {new BttlLightStruct84(), new BttlLightStruct84(), new BttlLightStruct84()};

  public static class Struct08 {
    public int _00;
    public int _02;
    public int _04;
  }

  public static class Struct04 {
    public int _00;
    public int _02;
  }
}
