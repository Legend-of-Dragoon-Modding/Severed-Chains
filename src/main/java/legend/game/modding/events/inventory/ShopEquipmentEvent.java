package legend.game.modding.events.inventory;

import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.screens.ShopScreen;
import org.legendofdragoon.modloader.events.Event;

import java.util.List;

public class ShopEquipmentEvent extends Event {
  public final int shopId;
  public final List<ShopScreen.ShopEntry<Equipment>> equipment;

  public ShopEquipmentEvent(final int shopId, final List<ShopScreen.ShopEntry<Equipment>> equipment) {
    this.shopId = shopId;
    this.equipment = equipment;
  }
}
