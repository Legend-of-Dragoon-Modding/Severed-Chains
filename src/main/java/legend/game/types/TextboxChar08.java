package legend.game.types;

import legend.core.opengl.Obj;

public class TextboxChar08 {
  public int x_00;
  public int y_02;
  public int colour_04;
  public int char_06;

  public Obj obj;

  public void set(final TextboxChar08 other) {
    this.x_00 = other.x_00;
    this.y_02 = other.y_02;
    this.colour_04 = other.colour_04;
    this.char_06 = other.char_06;
    this.obj = other.obj;
  }

  public void clear() {
    this.x_00 = 0;
    this.y_02 = 0;
    this.colour_04 = 0;
    this.char_06 = 0;
    this.delete();
  }

  public void delete() {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }
}
