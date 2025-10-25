package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

public class EquipmentCanEquipEvent extends Event {
  public final Equipment equipment;
  public int equipableFlags_03;

  public EquipmentCanEquipEvent(final Equipment equipment, final int equipableFlags) {
    this.equipment = equipment;
    this.equipableFlags_03 = equipableFlags;
  }
}
