package legend.game.unpacker.sobj;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import legend.game.unpacker.FileData;

public final class SobjModelEntry {
  private String name;
  private final FileData model;
  private final Long2ObjectOpenHashMap<SobjTextureEntry> textures = new Long2ObjectOpenHashMap<>();
  private final Long2ObjectOpenHashMap<SobjAnimationEntry> animations = new Long2ObjectOpenHashMap<>();

  SobjModelEntry(final FileData model) {
    this.model = model;
  }

  SobjTextureEntry processTexture(final FileData texture, final long hash) {
    if(this.textures.containsKey(hash)) {
      return this.textures.get(hash);
    }

    final SobjTextureEntry entry = new SobjTextureEntry(texture);
    this.textures.put(hash, entry);
    return entry;
  }

  SobjAnimationEntry processAnimation(final FileData animation, final long hash) {
    if(this.animations.containsKey(hash)) {
      return this.animations.get(hash);
    }

    final SobjAnimationEntry entry = new SobjAnimationEntry(animation);
    this.animations.put(hash, entry);
    return entry;
  }

  void fillNames(final long hash) {
    if(this.name == null) {
      this.name = Long.toHexString(hash);
    }

    for(final Long2ObjectOpenHashMap.Entry<SobjTextureEntry> textureEntry : this.textures.long2ObjectEntrySet()) {
      if(!textureEntry.getValue().hasName()) {
        textureEntry.getValue().setName(Long.toHexString(textureEntry.getLongKey()));
      }
    }

    for(final Long2ObjectOpenHashMap.Entry<SobjAnimationEntry> animationEntry : this.animations.long2ObjectEntrySet()) {
      if(!animationEntry.getValue().hasName()) {
        animationEntry.getValue().setName(Long.toHexString(animationEntry.getLongKey()));
      }
    }
  }

  String getName() {
    return this.name;
  }

  FileData getModel() {
    return this.model;
  }

  ObjectCollection<SobjTextureEntry> getTextures() {
    return this.textures.values();
  }

  ObjectCollection<SobjAnimationEntry> getAnimations() {
    return this.animations.values();
  }
}
