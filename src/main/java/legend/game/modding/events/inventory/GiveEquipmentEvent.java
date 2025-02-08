package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;

public class GiveEquipmentEvent extends Event {
  public List<Equipment> equips = new ArrayList<>();

  public GiveEquipmentEvent(final Equipment equip) {
    this.equips.add(equip);
  }
}
