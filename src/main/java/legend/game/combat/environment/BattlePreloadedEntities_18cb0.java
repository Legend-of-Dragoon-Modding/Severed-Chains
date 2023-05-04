package legend.game.combat.environment;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;

import java.util.Arrays;

/**
 * 0x18cb0 bytes
 */
public class BattlePreloadedEntities_18cb0 {
  public EncounterData38 encounterData_00;
  /**
   * 3 slots for chars, 3 slots for dragoons
   */
  public final AdditionHits100[] additionHits_38 = new AdditionHits100[0x100];
  /**
   * This reference is only valid while it's loading
   */
  public MrgFile stageMrg_638;
  public MrgFile stageTmdMrg_63c;

  public final BattleStage stage_963c = new BattleStage();
  public McqHeader stageMcq_9cb0;

  public final Rendering1298[] _9ce8 = new Rendering1298[3];

  public BattlePreloadedEntities_18cb0() {
    Arrays.setAll(this.additionHits_38, i -> new AdditionHits100());
    Arrays.setAll(this._9ce8, i -> new Rendering1298());
  }

  public static class AdditionHits100 {
    public final AdditionHitProperties20[] hits_00 = new AdditionHitProperties20[8];

    public AdditionHits100() {
      Arrays.setAll(this.hits_00, i -> new AdditionHitProperties20());
    }
  }

  public static class AdditionHitProperties20 {
    public int length;

    public short isHitStep_00;
    public short totalFrames_02;
    public short frameBeginOverlayDisplay_04;
    public short totalGrayFrames_06;
    public short damageMultiplier_08;
    public short spMultiplier_0a;
    public short audioFile_0c;
    public short isFinalHit_0e;
    public short _10; // related to camera or voice?
    public short _12; // related to camera or voice?
    public short _14; // related to camera?
    public short distanceFromTarget_16;
    public short moveToTargetSpeed_18;
    public short _1a; //always 32
    public short unused_1c;
    public short reticleSpeedModifier_1e;

    public AdditionHitProperties20() {
      this.length = 16;
    }

    public short get(final int propertyIndex) {
      return switch(propertyIndex) {
        case 0 -> this.isHitStep_00;
        case 1 -> this.totalFrames_02;
        case 2 -> this.frameBeginOverlayDisplay_04;
        case 3 -> this.totalGrayFrames_06;
        case 4 -> this.damageMultiplier_08;
        case 5 -> this.spMultiplier_0a;
        case 6 -> this.audioFile_0c;
        case 7 -> this.isFinalHit_0e;
        case 8 -> this._10;
        case 9 -> this._12;
        case 10 -> this._14;
        case 11 -> this.distanceFromTarget_16;
        case 12 -> this.moveToTargetSpeed_18;
        case 13 -> this._1a;
        case 14 -> this.unused_1c;
        case 15 -> this.reticleSpeedModifier_1e;
        default -> throw new IllegalArgumentException(propertyIndex + " is an invalid addition hit property index.");
      };
    }

    public void set(final int propertyIndex, final short value) {
      switch(propertyIndex) {
        case 0 -> this.isHitStep_00 = value;
        case 1 -> this.totalFrames_02 = value;
        case 2 -> this.frameBeginOverlayDisplay_04 = value;
        case 3 -> this.totalGrayFrames_06 = value;
        case 4 -> this.damageMultiplier_08 = value;
        case 5 -> this.spMultiplier_0a = value;
        case 6 -> this.audioFile_0c = value;
        case 7 -> this.isFinalHit_0e = value;
        case 8 -> this._10 = value;
        case 9 -> this._12 = value;
        case 10 -> this._14 = value;
        case 11 -> this.distanceFromTarget_16 = value;
        case 12 -> this.moveToTargetSpeed_18 = value;
        case 13 -> this._1a = value;
        case 14 -> this.unused_1c = value;
        case 15 -> this.reticleSpeedModifier_1e = value;
        default -> throw new IllegalArgumentException(propertyIndex + " is an invalid addition hit property index.");
      }
    }
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
