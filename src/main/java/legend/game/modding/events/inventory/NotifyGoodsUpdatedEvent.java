package legend.game.modding.events.inventory;

import legend.game.characters.LevelUpSource;
import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.GoodsSource;

import org.legendofdragoon.modloader.events.Event;

/**
 * Fired after a good is updated via {@link GoodsInventory}.
 *
 * <p>The {@link Good} stored in {@link #good} is the Good that was given or taken</p>
 * <p>The {@link boolean} stored in {@link #set} is if good was given or taken</p>
 * <p>The {@link GoodsSource} stored in {@link #source} is one of:</p>
 * <ul>
 *   <li>{@link GoodsSource#INITIALIZATION} not set</li>
 *   <li>{@link GoodsSource#GAMEPLAY} submaps can trigger goods give/take</li>
 *   <li>{@link GoodsSource#DEBUGGER} not set</li>
 *   <li>{@link GoodsSource#EXTERNAL} not set</li>
 * </ul>
 */
public class NotifyGoodsUpdatedEvent extends Event {
  public final Good good;
  public final GoodsSource source;
  public final Boolean set;

  public NotifyGoodsUpdatedEvent(final Good good, final boolean set, final GoodsSource source) {
    this.good = good;
    this.source = source;
    this.set = set;
  }
}
