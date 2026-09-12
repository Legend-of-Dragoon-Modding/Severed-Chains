package legend.game.wmap.preset;

import legend.game.wmap.world.WorldMapAccess;
import legend.game.wmap.world.WorldMapCoolonDestination;
import legend.game.wmap.world.WorldMapEncounterPool;
import legend.game.wmap.world.WorldMapGeometry;
import legend.game.wmap.world.WorldMapNode;
import legend.game.wmap.world.WorldMapPlace;
import legend.game.wmap.world.WorldMapPolicy;
import legend.game.wmap.world.WorldMapPortal;
import legend.game.wmap.world.WorldMapRouteData;
import legend.game.wmap.world.WorldMapStoryPreset;
import legend.game.wmap.world.WorldMapTeleportLink;
import legend.game.wmap.world.WorldMapTravel;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/** Versioned UTF-8 XML. The fixed record allowlist defines the wire schema; XML never names Java classes. */
public final class WorldMapPresetCodec {
  public static final int VERSION = 1;
  public static final int MAX_DOCUMENT_BYTES = 16 * 1024 * 1024;

  private record Section<T>(String plural, String singular, Class<T> type,
                            Function<WorldMapPreset, Map<RegistryId, T>> values,
                            Function<WorldMapPreset.Builder, Map<RegistryId, T>> destination) { }

  private static final List<Section<?>> SECTIONS = List.of(
    new Section<>("nodes", "node", WorldMapNode.class, WorldMapPreset::nodes, builder -> builder.nodes),
    new Section<>("geometry", "geometry", WorldMapGeometry.class, WorldMapPreset::geometry, builder -> builder.geometry),
    new Section<>("places", "place", WorldMapPlace.class, WorldMapPreset::places, builder -> builder.places),
    new Section<>("routes", "route", WorldMapRouteData.class, WorldMapPreset::routes, builder -> builder.routes),
    new Section<>("portals", "portal", WorldMapPortal.class, WorldMapPreset::portals, builder -> builder.portals),
    new Section<>("encounterPools", "encounterPool", WorldMapEncounterPool.class, WorldMapPreset::encounterPools, builder -> builder.encounterPools),
    new Section<>("storyPresets", "storyPreset", WorldMapStoryPreset.class, WorldMapPreset::storyPresets, builder -> builder.storyPresets),
    new Section<>("coolonDestinations", "coolonDestination", WorldMapCoolonDestination.class, WorldMapPreset::coolonDestinations, builder -> builder.coolonDestinations),
    new Section<>("teleportLinks", "teleportLink", WorldMapTeleportLink.class, WorldMapPreset::teleportLinks, builder -> builder.teleportLinks),
    new Section<>("regions", "region", WorldMapPreset.Region.class, WorldMapPreset::regions, builder -> builder.regions),
    new Section<>("avatars", "avatar", WorldMapPreset.Avatar.class, WorldMapPreset::avatars, builder -> builder.avatars),
    new Section<>("traversalProfiles", "traversalProfile", WorldMapPreset.TraversalProfile.class, WorldMapPreset::traversalProfiles, builder -> builder.traversalProfiles),
    new Section<>("presentationProfiles", "presentationProfile", WorldMapPreset.PresentationProfile.class, WorldMapPreset::presentationProfiles, builder -> builder.presentationProfiles)
  );

  private static final Set<String> OPTIONAL = Set.of(
    "WorldMapPlace.name", "WorldMapPortal.route", "WorldMapPortal.place", "WorldMapPortal.region",
    "WorldMapRouteData.encounterPool", "WorldMapRouteData.avatar", "WorldMapStoryPreset.place",
    "WorldMapCameraSettings.overviewPosition", "WorldMapCameraSettings.minimum", "WorldMapCameraSettings.maximum",
    "Region.assets", "Avatar.provider", "Avatar.assets", "AvatarAssets.texture",
    "TraversalProfile.provider", "TraversalProfile.avatar", "TraversalProfile.visualOffset", "Warp.marker"
  );

  private WorldMapPresetCodec() { }

  public static WorldMapPreset read(final Path file) throws IOException {
    try(final InputStream input = Files.newInputStream(file)) {
      final byte[] bytes = input.readNBytes(MAX_DOCUMENT_BYTES + 1);
      return read(bytes, file.toAbsolutePath().normalize().getParent());
    } catch(final IOException | RuntimeException failure) {
      throw new IOException("Cannot read world map preset " + file + ": " + failure.getMessage(), failure);
    }
  }

  public static WorldMapPreset read(final byte[] bytes, final Path packageRoot) throws IOException {
    if(bytes.length > MAX_DOCUMENT_BYTES) throw new IOException("World map preset exceeds " + MAX_DOCUMENT_BYTES + " bytes");
    try {
      // Reject non-UTF-8 input instead of silently replacing malformed sequences.
      StandardCharsets.UTF_8.newDecoder().decode(java.nio.ByteBuffer.wrap(bytes));
      final var builder = factory().newDocumentBuilder();
      builder.setErrorHandler(new ErrorHandler() {
        @Override public void warning(final SAXParseException error) throws SAXParseException { throw error; }
        @Override public void error(final SAXParseException error) throws SAXParseException { throw error; }
        @Override public void fatalError(final SAXParseException error) throws SAXParseException { throw error; }
      });
      final Document document = builder.parse(new ByteArrayInputStream(bytes));
      if(document.getXmlEncoding() != null && !document.getXmlEncoding().equalsIgnoreCase("UTF-8")) {
        throw new IllegalArgumentException("Preset encoding must be UTF-8");
      }
      final Element root = document.getDocumentElement();
      if(!root.getTagName().equals("worldMapPreset")) throw error(root, "Expected worldMapPreset root");
      final Set<String> sections = new HashSet<>(Set.of("requiredMods", "rules", "behaviours", "removals", "thumbnails"));
      SECTIONS.forEach(section -> sections.add(section.plural));
      shape(root, Set.of("version", "id", "name", "description"), sections);
      if(!attribute(root, "version").equals(Integer.toString(VERSION))) throw error(root, "Unsupported schema version " + root.getAttribute("version"));
      final WorldMapPreset.Builder result = new WorldMapPreset.Builder(id(root, "id"), attribute(root, "name"))
        .description(root.getAttribute("description")).packageRoot(packageRoot);
      final Element mods = child(root, "requiredMods", false);
      if(mods != null) {
        shape(mods, Set.of(), Set.of("mod"));
        final Set<String> required = new LinkedHashSet<>();
        for(final Element mod : children(mods)) {
          shape(mod, Set.of("id"), Set.of());
          final String modId = attribute(mod, "id");
          if(!modId.matches("[a-z0-9_][a-z0-9_.-]*")) throw error(mod, "Invalid mod ID " + modId);
          if(!required.add(modId)) throw error(mod, "Duplicate required mod " + modId);
        }
        result.requiredMods(required);
      }
      for(final Section<?> section : SECTIONS) readSection(root, result, section);
      final Element removals = child(root, "removals", false);
      if(removals != null) {
        shape(removals, Set.of(), Set.of("remove"));
        for(final Element removal : children(removals)) {
          shape(removal, Set.of("kind", "id"), Set.of());
          if(!result.removals.add(new WorldMapPreset.Removal(attribute(removal, "kind"), id(removal, "id")))) throw error(removal, "Duplicate removal");
        }
      }
      final Element thumbnails = child(root, "thumbnails", false);
      if(thumbnails != null) {
        shape(thumbnails, Set.of(), Set.of("thumbnail"));
        for(final Element thumbnail : children(thumbnails)) {
          shape(thumbnail, Set.of("id", "texture"), Set.of());
          if(result.thumbnails.putIfAbsent(id(thumbnail, "id"), attribute(thumbnail, "texture")) != null) throw error(thumbnail, "Duplicate thumbnail");
        }
      }
      final Element rules = child(root, "rules", false);
      if(rules != null) result.rules(readRules(rules));
      final Element behaviours = child(root, "behaviours", false);
      if(behaviours != null) {
        shape(behaviours, Set.of(), Set.of("behaviour"));
        final Set<RegistryId> ids = new LinkedHashSet<>();
        for(final Element behaviour : children(behaviours)) {
          shape(behaviour, Set.of("id"), Set.of());
          if(!ids.add(id(behaviour, "id"))) throw error(behaviour, "Duplicate behaviour");
        }
        result.behaviours(ids);
      }
      final WorldMapPreset preset = result.build();
      preset.assetPaths();
      return preset;
    } catch(final Exception failure) {
      if(failure instanceof IOException io) throw io;
      throw new IOException("Invalid world map preset: " + failure.getMessage(), failure);
    }
  }

  private static DocumentBuilderFactory factory() throws Exception {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setAttribute("jdk.xml.maxElementDepth", "64");
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    return factory;
  }

  private static <T> void readSection(final Element root, final WorldMapPreset.Builder builder, final Section<T> section) throws Exception {
    final Element container = child(root, section.plural, false);
    if(container == null) return;
    shape(container, Set.of(), Set.of(section.singular));
    final Map<RegistryId, T> values = section.destination.apply(builder);
    for(final Element entry : children(container)) {
      final RegistryId id = id(entry, "id");
      if(values.putIfAbsent(id, readRecord(entry, section.type, true)) != null) throw error(entry, "Duplicate entry " + id);
    }
  }

  private static WorldMapPreset.Rules readRules(final Element rules) {
    shape(rules, Set.of("policy"), Set.of("capabilities", "portals"));
    final WorldMapPolicy policy = rules.hasAttribute("policy") ? WorldMapPolicy.valueOf(attribute(rules, "policy")) : null;
    final Map<WorldMapTravel.Capability, Boolean> capabilities = new LinkedHashMap<>();
    final Element capabilitySection = child(rules, "capabilities", false);
    if(capabilitySection != null) {
      shape(capabilitySection, Set.of(), Set.of("capability"));
      for(final Element entry : children(capabilitySection)) {
        shape(entry, Set.of("id", "allowed"), Set.of());
        final WorldMapTravel.Capability id = WorldMapTravel.Capability.valueOf(attribute(entry, "id"));
        if(capabilities.putIfAbsent(id, bool(entry, "allowed")) != null) throw error(entry, "Duplicate capability " + id);
      }
    }
    final Map<RegistryId, WorldMapAccess> portals = new LinkedHashMap<>();
    final Element portalSection = child(rules, "portals", false);
    if(portalSection != null) {
      shape(portalSection, Set.of(), Set.of("portal"));
      for(final Element entry : children(portalSection)) {
        shape(entry, Set.of("id", "code", "reason"), Set.of());
        final RegistryId id = id(entry, "id");
        final WorldMapAccess access = new WorldMapAccess(WorldMapAccess.Code.valueOf(attribute(entry, "code")), attribute(entry, "reason"));
        if(portals.putIfAbsent(id, access) != null) throw error(entry, "Duplicate portal rule " + id);
      }
    }
    return new WorldMapPreset.Rules(policy, capabilities, portals);
  }

  private static <T> T readRecord(final Element element, final Class<T> type, final boolean entry) throws Exception {
    final RecordComponent[] components = type.getRecordComponents();
    if(components == null) throw error(element, "Unsupported schema type " + type.getSimpleName());
    final Set<String> attributes = new HashSet<>();
    final Set<String> childNames = new HashSet<>();
    if(entry) attributes.add("id");
    for(final RecordComponent component : components) {
      (scalar(component.getType()) ? attributes : childNames).add(component.getName());
    }
    shape(element, attributes, childNames);
    final Object[] args = new Object[components.length];
    for(int i = 0; i < components.length; i++) {
      final RecordComponent component = components[i];
      final String name = component.getName();
      final boolean optional = OPTIONAL.contains(type.getSimpleName() + '.' + name);
      if(scalar(component.getType())) {
        args[i] = optional && !element.hasAttribute(name) ? null : scalarValue(element, name, component.getType());
      } else {
        final Element value = child(element, name, !optional);
        if(value == null) {
          args[i] = null;
        } else if(component.getGenericType() instanceof ParameterizedType parameterized) {
          shape(value, Set.of(), Set.of("item"));
          final Class<?> itemType = (Class<?>)parameterized.getActualTypeArguments()[0];
          final List<Object> items = new ArrayList<>();
          for(final Element item : children(value)) {
            if(scalar(itemType)) {
              final String attr = itemType == RegistryId.class ? "id" : "value";
              shape(item, Set.of(attr), Set.of());
              items.add(scalarValue(item, attr, itemType));
            } else {
              items.add(readRecord(item, itemType, false));
            }
          }
          if(component.getType() == Set.class) {
            final Set<Object> unique = new LinkedHashSet<>(items);
            if(unique.size() != items.size()) throw error(value, "Duplicate set item");
            args[i] = Set.copyOf(unique);
          } else {
            args[i] = List.copyOf(items);
          }
        } else {
          args[i] = readRecord(value, component.getType(), false);
        }
      }
    }
    try {
      return type.getDeclaredConstructor(Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new)).newInstance(args);
    } catch(final InvocationTargetException failure) {
      throw error(element, failure.getCause().getMessage());
    }
  }

  private static boolean scalar(final Class<?> type) {
    return type == String.class || type == RegistryId.class || type == int.class || type == Integer.class ||
      type == float.class || type == boolean.class || type.isEnum();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static Object scalarValue(final Element element, final String name, final Class<?> type) {
    final String value = attribute(element, name);
    try {
      if(type == String.class) return value;
      if(type == RegistryId.class) return id(element, name);
      if(type == int.class || type == Integer.class) return Integer.parseInt(value);
      if(type == boolean.class) return bool(element, name);
      if(type == float.class) {
        final float number = Float.parseFloat(value);
        if(!Float.isFinite(number)) throw new IllegalArgumentException("Non-finite number");
        return number;
      }
      if(type.isEnum()) return Enum.valueOf((Class)type, value);
      throw new IllegalArgumentException("Unsupported scalar");
    } catch(final IllegalArgumentException failure) {
      throw error(element, "Invalid " + name + "='" + value + "': " + failure.getMessage());
    }
  }

  private static RegistryId id(final Element element, final String name) {
    final String value = attribute(element, name);
    if(!value.matches("[a-z0-9_][a-z0-9_.-]*:[a-z0-9_][a-z0-9_./-]*")) throw error(element, "Invalid registry ID " + value);
    final int separator = value.indexOf(':');
    return new RegistryId(value.substring(0, separator), value.substring(separator + 1));
  }

  private static boolean bool(final Element element, final String name) {
    final String value = attribute(element, name);
    if(!value.equals("true") && !value.equals("false")) throw error(element, "Expected true or false for " + name);
    return Boolean.parseBoolean(value);
  }

  private static String attribute(final Element element, final String name) {
    if(!element.hasAttribute(name)) throw error(element, "Missing attribute " + name);
    return element.getAttribute(name);
  }

  private static Element child(final Element element, final String name, final boolean required) {
    Element found = null;
    for(final Element candidate : children(element)) {
      if(candidate.getTagName().equals(name)) {
        if(found != null) throw error(element, "Duplicate child " + name);
        found = candidate;
      }
    }
    if(found == null && required) throw error(element, "Missing child " + name);
    return found;
  }

  private static List<Element> children(final Element element) {
    final List<Element> result = new ArrayList<>();
    for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
      if(node instanceof Element child) {
        result.add(child);
      } else if(node.getNodeType() == Node.TEXT_NODE && !node.getTextContent().isBlank() || node.getNodeType() == Node.CDATA_SECTION_NODE || node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
        throw error(element, "Unexpected text or entity content");
      }
    }
    return result;
  }

  private static void shape(final Element element, final Set<String> attributes, final Set<String> children) {
    for(int i = 0; i < element.getAttributes().getLength(); i++) {
      final String name = element.getAttributes().item(i).getNodeName();
      if(!attributes.contains(name)) throw error(element, "Unknown attribute " + name);
    }
    for(final Element child : children(element)) {
      if(!children.contains(child.getTagName())) throw error(child, "Unknown element");
    }
  }

  private static IllegalArgumentException error(final Element element, final String message) {
    String context = element.getTagName();
    for(Node parent = element.getParentNode(); parent instanceof Element; parent = parent.getParentNode()) context = parent.getNodeName() + '/' + context;
    return new IllegalArgumentException(context + (element.hasAttribute("id") ? "[" + element.getAttribute("id") + "]" : "") + ": " + message);
  }

  public static void write(final WorldMapPreset preset, final Path file) throws IOException {
    final byte[] bytes = write(preset);
    final Path target = file.toAbsolutePath().normalize();
    Files.createDirectories(target.getParent());
    final Path temporary = Files.createTempFile(target.getParent(), ".wmap-", ".tmp");
    try {
      Files.write(temporary, bytes);
      try {
        Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch(final AtomicMoveNotSupportedException ignored) {
        Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
      }
    } finally {
      Files.deleteIfExists(temporary);
    }
  }

  public static byte[] write(final WorldMapPreset preset) throws IOException {
    try {
      preset.assetPaths();
      final Document document = factory().newDocumentBuilder().newDocument();
      final Element root = document.createElement("worldMapPreset");
      document.appendChild(root);
      root.setAttribute("version", Integer.toString(VERSION));
      root.setAttribute("id", preset.id().toString());
      root.setAttribute("name", preset.name());
      root.setAttribute("description", preset.description());
      final Element mods = append(root, "requiredMods");
      preset.requiredMods().stream().sorted().forEach(id -> append(mods, "mod").setAttribute("id", id));
      for(final Section<?> section : SECTIONS) writeSection(root, preset, section);
      if(!preset.removals().isEmpty()) {
        final Element removals = append(root, "removals");
        for(final WorldMapPreset.Removal removal : preset.removals().stream().sorted(java.util.Comparator.comparing(WorldMapPreset.Removal::kind).thenComparing(value -> value.id().toString())).toList()) {
          final Element entry = append(removals, "remove");
          entry.setAttribute("kind", removal.kind());
          entry.setAttribute("id", removal.id().toString());
        }
      }
      if(!preset.thumbnails().isEmpty()) {
        final Element thumbnails = append(root, "thumbnails");
        for(final RegistryId id : sorted(preset.thumbnails().keySet())) {
          final Element entry = append(thumbnails, "thumbnail");
          entry.setAttribute("id", id.toString());
          entry.setAttribute("texture", preset.thumbnails().get(id));
        }
      }
      final WorldMapPreset.Rules spec = preset.rules();
      final Element rules = append(root, "rules");
      if(spec.policy() != null) rules.setAttribute("policy", spec.policy().name());
      final Element capabilities = append(rules, "capabilities");
      spec.capabilities().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(value -> {
        final Element entry = append(capabilities, "capability");
        entry.setAttribute("id", value.getKey().name());
        entry.setAttribute("allowed", value.getValue().toString());
      });
      final Element portals = append(rules, "portals");
      for(final RegistryId id : sorted(spec.portals().keySet())) {
        final Element entry = append(portals, "portal");
        entry.setAttribute("id", id.toString());
        entry.setAttribute("code", spec.portals().get(id).code().name());
        entry.setAttribute("reason", spec.portals().get(id).reason());
      }
      if(preset.behaviours() != null) {
        final Element behaviours = append(root, "behaviours");
        sorted(preset.behaviours()).forEach(id -> append(behaviours, "behaviour").setAttribute("id", id.toString()));
      }
      final TransformerFactory factory = TransformerFactory.newInstance();
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      final var transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      transformer.transform(new DOMSource(document), new StreamResult(output));
      final byte[] bytes = output.toByteArray();
      if(bytes.length > MAX_DOCUMENT_BYTES) throw new IOException("World map preset exceeds document size limit");
      return bytes;
    } catch(final Exception failure) {
      if(failure instanceof IOException io) throw io;
      throw new IOException("Cannot encode world map preset " + preset.id() + ": " + failure.getMessage(), failure);
    }
  }

  private static <T> void writeSection(final Element root, final WorldMapPreset preset, final Section<T> section) throws Exception {
    final Map<RegistryId, T> values = section.values.apply(preset);
    if(values.isEmpty()) return;
    final Element container = append(root, section.plural);
    for(final RegistryId id : sorted(values.keySet())) {
      final Element entry = append(container, section.singular);
      entry.setAttribute("id", id.toString());
      writeRecord(entry, values.get(id));
    }
  }

  private static List<RegistryId> sorted(final Set<RegistryId> ids) {
    return ids.stream().sorted(java.util.Comparator.comparing(RegistryId::toString)).toList();
  }

  private static void writeRecord(final Element element, final Object record) throws Exception {
    for(final RecordComponent component : record.getClass().getRecordComponents()) {
      final Object value = component.getAccessor().invoke(record);
      if(value == null) continue;
      if(scalar(component.getType())) {
        element.setAttribute(component.getName(), value.toString());
      } else {
        final Element child = append(element, component.getName());
        if(value instanceof Iterable<?> iterable) {
          final List<Object> items = new ArrayList<>();
          iterable.forEach(items::add);
          if(value instanceof Set<?>) items.sort(java.util.Comparator.comparing(Object::toString));
          for(final Object item : items) {
            final Element itemElement = append(child, "item");
            if(scalar(item.getClass())) {
              itemElement.setAttribute(item instanceof RegistryId ? "id" : "value", item.toString());
            } else {
              writeRecord(itemElement, item);
            }
          }
        } else {
          writeRecord(child, value);
        }
      }
    }
  }

  private static Element append(final Element parent, final String name) {
    final Element element = parent.getOwnerDocument().createElement(name);
    parent.appendChild(element);
    return element;
  }
}
