package legend.core.gpu;

public record Rect4i(int x, int y, int w, int h) {
  public boolean contains(final int x, final int y) {
    if(x < this.x || y < this.y) {
      return false;
    }

    return x < this.x + this.w && y < this.y + this.h;
  }
}
