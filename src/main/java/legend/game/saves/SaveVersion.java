package legend.game.saves;

import legend.game.saves.serializers.RetailSerializer;
import legend.game.saves.serializers.LegacySerializer;
import legend.game.saves.serializers.V8Serializer;
import legend.game.saves.serializers.V9Serializer;
import legend.game.unpacker.FileData;

public enum SaveVersion {
  RETAIL(0x01114353, "OG", SaveVersion::defaultMatcher, RetailSerializer::fromRetail), // SC__
  V1(0x76615344, "V1", SaveVersion::defaultMatcher, RetailSerializer::fromV1), // DSav
  V2(0x32615344, "V2", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa2
  V3(0x33615344, "V3", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa3
  V4(0x34615344, "V4", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa4
  V5(0x35615344, "V5", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa5
  V6(0x36615344, "V6", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa6
  V7(0x37615344, "V7", SaveVersion::defaultMatcher, LegacySerializer::fromV2To7), // DSa7
  V8(0x38615344, "V8", SaveVersion::defaultMatcher, V8Serializer::fromV8), // DSa8
  V9(0x39615344, "V9", SaveVersion::defaultMatcher, V9Serializer::fromV9), // DSa9
  ;

  public final int code;
  public final String name;
  private final SaveMatcher matcher;
  private final SaveDeserializer deserializer;

  SaveVersion(final int code, final String name, final SaveMatcher matcher, final SaveDeserializer deserializer) {
    this.code = code;
    this.name = name;
    this.matcher = matcher;
    this.deserializer = deserializer;
  }

  public FileData match(final FileData data) {
    return this.matcher.match(this, data);
  }

  public SavedGame deserialize(final Campaign campaign, final String name, final FileData data) {
    return this.deserializer.deserialize(this, campaign, name, data);
  }

  private static FileData defaultMatcher(final SaveVersion version, final FileData data) {
    if(data.readInt(0) == version.code) {
      return data.slice(0x4);
    }

    return null;
  }
}
