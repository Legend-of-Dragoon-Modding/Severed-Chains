package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class MeshObj extends Obj {
  private final Mesh[] meshes;
  private final boolean backfaceCulling;
  private final boolean hasTranslucency;

  public MeshObj(final String name, final Mesh[] meshes) {
    this(name, meshes, true);
  }

  public MeshObj(final String name, final Mesh[] meshes, final boolean backfaceCulling) {
    super(name);
    this.meshes = meshes;
    this.backfaceCulling = backfaceCulling;

    boolean hasTranslucency = false;
    for(int i = 1; i < meshes.length; i++) {
      if(meshes[i] != null) {
        hasTranslucency = true;
        break;
      }
    }

    this.hasTranslucency = hasTranslucency;
  }

  @Override
  public boolean useBackfaceCulling() {
    return this.backfaceCulling;
  }

  @Override
  public boolean hasTranslucency() {
    return this.hasTranslucency;
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.deleted) {
      return false;
    }

    if(translucency == null) {
      return this.meshes[0] != null;
    }

    return this.meshes[translucency.ordinal() + 1] != null;
  }

  @Override
  public void render(final int startVertex, final int vertexCount) {
    for(int i = 0; i < this.meshes.length; i++) {
      if(this.meshes[i] != null) {
        this.meshes[i].draw(startVertex, vertexCount);
      }
    }
  }

  @Override
  public void render(@Nullable final Translucency translucency, final int startVertex, final int vertexCount) {
    if(translucency == null) {
      this.meshes[0].draw(startVertex, vertexCount);
    } else {
      if(translucency != Translucency.HALF_B_PLUS_HALF_F && translucency != Translucency.B_PLUS_F && translucency != Translucency.B_MINUS_F && translucency != Translucency.B_PLUS_QUARTER_F) {
        throw new RuntimeException("Need to implement " + translucency);
      }

      this.meshes[translucency.ordinal() + 1].draw(startVertex, vertexCount);
    }
  }

  @Override
  public void delete() {
    if(!this.deleted) {
      super.delete();

      for(final Mesh mesh : this.meshes) {
        if(mesh != null) {
          mesh.delete();
        }
      }
    }
  }
}
