package legend.game.unpacker;

import it.unimi.dsi.fastutil.longs.Long2LongArrayMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class SobjTransformer {
  private SobjTransformer() {}

  private static final Logger LOGGER = LogManager.getFormatterLogger(SobjTransformer.class);

  private static final int MAX_SIZE = 84580;

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    final XXH3State xxhash = XXHash.XXH3_createState();

    if(xxhash == null) {
      LOGGER.log(Level.ERROR, "Failed to initialize hash state. Skipping");

      return;
    }

    final Long2ObjectArrayMap<Model> models = new Long2ObjectArrayMap<>();
    final Long2ObjectArrayMap<String> nameMap = hashNameMap();
    final Long2LongArrayMap replacer = replacerMap();

    final ByteBuffer buffer = BufferUtils.createByteBuffer(MAX_SIZE);

    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      final int mrgEntries = countMrg(drgn);

      for(int map = 5; map < mrgEntries; map += 3) {
        final PathNode folder = drgn.children.get(Integer.toString(map));

        if(folder == null) {
          continue;
        }

        final PathNode textures = folder.children.get("textures");

        if(textures == null) {
          continue;
        }

        final PathNode mrg = folder.children.get("mrg");
        final RefArchive refArchive = new RefArchive(mrg);

        final PathNode textureMrg = textures.children.get("mrg");
        final RefArchive textureRefArchive = new RefArchive(textureMrg);

        final int[] modelMap = parseModels(refArchive);
        final long[] modelHashes = new long[modelMap.length];

        for(int modelIndex = 0; modelIndex < modelMap.length; modelIndex++) {
          final PathNode model = folder.children.get(Integer.toString(modelIndex * 33));

          if(model != null) {
            XXHash.XXH3_64bits_reset(xxhash);

            buffer.clear();
            buffer.put(model.data.data, model.data.offset, Math.min(model.data.size(), MAX_SIZE));
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long modelHash = XXHash.XXH3_64bits_digest(xxhash);

            if(!models.containsKey(modelHash)) {
              models.put(modelHash, new Model(model.data));
            }

            modelHashes[modelIndex] = modelHash;
            modelMap[modelIndex] = modelIndex;

            final long replacedModelHash = replacer.getOrDefault(modelHash, modelHash);
            final String name = nameMap.getOrDefault(replacedModelHash, Long.toHexString(replacedModelHash));
            refArchive.files.get(modelIndex * 33).ref = "/sobj/" + name + "/model";
            folder.children.remove(model.pathSegment);
          } else {
            // Linked to a model file (luckily this is always the case for virtual models)
            if(modelMap[modelIndex] % 33 == 0) {
              modelMap[modelIndex] /= 33;
            }
          }
        }

        for(int modelIndex = 0; modelIndex < modelMap.length; modelIndex++) {
          final PathNode texture = textures.children.get(Integer.toString(modelIndex));

          if(texture != null) {
            XXHash.XXH3_64bits_reset(xxhash);

            buffer.clear();
            buffer.put(texture.data.data, texture.data.offset, Math.min(texture.data.size(), MAX_SIZE));
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long textureHash = XXHash.XXH3_64bits_digest(xxhash);

            final long modelHash = modelHashes[modelMap[modelIndex]];
            final Model m = models.get(modelHash);
            if(!m.textures.containsKey(textureHash)) {
              m.textures.put(textureHash, texture.data);
            }


            final long replacedModelHash = replacer.getOrDefault(modelHash, modelHash);
            final String modelName = nameMap.getOrDefault(replacedModelHash, Long.toHexString(replacedModelHash));

            final long replacedTextureHash;
            if(modelHash == replacedModelHash) {
              replacedTextureHash = textureHash;
            } else {
              replacedTextureHash = replacer.getOrDefault(textureHash, textureHash);
            }
            final String textureName = nameMap.getOrDefault(replacedTextureHash, Long.toHexString(replacedTextureHash));

            textureRefArchive.files.get(modelIndex).ref = "/sobj/" + modelName + "/textures/" + textureName;
            textures.children.remove(texture.pathSegment);
          }

          final int baseIndex = modelIndex * 33;
          for(int animationIndex = 1; animationIndex <= 32; animationIndex++) {
            final PathNode animation = folder.children.get(Integer.toString(baseIndex + animationIndex));

            if(animation != null) {
              XXHash.XXH3_64bits_reset(xxhash);

              buffer.clear();
              buffer.put(animation.data.data, animation.data.offset, Math.min(animation.data.size(), MAX_SIZE));
              buffer.flip();

              XXHash.XXH3_64bits_update(xxhash, buffer);

              final long animationHash = XXHash.XXH3_64bits_digest(xxhash);

              final Model m = models.get(modelHashes[modelMap[modelIndex]]);

              if(!m.animations.containsKey(animationHash)) {
                m.animations.put(animationHash, animation.data);
              }

              // We're referencing the original model file here. It's a little janky, but works for the models we're targeting
              final long modelHash = modelHashes[modelMap[modelIndex]];
              final String modelName = nameMap.getOrDefault(modelHash, Long.toHexString(modelHash));
              final long replacedAnimationHash = replacer.getOrDefault(animationHash, animationHash);
              final String animationName = nameMap.getOrDefault(replacedAnimationHash, Long.toHexString(replacedAnimationHash));
              refArchive.files.get(baseIndex + animationIndex).ref = "/sobj/" + modelName + "/animations/" + animationName;
              folder.children.remove(animation.pathSegment);
            }
          }
        }

        folder.children.remove("mrg");
        final PathNode folderRef = createChild(folder, "ref", refArchive.toFileData());
        textures.children.remove("mrg");
        final PathNode texturesRef = createChild(textures, "ref", textureRefArchive.toFileData());
      }
    }

    final PathNode sobjFolder = new PathNode("sobj", "sobj", null, root);

    root.addChild(sobjFolder);

    for(final var entry : models.long2ObjectEntrySet()) {

      final long key = entry.getLongKey();
      final String name = nameMap.getOrDefault(key, Long.toHexString(key));
      final PathNode folder = createChild(sobjFolder, name, null);

      final PathNode model = createChild(folder, "model", entry.getValue().file);

      final PathNode textures = createChild(folder, "textures", null);

      for(final var textureEntry : entry.getValue().textures.long2ObjectEntrySet()) {
        final long textureKey = textureEntry.getLongKey();
        final String textureName = nameMap.getOrDefault(textureKey, Long.toHexString(textureKey));
        final PathNode texture = createChild(textures, textureName, textureEntry.getValue());
      }

      final PathNode animations = createChild(folder, "animations", null);

      for(final var animationEntry : entry.getValue().animations.long2ObjectEntrySet()) {
        final long animationKey = animationEntry.getLongKey();
        final String animationName = nameMap.getOrDefault(animationKey, Long.toHexString(animationKey));
        final PathNode animation = createChild(animations, animationName, animationEntry.getValue());
      }
    }
  }

  private static Long2LongArrayMap replacerMap() {
    final Long2LongArrayMap replacerMap = new Long2LongArrayMap();

    try (final BufferedReader reader = Files.newBufferedReader(Path.of(".","sobj_replacer.txt"))) {
      reader.lines().forEach(line -> {
        line = line.trim();

        if (line.isEmpty() || line.startsWith("#")) {
          return;
        }

        // Remove trailing comments
        final int commentIndex = line.indexOf('#');
        if (commentIndex >= 0) {
          line = line.substring(0, commentIndex).trim();
        }

        final String[] parts = line.split("=");

        if (parts.length != 2) {
          return;
        }

        final long base = Long.parseUnsignedLong(parts[0].trim(), 16);
        final long replacement = Long.parseUnsignedLong(parts[1].trim(), 16);

        replacerMap.put(base, replacement);
      });
    } catch(final IOException e) {
      LOGGER.log(Level.ERROR, "Failed to load replacements file. Skipping replacements.", e);
    }

    return replacerMap;
  }

  private static Long2ObjectArrayMap<String> hashNameMap() {
    final Long2ObjectArrayMap<String> nameMap = new Long2ObjectArrayMap<>();

    try (final BufferedReader reader = Files.newBufferedReader(Path.of(".","sobj_namer.txt"))) {
      reader.lines().forEach(line -> {
        line = line.trim();

        if (line.isEmpty() || line.startsWith("#")) {
          return;
        }

        // Remove trailing comments
        final int commentIndex = line.indexOf('#');
        if (commentIndex >= 0) {
          line = line.substring(0, commentIndex).trim();
        }

        final String[] parts = line.split("=");

        if (parts.length != 2) {
          return;
        }

        final long hash = Long.parseUnsignedLong(parts[0].trim(), 16);
        final String name = parts[1].trim();

        nameMap.put(hash, name);
      });
    } catch(final IOException e) {
      LOGGER.log(Level.ERROR, "Failed to load names file. Skipping naming.", e);
    }

    return nameMap;
  }

  private static PathNode createChild(final PathNode parent, final String segment, final FileData data) {
    final PathNode node = new PathNode(parent.fullPath + '/' + segment, segment, data, parent);

    parent.addChild(node);

    return node;
  }

  private static int countMrg(final PathNode parent) {
    final PathNode mrg = parent.children.get("mrg");

    if(mrg == null) {
      return 0;
    }

    final String mrgContents = mrg.data.readFixedLengthAscii(0x0, mrg.data.size());

    return (int)mrgContents.lines().count();
  }

  private static int[] parseModels(final RefArchive refArchive) {
    final int modelCount = refArchive.files.size() / 33;

    final int[] out = new int[modelCount];

    for(int i = 0; i < modelCount; i++) {
      out[i] = Integer.parseInt(refArchive.files.get(i).ref) / 33;
    }

    return out;
  }


  private static class Model {
    final FileData file;
    final Long2ObjectArrayMap<FileData> textures = new Long2ObjectArrayMap<>();
    final Long2ObjectArrayMap<FileData> animations = new Long2ObjectArrayMap<>();

    Model(final FileData file) {
      this.file = file;
    }
  }
}
