package legend.game.unpacker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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
    final Long2ObjectArrayMap<String> namer = initNamer();
    final Map<Long, ReplacementModel> replacer = initReplacer();

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
            buffer.put(model.data.data, model.data.offset, model.data.size());
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long modelHash = XXHash.XXH3_64bits_digest(xxhash);

            if(!models.containsKey(modelHash)) {
              models.put(modelHash, new Model(model.data));
            }

            modelHashes[modelIndex] = modelHash;
            modelMap[modelIndex] = modelIndex;

            refArchive.files.get(modelIndex * 33).ref = resolveModelReference(modelHash, replacer, namer);
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

          if(modelMap[modelIndex] >= modelHashes.length) {
            continue;
          }

          final long modelHash = modelHashes[modelMap[modelIndex]];
          @Nullable final ReplacementModel replacementModel = replacer.get(modelHash);

          if(texture != null) {
            XXHash.XXH3_64bits_reset(xxhash);

            buffer.clear();
            buffer.put(texture.data.data, texture.data.offset, texture.data.size());
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long textureHash = XXHash.XXH3_64bits_digest(xxhash);

            final Model m = models.get(modelHash);
            if(!m.textures.containsKey(textureHash)) {
              m.textures.put(textureHash, texture.data);
            }

            textureRefArchive.files.get(modelIndex).ref = resolveTextureReference(modelHash, replacementModel, textureHash, namer);
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

              final Model m = models.get(modelHash);

              if(!m.animations.containsKey(animationHash)) {
                m.animations.put(animationHash, animation.data);
              }

              refArchive.files.get(baseIndex + animationIndex).ref = resolveAnimationReference(modelHash, replacementModel, animationHash, namer);
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
      final String name = namer.getOrDefault(key, String.format("%016x", key));
      final PathNode folder = createChild(sobjFolder, name, null);

      final PathNode model = createChild(folder, "model", entry.getValue().file);

      final PathNode textures = createChild(folder, "textures", null);

      for(final var textureEntry : entry.getValue().textures.long2ObjectEntrySet()) {
        final long textureKey = textureEntry.getLongKey();
        final String textureName = namer.getOrDefault(textureKey, String.format("%016x", textureKey));
        final PathNode texture = createChild(textures, textureName, textureEntry.getValue());
      }

      final PathNode animations = createChild(folder, "animations", null);

      for(final var animationEntry : entry.getValue().animations.long2ObjectEntrySet()) {
        final long animationKey = animationEntry.getLongKey();
        final String animationName = namer.getOrDefault(animationKey, String.format("%016x", animationKey));
        final PathNode animation = createChild(animations, animationName, animationEntry.getValue());
      }
    }
  }

  private static class ReplacementConfig {
    Map<Long, ReplacementModel> replacer = new HashMap<>();

    public Map<Long, ReplacementModel> getReplacer() {
      return this.replacer;
    }

    public void setReplacer(final Map<Long, ReplacementModel> replacer) {
      this.replacer = replacer;
    }
  }

  private static class HexLongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(final JsonParser p, DeserializationContext ctxt) throws IOException {

      final String text = p.getText().trim();

      if (text.startsWith("0x") || text.startsWith("0X")) {
        return Long.parseUnsignedLong(text.substring(2), 16);
      }

      return Long.parseLong(text);
    }
  }

  private static  class HexLongKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(final String key, final DeserializationContext ctxt) {

      if (key.startsWith("0x") || key.startsWith("0X")) {
        return Long.parseUnsignedLong(key.substring(2), 16);
      }

      return Long.parseLong(key);
    }
  }

  private static Map<Long, ReplacementModel> initReplacer() {
    try {
      SimpleModule module = new SimpleModule();

      module.addKeyDeserializer(Long.class, new HexLongKeyDeserializer());

      final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      mapper.registerModule(module);

      final ReplacementConfig config = mapper.readValue(Path.of(".", "sob_replacer.yaml").toFile(), ReplacementConfig.class);

      return config.replacer;
    } catch(final IOException e) {
      LOGGER.error("Failed to initialize model replacer. Skipping...");
    }

    return new HashMap<>();
  }

  private static Long2ObjectArrayMap<String> initNamer() {
    final Long2ObjectArrayMap<String> namer = new Long2ObjectArrayMap<>();

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

        namer.put(hash, name);
      });
    } catch(final IOException e) {
      LOGGER.log(Level.ERROR, "Failed to load names file. Skipping naming.", e);
    }

    return namer;
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

  private static class ReplacementAsset {
    @JsonDeserialize(using = HexLongDeserializer.class)
    long modelHash;
    @JsonDeserialize(using = HexLongDeserializer.class)
    long assetHash;
  }

  private static class ReplacementModel {
    @JsonDeserialize(using = HexLongDeserializer.class)
    long replacement;

    Map<Long, ReplacementAsset> textures = new HashMap<>();
    Map<Long, ReplacementAsset> animations = new HashMap<>();

    public Map<Long, ReplacementAsset> getTextures() {
      return this.textures;
    }

    public void setTextures(final Map<Long, ReplacementAsset> textures) {
      this.textures = textures;
    }

    public Map<Long, ReplacementAsset> getAnimations() {
      return this.animations;
    }

    public void setAnimations(final Map<Long, ReplacementAsset> animations) {
      this.animations = animations;
    }
  }

  private static String resolveModelReference(final long hash, final Map<Long, ReplacementModel> replacer, final Long2ObjectArrayMap<String> namer) {
    final ReplacementModel model = replacer.get(hash);

    final long resolvedHash = model != null
      ? model.replacement
      : hash;

    final String name = namer.getOrDefault(resolvedHash, String.format("%016x", resolvedHash));
    return "/sobj/" + name + "/model";
  }

  private static String resolveTextureReference(final long modelHash, @Nullable final ReplacementModel replacementModel, final long textureHash, final Long2ObjectArrayMap<String> namer) {
    final ReplacementAsset texture = replacementModel != null
      ? replacementModel.textures.get(textureHash)
      : null;

    final long resolvedModelHash = texture != null
      ? texture.modelHash
      : modelHash;

    final long resolvedTextureHash = texture != null
      ? texture.assetHash
      : textureHash;

    final String modelName = namer.getOrDefault(resolvedModelHash, String.format("%016x", resolvedModelHash));
    final String textureName = namer.getOrDefault(resolvedTextureHash, String.format("%016x", resolvedTextureHash));

    return "/sobj/" + modelName + "/textures/" + textureName;
  }

  private static String resolveAnimationReference(final long modelHash, @Nullable final ReplacementModel replacementModel, final long animationHash, final Long2ObjectArrayMap<String> namer) {
    final ReplacementAsset texture = replacementModel != null
      ? replacementModel.animations.get(animationHash)
      : null;

    final long resolvedModelHash = texture != null
      ? texture.modelHash
      : modelHash;

    final long resolvedTextureHash = texture != null
      ? texture.assetHash
      : animationHash;

    final String modelName = namer.getOrDefault(resolvedModelHash, String.format("%016x", resolvedModelHash));
    final String animationName = namer.getOrDefault(resolvedTextureHash, String.format("%016x", resolvedTextureHash));

    return "/sobj/" + modelName + "/animations/" + animationName;
  }
}
