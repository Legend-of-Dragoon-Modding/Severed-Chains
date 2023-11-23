package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.characters.Element;
import legend.game.characters.TurnBasedPercentileBuff;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.AttackEvent;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundDisplayMetrics0c;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.combat.environment.BattleMenuHighlightMetrics12;
import legend.game.combat.types.AttackType;
import legend.game.combat.ui.BattleDisplayStats144;
import legend.game.combat.ui.BattleDisplayStatsDigit10;
import legend.game.combat.ui.BattleHudCharacterDisplay3c;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.CombatItem02;
import legend.game.combat.ui.FloatingNumberC4;
import legend.game.combat.ui.FloatingNumberDigit20;
import legend.game.combat.ui.SpellAndItemMenuA4;
import legend.game.combat.ui.UiBox;
import legend.game.inventory.Item;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleDescriptionEvent;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.modding.events.inventory.RepeatItemReturnEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import legend.game.types.SpellStats0c;
import legend.game.types.Translucency;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c697c;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c.activePartyBattleHudCharacterDisplays_800c6c40;
import static legend.game.combat.Bttl_800c.aliveBentCount_800c669c;
import static legend.game.combat.Bttl_800c.aliveMonsterCount_800c6758;
import static legend.game.combat.Bttl_800c.allText_800fb3c0;
import static legend.game.combat.Bttl_800c.battleMenuBackgroundDisplayMetrics_800fb614;
import static legend.game.combat.Bttl_800c.battleMenuIconHeights_800fb6bc;
import static legend.game.combat.Bttl_800c.battleMenuIconStates_800c71e4;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.battleUiElementClutVramXy_800c7114;
import static legend.game.combat.Bttl_800c.cameraPositionIndicesIndices_800c6c30;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatItems_800c6988;
import static legend.game.combat.Bttl_800c.countCameraPositionIndicesIndices_800c6ba0;
import static legend.game.combat.Bttl_800c.currentCameraPositionIndicesIndex_800c66b0;
import static legend.game.combat.Bttl_800c.currentCameraPositionIndicesIndicesIndex_800c6ba1;
import static legend.game.combat.Bttl_800c.currentStageData_800c6718;
import static legend.game.combat.Bttl_800c.digitOffsetX_800c7014;
import static legend.game.combat.Bttl_800c.digitOffsetY_800c7014;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonSpaceElement_800c6b64;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.floatingTextType1DigitUs_800c7028;
import static legend.game.combat.Bttl_800c.floatingTextType1Digits;
import static legend.game.combat.Bttl_800c.floatingTextType3DigitUs_800c70e0;
import static legend.game.combat.Bttl_800c.iconFlags_800c7194;
import static legend.game.combat.Bttl_800c.itemTargetAll_800c69c8;
import static legend.game.combat.Bttl_800c.itemTargetType_800c6b68;
import static legend.game.combat.Bttl_800c.melbuMonsterNames_800c6ba8;
import static legend.game.combat.Bttl_800c.melbuStageToMonsterNameIndices_800c6f30;
import static legend.game.combat.Bttl_800c.protectedItems_800c72cc;
import static legend.game.combat.Bttl_800c.repeatItemIds_800c6e34;
import static legend.game.combat.Bttl_800c.spellAndItemMenu_800c6b60;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;
import static legend.game.combat.Bttl_800c.targetAllItemIds_800c7124;
import static legend.game.combat.Bttl_800c.targetBents_800c71f0;
import static legend.game.combat.Bttl_800c.textboxColours_800c6fec;
import static legend.game.combat.Bttl_800c.usedRepeatItems_800c6c3c;
import static legend.game.combat.Bttl_800e.initializeBattleHudCharacterDisplay;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;

public final class Bttl_800f {
  private Bttl_800f() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bttl_800f.class);

  public static void deleteFloatingTextDigits() {
    for(int i = 0; i < floatingTextType1Digits.length; i++) {
      if(floatingTextType1Digits[i] != null) {
        floatingTextType1Digits[i].delete();
        floatingTextType1Digits[i] = null;
      }
    }

    for(int i = 0; i < 10; i++) {
      if(type1FloatingDigits[i] != null) {
        type1FloatingDigits[i].delete();
        type1FloatingDigits[i] = null;
      }

      if(type3FloatingDigits[i] != null) {
        type3FloatingDigits[i].delete();
        type3FloatingDigits[i] = null;
      }

      if(miss != null) {
        miss.delete();
        miss = null;
      }
    }
  }

  /**
   * @param numberIndex which UI element it is, controls positioning
   */
  @Method(0x800f1550L)
  public static void renderNumber(final int charSlot, final int numberIndex, int value, final int colour) {
    if(floatingTextType1Digits[0] == null) {
      for(int i = 0; i < 10; i++) {
        floatingTextType1Digits[i] = buildUiTextureElement(
          "Floating text type 1 digit " + i,
          floatingTextType1DigitUs_800c7028[i], 32,
          8, 8,
          0x80
        );
      }
    }

    final int digitCount = MathHelper.digitCount(value);

    //LAB_800f16e4
    final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];

    final int[] digits = new int[digitCount];

    for(int i = 0; i < displayStats.digits_04[numberIndex].length; i++) {
      displayStats.digits_04[numberIndex][i].digitValue_00 = -1;
    }

    //LAB_800f171c
    Arrays.fill(digits, -1);

    int divisor = 1;

    //LAB_800f1768
    for(int i = 0; i < digitCount - 1; i++) {
      divisor *= 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < digitCount; i++) {
      digits[i] = value / divisor;
      value %= divisor;
      divisor /= 10;
    }

    //LAB_800f1800
    //LAB_800f1828
    final int rightAlignOffset = 4 - digitCount;

    //LAB_800f1848
    //LAB_800f184c
    //LAB_800f18cc
    for(int i = 0; i < digitCount; i++) {
      final BattleDisplayStatsDigit10 digit = displayStats.digits_04[numberIndex][i];

      if(numberIndex == 1 || numberIndex == 3 || numberIndex == 4) {
        //LAB_800f18f0
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + i * 5;
      } else {
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + (i + rightAlignOffset) * 5;
      }

      //LAB_800f1920
      digit.y_04 = digitOffsetY_800c7014[numberIndex];

      if(colour == 1) {
        digit.colour.set(1.0f, 1.0f, 1.0f);
      } else if(colour == 2) {
        digit.colour.set(248 / 240.0f, 224 / 240.0f, 72 / 240.0f);
      } else if(colour == 3) {
        digit.colour.set(224 / 240.0f, 72 / 240.0f, 0.0f);
      }

      //LAB_800f1998
      //LAB_800f199c
      digit.digitValue_00 = digits[i];
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public static void FUN_800f1a00(final long a0) {
    if(a0 != 1) {
      //LAB_800f1a10
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);

        if(v1.charIndex_00.get() != -1) {
          v1._14.get(2).set(0);
          v1.flags_06.and(0xffff_fffe).and(0xffff_fffd);
        }

        //LAB_800f1a4c
      }

      return;
    }

    //LAB_800f1a64
    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);
      if(v1.charIndex_00.get() != -1) {
        v1._14.get(2).set(0);
        v1.flags_06.or(0x3);
      }

      //LAB_800f1a90
    }
  }

  @Method(0x800f1aa8L)
  public static boolean checkHit(final int attackerIndex, final int defenderIndex, final AttackType attackType) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerIndex].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[defenderIndex].innerStruct_00;
    final boolean isMonster = attacker instanceof MonsterBattleEntity;

    int effectAccuracy;
    if(attackType == AttackType.PHYSICAL) {
      setTempSpellStats(attacker);

      if(isMonster) {
        effectAccuracy = attacker.spell_94.accuracy_05;
      } else {
        //LAB_800f1bf4
        effectAccuracy = attacker.attackHit_3c;
      }
    } else if(attackType == AttackType.DRAGOON_MAGIC_STATUS_ITEMS) {
      //LAB_800f1c08
      setTempSpellStats(attacker);
      effectAccuracy = attacker.spell_94.accuracy_05;
    } else {
      //LAB_800f1c38
      setTempItemMagicStats(attacker);
      effectAccuracy = 100;
    }

    //LAB_800f1c44
    final int hitStat = (byte)attacker.getStat(attackType.tempHitStat);
    effectAccuracy = effectAccuracy * (hitStat + 100) / 100;

    final int avoidChance;
    if(attackType == AttackType.PHYSICAL) {
      avoidChance = defender.attackAvoid_40;
    } else {
      //LAB_800f1c9c
      avoidChance = defender.magicAvoid_42;
    }

    boolean effectHit = false;

    //LAB_800f1ca8
    final int modifiedAvoidChance = (avoidChance * ((byte)attacker.getStat(attackType.tempAvoidStat) + 100)) / 100;
    if(modifiedAvoidChance < effectAccuracy && effectAccuracy - modifiedAvoidChance >= (simpleRand() * 101 >> 16)) {
      effectHit = true;

      if(isMonster) {
        setTempSpellStats(attacker);
      }
    }

    //LAB_800f1d28
    if((attacker.getStat(attackType.alwaysHitStat) & attackType.alwaysHitMask) != 0) {
      effectHit = true;
    }

    //LAB_800f1d5c
    return effectHit;
  }

  public static int adjustDamageForPower(final int damage, final int attackerStat, final int defenderStat) {
    float base = 1.0f;

    if(attackerStat < 0) {
      base /= 2.0f;
    }

    if(defenderStat > 0) {
      base /= 2.0f;
    }

    if(attackerStat > 0) {
      base += 0.5f;
    }

    if(defenderStat < 0) {
      base += 0.5f;
    }

    return (int)(damage * base);
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Method(0x800f204cL)
  public static int calculateMagicDamage(final BattleEntity27c attacker, final BattleEntity27c defender, final int magicType) {
    // Stat mod item
    if(magicType == 0 && attacker.item_d4.type_0b != 0) {
      //LAB_800f2404
      //LAB_800f2410
      int s1;
      // HP, MP, SP, revive, cure status, cause status
      for(s1 = 0; s1 < 8; s1++) {
        if((attacker.item_d4.type_0b & 0x80 >> s1) != 0) {
          break;
        }
      }

      //LAB_800f2430
      final int value = switch(s1) {
        case 0 -> {
          //LAB_800f2454
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();
        }

        case 1 -> {
          //LAB_800f2464
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();
        }

        //LAB_800f2478
        case 6 -> defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();

        //LAB_800f2484
        case 7 -> defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();

        //LAB_800f2490
        default -> 0;
      };

      //LAB_800f2494
      //LAB_800f24bc
      return value * attacker.item_d4.percentage_09 / 100;
    }

    //LAB_800f2140
    int damage;
    if(attacker.spell_94 != null && (attacker.spell_94.flags_01 & 0x4) != 0) {
      damage = defender.stats.getStat(CoreMod.HP_STAT.get()).getMax() * attacker.spell_94.multi_04 / 100;

      final List<BattleEntity27c> targets = new ArrayList<>();
      if((attacker.spell_94.targetType_00 & 0x8) != 0) { // Attack all
        if(attacker instanceof PlayerBattleEntity) {
          for(int i = 0; i < charCount_800c677c.get(); i++) {
            targets.add(battleState_8006e398.charBents_e40[i].innerStruct_00);
          }
        } else {
          for(int i = 0; i < aliveMonsterCount_800c6758.get(); i++) {
            targets.add(battleState_8006e398.aliveMonsterBents_ebc[i].innerStruct_00);
          }
        }
      } else { // Attack single
        targets.add(defender);
      }

      for(final BattleEntity27c target : targets) {
        applyBuffOrDebuff(attacker, target);
      }

      //LAB_800f2224
      attacker.status_0e |= 0x800;
    } else {
      final Element attackElement = magicType == 1 ? attacker.spell_94.element_08 : attacker.item_d4.element_01;
      final AttackType attackType = magicType == 1 ? AttackType.DRAGOON_MAGIC_STATUS_ITEMS : AttackType.ITEM_MAGIC;

      //LAB_800f2238
      damage = attacker.calculateMagicDamage(defender, magicType);
      damage = attackElement.adjustAttackingElementalDamage(attackType, damage, defender.getElement());
      damage = defender.getElement().adjustDefendingElementalDamage(attackType, damage, attackElement);
      damage = adjustDamageForPower(damage, attacker.powerMagicAttack_b6, defender.powerMagicDefence_ba);

      if(dragoonSpaceElement_800c6b64 != null) {
        damage = attackElement.adjustDragoonSpaceDamage(attackType, damage, dragoonSpaceElement_800c6b64);
      }
    }

    //LAB_800f24c0
    if(damage < 0) {
      damage = 0;
    }

    //LAB_800f24d0
    return damage;
  }

  @ScriptDescription("Perform a battle entity's physical attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2500L)
  public static FlowControl scriptPhysicalAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    final int damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.PHYSICAL, CoreMod.PHYSICAL_DAMAGE_FORMULA.calculate(attacker, defender))).damage;

    script.params_20[2].set(damage);
    script.params_20[3].set(determineAttackSpecialEffects(attacker, defender, AttackType.PHYSICAL));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's magic or status attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spellId", description = "The attacker's spell ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2694L)
  public static FlowControl scriptDragoonMagicStatusItemAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.spellId_4e = script.params_20[2].get();

    clearTempWeaponAndSpellStats(attacker);
    setTempSpellStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 1);
    damage = applyMagicDamageMultiplier(attacker, damage, 0);
    damage = Math.max(1, damage);

    //LAB_800f272c
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.DRAGOON_MAGIC_STATUS_ITEMS);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.spell_94.element_08);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS, damage)).damage;

    //LAB_800f27ec
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's item attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2838L)
  public static FlowControl scriptItemMagicAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    clearTempWeaponAndSpellStats(attacker);
    setTempItemMagicStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 0);
    damage = applyMagicDamageMultiplier(attacker, damage, 1);
    damage = Math.max(1, damage);

    //LAB_800f28c8
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.ITEM_MAGIC);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.item_d4.element_01);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.ITEM_MAGIC, damage)).damage;

    //LAB_800f2970
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.ITEM_MAGIC));
    applyItemSpecialEffects(attacker, defender);
    return FlowControl.CONTINUE;
  }

  private static final Obj[] type1FloatingDigits = new Obj[10];
  private static final Obj[] type3FloatingDigits = new Obj[10];
  private static Obj miss;

  @Method(0x800f3354L)
  public static void addFloatingNumber(final int numIndex, final int onHitTextType, final int onHitClutCol, final int number, final float x, final float y, int ticks, int colour) {
    if(miss == null) {
      for(int i = 0; i < 10; i++) {
        final QuadBuilder builder1 = new QuadBuilder("Type 1 Floating Digit " + i)
          .uv(floatingTextType1DigitUs_800c7028[i], 32)
          .size(8.0f, 8.0f);
        setGpuPacketClutAndTpageAndQueue(builder1, 0x80, null);
        type1FloatingDigits[i] = builder1.build();

        final QuadBuilder builder3 = new QuadBuilder("Type 3 Floating Digit " + i)
          .uv(floatingTextType3DigitUs_800c70e0.get(i).get(), 40)
          .size(8.0f, 16.0f);
        setGpuPacketClutAndTpageAndQueue(builder3, 0x80, null);
        type3FloatingDigits[i] = builder3.build();
      }

      final QuadBuilder builderMiss = new QuadBuilder("Miss Floating Digit")
        .uv(72, 128)
        .size(36.0f, 16.0f);
      setGpuPacketClutAndTpageAndQueue(builderMiss, 0x80, null);
      miss = builderMiss.build();
    }

    final FloatingNumberC4 num = floatingNumbers_800c6b5c[numIndex];
    final int[] damageDigits = new int[num.digits_24.length];

    final int floatingTextType;  // 0=floating numbers, 1=MP cost, 2=miss
    final int clutCol; //TODO: confirm this, it may not be this exactly
    if(number != -1) {
      floatingTextType = onHitTextType;
      clutCol = onHitClutCol;
    } else {
      floatingTextType = 2;
      clutCol = 2;
      colour = 14;
    }

    switch(colour) {
      case 0 -> num.colour.set(1.0333333f, 0.6666667f, 0.0f); // Orange
      case 1 -> num.colour.set(1.0f, 1.0f, 1.0f); // Whiter
      case 2, 6 -> num.colour.set(1.0f, 1.0f, 1.0f); // White
      case 3, 7 -> num.colour.set(0.4f, 0.93333334f, 1.0333333f); // Cyan
      case 4, 8 -> num.colour.set(1.0333333f, 0.93333334f, 0.3f); // Yellow
      case 5, 9, 14 -> num.colour.set(0.93333334f, 0.3f, 0.0f); // Red
      case 10, 12 -> num.colour.set(0.56666666f, 0.53333336f, 1.0333333f); // Violet
      case 11, 13 -> num.colour.set(0.3f, 1.0333333f, 0.3f); // Green
    }

    //LAB_800f34d4
    num.flags_02 = 0;
    num.bentIndex_04 = -1;
    num.translucent_08 = false;
    num.shade_0c = 0x80;
    num._18 = -1;
    num.ticksRemaining_14 = -1;

    //LAB_800f3528
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].flags_00 = 0;
      num.digits_24[i]._04 = 0;
      num.digits_24[i]._08 = 0;
      num.digits_24[i].digit_0c = -1;
    }

    num.state_00 = 1;

    if(ticks == 0) {
      num.flags_02 |= 0x1;
    }

    //LAB_800f3588
    num.flags_02 |= 0x8000;
    num._10 = clutCol;

    if(clutCol == 2 && ticks == 0) {
      ticks = 60 / vsyncMode_8007a3b8 * 2;
    }

    //LAB_800f35dc
    //LAB_800f35e4
    //LAB_800f3608
    int damage = MathHelper.clamp(number, 0, 999999999);

    //LAB_800f3614
    num.x_1c = x;
    num.y_20 = y;

    //LAB_800f3654
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].digit_0c = -1;
      damageDigits[i] = -1;
    }

    //LAB_800f36a0
    //Sets what places to render
    int currDigitPlace = (int)Math.pow(10, num.digits_24.length - 1);
    for(int i = 0; i < num.digits_24.length; i++) {
      damageDigits[i] = damage / currDigitPlace;
      damage %= currDigitPlace;
      currDigitPlace /= 10;
    }

    //LAB_800f36dc
    //LAB_800f36ec
    int digitIdx;
    for(digitIdx = 0; digitIdx < num.digits_24.length - 1; digitIdx++) {
      if(damageDigits[digitIdx] != 0) {
        break;
      }
    }

    //LAB_800f370c
    //LAB_800f3710
    int displayPosX;
    if(floatingTextType == 1) {
      //LAB_800f3738
      displayPosX = -(num.digits_24.length - digitIdx) * 5 / 2;
    } else if(floatingTextType == 2) {
      //LAB_800f3758
      displayPosX = -18;
    } else {
      //LAB_800f372c
      displayPosX = -(num.digits_24.length - digitIdx) * 4;
    }

    //LAB_800f375c
    //LAB_800f37ac
    int digitStructIdx;
    for(digitStructIdx = 0; digitStructIdx < num.digits_24.length && digitIdx < num.digits_24.length; digitStructIdx++) {
      final FloatingNumberDigit20 digit = num.digits_24[digitStructIdx];
      digit.flags_00 = 0x8000;
      digit.x_0e = displayPosX;
      digit.y_10 = 0;

      if(clutCol == 2) {
        digit.flags_00 = 0;
        digit._04 = digitStructIdx;
        digit._08 = 0;
      }

      //LAB_800f37d8
      if(floatingTextType == 1) {
        //LAB_800f382c
        digit.obj = type1FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 5;
      } else if(floatingTextType == 2) {
        //LAB_800f386c
        digit.obj = miss;
        displayPosX += 36;
      } else {
        //LAB_800f37f4
        digit.obj = type3FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 8;
      }

      //LAB_800f3898
      digit.digit_0c = damageDigits[digitIdx];

      digitIdx++;
    }

    //LAB_800f38e8
    num.ticksRemaining_14 = digitStructIdx + 12;
    num._18 = ticks + 4; //TODO: ID duration meaning
  }

  @Method(0x800f3940L)
  public static void tickFloatingNumbers() {
    //LAB_800f3978
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          if(num.bentIndex_04 != -1) {
            final ScriptState<?> state = scriptStatePtrArr_800bc1c0[num.bentIndex_04];
            final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

            final float x;
            final float y;
            final float z;
            if(bent instanceof final MonsterBattleEntity monster) {
              x = -monster.targetArrowPos_78.x * 100.0f;
              y = -monster.targetArrowPos_78.y * 100.0f;
              z = -monster.targetArrowPos_78.z * 100.0f;
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);
            num.x_1c = clampX(screenCoords.x + centreScreenX_1f8003dc.get());
            num.y_20 = clampY(screenCoords.y + centreScreenY_1f8003de.get());
          }

          //LAB_800f3ac8
          if(num.state_00 == 1) {
            //LAB_800f3b24
            if(num._10 == 0x2) {
              num.state_00 = 2;
            } else {
              //LAB_800f3b44
              num.state_00 = 98;
            }
          } else if(num.state_00 == 2) {
            //LAB_800f3b50
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];

              if(digit.digit_0c == -1) {
                break;
              }

              if((digit.flags_00 & 0x1) != 0) {
                if((digit.flags_00 & 0x2) != 0) {
                  if(digit._08 < 5) {
                    digit.y_10 += digit._08;
                    digit._08++;
                  }
                } else {
                  //LAB_800f3bb0
                  digit.flags_00 |= 0x8002;
                  digit._04 = digit.y_10;
                  digit._08 = -4;
                }
              } else {
                //LAB_800f3bc8
                if(digit._08 == digit._04) {
                  digit.flags_00 |= 0x1;
                }

                //LAB_800f3be0
                digit._08++;
              }
            }

            //LAB_800f3c00
            num.ticksRemaining_14--;
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 98;
              num.ticksRemaining_14 = num._18;
            }
          } else if(num.state_00 == 97) {
            //LAB_800f3c34
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 100;
            } else {
              //LAB_800f3c50
              num.ticksRemaining_14--;
              num.shade_0c = (num.shade_0c - (num._18 & 0xff)) & 0xff;
            }
          } else if(num.state_00 == 100) {
            //LAB_800f3d38
            num.state_00 = 0;
            num.flags_02 = 0;
            num.bentIndex_04 = -1;
            num.translucent_08 = false;
            num.shade_0c = 0x80;
            num.ticksRemaining_14 = -1;
            num._18 = -1;

            //LAB_800f3d60
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];
              digit.flags_00 = 0;
              digit._04 = 0;
              digit._08 = 0;
              digit.digit_0c = -1;
            }
            //LAB_800f3b04
          } else if(num.state_00 < 99) {
            //LAB_800f3c88
            if((num.flags_02 & 0x1) != 0) {
              num.state_00 = 99;
            } else {
              //LAB_800f3ca4
              num.ticksRemaining_14--;

              if(num.ticksRemaining_14 <= 0) {
                if(num._10 > 0 && num._10 < 3) {
                  num.state_00 = 97;
                  num.translucent_08 = true;
                  num.shade_0c = 0x60;

                  final int ticksRemaining = 60 / vsyncMode_8007a3b8 / 2;
                  num.ticksRemaining_14 = ticksRemaining;
                  num._18 = 96 / ticksRemaining;
                } else {
                  //LAB_800f3d24
                  //LAB_800f3d2c
                  num.state_00 = 100;
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800f3dbcL)
  public static void drawFloatingNumbers() {
    //LAB_800f3e20
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          //LAB_800f3e80
          for(int i = 0; i < num.digits_24.length; i++) {
            final FloatingNumberDigit20 digit = num.digits_24[i];

            if(digit.digit_0c == -1) {
              break;
            }

            if((digit.flags_00 & 0x8000) != 0) {
              //LAB_800f3ec0
              num.transforms.transfer.set(digit.x_0e + num.x_1c, digit.y_10 + num.y_20, 28.0f);
              RENDERER.queueOrthoOverlayModel(digit.obj, num.transforms)
                .colour(num.colour);

              if((num.state_00 & 97) == 0) {
                //LAB_800f4118
                break;
              }

              //LAB_800f4110
            }
          }
        }
      }
      //LAB_800f4134
    }
  }

  @Method(0x800f417cL)
  public static void FUN_800f417c() {
    //LAB_800f41ac
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      final BattleHudCharacterDisplay3c s1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);

      if(s1.charIndex_00.get() == -1 && _800be5d0.get() == 1) {
        initializeBattleHudCharacterDisplay(i);
      }

      //LAB_800f41dc
    }

    //LAB_800f41f4
    //LAB_800f41f8
    short x = 63;

    //LAB_800f4220
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
      final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);

      if(v1.charIndex_00.get() != -1) {
        v1.x_08.set(x);
        displayStats.x_00 = x;
      }

      //LAB_800f4238
      x += 94;
    }
  }

  @Method(0x800f4268L)
  public static void addFloatingNumberForBent(final int bentIndex, final int damage, final int s4) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[bentIndex];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

    final float x;
    final float y;
    final float z;
    if(bent instanceof final MonsterBattleEntity monster) {
      // ZYX is the correct order
      x = -monster.targetArrowPos_78.z * 100.0f;
      y = -monster.targetArrowPos_78.y * 100.0f;
      z = -monster.targetArrowPos_78.x * 100.0f;
    } else {
      //LAB_800f4314
      x = 0;
      y = -640;
      z = 0;
    }

    //LAB_800f4320
    final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);

    //LAB_800f4394
    FUN_800f89f4(bentIndex, 0, 2, damage, clampX(screenCoords.x + centreScreenX_1f8003dc.get()), clampY(screenCoords.y + centreScreenY_1f8003de.get()), 60 / vsyncMode_8007a3b8 / 4, s4);
  }

  @ScriptDescription("Gives SP to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to add")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "total", description = "The total SP after adding the amount requested")
  @Method(0x800f43dcL)
  public static FlowControl scriptGiveSp(final RunningScript<?> script) {
    //LAB_800f43f8
    //LAB_800f4410
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(battleState_8006e398.charBents_e40[charSlot].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f4430
    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() + script.params_20[1].get());
    spGained_800bc950.get(charSlot).add(script.params_20[1].get());

    //LAB_800f4500
    script.params_20[2].set(sp.getCurrent());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Consumes SP from a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to take away")
  @Method(0x800f4518L)
  public static FlowControl scriptConsumeSp(final RunningScript<?> script) {
    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < charCount_800c677c.get(); i++) {
      if(battleState_8006e398.charBents_e40[i].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f456c
    final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(i);
    charDisplay.unused_0c.set((short)0);
    charDisplay.unused_0e.set((short)script.params_20[1].get());

    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() - script.params_20[2].get());

    if(sp.getCurrent() == 0) {
      charDisplay.flags_06.and(0xfff3);
    }

    //LAB_800f45f8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, this might handle players selecting an attack target")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemOrSpellId", description = "The item or spell ID selected")
  @Method(0x800f4600L)
  public static FlowControl FUN_800f4600(final RunningScript<?> script) {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    int itemOrSpellId = menu.itemOrSpellId_1c;
    if(menu.charIndex_08 == 8 && menu.menuType_0a == 1) {
      if(itemOrSpellId == 10) {
        itemOrSpellId = 65;
      }

      //LAB_800f46ec
      if(itemOrSpellId == 11) {
        itemOrSpellId = 66;
      }

      //LAB_800f46f8
      if(itemOrSpellId == 12) {
        itemOrSpellId = 67;
      }
    }

    //LAB_800f4704
    //LAB_800f4708
    script.params_20[0].set(menu._a0);
    script.params_20[1].set(battleMenu_800c6c34.target_48);
    script.params_20[2].set(itemOrSpellId);

    //LAB_800f4770
    PlayerBattleEntity playerBent = null;
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      playerBent = battleState_8006e398.charBents_e40[i].innerStruct_00;

      if(playerBent.charId_272 == menu.charIndex_08) {
        break;
      }
    }

    //LAB_800f47ac
    playerBent.spellId_4e = itemOrSpellId;

    if(menu._a0 == 1 && menu.menuType_0a == 0) {
      //LAB_800f47e4
      for(int i = 0; i < 17; i++) {
        if(targetAllItemIds_800c7124.get(i).get() == itemOrSpellId + 0xc0) {
          //LAB_800f4674
          script.params_20[1].set(-1);
          break;
        }
      }
    }

    //LAB_800f4800
    //LAB_800f4804
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the item/spell attack target")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetMode")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @Method(0x800f480cL)
  public static FlowControl scriptGetItemOrSpellAttackTarget(final RunningScript<?> script) {
    BattleEntity27c a1 = null;
    final int[] sp0x10 = {0, 0, 1, 0, 2, 1, 1, 1};

    int targetMode = script.params_20[0].get();

    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    //LAB_800f489c
    for(int a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = battleState_8006e398.charBents_e40[a0].innerStruct_00;

      if(menu.charIndex_04 == a1.charId_272) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1.specialEffectFlag_14 & 0x8) != 0) { // "Attack all"
      targetMode = 3;
    }

    //LAB_800f48f4
    int ret = FUN_800f7768(sp0x10[targetMode * 2], sp0x10[targetMode * 2 + 1] != 0);
    if(ret == 0) { // No buttons pressed
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(ret == 1) { // Pressed X
      //LAB_800f4930
      ret = menu.target_48;
    } else { // Pressed O
      //LAB_800f4944
      //LAB_800f4948
      ret = -1;
    }

    //LAB_800f4950
    script.params_20[1].set(ret);

    //LAB_800f4954
    return FlowControl.CONTINUE;
  }

  @Method(0x800f4964L)
  public static void clearSpellAndItemMenu() {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    menu.menuState_00 = 0;
    menu._02 = 0;
    menu.x_04 = 0;
    menu.y_06 = 0;
    menu.charIndex_08 = 0;
    menu.menuType_0a = 0;
    menu._0c = 0;
    menu._0e = 0;
    menu._10 = 0;
    menu._12 = 0;
    menu._14 = 0;
    menu._16 = 0x1000;
    menu.textX_18 = 0;
    menu._1a = 0;
    menu.itemOrSpellId_1c = -1;
    menu.count_22 = 0;
    menu.listScroll_24 = 0;
  }

  @Method(0x800f49bcL)
  public static void initSpellAndItemMenu(final int charIndex, final int menuType) {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    menu.menuState_00 = 1;
    menu.x_04 = 160;
    menu.y_06 = 144;
    menu.charIndex_08 = (short)charIndex;
    menu.menuType_0a = (short)(menuType & 0x1);
    menu._0c = 0x20;
    menu._0e = 0x2b;
    menu._10 = 0;
    menu._12 = 0;
    menu._14 = 0x1;
    menu._16 = 0x1000;
    menu.textX_18 = 0;
    menu._1a = 0;
    menu.itemOrSpellId_1c = -1;
    menu.listIndex_1e = 0;
    menu._20 = 0;

    //LAB_800f4a58
    if(menuType == 0) {
      //LAB_800f4a9c
      prepareItemList();
      menu.count_22 = (short)combatItems_800c6988.size();
    } else if(menuType == 1) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == menu.charIndex_08) {
          break;
        }
      }

      //LAB_800f4b00
      //LAB_800f4b18
      short spellIndex;
      for(spellIndex = 0; spellIndex < 8; spellIndex++) {
        if(dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).get() == -1) {
          break;
        }
      }

      //LAB_800f4b3c
      menu.count_22 = spellIndex;
    } else if(menuType == 2) {
      //LAB_800f4b4c
      menu.count_22 = 0;
    }

    //LAB_800f4b50
    //LAB_800f4b54
    //LAB_800f4b60
    menu._7c = 0;
    menu._80 = 0;
    menu._84 = 0;
    menu._88 = 0;
    menu._8c = 0;
    menu._90 = 0;
    menu._94 = 0;
    menu._98 = 0;
    menu._9c = 0;
    menu._a0 = 0;
  }

  @Method(0x800f4b80L)
  public static void handleSpellAndItemMenu() {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    if(menu.menuState_00 == 0) {
      return;
    }

    int v0;
    final int a1;
    int s0;

    //LAB_800f4bc0
    switch(menu.menuState_00) {
      case 1 -> {
        menu._90 = 0;
        menu._a0 = 0;
        menu._12 = 0;
        menu._10 = 0;

        if(menu.menuType_0a == 0) {
          menu.listScroll_24 = menu._26;
          menu._02 |= 0x20;
          menu.listIndex_1e = menu._28;
          menu._20 = menu._2a;
          menu._94 = menu._2c;

          if(menu.count_22 - 1 < menu.listScroll_24 + menu.listIndex_1e) {
            menu.listScroll_24--;

            if(menu.listScroll_24 < 0) {
              menu.listScroll_24 = 0;
              menu.listIndex_1e = 0;
              menu._20 = menu._1a;
              menu._94 = 0; // This was a3.1a - a3.1a
            }
          }
        } else {
          //LAB_800f4ca0
          menu.listIndex_1e = 0;
          menu._20 = 0;
          menu._94 = 0;
          menu.listScroll_24 = menu._30;
        }

        //LAB_800f4cb4
        menu.itemOrSpellId_1c = (short)getItemOrSpellId();
        menu.menuState_00 = 7;
        menu._02 |= 0x40;
      }

      case 2 -> {
        menu._02 &= 0xfcff;
        menu.itemOrSpellId_1c = (short)getItemOrSpellId();

        if((press_800bee94.get() & 0x4) != 0) { // L1
          if(menu.listScroll_24 != 0) {
            menu._88 = 2;
            menu.listScroll_24 = 0;
            menu.menuState_00 = 5;
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((press_800bee94.get() & 0x1) != 0) { // L2
          final int oldScroll = menu.listScroll_24;

          if(menu.count_22 - 1 >= menu.listIndex_1e + 6) {
            menu.listScroll_24 = 6;
          } else {
            //LAB_800f4d8c
            menu.listScroll_24 = (short)(menu.count_22 - (menu.listIndex_1e + 1));
          }

          //LAB_800f4d90
          menu._88 = 2;
          menu.menuState_00 = 5;

          if(oldScroll != menu.listScroll_24) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4dc4
        if((press_800bee94.get() & 0x8) != 0) { // R1
          if(menu.listIndex_1e == 0) {
            break;
          }

          if(menu.listIndex_1e < 7) {
            menu.listScroll_24 = 0;
            menu.listIndex_1e = 0;
            menu._20 = menu._1a;
          } else {
            //LAB_800f4df4
            menu.listIndex_1e -= 7;
            menu._20 += 98;
          }

          //LAB_800f4e00
          menu._88 = 2;
          menu.menuState_00 = 5;
          menu._94 = menu._1a - menu._20;
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4e40
        if((press_800bee94.get() & 0x2) != 0) { // R2
          if(menu.listIndex_1e + 6 >= menu.count_22 - 1) {
            break;
          }

          menu.listIndex_1e += 7;
          menu._20 -= 98;

          if(menu.listIndex_1e + 6 >= menu.count_22 - 1) {
            menu.listScroll_24 = 0;
          }

          //LAB_800f4e98
          menu._88 = 2;
          menu.menuState_00 = 5;
          menu._94 = menu._1a - menu._20;
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4ecc
        if((input_800bee90.get() & 0x1000) != 0) { // Up
          if(menu.listScroll_24 != 0) {
            menu.menuState_00 = 5;
            menu.listScroll_24--;
            menu._88 = 2;
          } else {
            //LAB_800f4f18
            if(menu.listIndex_1e == 0) {
              break;
            }

            menu.menuState_00 = 3;
            menu._02 |= 0x200;
            menu._80 = 5;
            menu._7c = menu._20;
            menu._20 += 5;
            menu.listIndex_1e--;
          }

          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4f74
        if((input_800bee90.get() & 0x4000) != 0) { // Down
          if(menu.listScroll_24 != menu.count_22 - 1) {
            if(menu.listIndex_1e + menu.listScroll_24 + 1 < menu.count_22) {
              playSound(0, 1, 0, 0, (short)0, (short)0);

              if(menu.listScroll_24 != 6) {
                menu.listScroll_24++;
                menu._88 = 2;
                menu.menuState_00 = 5;
              } else {
                //LAB_800f4ff8
                menu._80 = -5;
                menu.menuState_00 = 4;
                menu._7c = menu._20;
                menu._20 -= 5;
                menu.listIndex_1e++;
                menu._02 |= 0x100;
              }
            }
          }

          break;
        }

        //LAB_800f5044
        menu._90 = 0;

        if((press_800bee94.get() & 0x20) != 0) { // X
          //LAB_800f5078
          PlayerBattleEntity player = null;

          for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
            player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

            if(menu.charIndex_08 == player.charId_272) {
              //LAB_800f503c
              _800c6980.set((short)charSlot);
              break;
            }
          }

          //LAB_800f50b8
          if(menu.menuType_0a == 0) {
            player.itemId_52 = menu.itemOrSpellId_1c;
            setTempItemMagicStats(player);

            if((player.item_d4.target_00 & 0x4) != 0) {
              itemTargetType_800c6b68.set(1);
            } else {
              //LAB_800f5100
              itemTargetType_800c6b68.set(0);
            }

            //LAB_800f5108
            //LAB_800f5128
            itemTargetAll_800c69c8.set((player.item_d4.target_00 & 0x2) != 0);
          } else {
            //LAB_800f5134
            final PlayerBattleEntity caster = setActiveCharacterSpell(menu.itemOrSpellId_1c);

            if(caster.stats.getStat(CoreMod.MP_STAT.get()).getCurrent() < caster.spell_94.mp_06) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            final FloatingNumberC4 num = floatingNumbers_800c6b5c[0];
            num.state_00 = 0;
            num.flags_02 = 0;
          }

          //LAB_800f5190
          playSound(0, 2, 0, 0, (short)0, (short)0);
          menu._8c = 0;
          menu._02 |= 0x4;
          if(menu.menuType_0a == 0) {
            menu._94 = menu._1a - menu._20;
          }

          //LAB_800f51e8
          menu.menuState_00 = 6;
          menu._02 &= 0xfffd;
          break;
        }

        //LAB_800f5208
        if((press_800bee94.get() & 0x40) != 0) { // O
          playSound(0, 3, 0, 0, (short)0, (short)0);
          menu.menuState_00 = 8;
          menu._02 &= 0xfff7;
        }
      }

      case 3 -> {
        s0 = menu._80;
        menu._90++;
        if(menu._90 >= 3) {
          s0 *= 2;
        }

        //LAB_800f5278
        a1 = menu._7c + 14;
        menu._20 += s0;
        if(menu._20 >= a1) {
          menu._20 = (short)a1;
          menu.menuState_00 = 2;
        }
      }

      case 4 -> {
        s0 = menu._80;
        menu._90++;
        if(menu._90 >= 3) {
          s0 = s0 * 2;
        }

        //LAB_800f52d4
        a1 = menu._7c - 14;
        menu._20 += s0;
        if(menu._20 <= a1) {
          //LAB_800f5300
          menu._20 = (short)a1;
          menu.menuState_00 = 2;
        }
      }

      case 5 -> {
        s0 = menu._88;
        menu._90++;
        if(menu._90 >= 3) {
          s0 = s0 / 2;
        }

        //LAB_800f5338
        if(s0 <= 1) {
          menu.menuState_00 = 2;
        }
      }

      case 6 -> {
        menu._a0 = 0;
        menu.itemOrSpellId_1c = (short)getItemOrSpellId();
        PlayerBattleEntity player;

        //LAB_800f538c
        int charSlot = 0;
        do {
          player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

          if(menu.charIndex_08 == player.charId_272) {
            break;
          }

          charSlot++;
        } while(charSlot < charCount_800c677c.get());

        //LAB_800f53c8
        final int targetType;
        final boolean targetAll;
        if(menu.menuType_0a == 0) { // Items
          targetType = itemTargetType_800c6b68.get();
          targetAll = itemTargetAll_800c69c8.get();
        } else { // Spells
          //LAB_800f53f8
          final int itemTargetType = player.spell_94.targetType_00;
          targetType = (itemTargetType & 0x40) > 0 ? 1 : 0;
          targetAll = (itemTargetType & 0x8) != 0;
        }

        //LAB_800f5410
        final int ret = FUN_800f7768(targetType, targetAll);
        if(ret == 1) { // Pressed X
          if(menu.menuType_0a == 0) {
            final int itemId = menu.itemOrSpellId_1c + 192;
            final Item item = REGISTRIES.items.getEntry(LodMod.itemIdMap.get(menu.itemOrSpellId_1c)).get(); //TODO
            takeItemId(item);

            boolean returnItem = false;
            for(int repeatItemIndex = 0; repeatItemIndex < 9; repeatItemIndex++) {
              if(itemId == repeatItemIds_800c6e34.get(repeatItemIndex).get()) {
                returnItem = true;
                break;
              }
            }

            if(itemId == 0xfa) { // Psych Bomb X
              returnItem = true;
            }

            final RepeatItemReturnEvent repeatItemReturnEvent = EVENTS.postEvent(new RepeatItemReturnEvent(itemId, returnItem));

            if(repeatItemReturnEvent.returnItem) {
              usedRepeatItems_800c6c3c.add(item);
            }
          }

          //LAB_800f545c
          if(menu.menuType_0a == 1) {
            final VitalsStat mp = player.stats.getStat(CoreMod.MP_STAT.get());
            mp.setCurrent(mp.getCurrent() - player.spell_94.mp_06);
          }

          //LAB_800f5488
          playSound(0, 2, 0, 0, (short)0, (short)0);
          menu._a0 = 1;
          menu.menuState_00 = 9;
        } else if(ret == -1) { // Pressed O
          //LAB_800f54b4
          playSound(0, 0, 3, 0, (short)0, (short)0);
          menu.menuState_00 = 7;
          menu._02 &= 0xfffb;
          menu._02 |= 0x20;
        }
      }

      case 7 -> {
        if(menu.menuType_0a != 0) {
          s0 = 0x80;
        } else {
          s0 = 0xba;
        }

        menu.menuState_00 = 2;
        playSound(0, 4, 0, 0, (short)0, (short)0);
        menu._12 = 0x52;
        menu._10 = s0;
        menu.textX_18 = (short)(menu.x_04 - s0 / 2 + 9);
        v0 = (menu.y_06 - menu._12) - 16;
        menu._1a = (short)v0;
        menu._20 = (short)v0;
        menu._02 |= 0xb;
        if((menu._02 & 0x20) != 0) {
          v0 = v0 - menu._94;
          menu._20 = (short)v0;
        }

        //LAB_800f5588
        if(menu.menuType_0a != 0) {
          menu.itemOrSpellId_1c = (short)getItemOrSpellId();
          addFloatingNumber(0, 1, 0, setActiveCharacterSpell(menu.itemOrSpellId_1c).spell_94.mp_06, 280, 135, 0, 1);
        }
      }

      case 8 -> {
        itemTargetAll_800c69c8.set(false);
        itemTargetType_800c6b68.set(0);
        menu._a0 = -1;
        menu.menuState_00 = 9;
        menu._12 = 0;
        menu._10 = 0;
        menu._02 &= 0xfffc;
        final FloatingNumberC4 num = floatingNumbers_800c6b5c[0];
        num.state_00 = 0;
        num.flags_02 = 0;
      }

      case 9 -> {
        if(menu.menuType_0a == 0) {
          v0 = menu._1a - menu._20;
          menu._26 = menu.listScroll_24;
          menu._28 = menu.listIndex_1e;
          menu._2a = menu._20;
          menu._94 = v0;
          menu._2c = v0;
        }

        //LAB_800f568c
        clearSpellAndItemMenu();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    menu._84 = tickCount_800bb0fc.get() & 0x7;

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  public static int getItemOrSpellId() {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    final short menuType = menu.menuType_0a;

    if(menuType == 0) {
      //LAB_800f56f0
      return LodMod.idItemMap.getInt(combatItems_800c6988.get(menu.listScroll_24 + menu.listIndex_1e).item.getRegistryId());
    }

    if(menuType == 1) {
      //LAB_800f5718
      //LAB_800f5740
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == menu.charIndex_08) {
          break;
        }
      }

      //LAB_800f5778
      int spellIndex = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(menu.listScroll_24 + menu.listIndex_1e).get();
      if(menu.charIndex_08 == 8) {
        if(spellIndex == 65) {
          spellIndex = 10;
        }

        //LAB_800f57d4
        if(spellIndex == 66) {
          spellIndex = 11;
        }

        //LAB_800f57e0
        if(spellIndex == 67) {
          spellIndex = 12;
        }
      }

      return spellIndex;
    }

    throw new RuntimeException("Undefined a0");
  }

  /**
   * @param type <ol start="0"><li>items</li><li>spells</li></ul>
   */
  @Method(0x800f57f8L)
  public static void renderList(final int type) {
    int trim;

    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;

    int y1 = menu._20;
    final int y2 = menu._1a;
    final int sp68 = menu.y_06;

    //LAB_800f5860
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == menu.charIndex_08) {
        break;
      }
    }

    //LAB_800f58a4
    int sp7c = 0;

    final LodString sp0x18 = new LodString(18);
    final LodString sp0x40 = new LodString(5);
    final LodString itemCount = new LodString(12);

    //LAB_800f58e0
    for(int spellSlot = 0; spellSlot < menu.count_22; spellSlot++) {
      if(y1 >= sp68) {
        break;
      }

      TextColour textColour = TextColour.WHITE;
      final LodString name;
      if(type == 0) {
        //LAB_800f5918
        name = new LodString(combatItems_800c6988.get(sp7c).item.getName());
        intToStr(combatItems_800c6988.get(sp7c).count, itemCount);

        //LAB_800f5968
        int i;
        for(i = 0; ; i++) {
          sp0x18.charAt(i, name.charAt(i));
          if(name.charAt(i) == 0xa0ff) {
            break;
          }
        }

        //LAB_800f5990
        //LAB_800f59a4
        for(; i < 16; i++) {
          sp0x18.charAt(i, 0);
        }

        //LAB_800f59bc
        if(combatItems_800c6988.get(sp7c).count < 10) {
          sp0x18.charAt(i, 0);
          i++;
        }

        //LAB_800f59e8
        sp0x18.charAt(i, 0xa0ff);
        sp0x40.charAt(0, 0xe);

        //LAB_800f5a10
        int n;
        for(n = 0; n < 2; n++) {
          final int chr = itemCount.charAt(n);
          if(chr == 0xa0ff) {
            break;
          }

          sp0x40.charAt(n + 1, chr);
        }

        //LAB_800f5a38
        sp0x40.charAt(n + 1, 0xa0ff);
      } else if(type == 1) {
        //LAB_800f5a4c
        int spellId = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellSlot).get();
        name = new LodString(spellStats_800fa0b8[spellId].name);

        if(menu.charIndex_08 == 8) {
          if(spellId == 65) {
            spellId = 10;
          }

          //LAB_800f5ab4
          if(spellId == 66) {
            spellId = 11;
          }

          //LAB_800f5ac0
          if(spellId == 67) {
            spellId = 12;
          }
        }

        //LAB_800f5acc
        final PlayerBattleEntity bent = setActiveCharacterSpell(spellId);

        // Not enough MP for spell
        if(bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent() < bent.spell_94.mp_06) {
          textColour = TextColour.GREY;
        }
      } else {
        throw new RuntimeException("Undefined s3");
      }

      //LAB_800f5af0
      if(y1 >= y2) {
        //LAB_800f5b90
        if(sp68 >= y1 + 12) {
          trim = 0;
        } else {
          trim = y1 - (sp68 - 12);
        }

        //LAB_800f5bb4
        if((menu._02 & 0x4) != 0) {
          trim = (short)menu._8c;
        }

        //LAB_800f5bd8
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y1, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, menu.textX_18 + 128, y1, textColour, trim);
        }
      } else if(y2 < y1 + 12) {
        if((menu._02 & 0x4) != 0) {
          trim = menu._8c;
        } else {
          trim = y1 - y2;
        }

        //LAB_800f5b40
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y2, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, menu.textX_18 + 128, y2, textColour, trim);
        }
      }

      //LAB_800f5c38
      y1 += 14;
      sp7c++;
    }

    //LAB_800f5c64
  }

  private static UiBox battleUiItemSpellList;
  private static UiBox battleUiSpellList;
  private static UiBox battleUiItemDescription;

  /** Draws most elements associated with item and dragoon magic menus.
   * This includes:
   *   - Item and Dragoon magic backgrounds, scroll arrows, and text
   *   - Item and Dragoon magic description background and text
   *   - Dragoon magic MP cost background and normal text (excluding the number value) */
  @Method(0x800f5c94L)
  public static void drawItemMenuElements() {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    menu.init();
    menu.transforms.identity();

    if(menu.menuState_00 != 0 && (menu._02 & 0x1) != 0) {
      if((menu._02 & 0x2) != 0) {
        //LAB_800f5ee8
        //Item menu
        final int a2 = menu._10 + 6;
        final int a3 = menu._12 + 17;

        if(battleUiItemSpellList == null) {
          battleUiItemSpellList = new UiBox("Battle UI Item/Spell List", menu.x_04 - a2 / 2, menu.y_06 - a3, a2, a3);
        }

        battleUiItemSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

        renderList(menu.menuType_0a);

        if((menu._02 & 0x8) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          menu.transforms.transfer.set(menu.textX_18 - 16, menu._1a + menu.listScroll_24 * 14 + 2, 124.0f);
          RENDERER.queueOrthoOverlayModel(menu.unknownObj1[menu._84], menu.transforms);

          final int s0;
          if(menu.menuType_0a != 0) {
            s0 = 0;
          } else {
            s0 = 26;
          }

          //LAB_800f5e00
          final int s1;
          if((menu._02 & 0x100) != 0) {
            s1 = 2;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final int t0;
          if((menu._02 & 0x200) != 0) {
            t0 = -2;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(menu.listIndex_1e > 0) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + t0 - 100, 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.upArrow, menu.transforms);
          }

          //LAB_800f5e7c
          if(menu.listIndex_1e + 6 < menu.count_22 - 1) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + s1 - 7, 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.downArrow, menu.transforms);
          }
        }
      }

      //LAB_800f5f50
      if((menu._02 & 0x40) != 0) {
        final int textType;
        if(menu.menuType_0a == 0) { // Item
          //LAB_800f5f8c
          textType = 4;
        } else if(menu.menuType_0a == 1) { // Spell
          //LAB_800f5f94
          textType = 5;
          if((menu._02 & 0x2) != 0) {
            final BattleEntity27c bent = setActiveCharacterSpell(menu.itemOrSpellId_1c);
            addFloatingNumber(0, 1, 0, bent.spell_94.mp_06, 280, 135, 0, 1);

            menu.transforms.transfer.set(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.unknownObj2, menu.transforms);

            if(battleUiSpellList == null) {
              battleUiSpellList = new UiBox("Battle UI Spell List", 236, 130, 64, 14);
            }

            battleUiSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
          }
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        //Selected item description
        if(battleUiItemDescription == null) {
          battleUiItemDescription = new UiBox("Battle UI Item Description", 44, 156, 232, 14);
        }

        battleUiItemDescription.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        renderText(textType, menu.itemOrSpellId_1c, 160, 163);
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public static void clearBattleMenu() {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;
    menu.state_00 = 0;
    menu.highlightState_02 = 0;
    menu.charIndex_04 = 0xff;
    menu.x_06 = 0;
    menu.y_08 = 0;
    menu.xShiftOffset_0a = 0;
    menu.iconCount_0e = 0;
    menu.selectedIcon_22 = 0;
    menu.currentIconStateTick_24 = 0;
    menu.iconStateIndex_26 = 0;
    menu.highlightX0_28 = 0;
    menu.highlightY_2a = 0;
    menu.colour_2c = 0;

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      menu.iconFlags_10[i] = -1;
    }

    //LAB_800f611c
    menu.countHighlightMovementStep_30 = 0;
    menu.highlightMovementDistance_34 = 0;
    menu.currentHighlightMovementStep_38 = 0;
    menu.highlightX1_3c = 0;
    menu.renderSelectedIconText_40 = false;
    menu.cameraPositionSwitchTicksRemaining_44 = 0;
    menu.target_48 = 0;
    menu.displayTargetArrowAndName_4c = false;
    menu.targetType_50 = 0;
    menu.combatantIndex_54 = 0;
  }

  @Method(0x800f6134L)
  public static void initializeCombatMenuIcons(final ScriptState<? extends BattleEntity27c> bentState, final int displayedIconsBitset, final int disabledIconsBitset) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;
    menu.initIcons();

    menu.state_00 = 1;
    menu.highlightState_02 = 2;
    menu.x_06 = 160;
    menu.y_08 = 172;
    menu.selectedIcon_22 = 0;
    menu.currentIconStateTick_24 = 0;
    menu.iconStateIndex_26 = 0;
    menu.highlightX0_28 = 0;
    menu.highlightY_2a = 0;
    menu.colour_2c = 128;

    //LAB_800f61d8
    for(int i = 0; i < 9; i++) {
      menu.iconFlags_10[i] = -1;
    }

    //LAB_800f61f8
    menu.countHighlightMovementStep_30 = 0;
    menu.highlightMovementDistance_34 = 0;
    menu.currentHighlightMovementStep_38 = 0;
    menu.highlightX1_3c = 0;
    menu.renderSelectedIconText_40 = false;
    menu.cameraPositionSwitchTicksRemaining_44 = 0;
    menu.target_48 = 0;
    menu.displayTargetArrowAndName_4c = false;
    menu.targetType_50 = 0;
    menu.combatantIndex_54 = 0;

    //LAB_800f6224
    //LAB_800f6234
    int bentIndex;
    for(bentIndex = 0; bentIndex < charCount_800c677c.get(); bentIndex++) {
      if(battleState_8006e398.charBents_e40[bentIndex] == bentState) {
        break;
      }
    }

    //LAB_800f6254
    menu.iconCount_0e = 0;
    menu.charIndex_04 = (short)battleState_8006e398.charBents_e40[bentIndex].innerStruct_00.charId_272;

    //LAB_800f62a4
    for(int i = 0, used = 0; i < 8; i++) {
      if((displayedIconsBitset & 0x1 << i) != 0) {
        menu.iconFlags_10[used++] = iconFlags_800c7194.get(i).get();
        menu.iconCount_0e++;
      }
      //LAB_800f62d0
    }

    menu.xShiftOffset_0a = (short)((menu.iconCount_0e * 19 - 3) / 2);
    setCombatMenuIconsDisabled(disabledIconsBitset);
  }

  /** Handles the various combat menu actions and then renders the menu:
   * <ol>
   *   <li>0 -> Set up camera positions</li>
   *   <li>1 -> Check for and handle input</li>
   *   <li>2 -> Cycle selector to adjacent icons</li>
   *   <li>3 -> Wrap selector to other end of menu</li>
   *   <li>4 -> Count down camera movement ticks</li>
   * </ol>
   */
  @Method(0x800f6330L)
  public static int handleCombatMenu() {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    if(menu.state_00 == 0) {
      return 0;
    }

    int selectedAction = 0;

    switch(menu.state_00 - 1) {
      case 0 -> {  // Set up camera position list at battle start or camera reset (like dragoon or after trying to run)
        menu.state_00 = 2;
        menu.highlightX0_28 = (short)(menu.x_06 - menu.xShiftOffset_0a + menu.selectedIcon_22 * 19 - 4);
        menu.highlightY_2a = (short)(menu.y_08 - 22);

        //LAB_800f63e8
        menu.countHighlightMovementStep_30 = 0;
        menu.highlightMovementDistance_34 = 0;
        menu.currentHighlightMovementStep_38 = 0;
        menu.highlightX1_3c = 0;
        menu.renderSelectedIconText_40 = false;
        menu.cameraPositionSwitchTicksRemaining_44 = 0;
        menu.target_48 = 0;
        menu.displayTargetArrowAndName_4c = false;
        menu.targetType_50 = 0;
        menu.combatantIndex_54 = 0;

        _800c697c.set((short)0);
        currentCameraPositionIndicesIndicesIndex_800c6ba1.set(0);
        countCameraPositionIndicesIndices_800c6ba0.set(0);

        //LAB_800f6424
        final int[] previousIndicesList = new int[4];
        for(int i = 0; i < 4; i++) {
          previousIndicesList[i] = 0xff;
          cameraPositionIndicesIndices_800c6c30.get(i).set(0);
        }

        //LAB_800f6458
        int cameraPositionIndicesIndex;
        boolean addCameraPositionIndex;
        int cameraPositionIndex;
        for(cameraPositionIndicesIndex = 0; cameraPositionIndicesIndex < 4; cameraPositionIndicesIndex++) {
          addCameraPositionIndex = true;
          cameraPositionIndex = currentStageData_800c6718.get(6 + cameraPositionIndicesIndex);

          //LAB_800f646c
          for(int i = 0; i < 4; i++) { // don't add duplicate indices
            if(previousIndicesList[i] == cameraPositionIndex) {
              addCameraPositionIndex = false;
              break;
            }
            //LAB_800f6480
          }

          if(addCameraPositionIndex) {
            previousIndicesList[countCameraPositionIndicesIndices_800c6ba0.get()] = currentStageData_800c6718.get(6 + cameraPositionIndicesIndex);
            cameraPositionIndicesIndices_800c6c30.get(countCameraPositionIndicesIndices_800c6ba0.get()).set(cameraPositionIndicesIndex);

            if(currentCameraPositionIndicesIndex_800c66b0.get() == cameraPositionIndicesIndex) {
              currentCameraPositionIndicesIndicesIndex_800c6ba1.set(countCameraPositionIndicesIndices_800c6ba0.get());
            }

            //LAB_800f64dc
            countCameraPositionIndicesIndices_800c6ba0.add(1);
          }
          //LAB_800f64ec
        }
      }

      case 1 -> {  // Checking for input
        final int countCameraPositionIndicesIndices = countCameraPositionIndicesIndices_800c6ba0.get();
        menu.renderSelectedIconText_40 = false;
        menu.cameraPositionSwitchTicksRemaining_44 = 0;

        // Input for changing camera angles
        if(countCameraPositionIndicesIndices >= 2 && (input_800bee90.get() & 0x2) != 0) {
          currentCameraPositionIndicesIndicesIndex_800c6ba1.add(1);
          if(currentCameraPositionIndicesIndicesIndex_800c6ba1.get() >= countCameraPositionIndicesIndices) {
            currentCameraPositionIndicesIndicesIndex_800c6ba1.set(0);
          }

          //LAB_800f6560
          _800c6748.set(33);
          menu.state_00 = 5;
          currentCameraPositionIndicesIndex_800c66b0.set(cameraPositionIndicesIndices_800c6c30.get(currentCameraPositionIndicesIndicesIndex_800c6ba1.get()).get());
          menu.cameraPositionSwitchTicksRemaining_44 = 60 / vsyncMode_8007a3b8 + 2;
          toggleBattleMenuSelectorRendering(false);
          break;
        }

        // Input for cycling right on menu bar
        //LAB_800f65b8
        if((input_800bee90.get() & 0x2000) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(menu.selectedIcon_22 < menu.iconCount_0e - 1) {
            //LAB_800f6640
            menu.selectedIcon_22++;
            menu.state_00 = 3;

            //LAB_800f664c
            menu.countHighlightMovementStep_30 = 3;
            menu.highlightMovementDistance_34 = 19;
            menu.currentHighlightMovementStep_38 = 0;
            menu.iconStateIndex_26 = 0;
            break;
          }

          menu.state_00 = 4;
          menu.highlightState_02 |= 1;
          menu.selectedIcon_22 = 0;
          menu.iconStateIndex_26 = 0;
          menu.countHighlightMovementStep_30 = 3;
          menu.highlightMovementDistance_34 = 19;
          menu.currentHighlightMovementStep_38 = 0;
          menu.highlightX1_3c = menu.x_06 - menu.xShiftOffset_0a - 23;
          break;
        }

        // Input for cycling left on menu bar
        //LAB_800f6664
        if((input_800bee90.get() & 0x8000) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(menu.selectedIcon_22 != 0) {
            //LAB_800f66f0
            menu.selectedIcon_22--;
            menu.state_00 = 3;

            //LAB_800f66fc
            menu.countHighlightMovementStep_30 = 3;
            menu.highlightMovementDistance_34 = -19;

            //LAB_800f6710
            menu.currentHighlightMovementStep_38 = 0;
            menu.iconStateIndex_26 = 0;
            break;
          }

          menu.state_00 = 4;
          menu.highlightState_02 |= 1;
          menu.selectedIcon_22 = (short)(menu.iconCount_0e - 1);
          menu.highlightX1_3c = menu.x_06 - menu.xShiftOffset_0a + menu.iconCount_0e * 19 - 4;
          menu.countHighlightMovementStep_30 = 3;
          menu.highlightMovementDistance_34 = -19;
          menu.currentHighlightMovementStep_38 = 0;
          menu.iconStateIndex_26 = 0;
          break;
        }

        // Input for pressing X on menu bar
        //LAB_800f671c
        if((press_800bee94.get() & 0x20) != 0) {
          int selectedIconFlag = menu.iconFlags_10[menu.selectedIcon_22];
          if((selectedIconFlag & 0x80) != 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);
          } else {
            selectedIconFlag = selectedIconFlag & 0xf;
            if(selectedIconFlag == 0x5) {
              prepareItemList();

              if(combatItems_800c6988.isEmpty()) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                selectedAction = menu.iconFlags_10[menu.selectedIcon_22] & 0xf;
              }
              //LAB_800f6790
            } else if(selectedIconFlag == 0x3L) {
              //LAB_800f67b8
              int charSlot;
              for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
                if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == menu.charIndex_04) {
                  break;
                }
              }

              //LAB_800f67d8
              //LAB_800f67f4
              int spellIndex;
              for(spellIndex = 0; spellIndex < 8; spellIndex++) {
                if(dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).get() != -1) {
                  break;
                }
              }

              //LAB_800f681c
              if(spellIndex == 8) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                selectedAction = menu.iconFlags_10[menu.selectedIcon_22] & 0xf;
              }
            } else {
              //LAB_800f6858
              //LAB_800f6860
              playSound(0, 2, 0, 0, (short)0, (short)0);
              selectedAction = menu.iconFlags_10[menu.selectedIcon_22] & 0xf;
            }
          }
          //LAB_800f6898
          // Input for pressing circle on menu bar
        } else if((press_800bee94.get() & 0x40) != 0) {
          //LAB_800f68a4
          //LAB_800f68bc
          playSound(0, 3, 0, 0, (short)0, (short)0);
        }

        //LAB_800f68c4
        //LAB_800f68c8
        menu.renderSelectedIconText_40 = true;
      }

      case 2 -> {  // Cycle to adjacent menu bar icon
        menu.currentHighlightMovementStep_38++;
        menu.highlightX0_28 += (short)(menu.highlightMovementDistance_34 / menu.countHighlightMovementStep_30);

        if(menu.currentHighlightMovementStep_38 >= menu.countHighlightMovementStep_30) {
          menu.state_00 = 2;
          menu.countHighlightMovementStep_30 = 0;
          menu.highlightMovementDistance_34 = 0;
          menu.currentHighlightMovementStep_38 = 0;
          menu.highlightX0_28 = (short)(menu.x_06 - menu.xShiftOffset_0a + menu.selectedIcon_22 * 19 - 4);
          menu.highlightY_2a = (short)(menu.y_08 - 22);
        }
      }

      case 3 -> {  // Wrap menu bar icon
        menu.currentHighlightMovementStep_38++;
        menu.highlightX0_28 += (short)(menu.highlightMovementDistance_34 / menu.countHighlightMovementStep_30);
        menu.highlightX1_3c += menu.highlightMovementDistance_34 / menu.countHighlightMovementStep_30;
        menu.colour_2c += (short)(0x80 / menu.countHighlightMovementStep_30);

        if(menu.currentHighlightMovementStep_38 >= menu.countHighlightMovementStep_30) {
          menu.state_00 = 2;
          menu.colour_2c = 0x80;
          menu.currentHighlightMovementStep_38 = 0;
          menu.highlightMovementDistance_34 = 0;
          menu.countHighlightMovementStep_30 = 0;
          menu.highlightX0_28 = (short)(menu.x_06 - menu.xShiftOffset_0a + menu.selectedIcon_22 * 19 - 4);
          menu.highlightY_2a = (short)(menu.y_08 - 22);
          menu.highlightState_02 &= 0xfffe;
        }
      }

      case 4 -> {  // Seems to be related to switching camera views
        menu.cameraPositionSwitchTicksRemaining_44--;
        if(menu.cameraPositionSwitchTicksRemaining_44 == 1) {
          toggleBattleMenuSelectorRendering(true);
          menu.state_00 = 2;
        }
      }
    }

    //LAB_800f6a88
    //LAB_800f6a8c
    menu.currentIconStateTick_24++;
    if(menu.currentIconStateTick_24 >= 4) {
      menu.currentIconStateTick_24 = 0;
      menu.iconStateIndex_26++;
      if(menu.iconStateIndex_26 >= 4) {
        menu.iconStateIndex_26 = 0;
      }
    }

    //LAB_800f6ae0
    renderCombatActionMenu();

    //LAB_800f6aec
    return selectedAction;
  }

  @Method(0x800f6b04L)
  public static void renderCombatActionMenu() {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    if(menu.state_00 != 0 && (menu.highlightState_02 & 0x2) != 0) {
      //LAB_800f704c
      final int variableW = menu.iconCount_0e * 19 + 1;
      int x = menu.x_06 - variableW / 2;
      int y = menu.y_08 - 10;
      menu.transforms.scaling(variableW, 1.0f, 1.0f);
      menu.transforms.transfer.set(x, y, 124.0f);
      RENDERER.queueOrthoOverlayModel(menu.actionMenuBackground[0], menu.transforms);

      final int[][] battleMenuBaseCoords = new int[4][2];

      battleMenuBaseCoords[0][0] = x;
      battleMenuBaseCoords[2][0] = x;
      x = x + variableW;
      battleMenuBaseCoords[1][0] = x;
      battleMenuBaseCoords[3][0] = x;
      battleMenuBaseCoords[0][1] = y;
      battleMenuBaseCoords[1][1] = y;
      y = menu.y_08 - 8;
      battleMenuBaseCoords[2][1] = y;
      battleMenuBaseCoords[3][1] = y;

      //LAB_800f710c
      for(int i = 0; i < 8; i++) {
        final BattleMenuBackgroundDisplayMetrics0c displayMetrics = battleMenuBackgroundDisplayMetrics_800fb614.get(i);
        x = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00.get()][0] + displayMetrics.vertexXMod_02.get();
        y = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00.get()][1] + displayMetrics.vertexYMod_04.get();

        final int w;
        if(displayMetrics.w_06.get() != 0) {
          w = displayMetrics.w_06.get();
        } else {
          w = variableW;
        }

        //LAB_800f7158
        final int h;
        if(displayMetrics.h_08.get() == 0) {
          h = 2;
        } else {
          h = displayMetrics.h_08.get();
        }

        //LAB_800f716c
        menu.transforms.scaling(w, h, 1.0f);
        menu.transforms.transfer.set(x, y, 124.0f);
        RENDERER.queueOrthoOverlayModel(menu.actionMenuBackground[i + 1], menu.transforms);
      }

      menu.transforms.identity();

      //LAB_800f6fc8
      // Draw red glow underneath selected menu item
      menu.transforms.transfer.set(menu.highlightX0_28, menu.highlightY_2a, 124.0f);
      RENDERER.queueOrthoOverlayModel(menu.highlight, menu.transforms)
        .monochrome(menu.colour_2c / 255.0f);

      if((menu.highlightState_02 & 0x1) != 0) {
        menu.transforms.transfer.set(menu.highlightX1_3c, menu.highlightY_2a, 124.0f);
        RENDERER.queueOrthoOverlayModel(menu.highlight, menu.transforms)
          .monochrome(Math.max(0, (0x80 - menu.colour_2c) / 255.0f));
      }

      //LAB_800f6c48
      for(int iconIndex = 0; iconIndex < menu.iconCount_0e; iconIndex++) {
        final int iconId = (menu.iconFlags_10[iconIndex] & 0xf) - 1;
        final int iconState;
        if(menu.selectedIcon_22 == iconIndex) {
          iconState = battleMenuIconStates_800c71e4.get(menu.iconStateIndex_26).get();
        } else {
          //LAB_800f6c88
          iconState = 0;
        }

        //LAB_800f6c90
        final int menuElementBaseX = menu.x_06 - menu.xShiftOffset_0a + iconIndex * 19;
        final int menuElementBaseY = menu.y_08 - battleMenuIconHeights_800fb6bc.get(iconId).get(iconState).get();

        if(menu.selectedIcon_22 == iconIndex && menu.renderSelectedIconText_40) {
          // Selected combat menu icon text
          menu.transforms.transfer.set(menuElementBaseX, menu.y_08, 124.0f);
          RENDERER.queueOrthoOverlayModel(menu.actionIconTextObj[iconId], menu.transforms);
        }

        // Combat menu icons
        //LAB_800f6d70
        menu.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 124.0f);

        if((menu.iconFlags_10[iconIndex] & 0xf) != 0x2) {
          RENDERER.queueOrthoOverlayModel(menu.actionIconObj[iconId][iconState], menu.transforms);
        } else if(menu.charIndex_04 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          RENDERER.queueOrthoOverlayModel(menu.dragoonIconObj[menu.charIndex_04][iconState], menu.transforms);
        } else {
          RENDERER.queueOrthoOverlayModel(menu.dragoonIconObj[9][iconState], menu.transforms);

          if(iconState != 0) {
            // Divine dragoon spirit overlay
            //LAB_800f6de0
            RENDERER.queueOrthoOverlayModel(menu.divineSpiritOverlay[iconState - 1], menu.transforms);
          }
        }

        if((menu.iconFlags_10[iconIndex] & 0x80) != 0) {
          menu.transforms.transfer.set(menuElementBaseX, menu.y_08 - 16, 124.0f);
          RENDERER.queueOrthoOverlayModel(menu.actionDisabledObj, menu.transforms);
        }

        //LAB_800f6fa4
      }
    }
    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static Obj buildBattleMenuSelectionHighlight(final String name, final BattleMenuHighlightMetrics12 highlightMetrics, final int clutOffset, @Nullable final Translucency transparencyMode, final float colour) {
    //LAB_800f7294
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(colour)
      .pos(highlightMetrics.xBase_00.get(), highlightMetrics.yBase_02.get(), 0.0f)
      .posSize(highlightMetrics.w_04.get(), highlightMetrics.h_06.get());

    final int uvShiftType = highlightMetrics.uvShiftType_10.get();
    if(uvShiftType == 0) {
      //LAB_800f7360
      builder
        .uv(highlightMetrics.u_08.get(), highlightMetrics.v_0a.get())
        .uvSize(highlightMetrics.uvW_0c.get(), highlightMetrics.uvH_0e.get());
    } else if(uvShiftType == 1) {
      //LAB_800f738c
      builder
        .uv(highlightMetrics.u_08.get(), highlightMetrics.v_0a.get() - 1)
        .uvSize(highlightMetrics.uvW_0c.get(), highlightMetrics.uvH_0e.get());
      //LAB_800f7344
    } else if(uvShiftType == 2) {
      //LAB_800f73b8
      builder
        .uv(highlightMetrics.u_08.get() - 1, highlightMetrics.v_0a.get())
        .uvSize(highlightMetrics.uvW_0c.get(), highlightMetrics.uvH_0e.get());
    } else if(uvShiftType == 3) {
      //LAB_800f740c
      builder
        .uv(highlightMetrics.u_08.get() - 1, highlightMetrics.v_0a.get() - 1)
        .uvSize(highlightMetrics.uvW_0c.get(), highlightMetrics.uvH_0e.get());
    }

    //LAB_800f745c
    //LAB_800f7460
    //LAB_800f746c
    final int clutX = 704 + clutOffset & 0x3f0;
    final int clutY = 496 + clutOffset % 16;

    builder
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      builder.translucency(transparencyMode);
    }

    return builder.build();
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static Obj buildBattleMenuBackground(final String name, final BattleMenuBackgroundUvMetrics04 menuBackgroundMetrics, final int x, final int y, final int w, final int h, final int baseClutOffset, @Nullable final Translucency transMode, final short uvShiftType) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, 0, 0, w, h, false);

    // Modified 1 and 3 from retail to properly align bottom row of pixels
    if(uvShiftType == 0) {
      //LAB_800f7628
      builder
        .uv(menuBackgroundMetrics.u_00.get(), menuBackgroundMetrics.v_01.get())
        .uvSize(menuBackgroundMetrics.w_02.get(), menuBackgroundMetrics.h_03.get());
    } else if(uvShiftType == 1) {
      //LAB_800f7654
      builder
        .uv(menuBackgroundMetrics.u_00.get(), menuBackgroundMetrics.v_01.get() + menuBackgroundMetrics.h_03.get())
        .uvSize(menuBackgroundMetrics.w_02.get(), -menuBackgroundMetrics.h_03.get());
      //LAB_800f7610
    } else if(uvShiftType == 2) {
      //LAB_800f7680
      builder
        .uv(menuBackgroundMetrics.u_00.get() + menuBackgroundMetrics.w_02.get() - 1, menuBackgroundMetrics.v_01.get())
        .uvSize(-menuBackgroundMetrics.w_02.get(), menuBackgroundMetrics.h_03.get());
    } else if(uvShiftType == 3) {
      //LAB_800f76d4
      builder
        .uv(menuBackgroundMetrics.u_00.get() + menuBackgroundMetrics.w_02.get() - 1, menuBackgroundMetrics.v_01.get() + menuBackgroundMetrics.h_03.get())
        .uvSize(-menuBackgroundMetrics.w_02.get(), -menuBackgroundMetrics.h_03.get());
    }

    //LAB_800f7724
    //LAB_800f772c
    setGpuPacketClutAndTpageAndQueue(builder, baseClutOffset, transMode);
    return builder.build();
  }

  /**
   * @param targetType 0: chars, 1: monsters, 2: all
   */
  @Method(0x800f7768L)
  public static int FUN_800f7768(final int targetType, final boolean targetAll) {
    final int count;
    short t3 = 1;

    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    if(targetType == 1) {
      menu.displayTargetArrowAndName_4c = true;
      //LAB_800f77d4
      count = aliveMonsterCount_800c6758.get();

      //LAB_800f77e8
      _800c697c.set(_800c697e.get());
    } else {
      menu.displayTargetArrowAndName_4c = true;
      if(targetType == 0) {
        _800c697c.set(_800c6980.get());
        count = charCount_800c677c.get();
      } else {
        //LAB_800f77f0
        count = aliveBentCount_800c669c.get();
      }
    }

    //LAB_800f77f4
    if((press_800bee94.get() & 0x3000) != 0) {
      _800c697c.add((short)1);
      if(_800c697c.get() >= count) {
        _800c697c.set((short)0);
      }
    }

    //LAB_800f7830
    if((press_800bee94.get() & 0xc000) != 0) {
      _800c697c.sub((short)1);
      if(_800c697c.get() < 0) {
        _800c697c.set((short)(count - 1));
      }
      t3 = -1;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(_800c697c.get() < 0 || _800c697c.get() >= count) {
      //LAB_800f78a0
      _800c697c.set((short)0);
      t3 = 1;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    int v1;
    ScriptState<BattleEntity27c> target = null;
    for(v1 = 0; v1 < count; v1++) {
      target = targetBents_800c71f0[targetType][_800c697c.get()];

      if(target != null && (target.storage_44[7] & 0x4000) == 0) {
        break;
      }

      _800c697c.add(t3);

      if(_800c697c.get() >= count) {
        _800c697c.set((short)0);
      }

      //LAB_800f792c
      if(_800c697c.get() < 0) {
        _800c697c.set((short)(count - 1));
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == count) {
      target = targetBents_800c71f0[targetType][_800c697c.get()];
      _800c697c.set((short)0);
    }

    //LAB_800f7998
    menu.targetType_50 = targetType;
    if(!targetAll) {
      menu.combatantIndex_54 = _800c697c.get();
    } else {
      //LAB_800f79b4
      menu.combatantIndex_54 = -1;
    }

    //LAB_800f79bc
    menu.target_48 = target.index;

    if(targetType == 1) {
      //LAB_800f79fc
      _800c697e.set(_800c697c.get());
    } else if(targetType == 0) {
      _800c6980.set(_800c697c.get());
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    int ret = 0;
    if((press_800bee94.get() & 0x20) != 0) { // Cross
      ret = 1;
      _800c697c.set((short)0);
      menu.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a38
    if((press_800bee94.get() & 0x40) != 0) { // Circle
      ret = -1;
      _800c697c.set((short)0);
      menu.target_48 = -1;
      menu.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a68
    return ret;
  }

  @Method(0x800f7a74L)
  public static void setTempItemMagicStats(final BattleEntity27c bent) {
    //LAB_800f7a98
    bent.item_d4 = itemStats_8004f2ac[bent.itemId_52];
    bent._ec = 0;
    bent._ee = 0;
    bent._f0 = 0;
    bent._f2 = 0;
  }

  @Method(0x800f7b68L)
  public static void setTempSpellStats(final BattleEntity27c bent) {
    //LAB_800f7b8c
    if(bent.spellId_4e != -1 && bent.spellId_4e <= 127) {
      bent.spell_94 = EVENTS.postEvent(new SpellStatsEvent(bent.spellId_4e, spellStats_800fa0b8[bent.spellId_4e])).spell;
    } else {
      if(bent.spellId_4e > 127) {
        LOGGER.error("Retail bug: spell index out of bounds (%d). This is known to happen during Shana/Miranda's dragoon attack.", bent.spellId_4e);
      }

      bent.spell_94 = new SpellStats0c();
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int determineAttackSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender, final AttackType attackType) {
    final BattleEntityStat[] statusEffectChances = {BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE}; // onHitStatusChance, statusChance, onHitStatusChance, statusChance, statusChance, onHitStatusChance
    final BattleEntityStat[] statusEffectStats = {BattleEntityStat.EQUIPMENT_ON_HIT_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS}; // onHitStatus, statusType, itemStatus, statusType, statusType, itemStatus
    final BattleEntityStat[] specialEffectStats = {BattleEntityStat.SPECIAL_EFFECT_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET}; // specialEffectFlag, spellFlags, itemTarget, spellFlags, spellFlags, itemTarget
    final int[] specialEffectMasks = {0x40, 0xf0, 0x80, 0xf0, 0xf0, 0x80};

    final boolean isAttackerMonster = attacker instanceof MonsterBattleEntity;

    final int index = (!isAttackerMonster ? 0 : 3) + attackType.ordinal(); //TODO

    final int effectChance = attackType == AttackType.ITEM_MAGIC ? 101 : attacker.getStat(statusEffectChances[index]);

    //LAB_800f7e98
    int effect = -1;
    if(simpleRand() * 101 >> 16 < effectChance) {
      final int statusType = attacker.getStat(statusEffectStats[index]);

      if((statusType & 0xff) != 0) {
        //LAB_800f7eec
        int statusIndex;
        for(statusIndex = 0; statusIndex < 8; statusIndex++) {
          if((statusType & (0x80 >> statusIndex)) != 0) {
            break;
          }
        }

        //LAB_800f7f0c
        effect = 0x80 >> statusIndex;
      }

      //LAB_800f7f14
      final int specialEffects = attacker.getStat(specialEffectStats[index]) & specialEffectMasks[index];
      if(specialEffects != 0) {
        if(
          !isAttackerMonster && attackType != AttackType.DRAGOON_MAGIC_STATUS_ITEMS ||
          specialEffects == 0x80
        ) {
          effect = 0;
        } else if(specialEffects == 0x10) {
          // I think this is vestigial, there are no spells with flag 0x10
          throw new RuntimeException("Flag 0x10 found");
//          if((attacker.spellElement_a4 & (isDefenderMonster ? defender.elementFlag_1c : characterElements_800c706c[defender.charIndex_272].get())) != 0) {
//            effect = 0;
//          }
        }

        //LAB_800f7fc8
        if((defender.specialEffectFlag_14 & 0x80) != 0) { // Resistance
          effect = -1;
        }
      }
    }

    //LAB_800f7fe0
    //LAB_800f7fe4
    return effect;
  }

  @Method(0x800f83c8L)
  public static void prepareItemList() {
    //LAB_800f83dc
    combatItems_800c6988.clear();

    //LAB_800f8420
    for(int itemSlot1 = 0; itemSlot1 < gameState_800babc8.items_2e9.size(); itemSlot1++) {
      final Item item = gameState_800babc8.items_2e9.get(itemSlot1);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : combatItems_800c6988) {
        if(combatItem.item == item) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        combatItems_800c6988.add(new CombatItem02(item));
      }
    }
  }

  @Method(0x800f84c8L)
  public static void loadBattleHudTextures() {
    loadDrgnDir(0, 4113, Bttl_800e::battleHudTexturesLoadedCallback);
  }

  @Method(0x800f8568L)
  public static LodString getTargetEnemyName(final BattleEntity27c target, final LodString targetName) {
    // Seems to be special-case handling to replace Tentacle, since the Melbu fight has more enemies than the engine can handle
    if(target.charId_272 == 0x185) {
      final int stageProgression = battleState_8006e398.stageProgression_eec;
      if(stageProgression == 0 || stageProgression == 4 || stageProgression == 6) {
        return melbuMonsterNames_800c6ba8.get(melbuStageToMonsterNameIndices_800c6f30.get(battleState_8006e398.stageProgression_eec).get());
      }
    }

    return targetName;
  }

  @Method(0x800f863cL)
  public static void FUN_800f863c() {
    loadSupportOverlay(2, Bttl_800e::FUN_800ef28c);
  }

  @Method(0x800f8670L)
  public static void loadMonster(final ScriptState<MonsterBattleEntity> state) {
    loadSupportOverlay(1, () -> Bttl_800e.loadMonster(state));
  }

  @Method(0x800f8854L)
  public static void applyItemSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender) {
    setTempItemMagicStats(attacker);

    final int turnCount = attacker != defender ? 3 : 4;

    if(attacker.item_d4.powerDefence != 0) {
      defender.powerDefence_b8 = attacker.item_d4.powerDefence;
      defender.powerDefenceTurns_b9 = turnCount;
    }

    if(attacker.item_d4.powerMagicDefence != 0) {
      defender.powerMagicDefence_ba = attacker.item_d4.powerMagicDefence;
      defender.powerMagicDefenceTurns_bb = turnCount;
    }

    if(attacker.item_d4.powerAttack != 0) {
      defender.powerAttack_b4 = attacker.item_d4.powerAttack;
      defender.powerAttackTurns_b5 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttack != 0) {
      defender.powerMagicAttack_b6 = attacker.item_d4.powerMagicAttack;
      defender.powerMagicAttackTurns_b7 = turnCount;
    }

    if(attacker.item_d4.powerAttackHit != 0) {
      defender.tempAttackHit_bc = attacker.item_d4.powerAttackHit;
      defender.tempAttackHitTurns_bd = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackHit != 0) {
      defender.tempMagicHit_be = attacker.item_d4.powerMagicAttackHit;
      defender.tempMagicHitTurns_bf = turnCount;
    }

    if(attacker.item_d4.powerAttackAvoid != 0) {
      defender.tempAttackAvoid_c0 = attacker.item_d4.powerAttackAvoid;
      defender.tempAttackAvoidTurns_c1 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackAvoid != 0) {
      defender.tempMagicAvoid_c2 = attacker.item_d4.powerMagicAttackAvoid;
      defender.tempMagicAvoidTurns_c3 = turnCount;
    }

    if(attacker.item_d4.physicalImmunity) {
      defender.tempPhysicalImmunity_c4 = 1;
      defender.tempPhysicalImmunityTurns_c5 = turnCount;
    }

    if(attacker.item_d4.magicalImmunity) {
      defender.tempMagicalImmunity_c6 = 1;
      defender.tempMagicalImmunityTurns_c7 = turnCount;
    }

    if(attacker.item_d4.speedDown != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedDown, turnCount));
    }

    if(attacker.item_d4.speedUp != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedUp, turnCount));
    }

    if(defender instanceof final PlayerBattleEntity playerDefender) {
      if(attacker.item_d4.spPerPhysicalHit != 0) {
        playerDefender.tempSpPerPhysicalHit_cc = attacker.item_d4.spPerPhysicalHit;
        playerDefender.tempSpPerPhysicalHitTurns_cd = turnCount;
      }

      if(attacker.item_d4.mpPerPhysicalHit != 0) {
        playerDefender.tempMpPerPhysicalHit_ce = attacker.item_d4.mpPerPhysicalHit;
        playerDefender.tempMpPerPhysicalHitTurns_cf = turnCount;
      }

      if(attacker.item_d4.spPerMagicalHit != 0) {
        playerDefender.tempSpPerMagicalHit_d0 = attacker.item_d4.spPerMagicalHit;
        playerDefender.tempSpPerMagicalHitTurns_d1 = turnCount;
      }

      if(attacker.item_d4.mpPerMagicalHit != 0) {
        playerDefender.tempMpPerMagicalHit_d2 = attacker.item_d4.mpPerMagicalHit;
        playerDefender.tempMpPerMagicalHitTurns_d3 = turnCount;
      }
    }

    defender.recalculateSpeedAndPerHitStats();
  }

  @Method(0x800f89f4L)
  public static boolean FUN_800f89f4(final int bentIndex, final int a1, final int a2, final int rawDamage, final float x, final float y, final int a6, final int a7) {
    //LAB_800f8a30
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, a1, a2, rawDamage, x, y, a6, a7);
        num.bentIndex_04 = bentIndex;
        return true;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return false;
  }

  @Method(0x800f8aa4L)
  public static void renderDamage(final int bentIndex, final int damage) {
    addFloatingNumberForBent(bentIndex, damage, 8);
  }

  /**
   * @param textType <ol start="0">
   *                   <li>Player names</li>
   *                   <li>Player names</li>
   *                   <li>Combat item names</li>
   *                   <li>Dragoon spells</li>
   *                   <li>Item descriptions</li>
   *                   <li>Spell descriptions</li>
   *                 </ol>
   */
  @Method(0x800f8ac4L)
  public static void renderText(final int textType, final int textIndex, final int x, final int y) {
    final LodString str;
    if(textType == 4) {
      str = new LodString(itemStats_8004f2ac[textIndex].combatDescription);
    } else if(textType == 5) {
      str = new LodString(spellStats_800fa0b8[textIndex].combatDescription);
    } else {
      str = allText_800fb3c0.get(textType).deref().get(textIndex).deref();
    }

    final BattleDescriptionEvent event = EVENTS.postEvent(new BattleDescriptionEvent(textType, textIndex, str));
    Scus94491BpeSegment_8002.renderText(event.string, x - textWidth(event.string) / 2, y - 6, TextColour.WHITE, 0);
  }

  @Method(0x800f8b74L)
  public static void setCombatMenuIconsDisabled(final int disabledIconBitset) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    //LAB_800f8bd8
    for(int i = 0; i < 8; i++) {
      if((disabledIconBitset & 0x1 << i) != 0) {
        //LAB_800f8bf4
        for(int j = 0; j < 8; j++) {
          if((menu.iconFlags_10[j] & 0xf) == iconFlags_800c7194.get(i).get()) {
            menu.iconFlags_10[j] |= 0x80;
            break;
          }
        }
      }
    }
  }

  @Method(0x800f8c38L)
  public static void toggleBattleMenuSelectorRendering(final boolean render) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    if(menu.state_00 != 0) {
      //LAB_800f8c78
      if(!render || menu.cameraPositionSwitchTicksRemaining_44 != 0) {
        //LAB_800f8c64
        menu.highlightState_02 &= 0xfffd;
      } else {
        menu.highlightState_02 |= 0x2;
      }
    }
    //LAB_800f8c98
  }

  @Method(0x800f8cd8L)
  public static Obj buildBattleMenuElement(final String name, final int x, final int y, final int u, final int v, final int w, final int h, final int clut, @Nullable final Translucency transMode) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, u, v, w, h, true);
    setGpuPacketClutAndTpageAndQueue(builder, clut, transMode);

    return builder.build();
  }

  @Method(0x800f8dfcL)
  public static Obj buildUiTextureElement(final String name, final int u, final int v, final int w, final int h, final int clut) {
    final QuadBuilder builder = new QuadBuilder(name)
      .size(w, h)
      .uv(u, v);

    setGpuPacketClutAndTpageAndQueue(builder, clut, null);

    return builder.build();
  }

  @Method(0x800f8facL)
  public static void setGpuPacketParams(final QuadBuilder cmd, final int x, final int y, final int u, final int v, final int w, final int h, final boolean textured) {
    cmd
      .pos(x, y, 0.0f)
      .size(w, h);

    if(textured) {
      cmd
        .uv(u, v);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void setGpuPacketClutAndTpageAndQueue(final QuadBuilder cmd, int clut, @Nullable final Translucency transparencyMode) {
    final int clutIndex;
    if(clut >= 0x80) {
      clutIndex = 1;
      clut -= 0x80;
    } else {
      //LAB_800f9080
      clutIndex = 0;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    final int clutX = battleUiElementClutVramXy_800c7114[clutIndex].x + clut & 0x3f0;
    final int clutY = battleUiElementClutVramXy_800c7114[clutIndex].y + clut % 16;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucency(transparencyMode);
    }
  }

  @Method(0x800f9380L)
  public static void applyBuffOrDebuff(final BattleEntity27c attacker, final BattleEntity27c defender) {
    final BattleEntityStat[] stats = {BattleEntityStat.POWER_DEFENCE, BattleEntityStat.POWER_MAGIC_DEFENCE, BattleEntityStat.POWER_ATTACK, BattleEntityStat.POWER_MAGIC_ATTACK};

    for(int i = 0; i < 8; i++) {
      // This has been intentionally changed to attacker.buffType. Defender.buffType was always set to attacker.buffType anyway.
      if((attacker.spell_94.buffType_0a & (0x80 >> i)) != 0) {
        final int turnCount = attacker.charId_272 != defender.charId_272 ? 3 : 4;
        final int amount = i < 4 ? 50 : -50;

        defender.setStat(stats[i % 4], turnCount << 8 | (amount & 0xff));
      }
    }
  }

  /**
   * @param magicType spell (0), item (1)
   */
  @Method(0x800f946cL)
  public static int applyMagicDamageMultiplier(final BattleEntity27c attacker, final int damage, final int magicType) {
    final int damageMultiplier;
    if(magicType == 0) {
      damageMultiplier = spellStats_800fa0b8[attacker.spellId_4e].damageMultiplier_03;
    } else {
      //LAB_800f949c
      damageMultiplier = attacker.item_d4.damageMultiplier_02;
    }

    if(damageMultiplier == 0x1) {
      //LAB_800f9570
      return damage * 8;
    }

    if(damageMultiplier == 0x2) {
      //LAB_800f9564
      return damage * 6;
    }

    //LAB_800f94d8
    if(damageMultiplier == 0x4) {
      //LAB_800f955c
      return damage * 5;
    }

    //LAB_800f94a0
    if(damageMultiplier == 0x8) {
      //LAB_800f9554
      return damage * 4;
    }

    if(damageMultiplier == 0x10) {
      //LAB_800f954c
      return damage * 3;
    }

    //LAB_800f94ec
    if(damageMultiplier == 0x20) {
      //LAB_800f9544
      return damage * 2;
    }

    //LAB_800f9510
    if(damageMultiplier == 0x40) {
      //LAB_800f9534
      return damage + damage / 2;
    }

    if(damageMultiplier == 0x80) {
      return damage / 2;
    }

    //LAB_800f9578
    //LAB_800f957c
    return damage;
  }

  @ScriptDescription("Checks if a battle entity's physical attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f95d0L)
  public static FlowControl scriptCheckPhysicalHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.PHYSICAL) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's spell or status attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9618L)
  public static FlowControl scriptCheckSpellOrStatusHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.DRAGOON_MAGIC_STATUS_ITEMS) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's item magic attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9660L)
  public static FlowControl scriptCheckItemHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.ITEM_MAGIC) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f96a8L)
  public static FlowControl scriptSetTempSpellStats(final RunningScript<?> script) {
    setTempSpellStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800f96d4L)
  public static FlowControl scriptGetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(Math.round(bent.model_148.coord2_14.coord.transfer.x));
    script.params_20[2].set(Math.round(bent.model_148.coord2_14.coord.transfer.y));
    script.params_20[3].set(Math.round(bent.model_148.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "number", description = "The number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y coordinate")
  @Method(0x800f9730L)
  public static FlowControl scriptAddFloatingNumber(final RunningScript<?> script) {
    //LAB_800f9758
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, 0, 0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), 60 / vsyncMode_8007a3b8 * 5, 0);
        break;
      }

      //LAB_800f97b8
    }

    //LAB_800f97c8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialized the battle menu for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "menuType", description = "0 = items, 1 = spells, 2 = ?")
  @Method(0x800f97d8L)
  public static FlowControl scriptInitSpellAndItemMenu(final RunningScript<?> script) {
    clearSpellAndItemMenu();
    initSpellAndItemMenu(((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).charId_272, (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Render recovery amount for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount recovered")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourIndex", description = "Which colour to use (indices are unknown)")
  @Method(0x800f984cL)
  public static FlowControl scriptRenderRecover(final RunningScript<?> script) {
    addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9884L)
  public static FlowControl scriptSetTempItemMagicStats(final RunningScript<?> script) {
    setTempItemMagicStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a specific (or random) item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID (or -1 to take a random item)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemTaken", description = "The item ID that was taken (or -1 if none could be taken)")
  @Method(0x800f98b0L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    int itemId = script.params_20[0].get();

    if(gameState_800babc8.items_2e9.isEmpty()) {
      script.params_20[1].set(-1);
      return FlowControl.CONTINUE;
    }

    Item item = null;
    if(itemId == -1) {
      item = gameState_800babc8.items_2e9.get((simpleRand() * gameState_800babc8.items_2e9.size()) >> 16);
      itemId = LodMod.idItemMap.getInt(item.getRegistryId());

      //LAB_800f996c
      for(int i = 0; i < 10; i++) {
        if(itemId == protectedItems_800c72cc.get(i).get()) {
          //LAB_800f999c
          item = null;
          itemId = -1;
          break;
        }
      }
    }

    //LAB_800f9988
    //LAB_800f99a4
    if(item != null && takeItemId(item) != 0) {
      itemId = -1;
    }

    //LAB_800f99c0
    script.params_20[1].set(itemId);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a specific item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemGiven", description = "The item ID that was given (or -1 if none could be given)")
  @Method(0x800f99ecL)
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final int givenItem;

    final int itemId = script.params_20[0].get();
    final boolean given;
    if(itemId < 192) {
      given = giveEquipment(REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(itemId)).get());
    } else {
      given = giveItem(REGISTRIES.items.getEntry(LodMod.itemIdMap.get(itemId - 192)).get());
    }

    if(given) {
      givenItem = itemId;
    } else {
      givenItem = -1;
    }

    //LAB_800f9a2c
    script.params_20[1].set(givenItem);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to targeting")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetType", description = "0 = characters, 1 = monsters, 2 = any")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index")
  @Method(0x800f9a50L)
  public static FlowControl FUN_800f9a50(final RunningScript<?> script) {
    final int targetType = script.params_20[0].get();
    final int targetBent = script.params_20[1].get();

    final ScriptState<? extends BattleEntity27c>[] bents;
    final int count;
    if(targetType == 0) {
      bents = battleState_8006e398.charBents_e40;
      count = charCount_800c677c.get();
    } else if(targetType == 1) {
      //LAB_800f9a94
      bents = battleState_8006e398.aliveMonsterBents_ebc;
      count = aliveMonsterCount_800c6758.get();
    } else {
      //LAB_800f9aac
      bents = battleState_8006e398.aliveBents_e78;
      count = aliveBentCount_800c669c.get();
    }

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < count; i++) {
      if(targetBent == bents[i].index) {
        if(targetType == 0) {
          _800c6980.set((short)i);
        } else if(targetType == 1) {
          //LAB_800f9b0c
          _800c697e.set((short)i);
        }

        break;
      }

      //LAB_800f9b14
    }

    //LAB_800f9b24
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if any floating number is on the screen")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "onScreen", description = "True if any floating number is on screen, false otherwise")
  @Method(0x800f9b2cL)
  public static FlowControl scriptIsFloatingNumberOnScreen(final RunningScript<?> script) {
    //LAB_800f9b3c
    int found = 0;
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if(num.state_00 != 0) {
        found = 1;
        break;
      }

      //LAB_800f9b58
    }

    //LAB_800f9b64
    script.params_20[0].set(found);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the active dragoon space element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID whose element should be used")
  @Method(0x800f9b78L)
  public static FlowControl scriptSetDragoonSpaceElementIndex(final RunningScript<?> script) {
    final int characterId = script.params_20[0].get();

    dragoonSpaceElement_800c6b64 = null;

    if(characterId != -1) {
      if(characterId == 9) { //TODO stupid special case handling for DD Dart
        dragoonSpaceElement_800c6b64 = CoreMod.DIVINE_ELEMENT.get();
      } else {
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          if(battleState_8006e398.charBents_e40[i].innerStruct_00.charId_272 == characterId) {
            dragoonSpaceElement_800c6b64 = battleState_8006e398.charBents_e40[i].innerStruct_00.element;
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800f9b94L)
  public static FlowControl FUN_800f9b94(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9bd4L)
  public static FlowControl FUN_800f9bd4(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9c00L)
  public static FlowControl FUN_800f9c00(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  private static UiBox scriptUi;

  @ScriptDescription("Renders the battle HUD background")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The height")
  @Method(0x800f9c2cL)
  public static FlowControl scriptRenderBattleHudBackground(final RunningScript<?> script) {
    final int colourIndex = script.params_20[4].get();
    final int r = textboxColours_800c6fec.get(colourIndex).get(0).get();
    final int g = textboxColours_800c6fec.get(colourIndex).get(1).get();
    final int b = textboxColours_800c6fec.get(colourIndex).get(2).get();

    // This is kinda dumb since we'll have to upload a new box each frame, but there isn't a great
    // way to deal with it. Maybe check to see if any of the params have changed before deleting?

    if(scriptUi != null) {
      scriptUi.delete();
    }

    scriptUi = new UiBox(
      "Scripted Battle UI",
      (short)script.params_20[0].get() - script.params_20[2].get() / 2,
      (short)script.params_20[1].get() - script.params_20[3].get() / 2,
      (short)script.params_20[2].get(),
      (short)script.params_20[3].get()
    );

    scriptUi.render(r / 255.0f, g / 255.0f, b / 255.0f);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, disables menu icons if certain flags are set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "iconIndicesBitset", description = "The icons to disable if their flag matches a certain value (unknown)")
  @Method(0x800f9cacL)
  public static FlowControl FUN_800f9cac(final RunningScript<?> script) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34;
    final int iconIndicesBitset = script.params_20[0].get();

    //LAB_800f9d18
    for(int i = 0; i < 8; i++) {
      if((iconIndicesBitset & 0x1 << i) != 0) {
        //LAB_800f9d34
        for(int icon = 0; icon < 8; icon++) {
          if((menu.iconFlags_10[icon] & 0xf) == iconFlags_800c7194.get(i).get()) {
            menu.iconFlags_10[icon] |= 0x80;
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Called after any battle entity finishes its turn, ticks temporary stats and calls turnFinished on custom battle entities")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9d7cL)
  public static FlowControl scriptFinishBentTurn(final RunningScript<?> script) {
    final int bentIndex = script.params_20[0].get();
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    bent.turnFinished();
    bent.recalculateSpeedAndPerHitStats();
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9e10L)
  public static void clearTempWeaponAndSpellStats(final BattleEntity27c a0) {
    a0.item_d4 = null;
    a0.spell_94 = null;
  }

  @Method(0x800f9e50L)
  public static PlayerBattleEntity setActiveCharacterSpell(final int spellId) {
    final int charIndex = spellAndItemMenu_800c6b60.charIndex_08;

    //LAB_800f9e8c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final ScriptState<PlayerBattleEntity> playerState = battleState_8006e398.charBents_e40[charSlot];
      final PlayerBattleEntity player = playerState.innerStruct_00;

      if(charIndex == player.charId_272) {
        //LAB_800f9ec8
        player.spellId_4e = spellId;
        setTempSpellStats(player);
        return player;
      }
    }

    throw new IllegalStateException();
  }

  @Method(0x800f9ee8L)
  public static void drawLine(final int x1, final int y1, final int x2, final int y2, final int r, final int g, final int b, final boolean translucent) {
    final GpuCommandLine cmd = new GpuCommandLine()
      .rgb(0, r, g, b)
      .rgb(1, r, g, b)
      .pos(0, x1, y1)
      .pos(1, x2, y2);

    if(translucent) {
      cmd.translucent(Translucency.B_PLUS_F);
    }

    GPU.queueCommand(31, cmd);
  }

  @Method(0x800fa068L)
  public static float clampX(final float x) {
    return MathHelper.clamp(x, 20.0f, 300.0f);
  }

  @Method(0x800fa090L)
  public static float clampY(final float y) {
    return MathHelper.clamp(y, 20.0f, 220.0f);
  }
}
