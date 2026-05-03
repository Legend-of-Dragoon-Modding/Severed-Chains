package legend.game.combat.types;

import legend.game.combat.bent.BattleEntity27c;

import java.util.function.ToIntFunction;

public enum AttackType {
  PHYSICAL(bent -> bent.tempAttackHit_bc, bent -> bent.tempAttackAvoid_c0, bent -> bent.spell_94.flags_01, 0x4),
  DRAGOON_MAGIC_STATUS_ITEMS(bent -> bent.tempMagicHit_be, bent -> bent.tempMagicAvoid_c2, bent -> bent.spell_94.flags_01, 0x4),
  ITEM_MAGIC(bent -> bent.tempMagicHit_be, bent -> bent.tempMagicAvoid_c2, bent -> 0, 0xe0),
  ;

  public final ToIntFunction<BattleEntity27c> tempHitStat;
  public final ToIntFunction<BattleEntity27c> tempAvoidStat;
  public final ToIntFunction<BattleEntity27c> alwaysHitStat;
  public final int alwaysHitMask;

  AttackType(final ToIntFunction<BattleEntity27c> tempHitStat, final ToIntFunction<BattleEntity27c> tempAvoidStat, final ToIntFunction<BattleEntity27c> alwaysHitStat, final int alwaysHitMask) {
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
