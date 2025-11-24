package legend.game.inventory;

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

import static legend.core.GameEngine.REGISTRIES;
import static legend.lodmod.LodMod.GOODS_IDS;
import static legend.lodmod.LodMod.id;

public class GoodsInventory implements Iterable<Good> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GoodsInventory.class);

  private final Set<Good> goods = new HashSet<>();

  public Good give(final Good good) {
    this.goods.add(good);
    return good;
  }

  public RegistryDelegate<Good> give(final RegistryDelegate<Good> good) {
    if(!good.isValid()) {
      LOGGER.warn("Invalid good %s", good.getId());
      return null;
    }

    this.goods.add(good.get());
    return good;
  }

  public void take(final Good good) {
    this.goods.remove(good);
  }

  public void take(final RegistryDelegate<Good> good) {
    if(!good.isValid()) {
      LOGGER.warn("Invalid good %s", good.getId());
      return;
    }

    this.goods.remove(good.get());
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
