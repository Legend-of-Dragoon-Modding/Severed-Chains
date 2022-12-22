package legend.game.inventory.screens;

public abstract class MenuScreen {
  public abstract void render();
  public abstract void handleInput();

  public boolean renderNext() {
    return false;
  }
}
