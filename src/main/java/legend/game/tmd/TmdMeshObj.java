package legend.game.tmd;

import legend.core.renderer.Mesh;
import legend.core.renderer.MeshObj;
import legend.core.renderer.Translucency;

import javax.annotation.Nullable;

public class TmdMeshObj extends MeshObj {
  public TmdMeshObj(final String name, final Mesh[] meshes) {
    super(name, meshes);
  }

  public TmdMeshObj(final String name, final Mesh[] meshes, final boolean backfaceCulling) {
    super(name, meshes, backfaceCulling);
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    // For untextured translucent faces, no translucency is defined in the TMD data and will be passed in at runtime via tmdGp0Tpage_1f8003ec
    return super.shouldRender(translucency) || translucency != null && this.translucencies.isEmpty();
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    return super.shouldRender(translucency, layer) || translucency != null && this.translucencies.isEmpty();
  }
}
