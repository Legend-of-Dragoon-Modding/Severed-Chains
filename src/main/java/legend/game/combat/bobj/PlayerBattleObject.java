package legend.game.combat.bobj;

import legend.core.Latch;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public class PlayerBattleObject extends BattleObject27c {
  private final Latch<ScriptState<PlayerBattleObject>> scriptState;

  public int level_04;
  public int dlevel_06;

  public int sp_0a;
  public int mp_0c;

  public int maxMp_12;

  public int additionHits_56;
  public int selectedAddition_58;

  public int dragoonAttack_ac;
  public int dragoonMagic_ae;
  public int dragoonDefence_b0;
  public int dragoonMagicDefence_b2;

  public int tempSpPerPhysicalHit_cc;
  public int tempSpPerPhysicalHitTurns_cd;
  public int tempMpPerPhysicalHit_ce;
  public int tempMpPerPhysicalHitTurns_cf;
  public int tempSpPerMagicalHit_d0;
  public int tempSpPerMagicalHitTurns_d1;
  public int tempMpPerMagicalHit_d2;
  public int tempMpPerMagicalHitTurns_d3;

  public int _118;
  public int additionSpMultiplier_11a;
  public int additionDamageMultiplier_11c;
  public int equipment0_11e;
  public int equipment1_120;
  public int equipment2_122;
  public int equipment3_124;
  public int equipment4_126;
  public int spMultiplier_128;
  public int spPerPhysicalHit_12a;
  public int mpPerPhysicalHit_12c;
  public int itemSpPerMagicalHit_12e;
  public int mpPerMagicalHit_130;
  public int _132;
  public int hpRegen_134;
  public int mpRegen_136;
  public int spRegen_138;
  public int revive_13a;
  public int hpMulti_13c;
  public int mpMulti_13e;

  public PlayerBattleObject(final String name, final int scriptIndex) {
    super(name);

    //noinspection unchecked
    this.scriptState = new Latch<>(() -> (ScriptState<PlayerBattleObject>)scriptStatePtrArr_800bc1c0[scriptIndex]);
  }

  public boolean isDragoon() {
    return (this.scriptState.get().storage_44[7] & 0x2) != 0;
  }

  @Override
  public int getStat(final int statIndex) {
    int disableStatusFlag = 0x0;
    if(statIndex == 5 || statIndex == 16) {
      disableStatusFlag = CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()) ? 0xff : 0x0;
      if(disableStatusFlag == 0xff) {
        this.status_0e &= 0xff00;
      }
    }

    return switch(statIndex) {
      case 0 -> this.level_04;
      case 1 -> this.dlevel_06;

      case 3 -> this.sp_0a;
      case 4 -> this.mp_0c;

      case 7 -> this.maxMp_12;

      case 16 -> this.statusResistFlag_24 | disableStatusFlag;

      case 41 -> this.additionHits_56;
      case 42 -> this.selectedAddition_58;

      case 84 -> this.dragoonAttack_ac;
      case 85 -> this.dragoonMagic_ae;
      case 86 -> this.dragoonDefence_b0;
      case 87 -> this.dragoonMagicDefence_b2;

      case 100 -> (this.tempSpPerPhysicalHitTurns_cd & 0xff) << 8 | this.tempSpPerPhysicalHit_cc & 0xff;
      case 101 -> (this.tempMpPerPhysicalHitTurns_cf & 0xff) << 8 | this.tempMpPerPhysicalHit_ce & 0xff;
      case 102 -> (this.tempSpPerMagicalHitTurns_d1 & 0xff) << 8 | this.tempSpPerMagicalHit_d0 & 0xff;
      case 103 -> (this.tempMpPerMagicalHitTurns_d3 & 0xff) << 8 | this.tempMpPerMagicalHit_d2 & 0xff;

      case 138 -> this._118;
      case 139 -> this.additionSpMultiplier_11a;
      case 140 -> this.additionDamageMultiplier_11c;
      case 141 -> this.equipment0_11e;
      case 142 -> this.equipment1_120;
      case 143 -> this.equipment2_122;
      case 144 -> this.equipment3_124;
      case 145 -> this.equipment4_126;
      case 146 -> this.spMultiplier_128;
      case 147 -> this.spPerPhysicalHit_12a;
      case 148 -> this.mpPerPhysicalHit_12c;
      case 149 -> this.itemSpPerMagicalHit_12e;
      case 150 -> this.mpPerMagicalHit_130;
      case 151 -> this._132;
      case 152 -> this.hpRegen_134;
      case 153 -> this.mpRegen_136;
      case 154 -> this.spRegen_138;
      case 155 -> this.revive_13a;
      case 156 -> this.hpMulti_13c;
      case 157 -> this.mpMulti_13e;

      default -> super.getStat(statIndex);
    };
  }

  @Override
  public void setStat(final int statIndex, final int value) {
    switch(statIndex) {
      case 0 -> this.level_04 = value;
      case 1 -> this.dlevel_06 = value;

      case 3 -> this.sp_0a = value;
      case 4 -> this.mp_0c = value;

      case 7 -> this.maxMp_12 = value;

      case 41 -> this.additionHits_56 = value;
      case 42 -> this.selectedAddition_58 = value;

      case 84 -> this.dragoonAttack_ac = value;
      case 85 -> this.dragoonMagic_ae = value;
      case 86 -> this.dragoonDefence_b0 = value;
      case 87 -> this.dragoonMagicDefence_b2 = value;

      case 100 -> {
        this.tempSpPerPhysicalHit_cc = value & 0xff;
        this.tempSpPerPhysicalHitTurns_cd = value >>> 8 & 0xff;
      }
      case 101 -> {
        this.tempMpPerPhysicalHit_ce = value & 0xff;
        this.tempMpPerPhysicalHitTurns_cf = value >>> 8 & 0xff;
      }
      case 102 -> {
        this.tempSpPerMagicalHit_d0 = value & 0xff;
        this.tempSpPerMagicalHitTurns_d1 = value >>> 8 & 0xff;
      }
      case 103 -> {
        this.tempMpPerMagicalHit_d2 = value & 0xff;
        this.tempMpPerMagicalHitTurns_d3 = value >>> 8 & 0xff;
      }

      case 138 -> this._118 = value;
      case 139 -> this.additionSpMultiplier_11a = value;
      case 140 -> this.additionDamageMultiplier_11c = value;
      case 141 -> this.equipment0_11e = value;
      case 142 -> this.equipment1_120 = value;
      case 143 -> this.equipment2_122 = value;
      case 144 -> this.equipment3_124 = value;
      case 145 -> this.equipment4_126 = value;
      case 146 -> this.spMultiplier_128 = value;
      case 147 -> this.spPerPhysicalHit_12a = value;
      case 148 -> this.mpPerPhysicalHit_12c = value;
      case 149 -> this.itemSpPerMagicalHit_12e = value;
      case 150 -> this.mpPerMagicalHit_130 = value;
      case 151 -> this._132 = value;
      case 152 -> this.hpRegen_134 = value;
      case 153 -> this.mpRegen_136 = value;
      case 154 -> this.spRegen_138 = value;
      case 155 -> this.revive_13a = value;
      case 156 -> this.hpMulti_13c = value;
      case 157 -> this.mpMulti_13e = value;

      default -> super.setStat(statIndex, value);
    }
  }
}
