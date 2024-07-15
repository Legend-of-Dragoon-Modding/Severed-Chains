package legend.game.combat.types;

public class MonsterStats1c {
  public int hp_00;
  /** Unused */
  public int mp_02;
  public int attack_04;
  public int magicAttack_06;
  public int speed_08;
  public int defence_09;
  public int magicDefence_0a;
  public int attackAvoid_0b;
  public int magicAvoid_0c;
  public int specialEffectFlag_0d;
  public int _0e;
  public int elementFlag_0f;
  public int elementalImmunityFlag_10;
  public int statusResistFlag_11;
  public int targetArrowX_12;
  public int targetArrowY_13;
  public int targetArrowZ_14;
  public int hitCounterFrameThreshold_15;
  public int _16;
  public int _17;
  /** X offset for archer/item throw target, status ailment effect, 1/100 scale */
  public int middleOffsetX_18;
  /** Y offset for archer/item throw target, status ailment effect, 1/100 scale */
  public int middleOffsetY_19;
  public int _1a;
  public int _1b;

  public MonsterStats1c(final int hp, final int mp, final int attack, final int magicAttack, final int speed, final int defence, final int magicDefence, final int attackAvoid, final int magicAvoid, final int specialEffectFlag, final int _0e, final int elementFlag, final int elementalImmunityFlag, final int statusResistFlag, final int targetArrowX, final int targetArrowY, final int targetArrowZ, final int hitCounterFrameThreshold, final int _16, final int _17, final int middleOffsetX, final int middleOffsetY, final int _1a, final int _1b) {
    this.hp_00 = hp;
    this.mp_02 = mp;
    this.attack_04 = attack;
    this.magicAttack_06 = magicAttack;
    this.speed_08 = speed;
    this.defence_09 = defence;
    this.magicDefence_0a = magicDefence;
    this.attackAvoid_0b = attackAvoid;
    this.magicAvoid_0c = magicAvoid;
    this.specialEffectFlag_0d = specialEffectFlag;
    this._0e = _0e;
    this.elementFlag_0f = elementFlag;
    this.elementalImmunityFlag_10 = elementalImmunityFlag;
    this.statusResistFlag_11 = statusResistFlag;
    this.targetArrowX_12 = targetArrowX;
    this.targetArrowY_13 = targetArrowY;
    this.targetArrowZ_14 = targetArrowZ;
    this.hitCounterFrameThreshold_15 = hitCounterFrameThreshold;
    this._16 = _16;
    this._17 = _17;
    this.middleOffsetX_18 = middleOffsetX;
    this.middleOffsetY_19 = middleOffsetY;
    this._1a = _1a;
    this._1b = _1b;
  }
}
