package legend.game.combat.types;

import legend.game.combat.bent.BattleEntityStat;

public enum AttackType {
  PHYSICAL(BattleEntityStat.TEMP_ATTACK_HIT, BattleEntityStat.TEMP_ATTACK_AVOID, BattleEntityStat.SPELL_FLAGS, 0x4),
  DRAGOON_MAGIC_STATUS_ITEMS(BattleEntityStat.TEMP_MAGIC_HIT, BattleEntityStat.TEMP_MAGIC_AVOID, BattleEntityStat.SPELL_FLAGS, 0x4),
  ITEM_MAGIC(BattleEntityStat.TEMP_MAGIC_HIT, BattleEntityStat.TEMP_MAGIC_AVOID, BattleEntityStat.ITEM_TYPE, 0xe0),
  ;

  public final BattleEntityStat tempHitStat;
  public final BattleEntityStat tempAvoidStat;
  public final BattleEntityStat alwaysHitStat;
  public final int alwaysHitMask;

  AttackType(final BattleEntityStat tempHitStat, final BattleEntityStat tempAvoidStat, final BattleEntityStat alwaysHitStat, final int alwaysHitMask) {
    this.tempHitStat = tempHitStat;
    this.tempAvoidStat = tempAvoidStat;
    this.alwaysHitStat = alwaysHitStat;
    this.alwaysHitMask = alwaysHitMask;
  }

  public boolean isPhysical() {
    return this == PHYSICAL;
  }

  public boolean isMagical() {
    return this != PHYSICAL;
  }
}
