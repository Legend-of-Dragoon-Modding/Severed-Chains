package legend.game.inventory.screens.controls;

import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;

import static legend.game.SItem.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Button extends Control {
  private final Highlight hover;
  private String text;
  private char icon;
  private TextColour textColour = TextColour.BROWN;

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

  public char getIcon() {
    return this.icon;
  }

  public void setIcon(final char icon) {
    this.icon = icon;
  }

  public String getText() {
    return this.text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public TextColour getTextColour() {
    return this.textColour;
  }

  public void setTextColour(final TextColour colour) {
    this.textColour = colour;
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

    String text = this.text;
    if(this.icon != 0) {
      text = this.icon + " " + this.text;
    }

    renderCentredText(text, x + this.getWidth() / 2, y + (this.getHeight() - 11) / 2, this.isDisabled() ? TextColour.MIDDLE_BROWN : this.textColour);
    textZ_800bdf00 = oldZ;
  }

  public void onPressed(final Pressed handler) {
    this.pressedHandler = handler;
  }

  private Pressed pressedHandler;

  @FunctionalInterface public interface Pressed { void pressed(); }
}
