package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class MeshObj implements Obj {
  private final Mesh[] meshes;

  public MeshObj(final Mesh[] meshes) {
    this.meshes = meshes;
  }

  @Override
  public void render(@Nullable final Translucency translucency) {
    if(translucency == null) {
      if(this.meshes[0] != null) {
        this.meshes[0].draw();
      }
    } else if(this.meshes[translucency.ordinal() + 1] != null) {
      if(translucency != Translucency.B_PLUS_F && translucency != Translucency.HALF_B_PLUS_HALF_F) {
        throw new RuntimeException("Need to implement " + translucency);
      }

      this.meshes[translucency.ordinal() + 1].draw();
    }
  }

  @Override
  public void delete() {
    for(final Mesh mesh : this.meshes) {
      if(mesh != null) {
        mesh.delete();
      }
    }
  }
}
