package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

public class GiveEquipmentEvent extends Event {
  public Equipment equip;
  public boolean override = false;

  public GiveEquipmentEvent(final Equipment equip) {
    this.equip = equip;
  }
}
