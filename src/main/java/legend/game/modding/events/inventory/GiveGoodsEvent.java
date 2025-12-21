package legend.game.modding.events.inventory;

import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player receives a good
 */
public class GiveGoodsEvent extends InventoryEvent {
  /** The player's current goods */
  public final GoodsInventory goods;
  /** The goods that were given. Modders may add or remove goods from this list to change what goods the player receives. */
  public final List<Good> givenGoods = new ArrayList<>();

  public GiveGoodsEvent(final GoodsInventory goods, final Good givenGood) {
    this.goods = goods;
    this.givenGoods.add(givenGood);
  }
}
