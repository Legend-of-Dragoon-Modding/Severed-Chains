package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import legend.game.inventory.OverflowMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player receives equipment
 */
public class GiveEquipmentEvent extends InventoryEvent {
  /** The equipment that was given. Modders may add or remove equipment from this list to change what equipment the player receives. */
  public List<Equipment> givenEquipment = new ArrayList<>();
  /** An unmodifiable list of the player's current equipment */
  public final List<Equipment> currentEquipment;
  /** The maximum number of equipment the player can hold */
  public final int maxInventorySize;
  /** How to handle too much equipment being given */
  public OverflowMode overflowMode = OverflowMode.FAIL;

  public GiveEquipmentEvent(final Equipment equipment, final List<Equipment> currentEquipment, final int maxInventorySize) {
    this.givenEquipment.add(equipment);
    this.currentEquipment = currentEquipment;
    this.maxInventorySize = maxInventorySize;
  }
}
