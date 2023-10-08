package legend.core.ui;

import legend.core.ProjectionMode;
import legend.core.RenderEngine;
import legend.core.opengl.MatrixStack;
import legend.core.opengl.ScissorStack;
import legend.game.input.InputAction;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Screen extends ControlHost {
  private final Queue<Runnable> deferredActions = new LinkedList<>();

  private ScreenStack stack;
  private boolean initialized;

  private Control hover;
  private Control focus;

  private int x;
  private int y;
  private int w;
  private int h;

  private ProjectionMode projectionMode = ProjectionMode._2D;
  /** 0 means use screen aspect ratio */
  private float aspectRatio;
  private final HorizontalAlign horizontalAlign = HorizontalAlign.CENTRE;
  private final VerticalAlign verticalAlign = VerticalAlign.MIDDLE;

  void setStack(@Nullable final ScreenStack stack) {
    this.stack = stack;
  }

  public ScreenStack getStack() {
    return this.stack;
  }

  @Override
  protected Screen getScreen() {
    return this;
  }

  @Override
  protected ControlHost getParent() {
    return null;
  }

  @Override
  protected int getX() {
    return this.x;
  }

  @Override
  protected int getY() {
    return this.y;
  }

  @Override
  public int getWidth() {
    return this.w;
  }

  @Override
  public int getHeight() {
    return this.h;
  }

  public ProjectionMode getProjectionMode() {
    return this.projectionMode;
  }

  public void setProjectionMode(final ProjectionMode projectionMode) {
    this.projectionMode = projectionMode;
  }

  public float getAspectRatio() {
    return this.aspectRatio;
  }

  public void setAspectRatio(final float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  protected abstract void initialize();
  protected abstract void renderBackground(final MatrixStack matrixStack);
  protected abstract void renderForeground(final MatrixStack matrixStack);

  final void updateLayout(final int windowWidth, final int windowHeight) {
    final float aspect;

    if(this.getAspectRatio() == 0.0f) {
      aspect = (float)windowWidth / windowHeight;
    } else {
      aspect = this.getAspectRatio();
    }

    this.w = windowWidth;
    this.h = Math.round(this.w / aspect);

    if(this.h > windowHeight) {
      this.h = windowHeight;
      this.w = Math.round(this.h * aspect);
    }

    switch(this.horizontalAlign) {
      case LEFT -> this.x = 0;
      case CENTRE -> this.x = (windowWidth - this.w) / 2;
      case RIGHT -> this.x = windowWidth - this.w;
    }

    switch(this.verticalAlign) {
      case TOP -> this.y = 0;
      case MIDDLE -> this.y = (windowHeight - this.h) / 2;
      case BOTTOM -> this.y = windowHeight - this.h;
    }

    this.updateControlLayout(this.getWidth(), this.getHeight());
  }

  final void renderScreen(final RenderEngine engine, final MatrixStack matrixStack, final ScissorStack scissorStack) {
    if(!this.initialized) {
      this.initialize();
      this.updateLayout(engine.window().getWidth(), engine.window().getHeight());
      this.initialized = true;
    }

    engine.setProjectionMode(this.projectionMode);

    matrixStack.push();
    matrixStack.translate(this.getX(), this.getY(), 0.0f);
    scissorStack.push(this.getX(), this.getY(), this.getWidth(), this.getHeight());

    this.runDeferredActions();
    this.renderBackground(matrixStack);
    this.renderControls(matrixStack, scissorStack);
    this.renderForeground(matrixStack);

    scissorStack.pop();
    matrixStack.pop();
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.updateHover(x, y);
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    this.updateHover(x, y);
    this.updateFocus(x, y);

    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    if(super.keyPress(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.keyPress(key, scancode, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.charPress(codepoint);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.pressedThisFrame(inputAction);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.pressedWithRepeatPulse(inputAction);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation releasedThisFrame(final InputAction inputAction) {
    if(super.releasedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.releasedThisFrame(inputAction);
    }

    return InputPropagation.PROPAGATE;
  }

  private void updateHover(final int x, final int y) {
    final Control hover = this.findControlAt(x, y, Control::isInteractable);

    if(hover != this.hover) {
      if(this.hover != null) {
        this.hover.hoverOut();
      }

      this.hover = hover;

      if(this.hover != null) {
        this.hover.hoverIn();
      }
    }
  }

  private void updateFocus(final int x, final int y) {
    final Control focus = this.findControlAt(x, y, Control::isInteractable);

    if(focus != this.focus) {
      this.setFocus(focus);
    }
  }

  public void setFocus(@Nullable final Control control) {
    if(this.focus == control) {
      return;
    }

    if(this.focus != null) {
      this.focus.lostFocus();
    }

    this.focus = control;

    if(this.focus != null) {
      this.focus.gotFocus();
    }
  }

  @Nullable
  public Control getFocus() {
    return this.focus;
  }

  protected boolean propagateRender() {
    return false;
  }

  protected boolean propagateInput() {
    return false;
  }

  protected void deferAction(final Runnable action) {
    synchronized(this.deferredActions) {
      this.deferredActions.add(action);
    }
  }

  protected void runDeferredActions() {
    synchronized(this.deferredActions) {
      Runnable action;
      while((action = this.deferredActions.poll()) != null) {
        action.run();
      }
    }
  }
}
