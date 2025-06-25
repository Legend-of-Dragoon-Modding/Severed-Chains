package legend.game.combat.bent;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.characters.VitalsStat;
import legend.game.combat.Battle;
import legend.game.combat.types.AttackType;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodMod;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.combat.Battle.applyBuffOrDebuff;
import static legend.game.combat.Battle.applyMagicDamageMultiplier;
import static legend.game.combat.Battle.spellStats_800fa0b8;
import static legend.game.combat.SEffe.transformToScreenSpace;

public class MonsterBattleEntity extends BattleEntity27c {
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

  public final ElementSet monsterElementalImmunity_74 = new ElementSet();
  public int monsterStatusResistFlag_76;
  public final Vector3f targetArrowPos_78 = new Vector3f();

  public MonsterBattleEntity(final String name) {
    super(LodMod.MONSTER_TYPE.get(), name);
  }

  @Override
  public String getName() {
    final Battle battle = ((Battle)currentEngineState_8004dd04);
    return battle.currentEnemyNames_800c69d0[this.charSlot_276];
  }

  @Override
  protected ScriptFile getScript() {
    return this.combatant_144.scriptPtr_10;
  }

  @Override
  public ElementSet getAttackElements() {
    return new ElementSet().add(spellStats_800fa0b8[this.spellId_4e].element_08.get());
  }

  @Override
  public int applyPhysicalDamageMultipliers(final BattleEntity27c target, final int damage) {
    return applyMagicDamageMultiplier(this, target, damage, 0);
  }

  @Override
  public void applyAttackEffects() {
    applyBuffOrDebuff(this, this);
  }

  @Override
  public int applyDamageResistanceAndImmunity(final int damage, final AttackType attackType) {
    if(attackType.isPhysical() && (this.damageReductionFlags_6e & 0x2) != 0) {
      return 1;
    }

    if(attackType.isMagical() && (this.damageReductionFlags_6e & 0x1) != 0) {
      return 1;
    }

    return super.applyDamageResistanceAndImmunity(damage, attackType);
  }

  @Override
  @Method(0x800f2d48L)
  public int calculatePhysicalDamage(final BattleEntity27c target) {
    final int atk = this.attack_34 + spellStats_800fa0b8[this.spellId_4e].multi_04;

    //LAB_800f2e28
    //LAB_800f2e88
    return atk * atk * 5 / target.getEffectiveDefence();
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Override
  @Method(0x800f8768L)
  public int calculateMagicDamage(final BattleEntity27c target, final int magicType) {
    int matk = this.magicAttack_36;
    if(magicType == 1) {
      matk += spellStats_800fa0b8[this.spellId_4e].multi_04;
    }

    //LAB_800f87d0
    //LAB_800f8844
    return matk * matk * 5 / target.getEffectiveMagicDefence();
  }

  @Override
  protected void bentRenderer(final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
    super.bentRenderer(state, bent);

    if((state.storage_44[7] & (FLAG_NO_SCRIPT | FLAG_HIDE | FLAG_1)) == 0 && CONFIG.getConfig(CoreMod.ENEMY_HP_BARS_CONFIG.get())) {
      final VitalsStat stat = bent.stats.getStat(LodMod.HP_STAT.get());
      final float hp = (float)stat.getCurrent() / stat.getMax();

      if(hp != 0.0f) {
        final float r;
        final float g;
        final float b;
        if(hp <= 0.25f) {
          r = 0.85f;
          g = 0.0f;
          b = 0.0f;
        } else if(hp <= 0.5f) {
          r = 0.85f;
          g = 0.85f;
          b = 0.0f;
        } else {
          r = 0.0f;
          g = 0.0f;
          b = 0.85f;
        }

        final Vector2f screenspace = new Vector2f();
        final float z = transformToScreenSpace(screenspace, this.model_148.coord2_14.coord.transfer);
        if(z != 0) {
          final MV transforms = new MV();
          transforms.transfer.set(screenspace.x - 13.0f, screenspace.y + 7.0f, z * 4.0f);
          transforms.scaling(26.0f, 4.0f, 1.0f);
          RENDERER
            .queueOrthoModel(RENDERER.opaqueQuad, transforms, QueuedModelStandard.class)
            .screenspaceOffset(160.0f, 120.0f)
            .depthOffset(-400.0f)
            .monochrome(0.0f);

          transforms.transfer.set(screenspace.x - 12.0f, screenspace.y + 8.0f, z * 4.0f - 0.1f);
          transforms.scaling(24.0f * hp, 2.0f, 1.0f);
          RENDERER
            .queueOrthoModel(RENDERER.opaqueQuad, transforms, QueuedModelStandard.class)
            .screenspaceOffset(160.0f, 120.0f)
            .depthOffset(-400.0f)
            .colour(r, g, b);
        }
      }
    }
  }

  @Override
  public int getStat(final BattleEntityStat statIndex) {
    return switch(statIndex) {
      case EQUIPMENT_ATTACK_ELEMENT_OR_MONSTER_DISPLAY_ELEMENT, MONSTER_ELEMENT -> this.element.flag;

      case MONSTER_DAMAGE_REDUCTION -> this.damageReductionFlags_6e;
      case _54 -> this._70;
      case MONSTER_ELEMENTAL_IMMUNITY -> this.monsterElementalImmunity_74.pack();
      case MONSTER_STATUS_RESIST_FLAGS -> this.monsterStatusResistFlag_76;
      case MONSTER_TARGET_ARROW_POSITION_X -> Math.round(this.targetArrowPos_78.x);
      case MONSTER_TARGET_ARROW_POSITION_Y -> Math.round(this.targetArrowPos_78.y);
      case MONSTER_TARGET_ARROW_POSITION_Z -> Math.round(this.targetArrowPos_78.z);

      default -> super.getStat(statIndex);
    };
  }

  @Override
  public void setStat(final BattleEntityStat statIndex, final int value) {
    switch(statIndex) {
      case MONSTER_DAMAGE_REDUCTION -> this.damageReductionFlags_6e = value;
      case _54 -> this._70 = value;
      case MONSTER_ELEMENT -> this.element = Element.fromFlag(value).get();
      case MONSTER_ELEMENTAL_IMMUNITY -> this.monsterElementalImmunity_74.unpack(value);
      case MONSTER_STATUS_RESIST_FLAGS -> this.monsterStatusResistFlag_76 = value;
      case MONSTER_TARGET_ARROW_POSITION_X -> this.targetArrowPos_78.x = value;
      case MONSTER_TARGET_ARROW_POSITION_Y -> this.targetArrowPos_78.y = value;
      case MONSTER_TARGET_ARROW_POSITION_Z -> this.targetArrowPos_78.z = value;

      default -> super.setStat(statIndex, value);
    }
  }
}
