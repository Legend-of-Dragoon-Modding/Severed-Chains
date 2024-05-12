package legend.game.unpacker.sobj2;

import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;

import java.nio.charset.StandardCharsets;

final class SobjModel {
  private String name;
  private boolean isNamed;
  private final PathNode model;
  private final Long2ObjectOpenHashMap<SobjTexture> textures = new Long2ObjectOpenHashMap<>();
  private final Long2ObjectOpenHashMap<SobjAnimation> animations = new Long2ObjectOpenHashMap<>();

  SobjModel(final PathNode model, final String path) {
    this.model = model;
    this.name = path;
  }

  SobjTexture processTexture(final PathNode textureNode, final long hash, final int textureRealIndex, final int drgnIndex, final int folderIndex, final IntSet deletions) {
    if(this.textures.containsKey(hash)) {
      deletions.add(textureRealIndex);

      return this.textures.get(hash);
    }

    final SobjTexture texture = new SobjTexture(textureNode, "DRGN2%d-%d-textures-%d".formatted(drgnIndex, folderIndex, textureRealIndex));
    this.textures.put(hash, texture);
    return texture;
  }

  SobjAnimation processAnimation(final PathNode animationNode, final long hash, final int animationRealIndex, final int drgnIndex, final int folderIndex, final IntSet deletions) {
    if(this.animations.containsKey(hash)) {
      deletions.add(animationRealIndex);

      return this.animations.get(hash);
    }

    final SobjAnimation animation = new SobjAnimation(animationNode, "DRGN2%d-%d-%d".formatted(drgnIndex, folderIndex, animationRealIndex));
    this.animations.put(hash, animation);

    return animation;
  }

  void transformIfNew(final Transformations transformations) {
    final String path = "submap_objects/" + this.name;

    if(this.model != null) {
      transformations.addNode(path + "/model", this.model.data);
      this.model.parent.children.remove(this.model.toString());
    }

    for(final SobjTexture texture : this.textures.values()) {
      texture.transformIfNew(path, transformations);
    }

    for(final SobjAnimation animation : this.animations.values()) {
      animation.transformIfNew(path, transformations);
    }
  }

  FileData getRefFile() {
    return new FileData(this.name.getBytes(StandardCharsets.US_ASCII));
  }
}
