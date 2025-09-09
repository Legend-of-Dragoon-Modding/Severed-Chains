package legend.core.gpu;

import java.util.Objects;

public final class Rect4i {
  public int x;
  public int y;
  public int w;
  public int h;

  public Rect4i() {

  }

  public Rect4i(final int x, final int y, final int w, final int h) {
    this.set(x, y, w, h);
  }

  public Rect4i(final Rect4i other) {
    this.set(other.x, other.y, other.w, other.h);
  }

  public Rect4i set(final int x, final int y, final int w, final int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    return this;
  }

  public Rect4i set(final Rect4i other) {
    return this.set(other.x, other.y, other.w, other.h);
  }

  /**
   * Set the rect to a region inside the current rect using absolute coordinates.
   * Any part that falls outside the current rect will be clipped.
   */
  public Rect4i subregion(final int x, final int y, int w, int h) {
    if(this.x < x) {
      this.w -= x - this.x;
      this.x = x;
    } else {
      w -= this.x - x;
    }

    if(this.y < y) {
      this.h -= y - this.y;
      this.y = y;
    } else {
      h -= this.y - y;
    }

    if(this.w > w) {
      this.w = w;
    }

    if(this.h > h) {
      this.h = h;
    }

    if(this.w < 0) {
      this.w = 0;
    }

    if(this.h < 0) {
      this.h = 0;
    }

    return this;
  }

  /**
   * Set the rect to a region inside the current rect using absolute coordinates.
   * Any part that falls outside the current rect will be clipped.
   */
  public Rect4i subregion(final Rect4i other) {
    return this.subregion(other.x, other.y, other.w, other.h);
  }

  /**
   * Calculate the minimum bounding box that contains other rects
   */
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
      if(rect == null) {
        continue;
      }

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

  public int right() {
    return this.x + this.w;
  }

  public int bottom() {
    return this.y + this.h;
  }

  public boolean contains(final int x, final int y) {
    if(x < this.x || y < this.y) {
      return false;
    }

    return x < this.x + this.w && y < this.y + this.h;
  }

  public int x() {
    return this.x;
  }

  public int y() {
    return this.y;
  }

  public int w() {
    return this.w;
  }

  public int h() {
    return this.h;
  }

  @Override
  public boolean equals(final Object obj) {
    if(obj == this) {
      return true;
    }
    if(obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    final var that = (Rect4i)obj;
    return this.x == that.x &&
      this.y == that.y &&
      this.w == that.w &&
      this.h == that.h;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.w, this.h);
  }

  @Override
  public String toString() {
    return "Rect4i[" +
      "x=" + this.x + ", " +
      "y=" + this.y + ", " +
      "w=" + this.w + ", " +
      "h=" + this.h + ']';
  }

}
