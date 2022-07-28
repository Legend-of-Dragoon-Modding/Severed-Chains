package legend.game.scriptdebugger;

import legend.core.opengl.Gui;
import legend.core.opengl.GuiManager;
import org.lwjgl.system.MemoryStack;

public class ScriptDebuggerGui extends Gui {
  @Override
  protected void draw(final GuiManager manager, final MemoryStack stack) {
    this.simpleWindow(manager, stack, "Script Debugger", 0, 0, manager.window.getWidth(), manager.window.getHeight(), () -> {

    });
  }
}
