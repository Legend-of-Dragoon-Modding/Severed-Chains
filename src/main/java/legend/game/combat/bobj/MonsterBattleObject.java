package legend.game.combat.bobj;

import legend.core.gte.SVECTOR;
import legend.game.combat.types.AttackType;
import legend.game.modding.coremod.CoreMod;

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
  public int _7e;
  public int _80;
  public int _82;
  public int _84;
  public int _86;
  public int _88;
  public int _8a;

  public MonsterBattleObject(final String name) {
    super(CoreMod.MONSTER_TYPE.get(), name);
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
      case 61 -> this._7e;
      case 62 -> this._80;
      case 63 -> this._82;
      case 64 -> this._84;
      case 65 -> this._86;
      case 66 -> this._88;
      case 67 -> this._8a;

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
      case 61 -> this._7e = value;
      case 62 -> this._80 = value;
      case 63 -> this._82 = value;
      case 64 -> this._84 = value;
      case 65 -> this._86 = value;
      case 66 -> this._88 = value;
      case 67 -> this._8a = value;

      default -> super.setStat(statIndex, value);
    }
  }
}
