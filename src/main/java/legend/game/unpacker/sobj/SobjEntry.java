package legend.game.unpacker.sobj;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

public final class SobjEntry {
  private final SobjModelEntry model;
  private final SobjTextureEntry texture;
  private final SobjAnimationEntry[] animations = new SobjAnimationEntry[32];

  SobjEntry(final PathNode modelNode, final String[] modelMgr, final PathNode textureNode, final String[] textureMgr, final int index, final Long2ObjectOpenHashMap<SobjModelEntry> sobjModels) {
    final XXH3State xxhash = XXHash.XXH3_createState();
    if(xxhash == null) {
      throw new RuntimeException("Failed to create XXH3 state");
    }

    final int modelIndex = index * 33;
    final int modelRealIndex = getRealFileIndex(modelMgr[modelIndex]);
    final FileData modelFile = modelNode.children.get(Integer.toString(modelRealIndex)).data;

    XXHash.XXH3_64bits_reset(xxhash);
    XXHash.XXH3_64bits_update(xxhash, BufferUtils.createByteBuffer(modelFile.size()).put(modelFile.getBytes()).position(0));
    long hash = XXHash.XXH3_64bits_digest(xxhash);

    if(sobjModels.containsKey(hash)) {
      this.model = sobjModels.get(hash);
    } else {
      this.model = new SobjModelEntry(modelFile);
      sobjModels.put(hash, this.model);
    }

    final FileData textureFile = textureNode.children.get(Integer.toString(getRealFileIndex(textureMgr[index]))).data;

    XXHash.XXH3_64bits_reset(xxhash);
    XXHash.XXH3_64bits_update(xxhash, BufferUtils.createByteBuffer(textureFile.size()).put(textureFile.getBytes()).position(0));
    hash = XXHash.XXH3_64bits_digest(xxhash);

    this.texture = this.model.processTexture(textureFile, hash);

    for(int animation = 0; animation < 32; animation++) {
      final int animationIndex = getRealFileIndex(modelMgr[modelIndex + animation + 1]);

      if(!animationInRange(animationIndex, modelIndex, modelRealIndex)) {
        continue;
      }

      final PathNode animationNode = modelNode.children.get(Integer.toString(animationIndex));
      if(animationNode == null) {
        continue;
      }

      final FileData animationFile = animationNode.data;

      XXHash.XXH3_64bits_reset(xxhash);
      XXHash.XXH3_64bits_update(xxhash, BufferUtils.createByteBuffer(animationFile.size()).put(animationFile.getBytes()).position(0));
      hash = XXHash.XXH3_64bits_digest(xxhash);

      this.animations[animation] = this.model.processAnimation(animationFile, hash);
    }

    XXHash.XXH3_freeState(xxhash);
  }

  String getModelName() {
    return this.model.getName();
  }

  String getTextureName() {
    return this.texture.getName();
  }

  String getAnimationNames() {
    final StringBuilder sb = new StringBuilder();

    for(int animation = 0; animation < this.animations.length; animation++) {
      sb.append(animation);
      sb.append('=');
      if(this.animations[animation] != null) {
        sb.append(this.animations[animation].getName());
      }
      sb.append('\n');
    }

    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  private static boolean animationInRange(final int animationIndex, final int modelIndex, final int realModelIndex) {
    // Outside of model Index
    if((animationIndex <= modelIndex) || (animationIndex >= (modelIndex + 33))) {
      // Outside of model's real Index
      if((animationIndex <= realModelIndex) || (animationIndex >= (realModelIndex + 33))) {
        return false;
      }
    }

    return true;
  }

  private static int getRealFileIndex(final String mrgLine) {
    try {
      final int equalsIndex = mrgLine.indexOf('=', 1 );
      final int semicolonIndex = mrgLine.indexOf(';', equalsIndex + 2);
      return Integer.parseInt(mrgLine, equalsIndex + 1, semicolonIndex, 10);
    } catch(Exception ex) {
      System.out.printf("mgr failed for %s%n", mrgLine);
      return 0;
    }

  }
}
