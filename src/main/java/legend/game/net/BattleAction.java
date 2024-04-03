package legend.game.net;

public enum BattleAction {
  ATTACK(4),
  DEFEND(1),
  ITEM(5),
  RUN(6),
  DRAGOON(2),
  SPECIAL(7),
  ;

  public final int id;

  BattleAction(final int id) {
    this.id = id;
  }
}
