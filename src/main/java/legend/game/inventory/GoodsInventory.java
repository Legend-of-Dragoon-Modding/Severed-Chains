package legend.game.inventory;

import legend.game.modding.events.inventory.GiveGoodsEvent;
import legend.game.modding.events.inventory.TakeGoodsEvent;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.lodmod.LodMod.GOODS_IDS;
import static legend.lodmod.LodMod.id;

public class GoodsInventory implements Iterable<Good> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GoodsInventory.class);

  private final Set<Good> goods = new HashSet<>();

  public Good give(final Good good) {
    if(!this.has(good)) {
      final GiveGoodsEvent event = EVENTS.postEvent(new GiveGoodsEvent(this, good));

      if(!event.isCanceled()) {
        this.goods.addAll(event.givenGoods);
      }
    }

    return good;
  }

  public RegistryDelegate<Good> give(final RegistryDelegate<Good> good) {
    if(!good.isValid()) {
      LOGGER.warn("Invalid good %s", good.getId());
      return null;
    }

    this.give(good.get());
    return good;
  }

  public void take(final Good good) {
    if(this.has(good)) {
      final TakeGoodsEvent event = EVENTS.postEvent(new TakeGoodsEvent(this, good));

      if(!event.isCanceled()) {
        event.takenGoods.forEach(this.goods::remove);
      }
    }
  }

  public void take(final RegistryDelegate<Good> good) {
    if(!good.isValid()) {
      LOGGER.warn("Invalid good %s", good.getId());
      return;
    }

    this.take(good.get());
  }

  public boolean has(final Good good) {
    return this.goods.contains(good);
  }

  public boolean has(final RegistryDelegate<Good> good) {
    if(!good.isValid()) {
      LOGGER.warn("Invalid good %s", good.getId());
      return false;
    }

    return this.goods.contains(good.get());
  }

  public void clear() {
    this.goods.clear();
  }

  /**
   * Unpacks retail goods, leaving mod goods as-is
   */
  public void unpack(final int index, final int packed) {
    for(int bit = 0; bit < 32 && index * 32 + bit < GOODS_IDS.length; bit++) {
      final RegistryId id = id(GOODS_IDS[index * 32 + bit]);
      final RegistryDelegate<Good> good = REGISTRIES.goods.getEntry(id);
      final boolean set = (packed & 1 << bit) != 0;

      if(set) {
        this.give(good);
      } else {
        this.take(good);
      }
    }
  }

  /**
   * Packs retail goods, ignoring mod goods
   */
  public int pack(final int index) {
    int out = 0;

    for(final Good good : this) {
      if(good.getRegistryId().modId().equals(LodMod.MOD_ID)) {
        for(int bit = 0; bit < 32 && index * 32 + bit < GOODS_IDS.length; bit++) {
          final String bitName = GOODS_IDS[index * 32 + bit];

          if(good.getRegistryId().entryId().equals(bitName)) {
            out |= 1 << bit;
            break;
          }
        }
      }
    }

    return out;
  }

  public void set(final GoodsInventory other) {
    this.goods.clear();
    this.goods.addAll(other.goods);
  }

  public int size() {
    return this.goods.size();
  }

  public Stream<Good> stream() {
    return this.goods.stream();
  }

  @Override
  public @NotNull Iterator<Good> iterator() {
    return this.stream().iterator();
  }
}
