package legend.game.inventory.screens.controls;

import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;

import static legend.game.SItem.UI_TEXT_DISABLED_CENTERED;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Button extends Control {
  private final Highlight hover;
  private String text;
  private float textHeight;
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE);

  public Button(final String text) {
    this.hover = this.addControl(new Highlight());
    this.hover.setZ(this.getZ());
    this.hover.hide();

    this.setSize(59, 14);

    this.setText(text);
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.hover.setZ(z);
  }

  public String getText() {
    return this.text;
  }

  public void setText(final String text) {
    this.text = text;
    this.updateTextSize();
  }

  public void setTextColour(final TextColour colour) {
    this.fontOptions.colour(colour);
  }

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);
    this.fontOptions.size(scale);
    this.updateTextSize();
  }

  private void updateTextSize() {
    this.textHeight = textHeight(this.text) * this.getScale();
  }

  public void press() {
    playMenuSound(2);

    if(this.pressedHandler != null) {
      this.pressedHandler.pressed();
    }
  }

  @Override
  protected void hoverIn() {
    super.hoverIn();
    this.hover.show();
  }

  @Override
  protected void hoverOut() {
    super.hoverOut();
    this.hover.hide();
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.hover.setSize(this.getWidth(), this.getHeight());
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT && mods == 0) {
      this.press();
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      this.press();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    final int oldZ = textZ_800bdf00;
    textZ_800bdf00 = this.getZ() - 1;
    renderText(this.text, x + this.getWidth() / 2, y + (this.getHeight() - this.textHeight) / 2, this.isDisabled() ? UI_TEXT_DISABLED_CENTERED : this.fontOptions);
    textZ_800bdf00 = oldZ;
  }

  public void onPressed(final Pressed handler) {
    this.pressedHandler = handler;
  }

  private Pressed pressedHandler;

  @FunctionalInterface public interface Pressed { void pressed(); }
}
