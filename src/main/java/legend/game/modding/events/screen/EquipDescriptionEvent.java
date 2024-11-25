package legend.game.modding.events.screen;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class EquipDescriptionEvent extends Event {
  public final Equipment equip;
  public String description;

  public EquipDescriptionEvent(final Equipment equip, final String description) {
    this.equip = equip;
    this.description = description;
  }
}
