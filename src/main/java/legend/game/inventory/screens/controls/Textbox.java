package legend.game.inventory.screens.controls;

import legend.game.SItem;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;

public class Textbox extends Control {
  private final Panel panel;
  private LodString text = new LodString("");

  public Textbox() {
    this.panel = this.addControl(new Panel());
    this.panel.setPos(-7, -7);
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.panel.setZ(z + 1);
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.panel.setSize(this.getWidth() + 10, this.getHeight() + 10);
  }

  public void setText(final String text) {
    this.text = new LodString(text);
  }

  public void setText(final LodString text) {
    this.text = text;
  }

  @Override
  protected void render(final int x, final int y) {
    SItem.renderText(this.text, x, y, TextColour.BROWN);
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    if(super.keyPress(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(key == GLFW_KEY_BACKSPACE && this.text.length() > 0) {
      this.text = new LodString(this.text.get().substring(0, this.text.length() - 1));
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.text = new LodString(this.text.get() + (char)codepoint);
    return InputPropagation.HANDLED;
  }
}
