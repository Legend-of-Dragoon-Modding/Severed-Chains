package legend.game.inventory;

public enum CanEquip {
  /** Allows the character to equip the equipment no matter what */
  FORCE,
  /** Allows the character to equip the equipment if the character is able to use the equipment's equipment type */
  NORMAL,
  /** Prevents the character from equipping the equipment no matter what */
  DENY,
}
