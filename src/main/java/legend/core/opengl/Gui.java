package legend.core.opengl;

import legend.core.opengl.GuiManager;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import static org.lwjgl.nuklear.Nuklear.NK_DYNAMIC;
import static org.lwjgl.nuklear.Nuklear.NK_EDIT_FIELD;
import static org.lwjgl.nuklear.Nuklear.NK_EDIT_SIG_ENTER;
import static org.lwjgl.nuklear.Nuklear.NK_STATIC;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_CENTERED;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BACKGROUND;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MOVABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_button_label;
import static org.lwjgl.nuklear.Nuklear.nk_edit_focus;
import static org.lwjgl.nuklear.Nuklear.nk_edit_string;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_label;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_push;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_static;
import static org.lwjgl.nuklear.Nuklear.nk_option_label;
import static org.lwjgl.nuklear.Nuklear.nk_property_double;
import static org.lwjgl.nuklear.Nuklear.nk_property_float;
import static org.lwjgl.nuklear.Nuklear.nk_property_int;
import static org.lwjgl.nuklear.Nuklear.nk_recti;
import static org.lwjgl.system.MemoryStack.stackPush;

public abstract class Gui {
  private boolean firstDraw = true;

  public final void draw(final GuiManager manager) {
    try(final MemoryStack stack = stackPush()) {
      this.draw(manager, stack);
      nk_end(manager.ctx);
      this.firstDraw = false;
    }
  }

  protected abstract void draw(final GuiManager manager, final MemoryStack stack);

  protected void keyPress(final int key, final int scancode, final int mods) {

  }

  protected void keyRelease(final int key, final int scancode, final int mods) {

  }

  // Control rendering from here down

  protected void window(final GuiManager manager, final MemoryStack stack, final String title, final int x, final int y, final int w, final int h, final Runnable renderer) {
    final NkRect rect = NkRect.mallocStack(stack);

    if(nk_begin(manager.ctx, title, nk_recti(x, y, w, h, rect), NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_TITLE)) {
      renderer.run();
    }
  }

  protected void simpleWindow(final GuiManager manager, final MemoryStack stack, final String title, final int x, final int y, final int w, final int h, final Runnable renderer) {
    final NkRect rect = NkRect.mallocStack(stack);

    if(nk_begin(manager.ctx, title, nk_recti(x, y, w, h, rect), NK_WINDOW_BACKGROUND)) {
      renderer.run();
    }
  }

  protected void row(final GuiManager manager, final float height, final int itemWidth, final int columns) {
    nk_layout_row_static(manager.ctx, height, itemWidth, columns);
  }

  protected void row(final GuiManager manager, final float height, final int columns) {
    nk_layout_row_dynamic(manager.ctx, height, columns);
  }

  protected void row(final GuiManager manager, final float height, final int columns, final Consumer<Row> rowBuilder) {
    nk_layout_row_begin(manager.ctx, NK_DYNAMIC, height, columns);
    rowBuilder.accept(new Row(manager));
    nk_layout_row_end(manager.ctx);
  }

  protected void rowStatic(final GuiManager manager, final float height, final int columns, final Consumer<Row> rowBuilder) {
    nk_layout_row_begin(manager.ctx, NK_STATIC, height, columns);
    rowBuilder.accept(new Row(manager));
    nk_layout_row_end(manager.ctx);
  }

  protected void label(final GuiManager manager, final String title, final TextAlign align) {
    nk_label(manager.ctx, title, align.align);
  }

  protected void label(final GuiManager manager, final String title) {
    this.label(manager, title, TextAlign.LEFT);
  }

  protected void defaultTextbox(final GuiManager manager) {
    if(this.firstDraw) {
      nk_edit_focus(manager.ctx, NK_EDIT_FIELD | NK_EDIT_SIG_ENTER);
    }
  }

  protected int textbox(final GuiManager manager, final ByteBuffer text, final IntBuffer length, final int maxLength) {
    return nk_edit_string(manager.ctx, NK_EDIT_FIELD | NK_EDIT_SIG_ENTER, text, length, maxLength + 1, Nuklear::nnk_filter_ascii);
  }

  protected void button(final GuiManager manager, final String title, final Runnable onClick) {
    if(nk_button_label(manager.ctx, title)) {
      onClick.run();
    }
  }

  protected void option(final GuiManager manager, final String title, final boolean active, final Runnable onClick) {
    if(nk_option_label(manager.ctx, title, active)) {
      onClick.run();
    }
  }

  protected void property(final GuiManager manager, final String title, final int min, final int max, final int step, final int incPerPixel, final IntBuffer value) {
    nk_property_int(manager.ctx, title, min, value, max, step, incPerPixel);
  }

  protected void property(final GuiManager manager, final String title, final float min, final float max, final float step, final float incPerPixel, final FloatBuffer value) {
    nk_property_float(manager.ctx, title, min, value, max, step, incPerPixel);
  }

  protected void property(final GuiManager manager, final String title, final double min, final double max, final double step, final float incPerPixel, final DoubleBuffer value) {
    nk_property_double(manager.ctx, title, min, value, max, step, incPerPixel);
  }

  public enum TextAlign {
    LEFT(NK_TEXT_LEFT),
    CENTRED(NK_TEXT_CENTERED),
    RIGHT(NK_TEXT_RIGHT),
    ;

    private final int align;

    TextAlign(final int align) {
      this.align = align;
    }
  }

  public final static class Row {
    private final GuiManager manager;

    private Row(final GuiManager manager) {
      this.manager = manager;
    }

    public void nextColumn(final float width) {
      nk_layout_row_push(this.manager.ctx, width);
    }
  }
}
