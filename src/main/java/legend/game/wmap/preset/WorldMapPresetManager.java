package legend.game.wmap.preset;

import legend.game.modding.events.worldmap.WorldMapPresetsEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.world.WorldMapRegistrySnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.ModContainer;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.REGISTRIES;

/** File discovery and immutable, save-addressed campaign packages. Never mutates global registries. */
public final class WorldMapPresetManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(WorldMapPresetManager.class);
  public static final Path DIRECTORY = Path.of("config", "worldmaps");
  private static final long MAX_PACKAGE_BYTES = 256L * 1024 * 1024;
  private static final int MAX_FILE_BYTES = 64 * 1024 * 1024;
  private static final int MAX_FILES = 4096;

  private WorldMapPresetManager() { }

  public record Catalog(List<WorldMapPresetEntry> entries, List<String> errors) {
    public Catalog {
      entries = List.copyOf(entries);
      errors = List.copyOf(errors);
    }
  }

  public static Catalog discover() {
    final List<WorldMapPresetEntry> entries = new ArrayList<>();
    final List<String> errors = new ArrayList<>();
    entries.add(WorldMapPresetEntry.VANILLA);
    try {
      Files.createDirectories(DIRECTORY);
      try(final var files = Files.list(DIRECTORY)) {
        for(final Path candidate : files.filter(path -> !path.getFileName().toString().startsWith(".")).sorted().toList()) {
          try {
            final Path file;
            if(Files.isDirectory(candidate)) {
              try(final var contents = Files.walk(candidate)) {
                final List<Path> documents = contents.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".wmap")).sorted().toList();
                if(documents.isEmpty()) {
                  continue;
                }
                if(documents.size() != 1) {
                  throw new IOException("Expected one .wmap file in preset folder, found " + documents.size());
                }
                file = documents.getFirst();
              }
            } else if(Files.isRegularFile(candidate) && candidate.getFileName().toString().endsWith(".wmap")) {
              file = candidate;
            } else {
              continue;
            }
            final WorldMapPreset preset = readPackage(file);
            entries.add(new WorldMapPresetEntry(preset.id(), preset.name(), () -> preset, file));
          } catch(final RuntimeException | IOException e) {
            errors.add(candidate + ": " + e.getMessage());
          }
        }
      }
    } catch(final IOException e) {
      errors.add(DIRECTORY + ": " + e.getMessage());
    }

    for(final ModContainer mod : MODS.getLoadedMods()) {
      // The manifest also works for loose IDE classpaths where JAR directory enumeration is unavailable.
      try {
        for(final String name : modPresetNames(mod)) {
          try {
            entries.add(new WorldMapPresetEntry(readModPackage(mod, name)));
          } catch(final IOException | RuntimeException e) {
            errors.add(mod.modId + '/' + name + ": " + e.getMessage());
          }
        }
      } catch(final Exception e) {
        errors.add(mod.modId + "/worldmaps: " + e.getMessage());
      }
    }

    EVENTS.postEvent(new WorldMapPresetsEvent(entries));
    final Set<RegistryId> ids = new HashSet<>();
    final List<WorldMapPresetEntry> unique = new ArrayList<>();
    unique.add(WorldMapPresetEntry.VANILLA);
    ids.add(WorldMapPresetEntry.VANILLA.id());
    for(final WorldMapPresetEntry entry : entries) {
      if(entry == WorldMapPresetEntry.VANILLA) {
        continue;
      }
      if(!ids.add(entry.id())) {
        errors.add("Duplicate world map preset ID: " + entry.id());
      } else {
        unique.add(entry);
      }
    }
    errors.forEach(error -> LOGGER.warn("World map preset: %s", error));
    return new Catalog(unique, errors);
  }

  private static boolean isPreset(final Path path) {
    final String name = path.getFileName().toString();
    return name.endsWith(".wmap") || name.endsWith(".wmap.zip");
  }

  private static List<String> modPresetNames(final ModContainer mod) throws Exception {
    final var index = mod.getResource("worldmaps/index.txt");
    if(index != null) {
      try(final var stream = index.openStream()) {
        final String manifest = new String(readBounded(stream, 65536), java.nio.charset.StandardCharsets.UTF_8);
        return manifest.lines().map(String::strip).filter(value -> !value.isEmpty() && !value.startsWith("#")).distinct().sorted().toList();
      }
    }
    final var resource = mod.getResource("worldmaps");
    if(resource != null && resource.getProtocol().equals("file")) {
      final Path root = Path.of(resource.toURI());
      try(final var files = Files.walk(root, 3)) {
        return files.filter(Files::isRegularFile).filter(WorldMapPresetManager::isPreset).map(path -> root.relativize(path).toString().replace('\\', '/')).sorted().toList();
      }
    }
    final var codeSource = mod.mod.getClass().getProtectionDomain().getCodeSource();
    if(codeSource != null) {
      final Path path = Path.of(codeSource.getLocation().toURI());
      if(Files.isRegularFile(path)) {
        final String prefix = mod.modId + "/worldmaps/";
        try(final JarFile jar = new JarFile(path.toFile())) {
          return jar.stream().filter(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix)).map(entry -> entry.getName().substring(prefix.length())).filter(name -> name.endsWith(".wmap") || name.endsWith(".wmap.zip")).sorted().toList();
        }
      }
    }
    return List.of();
  }

  /** Accept an editor XML with sibling assets, or a ZIP with root preset.wmap and its assets. */
  public static WorldMapPreset readPackage(final Path source) throws IOException {
    if(source.getFileName().toString().endsWith(".wmap.zip")) {
      try(final InputStream stream = Files.newInputStream(source)) {
        return unpack(stream);
      }
    }
    return WorldMapPresetCodec.read(source);
  }

  public static Path importPreset(final WorldMapPreset preset) throws IOException {
    final String token = freeze(preset, DIRECTORY);
    // A newer import replaces only older managed catalog documents with the same identity.
    // Their assets and all independent campaign snapshots remain available.
    try(final var directories = Files.list(DIRECTORY)) {
      for(final Path directory : directories.filter(Files::isDirectory).filter(path -> path.getFileName().toString().matches("[0-9a-f]{64}")).toList()) {
        final Path document = directory.resolve("preset.wmap");
        if(!directory.getFileName().toString().equals(token) && Files.isRegularFile(document) && WorldMapPresetCodec.read(document).id().equals(preset.id())) {
          Files.delete(document);
        }
      }
    }
    return DIRECTORY.resolve(token).resolve("preset.wmap");
  }

  /** Removes only the catalog document; campaign snapshots and package assets remain intact. */
  public static void delete(final WorldMapPresetEntry entry) throws IOException {
    if(entry.file() == null || !entry.file().toRealPath().startsWith(DIRECTORY.toRealPath())) {
      throw new IOException("Only local catalog presets can be removed");
    }
    Files.delete(entry.file());
  }

  private static WorldMapPreset readModPackage(final ModContainer mod, final String name) throws IOException {
    requireRelative(name);
    final var url = mod.getResource("worldmaps/" + name);
    if(url == null) {
      throw new IOException("Missing mod resource " + name);
    }
    if(name.endsWith(".wmap.zip")) {
      try(final InputStream stream = url.openStream()) {
        return unpack(stream);
      }
    }
    if(!name.endsWith(".wmap")) {
      throw new IOException("Expected .wmap or .wmap.zip: " + name);
    }
    final Path staging = staging();
    try {
      try(final InputStream stream = url.openStream()) {
        Files.write(staging.resolve("preset.wmap"), readBounded(stream, MAX_FILE_BYTES));
      }
      final WorldMapPreset preset = WorldMapPresetCodec.read(staging.resolve("preset.wmap"));
      final int slash = name.lastIndexOf('/');
      final String prefix = slash < 0 ? "" : name.substring(0, slash + 1);
      long size = Files.size(staging.resolve("preset.wmap"));
      for(final String asset : preset.assetPaths()) {
        requireRelative(asset);
        final var resource = mod.getResource("worldmaps/" + prefix + asset);
        if(resource == null) {
          throw new IOException("Missing mod asset " + asset);
        }
        try(final InputStream stream = resource.openStream()) {
          final byte[] bytes = readBounded(stream, MAX_FILE_BYTES);
          size += bytes.length;
          requirePackageSize(size);
          final Path target = staging.resolve(asset);
          Files.createDirectories(target.getParent());
          Files.write(target, bytes);
        }
      }
      return cache(staging);
    } finally {
      deleteStaging(staging);
    }
  }

  private static WorldMapPreset unpack(final InputStream stream) throws IOException {
    final Path staging = staging();
    try {
      final Set<String> names = new HashSet<>();
      long size = 0;
      try(final ZipInputStream zip = new ZipInputStream(stream)) {
        for(var entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
          final String name = entry.getName();
          requireRelative(name);
          if(!entry.isDirectory() && name.toLowerCase(java.util.Locale.ROOT).endsWith(".wmap") && !name.equals("preset.wmap")) {
            throw new IOException("World map package must contain only root preset.wmap as its document");
          }
          if(!names.add(name.toLowerCase(java.util.Locale.ROOT)) || names.size() > MAX_FILES) {
            throw new IOException("Duplicate ZIP entry or too many files: " + name);
          }
          final Path target = staging.resolve(name).normalize();
          if(entry.isDirectory()) {
            Files.createDirectories(target);
          } else {
            final byte[] bytes = readBounded(zip, MAX_FILE_BYTES);
            size += bytes.length;
            requirePackageSize(size);
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
          }
          zip.closeEntry();
        }
      }
      if(!Files.isRegularFile(staging.resolve("preset.wmap"))) {
        throw new IOException("World map package requires preset.wmap at its root");
      }
      return cache(staging);
    } finally {
      deleteStaging(staging);
    }
  }

  private static WorldMapPreset cache(final Path staging) throws IOException {
    final WorldMapPreset preset = WorldMapPresetCodec.read(staging.resolve("preset.wmap"));
    final String token = freeze(preset, DIRECTORY.resolve(".cache"));
    return WorldMapPresetCodec.read(DIRECTORY.resolve(".cache").resolve(token).resolve("preset.wmap"));
  }

  public static void requireMods(final WorldMapPreset preset) {
    final Set<String> loaded = new HashSet<>();
    MODS.getLoadedMods().forEach(mod -> loaded.add(mod.modId));
    for(final String id : preset.requiredMods()) {
      if(!loaded.contains(id)) {
        throw new IllegalArgumentException("World map preset " + preset.id() + " requires enabled mod " + id);
      }
    }
  }

  /** Resolve everything before offering a preset for activation; providers still load asynchronously. */
  public static WorldMapRegistrySnapshot validate(@Nullable final WorldMapPreset preset) {
    if(preset != null) {
      requireMods(preset);
      for(final String path : preset.assetPaths()) {
        try {
          asset(preset.packageRoot(), path);
        } catch(final IOException e) {
          throw new IllegalArgumentException("Preset " + preset.id() + " asset " + path, e);
        }
      }
    }
    return preset == null ? WorldMapRegistrySnapshot.read(REGISTRIES) : WorldMapRegistrySnapshot.read(REGISTRIES, preset);
  }

  /** Store content and assets once. Save files retain this token even after the selected preset changes. */
  public static String store(final GameState52c state, @Nullable final WorldMapPreset preset) throws IOException {
    if(preset == null) {
      return "";
    }
    return freeze(preset, state.campaign.path.resolve("worldmaps"));
  }

  @Nullable
  public static WorldMapPreset load(final GameState52c state) {
    if(state.worldMapPreset.isEmpty()) {
      return null;
    }
    if(!state.worldMapPreset.matches("[0-9a-f]{64}")) {
      throw new IllegalArgumentException("Invalid saved world map package identity");
    }
    final Path path = state.campaign.path.resolve("worldmaps").resolve(state.worldMapPreset).resolve("preset.wmap");
    try {
      final WorldMapPreset preset = WorldMapPresetCodec.read(path);
      requireMods(preset);
      return preset;
    } catch(final IOException e) {
      throw new IllegalStateException("Saved world map package is missing or unreadable: " + path, e);
    }
  }

  /** Export the currently registered map, including mod overlays, as an editable blueprint. */
  public static Path exportRegistered() throws IOException {
    Files.createDirectories(DIRECTORY);
    final Path path = DIRECTORY.resolve("registered-world-map.wmap");
    WorldMapPresetCodec.write(WorldMapPreset.export(REGISTRIES, new RegistryId("sc", "registered_world_map"), "Registered world map", "Export of currently enabled WMAP registries"), path);
    return path;
  }

  public static Path exportVanilla() throws IOException {
    Files.createDirectories(DIRECTORY);
    final Path path = DIRECTORY.resolve("VanillaWorldMap.wmap");
    WorldMapPresetCodec.write(WorldMapPreset.vanilla(), path);
    return path;
  }

  private static String freeze(final WorldMapPreset preset, final Path directory) throws IOException {
    if(preset.assetPaths().size() >= MAX_FILES) {
      throw new IOException("World map package contains too many assets");
    }
    Files.createDirectories(directory);
    final Path staging = Files.createTempDirectory(directory, ".package-");
    try {
      WorldMapPresetCodec.write(preset, staging.resolve("preset.wmap"));
      long size = Files.size(staging.resolve("preset.wmap"));
      for(final String name : preset.assetPaths()) {
        final Path source = asset(preset.packageRoot(), name);
        size += Files.size(source);
        requirePackageSize(size);
        final Path target = staging.resolve(name);
        if(name.equals("preset.wmap")) {
          throw new IOException("An asset cannot replace preset.wmap");
        }
        Files.createDirectories(target.getParent());
        Files.copy(source, target);
      }
      final String token = digest(staging);
      final Path target = directory.resolve(token);
      if(!Files.exists(target)) {
        try {
          Files.move(staging, target, StandardCopyOption.ATOMIC_MOVE);
        } catch(final AtomicMoveNotSupportedException e) {
          Files.move(staging, target);
        }
      } else if(!Files.exists(target.resolve("preset.wmap"))) {
        Files.move(staging.resolve("preset.wmap"), target.resolve("preset.wmap"));
      }
      return token;
    } finally {
      deleteStaging(staging);
    }
  }

  private static String digest(final Path directory) throws IOException {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      try(final var files = Files.walk(directory)) {
        for(final Path file : files.filter(Files::isRegularFile).sorted().toList()) {
          final byte[] name = directory.relativize(file).toString().replace('\\', '/').getBytes(java.nio.charset.StandardCharsets.UTF_8);
          digest.update(java.nio.ByteBuffer.allocate(4).putInt(name.length).array());
          digest.update(name);
          digest.update(java.nio.ByteBuffer.allocate(8).putLong(Files.size(file)).array());
          try(final InputStream stream = Files.newInputStream(file)) {
            final byte[] buffer = new byte[8192];
            for(int length = stream.read(buffer); length != -1; length = stream.read(buffer)) {
              digest.update(buffer, 0, length);
            }
          }
        }
      }
      return HexFormat.of().formatHex(digest.digest());
    } catch(final NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private static Path staging() throws IOException {
    final Path cache = DIRECTORY.resolve(".cache");
    Files.createDirectories(cache);
    return Files.createTempDirectory(cache, ".import-");
  }

  private static Path asset(final Path root, final String name) throws IOException {
    return WorldMapPresetAssets.resolve(root, name);
  }

  private static void requireRelative(final String name) throws IOException {
    if(name.isBlank() || name.contains("\\") || name.contains(":") || Path.of(name).isAbsolute()) {
      throw new IOException("Expected a relative package path: " + name);
    }
    for(final Path part : Path.of(name)) {
      if(part.toString().equals("..") || part.toString().equals(".")) {
        throw new IOException("Package path cannot traverse directories: " + name);
      }
    }
  }

  private static byte[] readBounded(final InputStream stream, final int limit) throws IOException {
    final byte[] bytes = stream.readNBytes(limit + 1);
    if(bytes.length > limit) {
      throw new IOException("World map package file exceeds " + limit + " bytes");
    }
    return bytes;
  }

  private static void requirePackageSize(final long size) throws IOException {
    if(size > MAX_PACKAGE_BYTES) {
      throw new IOException("World map package exceeds 256 MiB");
    }
  }

  private static void deleteStaging(final Path directory) throws IOException {
    if(Files.exists(directory)) {
      try(final var paths = Files.walk(directory)) {
        for(final Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
          Files.delete(path);
        }
      }
    }
  }
}
