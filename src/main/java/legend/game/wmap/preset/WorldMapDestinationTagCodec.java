package legend.game.wmap.preset;

import legend.core.tags.BoolTag;
import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.LongTag;
import legend.core.tags.MapTag;
import legend.core.tags.RawTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Base64;

/** Preset-safe typed data: no class names, reflection, or executable payloads. */
public final class WorldMapDestinationTagCodec {
  private WorldMapDestinationTagCodec() { }

  public static Tag read(final Element element) {
    return read(element, new Budget(), 0);
  }

  public static void write(final Element element, final Tag tag) {
    write(element, tag, new Budget(), 0);
  }

  private static Tag read(final Element element, final Budget budget, final int depth) {
    budget.node(depth);
    for(int i = 0; i < element.getAttributes().getLength(); i++) {
      final String name = element.getAttributes().item(i).getNodeName();
      if(!name.equals("type") && !name.equals("value") && !name.equals("key")) throw new IllegalArgumentException("Unknown destination tag attribute " + name);
    }
    final String type = element.getAttribute("type");
    if(type.equals("map") || type.equals("list")) {
      if(element.hasAttribute("value")) throw new IllegalArgumentException("Container destination tags cannot have a value");
      final MapTag map = new MapTag();
      final ListTag list = new ListTag();
      for(Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
        if(child instanceof final Element entry) {
          if(!entry.getTagName().equals("entry")) throw new IllegalArgumentException("Expected destination tag entry");
          if(type.equals("map")) {
            if(!entry.hasAttribute("key")) throw new IllegalArgumentException("Map entry requires a key");
            final String key = entry.getAttribute("key");
            budget.text(key);
            if(map.has(key)) throw new IllegalArgumentException("Duplicate destination tag key " + key);
            map.set(key, read(entry, budget, depth + 1));
          } else {
            if(entry.hasAttribute("key")) throw new IllegalArgumentException("List entry cannot have a key");
            list.add(read(entry, budget, depth + 1));
          }
        } else if(child.getNodeType() != Node.COMMENT_NODE && !child.getTextContent().isBlank()) {
          throw new IllegalArgumentException("Unexpected destination tag text");
        }
      }
      return type.equals("map") ? map : list;
    }
    if(!element.hasAttribute("value")) throw new IllegalArgumentException("Destination scalar requires a value");
    for(Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      if(child instanceof Element || child.getNodeType() != Node.COMMENT_NODE && !child.getTextContent().isBlank()) throw new IllegalArgumentException("Scalar destination tag cannot contain children");
    }
    final String value = element.getAttribute("value");
    budget.text(value);
    return switch(type) {
      case "string" -> new StringTag(value);
      case "int" -> new IntTag(Integer.parseInt(value));
      case "long" -> new LongTag(Long.parseLong(value));
      case "float" -> {
        final float number = Float.parseFloat(value);
        if(!Float.isFinite(number)) throw new IllegalArgumentException("Non-finite destination number");
        yield new FloatTag(number);
      }
      case "bool" -> {
        if(!value.equals("true") && !value.equals("false")) throw new IllegalArgumentException("Expected destination boolean");
        yield new BoolTag(Boolean.parseBoolean(value));
      }
      case "registry" -> {
        if(!value.matches("[a-z0-9_][a-z0-9_.-]*:[a-z0-9_][a-z0-9_./-]*")) throw new IllegalArgumentException("Invalid destination registry ID");
        final int separator = value.indexOf(':');
        yield new RegistryIdTag(new RegistryId(value.substring(0, separator), value.substring(separator + 1)));
      }
      // Older presets used "enum" for symbolic names without storing a Java enum class.
      case "enum" -> {
        if(!value.matches("[A-Za-z_$][A-Za-z0-9_$]*")) throw new IllegalArgumentException("Invalid symbolic name");
        yield new StringTag(value);
      }
      case "raw" -> new RawTag(Base64.getDecoder().decode(value));
      default -> throw new IllegalArgumentException("Unknown destination tag type " + type);
    };
  }

  private static void write(final Element element, final Tag tag, final Budget budget, final int depth) {
    budget.node(depth);
    if(tag instanceof final MapTag map) {
      element.setAttribute("type", "map");
      for(final String key : map.keys().stream().sorted().toList()) {
        budget.text(key);
        final Element entry = element.getOwnerDocument().createElement("entry");
        element.appendChild(entry);
        entry.setAttribute("key", key);
        write(entry, map.get(key), budget, depth + 1);
      }
      return;
    }
    if(tag instanceof final ListTag list) {
      element.setAttribute("type", "list");
      for(final Tag value : list) {
        final Element entry = element.getOwnerDocument().createElement("entry");
        element.appendChild(entry);
        write(entry, value, budget, depth + 1);
      }
      return;
    }
    final String type;
    final String value;
    switch(tag) {
      case StringTag text -> { type = "string"; value = text.get(); }
      case IntTag number -> { type = "int"; value = Integer.toString(number.get()); }
      case LongTag number -> { type = "long"; value = Long.toString(number.get()); }
      case FloatTag number -> {
        if(!Float.isFinite(number.get())) throw new IllegalArgumentException("Non-finite destination number");
        type = "float";
        value = Float.toString(number.get());
      }
      case BoolTag flag -> { type = "bool"; value = Boolean.toString(flag.get()); }
      case RegistryIdTag id -> { type = "registry"; value = id.get().toString(); }
      case RawTag raw -> {
        final byte[] bytes = raw.get();
        if(bytes.length > 786432) throw new IllegalArgumentException("Destination bytes exceed data budget");
        type = "raw";
        value = Base64.getEncoder().encodeToString(bytes);
      }
      default -> throw new IllegalArgumentException("Unsupported destination tag " + tag.getClass().getSimpleName());
    }
    budget.text(value);
    element.setAttribute("type", type);
    element.setAttribute("value", value);
  }

  private static final class Budget {
    private int nodes;
    private int characters;

    private void node(final int depth) {
      if(depth > 32 || ++this.nodes > 4096) throw new IllegalArgumentException("Destination data is too deeply nested or has too many entries");
    }

    private void text(final String value) {
      this.characters += value.length();
      if(this.characters > 1048576) throw new IllegalArgumentException("Destination data exceeds 1 MiB text budget");
    }
  }
}
