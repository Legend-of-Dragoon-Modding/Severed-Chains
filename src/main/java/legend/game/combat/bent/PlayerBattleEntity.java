package legend.game.combat.bent;

import legend.core.Latch;
import legend.core.memory.Method;
import legend.game.additions.Addition;
import legend.game.characters.CharacterData2c;
import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.combat.Battle;
import legend.game.combat.types.AttackType;
import legend.game.inventory.Equipment;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.ArcherSpEvent;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.EquipmentSlot;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.Map;

import static java.lang.Math.round;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.ui.BattleHud.playerNames_800fb378;
import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.ATTACK_STAT;
import static legend.lodmod.LodMod.DIVINE_ELEMENT;
import static legend.lodmod.LodMod.DRAGOON_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.GUARD_HEAL_STAT;
import static legend.lodmod.LodMod.MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.PLAYER_TYPE;
import static legend.lodmod.LodMod.SP_STAT;

public class PlayerBattleEntity extends BattleEntity27c {
  private final Latch<ScriptState<PlayerBattleEntity>> scriptState;

  public int level_04;
  public int dlevel_06;

  public final ElementSet equipmentAttackElements_1c = new ElementSet();

  public int additionHits_56;
  public RegistryId selectedAddition_58;

//  public int dragoonAttack_ac;
//  public int dragoonMagic_ae;
//  public int dragoonDefence_b0;
//  public int dragoonMagicDefence_b2;

  public int tempSpPerPhysicalHit_cc;
  public int tempSpPerPhysicalHitTurns_cd;
  public int tempMpPerPhysicalHit_ce;
  public int tempMpPerPhysicalHitTurns_cf;
  public int tempSpPerMagicalHit_d0;
  public int tempSpPerMagicalHitTurns_d1;
  public int tempMpPerMagicalHit_d2;
  public int tempMpPerMagicalHitTurns_d3;

//  public int _118;
  public Addition addition;
//  public int additionSpMultiplier_11a;
//  public int additionDamageMultiplier_11c;
  public final Map<EquipmentSlot, Equipment> equipment_11e = new EnumMap<>(EquipmentSlot.class);
  public int spMultiplier_128;
  public int originalSpPerPhysicalHit;
  public int originalMpPerPhysicalHit;
  public int originalSpPerMagicalHit;
  public int originalMpPerMagicalHit;
  public int spPerPhysicalHit_12a;
  public int mpPerPhysicalHit_12c;
  public int spPerMagicalHit_12e;
  public int mpPerMagicalHit_130;
  /** Extra percent chance to run away */
  public int escapeBonus_132;
  public int hpRegen_134;
  public int mpRegen_136;
  public int spRegen_138;
  public int revive_13a;
  public int hpMulti_13c;
  public int mpMulti_13e;

  public DetransformationMode detransformationMode = DetransformationMode.NOW;

  private final ScriptFile script;

  public PlayerBattleEntity(final Battle battle, final String name, final int scriptIndex, final ScriptFile script) {
    super(PLAYER_TYPE.get(), battle, name);

    this.scriptState = new Latch<>(() -> SCRIPTS.getState(scriptIndex, PlayerBattleEntity.class));
    this.script = script;
  }

  @Override
  public String getName() {
    return this.charId_272 == 8 ? "Who?" : playerNames_800fb378[this.charId_272];
  }

  @Override
  protected ScriptFile getScript() {
    return this.script;
  }

  public boolean isDragoon() {
    return this.scriptState.get().hasFlag(FLAG_DRAGOON);
  }

  public boolean canBecomeDragoon() {
    return (this.status_0e & 0x2000) != 0;
  }

  @Override
  public int getEffectiveDefence() {
    if(this.isDragoon()) {
      return super.getEffectiveDefence() * this.stats.getStat(DRAGOON_DEFENSE_STAT.get()).get() / 100;
    }

    return super.getEffectiveDefence();
  }

  @Override
  public int getEffectiveMagicDefence() {
    if(this.isDragoon()) {
      return super.getEffectiveMagicDefence() * this.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get()).get() / 100;
    }

    return super.getEffectiveMagicDefence();
  }

  @Override
  public ElementSet getAttackElements() {
    return this.equipmentAttackElements_1c;
  }

  @Override
  public Element getElement() {
    if(this.charId_272 == 0 && gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT) && this.isDragoon()) { // Dart Divine Dragoon
      return DIVINE_ELEMENT.get();
    }

    return super.getElement();
  }

  @Override
  @Method(0x800f2af4L)
  public int calculatePhysicalDamage(final BattleEntity27c target) {
    int attack = this.stats.getStat(ATTACK_STAT.get()).get();
    int attackMultiplier = 100;

    if(this.selectedAddition_58 == null) { // No addition (Shana/???)
      //LAB_800f2c24
      if(this.isDragoon()) {
        //LAB_800f2c4c
        attackMultiplier = this.stats.getStat(DRAGOON_ATTACK_STAT.get()).get();
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
        damageMultiplier = this.stats.getStat(DRAGOON_ATTACK_STAT.get()).get();
      } else {
        //LAB_800f2bec
        damageMultiplier = this.getAdditionDamageMultiplier();
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
    int matk = this.stats.getStat(MAGIC_ATTACK_STAT.get()).get();
    if(magicType == 1) {
      matk += this.spell_94.multi_04;
    }

    //LAB_800f2f04
    if(this.isDragoon()) {
      matk = matk * this.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get()).get() / 100;
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
    if(this.tempSpPerPhysicalHitTurns_cd > 0) {
      this.tempSpPerPhysicalHitTurns_cd--;

      if(this.tempSpPerPhysicalHitTurns_cd == 0) {
        this.tempSpPerPhysicalHit_cc = 0;
      }
    }

    if(this.tempMpPerPhysicalHitTurns_cf > 0) {
      this.tempMpPerPhysicalHitTurns_cf--;

      if(this.tempMpPerPhysicalHitTurns_cf == 0) {
        this.tempMpPerPhysicalHit_ce = 0;
      }
    }

    if(this.tempSpPerMagicalHitTurns_d1 > 0) {
      this.tempSpPerMagicalHitTurns_d1--;

      if(this.tempSpPerMagicalHitTurns_d1 == 0) {
        this.tempSpPerMagicalHit_d0 = 0;
      }
    }

    if(this.tempMpPerMagicalHitTurns_d3 > 0) {
      this.tempMpPerMagicalHitTurns_d3--;

      if(this.tempMpPerMagicalHitTurns_d3 == 0) {
        this.tempMpPerMagicalHit_d2 = 0;
      }
    }

    super.turnFinished();
  }

  @Override
  public void recalculateSpeedAndPerHitStats() {
    super.recalculateSpeedAndPerHitStats();

    this.spPerPhysicalHit_12a = this.originalSpPerPhysicalHit;
    this.mpPerPhysicalHit_12c = this.originalMpPerPhysicalHit;
    this.spPerMagicalHit_12e = this.originalSpPerMagicalHit;
    this.mpPerMagicalHit_130 = this.originalMpPerMagicalHit;

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

  protected boolean hasWeaponTrail() {
    return this.charId_272 != 4; // Haschel
  }

  protected int getWeaponTrailColour() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 0x2068e8 : 0x808080;
      case 1, 5 -> 0x20a878;
      case 2, 6, 8 -> 0x808080;
      case 3 -> 0x1090d8;
      case 4 -> 0x88d4d8;
      case 7 -> 0x90d0f0;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getSpellRingColour() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 0x201996 : 0x808080;
      case 1, 5 -> 0x297231;
      case 2, 8 -> 0x6c8283;
      case 3 -> 0x5e263a;
      case 4 -> 0x6c306c;
      case 6 -> 0x78462c;
      case 7 -> 0x286499;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  public int getLeftHandModelPart() {
    return switch(this.charId_272) {
      case 0, 7 -> 5;
      case 1, 4, 5, 6 -> 6;
      case 2 -> 11;
      case 3 -> 8;
      case 8 -> 10;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  public int getRightHandModelPart() {
    return switch(this.charId_272) {
      case 0, 7 -> 6;
      case 1, 4, 5, 6 -> 7;
      case 2 -> 12;
      case 3 -> 9;
      case 8 -> 11;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  public int getFootModelPart() {
    if(this.isDragoon()) {
      return switch(this.charId_272) {
        case 0 -> (this.status_0e & 0x4000) == 0 ? 8 : 7;
        case 1, 5 -> 9;
        case 2, 3, 8 -> 11;
        case 4, 7 -> 8;
        case 6 -> 12;
        default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
      };
    }

    return switch(this.charId_272) {
      case 0, 7 -> 8;
      case 1, 5 -> 9;
      case 2 -> 13;
      case 3 -> 11;
      case 4 -> 10;
      case 6, 8 -> 12;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  public int getWeaponModelPart() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 14 : 0;
      case 1, 5 -> 3;
      case 2, 8 -> 0;
      case 3 -> 18;
      case 4 -> 5;
      case 6 -> 21;
      case 7 -> 9;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  public int getWeaponTrailVertexComponent() {
    return switch(this.charId_272) {
      case 0, 2, 6, 8 -> 0;
      case 1, 3, 4, 5, 7 -> 2;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getShadowSize() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 0x1800 : 0x1500;
      case 1 -> 0x1800;
      case 2 -> 0x1000;
      case 3, 6 -> 0xe00;
      case 4 -> 0x1600;
      case 5, 8 -> 0x1300;
      case 7 -> 0x2000;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getDragoonTransformDeff() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 0x20 : 0x2e;
      case 1 -> 0x22;
      case 2 -> 0x24;
      case 3 -> 0x26;
      case 4 -> 0x28;
      case 5 -> 0x2f;
      case 6 -> 0x2a;
      case 7 -> 0x2c;
      case 8 -> 0x40;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getDragoonAttackDeff() {
    return switch(this.charId_272) {
      case 0 -> (this.status_0e & 0x4000) == 0 ? 0x30 : 0x39;
      case 1 -> 0x31;
      case 2, 8 -> -1;
      case 3 -> 0x33;
      case 4 -> 0x34;
      case 5 -> 0x35;
      case 6 -> 0x36;
      case 7 -> 0x37;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getDragoonAttackSounds() {
    return switch(this.charId_272) {
      case 0 -> 0x68;
      case 1 -> 0x69;
      case 2 -> 0x6a;
      case 3 -> 0x6b;
      case 4 -> 0x6c;
      case 5 -> 0x6d;
      case 6 -> 0x6e;
      case 7 -> 0x6f;
      case 8 -> 0x70;
      default -> throw new IllegalStateException("Unknown character ID " + this.charId_272);
    };
  }

  protected int getArcherSp() {
    int sp;
    switch(this.dlevel_06) {
      case 1 -> sp = 35;
      case 2 -> sp = 50;
      case 3 -> sp = 70;
      case 4 -> sp = 100;
      case 5 -> sp = 150;
      default -> sp = 0;
    }
    sp = EVENTS.postEvent(new ArcherSpEvent(this.battle, this, sp)).sp;
    return sp;
  }

  protected int getAdditionHitCount() {
    return battlePreloadedEntities_1f8003f4.getHitCount(this.charSlot_276);
  }

  @Override
  public int getStatusEffectChance(final AttackType attackType) {
    return switch(attackType) {
      case PHYSICAL, ITEM_MAGIC -> this.onHitStatusChance_44;
      case DRAGOON_MAGIC_STATUS_ITEMS -> this.spell_94.statusChance_07;
    };
  }

  @Override
  public int getStatusEffectStatus(final AttackType attackType) {
    return switch(attackType) {
      case PHYSICAL -> this.equipmentOnHitStatus_4a;
      case DRAGOON_MAGIC_STATUS_ITEMS -> this.spell_94.statusType_09;
      default -> throw new RuntimeException("Not implemented");
    };
  }

  @Override
  public int getSpecialEffectStat(final AttackType attackType) {
    return switch(attackType) {
      case PHYSICAL -> this.specialEffectFlag_14;
      case DRAGOON_MAGIC_STATUS_ITEMS -> this.spell_94.flags_01;
      default -> throw new RuntimeException("Not implemented");
    };
  }

  @Override
  public int getSpecialEffectMask(final AttackType attackType) {
    return switch(attackType) {
      case PHYSICAL -> 0x40;
      case DRAGOON_MAGIC_STATUS_ITEMS -> 0xf0;
      case ITEM_MAGIC -> 0x80;
    };
  }

  public int getAdditionSpMultiplier() {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(this.charId_272);
    final float multiplier = this.addition.getSpMultiplier(gameState_800babc8, character, character.getAdditionInfo(this.addition.getRegistryId()));
    return Math.round(multiplier * 100);
  }

  public int getAdditionDamageMultiplier() {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(this.charId_272);
    final float multiplier = this.addition.getDamageMultiplier(gameState_800babc8, character, character.getAdditionInfo(this.addition.getRegistryId()));
    return Math.round(multiplier * 100);
  }

  @Override
  public void getStat(final BattleEntityStat statIndex, final Param out) {
    if(out.isRegistryId()) {
      switch(statIndex) {
        case EQUIPMENT_WEAPON_SLOT -> out.set(this.equipment_11e.get(EquipmentSlot.WEAPON).getRegistryId());
        case EQUIPMENT_HELMET_SLOT -> out.set(this.equipment_11e.get(EquipmentSlot.HELMET).getRegistryId());
        case EQUIPMENT_ARMOUR_SLOT -> out.set(this.equipment_11e.get(EquipmentSlot.ARMOUR).getRegistryId());
        case EQUIPMENT_BOOTS_SLOT -> out.set(this.equipment_11e.get(EquipmentSlot.BOOTS).getRegistryId());
        case EQUIPMENT_ACCESSORY_SLOT -> out.set(this.equipment_11e.get(EquipmentSlot.ACCESSORY).getRegistryId());
        default -> super.getStat(statIndex, out);
      }

      return;
    }

    int disableStatusFlag = 0x0;
    if(statIndex == BattleEntityStat.STATUS || statIndex == BattleEntityStat.EQUIPMENT_STATUS_RESIST) {
      disableStatusFlag = CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()) ? 0xff : 0x0;
      if(disableStatusFlag == 0xff) {
        this.status_0e &= 0xff00;
      }
    }

    switch(statIndex) {
      case LEVEL -> out.set(this.level_04);
      case DLEVEL -> out.set(this.dlevel_06);

      case CURRENT_SP -> out.set(this.stats.getStat(SP_STAT.get()).getCurrent());
      case CURRENT_MP -> out.set(this.stats.getStat(MP_STAT.get()).getCurrent());

      case MAX_MP -> out.set(this.stats.getStat(MP_STAT.get()).getMax());

      case EQUIPMENT_ATTACK_ELEMENT_OR_MONSTER_DISPLAY_ELEMENT -> out.set(this.equipmentAttackElements_1c.pack());

      case EQUIPMENT_STATUS_RESIST -> out.set(this.equipmentStatusResist_24 | disableStatusFlag);

      case ADDITION_HITS -> out.set(this.additionHits_56);

      case DRAGOON_ATTACK -> out.set(this.stats.getStat(DRAGOON_ATTACK_STAT.get()).get());
      case DRAGOON_MAGIC -> out.set(this.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get()).get());
      case DRAGOON_DEFENCE -> out.set(this.stats.getStat(DRAGOON_DEFENSE_STAT.get()).get());
      case DRAGOON_MAGIC_DEFENCE -> out.set(this.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get()).get());

      case TEMP_SP_PER_PHYSICAL_HIT -> out.set((this.tempSpPerPhysicalHitTurns_cd & 0xff) << 8 | this.tempSpPerPhysicalHit_cc & 0xff);
      case TEMP_MP_PER_PHYSICAL_HIT -> out.set((this.tempMpPerPhysicalHitTurns_cf & 0xff) << 8 | this.tempMpPerPhysicalHit_ce & 0xff);
      case TEMP_SP_PER_MAGICAL_HIT -> out.set((this.tempSpPerMagicalHitTurns_d1 & 0xff) << 8 | this.tempSpPerMagicalHit_d0 & 0xff);
      case TEMP_MP_PER_MAGICAL_HIT -> out.set((this.tempMpPerMagicalHitTurns_d3 & 0xff) << 8 | this.tempMpPerMagicalHit_d2 & 0xff);

      case ADDITION_SP_MULTIPLIER -> out.set(this.getAdditionSpMultiplier() - 100);
      case ADDITION_DAMAGE_MULTIPLIER -> out.set(this.getAdditionDamageMultiplier() - 100);

      case SP_MULTIPLIER -> out.set(this.spMultiplier_128);
      case SP_PER_PHYSICAL_HIT -> out.set(this.spPerPhysicalHit_12a);
      case MP_PER_PHYSICAL_HIT -> out.set(this.mpPerPhysicalHit_12c);
      case SP_PER_MAGICAL_HIT -> out.set(this.spPerMagicalHit_12e);
      case MP_PER_MAGICAL_HIT -> out.set(this.mpPerMagicalHit_130);
      case ESCAPE_BONUS_151 -> out.set(this.escapeBonus_132);
      case HP_REGEN -> out.set(this.hpRegen_134);
      case MP_REGEN -> out.set(this.mpRegen_136);
      case SP_REGEN -> out.set(this.spRegen_138);
      case REVIVE -> out.set(this.revive_13a);
      case HP_MULTI -> out.set(this.hpMulti_13c);
      case MP_MULTI -> out.set(this.mpMulti_13e);

      case HAS_WEAPON_TRAIL -> out.set(this.hasWeaponTrail() ? 1 : 0);
      case WEAPON_TRAIL_COLOUR -> out.set(this.getWeaponTrailColour());
      case SPELL_RING_COLOUR -> out.set(this.getSpellRingColour());
      case HAND_MODEL_PART -> out.set(this.getLeftHandModelPart());
      case FOOT_MODEL_PART -> out.set(this.getFootModelPart());
      case WEAPON_MODEL_PART -> out.set(this.getWeaponModelPart());
      case WEAPON_TRAIL_VERTEX_COMPONENT -> out.set(this.getWeaponTrailVertexComponent());
      case SHADOW_SIZE -> out.set(this.getShadowSize());
      case DRAGOON_TRANSFORM_DEFF -> out.set(this.getDragoonTransformDeff());
      case DRAGOON_ATTACK_DEFF -> out.set(this.getDragoonAttackDeff());
      case DRAGOON_ATTACK_SOUNDS -> out.set(this.getDragoonAttackSounds());
      case ARCHER_SP -> out.set(this.getArcherSp());
      case ADDITION_HIT_COUNT -> out.set(this.getAdditionHitCount());

      case GUARD_HEAL -> out.set(this.stats.getStat(GUARD_HEAL_STAT.get()).get());
      case GUARD_HEAL_RAW -> out.set(this.stats.getStat(GUARD_HEAL_STAT.get()).getRaw());

      case DETRANSFORMATION_MODE -> out.set(this.detransformationMode.ordinal());

      default -> super.getStat(statIndex, out);
    }
  }

  @Override
  public void setStat(final BattleEntityStat statIndex, final Param value) {
    switch(statIndex) {
      case LEVEL -> this.level_04 = value.get();
      case DLEVEL -> this.dlevel_06 = value.get();

      case CURRENT_SP -> this.stats.getStat(SP_STAT.get()).setCurrent(value.get());
      case CURRENT_MP -> this.stats.getStat(MP_STAT.get()).setCurrent(value.get());

      case ADDITION_HITS -> this.additionHits_56 = value.get();

      case DRAGOON_ATTACK -> this.stats.getStat(DRAGOON_ATTACK_STAT.get()).setRaw(value.get());
      case DRAGOON_MAGIC -> this.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get()).setRaw(value.get());
      case DRAGOON_DEFENCE -> this.stats.getStat(DRAGOON_DEFENSE_STAT.get()).setRaw(value.get());
      case DRAGOON_MAGIC_DEFENCE -> this.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get()).setRaw(value.get());

      case TEMP_SP_PER_PHYSICAL_HIT -> {
        this.tempSpPerPhysicalHit_cc = value.get() & 0xff;
        this.tempSpPerPhysicalHitTurns_cd = value.get() >>> 8 & 0xff;
      }
      case TEMP_MP_PER_PHYSICAL_HIT -> {
        this.tempMpPerPhysicalHit_ce = value.get() & 0xff;
        this.tempMpPerPhysicalHitTurns_cf = value.get() >>> 8 & 0xff;
      }
      case TEMP_SP_PER_MAGICAL_HIT -> {
        this.tempSpPerMagicalHit_d0 = value.get() & 0xff;
        this.tempSpPerMagicalHitTurns_d1 = value.get() >>> 8 & 0xff;
      }
      case TEMP_MP_PER_MAGICAL_HIT -> {
        this.tempMpPerMagicalHit_d2 = value.get() & 0xff;
        this.tempMpPerMagicalHitTurns_d3 = value.get() >>> 8 & 0xff;
      }

//      case ADDITION_SP_MULTIPLIER -> this.additionSpMultiplier_11a = value.get();
//      case ADDITION_DAMAGE_MULTIPLIER -> this.additionDamageMultiplier_11c = value.get();
      case SP_MULTIPLIER -> this.spMultiplier_128 = value.get();
      case SP_PER_PHYSICAL_HIT -> this.spPerPhysicalHit_12a = value.get();
      case MP_PER_PHYSICAL_HIT -> this.mpPerPhysicalHit_12c = value.get();
      case SP_PER_MAGICAL_HIT -> this.spPerMagicalHit_12e = value.get();
      case MP_PER_MAGICAL_HIT -> this.mpPerMagicalHit_130 = value.get();
      case ESCAPE_BONUS_151 -> this.escapeBonus_132 = value.get();
      case HP_REGEN -> this.hpRegen_134 = value.get();
      case MP_REGEN -> this.mpRegen_136 = value.get();
      case SP_REGEN -> this.spRegen_138 = value.get();
      case REVIVE -> this.revive_13a = value.get();
      case HP_MULTI -> this.hpMulti_13c = value.get();
      case MP_MULTI -> this.mpMulti_13e = value.get();

      case GUARD_HEAL_RAW -> this.stats.getStat(GUARD_HEAL_STAT.get()).setRaw(value.get());

      default -> super.setStat(statIndex, value);
    }
  }
}
