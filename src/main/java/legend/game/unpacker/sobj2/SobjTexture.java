package legend.game.unpacker.sobj2;

import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;

import java.nio.charset.StandardCharsets;

final class SobjTexture {
  private String name;
  private boolean isNamed;
  private final PathNode texture;

  SobjTexture(final PathNode texture, final String path) {
    this.texture = texture;
    this.name = path;
  }

  void transformIfNew(final String modelPath, final Transformations transformations) {
    if(this.texture != null) {
      transformations.addNode(modelPath + "/textures/" + this.name, this.texture.data);
      this.texture.parent.children.remove(this.texture.toString());
    }
  }

  FileData getRefFile() {
    return new FileData(this.name.getBytes(StandardCharsets.US_ASCII));
  }
}
