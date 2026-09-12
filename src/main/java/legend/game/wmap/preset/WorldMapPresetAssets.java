package legend.game.wmap.preset;

import legend.game.tim.Tim;
import legend.game.tmd.TmdWithId;
import legend.game.types.CContainer;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.game.wmap.world.WorldMapAvatarAssets;
import legend.game.wmap.world.WorldMapModelAssets;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Confined file assets are parsed on a worker; GPU work belongs to existing WMAP adoption. */
public final class WorldMapPresetAssets {
  public static final int MAX_ASSET_BYTES = 64 * 1024 * 1024;

  private WorldMapPresetAssets() { }

  public static void validateRelative(final String relative) {
    if(relative == null || relative.isBlank() || relative.indexOf('\0') >= 0 || relative.indexOf(':') >= 0 || relative.indexOf('\\') >= 0) {
      throw new IllegalArgumentException("Invalid preset asset path: " + relative + "; use portable relative paths with / separators");
    }
    final Path path = Path.of(relative);
    if(path.isAbsolute()) throw new IllegalArgumentException("Absolute preset asset path: " + relative);
    for(final Path part : path) {
      if(part.toString().equals("..") || part.toString().equals(".")) throw new IllegalArgumentException("Preset asset path escapes package: " + relative);
    }
  }

  public static Path resolve(final Path packageRoot, final String relative) throws IOException {
    validateRelative(relative);
    final Path root = packageRoot.toRealPath();
    final Path path = root.resolve(relative).normalize().toRealPath();
    if(!path.startsWith(root) || !Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
      throw new IOException("Preset asset is outside package or not a regular file: " + relative);
    }
    if(Files.size(path) > MAX_ASSET_BYTES) throw new IOException("Preset asset exceeds " + MAX_ASSET_BYTES + " bytes: " + relative);
    return path;
  }

  public static byte[] read(final Path packageRoot, final String relative) throws IOException {
    final Path path = resolve(packageRoot, relative);
    try(final InputStream input = Files.newInputStream(path, LinkOption.NOFOLLOW_LINKS)) {
      final byte[] bytes = input.readNBytes(MAX_ASSET_BYTES + 1);
      if(bytes.length > MAX_ASSET_BYTES) throw new IOException("Preset asset exceeds limit: " + relative);
      return bytes;
    }
  }

  private static FileData data(final Path root, final String path) throws IOException {
    return new FileData(read(root, path));
  }

  static CompletableFuture<WorldMapModelAssets> region(final Path root, final RegistryId id, final WorldMapPreset.RegionAssets spec) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final TmdWithId model = new TmdWithId(id.toString(), data(root, spec.model()));
        final List<Tim> textures = new ArrayList<>();
        for(final String texture : spec.textures()) textures.add(new Tim(data(root, texture)));
        return new WorldMapModelAssets(model, textures, null, spec.retailAnimations());
      } catch(final IOException | RuntimeException failure) {
        throw new CompletionException("Failed loading preset region " + id + " from " + root, failure);
      }
    });
  }

  static CompletableFuture<WorldMapAvatarAssets> avatar(final Path root, final RegistryId id, final WorldMapPreset.AvatarAssets spec) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final CContainer model = new CContainer(id.toString(), data(root, spec.model()));
        final Tim texture = spec.texture() == null ? null : new Tim(data(root, spec.texture()));
        final List<TmdAnimationFile> animations = new ArrayList<>();
        for(final String animation : spec.animations()) animations.add(new TmdAnimationFile(data(root, animation)));
        return new WorldMapAvatarAssets(model, texture, animations, spec.scale(), spec.shadowScale(), spec.idleAnimation(),
          spec.walkAnimation(), spec.runAnimation(), spec.textureSlot());
      } catch(final IOException | RuntimeException failure) {
        throw new CompletionException("Failed loading preset avatar " + id + " from " + root, failure);
      }
    });
  }
}
