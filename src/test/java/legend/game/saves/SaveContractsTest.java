package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.core.tags.ImmutableRawTag;
import legend.core.tags.MapTag;
import legend.core.tags.RawTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaveContractsTest {
  @Test
  void immutablePackageBytesSurviveCallerAndSnapshotMutation() {
    final byte[] source = {1, 2, 3};
    final ImmutableRawTag raw = new ImmutableRawTag(source);
    source[0] = 99;
    raw.get()[1] = 88;
    final MapTag original = new MapTag();
    original.set("asset", raw);
    final MapTag snapshot = original.clone();
    assertSame(raw, snapshot.get("asset"));
    snapshot.remove("asset");
    assertArrayEquals(new byte[]{1, 2, 3}, original.get("asset").asRaw().get());
  }

  @Test
  void rawTagRejectsDeclaredLengthLargerThanRemainingInput() {
    final FileData data = new FileData(new byte[1]);
    data.writeVarInt(new IntRef(), 127);
    assertThrows(IllegalArgumentException.class, () -> new RawTag().deserialize(data, new IntRef()));
  }

  @Test
  void rawTagAcceptsExactlyRemainingBytes() {
    final FileData data = new FileData(new byte[4]);
    final IntRef offset = new IntRef();
    new RawTag(new byte[]{4, 5, 6}).serialize(data, offset);
    final RawTag decoded = new RawTag();
    decoded.deserialize(data, new IntRef());
    assertArrayEquals(new byte[]{4, 5, 6}, decoded.get());
  }

  @Test
  void boundedExpandableSlicesCannotBypassRootCapacity() {
    final ExpandableFileData data = new ExpandableFileData(1, 16);
    final FileData slice = data.slice(8, 4);
    slice.writeInt(4, 42);
    assertEquals(42, data.readInt(12));
    assertThrows(IllegalArgumentException.class, () -> slice.writeInt(5, 42));
    assertThrows(IllegalArgumentException.class, () -> data.slice(Integer.MAX_VALUE, 1));
    assertEquals(16, data.size());
  }

  @Test
  void opaqueUnknownFieldsSurviveWhileClearedKnownOptionalFieldsStayAbsent() {
    final RegistryId id = new RegistryId("test", "item");
    final MapTag before = new MapTag();
    before.set("itemId", new RegistryIdTag(id));
    before.set("extraData", new StringTag("removed"));
    before.set("futureField", new StringTag("preserved"));
    final MapTag current = new MapTag();
    current.set("itemId", new RegistryIdTag(id));
    SaveRegistryData.merge(current, before);
    assertFalse(current.has("extraData"));
    assertEquals("preserved", current.get("futureField").asString().get());
    final MapTag oldCharacter = new MapTag();
    oldCharacter.set("templateId", new RegistryIdTag(id));
    oldCharacter.set("selectedAdditionId", new RegistryIdTag(id));
    final MapTag character = new MapTag();
    character.set("templateId", new RegistryIdTag(id));
    SaveRegistryData.merge(character, oldCharacter);
    assertFalse(character.has("selectedAdditionId"));
  }
}
