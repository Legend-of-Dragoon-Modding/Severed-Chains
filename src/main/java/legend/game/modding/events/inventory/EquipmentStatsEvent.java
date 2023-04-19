package legend.game.modding.events.inventory;

import legend.game.modding.events.Event;
import legend.game.types.EquipmentStats1c;

public class EquipmentStatsEvent extends Event {
  public final int charId;
  public final int equipmentId;

  public EquipmentStats1c equipment;

  public EquipmentStatsEvent(final int charId, final int equipmentId, final EquipmentStats1c equipment) {
    this.charId = charId;
    this.equipmentId = equipmentId;
    this.equipment = equipment;
  }
}
