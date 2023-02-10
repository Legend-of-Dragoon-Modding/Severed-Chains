package legend.game.inventory.screens;

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

  protected boolean propagateRender() {
    return false;
  }

  protected boolean propagateInput() {
    return false;
  }
}
