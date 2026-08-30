package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public interface Tag extends Cloneable {
  // Collections
  int TAG_TYPE_MAP = 0;
  int TAG_TYPE_LIST = 1;

  // Primitives
  int TAG_TYPE_STRING = 10;
  int TAG_TYPE_INT = 11;
  int TAG_TYPE_LONG = 12;
  int TAG_TYPE_FLOAT = 13;
  int TAG_TYPE_BOOL = 14;
  int TAG_TYPE_RAW = 15;

  // Complex types
  int TAG_TYPE_REGISTRY_ID = 30;
  int TAG_TYPE_ENUM = 31;

  int getType();
  void serialize(final FileData data, final IntRef offset);
  void deserialize(final FileData data, final IntRef offset);
  Tag clone();

  default MapTag asMap() {
    return (MapTag)this;
  }

  default ListTag asList() {
    return (ListTag)this;
  }

  default StringTag asString() {
    return (StringTag)this;
  }

  default IntTag asInt() {
    return (IntTag)this;
  }

  default LongTag asLong() {
    return (LongTag)this;
  }

  default FloatTag asFloat() {
    return (FloatTag)this;
  }

  default BoolTag asBool() {
    return (BoolTag)this;
  }

  default RawTag asRaw() {
    return (RawTag)this;
  }

  default RegistryIdTag asRegistryId() {
    return (RegistryIdTag)this;
  }

  default EnumTag asEnum() {
    return (EnumTag)this;
  }

  static Tag makeTag(final int type) {
    return switch(type) {
      case TAG_TYPE_MAP -> new MapTag();
      case TAG_TYPE_LIST -> new ListTag();

      case TAG_TYPE_STRING -> new StringTag();
      case TAG_TYPE_INT -> new IntTag();
      case TAG_TYPE_LONG -> new LongTag();
      case TAG_TYPE_FLOAT -> new FloatTag();
      case TAG_TYPE_BOOL -> new BoolTag();
      case TAG_TYPE_RAW -> new RawTag();

      case TAG_TYPE_REGISTRY_ID -> new RegistryIdTag();
      case TAG_TYPE_ENUM -> new EnumTag();

      default -> throw new IllegalStateException("Unknown type: " + type);
    };
  }
}
