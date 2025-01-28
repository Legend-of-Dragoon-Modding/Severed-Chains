package legend.game.inventory.screens;

import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Panel;

public class TooltipScreen extends MenuScreen {
  private static final int PADDING = 3;

  public TooltipScreen(final String text, final int x, final int y) {
    final Panel panel = this.addControl(Panel.dark());
    panel.onHoverOut(() -> this.deferAction(() -> this.getStack().popScreen()));

    final Label label = panel.addControl(new Label(text));
    label.getFontOptions().colour(TextColour.LIGHT_BROWN).noShadow();
    label.ignoreInput();
    label.setScale(0.5f);
    label.setPos(PADDING, PADDING);
    label.setZ(1);

    panel.setSize(label.getWidth() + PADDING * 2, label.getHeight() + PADDING * 2);
    panel.setPos(Math.max(0, x - panel.getWidth() / 2), y - panel.getHeight() / 2);
    panel.setZ(1);
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH || inputAction == InputAction.BUTTON_EAST) {
      this.deferAction(() -> this.getStack().popScreen());
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected boolean propagateRender() {
    return true;
  }

  @Override
  protected void render() {

  }
}
