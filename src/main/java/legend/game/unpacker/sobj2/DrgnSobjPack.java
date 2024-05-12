package legend.game.unpacker.sobj2;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

final class DrgnSobjPack {
  Int2ObjectOpenHashMap<Sobj[]> maps = new Int2ObjectOpenHashMap<>();

  void addMap(final int index, final Sobj[] sobjs) {
    this.maps.put(index, sobjs);
  }

  Int2ObjectMap.FastEntrySet<Sobj[]> getMaps() {
    return this.maps.int2ObjectEntrySet();
  }
}
