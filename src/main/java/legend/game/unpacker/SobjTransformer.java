package legend.game.unpacker;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

public final class SobjTransformer {
  private SobjTransformer() {}

  private static final Logger LOGGER = LogManager.getFormatterLogger(SobjTransformer.class);

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    final XXH3State xxhash = XXHash.XXH3_createState();

    if(xxhash == null) {
      LOGGER.log(Level.ERROR, "Failed to initialize hash state. Skipping");

      return;
    }

    final Long2ObjectArrayMap<Model> models = new Long2ObjectArrayMap<>();

    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      final int mrgEntries = countMrg(drgn);

      final ByteBuffer buffer = BufferUtils.createByteBuffer(40968);

      for(int map = 5; map < mrgEntries; map += 3) {
        final PathNode folder = drgn.children.get(Integer.toString(map));

        if(folder == null) {
          continue;
        }

        final int modelAndAnimCount = countMrg(folder) - 3;

        for(int modelIndex = 0; modelIndex < modelAndAnimCount; modelIndex += 33) {
          final PathNode model = folder.children.get(Integer.toString(modelIndex));

          if(model != null) {
            XXHash.XXH3_64bits_reset(xxhash);

            buffer.clear();
            buffer.put(model.data.data, model.data.offset, Math.min(model.data.size(), 40968));
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long modelHash = XXHash.XXH3_64bits_digest(xxhash);

            if (!models.containsKey(modelHash)) {
              models.put(modelHash, new Model(Long.toHexString(modelHash), model.data));
            }

            final PathNode textures = folder.children.get("textures");

            if(textures != null) {
              final PathNode texture = textures.children.get(Integer.toString(modelIndex / 33));

              if(texture != null) {
                XXHash.XXH3_64bits_reset(xxhash);

                buffer.clear();
                buffer.put(texture.data.data, texture.data.offset, Math.min(texture.data.size(), 40968));
                buffer.flip();

                XXHash.XXH3_64bits_update(xxhash, buffer);

                final long textureHash = XXHash.XXH3_64bits_digest(xxhash);

                final Model m = models.get(modelHash);
                if (!m.textures.containsKey(textureHash)) {
                  m.textures.put(textureHash, new Sobj(Long.toHexString(textureHash), texture.data));
                }
              }
            }

            for(int animationIndex = 1; animationIndex <= 32; animationIndex++) {
              final PathNode animation = folder.children.get(Integer.toString(modelIndex + animationIndex));

              if(animation != null && animation.data.real()) {
                XXHash.XXH3_64bits_reset(xxhash);

                buffer.clear();
                buffer.put(animation.data.data, animation.data.offset, Math.min(animation.data.size(), 40968));
                buffer.flip();

                XXHash.XXH3_64bits_update(xxhash, buffer);

                final long animationHash = XXHash.XXH3_64bits_digest(xxhash);

                final Model m = models.get(modelHash);
                if (!m.animations.containsKey(animationHash)) {
                  m.animations.put(animationHash, new Sobj(Long.toHexString(animationHash), animation.data));
                }
              }
            }
          }
        }
      }
    }

    final PathNode sobjFolder = new PathNode("sobj", "sobj", null, root);

    root.addChild(sobjFolder);

    for(final Model entry : models.values()) {
      final PathNode folder = createChild(sobjFolder, entry.name, null);

      final PathNode model = createChild(folder, "model", entry.file);

      final PathNode textures = createChild(folder, "textures", null);

      for(final Sobj textureEntry : entry.textures.values()) {
        final PathNode texture = createChild(textures, textureEntry.name, textureEntry.file);
      }

      final PathNode animations = createChild(folder, "animations", null);

      for(final Sobj animationEntry : entry.animations.values()) {
        final PathNode animation = createChild(animations, animationEntry.name, animationEntry.file);
      }
    }
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


  private static class Model {
    final String name;
    final FileData file;
    final Long2ObjectArrayMap<Sobj> textures = new Long2ObjectArrayMap<>();
    final Long2ObjectArrayMap<Sobj> animations = new Long2ObjectArrayMap<>();

    Model(final String name, final FileData file) {
      this.name = name;
      this.file = file;
    }
  }

  private static class Sobj {
    String name;
    FileData file;

    Sobj(final String name, final FileData file) {
      this.name = name;
      this.file = file;
    }
  }
}
