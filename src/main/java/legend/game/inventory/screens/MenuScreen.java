package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputCodepoints;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.SItem;
import legend.game.i18n.I18n;
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
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;

public abstract class MenuScreen extends ControlHost {
  private final Queue<Runnable> deferredActions = new LinkedList<>();

  private MenuStack stack;

  private Control hover;
  private Control focus;

  private final FontOptions fontOptions = new FontOptions().size(0.66f).colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);
  private final List<Hotkey> hotkeys = new ArrayList<>();

  public void addHotkey(final String label, final RegistryDelegate<InputAction> action, final Runnable handler) {
    this.hotkeys.add(new Hotkey(label, action, handler));
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

    float offsetX = 0.0f;
    for(int i = 0; i < this.hotkeys.size(); i++) {
      final Hotkey hotkey = this.hotkeys.get(i);
      final String string = I18n.translate("lod_core.ui.hotkey", hotkey.label, InputCodepoints.getActionName(hotkey.action.get()));
      renderText(string, 8 + offsetX, 228, this.fontOptions);
      offsetX += (textWidth(string) + 12.0f) * this.fontOptions.getSize();
    }
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
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && PLATFORM.hasGamepad()) {
      return InputPropagation.HANDLED;
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

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.buttonRelease(button);
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
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
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

    if(this.focus != null && !this.focus.isDisabled()) {
      return this.focus.inputActionReleased(action);
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

  private static class Hotkey {
    private final String label;
    private final RegistryDelegate<InputAction> action;
    private final Runnable handler;

    private Hotkey(final String label, final RegistryDelegate<InputAction> action, final Runnable handler) {
      this.label = label;
      this.action = action;
      this.handler = handler;
    }
  }
}
