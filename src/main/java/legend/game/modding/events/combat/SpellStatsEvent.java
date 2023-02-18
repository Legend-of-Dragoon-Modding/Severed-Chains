package legend.game.modding.events.combat;

import legend.game.modding.events.Event;

public class SpellStatsEvent extends Event {
  public int spellId;
  public int targetType;
  public int _01;
  public int specialEffect;
  public int damageFlag;
  public int healingPercent;
  public int accuracy;
  public int mpUsage;
  public int statusChance;
  public int element;
  public int statusType;
  public int buffType;
  public int _0B;

  public SpellStatsEvent(int spellId, int targetType, int _01, int specialEffect, int damageFlag, int healingPercent, int accuracy, int mpUsage, int statusChance, int element, int statusType, int buffType, int _0B) {
    this.spellId = spellId;
    this.targetType = targetType;
    this._01 = _01;
    this.specialEffect = specialEffect;
    this.damageFlag = damageFlag;
    this.healingPercent = healingPercent;
    this.accuracy = accuracy;
    this.mpUsage = mpUsage;
    this.statusChance = statusChance;
    this.element = element;
    this.statusType = statusType;
    this.buffType = buffType;
    this._0B = _0B;
  }
}
