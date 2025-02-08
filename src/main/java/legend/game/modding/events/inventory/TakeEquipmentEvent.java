package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

public class TakeEquipmentEvent extends Event {
  public final Equipment equip;
  public int equipmentIndex;
  public boolean takeEquip = false;

  public TakeEquipmentEvent(final Equipment equip, final int equipmentIndex, final boolean takeEquip) {
    this.equip = equip;
    this.equipmentIndex = equipmentIndex;
    this.takeEquip = takeEquip;
  }
}
