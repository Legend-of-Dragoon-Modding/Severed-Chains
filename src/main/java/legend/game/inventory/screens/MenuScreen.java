package legend.game.inventory.screens;

import legend.game.input.InputAction;

import javax.annotation.Nullable;

public abstract class MenuScreen extends ControlHost {
  private Control hover;
  private Control focus;

  protected abstract void render();

  final void renderScreen() {
    this.render();
    this.renderControls(0, 0);
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    super.mouseMove(x, y);
    this.updateHover(x, y);
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    super.mouseClick(x, y, button, mods);
    this.updateHover(x, y);
    this.updateFocusFromHover();
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    super.keyPress(key, scancode, mods);

    if(this.focus != null) {
      this.focus.keyPress(key, scancode, mods);
    }
  }

  @Override
  protected void pressedThisFrame(final InputAction inputAction) {
    super.pressedThisFrame(inputAction);

    if(this.focus != null) {
      this.focus.pressedThisFrame(inputAction);
    }
  }

  @Override
  protected void pressedWithRepeatPulse(final InputAction inputAction) {
    super.pressedWithRepeatPulse(inputAction);

    if(this.focus != null) {
      this.focus.pressedWithRepeatPulse(inputAction);
    }
  }

  @Override
  protected void releasedThisFrame(final InputAction inputAction) {
    super.releasedThisFrame(inputAction);

    if(this.focus != null) {
      this.focus.releasedThisFrame(inputAction);
    }
  }

  private void updateHover(final int x, final int y) {
    final Control hover = this.findControlAt(x, y);

    if(hover != this.hover) {
      if(this.hover != null) {
        this.hover.hoverOut();
      }

      this.hover = hover;

      if(this.hover != null) {
        this.hover.hoverIn();
      }
    }
  }

  private void updateFocusFromHover() {
    if(this.focus != this.hover) {
      this.setFocus(this.hover);
    }
  }

  public void setFocus(@Nullable final Control control) {
    if(this.focus != null) {
      this.focus.lostFocus();
    }

    this.focus = control;

    if(this.focus != null) {
      this.focus.gotFocus();
    }
  }

  @Nullable
  public Control getFocus() {
    return this.focus;
  }

  protected boolean propagateRender() {
    return false;
  }

  protected boolean propagateInput() {
    return false;
  }
}
