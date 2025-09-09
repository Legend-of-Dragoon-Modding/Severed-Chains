package legend.game.inventory;

/** How to handle giving too many items/equips to a player */
public enum OverflowMode {
  /**
   * Player will be given all items, ignoring inventory size limit. If the
   * player has 31/32 items and is given 2, they will have 33/32 items.
   */
  OVERFLOW,
  /**
   * Player will only be given items that fit in their inventory, and the
   * rest will be discarded. If the player has 31/32 items and is given 2,
   * they will receive the first item and the rest will be discarded.
   */
  TRUNCATE,
  /**
   * The player will not be given any items at all if they won't all fit.
   */
  FAIL,
}
