package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.DVECTOR;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ShortRef;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.characters.Element;
import legend.game.characters.TurnBasedPercentileBuff;
import legend.game.characters.VitalsStat;
import legend.game.modding.events.battle.AttackEvent;
import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.bobj.MonsterBattleObject;
import legend.game.combat.bobj.PlayerBattleObject;
import legend.game.combat.types.AttackType;
import legend.game.combat.ui.BattleDisplayStats144;
import legend.game.combat.ui.BattleDisplayStats144Sub10;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.BattleStruct3c;
import legend.game.combat.ui.CombatItem02;
import legend.game.combat.ui.CombatMenua4;
import legend.game.combat.ui.FloatingNumberC4;
import legend.game.combat.ui.FloatingNumberC4Sub20;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleDescriptionEvent;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.LodString;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8005._80050ae8;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c697c;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6ba1;
import static legend.game.combat.Bttl_800c._800c6c40;
import static legend.game.combat.Bttl_800c._800c6f4c;
import static legend.game.combat.Bttl_800c._800c70e0;
import static legend.game.combat.Bttl_800c._800c70f4;
import static legend.game.combat.Bttl_800c._800c7114;
import static legend.game.combat.Bttl_800c._800c7190;
import static legend.game.combat.Bttl_800c._800c7192;
import static legend.game.combat.Bttl_800c._800c7193;
import static legend.game.combat.Bttl_800c._800c7194;
import static legend.game.combat.Bttl_800c._800c71bc;
import static legend.game.combat.Bttl_800c._800c71d0;
import static legend.game.combat.Bttl_800c._800c71e4;
import static legend.game.combat.Bttl_800c._800c71ec;
import static legend.game.combat.Bttl_800c._800c723c;
import static legend.game.combat.Bttl_800c._800d66b0;
import static legend.game.combat.Bttl_800c._800fb5dc;
import static legend.game.combat.Bttl_800c._800fb614;
import static legend.game.combat.Bttl_800c._800fb674;
import static legend.game.combat.Bttl_800c._800fb6bc;
import static legend.game.combat.Bttl_800c._800fb6f4;
import static legend.game.combat.Bttl_800c._800fb72c;
import static legend.game.combat.Bttl_800c.aliveBobjCount_800c669c;
import static legend.game.combat.Bttl_800c.aliveMonsterCount_800c6758;
import static legend.game.combat.Bttl_800c.allText_800fb3c0;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.cameraPositionIndicesIndex_800c6ba0;
import static legend.game.combat.Bttl_800c.cameraPositionIndices_800c6c30;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatItems_800c6988;
import static legend.game.combat.Bttl_800c.combatMenu_800c6b60;
import static legend.game.combat.Bttl_800c.digitOffsetXy_800c7014;
import static legend.game.combat.Bttl_800c.digitU_800c7028;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonSpaceElement_800c6b64;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.intRef_800c6718;
import static legend.game.combat.Bttl_800c.itemTargetAll_800c69c8;
import static legend.game.combat.Bttl_800c.itemTargetType_800c6b68;
import static legend.game.combat.Bttl_800c.melbuMonsterNames_800c6ba8;
import static legend.game.combat.Bttl_800c.melbuStageToMonsterNameIndices_800c6f30;
import static legend.game.combat.Bttl_800c.protectedItems_800c72cc;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;
import static legend.game.combat.Bttl_800c.targetAllItemIds_800c7124;
import static legend.game.combat.Bttl_800c.targetBobjs_800c71f0;
import static legend.game.combat.Bttl_800c.textboxColours_800c6fec;
import static legend.game.combat.Bttl_800e.FUN_800ef8d8;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;

public final class Bttl_800f {
  private Bttl_800f() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bttl_800f.class);

  @Method(0x800f0f5cL)
  public static void FUN_800f0f5c(final GpuCommandPoly parentCommand) {
    //LAB_800f0fe4
    //LAB_800f0fe8
    final int[] sp0x20 = new int[80];
    for(int i = 0; i < sp0x20.length; i++) {
      sp0x20[i] = (int)_800c6f4c.offset(i * 0x2L).get();
    }

    //LAB_800f1014
    final int[] sp0x10 = new int[4];
    final int[] sp0x18 = new int[4];
    int v0 = parentCommand.getX(0) + 1;
    sp0x10[0] = v0;
    sp0x10[2] = v0;
    v0 = parentCommand.getX(1) - 1;
    sp0x10[1] = v0;
    sp0x10[3] = v0;
    v0 = parentCommand.getY(0);
    sp0x18[0] = v0;
    sp0x18[1] = v0;
    v0 = parentCommand.getY(2);
    sp0x18[2] = v0;
    sp0x18[3] = v0;

    //LAB_800f1060
    for(int i = 0; i < 8; i++) {
      final int left;
      final int right;
      final int leftU;
      final int rightU;
      final int top = sp0x18[sp0x20[i * 10]] - sp0x20[i * 10 + 5];
      final int bottom = sp0x18[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 5];
      final int topV = sp0x20[i * 10 + 3];
      final int bottomV = topV + sp0x20[i * 10 + 7];

      if(i == 5 || i == 7) {
        //LAB_800f10ac
        left = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        right = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        rightU = sp0x20[i * 10 + 2];
        leftU = rightU + sp0x20[i * 10 + 6] - 1;
      } else {
        //LAB_800f1128
        left = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        right = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        leftU = sp0x20[i * 10 + 2];
        rightU = leftU + sp0x20[i * 10 + 6];
      }

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .clut(720, 497)
        .vramPos(704, 256)
        .monochrome(0x80)
        .pos(0, left, top)
        .pos(1, right, top)
        .pos(2, left, bottom)
        .pos(3, right, bottom)
        .uv(0, leftU, topV)
        .uv(1, rightU, topV)
        .uv(2, leftU, bottomV)
        .uv(3, rightU, bottomV);

      GPU.queueCommand(31, cmd);
    }
  }

  @Method(0x800f1268L)
  public static void renderTextBoxBackground(final int x, final int y, final int width, final int height, int colour) {
    //LAB_800f1340
    final int left = x - centreScreenX_1f8003dc.get();
    final int top = y - centreScreenY_1f8003de.get();
    final int b = colour & 0xff;
    final int g = colour >> 8 & 0xff;
    final int r = colour >> 16 & 0xff;

    final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 0)
      .rgb(1, r, g, b)
      .rgb(2, r, g, b)
      .monochrome(3, 0)
      .pos(0, left, top)
      .pos(1, left + width, top)
      .pos(2, left, top + height)
      .pos(3, left + width, top + height);

    FUN_800f0f5c(cmd1);

    GPU.queueCommand(31, cmd1);

    final GpuCommandPoly cmd2 = new GpuCommandPoly(4)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0)
      .pos(0, left, top)
      .pos(1, left + width, top)
      .pos(2, left, top + height)
      .pos(3, left + width, top + height);

    GPU.queueCommand(31, cmd2);
  }

  @Method(0x800f1550L)
  public static void renderNumber(final int charSlot, final int a1, int value, final int a3) {
    final int digitCount = MathHelper.digitCount(value);

    //LAB_800f16e4
    final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];

    final short[] digits = new short[digitCount];

    for(int i = 0; i < displayStats._04[a1].length; i++) {
      displayStats._04[a1][i].digitValue_00 = -1;
    }

    //LAB_800f171c
    Arrays.fill(digits, (short)-1);

    int divisor = 1;

    //LAB_800f1768
    for(int i = 0; i < digitCount - 1; i++) {
      divisor *= 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < digitCount; i++) {
      digits[i] = (short)(value / divisor);
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
      final BattleDisplayStats144Sub10 struct = displayStats._04[a1][i];

      if(a1 == 1 || a1 == 3 || a1 == 4) {
        //LAB_800f18f0
        struct.x_02 = digitOffsetXy_800c7014.get(a1 * 2).get() + i * 5;
      } else {
        struct.x_02 = digitOffsetXy_800c7014.get(a1 * 2).get() + (i + rightAlignOffset) * 5;
      }

      //LAB_800f1920
      struct.y_04 = digitOffsetXy_800c7014.get(a1 * 2 + 1).get();
      struct.u_06 = digitU_800c7028.get(digits[i]).get();
      struct.v_08 = 0x20;
      struct.w_0a = 0x8;
      struct.h_0c = 0x8;

      final int v0;
      if(a3 == 1) {
        //LAB_800f1984
        v0 = 0x80;
      } else if(a3 == 2) {
        //LAB_800f198c
        v0 = 0x82;
      } else if(a3 == 3) {
        //LAB_800f1994
        v0 = 0x83;
      } else {
        v0 = 0x2d;
      }

      //LAB_800f1998
      struct._0e = v0;

      //LAB_800f199c
      struct.digitValue_00 = digits[i];
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public static void FUN_800f1a00(final long a0) {
    if(a0 != 1) {
      //LAB_800f1a10
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleStruct3c v1 = _800c6c40.get(i);

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
      final BattleStruct3c v1 = _800c6c40.get(i);
      if(v1.charIndex_00.get() != -1) {
        v1._14.get(2).set(0);
        v1.flags_06.or(0x3);
      }

      //LAB_800f1a90
    }
  }

  @Method(0x800f1aa8L)
  public static boolean checkHit(final int attackerIndex, final int defenderIndex, final AttackType attackType) {
    final BattleObject27c attacker = (BattleObject27c)scriptStatePtrArr_800bc1c0[attackerIndex].innerStruct_00;
    final BattleObject27c defender = (BattleObject27c)scriptStatePtrArr_800bc1c0[defenderIndex].innerStruct_00;
    final boolean isMonster = attacker instanceof MonsterBattleObject;

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
  public static int calculateMagicDamage(final BattleObject27c attacker, final BattleObject27c defender, final int magicType) {
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

      final List<BattleObject27c> targets = new ArrayList<>();
      if((attacker.spell_94.targetType_00 & 0x8) != 0) { // Attack all
        if(attacker instanceof PlayerBattleObject) {
          for(int i = 0; i < charCount_800c677c.get(); i++) {
            targets.add(battleState_8006e398.charBobjs_e40[i].innerStruct_00);
          }
        } else {
          for(int i = 0; i < aliveMonsterCount_800c6758.get(); i++) {
            targets.add(battleState_8006e398.aliveMonsterBobjs_ebc[i].innerStruct_00);
          }
        }
      } else { // Attack single
        targets.add(defender);
      }

      for(final BattleObject27c target : targets) {
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

  @Method(0x800f2500L)
  public static FlowControl scriptPhysicalAttack(final RunningScript<?> script) {
    final BattleObject27c attacker = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleObject27c defender = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    final int damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.PHYSICAL, CoreMod.PHYSICAL_DAMAGE_FORMULA.calculate(attacker, defender))).damage;

    script.params_20[2].set(damage);
    script.params_20[3].set(determineAttackSpecialEffects(attacker, defender, AttackType.PHYSICAL));
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2694L)
  public static FlowControl scriptDragoonMagicStatusItemAttack(final RunningScript<?> script) {
    final BattleObject27c attacker = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleObject27c defender = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

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

  @Method(0x800f2838L)
  public static FlowControl scriptItemMagicAttack(final RunningScript<?> script) {
    final BattleObject27c attacker = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleObject27c defender = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

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

  @Method(0x800f3204L)
  public static void recalculateSpeedAndPerHitStats(final BattleObject27c bobj) {
    if(bobj instanceof final PlayerBattleObject player) {
      final ActiveStatsa0 stats = stats_800be5f8[bobj.charId_272];

      player.spPerPhysicalHit_12a = stats.equipmentSpPerPhysicalHit_4e;
      player.mpPerPhysicalHit_12c = stats.equipmentMpPerPhysicalHit_50;
      player.spPerMagicalHit_12e = stats.equipmentSpPerMagicalHit_52;
      player.mpPerMagicalHit_130 = stats.equipmentMpPerMagicalHit_54;

      if(player.tempSpPerPhysicalHitTurns_cd != 0) {
        player.spPerPhysicalHit_12a += player.tempSpPerPhysicalHit_cc;
      }

      if(player.tempMpPerPhysicalHitTurns_cf != 0) {
        player.mpPerPhysicalHit_12c += player.tempMpPerPhysicalHit_ce;
      }

      if(player.tempSpPerMagicalHitTurns_d1 != 0) {
        player.spPerMagicalHit_12e += player.tempSpPerMagicalHit_d0;
      }

      if(player.tempMpPerMagicalHitTurns_d3 != 0) {
        player.mpPerMagicalHit_130 += player.tempMpPerMagicalHit_d2;
      }
    }
  }

  @Method(0x800f3354L)
  public static void addFloatingNumber(final int numIndex, final long onHitTextType, final long onHitClutCol, final int number, final int x, final int y, int a6, final long onHitClutRow) {
    final FloatingNumberC4 num = floatingNumbers_800c6b5c[numIndex];
    final short[] damageDigits = new short[num.digits_24.length];

    final byte floatingTextType;  // 0=floating numbers, 1=MP cost, 2=miss
    final byte clutCol; //TODO: confirm this, it may not be this exactly
    final byte clutRow; //TODO: confirm this, it may not be this exactly
    if(number != -1) {
      floatingTextType = (byte)onHitTextType;
      clutCol = (byte)onHitClutCol;
      clutRow = (byte)onHitClutRow;
    } else {
      floatingTextType = 2;
      clutCol = 2;
      clutRow = 14;
    }

    //LAB_800f34d4
    num.flags_02 = 0;
    num.bobjIndex_04 = -1;
    num.translucent_08 = false;
    num.b_0c = 0x80;
    num.g_0d = 0x80;
    num.r_0e = 0x80;
    num._18 = -1;
    num._14 = -1;

    //LAB_800f3528
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i]._00 = 0;
      num.digits_24[i]._04 = 0;
      num.digits_24[i]._08 = 0;
      num.digits_24[i].digit_0c = -1;
      num.digits_24[i]._1c = 0;
    }

    num.state_00 = 1;
    if(a6 == 0) {
      num.flags_02 |= 0x1;
    }

    //LAB_800f3588
    num.flags_02 |= 0x8000;
    num._10 = clutCol;

    if(clutCol == 2 && a6 == 0) {
      a6 = 60 / vsyncMode_8007a3b8.get() * 2;
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
    int currDigit;
    for(int i = 0; i < num.digits_24.length; i++) {
      currDigit = damage / currDigitPlace;
      damage = damage % currDigitPlace;
      damageDigits[i] = (short)currDigit;
      currDigitPlace = currDigitPlace / 10;
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
      final FloatingNumberC4Sub20 digitStruct = num.digits_24[digitStructIdx];
      digitStruct._00 = 0x8000;
      digitStruct.y_10 = 0;

      if(clutCol == 2) {
        digitStruct._00 = 0;
        digitStruct._04 = digitStructIdx;
        digitStruct._08 = 0;
      }

      //LAB_800f37d8
      if(floatingTextType == 1) {
        //LAB_800f382c
        digitStruct.x_0e = displayPosX;
        digitStruct.u_12 = digitU_800c7028.get(damageDigits[digitIdx]).get();
        digitStruct.v_14 = 32;
        digitStruct.texW_16 = 8;
        digitStruct.texH_18 = 8;
        displayPosX += 5;
      } else if(floatingTextType == 2) {
        //LAB_800f386c
        digitStruct.x_0e = displayPosX;
        digitStruct.u_12 = 72;
        digitStruct.v_14 = 128;
        digitStruct.texW_16 = 36;
        digitStruct.texH_18 = 16;
        displayPosX += 36;
      } else {
        //LAB_800f37f4
        digitStruct.x_0e = displayPosX;
        digitStruct.u_12 = (int)_800c70e0.offset(damageDigits[digitIdx] * 2).get();
        digitStruct.v_14 = 40;
        digitStruct.texW_16 = 8;
        digitStruct.texH_18 = 16;
        displayPosX += 8;
      }

      //LAB_800f3898
      digitStruct.digit_0c = damageDigits[digitIdx];
      digitStruct._1a = (short)_800c70f4.offset(clutRow * 2).get();
      digitStruct._1c = 0x1000;

      digitIdx++;
    }

    //LAB_800f38e8
    num._14 = digitStructIdx + 12; //TODO: ID duration meaning
    num._18 = a6 + 4; //TODO: ID duration meaning
  }

  @Method(0x800f3940L)
  public static void FUN_800f3940() {
    //LAB_800f3978
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          final int bobjIndex = num.bobjIndex_04;

          if(bobjIndex != -1) {
            final ScriptState<?> state = scriptStatePtrArr_800bc1c0[bobjIndex];
            final BattleObject27c bobj = (BattleObject27c)state.innerStruct_00;

            final short x;
            final short y;
            final short z;
            if(bobj instanceof final MonsterBattleObject monster) {
              x = (short)(-monster.targetArrowPos_78.getZ() * 100);
              y = (short)(-monster.targetArrowPos_78.getY() * 100);
              z = (short)(-monster.targetArrowPos_78.getX() * 100);
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final DVECTOR screenCoords = perspectiveTransformXyz(bobj.model_148, x, y, z);
            num.x_1c = clampX(screenCoords.getX() + centreScreenX_1f8003dc.get());
            num.y_20 = clampY(screenCoords.getY() + centreScreenY_1f8003de.get());
          }

          //LAB_800f3ac8
          final int state = num.state_00;

          if(state == 1) {
            //LAB_800f3b24
            if(num._10 == 0x2) {
              num.state_00 = 2;
            } else {
              //LAB_800f3b44
              num.state_00 = 98;
            }
          } else if(state == 2) {
            //LAB_800f3b50
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberC4Sub20 a1 = num.digits_24[n];

              if(a1.digit_0c == -1) {
                break;
              }

              final int a0 = a1._00;

              if((a0 & 0x1) != 0) {
                if((a0 & 0x2) != 0) {
                  if(a1._08 < 5) {
                    a1.y_10 += a1._08;
                    a1._08++;
                  }
                } else {
                  //LAB_800f3bb0
                  a1._00 |= 0x8002;
                  a1._04 = a1.y_10;
                  a1._08 = -4;
                }
              } else {
                //LAB_800f3bc8
                if(a1._08 == a1._04) {
                  a1._00 |= 0x1;
                }

                //LAB_800f3be0
                a1._08++;
              }
            }

            //LAB_800f3c00
            num._14--;
            if(num._14 <= 0) {
              num.state_00 = 98;
              num._14 = num._18;
            }
          } else if(state == 97) {
            //LAB_800f3c34
            if(num._14 <= 0) {
              num.state_00 = 100;
            } else {
              //LAB_800f3c50
              num._14--;
              // Monochromify
              final int b = num.b_0c;
              final int colour = (b - (num._18 & 0xff)) & 0xff;
              num.b_0c = colour;
              num.g_0d = colour;
              num.r_0e = colour;
            }
          } else if(state == 100) {
            //LAB_800f3d38
            num.state_00 = 0;
            num.flags_02 = 0;
            num.bobjIndex_04 = -1;
            num.translucent_08 = false;
            num.b_0c = 0x80;
            num.g_0d = 0x80;
            num.r_0e = 0x80;
            num._14 = -1;
            num._18 = -1;

            //LAB_800f3d60
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberC4Sub20 v1 = num.digits_24[n];
              v1._00 = 0;
              v1._04 = 0;
              v1._08 = 0;
              v1.digit_0c = -1;
              v1._1c = 0;
            }
            //LAB_800f3b04
          } else if(state < 99) {
            //LAB_800f3c88
            if((num.flags_02 & 0x1) != 0) {
              num.state_00 = 99;
            } else {
              //LAB_800f3ca4
              num._14--;

              if(num._14 <= 0) {
                final int v1 = num._10;

                if(v1 > 0 && v1 < 3) {
                  num.state_00 = 97;
                  num.translucent_08 = true;
                  num.b_0c = 0x60;
                  num.g_0d = 0x60;
                  num.r_0e = 0x60;

                  final int a2 = 60 / vsyncMode_8007a3b8.get() / 2;
                  num._14 = a2;
                  num._18 = 96 / a2;
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
          final boolean translucent = num.translucent_08;
          final int r = num.r_0e;
          final int g = num.g_0d;
          final int b = num.b_0c;

          //LAB_800f3e80
          for(int s7 = 0; s7 < num.digits_24.length; s7++) {
            final FloatingNumberC4Sub20 digit = num.digits_24[s7];

            if(digit.digit_0c == -1) {
              break;
            }

            if((digit._00 & 0x8000) != 0) {
              //LAB_800f3ec0
              for(int s3 = 1; s3 < 3; s3++) {
                final int a1 = num.x_1c - centreScreenX_1f8003dc.get();
                final int a2 = num.y_20 - centreScreenY_1f8003de.get();
                final int left = digit.x_0e + a1;
                final int right = digit.x_0e + digit.texW_16 + a1;
                final int top = digit.y_10 + a2;
                final int bottom = digit.y_10 + digit.texH_18 + a2;
                final int leftU = digit.u_12;
                final int rightU = digit.u_12 + digit.texW_16;
                final int topV = digit.v_14;
                final int bottomV = digit.v_14 + digit.texH_18;
                final int v1 = digit._1a;

                final int t1;
                final int t0;
                if(v1 >= 0x80) {
                  t1 = 1;
                  t0 = v1 - 0x80;
                } else {
                  t1 = 0;

                  //LAB_800f4044
                  t0 = v1;
                }

                final int clutY = (int)_800c7114.offset(2, (t1 * 2 + 1) * 0x4L).get() + t0 % 16;
                final int clutX = (int)_800c7114.offset(2, t1 * 2 * 0x4L).get() + t0 / 16 * 16 & 0x3f0;

                final GpuCommandPoly cmd = new GpuCommandPoly(4)
                  .bpp(Bpp.BITS_4)
                  .clut(clutX, clutY)
                  .vramPos(704, 256)
                  .rgb(r, g, b)
                  .pos(0, left, top)
                  .pos(1, right, top)
                  .pos(2, left, bottom)
                  .pos(3, right, bottom)
                  .uv(0, leftU, topV)
                  .uv(1, rightU, topV)
                  .uv(2, leftU, bottomV)
                  .uv(3, rightU, bottomV);

                if(translucent) {
                  cmd.translucent(Translucency.of(s3));
                }

                //LAB_800f4048
                //LAB_800f4058
                //LAB_800f4068
                GPU.queueCommand(7, cmd);

                if((num.state_00 & 97) == 0) {
                  //LAB_800f4118
                  break;
                }

                //LAB_800f4110
              }
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
      final BattleStruct3c s1 = _800c6c40.get(i);

      if(s1.charIndex_00.get() == -1 && _800be5d0.get() == 1) {
        FUN_800ef8d8(i);
      }

      //LAB_800f41dc
    }

    //LAB_800f41f4
    //LAB_800f41f8
    short x = 63;

    //LAB_800f4220
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
      final BattleStruct3c v1 = _800c6c40.get(charSlot);

      if(v1.charIndex_00.get() != -1) {
        v1.x_08.set(x);
        displayStats.x_00 = x;
      }

      //LAB_800f4238
      x += 94;
    }
  }

  @Method(0x800f4268L)
  public static void addFloatingNumberForBobj(final int bobjIndex, final int damage, final long s4) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[bobjIndex];
    final BattleObject27c bobj = (BattleObject27c)state.innerStruct_00;

    final short x;
    final short y;
    final short z;
    if(bobj instanceof final MonsterBattleObject monster) {
      x = (short)(-monster.targetArrowPos_78.getZ() * 100);
      y = (short)(-monster.targetArrowPos_78.getY() * 100);
      z = (short)(-monster.targetArrowPos_78.getX() * 100);
    } else {
      //LAB_800f4314
      x = 0;
      y = -640;
      z = 0;
    }

    //LAB_800f4320
    final DVECTOR screenCoords = perspectiveTransformXyz(bobj.model_148, x, y, z);

    //LAB_800f4394
    FUN_800f89f4(bobjIndex, 0, 0x2L, damage, clampX(screenCoords.getX() + centreScreenX_1f8003dc.get()), clampY(screenCoords.getY() + centreScreenY_1f8003de.get()), 60 / vsyncMode_8007a3b8.get() / 4, s4);
  }

  @Method(0x800f43dcL)
  public static FlowControl scriptGiveSp(final RunningScript<?> script) {
    //LAB_800f43f8
    //LAB_800f4410
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(battleState_8006e398.charBobjs_e40[charSlot].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f4430
    final PlayerBattleObject player = (PlayerBattleObject)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() + script.params_20[1].get());
    spGained_800bc950.get(charSlot).add(script.params_20[1].get());

    //LAB_800f4500
    script.params_20[2].set(sp.getCurrent());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f4518L)
  public static FlowControl scriptConsumeSp(final RunningScript<?> script) {
    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < charCount_800c677c.get(); i++) {
      if(battleState_8006e398.charBobjs_e40[i].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f456c
    final BattleStruct3c a2 = _800c6c40.get(i);
    a2._0c.set((short)0);
    a2._0e.set((short)script.params_20[1].get());

    final PlayerBattleObject player = (PlayerBattleObject)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() - script.params_20[2].get());

    if(sp.getCurrent() == 0) {
      a2.flags_06.and(0xfff3);
    }

    //LAB_800f45f8
    return FlowControl.CONTINUE;
  }

  @Method(0x800f4600L)
  public static FlowControl FUN_800f4600(final RunningScript<?> script) {
    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();
    int itemOrSpellId = combatMenu.itemOrSpellId_1c.get();
    if(combatMenu.charIndex_08.get() == 8 && combatMenu.menuType_0a.get() == 1) {
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
    script.params_20[0].set(combatMenu._a0.get());
    script.params_20[1].set(battleMenu_800c6c34.deref().target_48.get());
    script.params_20[2].set(itemOrSpellId);

    //LAB_800f4770
    PlayerBattleObject playerBobj = null;
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      playerBobj = battleState_8006e398.charBobjs_e40[i].innerStruct_00;

      if(playerBobj.charId_272 == combatMenu_800c6b60.deref().charIndex_08.get()) {
        break;
      }
    }

    //LAB_800f47ac
    playerBobj.spellId_4e = itemOrSpellId;

    if(combatMenu._a0.get() == 1 && combatMenu.menuType_0a.get() == 0) {
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

  @Method(0x800f480cL)
  public static FlowControl FUN_800f480c(final RunningScript<?> script) {
    BattleObject27c a1 = null;
    final int[] sp0x10 = {0, 0, 1, 0, 2, 1, 1, 1};

    int targetMode = script.params_20[0].get();

    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    //LAB_800f489c
    for(int a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = battleState_8006e398.charBobjs_e40[a0].innerStruct_00;

      if(struct58.charIndex_04.get() == a1.charId_272) {
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
      ret = struct58.target_48.get();
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
  public static void resetCombatMenu() {
    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();
    combatMenu.menuState_00.set((short)0);
    combatMenu._02.set(0);
    combatMenu.x_04.set(0);
    combatMenu.y_06.set(0);
    combatMenu.charIndex_08.set((short)0);
    combatMenu.menuType_0a.set((short)0);
    combatMenu._0c.set(0);
    combatMenu._0e.set(0);
    combatMenu._10.set(0);
    combatMenu._12.set(0);
    combatMenu._14.set(0);
    combatMenu._16.set(0x1000);
    combatMenu.textX_18.set((short)0);
    combatMenu._1a.set((short)0);
    combatMenu.itemOrSpellId_1c.set((short)-1);
    combatMenu.count_22.set((short)0);
    combatMenu.listScroll_24.set((short)0);
  }

  @Method(0x800f49bcL)
  public static void initCombatMenu(final int charIndex, final int menuType) {
    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();
    combatMenu.menuState_00.set((short)1);
    combatMenu.x_04.set(160);
    combatMenu.y_06.set(144);
    combatMenu.charIndex_08.set((short)charIndex);
    combatMenu.menuType_0a.set((short)(menuType & 1));
    combatMenu._0c.set(0x20);
    combatMenu._0e.set(0x2b);
    combatMenu._10.set(0);
    combatMenu._12.set(0);
    combatMenu._14.set(0x1);
    combatMenu._16.set(0x1000);
    combatMenu.textX_18.set((short)0);
    combatMenu._1a.set((short)0);
    combatMenu.itemOrSpellId_1c.set((short)-1);
    combatMenu.listIndex_1e.set((short)0);
    combatMenu._20.set((short)0);

    //LAB_800f4a58
    if(menuType == 0) {
      //LAB_800f4a9c
      prepareItemList();
      combatMenu.count_22.set((short)combatItems_800c6988.size());
    } else if(menuType == 1) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == combatMenu.charIndex_08.get()) {
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
      combatMenu.count_22.set(spellIndex);
    } else if(menuType == 2) {
      //LAB_800f4b4c
      combatMenu.count_22.set((short)0);
    }

    //LAB_800f4b50
    //LAB_800f4b54
    //LAB_800f4b60
    combatMenu._7c.set(0);
    combatMenu._80.set(0);
    combatMenu._84.set(0);
    combatMenu._88.set(0);
    combatMenu._8c.set(0);
    combatMenu._90.set(0);
    combatMenu._94.set(0);
    combatMenu._98.set(0);
    combatMenu._9c.set(0);
    combatMenu._a0.set(0);
  }

  @Method(0x800f4b80L)
  public static void FUN_800f4b80() {
    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();
    if(combatMenu.menuState_00.get() == 0) {
      return;
    }

    int v0;
    final int a1;
    int s0;

    //LAB_800f4bc0
    switch(combatMenu.menuState_00.get()) {
      case 1 -> {
        combatMenu._90.set(0);
        combatMenu._a0.set(0);
        combatMenu._12.set(0);
        combatMenu._10.set(0);

        if(combatMenu.menuType_0a.get() == 0) {
          combatMenu.listScroll_24.set(combatMenu._26.get());
          combatMenu._02.or(0x20);
          combatMenu.listIndex_1e.set(combatMenu._28.get());
          combatMenu._20.set(combatMenu._2a.get());
          combatMenu._94.set(combatMenu._2c.get());

          if(combatMenu.count_22.get() - 1 < combatMenu.listScroll_24.get() + combatMenu.listIndex_1e.get()) {
            combatMenu.listScroll_24.decr();

            if(combatMenu.listScroll_24.get() < 0) {
              combatMenu.listScroll_24.set((short)0);
              combatMenu.listIndex_1e.set((short)0);
              combatMenu._20.set(combatMenu._1a.get());
              combatMenu._94.set(0); // This was a3.1a - a3.1a
            }
          }
        } else {
          //LAB_800f4ca0
          combatMenu.listIndex_1e.set((short)0);
          combatMenu._20.set((short)0);
          combatMenu._94.set(0);
          combatMenu.listScroll_24.set(combatMenu._30.get());
        }

        //LAB_800f4cb4
        combatMenu.itemOrSpellId_1c.set((short)getItemOrSpellId());
        combatMenu.menuState_00.set((short)7);
        combatMenu._02.or(0x40);
      }

      case 2 -> {
        combatMenu._02.and(0xfcff);
        combatMenu.itemOrSpellId_1c.set((short)getItemOrSpellId());

        if((joypadPress_8007a398.get() & 0x4) != 0) { // L1
          if(combatMenu.listScroll_24.get() != 0) {
            combatMenu._88.set(2);
            combatMenu.listScroll_24.set((short)0);
            combatMenu.menuState_00.set((short)5);
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((joypadPress_8007a398.get() & 0x1) != 0) { // L2
          s0 = combatMenu.listScroll_24.get();

          if(combatMenu.count_22.get() - 1 >= combatMenu.listIndex_1e.get() + 6) {
            combatMenu.listScroll_24.set((short)6);
          } else {
            //LAB_800f4d8c
            combatMenu.listScroll_24.set((short)(combatMenu.count_22.get() - (combatMenu.listIndex_1e.get() + 1)));
          }

          //LAB_800f4d90
          combatMenu._88.set(2);
          combatMenu.menuState_00.set((short)5);

          if(s0 != combatMenu.listScroll_24.get()) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4dc4
        if((joypadPress_8007a398.get() & 0x8) != 0) { // R1
          if(combatMenu.listIndex_1e.get() == 0) {
            break;
          }

          if(combatMenu.listIndex_1e.get() < 7) {
            combatMenu.listScroll_24.set((short)0);
            combatMenu.listIndex_1e.set((short)0);
            combatMenu._20.set(combatMenu._1a.get());
          } else {
            //LAB_800f4df4
            combatMenu.listIndex_1e.sub((short)7);
            combatMenu._20.add((short)98);
          }

          //LAB_800f4e00
          combatMenu._88.set(2);
          combatMenu.menuState_00.set((short)5);
          combatMenu._94.set(combatMenu._1a.get() - combatMenu._20.get());
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4e40
        if((joypadPress_8007a398.get() & 0x2) != 0) { // R2
          if(combatMenu.listIndex_1e.get() + 6 >= combatMenu.count_22.get() - 1) {
            break;
          }

          combatMenu.listIndex_1e.add((short)7);
          combatMenu._20.sub((short)98);

          if(combatMenu.listIndex_1e.get() + 6 >= combatMenu.count_22.get() - 1) {
            combatMenu.listScroll_24.set((short)0);
          }

          //LAB_800f4e98
          combatMenu._88.set(2);
          combatMenu.menuState_00.set((short)5);
          combatMenu._94.set(combatMenu._1a.get() - combatMenu._20.get());
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4ecc
        if((joypadInput_8007a39c.get() & 0x1000) != 0) { // Up
          if(combatMenu.listScroll_24.get() != 0) {
            combatMenu.menuState_00.set((short)5);
            combatMenu.listScroll_24.decr();
            combatMenu._88.set(2);
          } else {
            //LAB_800f4f18
            if(combatMenu.listIndex_1e.get() == 0) {
              break;
            }

            combatMenu.menuState_00.set((short)3);
            combatMenu._02.or(0x200);
            combatMenu._80.set(5);
            combatMenu._7c.set(combatMenu._20.get());
            combatMenu._20.add((short)5);
            combatMenu.listIndex_1e.decr();
          }

          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4f74
        if((joypadInput_8007a39c.get() & 0x4000) != 0) { // Down
          if(combatMenu.listScroll_24.get() != combatMenu.count_22.get() - 1) {
            if(combatMenu.listIndex_1e.get() + combatMenu.listScroll_24.get() + 1 < combatMenu.count_22.get()) {
              playSound(0, 1, 0, 0, (short)0, (short)0);

              if(combatMenu.listScroll_24.get() != 6) {
                combatMenu.listScroll_24.incr();
                combatMenu._88.set(2);
                combatMenu.menuState_00.set((short)5);
              } else {
                //LAB_800f4ff8
                combatMenu._80.set(-5);
                combatMenu.menuState_00.set((short)4);
                combatMenu._7c.set(combatMenu._20.get());
                combatMenu._20.sub((short)5);
                combatMenu.listIndex_1e.incr();
                combatMenu._02.or(0x100);
              }
            }
          }

          break;
        }

        //LAB_800f5044
        combatMenu._90.set(0);

        if((joypadPress_8007a398.get() & 0x20) != 0) { // X
          //LAB_800f5078
          PlayerBattleObject player = null;

          for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
            player = battleState_8006e398.charBobjs_e40[charSlot].innerStruct_00;

            if(combatMenu.charIndex_08.get() == player.charId_272) {
              //LAB_800f503c
              _800c6980.setu(charSlot);
              break;
            }
          }

          //LAB_800f50b8
          if(combatMenu.menuType_0a.get() == 0) {
            player.itemId_52 = combatMenu.itemOrSpellId_1c.get();
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
            final PlayerBattleObject caster = setActiveCharacterSpell(combatMenu.itemOrSpellId_1c.get());

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
          combatMenu._8c.set(0);
          combatMenu._02.or(0x4);
          if(combatMenu.menuType_0a.get() == 0) {
            combatMenu._94.set(combatMenu._1a.get() - combatMenu._20.get());
          }

          //LAB_800f51e8
          combatMenu.menuState_00.set((short)6);
          combatMenu._02.and(0xfffd);
          break;
        }

        //LAB_800f5208
        if((joypadPress_8007a398.get() & 0x40) != 0) { // O
          playSound(0, 3, 0, 0, (short)0, (short)0);
          combatMenu.menuState_00.set((short)8);
          combatMenu._02.and(0xfff7);
        }
      }

      case 3 -> {
        s0 = combatMenu._80.get();
        combatMenu._90.incr();
        if(combatMenu._90.get() >= 0x3L) {
          s0 = s0 * 2;
        }

        //LAB_800f5278
        a1 = combatMenu._7c.get() + 14;
        combatMenu._20.add((short)s0);
        if(combatMenu._20.get() >= a1) {
          combatMenu._20.set((short)a1);
          combatMenu.menuState_00.set((short)2);
        }
      }

      case 4 -> {
        s0 = combatMenu._80.get();
        combatMenu._90.incr();
        if(combatMenu._90.get() >= 3) {
          s0 = s0 * 2;
        }

        //LAB_800f52d4
        a1 = combatMenu._7c.get() - 14;
        combatMenu._20.add((short)s0);
        if(combatMenu._20.get() <= a1) {
          //LAB_800f5300
          combatMenu._20.set((short)a1);
          combatMenu.menuState_00.set((short)2);
        }
      }

      case 5 -> {
        s0 = combatMenu._88.get();
        combatMenu._90.incr();
        if(combatMenu._90.get() >= 3) {
          s0 = s0 / 2;
        }

        //LAB_800f5338
        if(s0 <= 1) {
          combatMenu.menuState_00.set((short)2);
        }
      }

      case 6 -> {
        combatMenu._a0.set(0);
        combatMenu.itemOrSpellId_1c.set((short)getItemOrSpellId());
        PlayerBattleObject player;

        //LAB_800f538c
        int charSlot = 0;
        do {
          player = battleState_8006e398.charBobjs_e40[charSlot].innerStruct_00;

          if(combatMenu.charIndex_08.get() == player.charId_272) {
            break;
          }

          charSlot++;
        } while(charSlot < charCount_800c677c.get());

        //LAB_800f53c8
        final int targetType;
        final boolean targetAll;
        if(combatMenu.menuType_0a.get() == 0) { // Items
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
          if(combatMenu.menuType_0a.get() == 0) {
            takeItemId(combatMenu.itemOrSpellId_1c.get() + 192);
          }

          //LAB_800f545c
          if(combatMenu.menuType_0a.get() == 1) {
            final VitalsStat mp = player.stats.getStat(CoreMod.MP_STAT.get());
            mp.setCurrent(mp.getCurrent() - player.spell_94.mp_06);
          }

          //LAB_800f5488
          playSound(0, 2, 0, 0, (short)0, (short)0);
          combatMenu._a0.set(1);
          combatMenu.menuState_00.set((short)9);
        } else if(ret == -1) { // Pressed O
          //LAB_800f54b4
          playSound(0, 0, 3, 0, (short)0, (short)0);
          combatMenu.menuState_00.set((short)7);
          combatMenu._02.and(0xfffb).or(0x20);
        }
      }

      case 7 -> {
        if(combatMenu.menuType_0a.get() != 0) {
          s0 = 0x80;
        } else {
          s0 = 0xba;
        }

        combatMenu.menuState_00.set((short)2);
        playSound(0, 4, 0, 0, (short)0, (short)0);
        combatMenu._12.set(0x52);
        combatMenu._10.set(s0);
        combatMenu.textX_18.set((short)(combatMenu.x_04.get() - s0 / 2 + 9));
        v0 = (combatMenu.y_06.get() - combatMenu._12.get()) - 16;
        combatMenu._1a.set((short)v0);
        combatMenu._20.set((short)v0);
        combatMenu._02.or(0xb);
        if((combatMenu._02.get() & 0x20L) != 0) {
          v0 = v0 - combatMenu._94.get();
          combatMenu._20.set((short)v0);
        }

        //LAB_800f5588
        if(combatMenu.menuType_0a.get() != 0) {
          combatMenu.itemOrSpellId_1c.set((short)getItemOrSpellId());
          addFloatingNumber(0, 0x1L, 0, setActiveCharacterSpell(combatMenu.itemOrSpellId_1c.get()).spell_94.mp_06, 280, 135, 0, 0x1L);
        }
      }

      case 8 -> {
        itemTargetAll_800c69c8.set(false);
        itemTargetType_800c6b68.set(0);
        combatMenu._a0.set(-1);
        combatMenu.menuState_00.set((short)9);
        combatMenu._12.set(0);
        combatMenu._10.set(0);
        combatMenu._02.and(0xfffc);
        final FloatingNumberC4 num = floatingNumbers_800c6b5c[0];
        num.state_00 = 0;
        num.flags_02 = 0;
      }

      case 9 -> {
        if(combatMenu.menuType_0a.get() == 0) {
          v0 = combatMenu._1a.get() - combatMenu._20.get();
          combatMenu._26.set(combatMenu.listScroll_24.get());
          combatMenu._28.set(combatMenu.listIndex_1e.get());
          combatMenu._2a.set(combatMenu._20.get());
          combatMenu._94.set(v0);
          combatMenu._2c.set(v0);
        }

        //LAB_800f568c
        resetCombatMenu();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    combatMenu._84.set(tickCount_800bb0fc.get() & 0x7);

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  public static int getItemOrSpellId() {
    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();
    final short menuType = combatMenu.menuType_0a.get();

    if(menuType == 0) {
      //LAB_800f56f0
      return combatItems_800c6988.get(combatMenu.listScroll_24.get() + combatMenu.listIndex_1e.get()).itemId - 0xc0;
    }

    if(menuType == 1) {
      //LAB_800f5718
      //LAB_800f5740
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == combatMenu.charIndex_08.get()) {
          break;
        }
      }

      //LAB_800f5778
      int spellIndex = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(combatMenu.listScroll_24.get() + combatMenu.listIndex_1e.get()).get();
      if(combatMenu.charIndex_08.get() == 8) {
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

    final CombatMenua4 combatMenu = combatMenu_800c6b60.deref();

    int y1 = combatMenu._20.get();
    final int y2 = combatMenu._1a.get();
    final int sp68 = combatMenu.y_06.get();

    //LAB_800f5860
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == combatMenu.charIndex_08.get()) {
        break;
      }
    }

    //LAB_800f58a4
    int sp7c = 0;

    final LodString sp0x18 = new LodString(18);
    final LodString sp0x40 = new LodString(5);
    final LodString itemCount = new LodString(12);

    //LAB_800f58e0
    for(int spellSlot = 0; spellSlot < combatMenu.count_22.get(); spellSlot++) {
      if(y1 >= sp68) {
        break;
      }

      TextColour textColour = TextColour.WHITE;
      final LodString name;
      if(type == 0) {
        //LAB_800f5918
        name = _80050ae8.get(combatItems_800c6988.get(sp7c).itemId - 0xc0).deref();
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

        if(combatMenu.charIndex_08.get() == 8) {
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
        final PlayerBattleObject bobj = setActiveCharacterSpell(spellId);

        // Not enough MP for spell
        if(bobj.stats.getStat(CoreMod.MP_STAT.get()).getCurrent() < bobj.spell_94.mp_06) {
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
        if((combatMenu._02.get() & 0x4) != 0) {
          trim = (short)combatMenu._8c.get();
        }

        //LAB_800f5bd8
        Scus94491BpeSegment_8002.renderText(name, combatMenu.textX_18.get(), y1, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, combatMenu.textX_18.get() + 128, y1, textColour, trim);
        }
      } else if(y2 < y1 + 12) {
        if((combatMenu._02.get() & 0x4) != 0) {
          trim = combatMenu._8c.get();
        } else {
          trim = y1 - y2;
        }

        //LAB_800f5b40
        Scus94491BpeSegment_8002.renderText(name, combatMenu.textX_18.get(), y2, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, combatMenu.textX_18.get() + 128, y2, textColour, trim);
        }
      }

      //LAB_800f5c38
      y1 += 14;
      sp7c++;
    }

    //LAB_800f5c64
  }

  /** Draws most elements associated with item and dragoon magic menus.
   * This includes:
   *   - Item and Dragoon magic backgrounds, scroll arrows, and text
   *   - Item and Dragoon magic description background and text
   *   - Dragoon magic MP cost background and normal text (excluding the number value) */
  @Method(0x800f5c94L)
  public static void drawItemMenuElements() {
    final CombatMenua4 structa4 = combatMenu_800c6b60.deref();

    if(structa4.menuState_00.get() != 0 && (structa4._02.get() & 0x1L) != 0) {
      if((structa4._02.get() & 0x2L) != 0) {
        renderList(structa4.menuType_0a.get());

        if((structa4._02.get() & 0x8L) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          FUN_800f8cd8(structa4.textX_18.get() - centreScreenX_1f8003dc.get() - 16, structa4._1a.get() - centreScreenY_1f8003de.get() + structa4.listScroll_24.get() * 14 + 2, structa4._84.get() % 4 * 16 + 192 & 0xf0, structa4._84.get() / 4 * 8 + 32 & 0xf8, 15, 8, 0xd, Translucency.B_PLUS_F);

          final int s0;
          if(structa4.menuType_0a.get() != 0) {
            s0 = 0;
          } else {
            s0 = 26;
          }

          //LAB_800f5e00
          final int s1;
          if((structa4._02.get() & 0x100L) != 0) {
            s1 = 2;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final int t0;
          if((structa4._02.get() & 0x200L) != 0) {
            t0 = -2;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(structa4.listIndex_1e.get() > 0) {
            FUN_800f74f4(_800c7190.getAddress(), structa4.x_04.get() + s0 + 56, structa4.y_06.get() + t0 - 100, (int)_800c7192.get(), (int)_800c7193.get(), 0xdL, null, (short)0);
          }

          //LAB_800f5e7c
          if(structa4.listIndex_1e.get() + 6 < structa4.count_22.get() - 1) {
            FUN_800f74f4(_800c7190.getAddress(), structa4.x_04.get() + s0 + 56, structa4.y_06.get() + s1 - 7, (int)_800c7192.get(), (int)_800c7193.get(), 0xdL, null, (short)1);
          }
        }

        //LAB_800f5ee8
        //Item menu
        final int a2 = structa4._10.get() + 6;
        final int a3 = structa4._12.get() + 17;
        renderTextBoxBackground(structa4.x_04.get() - a2 / 2, structa4.y_06.get() - a3, a2, a3, Config.changeBattleRgb() ? Config.getBattleRgb() : 0x00299f);
      }

      //LAB_800f5f50
      if((structa4._02.get() & 0x40) != 0) {
        final int textType;
        if(structa4.menuType_0a.get() == 0) { // Item
          //LAB_800f5f8c
          textType = 4;
        } else if(structa4.menuType_0a.get() == 1) { // Spell
          //LAB_800f5f94
          textType = 5;
          if((structa4._02.get() & 0x2L) != 0) {
            final BattleObject27c bobj = setActiveCharacterSpell(structa4.itemOrSpellId_1c.get());
            addFloatingNumber(0, 0x1L, 0, bobj.spell_94.mp_06, 280, 135, 0, structa4.menuType_0a.get());
            FUN_800f8cd8(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 16, 128, 24, 16, 0x2c, null);
            renderTextBoxBackground(236, 130, 64, 14, Config.changeBattleRgb() ? Config.getBattleRgb() : 0x00299f);
          }
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        //Selected item description
        renderTextBoxBackground(44, 156, 232, 14, Config.changeBattleRgb() ? Config.getBattleRgb() : 0x00299f);
        renderText(textType, structa4.itemOrSpellId_1c.get(), 160, 163);
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public static void FUN_800f60ac() {
    final BattleMenuStruct58 v0 = battleMenu_800c6c34.deref();
    v0._00.set((short)0);
    v0._02.set(0);
    v0.charIndex_04.set((short)0xff);
    v0.x_06.set((short)0);
    v0.y_08.set((short)0);
    v0._0a.set((short)0);
    v0._0c.set((short)0);
    v0.iconCount_0e.set((short)0);
    v0.selectedIcon_22.set((short)0);
    v0._24.set((short)0);
    v0._26.set((short)0);
    v0._28.set((short)0);
    v0._2a.set((short)0);
    v0.colour_2c.set((short)0);

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      v0.iconFlags_10.get(i).set((short)-1);
    }

    //LAB_800f611c
    for(int i = 0; i < 10; i++) {
      v0.all_30.get(i).set(0);
    }
  }

  @Method(0x800f6134L)
  public static void FUN_800f6134(final ScriptState<? extends BattleObject27c> bobjState, final long a1, final long a2) {
    final BattleMenuStruct58 v0 = battleMenu_800c6c34.deref();
    v0._00.set((short)1);
    v0._02.set(2);
    v0.x_06.set((short)160);
    v0.y_08.set((short)172);
    v0.selectedIcon_22.set((short)0);
    v0._24.set((short)0);
    v0._26.set((short)0);
    v0._28.set((short)0);
    v0._2a.set((short)0);
    v0.colour_2c.set((short)128);

    //LAB_800f61d8
    for(int i = 0; i < 9; i++) {
      v0.iconFlags_10.get(i).set((short)-1);
    }

    //LAB_800f61f8
    for(int i = 0; i < 10; i++) {
      v0.all_30.get(i).set(0);
    }

    //LAB_800f6224
    //LAB_800f6234
    int a3;
    for(a3 = 0; a3 < charCount_800c677c.get(); a3++) {
      if(battleState_8006e398.charBobjs_e40[a3] == bobjState) {
        break;
      }
    }

    //LAB_800f6254
    v0.iconCount_0e.set((short)0);
    v0.charIndex_04.set((short)battleState_8006e398.charBobjs_e40[a3].innerStruct_00.charId_272);

    //LAB_800f62a4
    for(int i = 0, used = 0; i < 8; i++) {
      if((a1 & 1 << i) != 0) {
        v0.iconFlags_10.get(used++).set(_800c7194.get(i).get());
        v0.iconCount_0e.incr();
      }

      //LAB_800f62d0
    }

    v0._0c.set((short)0);
    v0._0a.set((short)((v0.iconCount_0e.get() * 19 - 3) / 2));
    FUN_800f8b74(a2);
  }

  @Method(0x800f6330L)
  public static int FUN_800f6330() {
    long v1;
    long a0;
    long a1;
    int cameraPositionIndicesIndex;
    long s1;
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    if(struct58._00.get() == 0) {
      return 0;
    }

    s1 = 0;

    switch(struct58._00.get() - 1) {
      case 0 -> {  // Set up camera position list at battle start or camera reset (like dragoon or after trying to run)
        struct58._00.set((short)2);
        struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
        struct58._2a.set((short)(struct58.y_08.get() - 22));

        //LAB_800f63e8
        for(int i = 0; i < 10; i++) {
          struct58.all_30.get(i).set(0);
        }

        _800c697c.setu(0);
        _800c6ba1.setu(0);
        cameraPositionIndicesIndex_800c6ba0.setu(0);

        //LAB_800f6424
        final long[] sp0x18 = new long[4];
        for(int i = 0; i < 4; i++) {
          sp0x18[i] = 0xffL;
          cameraPositionIndices_800c6c30.get(i).set(0);
        }

        //LAB_800f6458
        for(cameraPositionIndicesIndex = 0; cameraPositionIndicesIndex < 4; cameraPositionIndicesIndex++) {
          a0 = 0;
          a1 = intRef_800c6718.get(6 + cameraPositionIndicesIndex).get();

          //LAB_800f646c
          for(int i = 0; i < 4; i++) {
            if(sp0x18[i] == a1) {
              a0 = 0x1L;
              break;
            }

            //LAB_800f6480
          }

          if(a0 == 0) {
            sp0x18[(int)cameraPositionIndicesIndex_800c6ba0.get()] = intRef_800c6718.get(6 + cameraPositionIndicesIndex).get();
            cameraPositionIndices_800c6c30.get((int)cameraPositionIndicesIndex_800c6ba0.get()).set(cameraPositionIndicesIndex);

            if(_800d66b0.get() == cameraPositionIndicesIndex) {
              _800c6ba1.setu(cameraPositionIndicesIndex_800c6ba0.get());
            }

            //LAB_800f64dc
            cameraPositionIndicesIndex_800c6ba0.addu(0x1L);
          }

          //LAB_800f64ec
        }
      }

      case 1 -> {  // Checking for input
        a0 = cameraPositionIndicesIndex_800c6ba0.get();
        struct58._40.set(0);
        struct58._44.set(0);

        // Input for changing camera angles
        if(a0 >= 0x2L && (joypadInput_8007a39c.get() & 0x2L) != 0) {
          _800c6ba1.addu(0x1L);
          if(_800c6ba1.get() >= a0) {
            _800c6ba1.setu(0);
          }

          //LAB_800f6560
          _800c6748.set(0x21);
          struct58._00.set((short)5);
          _800c66b0.set(cameraPositionIndices_800c6c30.get((int)_800c6ba1.get()).get());
          struct58._44.set(60 / vsyncMode_8007a3b8.get() + 2);
          FUN_800f8c38(0);
          break;
        }

        // Input for cycling right on menu bar
        //LAB_800f65b8
        if((joypadInput_8007a39c.get() & 0x2000L) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(struct58.selectedIcon_22.get() < struct58.iconCount_0e.get() - 1) {
            //LAB_800f6640
            struct58.selectedIcon_22.incr();
            struct58._00.set((short)3);

            //LAB_800f664c
            struct58._30.set(3);
            struct58._34.set(19);
            struct58._38.set(0);
            struct58._26.set((short)0);
            break;
          }

          struct58._00.set((short)4);
          struct58._02.or(1);
          struct58.selectedIcon_22.set((short)0);
          struct58._26.set((short)0);
          struct58._30.set(3);
          struct58._34.set(19);
          struct58._38.set(0);
          struct58._3c.set(struct58.x_06.get() - struct58._0a.get() - 23);
          break;
        }

        // Input for cycling left on menu bar
        //LAB_800f6664
        if((joypadInput_8007a39c.get() & 0x8000L) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(struct58.selectedIcon_22.get() != 0) {
            //LAB_800f66f0
            struct58.selectedIcon_22.decr();
            struct58._00.set((short)3);

            //LAB_800f66fc
            struct58._30.set(3);
            struct58._34.set(-19);

            //LAB_800f6710
            struct58._38.set(0);
            struct58._26.set((short)0);
            break;
          }

          struct58._00.set((short)4);
          struct58._02.or(1);
          struct58.selectedIcon_22.set((short)(struct58.iconCount_0e.get() - 1));
          struct58._3c.set(struct58.x_06.get() - struct58._0a.get() + struct58.iconCount_0e.get() * 19 - 4);
          struct58._30.set(3);
          struct58._34.set(-19);
          struct58._38.set(0);
          struct58._26.set((short)0);
          break;
        }

        // Input for pressing X on menu bar
        //LAB_800f671c
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          v1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get();
          if((v1 & 0x80L) != 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);
          } else {
            v1 = v1 & 0xfL;
            if(v1 == 0x5L) {
              prepareItemList();

              if(combatItems_800c6988.isEmpty()) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              }
              //LAB_800f6790
            } else if(v1 == 0x3L) {
              //LAB_800f67b8
              int charSlot;
              for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
                if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == struct58.charIndex_04.get()) {
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
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              }
            } else {
              //LAB_800f6858
              //LAB_800f6860
              playSound(0, 2, 0, 0, (short)0, (short)0);
              s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
            }
          }
          //LAB_800f6898
          // Input for pressing circle on menu bar
        } else if((joypadPress_8007a398.get() & 0x40L) != 0) {
          //LAB_800f68a4
          //LAB_800f68bc
          playSound(0, 3, 0, 0, (short)0, (short)0);
        }

        //LAB_800f68c4
        //LAB_800f68c8
        struct58._40.set(0x1L);
      }

      case 2 -> {  // Cycle to adjacent menu bar icon
        struct58._38.incr();
        struct58._28.add((short)(struct58._34.get() / struct58._30.get()));

        if(struct58._38.get() >= struct58._30.get()) {
          struct58._00.set((short)2);
          struct58._30.set(0);
          struct58._34.set(0);
          struct58._38.set(0);
          struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
          struct58._2a.set((short)(struct58.y_08.get() - 22));
        }
      }

      case 3 -> {  // Wrap menu bar icon
        struct58._38.incr();
        struct58._28.add((short)(struct58._34.get() / struct58._30.get()));
        struct58._3c.add(struct58._34.get() / struct58._30.get());
        struct58.colour_2c.add((short)(0x80 / struct58._30.get()));

        if(struct58._38.get() >= struct58._30.get()) {
          struct58._00.set((short)2);
          struct58.colour_2c.set((short)0x80);
          struct58._38.set(0);
          struct58._34.set(0);
          struct58._30.set(0);
          struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
          struct58._2a.set((short)(struct58.y_08.get() - 22));
          struct58._02.and(0xfffe);
        }
      }

      case 4 -> {  // Seems to be related to switching camera views
        struct58._44.decr();
        if(struct58._44.get() == 1) {
          FUN_800f8c38(0x1L);
          struct58._00.set((short)2);
        }
      }
    }

    //LAB_800f6a88
    //LAB_800f6a8c
    struct58._24.incr();
    if(struct58._24.get() >= 4) {
      struct58._24.set((short)0);
      struct58._26.incr();
      if(struct58._26.get() >= 4) {
        struct58._26.set((short)0);
      }
    }

    //LAB_800f6ae0
    FUN_800f6b04();

    //LAB_800f6aec
    return (int)s1;
  }

  @Method(0x800f6b04L)
  public static void FUN_800f6b04() {
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s1;
    final long s2;
    int s3;
    int s4;
    long s6;
    final long s7;
    long fp;

    final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
    if(menu._00.get() != 0 && (menu._02.get() & 0x2L) != 0) {
      //LAB_800f6c48
      for(int iconIndex = 0; iconIndex < menu.iconCount_0e.get(); iconIndex++) {
        fp = (menu.iconFlags_10.get(iconIndex).get() & 0xfL) - 0x1L;
        if(menu.selectedIcon_22.get() == iconIndex) {
          s6 = _800c71e4.get(menu._26.get()).get();
        } else {
          //LAB_800f6c88
          s6 = 0;
        }

        //LAB_800f6c90
        s3 = menu.x_06.get() - menu._0a.get() + iconIndex * 19 - centreScreenX_1f8003dc.get();
        s4 = menu.y_08.get() - (int)_800fb6bc.offset(2, fp * 6 + s6 * 2).get() - centreScreenY_1f8003de.get();
        if((menu.iconFlags_10.get(iconIndex).get() & 0x80L) != 0) {
          // "X" icon over attack icon if attack is disabled
          FUN_800f8cd8(s3, menu.y_08.get() - (centreScreenY_1f8003de.get() + 16), 96, 112, 16, 16, 0x19, null);
        }

        //LAB_800f6d70
        if((menu.iconFlags_10.get(iconIndex).get() & 0xfL) != 0x2L) {
          //LAB_800f6e24
          s0 = _800fb674.offset(fp * 0x8L).offset(2, 0x4L).get();
        } else if(menu.charIndex_04.get() == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
          s0 = _800c71d0.get(9).get();
          if(s6 != 0) {
            //LAB_800f6de0
            FUN_800f8cd8(s3 + 4, s4, s6 != 1 ? 88 : 80, 112, 8, 16, 0x98, Translucency.B_PLUS_F);
          }
        } else {
          s0 = _800c71d0.get(menu.charIndex_04.get()).get();
        }

        //LAB_800f6e34
        //LAB_800f6e38
        //LAB_800f6e3c
        t1 = _800fb674.offset(fp * 8).getAddress();
        v1 = s6 * 0x2L + fp * 0x6L;
        t0 = _800fb6f4.offset(v1).getAddress();
        v1 = _800fb6bc.offset(v1).getAddress();
        // Combat menu icons
        FUN_800f8cd8(s3, s4, (int)MEMORY.ref(1, t1).offset(0x0L).get(), (int)(MEMORY.ref(1, t1).offset(0x2L).get() + MEMORY.ref(1, t0).offset(0x0L).get()) & 0xff, 16, (int)MEMORY.ref(2, v1).getSigned(), (int)s0, Translucency.of((int)MEMORY.ref(2, t1).offset(0x6L).getSigned()));

        if(menu.selectedIcon_22.get() == iconIndex && menu._40.get() == 0x1L) {
          t1 = _800fb72c.offset(fp * 8).getAddress();
          // Selected combat menu icon text
          FUN_800f8cd8(menu.x_06.get() - menu._0a.get() + iconIndex * 19 - centreScreenX_1f8003dc.get() - (int)MEMORY.ref(2, t1).offset(0x4L).get() / 2 + 8, menu.y_08.get() - centreScreenY_1f8003de.get() - 24, (int)MEMORY.ref(1, t1).offset(0x0L).get(), (int)MEMORY.ref(1, t1).offset(0x2L).get(), (int)MEMORY.ref(2, t1).offset(0x4L).get(), 8, (int)MEMORY.ref(2, t1).offset(0x6L).getSigned(), null);
        }

        //LAB_800f6fa4
      }

      //LAB_800f6fc8
      // Draw red glow underneath selected menu item
      FUN_800f7210(menu._28.get(), menu._2a.get(), _800c71bc, 31, 0xc, Translucency.B_PLUS_F, menu.colour_2c.get());

      if((menu._02.get() & 0x1L) != 0) {
        FUN_800f7210(menu._3c.get(), menu._2a.get(), _800c71bc, 31, 0xc, Translucency.B_PLUS_F, Math.max(0, 0x80 - menu.colour_2c.get()));
      }

      //LAB_800f704c
      s0 = menu.iconCount_0e.get() * 19 + 1;
      s1 = menu.x_06.get() - s0 / 2;
      s2 = menu.y_08.get() - 10;
      FUN_800f74f4(_800fb5dc.getAddress(), (int)s1, (int)s2, (int)s0, 2, 0x2bL, Translucency.B_PLUS_F, (short)_800fb5dc.offset(1, 0x4L).get());

      final long[] sp0x20 = new long[4];
      final long[] sp0x28 = new long[4];

      sp0x20[0] = s1;
      sp0x20[2] = s1;
      s1 = s1 + s0;
      s3 = menu.y_08.get() - 8;
      sp0x20[1] = s1;
      sp0x20[3] = s1;
      sp0x28[0] = s2;
      sp0x28[1] = s2;
      sp0x28[2] = s3;
      sp0x28[3] = s3;

      //LAB_800f710c
      s7 = _800fb5dc.getAddress();
      fp = _800fb5dc.getAddress() + 0x6L;
      s6 = _800fb614.getAddress();
      for(int i = 0; i < 8; i++) {
        t0 = s6 + i * 0xcL;
        a2 = MEMORY.ref(2, t0).offset(0x6L).getSigned();
        a1 = sp0x20[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x2L).get();
        t2 = sp0x28[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x4L).get();
        if(a2 != 0) {
          a3 = a2;
        } else {
          a3 = s0;
        }

        //LAB_800f7158
        if(MEMORY.ref(2, t0).offset(0x8L).getSigned() == 0) {
          v1 = 0x2L;
        } else {
          v1 = MEMORY.ref(2, t0).offset(0x8L).get();
        }

        //LAB_800f716c
        FUN_800f74f4(fp + i * 0x6L, (short)a1, (short)t2, (short)a3, (short)v1, 0x2bL, Translucency.B_PLUS_F, (short)MEMORY.ref(1, s7).offset((i + 0x1L) * 0x6L).offset(0x4L).get());
      }
    }

    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static void FUN_800f7210(final int x, final int y, final ArrayRef<ShortRef> a2, final int z, final int a4, @Nullable final Translucency transparencyMode, final int colour) {
    //LAB_800f7294
    final int left = a2.get(0).get() + x - centreScreenX_1f8003dc.get();
    final int right = left + a2.get(2).get();
    final int top = a2.get(1).get() + y - centreScreenY_1f8003de.get();
    final int bottom = top + a2.get(3).get();

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .monochrome(colour)
      .pos(0, left, top)
      .pos(1, right, top)
      .pos(2, left, bottom)
      .pos(3, right, bottom);

    final int leftU = a2.get(4).get();
    final int rightU = leftU + a2.get(6).get();
    final int topV = a2.get(5).get();
    final int bottomV = topV + a2.get(7).get();

    final int v1 = a2.get(8).get();
    if(v1 == 0) {
      //LAB_800f7360
      cmd
        .uv(0, leftU, topV)
        .uv(1, rightU, topV)
        .uv(2, leftU, bottomV)
        .uv(3, rightU, bottomV);
    } else if(v1 == 1) {
      //LAB_800f738c
      cmd
        .uv(0, leftU, topV - 1)
        .uv(1, rightU, topV - 1)
        .uv(2, leftU, bottomV - 1)
        .uv(3, rightU, bottomV - 1);
      //LAB_800f7344
    } else if(v1 == 2) {
      //LAB_800f73b8
      cmd
        .uv(0, leftU - 1, topV)
        .uv(1, rightU - 1, topV)
        .uv(2, leftU - 1, bottomV)
        .uv(3, rightU - 1, bottomV);
    } else if(v1 == 3) {
      //LAB_800f740c
      cmd
        .uv(0, leftU - 1, topV - 1)
        .uv(1, rightU - 1, topV - 1)
        .uv(2, leftU - 1, bottomV - 1)
        .uv(3, rightU - 1, bottomV - 1);
    }

    //LAB_800f745c
    //LAB_800f7460
    //LAB_800f746c
    final int clutX = 704 + a4 / 16 * 16 & 0x3f0;
    final int clutY = 496 + a4 % 16;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucent(transparencyMode);
    }

    GPU.queueCommand(z, cmd);
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static void FUN_800f74f4(final long a0, final int x, final int y, final int w, final int h, final long a5, @Nullable final Translucency transMode, final short a7) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .monochrome(0x80);

    setGpuPacketParams(cmd, x - centreScreenX_1f8003dc.get(), y - centreScreenY_1f8003de.get(), 0, 0, w, h, false);

    final int leftU = (int)MEMORY.ref(1, a0).offset(0x0L).get();
    final int rightU = leftU + (int)MEMORY.ref(1, a0).offset(0x2L).get();
    final int topV = (int)MEMORY.ref(1, a0).offset(0x1L).get();
    final int bottomV = topV + (int)MEMORY.ref(1, a0).offset(0x3L).get();

    if(a7 == 0) {
      //LAB_800f7628
      cmd
        .uv(0, leftU, topV)
        .uv(1, rightU, topV)
        .uv(2, leftU, bottomV)
        .uv(3, rightU, bottomV);
    } else if(a7 == 1) {
      //LAB_800f7654
      cmd
        .uv(0, leftU, bottomV - 1)
        .uv(1, rightU, bottomV - 1)
        .uv(2, leftU, topV - 1)
        .uv(3, rightU, topV - 1);
      //LAB_800f7610
    } else if(a7 == 2) {
      //LAB_800f7680
      cmd
        .uv(0, rightU - 1, topV)
        .uv(1, leftU - 1, topV)
        .uv(2, rightU - 1, bottomV)
        .uv(3, leftU - 1, bottomV);
    } else if(a7 == 3) {
      //LAB_800f76d4
      cmd
        .uv(0, rightU - 1, bottomV - 1)
        .uv(1, leftU - 1, bottomV - 1)
        .uv(2, rightU - 1, topV - 1)
        .uv(3, leftU - 1, topV - 1);
    }

    //LAB_800f7724
    //LAB_800f772c
    setGpuPacketClutAndTpageAndQueue(cmd, (short)a5, transMode);
  }

  /**
   * @param targetType 0: chars, 1: monsters, 2: all
   */
  @Method(0x800f7768L)
  public static int FUN_800f7768(final int targetType, final boolean targetAll) {
    final int count;
    long t3 = 0x1L;

    final BattleMenuStruct58 battleMenu = battleMenu_800c6c34.deref();

    if(targetType == 1) {
      battleMenu._4c.set(0x1L);
      //LAB_800f77d4
      count = aliveMonsterCount_800c6758.get();

      //LAB_800f77e8
      _800c697c.setu(_800c697e.get());
    } else {
      battleMenu._4c.set(0x1L);
      if(targetType == 0) {
        _800c697c.setu(_800c6980.get());
        count = charCount_800c677c.get();
      } else {
        //LAB_800f77f0
        count = aliveBobjCount_800c669c.get();
      }
    }

    //LAB_800f77f4
    if((joypadPress_8007a398.get() & 0x3000) != 0) {
      _800c697c.addu(0x1L);
      if(_800c697c.getSigned() >= count) {
        _800c697c.setu(0);
      }
    }

    //LAB_800f7830
    if((joypadPress_8007a398.get() & 0xc000L) != 0) {
      _800c697c.subu(0x1L);
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(count - 1);
      }
      t3 = -0x1L;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(_800c697c.getSigned() < 0 || _800c697c.getSigned() >= count) {
      //LAB_800f78a0
      _800c697c.setu(0);
      t3 = 0x1L;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    int v1;
    ScriptState<BattleObject27c> target = null;
    for(v1 = 0; v1 < count; v1++) {
      target = targetBobjs_800c71f0[targetType][(int)_800c697c.getSigned()];

      if(target != null && (target.storage_44[7] & 0x4000) == 0) {
        break;
      }

      _800c697c.addu(t3);

      if(_800c697c.get() >= count) {
        _800c697c.setu(0);
      }

      //LAB_800f792c
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(count - 1);
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == count) {
      target = targetBobjs_800c71f0[targetType][(int)_800c697c.getSigned()];
      _800c697c.setu(0);
    }

    //LAB_800f7998
    battleMenu.targetType_50.set(targetType);
    if(!targetAll) {
      battleMenu.combatantIndex.set((int)_800c697c.getSigned());
    } else {
      //LAB_800f79b4
      battleMenu.combatantIndex.set(-1);
    }

    //LAB_800f79bc
    battleMenu.target_48.set(target.index);

    if(targetType == 1) {
      //LAB_800f79fc
      _800c697e.setu(_800c697c.get());
    } else if(targetType == 0) {
      _800c6980.setu(_800c697c.get());
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    int ret = 0;
    if((joypadPress_8007a398.get() & 0x20) != 0) { // Cross
      ret = 1;
      _800c697c.setu(0);
      battleMenu._4c.set(0);
    }

    //LAB_800f7a38
    if((joypadPress_8007a398.get() & 0x40L) != 0) { // Circle
      ret = -1;
      _800c697c.setu(0);
      battleMenu.target_48.set(-1);
      battleMenu._4c.set(0);
    }

    //LAB_800f7a68
    return ret;
  }

  @Method(0x800f7a74L)
  public static void setTempItemMagicStats(final BattleObject27c bobj) {
    //LAB_800f7a98
    bobj.item_d4 = itemStats_8004f2ac[bobj.itemId_52];
    bobj._ec = 0;
    bobj._ee = 0;
    bobj._f0 = 0;
    bobj._f2 = 0;
  }

  @Method(0x800f7b68L)
  public static void setTempSpellStats(final BattleObject27c bobj) {
    //LAB_800f7b8c
    if(bobj.spellId_4e != -1 && bobj.spellId_4e <= 127) {
      bobj.spell_94 = EVENTS.postEvent(new SpellStatsEvent(bobj.spellId_4e, spellStats_800fa0b8[bobj.spellId_4e])).spell;
    } else {
      if(bobj.spellId_4e > 127) {
        LOGGER.error("Retail bug: spell index out of bounds (%d). This is known to happen during Shana/Miranda's dragoon attack.", bobj.spellId_4e);
      }

      bobj.spell_94 = null;
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int determineAttackSpecialEffects(final BattleObject27c attacker, final BattleObject27c defender, final AttackType attackType) {
    final int[] statusEffectChances = {32, 79, 32, 79, 79, 32}; // onHitStatusChance, statusChance, onHitStatusChance, statusChance, statusChance, onHitStatusChance
    final int[] statusEffectStats = {35, 81, 112, 81, 81, 112}; // onHitStatus, statusType, itemStatus, statusType, statusType, itemStatus
    final int[] specialEffectStats = {8, 73, 104, 73, 73, 104}; // specialEffectFlag, spellFlags, itemTarget, spellFlags, spellFlags, itemTarget
    final int[] specialEffectMasks = {0x40, 0xf0, 0x80, 0xf0, 0xf0, 0x80};

    final boolean isAttackerMonster = attacker instanceof MonsterBattleObject;

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
      final int itemId1 = gameState_800babc8.items_2e9.getInt(itemSlot1);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : combatItems_800c6988) {
        if(combatItem.itemId == itemId1) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        combatItems_800c6988.add(new CombatItem02(itemId1));
      }
    }
  }

  @Method(0x800f84c0L)
  public static void FUN_800f84c0() {
    // empty
  }

  @Method(0x800f84c8L)
  public static void loadBattleHudTextures() {
    loadDrgnDir(0, 4113, Bttl_800e::battleHudTexturesLoadedCallback);
  }

  @Method(0x800f8568L)
  public static LodString getTargetEnemyName(final BattleObject27c target, final LodString targetName) {
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
  public static void loadMonster(final ScriptState<MonsterBattleObject> state) {
    loadSupportOverlay(1, () -> Bttl_800e.loadMonster(state));
  }

  @Method(0x800f8854L)
  public static void applyItemSpecialEffects(final BattleObject27c attacker, final BattleObject27c defender) {
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

    if(defender instanceof final PlayerBattleObject playerDefender) {
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

    recalculateSpeedAndPerHitStats(defender);
  }

  @Method(0x800f89f4L)
  public static long FUN_800f89f4(final int bobjIndex, final long a1, final long a2, final int rawDamage, final int x, final int y, final int a6, final long a7) {
    //LAB_800f8a30
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, a1, a2, rawDamage, x, y, a6, a7);
        num.bobjIndex_04 = bobjIndex;
        return 0x1L;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return 0;
  }

  @Method(0x800f8aa4L)
  public static void renderDamage(final int bobjIndex, final int damage) {
    addFloatingNumberForBobj(bobjIndex, damage, 0x8L);
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
  public static void FUN_800f8b74(final long a0) {
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    //LAB_800f8bd8
    for(int t1 = 0; t1 < 8; t1++) {
      if((a0 & 0x1L << t1) != 0) {
        //LAB_800f8bf4
        for(int a3 = 0; a3 < 8; a3++) {
          if((struct58.iconFlags_10.get(a3).get() & 0xfL) == _800c7194.get(t1).get()) {
            struct58.iconFlags_10.get(a3).or(0x80);
            break;
          }
        }
      }
    }
  }

  @Method(0x800f8c38L)
  public static void FUN_800f8c38(final long a0) {
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    if(struct58._00.get() != 0) {
      //LAB_800f8c78
      if(a0 != 0x1L || struct58._44.get() != 0) {
        //LAB_800f8c64
        struct58._02.and(0xfffd);
      } else {
        struct58._02.or(0x2);
      }
    }

    //LAB_800f8c98
  }

  @Method(0x800f8ca0L)
  public static int getFirstSetBitIndex(final int bitset) {
    //LAB_800f8cac
    int bitIndex = -1;
    for(int i = 0; i < 32; i++) {
      if((bitset & 1 << i) != 0) {
        bitIndex = i;
        break;
      }

      //LAB_800f8cc0
    }

    //LAB_800f8cd0
    return bitIndex;
  }

  @Method(0x800f8cd8L)
  public static void FUN_800f8cd8(final int x, final int y, final int u, final int v, final int w, final int h, final int a6, @Nullable final Translucency transMode) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .monochrome(0x80);

    setGpuPacketParams(cmd, x, y, u, v, w, h, true);
    setGpuPacketClutAndTpageAndQueue(cmd, a6, transMode);
  }

  @Method(0x800f8dfcL)
  public static void drawUiTextureElement(final int x, final int y, final int u, final int v, final int w, final int h, final int a6, final int a7, final int a8) {
    final long t3 = _800c71ec.getAddress();

    final byte[] sp0x20 = {
      (byte)MEMORY.ref(1, t3).offset(0x0L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x1L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x2L).getSigned(),
    };

    final GpuCommandPoly cmd = new GpuCommandPoly(4);

    if(a8 < 6) {
      cmd.monochrome((byte)(sp0x20[a7] + 0x80) / 6 * a8 - 0x80 & 0xff);
    } else {
      //LAB_800f8ef4
      cmd.monochrome(sp0x20[a7] & 0xff);
    }

    setGpuPacketParams(cmd, x, y, u, v, w, h, true);
    setGpuPacketClutAndTpageAndQueue(cmd, a6, null);
  }

  @Method(0x800f8facL)
  public static void setGpuPacketParams(final GpuCommandPoly cmd, final int x, final int y, final int u, final int v, final int w, final int h, final boolean textured) {
    cmd
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h);

    if(textured) {
      cmd
        .uv(0, u, v)
        .uv(1, u + w, v)
        .uv(2, u, v + h)
        .uv(3, u + w, v + h);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void setGpuPacketClutAndTpageAndQueue(final GpuCommandPoly cmd, final int a1, @Nullable final Translucency transparencyMode) {
    final int t0;
    final int t1;
    if(a1 >= 0x80) {
      t1 = 1;
      t0 = a1 - 0x80;
    } else {
      //LAB_800f9080
      t1 = 0;
      t0 = a1;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    final int clutY = (int)_800c7114.offset(2, t1 * 0x8L + 0x4L).get() + t0 % 16;
    final int clutX = (int)_800c7114.offset(2, t1 * 0x8L).get() + t0 / 16 * 16 & 0x3f0;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucent(transparencyMode);
    }

    GPU.queueCommand(31, cmd);
  }

  @Method(0x800f9380L)
  public static void applyBuffOrDebuff(final BattleObject27c attacker, final BattleObject27c defender) {
    for(int i = 0; i < 8; i++) {
      // This has been intentionally changed to attacker.buffType. Defender.buffType was always set to attacker.buffType anyway.
      if((attacker.spell_94.buffType_0a & (0x80 >> i)) != 0) {
        final int turnCount = attacker.charId_272 != defender.charId_272 ? 3 : 4;
        final int amount = i < 4 ? 50 : -50;

        defender.setStat(_800c723c.get(i % 4).get(), turnCount << 8 | (amount & 0xff));
      }
    }
  }

  /**
   * @param magicType spell (0), item (1)
   */
  @Method(0x800f946cL)
  public static int applyMagicDamageMultiplier(final BattleObject27c attacker, final int damage, final int magicType) {
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

  @Method(0x800f9584L)
  public static void FUN_800f9584() {
    final long v0 = _800c6b6c.get();
    MEMORY.ref(2, v0).offset(0x00L).setu(0);
    MEMORY.ref(2, v0).offset(0x02L).setu(0);
    MEMORY.ref(2, v0).offset(0x04L).setu(0);
    MEMORY.ref(2, v0).offset(0x06L).setu(0);
    MEMORY.ref(2, v0).offset(0x08L).setu(0);
    MEMORY.ref(2, v0).offset(0x0aL).setu(0);
    MEMORY.ref(2, v0).offset(0x0cL).setu(0);
    MEMORY.ref(2, v0).offset(0x0eL).setu(0);
    MEMORY.ref(2, v0).offset(0x10L).setu(0);

    //LAB_800f95b8
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, v0).offset(0x14L).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f95d0L)
  public static FlowControl scriptCheckPhysicalHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.PHYSICAL) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9618L)
  public static FlowControl scriptCheckSpellOrStatusHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.DRAGOON_MAGIC_STATUS_ITEMS) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9660L)
  public static FlowControl scriptCheckItemHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.ITEM_MAGIC) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f96a8L)
  public static FlowControl scriptSetTempSpellStats(final RunningScript<?> script) {
    setTempSpellStats((BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f96d4L)
  public static FlowControl scriptGetBobjPos(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.model_148.coord2_14.coord.transfer.getX());
    script.params_20[2].set(bobj.model_148.coord2_14.coord.transfer.getY());
    script.params_20[3].set(bobj.model_148.coord2_14.coord.transfer.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9730L)
  public static FlowControl scriptAddFloatingNumber(final RunningScript<?> script) {
    //LAB_800f9758
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, 0, 0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), 60 / vsyncMode_8007a3b8.get() * 5, 0);
        break;
      }

      //LAB_800f97b8
    }

    //LAB_800f97c8
    return FlowControl.CONTINUE;
  }

  @Method(0x800f97d8L)
  public static FlowControl scriptInitCombatMenu(final RunningScript<?> script) {
    resetCombatMenu();
    initCombatMenu(((BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).charId_272, (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f984cL)
  public static FlowControl scriptRenderRecover(final RunningScript<?> script) {
    addFloatingNumberForBobj(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9884L)
  public static FlowControl scriptSetTempItemMagicStats(final RunningScript<?> script) {
    setTempItemMagicStats((BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  /** If param 0 is -1 then a random item is taken */
  @Method(0x800f98b0L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    int itemId = script.params_20[0].get();

    if(gameState_800babc8.items_2e9.isEmpty()) {
      script.params_20[1].set(-1);
      return FlowControl.CONTINUE;
    }

    if(itemId == -1) {
      itemId = gameState_800babc8.items_2e9.getInt((simpleRand() * gameState_800babc8.items_2e9.size()) >> 16);

      //LAB_800f996c
      for(int i = 0; i < 10; i++) {
        if(itemId == protectedItems_800c72cc.get(i).get()) {
          //LAB_800f999c
          itemId = -1;
          break;
        }
      }
    }

    //LAB_800f9988
    //LAB_800f99a4
    if(itemId != -1 && takeItemId(itemId) != 0) {
      itemId = -1;
    }

    //LAB_800f99c0
    script.params_20[1].set(itemId);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f99ecL)
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final int s1;
    if(giveItem(script.params_20[0].get()) == 0) {
      s1 = script.params_20[0].get();
    } else {
      s1 = -1;
    }

    //LAB_800f9a2c
    script.params_20[1].set(s1);
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9a50L)
  public static FlowControl FUN_800f9a50(final RunningScript<?> script) {
    final int targetType = script.params_20[0].get();
    final int targetBobj = script.params_20[1].get();

    final ScriptState<? extends BattleObject27c>[] bobjs;
    final int count;
    if(targetType == 0) {
      bobjs = battleState_8006e398.charBobjs_e40;
      count = charCount_800c677c.get();
    } else if(targetType == 1) {
      //LAB_800f9a94
      bobjs = battleState_8006e398.aliveMonsterBobjs_ebc;
      count = aliveMonsterCount_800c6758.get();
    } else {
      //LAB_800f9aac
      bobjs = battleState_8006e398.aliveBobjs_e78;
      count = aliveBobjCount_800c669c.get();
    }

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < count; i++) {
      if(targetBobj == bobjs[i].index) {
        if(targetType == 0) {
          _800c6980.setu(i);
        } else if(targetType == 1) {
          //LAB_800f9b0c
          _800c697e.setu(i);
        }

        break;
      }

      //LAB_800f9b14
    }

    //LAB_800f9b24
    return FlowControl.CONTINUE;
  }

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

  @Method(0x800f9b78L)
  public static FlowControl scriptSetDragoonSpaceElementIndex(final RunningScript<?> script) {
    final int characterId = script.params_20[0].get();

    dragoonSpaceElement_800c6b64 = null;

    if(characterId != -1) {
      if(characterId == 9) { //TODO stupid special case handling for DD Dart
        dragoonSpaceElement_800c6b64 = CoreMod.DIVINE_ELEMENT.get();
      } else {
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          if(battleState_8006e398.charBobjs_e40[i].innerStruct_00.charId_272 == characterId) {
            dragoonSpaceElement_800c6b64 = battleState_8006e398.charBobjs_e40[i].innerStruct_00.element;
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x800f9b94L)
  public static FlowControl FUN_800f9b94(final RunningScript<?> script) {
    final long v1 = _800c6b6c.get();
    MEMORY.ref(2, v1).offset(0x0L).setu(0x1L);
    MEMORY.ref(2, v1).offset(0x6L).setu(script.params_20[0].get());
    MEMORY.ref(2, v1).offset(0x8L).setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9bd4L)
  public static FlowControl FUN_800f9bd4(final RunningScript<?> script) {
    final long v1 = _800c6b6c.get();
    MEMORY.ref(2, v1).offset(0x0L).setu(0x4L);
    MEMORY.ref(2, v1).offset(0x8L).setu(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9c00L)
  public static FlowControl FUN_800f9c00(final RunningScript<?> script) {
    FUN_800fa018(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9c2cL)
  public static FlowControl FUN_800f9c2c(final RunningScript<?> script) {
    final int colourIndex = script.params_20[4].get();
    final int r = textboxColours_800c6fec.get(colourIndex).get(0).get();
    final int g = textboxColours_800c6fec.get(colourIndex).get(1).get();
    final int b = textboxColours_800c6fec.get(colourIndex).get(2).get();
    final int colour = b << 16 | g << 8 | b;

    renderTextBoxBackground(
      (short)script.params_20[0].get() - script.params_20[2].get() / 2,
      (short)script.params_20[1].get() - script.params_20[3].get() / 2,
      (short)script.params_20[2].get(),
      (short)script.params_20[3].get(),
      colour
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x800f9cacL)
  public static FlowControl FUN_800f9cac(final RunningScript<?> script) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
    final int t2 = script.params_20[0].get();

    //LAB_800f9d18
    for(int t0 = 0; t0 < 8; t0++) {
      if((t2 & 1 << t0) != 0) {
        //LAB_800f9d34
        for(int icon = 0; icon < 8; icon++) {
          if((menu.iconFlags_10.get(icon).get() & 0xf) == _800c7194.get(t0).get()) {
            menu.iconFlags_10.get(icon).or(0x80);
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  /** Called after any bobj finishes its turn */
  @Method(0x800f9d7cL)
  public static FlowControl scriptFinishBobjTurn(final RunningScript<?> script) {
    final int bobjIndex = script.params_20[0].get();
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[bobjIndex].innerStruct_00;

    // Temporary power stats
    for(int statIndex = 88; statIndex <= 97; statIndex++) {
      scriptTickTemporaryStatMod(bobj, statIndex);
    }

    if(bobj instanceof PlayerBattleObject) {
      // Temp MP/SP per hit stats
      for(int statIndex = 100; statIndex <= 103; statIndex++) {
        scriptTickTemporaryStatMod(bobj, statIndex);
      }
    }

    bobj.turnFinished();

    recalculateSpeedAndPerHitStats(bobj);
    return FlowControl.CONTINUE;
  }

  private static void scriptTickTemporaryStatMod(final BattleObject27c bobj, final int statIndex) {
    if(bobj.getStat(statIndex) != 0) {
      if((bobj.getStat(statIndex) & 0xff00) < 0x200) { // Turns is stored in upper byte
        bobj.setStat(statIndex, 0);
      } else {
        bobj.setStat(statIndex, bobj.getStat(statIndex) - 0x100); // Subtract one turn
      }
    }
  }

  @Method(0x800f9e10L)
  public static void clearTempWeaponAndSpellStats(final BattleObject27c a0) {
    a0.item_d4 = null;
    a0.spell_94 = null;
  }

  @Method(0x800f9e50L)
  public static PlayerBattleObject setActiveCharacterSpell(final int spellId) {
    final int charIndex = combatMenu_800c6b60.deref().charIndex_08.get();

    //LAB_800f9e8c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final ScriptState<PlayerBattleObject> playerState = battleState_8006e398.charBobjs_e40[charSlot];
      final PlayerBattleObject player = playerState.innerStruct_00;

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

  @Method(0x800fa018L)
  public static void FUN_800fa018(final long a0) {
    final long v1 = _800c6b6c.get();

    if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != 0) {
      if(a0 == 0x1L) {
        MEMORY.ref(2, v1).offset(0x2L).oru(0x1L);
      } else {
        //LAB_800fa050
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffeL);
      }
    }

    //LAB_800fa060
  }

  @Method(0x800fa068L)
  public static int clampX(final int x) {
    return MathHelper.clamp(x, 20, 300);
  }

  @Method(0x800fa090L)
  public static int clampY(final int y) {
    return MathHelper.clamp(y, 20, 220);
  }
}
