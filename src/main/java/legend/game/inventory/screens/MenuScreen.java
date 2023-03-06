package legend.game.inventory.screens;

import legend.game.input.InputAction;

public abstract class MenuScreen {
  protected abstract void render();

  protected void mouseMove(final int x, final int y) {
  }

  protected void mouseClick(final int x, final int y, final int button, final int mods) {
  }

  protected void mouseScroll(final double deltaX, final double deltaY) {
  }

  protected void keyPress(final int key, final int scancode, final int mods) {
  }

  protected void pressedThisFrame(final InputAction inputAction) {
  }

  protected void pressedWithRepeatPulse(final InputAction inputAction) {
  }

  protected void releasedThisFrame(final InputAction inputAction) {
  }

  protected boolean propagateRender() {
    return false;
  }

  protected boolean propagateInput() {
    return false;
  }
}
