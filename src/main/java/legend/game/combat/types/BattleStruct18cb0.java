package legend.game.combat.types;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;

import java.util.Arrays;

/** 0x18cb0 bytes */
public class BattleStruct18cb0 {
  public EncounterData38 encounterData_00;
  /** 3 slots for chars, 3 slots for dragoons */
  public final AdditionStruct100[] _38 = new AdditionStruct100[0x100];
  /** This reference is only valid while it's loading */
  public MrgFile stageMrg_638;
  public MrgFile stageTmdMrg_63c;

  public final BattleStage stage_963c = new BattleStage();
  public McqHeader stageMcq_9cb0;

  public final Rendering1298[] _9ce8 = new Rendering1298[3];

  public BattleStruct18cb0() {
    Arrays.setAll(this._38, i -> new AdditionStruct100());
    Arrays.setAll(this._9ce8, i -> new Rendering1298());
  }

  public static class AdditionStruct100 {
    public final AdditionHitStruct20[] hits_00 = new AdditionHitStruct20[8];

    public AdditionStruct100() {
      Arrays.setAll(this.hits_00, i -> new AdditionHitStruct20());
    }
  }

  public static class AdditionHitStruct20 {
    public final short[] _00 = new short[16];
  }

  public static class Rendering1298 {
    public final GsDOBJ2[] dobj2s_00 = new GsDOBJ2[35];
    public final GsCOORDINATE2[] coord2s_230 = new GsCOORDINATE2[35];
    public final GsCOORD2PARAM[] params_d20 = new GsCOORD2PARAM[35];

    public Rendering1298() {
      Arrays.setAll(this.dobj2s_00, i -> new GsDOBJ2());
      Arrays.setAll(this.coord2s_230, i -> new GsCOORDINATE2());
      Arrays.setAll(this.params_d20, i -> new GsCOORD2PARAM());
    }
  }
}
