package legend.game.submap;

import java.util.Arrays;
import java.util.function.Consumer;

public class MapTransitionData4c {
  /** The primitives bordering exits to the world map */
  public final int[] worldMapExitPrimitiveIndices_00 = new int[16];
  public int worldMapExitCount_40;
  /** Normally true, latched to false once the player steps onto a boundary that leads to the world map. Once false, stops updating the collided index while Dart leaves the map. */
  public boolean shouldUpdateCollidedWith_44;
  public Consumer<MapTransitionData4c> collidedPrimitiveSetterCallback_48;

  public void clear() {
    Arrays.fill(this.worldMapExitPrimitiveIndices_00, 0);
    this.worldMapExitCount_40 = 0;
    this.shouldUpdateCollidedWith_44 = false;
    this.collidedPrimitiveSetterCallback_48 = null;
  }
}
