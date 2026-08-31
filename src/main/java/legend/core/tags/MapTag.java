package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

import java.util.HashMap;
import java.util.Map;

public class MapTag implements Tag {
  private final Map<String, Tag> tags = new HashMap<>();

  public void set(final String key, final Tag tag) {
    this.tags.put(key, tag);
  }

  public Tag get(final String key) {
    return this.tags.get(key);
  }

  public boolean has(final String key) {
    return this.tags.containsKey(key);
  }

  @Override
  public int getType() {
    return TAG_TYPE_MAP;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeVarInt(offset, this.tags.size());

    for(final var entry : this.tags.entrySet()) {
      final Tag tag = entry.getValue();
      data.writeVarInt(offset, tag.getType());
      data.writeString(offset, entry.getKey());
      tag.serialize(data, offset);
    }
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.tags.clear();

    final int size = data.readVarInt(offset);

    for(int i = 0; i < size; i++) {
      final int type = data.readVarInt(offset);
      final String key = data.readString(offset);
      final Tag tag = Tag.makeTag(type);
      tag.deserialize(data, offset);
      this.tags.put(key, tag);
    }
  }

  @Override
  public MapTag clone() {
    final MapTag tag = new MapTag();

    for(final var entry : this.tags.entrySet()) {
      tag.tags.put(entry.getKey(), entry.getValue().clone());
    }

    return tag;
  }

  @Override
  public String toString() {
    return "map:" + this.tags;
  }
}
