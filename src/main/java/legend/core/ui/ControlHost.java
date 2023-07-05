package legend.core.ui;

import legend.core.opengl.MatrixStack;
import legend.core.opengl.ScissorStack;
import legend.core.ui.layouts.GridLayout;
import legend.core.ui.layouts.Layout;
import legend.game.input.InputAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class ControlHost implements Iterable<Control> {
  private final List<Control> controls = new ArrayList<>();

  protected int mouseX;
  protected int mouseY;

  private Layout layout = new GridLayout();

  public Layout getLayout() {
    return this.layout;
  }

  public <T extends Layout> T setLayout(final T layout) {
    this.layout = layout;
    return layout;
  }

  protected abstract Screen getScreen();
  protected abstract ControlHost getParent();
  protected abstract int getX();
  protected abstract int getY();
  protected abstract int getWidth();
  protected abstract int getHeight();

  public int calculateTotalX() {
    int x = this.getX();

    ControlHost current = this;
    while((current = current.getParent()) != null) {
      x += current.getX();
    }

    return x;
  }

  public int calculateTotalY() {
    int y = this.getY();

    ControlHost current = this;
    while((current = current.getParent()) != null) {
      y += current.getY();
    }

    return y;
  }

  public <T extends Control> T addControl(final T control) {
    control.setScreen(this.getScreen());
    control.setParent(this);
    this.controls.add(control);
    this.controls.sort(Comparator.comparingInt(Control::getOrder));
    return control;
  }

  public void removeControl(final Control control) {
    this.controls.remove(control);
  }

  public <T extends Control> Optional<T> findControl(final Class<T> type, final Predicate<T> predicate) {
    return this.controls.stream()
      .filter(type::isInstance)
      .map(type::cast)
      .filter(predicate)
      .findFirst();
  }

  void renderControls(final MatrixStack matrixStack, final ScissorStack scissorStack) {
    this.controls.forEach(control -> control.renderControl(matrixStack, scissorStack));
  }

  void updateControlLayout(final int parentWidth, final int parentHeight) {
    for(int i = 0; i < this.controls.size(); i++) {
      final Control control = this.controls.get(i);

      this.layout.updateLayout(parentWidth, parentHeight, i, control);
      control.updateLayout();
      control.updateControlLayout(control.getWidth(), control.getHeight());
    }
  }

  @Override
  public Iterator<Control> iterator() {
    return this.controls.iterator();
  }

  protected Control findControlAt(final int x, final int y, final Predicate<Control> predicate) {
    final List<Control> controls = new ArrayList<>();

    for(final Control control : this.controls) {
      if(predicate.test(control) && control.containsPoint(x, y)) {
        final Control subcontrol = control.findControlAt(x - control.getX(), y - control.getY(), predicate);
        controls.add(subcontrol != null ? subcontrol : control);
      }
    }

    if(controls.isEmpty()) {
      return null;
    }

    controls.sort(Comparator.comparingInt(Control::getOrder).reversed());
    return controls.get(0);
  }

  protected InputPropagation mouseMove(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;

    final Control control = this.findControlAt(x, y, Control::isInteractable);

    if(control != null && !control.isDisabled()) {
      return control.mouseMove(x - control.getX(), y - control.getY());
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    final Control control = this.findControlAt(x, y, Control::isInteractable);

    if(control != null && !control.isDisabled()) {
      return control.mouseClick(x - control.getX(), y - control.getY(), button, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    final Control control = this.findControlAt(this.mouseX, this.mouseY, Control::isInteractable);

    if(control != null && !control.isDisabled()) {
      return control.mouseScroll(deltaX, deltaY);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    final Control control = this.findControlAt(this.mouseX, this.mouseY, Control::isInteractable);

    if(control != null && !control.isDisabled()) {
      return control.mouseScrollHighRes(deltaX, deltaY);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation charPress(final int codepoint) {
    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation releasedThisFrame(final InputAction inputAction) {
    return InputPropagation.PROPAGATE;
  }
}
