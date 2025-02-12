package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;

/**
 * Fired any time the player's equipment is taken
 */
public class TakeEquipmentEvent extends InventoryEvent {
  /** The equipment that was taken */
  public final Equipment equip;
  /** The slot from which the equipment was taken. Modify this to take equipment from a different slot. */
  public int equipmentSlot;

  public TakeEquipmentEvent(final Equipment equip, final int equipmentSlot) {
    this.equip = equip;
    this.equipmentSlot = equipmentSlot;
  }
}
