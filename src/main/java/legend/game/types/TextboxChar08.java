package legend.game.types;

import legend.game.inventory.screens.TextColour;

public class TextboxChar08 {
  public int x_00;
  public int y_02;
  public TextColour colour_04 = TextColour.WHITE;
  public int char_06;

  public void set(final TextboxChar08 other) {
    this.x_00 = other.x_00;
    this.y_02 = other.y_02;
    this.colour_04 = other.colour_04;
    this.char_06 = other.char_06;
  }

  public void clear() {
    this.x_00 = 0;
    this.y_02 = 0;
    this.colour_04 = TextColour.WHITE;
    this.char_06 = 0;
  }
}
