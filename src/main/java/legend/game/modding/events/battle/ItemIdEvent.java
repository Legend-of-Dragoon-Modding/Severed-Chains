package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import org.legendofdragoon.modloader.registries.RegistryId;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class ItemIdEvent extends BattleEvent {
  public int itemId;
  public RegistryId registryId;

  public ItemIdEvent(final int itemId, final RegistryId registryId) {
    this.itemId = itemId;
    this.registryId = registryId;
  }
}