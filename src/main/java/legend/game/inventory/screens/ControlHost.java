package legend.game.inventory.screens;

import legend.game.input.InputAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ControlHost implements Iterable<Control> {
  private final List<Control> controls = new ArrayList<>();

  protected int mouseX;
  protected int mouseY;

  public <T extends Control> T addControl(final T control) {
    this.controls.add(control);
    this.controls.sort(Comparator.comparingInt(Control::getZ));
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

  protected void renderControls(final int parentX, final int parentY) {
    this.controls.forEach(control -> control.renderControl(parentX, parentY));
  }

  @Override
  public Iterator<Control> iterator() {
    return this.controls.iterator();
  }

  protected Control findControlAt(final int x, final int y) {
    final List<Control> controls = new ArrayList<>();

    for(final Control control : this.controls) {
      if(control.acceptsInput() && control.isVisible() && control.containsPoint(x, y)) {
        controls.add(control);
      }
    }

    if(controls.isEmpty()) {
      return null;
    }

    controls.sort(Comparator.comparingInt(Control::getZ).reversed());
    return controls.get(0);
  }

  protected InputPropagation mouseMove(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;

    final Control control = this.findControlAt(x, y);

    if(control != null) {
      return control.mouseMove(x - control.getX(), y - control.getY());
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    final Control control = this.findControlAt(x, y);

    if(control != null) {
      return control.mouseClick(x - control.getX(), y - control.getY(), button, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseScroll(final double deltaX, final double deltaY) {
    final Control control = this.findControlAt(this.mouseX, this.mouseY);

    if(control != null) {
      return control.mouseScroll(deltaX, deltaY);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
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
