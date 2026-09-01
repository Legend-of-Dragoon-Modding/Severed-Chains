package legend.core.renderer.noop;

import legend.core.renderer.Mesh;
import legend.core.renderer.Translucency;

import javax.annotation.Nullable;

public class NoopMesh implements Mesh {
  public final float[] vertexData;
  public final boolean textured;
  public final boolean translucent;
  public final Translucency translucencyMode;

  NoopMesh(final float[] vertexData, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode) {
    this.vertexData = vertexData;
    this.textured = textured;
    this.translucent = translucent;
    this.translucencyMode = translucencyMode;
  }

  @Override
  public void update() {

  }

  @Override
  public void delete() {

  }

  @Override
  public void attribute(final int index, final long offset, final int size, final int stride) {

  }

  @Override
  public void draw() {

  }

  @Override
  public void draw(final int start, final int count) {

  }

  @Override
  public float[] vertices() {
    return this.vertexData;
  }

  @Override
  public boolean textured() {
    return this.textured;
  }

  @Override
  public boolean translucent() {
    return this.translucent;
  }

  @Override
  public Translucency translucencyMode() {
    return this.translucencyMode;
  }
}
