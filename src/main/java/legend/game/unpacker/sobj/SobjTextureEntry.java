package legend.game.unpacker.sobj;

import legend.game.unpacker.FileData;

import java.util.UUID;

public class SobjTextureEntry {
  private String name;
  private final FileData texture;

  SobjTextureEntry(final FileData texture) {
    this.texture = texture;
  }

  String getName() {
    return this.name;
  }

  void setName(final String name) {
    this.name = name;
  }

  boolean hasName() {
    return this.name != null;
  }

  FileData getTexture() {
    return this.texture;
  }
}
