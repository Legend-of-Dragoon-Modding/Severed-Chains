package legend.game.combat.bent;

import legend.core.Latch;
import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.inventory.Equipment;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EquipmentSlot;
import legend.lodmod.LodMod;

import java.util.EnumMap;
import java.util.Map;

import static java.lang.Math.round;
import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.combat.Battle.spellStats_800fa0b8;

public class PlayerBattleEntity extends BattleEntity27c {
  private final Latch<ScriptState<PlayerBattleEntity>> scriptState;

  public Element element;

  public int level_04;
  public int dlevel_06;

  public final ElementSet equipmentAttackElements_1c = new ElementSet();

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
  public final Map<EquipmentSlot, Equipment> equipment_11e = new EnumMap<>(EquipmentSlot.class);
  public int spMultiplier_128;
  public int spPerPhysicalHit_12a;
  public int mpPerPhysicalHit_12c;
  public int spPerMagicalHit_12e;
  public int mpPerMagicalHit_130;
  public int _132;
  public int hpRegen_134;
  public int mpRegen_136;
  public int spRegen_138;
  public int revive_13a;
  public int hpMulti_13c;
  public int mpMulti_13e;

  private final ScriptFile script;

  public PlayerBattleEntity(final String name, final int scriptIndex, final ScriptFile script) {
    super(LodMod.PLAYER_TYPE.get(), name);

    //noinspection unchecked
    this.scriptState = new Latch<>(() -> (ScriptState<PlayerBattleEntity>)scriptStatePtrArr_800bc1c0[scriptIndex]);
    this.script = script;
  }

  @Override
  protected ScriptFile getScript() {
    return this.script;
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
  public ElementSet getAttackElements() {
    return this.equipmentAttackElements_1c;
  }

  @Override
  public Element getElement() {
    if(this.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && this.isDragoon()) { // Dart Divine Dragoon
      return LodMod.DIVINE_ELEMENT.get();
    }

    return this.element;
  }

  @Override
  @Method(0x800f2af4L)
  public int calculatePhysicalDamage(final BattleEntity27c target) {
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
        additionMultiplier += battlePreloadedEntities_1f8003f4.getHitProperty(this.charSlot_276, i, 4);
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
  public int calculateMagicDamage(final BattleEntity27c target, final int magicType) {
    int matk = this.magicAttack_36;
    if(magicType == 1) {
      matk += spellStats_800fa0b8[this.spellId_4e].multi_04;
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
    if(this.equipmentElementalResistance_20.contains(element)) {
      return damage / 2;
    }

    return super.applyElementalResistanceAndImmunity(damage, element);
  }

  @Override
  public void turnFinished() {
    this.tickTemporaryStatMod(this, BattleEntityStat.TEMP_SP_PER_PHYSICAL_HIT);
    this.tickTemporaryStatMod(this, BattleEntityStat.TEMP_MP_PER_PHYSICAL_HIT);
    this.tickTemporaryStatMod(this, BattleEntityStat.TEMP_SP_PER_MAGICAL_HIT);
    this.tickTemporaryStatMod(this, BattleEntityStat.TEMP_MP_PER_MAGICAL_HIT);

    super.turnFinished();
  }

  @Override
  public void recalculateSpeedAndPerHitStats() {
    super.recalculateSpeedAndPerHitStats();

    final ActiveStatsa0 stats = stats_800be5f8[this.charId_272];

    this.spPerPhysicalHit_12a = stats.equipmentSpPerPhysicalHit_4e;
    this.mpPerPhysicalHit_12c = stats.equipmentMpPerPhysicalHit_50;
    this.spPerMagicalHit_12e = stats.equipmentSpPerMagicalHit_52;
    this.mpPerMagicalHit_130 = stats.equipmentMpPerMagicalHit_54;

    if(this.tempSpPerPhysicalHitTurns_cd != 0) {
      this.spPerPhysicalHit_12a += this.tempSpPerPhysicalHit_cc;
    }

    if(this.tempMpPerPhysicalHitTurns_cf != 0) {
      this.mpPerPhysicalHit_12c += this.tempMpPerPhysicalHit_ce;
    }

    if(this.tempSpPerMagicalHitTurns_d1 != 0) {
      this.spPerMagicalHit_12e += this.tempSpPerMagicalHit_d0;
    }

    if(this.tempMpPerMagicalHitTurns_d3 != 0) {
      this.mpPerMagicalHit_130 += this.tempMpPerMagicalHit_d2;
    }
  }

  @Override
  public int getStat(final BattleEntityStat statIndex) {
    int disableStatusFlag = 0x0;
    if(statIndex == BattleEntityStat.STATUS || statIndex == BattleEntityStat.EQUIPMENT_STATUS_RESIST) {
      disableStatusFlag = CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()) ? 0xff : 0x0;
      if(disableStatusFlag == 0xff) {
        this.status_0e &= 0xff00;
      }
    }

    return switch(statIndex) {
      case LEVEL -> this.level_04;
      case DLEVEL -> this.dlevel_06;

      case CURRENT_SP -> this.stats.getStat(LodMod.SP_STAT.get()).getCurrent();
      case CURRENT_MP -> this.stats.getStat(LodMod.MP_STAT.get()).getCurrent();

      case MAX_MP -> this.stats.getStat(LodMod.MP_STAT.get()).getMax();

      case EQUIPMENT_ATTACK_ELEMENT_OR_MONSTER_DISPLAY_ELEMENT -> this.equipmentAttackElements_1c.pack();

      case EQUIPMENT_STATUS_RESIST -> this.equipmentStatusResist_24 | disableStatusFlag;

      case ADDITION_HITS -> this.additionHits_56;
      case SELECTED_ADDITION -> this.selectedAddition_58;

      case DRAGOON_ATTACK -> this.dragoonAttack_ac;
      case DRAGOON_MAGIC -> this.dragoonMagic_ae;
      case DRAGOON_DEFENCE -> this.dragoonDefence_b0;
      case DRAGOON_MAGIC_DEFENCE -> this.dragoonMagicDefence_b2;

      case TEMP_SP_PER_PHYSICAL_HIT -> (this.tempSpPerPhysicalHitTurns_cd & 0xff) << 8 | this.tempSpPerPhysicalHit_cc & 0xff;
      case TEMP_MP_PER_PHYSICAL_HIT -> (this.tempMpPerPhysicalHitTurns_cf & 0xff) << 8 | this.tempMpPerPhysicalHit_ce & 0xff;
      case TEMP_SP_PER_MAGICAL_HIT -> (this.tempSpPerMagicalHitTurns_d1 & 0xff) << 8 | this.tempSpPerMagicalHit_d0 & 0xff;
      case TEMP_MP_PER_MAGICAL_HIT -> (this.tempMpPerMagicalHitTurns_d3 & 0xff) << 8 | this.tempMpPerMagicalHit_d2 & 0xff;

      case _138 -> this._118;
      case ADDITION_SP_MULTIPLIER -> this.additionSpMultiplier_11a;
      case ADDITION_DAMAGE_MULTIPLIER -> this.additionDamageMultiplier_11c;
      case EQUIPMENT_WEAPON_SLOT -> LodMod.idEquipmentMap.getInt(this.equipment_11e.get(EquipmentSlot.WEAPON).getRegistryId()); //TODO
      case EQUIPMENT_HELMET_SLOT -> LodMod.idEquipmentMap.getInt(this.equipment_11e.get(EquipmentSlot.HELMET).getRegistryId());
      case EQUIPMENT_ARMOUR_SLOT -> LodMod.idEquipmentMap.getInt(this.equipment_11e.get(EquipmentSlot.ARMOUR).getRegistryId());
      case EQUIPMENT_BOOTS_SLOT -> LodMod.idEquipmentMap.getInt(this.equipment_11e.get(EquipmentSlot.BOOTS).getRegistryId());
      case EQUIPMENT_ACCESSORY_SLOT -> LodMod.idEquipmentMap.getInt(this.equipment_11e.get(EquipmentSlot.ACCESSORY).getRegistryId());
      case SP_MULTIPLIER -> this.spMultiplier_128;
      case SP_PER_PHYSICAL_HIT -> this.spPerPhysicalHit_12a;
      case MP_PER_PHYSICAL_HIT -> this.mpPerPhysicalHit_12c;
      case SP_PER_MAGICAL_HIT -> this.spPerMagicalHit_12e;
      case MP_PER_MAGICAL_HIT -> this.mpPerMagicalHit_130;
      case _151 -> this._132;
      case HP_REGEN -> this.hpRegen_134;
      case MP_REGEN -> this.mpRegen_136;
      case SP_REGEN -> this.spRegen_138;
      case REVIVE -> this.revive_13a;
      case HP_MULTI -> this.hpMulti_13c;
      case MP_MULTI -> this.mpMulti_13e;

      default -> super.getStat(statIndex);
    };
  }

  @Override
  public void setStat(final BattleEntityStat statIndex, final int value) {
    switch(statIndex) {
      case LEVEL -> this.level_04 = value;
      case DLEVEL -> this.dlevel_06 = value;

      case CURRENT_SP -> this.stats.getStat(LodMod.SP_STAT.get()).setCurrent(value);
      case CURRENT_MP -> this.stats.getStat(LodMod.MP_STAT.get()).setCurrent(value);

      case ADDITION_HITS -> this.additionHits_56 = value;
      case SELECTED_ADDITION -> this.selectedAddition_58 = value;

      case DRAGOON_ATTACK -> this.dragoonAttack_ac = value;
      case DRAGOON_MAGIC -> this.dragoonMagic_ae = value;
      case DRAGOON_DEFENCE -> this.dragoonDefence_b0 = value;
      case DRAGOON_MAGIC_DEFENCE -> this.dragoonMagicDefence_b2 = value;

      case TEMP_SP_PER_PHYSICAL_HIT -> {
        this.tempSpPerPhysicalHit_cc = value & 0xff;
        this.tempSpPerPhysicalHitTurns_cd = value >>> 8 & 0xff;
      }
      case TEMP_MP_PER_PHYSICAL_HIT -> {
        this.tempMpPerPhysicalHit_ce = value & 0xff;
        this.tempMpPerPhysicalHitTurns_cf = value >>> 8 & 0xff;
      }
      case TEMP_SP_PER_MAGICAL_HIT -> {
        this.tempSpPerMagicalHit_d0 = value & 0xff;
        this.tempSpPerMagicalHitTurns_d1 = value >>> 8 & 0xff;
      }
      case TEMP_MP_PER_MAGICAL_HIT -> {
        this.tempMpPerMagicalHit_d2 = value & 0xff;
        this.tempMpPerMagicalHitTurns_d3 = value >>> 8 & 0xff;
      }

      case _138 -> this._118 = value;
      case ADDITION_SP_MULTIPLIER -> this.additionSpMultiplier_11a = value;
      case ADDITION_DAMAGE_MULTIPLIER -> this.additionDamageMultiplier_11c = value;
//      case 141, 142, 143, 144, 145 -> this.equipment_11e.put(EquipmentSlot.fromLegacy(statIndex - 141), value); //TODO
      case SP_MULTIPLIER -> this.spMultiplier_128 = value;
      case SP_PER_PHYSICAL_HIT -> this.spPerPhysicalHit_12a = value;
      case MP_PER_PHYSICAL_HIT -> this.mpPerPhysicalHit_12c = value;
      case SP_PER_MAGICAL_HIT -> this.spPerMagicalHit_12e = value;
      case MP_PER_MAGICAL_HIT -> this.mpPerMagicalHit_130 = value;
      case _151 -> this._132 = value;
      case HP_REGEN -> this.hpRegen_134 = value;
      case MP_REGEN -> this.mpRegen_136 = value;
      case SP_REGEN -> this.spRegen_138 = value;
      case REVIVE -> this.revive_13a = value;
      case HP_MULTI -> this.hpMulti_13c = value;
      case MP_MULTI -> this.mpMulti_13e = value;

      default -> super.setStat(statIndex, value);
    }
  }
}
