package legend.game.inventory.screens;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class MenuStack {
  private final Deque<MenuScreen> screens = new LinkedList<>();

  public void pushScreen(final MenuScreen screen) {
    this.screens.push(screen);
  }

  public void popScreen() {
    this.screens.pop();
  }

  public void render() {
    final Iterator<MenuScreen> it = this.screens.iterator();

    if(it.hasNext()) {
      this.render(it);
    }
  }

  private void render(final Iterator<MenuScreen> it) {
    final MenuScreen screen = it.next();

    // Render screen below this one
    if(screen.propogateRender() && it.hasNext()) {
      this.render(it);
    }

    screen.render();
  }
}
