package legend.game.combat.deff;

import legend.core.gte.TmdObjTable1c;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BttlLightStruct84;
import legend.game.combat.environment.StageAmbiance4c;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;

import java.util.Arrays;

public class DeffManager7cc {
  public Struct08 _00 = new Struct08();
  public final Struct04[] _08 = {new Struct04(), new Struct04(), new Struct04(), new Struct04(), new Struct04()};
  public ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> scriptState_1c;
  /**
   * <ul>
   *   <li>0x4_0000 - Has sounds?</li>
   *   <li>0x10_0000 - Has attack animations?</li>
   *   <li>0x60_0000 - Has combat stage effects?</li>
   * </ul>
   */
  public int flags_20;
  // TODO this was just set to a pointer to _28, but _28 wasn't used
//  public final UnsignedIntRef ptr_24;
  // TODO unused?
//  public final Value _28;
  //TODO sub-structs from here down?
  public ScriptFile[] scripts_2c;
//  public long ptr_30;
//  public long ptr_34;

  public final StageAmbiance4c stageAmbiance_4c = new StageAmbiance4c();
  /** One instance for each dragoon */
  public final StageAmbiance4c[] dragoonSpaceAmbiance_98 = {new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c(), new StageAmbiance4c()};
  /** Only type 3 TMDs (see {@link DeffPart#flags_00}) */
  public final TmdObjTable1c[] tmds_2f8 = new TmdObjTable1c[38];
  public final DeffPart.LmbType[] lmbs_390 = new DeffPart.LmbType[3];
  public final SpriteMetrics08[] spriteMetrics_39c = new SpriteMetrics08[65];
  public DeffPart[] deffPackage_5a8;
//  public DeffFile deff_5ac; // No longer needed

  public final BattleStruct24_2 _5b8 = new BattleStruct24_2();
  public final BattleLightStruct64 _5dc = new BattleLightStruct64();
  public final BttlLightStruct84[] _640 = {new BttlLightStruct84(), new BttlLightStruct84(), new BttlLightStruct84()};

  public DeffManager7cc() {
    Arrays.setAll(this.spriteMetrics_39c, i -> new SpriteMetrics08());
  }

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
