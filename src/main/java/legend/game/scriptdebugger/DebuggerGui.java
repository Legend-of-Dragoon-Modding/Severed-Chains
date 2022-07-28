package legend.game.scriptdebugger;

import legend.core.opengl.Gui;
import legend.core.opengl.GuiManager;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.NK_STATIC;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_LEFT;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_push;
import static org.lwjgl.nuklear.Nuklear.nk_menu_begin_label;
import static org.lwjgl.nuklear.Nuklear.nk_menu_end;
import static org.lwjgl.nuklear.Nuklear.nk_menu_item_label;
import static org.lwjgl.nuklear.Nuklear.nk_vec2;

public class DebuggerGui extends Gui {
  @Override
  protected void draw(final GuiManager manager, final MemoryStack stack) {
    this.simpleWindow(manager, stack, "Debugger", 0, 0, manager.window.getWidth(), manager.window.getHeight(), () -> {
      Nuklear.nk_menubar_begin(manager.ctx);

      nk_layout_row_begin(manager.ctx, NK_STATIC, 25, 5);
      nk_layout_row_push(manager.ctx, 45);

      final NkVec2 vec2 = NkVec2.mallocStack(stack);
      if(nk_menu_begin_label(manager.ctx, "MENU", NK_TEXT_LEFT, nk_vec2(120, 200, vec2))) {
        nk_layout_row_dynamic(manager.ctx, 25, 1);

        nk_menu_item_label(manager.ctx, "Hide", NK_TEXT_LEFT);

        nk_menu_end(manager.ctx);
      }

      Nuklear.nk_menubar_end(manager.ctx);
    });
  }
}
