package legend.game.modding.events.inventory;

import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player's goods are taken
 */
public class TakeGoodsEvent extends InventoryEvent {
  /** The player's current goods */
  public final GoodsInventory goods;
  /** The goods that were taken. Modders may add or remove goods from this list to change what goods are taken from the player. */
  public final List<Good> takenGoods = new ArrayList<>();

  public TakeGoodsEvent(final GoodsInventory goods, final Good takenGood) {
    this.goods = goods;
    this.takenGoods.add(takenGood);
  }
}
