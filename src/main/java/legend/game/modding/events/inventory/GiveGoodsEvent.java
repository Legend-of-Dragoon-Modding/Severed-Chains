package legend.game.modding.events.inventory;

import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.GoodsSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player receives a good
 *
 * <p>The {@link GoodsSource} stored in {@link #source} is one of:</p>
 * <ul>
 *   <li>{@link GoodsSource#INITIALIZATION} when building initial or restored character state</li>
 *   <li>{@link GoodsSource#GAMEPLAY} when progression is earned during gameplay</li>
 *   <li>{@link GoodsSource#DEBUGGER} when applied through the character debugger</li>
 *   <li>{@link GoodsSource#EXTERNAL} when applied through external means/mods</li>
 * </ul>
 */
public class GiveGoodsEvent extends InventoryEvent {
  /** The player's current goods */
  public final GoodsInventory goods;
  /** The goods that were given. Modders may add or remove goods from this list to change what goods the player receives. */
  public final List<Good> givenGoods = new ArrayList<>();

  public final GoodsSource source;

  public GiveGoodsEvent(final GoodsInventory goods, final Good givenGood, final GoodsSource source) {
    this.goods = goods;
    this.givenGoods.add(givenGood);
    this.source = source;
  }
}
