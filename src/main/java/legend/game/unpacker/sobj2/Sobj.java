package legend.game.unpacker.sobj2;

import legend.game.unpacker.FileData;

import java.nio.charset.StandardCharsets;

final class Sobj {
  private final SobjModel model;
  private final SobjTexture texture;
  private final SobjAnimation[] animations;

  Sobj(final SobjModel model, final SobjTexture texture, final SobjAnimation[] animations) {
    this.model = model;
    this.texture = texture;
    this.animations = animations;
  }

  FileData getModelRefFile() {
    return this.model.getRefFile();
  }

  FileData getTextureRefFile() {
    return this.texture.getRefFile();
  }

  FileData getAnimationsRefFile() {
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
    return new FileData(sb.toString().getBytes(StandardCharsets.US_ASCII));
  }
}
