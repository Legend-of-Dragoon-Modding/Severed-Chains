package legend.game.combat.bobj;

import legend.core.Config;
import legend.core.Latch;
import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;

import static java.lang.Math.round;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c.characterElements_800c706c;
import static legend.game.combat.Bttl_800c.getHitMultiplier;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;

public class PlayerBattleObject extends BattleObject27c {
  private final Latch<ScriptState<PlayerBattleObject>> scriptState;

  public int level_04;
  public int dlevel_06;

  public Element weaponElement_1c;

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
    super(CoreMod.PLAYER_TYPE.get(), name);

    //noinspection unchecked
    this.scriptState = new Latch<>(() -> (ScriptState<PlayerBattleObject>)scriptStatePtrArr_800bc1c0[scriptIndex]);
  }

  public boolean isDragoon() {
    return (this.scriptState.get().storage_44[7] & 0x2) != 0;
  }

  @Override
  public int getEffectiveDefence() {
    if(this.isDragoon()) {
      return super.getEffectiveDefence() * this.dragoonDefence_b0 / 100;
    }

    return super.getEffectiveDefence();
  }

  @Override
  public int getEffectiveMagicDefence() {
    if(this.isDragoon()) {
      return super.getEffectiveMagicDefence() * this.dragoonMagicDefence_b2 / 100;
    }

    return super.getEffectiveMagicDefence();
  }

  @Override
  public Element getAttackElement() {
    return this.weaponElement_1c;
  }

  @Override
  public Element getElement() {
    if(this.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && this.isDragoon()) { // Dart Divine Dragoon
      return CoreMod.DIVINE_ELEMENT.get();
    }

    return characterElements_800c706c[this.charId_272].get();
  }

  @Override
  @Method(0x800f2af4L)
  public int calculatePhysicalAttack(final BattleObject27c target) {
    int attack = this.attack_34;
    int attackMultiplier = 100;

    if(this.selectedAddition_58 == -1) { // No addition (Shana/???)
      //LAB_800f2c24
      if(this.isDragoon()) {
        //LAB_800f2c4c
        attackMultiplier = this.dragoonAttack_ac;
      }
    } else if(this.additionHits_56 > 0) {
      //LAB_800f2b94
      int additionMultiplier = 0;
      for(int i = 0; i < this.additionHits_56; i++) {
        additionMultiplier += getHitMultiplier(this.charSlot_276, i, 4);
      }

      //LAB_800f2bb4
      final int damageMultiplier;
      if(this.isDragoon()) { // Is dragoon
        damageMultiplier = this.dragoonAttack_ac;
      } else {
        //LAB_800f2bec
        damageMultiplier = this.additionDamageMultiplier_11c + 100;
      }

      //LAB_800f2bfc
      attackMultiplier = additionMultiplier * damageMultiplier / 100;
    }

    attack = attack * attackMultiplier / 100;

    //LAB_800f2c6c
    //LAB_800f2c70
    //LAB_800f2ccc
    return round((this.level_04 + 5) * attack * 5 / (float)target.getEffectiveDefence());
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Override
  @Method(0x800f2e98L)
  public int calculateMagicAttack(final BattleObject27c target, final int magicType) {
    int matk = this.magicAttack_36;
    if(magicType == 1) {
      matk += spellStats_800fa0b8.get(this.spellId_4e).multi_04.get();
    } else {
      //LAB_800f2ef8
      matk += this.item_d4.damage_05;
    }

    //LAB_800f2f04
    if(this.isDragoon()) {
      matk = matk * this.dragoonMagic_ae / 100;
    }

    //LAB_800f2f5c
    //LAB_800f2fb4
    return (this.level_04 + 5) * matk * 5 / target.getEffectiveMagicDefence();
  }

  @Override
  public int applyElementalResistanceAndImmunity(final int damage, final Element element) {
    if(this.elementalResistanceFlag_20.contains(element)) {
      return damage / 2;
    }

    return super.applyElementalResistanceAndImmunity(damage, element);
  }

  @Override
  public int getStat(final int statIndex) {
    int disableStatusFlag = 0x0;
    if(statIndex == 5 || statIndex == 16) {
      disableStatusFlag = Config.disableStatusEffects() ? 0xff : 0x0;
      if(disableStatusFlag == 0xff) {
        this.status_0e &= 0xff00;
      }
    }

    return switch(statIndex) {
      case 0 -> this.level_04;
      case 1 -> this.dlevel_06;

      case 3 -> this.stats.getStat(CoreMod.SP_STAT.get()).getCurrent();
      case 4 -> this.stats.getStat(CoreMod.MP_STAT.get()).getCurrent();

      case 7 -> this.stats.getStat(CoreMod.MP_STAT.get()).getMax();

      case 12 -> this.weaponElement_1c.flag;

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

      case 3 -> this.stats.getStat(CoreMod.SP_STAT.get()).setCurrent(value);
      case 4 -> this.stats.getStat(CoreMod.MP_STAT.get()).setCurrent(value);

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
