package legend.game.inventory;

import javax.annotation.Nullable;

public class EquipItemResult {
  public final boolean success;
  @Nullable public final Equipment previousEquipment;

  private EquipItemResult(final boolean success, @Nullable final Equipment previousEquipment) {
    this.success = success;
    this.previousEquipment = previousEquipment;
  }

  public static EquipItemResult success(@Nullable final Equipment previousEquipment) {
    return new EquipItemResult(true, previousEquipment);
  }

  public static EquipItemResult failure() {
    return new EquipItemResult(false, null);
  }
}
