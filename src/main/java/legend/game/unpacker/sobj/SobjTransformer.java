package legend.game.unpacker.sobj;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class SobjTransformer {
  private SobjTransformer() {}

  private static final Logger LOGGER = LogManager.getFormatterLogger(SobjTransformer.class);

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    final Long2ObjectOpenHashMap<SobjModelEntry> sobjModelMap = new Long2ObjectOpenHashMap<>();
    final DrgnSobjMapPack[] drgnSobjMapPacks = new DrgnSobjMapPack[4];

    // TODO load in case of partial transform


    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      final DrgnSobjMapPack drgnSobjMapPack = new DrgnSobjMapPack();
      drgnSobjMapPacks[drgnIndex - 1] = drgnSobjMapPack;

      final FileData drgnMrg = drgn.children.get("mrg").data;
      final String drgnMrgContents = drgnMrg.readFixedLengthAscii(0x0, drgnMrg.size());
      final int drgnMrgEntries = (int)drgnMrgContents.lines().count();

      for(int sobjAssetPackIndex = 5; sobjAssetPackIndex < drgnMrgEntries; sobjAssetPackIndex += 3) {
        final PathNode sobjAssetsNode = drgn.children.get(Integer.toString(sobjAssetPackIndex));

        // Doesn't exist or isn't a folder
        if(sobjAssetsNode == null || sobjAssetsNode.data != null) {
          continue;
        }

        final PathNode textureAssetsNode = sobjAssetsNode.children.get("textures");

        // Doesn't exist or isn't a folder
        if(textureAssetsNode == null || textureAssetsNode.data != null) {
          continue;
        }

        final String[] sobjAssetsMgr = loadMrg(sobjAssetsNode);
        final String[] sobjTextureMrg = loadMrg(textureAssetsNode);

        final List<SobjEntry> mapSobjEntries = new ArrayList<>();
        drgnSobjMapPack.addMap(sobjAssetPackIndex, mapSobjEntries);

        for(int sobj = 0; sobj < sobjTextureMrg.length; sobj++) {
          mapSobjEntries.add(new SobjEntry(sobjAssetsNode, sobjAssetsMgr, textureAssetsNode, sobjTextureMrg, sobj, sobjModelMap));
        }
      }
    }

    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      // Adds names to assets. Any already named asset won't be renamed to ensure consistency on partial unpacks.
      //TODO namify

      final PathNode assetsNode = transformations.addChild(root,"submap_objects", null);

      for(final Long2ObjectOpenHashMap.Entry<SobjModelEntry> modelEntry : sobjModelMap.long2ObjectEntrySet()) {
        final SobjModelEntry model = modelEntry.getValue();

        model.fillNames(modelEntry.getLongKey());

        final PathNode sobjNode = transformations.addChild(assetsNode, model.getName(), null);

        transformations.addChild(sobjNode, "model", model.getModel());

        final PathNode textureNode = transformations.addChild(sobjNode,"/textures", null);

        for(final SobjTextureEntry textureEntry : modelEntry.getValue().getTextures()) {
          transformations.addChild(textureNode, textureEntry.getName(), textureEntry.getTexture());
        }

        final PathNode animationNode = transformations.addChild(sobjNode,"/animations", null);

        for(final SobjAnimationEntry animationEntry : modelEntry.getValue().getAnimations()) {
          transformations.addChild(animationNode, animationEntry.getName(), animationEntry.getAnimation());
        }
      }


      break;
    }


    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      final DrgnSobjMapPack drgnSobjMapPack = drgnSobjMapPacks[drgnIndex - 1];

      for(final Int2ObjectOpenHashMap.Entry<List<SobjEntry>> modelEntry : drgnSobjMapPack.getMaps()) {
        final int index = modelEntry.getIntKey();
        final List<SobjEntry> sobjs = modelEntry.getValue();

        transformations.replaceNode(drgn.children.get(Integer.toString(index)), null);

//        drgn.children.remove(Integer.toString(index));

        final PathNode node = transformations.addChild(drgn, Integer.toString(index), null);

        for(int sobj = 0; sobj < sobjs.size(); sobj++) {
          final PathNode sobjNode = transformations.addChild(node, Integer.toString(sobj), null);

          final SobjEntry entry = sobjs.get(sobj);

          transformations.addChild(sobjNode, "model", new FileData(entry.getModelName().getBytes()));
          transformations.addChild(sobjNode, "texture", new FileData(entry.getTextureName().getBytes()));
          transformations.addChild(sobjNode, "animations", new FileData(entry.getAnimationNames().getBytes()));
        }
      }
    }

    final int t = 0;
    System.out.println("boop");
  }

  private static String[] loadMrg(final PathNode directory) {
    final PathNode mrgFile = directory.children.get("mrg");
    return mrgFile.data.readFixedLengthAscii(0x0, mrgFile.data.size()).split("\n");
  }
}
