package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputAxisDirection;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputCodepoints;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Checkbox;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.game.sound.Audio.playMenuSound;

public abstract class MenuScreen extends ControlHost {
  private final Queue<Runnable> deferredActions = new LinkedList<>();

  private MenuStack stack;

  private Control hover;
  private Control focus;

  private final List<Hotkey> hotkeys = new ArrayList<>();
  private int hotkeyX = 8;

  public void addHotkey(final String label, final RegistryDelegate<InputAction> action, final Runnable handler) {
    final Button button = this.addControl(new Button(I18n.translate("lod_core.ui.hotkey", label, InputCodepoints.getActionName(action.get()))));
    button.setScale(0.66f);
    button.setSize((int)(button.getFont().textWidth(button.getText()) * button.getFontOptions().getSize() + 10), 10);
    button.setPos(this.hotkeyX, 227);
    button.onPressed(handler::run);
    button.onHoverIn(() -> playMenuSound(1));
    this.hotkeyX += button.getWidth();

    this.hotkeys.add(new Hotkey(label, action, handler, button));
  }

  public void addToggleHotkey(final String label, final RegistryDelegate<InputAction> action, final boolean checked, final BooleanConsumer handler) {
    final Checkbox checkbox = this.addControl(new Checkbox());
    checkbox.setSize(10, 10);
    checkbox.setPos(this.hotkeyX, 226);
    checkbox.onToggled(handler);
    checkbox.setChecked(checked);
    this.hotkeyX += checkbox.getWidth() + 3;

    final Label checkboxLabel = this.addControl(new Label(I18n.translate("lod_core.ui.hotkey", label, InputCodepoints.getActionName(action.get()))));
    checkboxLabel.setScale(0.66f);
    checkboxLabel.setSize((int)(checkboxLabel.getFont().textWidth(checkboxLabel.getText()) * checkboxLabel.getFontOptions().getSize() + 10), 10);
    checkboxLabel.setPos(this.hotkeyX, 228);
    this.hotkeyX += checkboxLabel.getWidth() - 5;

    this.hotkeys.add(new Hotkey(label, action, () -> {
      playMenuSound(2);
      checkbox.setChecked(!checkbox.isChecked());
    }, checkbox, checkboxLabel));
  }

  private void updateHotkeys() {
    this.hotkeyX = 8;
    for(int hotkeyIndex = 0; hotkeyIndex < this.hotkeys.size(); hotkeyIndex++) {
      final Hotkey hotkey = this.hotkeys.get(hotkeyIndex);
      for(int controlIndex = 0; controlIndex < hotkey.controls.length; controlIndex++) {
        final Control control = hotkey.controls[controlIndex];
        control.setX(this.hotkeyX);
        this.hotkeyX += control.getWidth();

        if(control instanceof final Button button) {
          button.setText(I18n.translate("lod_core.ui.hotkey", hotkey.label, InputCodepoints.getActionName(hotkey.action.get())));
          button.setSize((int)(button.getFont().textWidth(button.getText()) * button.getFontOptions().getSize() + 10), 10);
        } else if(control instanceof Checkbox) {
          this.hotkeyX += 3;
        } else if(control instanceof final Label label) {
          label.setText(I18n.translate("lod_core.ui.hotkey", hotkey.label, InputCodepoints.getActionName(hotkey.action.get())));
          this.hotkeyX -= 5;
        }
      }
    }
  }

  void setStack(@Nullable final MenuStack stack) {
    this.stack = stack;
  }

  public MenuStack getStack() {
    return this.stack;
  }

  @Override
  protected MenuScreen getScreen() {
    return this;
  }

  @Override
  protected ControlHost getParent() {
    return null;
  }

  @Override
  protected int getX() {
    return 0;
  }

  @Override
  protected int getY() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 368;
  }

  @Override
  public int getHeight() {
    return 240;
  }

  protected abstract void render();

  protected void renderNumber(final int x, final int y, final int value, final int digitCount) {
    SItem.renderNumber(x, y, value, 0x2, digitCount);
  }

  protected void renderNumber(final int x, final int y, final int value, final int digitCount, final int flags) {
    SItem.renderNumber(x, y, value, flags | 0x2, digitCount);
  }

  final void renderScreen() {
    this.runDeferredActions();
    this.render();
    this.renderControls(0, 0);
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.mouseMove(x, y) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    this.updateHover(x, y);
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && PLATFORM.hasGamepad()) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    this.updateHover(x, y);
    this.updateFocus(x, y);

    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyPress(final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat) {
    if(super.keyPress(key, scancode, mods, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.keyPress(key, scancode, mods, repeat) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.keyPress(key, scancode, mods, repeat);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyRelease(final InputKey key, final InputKey scancode, final Set<InputMod> mods) {
    if(super.keyRelease(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.keyRelease(key, scancode, mods) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.keyRelease(key, scancode, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation buttonPress(final InputButton button, final boolean repeat) {
    if(super.buttonPress(button, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.buttonPress(button, repeat) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.buttonPress(button, repeat);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation buttonRelease(final InputButton button) {
    if(super.buttonRelease(button) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.buttonRelease(button) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.buttonRelease(button);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation axis(final InputAxis axis, final InputAxisDirection direction, final float menuValue, final float movementValue) {
    if(super.axis(axis, direction, menuValue, movementValue) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.axis(axis, direction, menuValue, movementValue) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.axis(axis, direction, menuValue, movementValue);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.charPress(codepoint) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.charPress(codepoint);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      if(this.focus.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }
    }

    if(!repeat) {
      for(int i = 0; i < this.hotkeys.size(); i++) {
        final Hotkey hotkey = this.hotkeys.get(i);

        if(action == hotkey.action.get()) {
          hotkey.handler.run();
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(final Control control : this) {
      if(control.alwaysReceiveInput) {
        if(control.inputActionReleased(action) == InputPropagation.HANDLED) {
          return InputPropagation.HANDLED;
        }
      }
    }

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.inputActionReleased(action);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputClassChanged(final InputClass type) {
    this.updateHotkeys();

    if(super.inputClassChanged(type) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.getControls().size(); i++) {
      if(this.getControls().get(i).inputClassChanged(type) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void updateHover(final int x, final int y) {
    final Control hover = this.findControlAt(x, y, true);

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
    final Control focus = this.findControlAt(x, y);

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

  public void deferAction(final Runnable action) {
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

  private static class Hotkey {
    private final String label;
    private final RegistryDelegate<InputAction> action;
    private final Runnable handler;
    private final Control[] controls;

    private Hotkey(final String label, final RegistryDelegate<InputAction> action, final Runnable handler, final Control... controls) {
      this.label = label;
      this.action = action;
      this.handler = handler;
      this.controls = controls;
    }
  }
}
