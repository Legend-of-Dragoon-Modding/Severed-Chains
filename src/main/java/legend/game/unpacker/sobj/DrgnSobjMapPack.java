package legend.game.unpacker.sobj;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;

public final class DrgnSobjMapPack {
  final Int2ObjectOpenHashMap<List<SobjEntry>> maps = new Int2ObjectOpenHashMap<>();

  void addMap(final int index, final List<SobjEntry> sobjs) {
    this.maps.put(index, sobjs);
  }

  Int2ObjectMap.FastEntrySet<List<SobjEntry>> getMaps() {
    return this.maps.int2ObjectEntrySet();
  }
}
