package legend.game.modding.events.battle;

import legend.game.characters.ElementSet;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.events.Event;

public class ResolvePhysicalAttackElementsEvent extends Event {
  public final PlayerBattleEntity attacker;
  public final ElementSet baseElements;
  public final ElementSet elements;

  public ResolvePhysicalAttackElementsEvent(final PlayerBattleEntity attacker, final ElementSet baseElements) {
    if(attacker == null) {
      throw new IllegalArgumentException("Physical attack element attacker cannot be null");
    }
    if(baseElements == null) {
      throw new IllegalArgumentException("Physical attack base elements cannot be null");
    }

    this.attacker = attacker;
    this.baseElements = new ElementSet().set(baseElements);
    this.elements = new ElementSet().set(baseElements);
  }
}
