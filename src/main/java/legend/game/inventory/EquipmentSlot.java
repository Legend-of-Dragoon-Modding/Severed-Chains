package legend.game.inventory;

public enum EquipmentSlot {
  WEAPON,
  HEAD,
  BODY,
  FEET,
  ACCESSORY,
  ;

  public static EquipmentSlot fromRetail(final int index) {
    return values()[index];
  }
}
