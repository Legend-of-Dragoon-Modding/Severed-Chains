package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListTag implements Tag, Iterable<Tag> {
  private final List<Tag> tags = new ArrayList<>();

  public void add(final Tag tag) {
    this.tags.add(tag);
  }

  public Tag get(final int index) {
    return this.tags.get(index);
  }

  public void remove(final int index) {
    this.tags.remove(index);
  }

  public int size() {
    return this.tags.size();
  }

  public boolean isEmpty() {
    return this.tags.isEmpty();
  }

  @Override
  public int getType() {
    return TAG_TYPE_LIST;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeVarInt(offset, this.tags.size());

    for(final Tag tag : this.tags) {
      data.writeVarInt(offset, tag.getType());
      tag.serialize(data, offset);
    }
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.tags.clear();

    final int size = data.readVarInt(offset);

    for(int i = 0; i < size; i++) {
      final int type = data.readVarInt(offset);
      final Tag tag = Tag.makeTag(type);
      tag.deserialize(data, offset);
      this.tags.add(tag);
    }
  }

  @Override
  public @NonNull Iterator<Tag> iterator() {
    return this.tags.iterator();
  }

  @Override
  public ListTag clone() {
    final ListTag tag = new ListTag();

    for(final Tag entry : this.tags) {
      tag.tags.add(entry.clone());
    }

    return tag;
  }

  @Override
  public String toString() {
    return "list:" + this.tags;
  }
}
