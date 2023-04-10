package legend.game.modding.events.combat;

import legend.game.modding.events.Event;

public class SpellStatsEvent extends Event {
  public final int spellId;
  public int targetType;
  public int flags_01;
  public int specialEffect;
  public int damageFlag;
  public int healingPercent;
  public int accuracy;
  public int mpUsage;
  public int statusChance;
  public int element;
  public int statusType;
  public int buffType;
  public int _0b;

  public SpellStatsEvent(final int spellId, final int targetType, final int flags, final int specialEffect, final int damageFlag, final int healingPercent, final int accuracy, final int mpUsage, final int statusChance, final int element, final int statusType, final int buffType, final int _0b) {
    this.spellId = spellId;
    this.targetType = targetType;
    this.flags_01 = flags;
    this.specialEffect = specialEffect;
    this.damageFlag = damageFlag;
    this.healingPercent = healingPercent;
    this.accuracy = accuracy;
    this.mpUsage = mpUsage;
    this.statusChance = statusChance;
    this.element = element;
    this.statusType = statusType;
    this.buffType = buffType;
    this._0b = _0b;
  }
}
