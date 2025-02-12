package legend.game.inventory.screens;

import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.modding.coremod.CoreMod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static legend.core.GameEngine.CONFIG;

public abstract class ControlHost implements Iterable<Control> {
  private final List<Control> controls = new ArrayList<>();

  protected int mouseX;
  protected int mouseY;

  protected abstract MenuScreen getScreen();
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
    this.controls.sort(Comparator.comparingInt(Control::getZ));
    return control;
  }

  public void removeControl(final Control control) {
    this.controls.remove(control);
    control.delete();
  }

  protected void delete() {
    for(final Control control : this.controls) {
      control.delete();
    }
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

  protected List<Control> getControls() {
    return this.controls;
  }

  protected Control getControl(final int index) {
    return index > -1 && index < this.controls.size() ? this.controls.get(index) : null;
  }

  protected Control findControlAt(final int x, final int y) {
    return this.findControlAt(x, y, false);
  }

  protected Control findControlAt(final int x, final int y, final boolean nested) {
    final List<Control> controls = new ArrayList<>();

    for(final Control control : this.controls) {
      if(control.acceptsInput() && control.isVisible() && control.containsPoint(x, y)) {
        final Control subcontrol;
        if(nested) {
          subcontrol = control.findControlAt(x - control.getX(), y - control.getY());
        } else {
          subcontrol = null;
        }

        controls.add(subcontrol != null ? subcontrol : control);
      }
    }

    if(controls.isEmpty()) {
      return null;
    }

    controls.sort(Comparator.comparingInt(Control::getZ).reversed());
    return controls.get(0);
  }

  protected InputPropagation mouseMove(final int x, final int y) {
    if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && !Input.getController().getGuid().isEmpty()) {
      return InputPropagation.HANDLED;
    }

    this.mouseX = x;
    this.mouseY = y;

    final Control control = this.findControlAt(x, y);

    if(control != null && !control.isDisabled()) {
      return control.mouseMove(x - control.getX(), y - control.getY());
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && !Input.getController().getGuid().isEmpty()) {
      return InputPropagation.HANDLED;
    }

    final Control control = this.findControlAt(x, y);

    if(control != null && !control.isDisabled()) {
      return control.mouseClick(x - control.getX(), y - control.getY(), button, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    final Control control = this.findControlAt(this.mouseX, this.mouseY);

    if(control != null && !control.isDisabled()) {
      return control.mouseScroll(deltaX, deltaY);
    }

    return InputPropagation.PROPAGATE;
  }

  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    final Control control = this.findControlAt(this.mouseX, this.mouseY);

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
