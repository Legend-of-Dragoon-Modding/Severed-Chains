package legend.game.sound;

import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.Font;
import legend.core.opengl.GuiManager;
import legend.core.opengl.Window;

import java.io.IOException;

public class MusicDebugger {
  private final Window window;
  public final Camera camera;
  private final Context ctx;
  private final GuiManager guiManager;

  public MusicDebugger() throws IOException {
    this.window = new Window("Music Debugger", 728, 600);
    this.camera = new Camera(0.0f, 0.0f);
    this.ctx = new Context(this.window, this.camera);

    this.guiManager = new GuiManager(this.window);
    this.window.setEventPoller(this.guiManager::captureInput);

    final Font font = new Font("gfx/fonts/Consolas.ttf");
    this.guiManager.setFont(font);

    final MusicDebuggerGui gui = new MusicDebuggerGui();
    this.guiManager.pushGui(gui);

    this.ctx.onDraw(() -> this.guiManager.draw(this.ctx.getWidth(), this.ctx.getHeight(), this.ctx.getWidth() / this.window.getScale(), this.ctx.getHeight() / this.window.getScale()));

    this.window.show();
    this.window.run();

    this.guiManager.free();
    font.free();
  }
}
