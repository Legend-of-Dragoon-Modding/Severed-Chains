package legend.game.modding.events.combat;

import legend.game.combat.types.MonsterStats1c;
import legend.game.modding.events.Event;

import static legend.game.combat.SBtld.monsterStats_8010ba98;

public class EnemyStatsEvent extends Event {
  public final int enemyId;

  public int hp;
  public int mp;
  public int maxHp;
  public int maxMp;
  /**
   * <ul>
   *   <li>0x1 - 1 damage from magic</li>
   *   <li>0x2 - 1 damage from physical</li>
   * </ul>
   */
  public int specialEffectFlag;
  public int elementFlag;
  public int elementalImmunityFlag;
  public int statusResistFlag;
  public int speed;
  public int attack;
  public int magicAttack;
  public int defence;
  public int magicDefence;
  public int attackAvoid;
  public int magicAvoid;

  public EnemyStatsEvent(final int enemyId) {
    this.enemyId = enemyId;

    final MonsterStats1c monsterStats = monsterStats_8010ba98.get(enemyId);
    this.hp = monsterStats.hp_00.get();
    this.mp = monsterStats.mp_02.get();
    this.maxHp = monsterStats.hp_00.get();
    this.maxMp = monsterStats.mp_02.get();
    this.specialEffectFlag = monsterStats.specialEffectFlag_0d.get();
    this.elementFlag = monsterStats.elementFlag_0f.get();
    this.elementalImmunityFlag = monsterStats.elementalImmunityFlag_10.get();
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
