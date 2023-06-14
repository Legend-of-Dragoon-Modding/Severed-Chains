package legend.game.combat.bobj;

import legend.core.gte.SVECTOR;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;

import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.combat.Bttl_800f.renderDamage;

public class MonsterBattleObject extends BattleObject27c {
  public int originalHp_5c;
  /** Unused */
  public int originalMp_5e;
  public int originalAttack_60;
  public int originalMagicAttack_62;
  public int originalSpeed_64;
  public int originalDefence_66;
  public int originalMagicDefence_68;
  public int originalAttackAvoid_6a;
  public int originalMagicAvoid_6c;
  /**
   * <ul>
   *   <li>0x1 - special enemy - takes one damage from magical attacks</li>
   *   <li>0x2 - special enemy - takes one damage from physical attacks</li>
   *   <li>0x4 - magical immunity</li>
   *   <li>0x8 - physical immunity</li>
   * </ul>
   */
  public int damageReductionFlags_6e;
  public int _70;
  public int monsterElementFlag_72;
  public int monsterElementalImmunityFlag_74;
  public int monsterStatusResistFlag_76;
  public final SVECTOR targetArrowPos_78 = new SVECTOR();

  public MonsterBattleObject(final String name) {
    super(name);
  }

  @Override
  public void turnStart() {
    super.turnStart();
//    this.getState().storage_44[7] &= ~0x8;

    final ScriptState<MonsterBattleObject> state = (ScriptState<MonsterBattleObject>)this.getState();

    // sub 0x1c40
    _8006e398.monsterMoveId_570 = 0x20;
    _8006e398._3d0 = 0;

    this.guard_54 = 0;

/*
    final SpecialEffects20 effects = _8006e398.specialEffect_00[this.combatControllerAllBobjSlot_274];

    if((effects.shieldsSigStoneCharmTurns_1c & 0x3) != 0) {
      effects.shieldsSigStoneCharmTurns_1c--;
    }

    if((effects.shieldsSigStoneCharmTurns_1c & 0xc) != 0) {
      effects.shieldsSigStoneCharmTurns_1c -= 0x4;
    }

    state.storage_44[8] = 0; // able to act

    // sub 6694
    final Status04 status = _8006e398.status_384[this.combatControllerAllBobjSlot_274];
    if((status.statusEffect_00 & 0x80) != 0) {
      status.statusEffect_00 |= 0x40;
    }
    // ret

    if((state.storage_44[7] & 0x40) != 0) { // dead
      state.storage_44[8] = 1; // unable to act
    } else {
      // sub 1d78
      state.storage_44[10] = this.combatControllerAllBobjSlot_274;

      if((effects.shieldsSigStoneCharmTurns_1c & 0x30) != 0) {
        effects.shieldsSigStoneCharmTurns_1c -= 0x10;
        this.model_148.animationState_9c = 2;

        // sub 21c8
        final ScriptState<EffectManagerData6c> childScript = allocateEffectManager("Empty EffectManager child, custom monster %s".formatted(this), state, 0, (forkedState, effectManagerData6c) -> {
          if(forkedState.storage_44[10] != 0) {
            forkedState.storage_44[10]--;
            return;
          }

          final Param textboxIndex = new IntParam(0);
          this.callScriptFunc(Scus94491BpeSegment_8002::scriptGetFreeTextboxIndex, textboxIndex);
          this.callScriptFunc(Scus94491BpeSegment_8002::FUN_800254bc, textboxIndex, new IntParam(0x1121), new IntParam(0xa0), new IntParam(0x32), new IntParam(0x1c), new IntParam(0x1), new StringParam(new LodString("Cannot Move")));

          if(forkedState.storage_44[9] != 0) {
            forkedState.storage_44[9]--;
            return;
          }

          this.callScriptFunc(Scus94491BpeSegment_8002::scriptDeallocateTextbox, textboxIndex);
          forkedState.deallocate();
        }, null, null, null);
        childScript.storage_44[8] = 14; // text index for textbox (cannot move)
        childScript.storage_44[9] = 30; // frames to wait before hiding textbox
        childScript.loadScriptFile(state.scriptPtr_14, 0);
        childScript.scriptPtr_14 = state.scriptPtr_14;
        childScript.offset_18 = 0x2204;
        // ret

        //TODO wait 30 frames
        this.model_148.animationState_9c = 1;
        state.storage_44[8] = 1; // unable to act
      }
      // ret

      //TODO implement from 001d48 on
    }
    // ret
*/
    final ScriptState<PlayerBattleObject> targetState = _8006e398.aliveCharBobjs_eac[1];
    final PlayerBattleObject target = targetState.innerStruct_00;
    target.hp_08 -= 10;
    renderDamage(targetState.index, 999999999);

    if(target.hp_08 <= 0) {
      target.hp_08 = 0;
      targetState.storage_44[7] |= 0x40;
    }

//    this.getState().storage_44[7] &= ~0x8;
  }

  private <T> T callScriptFunc(final Function<RunningScript<? extends BattleObject27c>, T> func, final Param... params) {
    final RunningScript<? extends BattleObject27c> script = new RunningScript<>(this.getState());
    System.arraycopy(params, 0, script.params_20, 0, params.length);
    return func.apply(script);
  }

  @Override
  public int getStat(final int statIndex) {
    return switch(statIndex) {
      case 44 -> this.originalHp_5c;
      case 45 -> this.originalMp_5e;
      case 46 -> this.originalAttack_60;
      case 47 -> this.originalMagicAttack_62;
      case 48 -> this.originalSpeed_64;
      case 49 -> this.originalDefence_66;
      case 50 -> this.originalMagicDefence_68;
      case 51 -> this.originalAttackAvoid_6a;
      case 52 -> this.originalMagicAvoid_6c;
      case 53 -> this.damageReductionFlags_6e;
      case 54 -> this._70;
      case 55 -> this.monsterElementFlag_72;
      case 56 -> this.monsterElementalImmunityFlag_74;
      case 57 -> this.monsterStatusResistFlag_76;
      case 58 -> this.targetArrowPos_78.getX();
      case 59 -> this.targetArrowPos_78.getY();
      case 60 -> this.targetArrowPos_78.getZ();

      default -> super.getStat(statIndex);
    };
  }

  @Override
  public void setStat(final int statIndex, final int value) {
    switch(statIndex) {
      case 44 -> this.originalHp_5c = value;
      case 45 -> this.originalMp_5e = value;
      case 46 -> this.originalAttack_60 = value;
      case 47 -> this.originalMagicAttack_62 = value;
      case 48 -> this.originalSpeed_64 = value;
      case 49 -> this.originalDefence_66 = value;
      case 50 -> this.originalMagicDefence_68 = value;
      case 51 -> this.originalAttackAvoid_6a = value;
      case 52 -> this.originalMagicAvoid_6c = value;
      case 53 -> this.damageReductionFlags_6e = value;
      case 54 -> this._70 = value;
      case 55 -> this.monsterElementFlag_72 = value;
      case 56 -> this.monsterElementalImmunityFlag_74 = value;
      case 57 -> this.monsterStatusResistFlag_76 = value;
      case 58 -> this.targetArrowPos_78.setX((short)value);
      case 59 -> this.targetArrowPos_78.setY((short)value);
      case 60 -> this.targetArrowPos_78.setZ((short)value);

      default -> super.setStat(statIndex, value);
    }
  }
}
