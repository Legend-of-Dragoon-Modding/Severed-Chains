package legend.game.combat.types;

public enum AttackType {
  PHYSICAL(92, 94, 73, 0x4),
  DRAGOON_MAGIC_STATUS_ITEMS(93, 95, 73, 0x4),
  ITEM_MAGIC(93, 95, 115, 0xe0),
  ;

  public final int hitStat;
  public final int avoidStat;
  public final int alwaysHitStat;
  public final int alwaysHitMask;

  AttackType(final int hitStat, final int avoidStat, final int alwaysHitStat, final int alwaysHitMask) {
    this.hitStat = hitStat;
    this.avoidStat = avoidStat;
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
