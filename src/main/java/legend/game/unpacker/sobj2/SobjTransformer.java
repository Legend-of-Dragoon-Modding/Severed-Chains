package legend.game.unpacker.sobj2;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import java.util.Set;

public final class SobjTransformer {
  private SobjTransformer() {}

  private static final Logger LOGGER = LogManager.getFormatterLogger(legend.game.unpacker.sobj.SobjTransformer.class);

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    // TODO load names on partial unpack

    final Long2ObjectOpenHashMap<SobjModel> sobjModelMap = new Long2ObjectOpenHashMap<>();
    final DrgnSobjPack[] drgnSobjPacks = new DrgnSobjPack[4];
    final XXH3State xxhash = XXHash.XXH3_createState();
    if(xxhash == null) {
      throw new RuntimeException("Failed to create XXH3 state");
    }

    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgnNode = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgnNode == null) {
        continue;
      }

      final DrgnSobjPack sobjPack = new DrgnSobjPack();
      drgnSobjPacks[drgnIndex - 1] = sobjPack;
      final int entryCount = getFolderMrgCount(drgnNode);

      for(int sobjAssetPackIndex = 5; sobjAssetPackIndex < entryCount; sobjAssetPackIndex += 3) {
        sobjPack.addMap(sobjAssetPackIndex, processSobjFolder(drgnNode, sobjAssetPackIndex, drgnIndex, sobjModelMap, xxhash));
      }

      // TODO name
    }

    XXHash.XXH3_freeState(xxhash);

    moveUniqueNodes(sobjModelMap, transformations);

    generateSobjReferences(drgnSobjPacks, root, transformations);
  }

  private static Sobj[] processSobjFolder(final PathNode drgnNode, final int folderIndex, final int drgnIndex, final Long2ObjectOpenHashMap<SobjModel> sobjModels, final XXH3State xxhash) {
    final PathNode folderNode = drgnNode.children.get(Integer.toString(folderIndex));

    // TODO these should probably just return a reference to an empty Sobj[] to keep it slightly faster
    // Doesn't exist or isn't a folder
    if(folderNode == null || folderNode.data != null) {
      return new Sobj[0];
    }

    final PathNode texturesNode = folderNode.children.get("textures");

    // Doesn't exist or isn't a folder
    if(texturesNode == null || texturesNode.data != null) {
      return new Sobj[0];
    }

    final String[] sobjAssetsMgr = loadMrg(folderNode);
    final String[] sobjTextureMrg = loadMrg(texturesNode);

    final Sobj[] sobjs = new Sobj[sobjTextureMrg.length];
    final IntOpenHashSet modelAndAnimationDeletions = new IntOpenHashSet();
    final IntOpenHashSet textureDeletions = new IntOpenHashSet();

    for(int sobj = 0; sobj < sobjTextureMrg.length; sobj++) {
      final SobjModel model = processSobjModel(sobj, folderNode, sobjAssetsMgr, sobjModels, drgnIndex, folderIndex, modelAndAnimationDeletions, xxhash);

      final SobjTexture texture = processSobjTexture(sobj, texturesNode, sobjTextureMrg, model, drgnIndex, folderIndex, textureDeletions, xxhash);

      final int placeholderAnimationIndex = findPlaceholderAnimation(sobjAssetsMgr);

      final SobjAnimation[] animations = processSobjAnimations(sobj, folderNode, sobjAssetsMgr, model, placeholderAnimationIndex, drgnIndex, folderIndex, modelAndAnimationDeletions, xxhash);

      sobjs[sobj] = new Sobj(model, texture, animations);
    }

    deleteRedundantNodes(folderNode, texturesNode, modelAndAnimationDeletions, textureDeletions);

    return sobjs;
  }

  private static SobjModel processSobjModel(final int sobjIndex, final PathNode folderNode, final String[] mrg, final Long2ObjectOpenHashMap<SobjModel> sobjModels, final int drngIndex, final int folderIndex, final IntSet deletions, final XXH3State xxhash) {
    final int modelIndex = sobjIndex * 33;
    final int modelRealIndex = getRealFileIndex(mrg[modelIndex]);
    final PathNode modelNode = folderNode.children.get(Integer.toString(modelRealIndex));

    final long hash = generateHash(modelNode.data, xxhash);

    if(sobjModels.containsKey(hash)) {
      deletions.add(modelRealIndex);

      return sobjModels.get(hash);
    }

    final SobjModel model = new SobjModel(modelNode, "DRGN2%d-%d-%d".formatted(drngIndex, folderIndex, modelRealIndex));
    sobjModels.put(hash, model);
    return model;
  }

  private static SobjTexture processSobjTexture(final int sobjIndex, final PathNode folderNode, final String[] mrg, final SobjModel model, final int drgnIndex, final int folderIndex, final IntSet deletions, final XXH3State xxhash) {
    final int textureRealIndex = getRealFileIndex(mrg[sobjIndex]);
    final PathNode textureNode = folderNode.children.get(Integer.toString(textureRealIndex));

    final long hash = generateHash(textureNode.data, xxhash);

    return model.processTexture(textureNode, hash, textureRealIndex, drgnIndex, folderIndex, deletions);
  }

  private static SobjAnimation[] processSobjAnimations(final int sobjIndex, final PathNode folderNode, final String[] mrg, final SobjModel model, final int placeholderAnimation, final int drgnIdex, final int folderIndex, final IntSet deletions, final XXH3State xxhash) {
    final SobjAnimation[] animations = new SobjAnimation[32];

    final int modelIndex = sobjIndex * 33;
    final int realModelIndex = getRealFileIndex(mrg[modelIndex]);

    for(int animation = 0; animation < 32; animation++) {
      final int animationRealIndex = getRealFileIndex(mrg[sobjIndex * 33 + animation + 1]);

      if(!animationInRange(animationRealIndex, modelIndex, realModelIndex, placeholderAnimation)) {
        continue;
      }

      final PathNode animationNode = folderNode.children.get(Integer.toString(animationRealIndex));

      final long hash = generateHash(animationNode.data, xxhash);

      animations[animation] = model.processAnimation(animationNode, hash, animationRealIndex, drgnIdex, folderIndex, deletions);
    }

    // TODO consider re-pointing nulls

    return animations;
  }

  private static void deleteRedundantNodes(final PathNode folderNode, final PathNode textureFolderNode, final IntSet modelAndAnimationDeletions, final IntSet textureDeletions) {
    for(final int node : modelAndAnimationDeletions) {
      folderNode.children.remove(Integer.toString(node));
    }

    for(final int node : textureDeletions) {
      textureFolderNode.children.remove(Integer.toString(node));
    }

    textureFolderNode.children.remove("mrg");
  }

  private static long generateHash(final FileData file, final XXH3State xxhash) {
    XXHash.XXH3_64bits_reset(xxhash);
    XXHash.XXH3_64bits_update(xxhash, BufferUtils.createByteBuffer(file.size()).put(file.getBytes()).position(0));
    return XXHash.XXH3_64bits_digest(xxhash);
  }

  private static String[] loadMrg(final PathNode directory) {
    final PathNode mrgFile = directory.children.get("mrg");
    return mrgFile.data.readFixedLengthAscii(0x0, mrgFile.data.size()).split("\n");
  }

  private static int getFolderMrgCount(final PathNode node) {
    final PathNode mrgNode = node.children.get("mrg");

    if(mrgNode == null) {
      LOGGER.error("Failed to find mrg file for node %s", node.fullPath);
      return 0;
    }

    final FileData mrgFile = mrgNode.data;
    final String mrgContents = mrgFile.readFixedLengthAscii(0x0, mrgFile.size());
    return (int)mrgContents.lines().count();
  }

  private static int getRealFileIndex(final String mrgLine) {
    try {
      final int equalsIndex = mrgLine.indexOf('=', 1 );
      final int semicolonIndex = mrgLine.indexOf(';', equalsIndex + 2);
      return Integer.parseInt(mrgLine, equalsIndex + 1, semicolonIndex, 10);
    } catch(Exception ex) {
      LOGGER.error("MRG failed for %s", mrgLine);
      return 0;
    }
  }

  private static boolean animationInRange(final int animationRealIndex, final int modelIndex, final int modelRealIndex, final int placeholderAnimation) {
    if((animationRealIndex > modelIndex && animationRealIndex < modelIndex + 33) || (animationRealIndex > modelRealIndex && animationRealIndex < modelRealIndex + 33)) {
      return true;
    }

    return animationRealIndex != placeholderAnimation;
  }

  private static int findPlaceholderAnimation(final String[] mrg) {
    final Int2IntOpenHashMap map = new Int2IntOpenHashMap();

    for(int line = 1; line < mrg.length; line++) {
      if(line % 33 == 0) {
        continue;
      }

      final int key = getRealFileIndex(mrg[line]);

      if(map.containsKey(key)) {
        final int count = map.get(key) + 1;
        map.put(key, count);
      } else {
        map.put(key, 1);
      }
    }

    int count = 0;
    int position = -1;

    for(final Int2IntMap.Entry entry : map.int2IntEntrySet()) {
      if(count < entry.getIntValue()) {
        position = entry.getIntKey();
        count = entry.getIntValue();
      }
    }

    return position;
  }

  private static void moveUniqueNodes(final Long2ObjectOpenHashMap<SobjModel> models, final Transformations transformations) {
    for(final SobjModel model : models.values()) {
      model.transformIfNew(transformations);
    }
  }


  private static void generateSobjReferences(final DrgnSobjPack[] drgnSobjPacks, final PathNode root, final Transformations transformations) {
    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgnNode = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgnNode == null) {
        continue;
      }

      final DrgnSobjPack sobjPack = drgnSobjPacks[drgnIndex - 1];

      for(final Int2ObjectMap.Entry<Sobj[]> map : sobjPack.getMaps()) {
        final PathNode folderNode = drgnNode.children.get(Integer.toString(map.getIntKey()));

        final Sobj[] sobjs = map.getValue();
        for(int sobj = 0; sobj < sobjs.length; sobj++) {
          final PathNode sobjNode = transformations.addChild(folderNode, Integer.toString(sobj), null);

          transformations.addChild(sobjNode, "model", sobjs[sobj].getModelRefFile());
          transformations.addChild(sobjNode, "texture", sobjs[sobj].getTextureRefFile());
          transformations.addChild(sobjNode, "animations", sobjs[sobj].getAnimationsRefFile());
        }
      }
    }


  }
}
