package legend.game.ui;

import legend.core.lang.TextComponent;
import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;

import java.util.LinkedList;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.SHOW_FPS;

public final class GameOverlay {
  private GameOverlay() { }

  private static final FontOptions FONT = new FontOptions().set(SItem.UI_WHITE_SHADOWED_RIGHT).size(0.33f);

  private static final LinkedList<Notification> notifications = new LinkedList<>();

  public static void drawFps() {
    if(SHOW_FPS.isValid() && CONFIG.getConfig(SHOW_FPS.get())) {
      final String fps = I18n.translate("lod_core.ui.fps", RENDERER.getCurrentFps(), RENDERER.window().getFpsLimit());

      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 1;
      renderText(fps, RENDERER.getNativeWidth() - 4.0f + RENDERER.getWidescreenOrthoOffsetX(), 4.0f, FONT);
      textZ_800bdf00 = oldZ;
    }
  }

  public static void addNotification(final int seconds, final TextComponent text) {
    synchronized(notifications) {
      notifications.addLast(new Notification(System.nanoTime() + seconds * 1_000_000_000L, text));
    }
  }

  public static void drawNotifications() {
    synchronized(notifications) {
      final float x = 4.0f - RENDERER.getWidescreenOrthoOffsetX();
      float y = 4.0f;

      for(int i = 0; i < notifications.size(); i++) {
        y += notifications.get(i).draw(x, y);
      }

      notifications.removeIf(Notification::isFinished);
    }
  }
}
