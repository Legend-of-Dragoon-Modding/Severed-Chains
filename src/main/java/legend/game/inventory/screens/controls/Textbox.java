package legend.game.inventory.screens.controls;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Textbox extends Control {
  private final Panel background;
  private LodString text = new LodString("");
  private int maxLength = -1;

  public Textbox() {
    this.background = this.addControl(Panel.subtle());
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.background.setZ(z + 1);
  }

  public void setMaxLength(final int maxLength) {
    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return this.maxLength;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.background.setSize(this.getWidth(), this.getHeight());
  }

  public void setText(final String text) {
    this.text = new LodString(text);
  }

  public void setText(final LodString text) {
    this.text = text;
  }

  public String getText() {
    return this.text.get();
  }

  @Override
  protected void render(final int x, final int y) {
    final int oldZ = textZ_800bdf00.get();
    textZ_800bdf00.set(this.getZ() - 1);
    SItem.renderText(this.text, x + 4, y + (this.getHeight() - 11) / 2 + 1, TextColour.BROWN);
    textZ_800bdf00.set(oldZ);
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    if(super.keyPress(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(key == GLFW_KEY_BACKSPACE && this.text.length() > 0) {
      this.text = new LodString(this.text.get().substring(0, this.text.length() - 1));
    }

    if(key == GLFW_KEY_ESCAPE) {
      this.deferAction(this::unfocus);
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.maxLength != -1 && this.text.length() >= this.maxLength) {
      return InputPropagation.HANDLED;
    }

    this.text = new LodString(this.text.get() + (char)codepoint);
    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    return InputPropagation.HANDLED;
  }
}
