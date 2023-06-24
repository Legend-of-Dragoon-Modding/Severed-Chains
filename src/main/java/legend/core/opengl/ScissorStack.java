package legend.core.opengl;

import legend.core.gpu.Rect4i;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glScissor;

public class ScissorStack {
  private final Window window;
  private final LinkedList<Rect4i> stack = new LinkedList<>();

  public ScissorStack(final Window window) {
    this.window = window;
  }

  public void push(final int x, final int y, final int width, final int height) {
    if(this.stack.isEmpty()) {
      this.stack.push(new Rect4i(x, y, width, height));
    } else {
      final Rect4i top = this.top();
      final int newX = top.x() + x;
      final int newY = top.y() + y;
      final int newW = Math.min(newX + width, top.right()) - newX;
      final int newH = Math.min(newY + height, top.bottom()) - newY;
      this.stack.push(new Rect4i(newX, newY, newW, newH));
    }

    this.update();
  }

  public void pop() {
    this.stack.pop();
    this.update();
  }

  public Rect4i top() {
    return this.stack.element();
  }

  private void update() {
    if(!this.stack.isEmpty()) {
      glEnable(GL_SCISSOR_TEST);
      final float scale = this.window.getScale();
      final Rect4i top = this.top();
      glScissor((int)(top.x() * scale), (int)((this.window.getHeight() - top.h() - top.y()) * scale), (int)(top.w() * scale), (int)(top.h() * scale));
    } else {
      glDisable(GL_SCISSOR_TEST);
    }
  }
}
