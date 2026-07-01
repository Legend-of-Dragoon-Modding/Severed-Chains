package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ActiveItemEvent extends Event {
  public final BattleEntity27c bent;
  public final RegistryId registryId;
  public ItemStack item;

  /**
   * Changes the active item of a battle entity. Allowing the ability to change its properties. Typically used for MonsterBattleEntities.
   *
   * @param bent Battle Entity to replace the SpellStats0c spell of
   * @param registryId The registry ID of the SpellStats0c spell
   * @param item The current ItemStack that was set by the script
   */
  public ActiveItemEvent(final BattleEntity27c bent, final RegistryId registryId, final ItemStack item) {
    this.bent = bent;
    this.item = item;
    this.registryId = registryId;
  }
}
