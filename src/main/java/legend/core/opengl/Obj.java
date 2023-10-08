package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class Obj {
  private final Mesh[] meshes;

  public Obj(final Mesh[] meshes) {
    this.meshes = meshes;
  }

  public void render(@Nullable final Translucency translucency) {
    if(translucency == null) {
      if(this.meshes[0] != null) {
        this.meshes[0].draw();
      }
    } else if(this.meshes[translucency.ordinal() + 1] != null) {
      if(translucency != Translucency.B_PLUS_F) {
        throw new RuntimeException("Need to implement " + translucency);
      }

      this.meshes[translucency.ordinal() + 1].draw();
    }
  }

  public void delete() {
    for(final Mesh mesh : this.meshes) {
      if(mesh != null) {
        mesh.delete();
      }
    }
  }
}
