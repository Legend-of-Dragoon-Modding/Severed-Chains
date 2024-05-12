package legend.game.unpacker.sobj;

import legend.game.unpacker.FileData;

import java.util.UUID;

public final class SobjAnimationEntry {
  private String name;
  private final FileData animation;

  SobjAnimationEntry(final FileData animation) {
    this.animation = animation;
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

  FileData getAnimation() {
    return this.animation;
  }
}
