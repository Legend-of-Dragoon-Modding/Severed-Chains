package legend.game.scriptdebugger;

import legend.core.Config;
import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.Font;
import legend.core.opengl.GuiManager;
import legend.core.opengl.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Debugger {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Debugger.class);

  private final Camera camera;
  private final Window window;
  private final Context ctx;
  private final GuiManager guiManager;

  public Debugger() {
    this.camera = new Camera(0.0f, 0.0f);
    this.window = new Window(Config.GAME_NAME, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
    this.ctx = new Context(this.window, this.camera);

    this.guiManager = new GuiManager(this.window);
    this.window.setEventPoller(this.guiManager::captureInput);

    final Font font;
    try {
      font = new Font("gfx/fonts/Consolas.ttf");
    } catch(IOException e) {
      LOGGER.error("Failed to load script debugger font:", e);
      this.guiManager.free();
      return;
    }

    this.guiManager.setFont(font);
    this.guiManager.pushGui(new DebuggerGui());

    this.ctx.onDraw(() -> this.guiManager.draw(this.ctx.getWidth(), this.ctx.getHeight(), this.ctx.getWidth() / this.window.getScale(), this.ctx.getHeight() / this.window.getScale()));
    this.window.show();

    try {
      this.window.run();
    } catch(final Throwable t) {
      LOGGER.error("Shutting down due to exception:", t);
      this.window.close();
    } finally {
      this.guiManager.free();
      font.free();
    }
  }

  public void close() {
    this.window.close();
  }
}
