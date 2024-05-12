package legend.game.unpacker.sobj2;

import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;

final class SobjAnimation {
  private String name;
  private boolean isNamed;
  private final PathNode animation;

  SobjAnimation(final PathNode animation, final String path) {
    this.animation = animation;
    this.name = path;
  }

  void transformIfNew(final String modelPath, final Transformations transformations) {
    if(this.animation != null) {
      transformations.addNode(modelPath + "/animations/" + this.name, this.animation.data);
      this.animation.parent.children.remove(this.animation.toString());
    }
  }

  String getName() {
    return this.name;
  }
}
