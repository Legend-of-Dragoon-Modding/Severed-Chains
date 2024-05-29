package legend.core.opengl;

import org.lwjgl.nuklear.NkColor;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeType;

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
import static org.lwjgl.nuklear.Nuklear.nk_draw_image;
import static org.lwjgl.nuklear.Nuklear.nk_edit_focus;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_image_id;
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
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static org.lwjgl.nuklear.Nuklear.nk_recti;
import static org.lwjgl.nuklear.Nuklear.nk_rgb;
import static org.lwjgl.nuklear.Nuklear.nk_stroke_rect;
import static org.lwjgl.nuklear.Nuklear.nk_window_get_canvas;
import static org.lwjgl.nuklear.Nuklear.nnk_edit_string;
import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.Checks.checkNT1;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAddress;

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
    final NkRect rect = NkRect.malloc(stack);

    if(nk_begin(manager.ctx, title, nk_recti(x, y, w, h, rect), NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_TITLE)) {
      renderer.run();
    }
  }

  protected void simpleWindow(final GuiManager manager, final MemoryStack stack, final String title, final int x, final int y, final int w, final int h, final Runnable renderer) {
    final NkRect rect = NkRect.malloc(stack);

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
    return nk_edit_string(manager.ctx, NK_EDIT_FIELD | NK_EDIT_SIG_ENTER, text, length, maxLength + 1, GuiManager.FILTER_ASCII);
  }

  protected int numberbox(final GuiManager manager, final ByteBuffer text, final IntBuffer length, final int maxLength) {
    return nk_edit_string(manager.ctx, NK_EDIT_FIELD | NK_EDIT_SIG_ENTER, text, length, maxLength + 1, GuiManager.FILTER_DECIMAL);
  }

  /**
   * Fix memory leak <a href="https://github.com/LWJGL/lwjgl3/issues/959">(issue here)</a>
   */
  @NativeType("nk_flags")
  public static int nk_edit_string(@NativeType("struct nk_context *") final NkContext ctx, @NativeType("nk_flags") final int flags, @NativeType("char *") final ByteBuffer memory, @NativeType("int *") final IntBuffer len, final int max, final long filter) {
    if(CHECKS) {
      checkNT1(memory);
      check(len, 1);
    }
    return nnk_edit_string(ctx.address(), flags, memAddress(memory), memAddress(len), max, filter);
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

  protected void image(final GuiManager manager, final MemoryStack stack, final Texture texture, final float x, final float y) {
    final NkColor colour = NkColor.malloc(stack);
    nk_rgb(255, 255, 255, colour);

    final NkRect rect = NkRect.malloc(stack);
    nk_rect(x, y, texture.width, texture.height, rect);

    final NkImage image = NkImage.malloc(stack);
    nk_image_id(texture.id, image);
    nk_draw_image(nk_window_get_canvas(manager.ctx), rect, image, colour);
  }

  protected void rect(final GuiManager manager, final MemoryStack stack, final float x, final float y, final float w, final float h, final int r, final int g, final int b) {
    final NkColor colour = NkColor.malloc(stack);
    nk_rgb(r, g, b, colour);

    final NkRect rect = NkRect.malloc(stack);
    nk_rect(x, y, w, h, rect);

    nk_stroke_rect(nk_window_get_canvas(manager.ctx), rect, 0.0f, 1.0f, colour);
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
