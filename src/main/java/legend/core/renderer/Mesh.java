package legend.core.renderer;

public interface Mesh {
  void update();
  void delete();
  void attribute(int index, long offset, int size, int stride);
  void draw();
  void draw(int start, int count);

  float[] vertices();
  boolean textured();
  boolean translucent();
  Translucency translucencyMode();
}
