package legend.game.modding.events.screen;

import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.events.Event;

public class EquipMenuEntryIconEvent extends Event {
  public final Equipment equip;
  public int icon;

  public EquipMenuEntryIconEvent(final Equipment equip) {
    this.equip = equip;
    this.icon = equip.getIcon();
  }
}
