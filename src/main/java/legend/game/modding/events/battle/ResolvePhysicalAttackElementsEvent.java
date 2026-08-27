package legend.game.modding.events.battle;

import legend.game.characters.ElementSet;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.events.Event;

/**
 * Fired when the element set for a player's physical attack is requested.
 *
 * <p>{@link #baseElements} and {@link #elements} are independent defensive copies. Listeners may
 * add, remove, clear, or replace the contents of {@code elements}; that mutated set is returned to
 * the requester. Mutating {@code elements} does not change {@code baseElements} or the attacker's
 * equipment element set.</p>
 */
public class ResolvePhysicalAttackElementsEvent extends Event {
  /** The player making the physical attack. */
  public final PlayerBattleEntity attacker;

  /** Snapshot of the attack elements before listener changes. */
  public final ElementSet baseElements;

  /** Mutable attack-element result returned after the event. */
  public final ElementSet elements;

  /**
   * Creates a physical-attack element-resolution request.
   *
   * @param attacker player making the physical attack
   * @param baseElements attack elements used to initialize independent base and result snapshots
   */
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
