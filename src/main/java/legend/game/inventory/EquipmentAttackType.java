package legend.game.inventory;

import legend.lodmod.equipment.DetonateArrowEquipment;

public enum EquipmentAttackType {
  /** Uses the normal attack code */
  NORMAL,
  /** Uses the DEFF loader (see {@link DetonateArrowEquipment}) */
  DEFF,
  /** Skips all attack code in the player combat script and allows fully custom attacks */
  CUSTOM,
}
