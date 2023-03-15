package legend.core.gpu;

public record Rect4i(int x, int y, int w, int h) {
  /** Calculate the minimum bounding box that contains other rects */
  public static Rect4i bound(final Rect4i... rects) {
    if(rects.length == 0) {
      return new Rect4i(0, 0, 0, 0);
    }

    if(rects.length == 1) {
      return rects[0];
    }

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    for(final Rect4i rect : rects) {
      if(rect.x < minX) {
        minX = rect.x;
      }

      if(rect.y < minY) {
        minY = rect.y;
      }

      if(rect.x + rect.w > maxX) {
        maxX = rect.x + rect.w;
      }

      if(rect.y + rect.h > maxY) {
        maxY = rect.y + rect.h;
      }
    }

    return new Rect4i(minX, minY, maxX - minX, maxY - minY);
  }

  public boolean contains(final int x, final int y) {
    if(x < this.x || y < this.y) {
      return false;
    }

    return x < this.x + this.w && y < this.y + this.h;
  }
}
