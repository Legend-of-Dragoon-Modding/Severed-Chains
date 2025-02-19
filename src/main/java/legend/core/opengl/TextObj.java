package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class TextObj extends Obj {
  private final Mesh mesh;

  public TextObj(final String name, final Mesh mesh) {
    super(name);
    this.mesh = mesh;
  }

  @Override
  public boolean hasTexture() {
    return true;
  }

  @Override
  public boolean hasTexture(final int index) {
    return true;
  }

  @Override
  public boolean hasTranslucency() {
    return false;
  }

  @Override
  public boolean hasTranslucency(final int index) {
    return false;
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    return !this.deleted && translucency == null;
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    return this.shouldRender(translucency);
  }

  @Override
  public int getLayers() {
    return 1;
  }

  @Override
  public void render(final int layer, final int startVertex, final int vertexCount) {
    this.mesh.draw(startVertex, vertexCount);
  }

  @Override
  public void render(@Nullable final Translucency translucency, final int layer, final int startVertex, final int vertexCount) {
    this.mesh.draw(startVertex, vertexCount);
  }

  @Override
  public void performDelete() {
    this.mesh.delete();
  }
}
