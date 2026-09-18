package legend.game.wmap.preset;

import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RawTag;
import legend.core.tags.StringTag;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorldMapDestinationTagCodecTest {
  @Test
  void opaqueSpawnDataRoundTripsNestedTypesAndEscapedText() throws Exception {
    final MapTag data = new MapTag();
    data.set("spawn", new StringTag("north<&\"gate"));
    final ListTag values = new ListTag();
    values.add(new IntTag(17));
    values.add(new StringTag("CUSTOM_SPAWN"));
    values.add(new RawTag(new byte[]{0, -1, 42}));
    data.set("values", values);
    final Element xml = element();
    WorldMapDestinationTagCodec.write(xml, data);
    final MapTag decoded = WorldMapDestinationTagCodec.read(xml).asMap();
    assertEquals("north<&\"gate", decoded.get("spawn").asString().get());
    assertEquals(17, decoded.get("values").asList().get(0).asInt().get());
    assertEquals("CUSTOM_SPAWN", decoded.get("values").asList().get(1).asString().get());
    assertArrayEquals(new byte[]{0, -1, 42}, decoded.get("values").asList().get(2).asRaw().get());
  }

  @Test
  void duplicateMapKeysAndNonFiniteNumbersAreRejected() throws Exception {
    final Element xml = element();
    xml.setAttribute("type", "map");
    for(int i = 0; i < 2; i++) {
      final Element child = xml.getOwnerDocument().createElement("entry");
      child.setAttribute("key", "spawn");
      child.setAttribute("type", "int");
      child.setAttribute("value", "1");
      xml.appendChild(child);
    }
    assertThrows(IllegalArgumentException.class, () -> WorldMapDestinationTagCodec.read(xml));
    assertThrows(IllegalArgumentException.class, () -> WorldMapDestinationTagCodec.write(element(), new FloatTag(Float.NaN)));
  }

  @Test
  void deeplyNestedPayloadCannotExhaustTheStack() throws Exception {
    final Element xml = element();
    Element child = xml;
    for(int i = 0; i < 40; i++) {
      child.setAttribute("type", "list");
      final Element next = xml.getOwnerDocument().createElement("entry");
      child.appendChild(next);
      child = next;
    }
    child.setAttribute("type", "string");
    child.setAttribute("value", "end");
    assertThrows(IllegalArgumentException.class, () -> WorldMapDestinationTagCodec.read(xml));
  }

  private static Element element() throws Exception {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("data");
  }
}
