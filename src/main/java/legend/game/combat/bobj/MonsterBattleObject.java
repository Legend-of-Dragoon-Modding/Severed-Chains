package legend.game.combat.bobj;

import legend.core.gte.SVECTOR;
import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.combat.types.AttackType;
import legend.game.modding.coremod.CoreMod;

import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;

public class MonsterBattleObject extends BattleObject27c {
  public Element displayElement_1c;

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
  public Element monsterElementFlag_72;
  public final ElementSet monsterElementalImmunityFlag_74 = new ElementSet();
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
  public Element getAttackElement() {
    return Element.fromFlag(spellStats_800fa0b8.get(this.spellId_4e).element_08.get());
  }

  @Override
  public Element getElement() {
    return this.monsterElementFlag_72;
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
  public int calculatePhysicalAttack(final BattleObject27c target) {
    final int atk = this.attack_34 + spellStats_800fa0b8.get(this.spellId_4e).multi_04.get();

    //LAB_800f2e28
    //LAB_800f2e88
    return atk * atk * 5 / target.getEffectiveDefence();
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Override
  @Method(0x800f8768L)
  public int calculateMagicAttack(final BattleObject27c target, final int magicType) {
    int matk = this.magicAttack_36;
    if(magicType == 1) {
      matk += spellStats_800fa0b8.get(this.spellId_4e).multi_04.get();
    } else {
      //LAB_800f87c4
      matk += this.item_d4.damage_05.get();
    }

    //LAB_800f87d0
    //LAB_800f8844
    return matk * matk * 5 / target.getEffectiveMagicDefence();
  }

  @Override
  public int getStat(final int statIndex) {
    return switch(statIndex) {
      case 12 -> this.displayElement_1c.flag;

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
      case 55 -> this.monsterElementFlag_72.flag;
      case 56 -> this.monsterElementalImmunityFlag_74.pack();
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
      case 55 -> this.monsterElementFlag_72 = Element.fromFlag(value);
      case 56 -> this.monsterElementalImmunityFlag_74.unpack(value);
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
