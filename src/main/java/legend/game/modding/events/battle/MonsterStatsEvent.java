package legend.game.modding.events.battle;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.combat.types.MonsterStats1c;
import legend.game.modding.events.Event;

import static legend.game.combat.SBtld.monsterStats_8010ba98;

public class MonsterStatsEvent extends Event {
  public final int enemyId;

  public int hp;
  public int maxHp;
  /**
   * <ul>
   *   <li>0x1 - 1 damage from magic</li>
   *   <li>0x2 - 1 damage from physical</li>
   * </ul>
   */
  public int specialEffectFlag;
  public Element elementFlag;
  public final ElementSet elementalImmunityFlag = new ElementSet();
  public int statusResistFlag;
  public int speed;
  public int attack;
  public int magicAttack;
  public int defence;
  public int magicDefence;
  public int attackAvoid;
  public int magicAvoid;

  public MonsterStatsEvent(final int enemyId) {
    this.enemyId = enemyId;

    final MonsterStats1c monsterStats = monsterStats_8010ba98.get(enemyId);
    this.hp = monsterStats.hp_00.get();
    this.maxHp = monsterStats.hp_00.get();
    this.specialEffectFlag = monsterStats.specialEffectFlag_0d.get();
    this.elementFlag = Element.fromFlag(monsterStats.elementFlag_0f.get());
    this.elementalImmunityFlag.unpack(monsterStats.elementalImmunityFlag_10.get());
    this.statusResistFlag = monsterStats.statusResistFlag_11.get();
    this.speed = monsterStats.speed_08.get();
    this.attack = monsterStats.attack_04.get();
    this.magicAttack = monsterStats.magicAttack_06.get();
    this.defence = monsterStats.defence_09.get();
    this.magicDefence = monsterStats.magicDefence_0a.get();
    this.attackAvoid = monsterStats.attackAvoid_0b.get();
    this.magicAvoid = monsterStats.magicAvoid_0c.get();
  }
}
