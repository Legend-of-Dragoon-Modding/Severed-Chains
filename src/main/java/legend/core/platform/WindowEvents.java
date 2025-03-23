package legend.core.platform;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class WindowEvents {
  private static final Object LOCK = new Object();

  private final List<Resize> resize = new ArrayList<>();
  private final List<Focus> gotFocus = new ArrayList<>();
  private final List<Focus> lostFocus = new ArrayList<>();
  private final List<KeyPressed> keyPress = new ArrayList<>();
  private final List<KeyReleased> keyRelease = new ArrayList<>();
  private final List<ButtonPressed> buttonPress = new ArrayList<>();
  private final List<ButtonReleased> buttonRelease = new ArrayList<>();
  private final List<Char> charPress = new ArrayList<>();
  private final List<Cursor> mouseMove = new ArrayList<>();
  private final List<Click> mousePress = new ArrayList<>();
  private final List<Click> mouseRelease = new ArrayList<>();
  private final List<Scroll> mouseScroll = new ArrayList<>();
  private final List<ControllerState> controllerConnected = new ArrayList<>();
  private final List<ControllerState> controllerDisconnected = new ArrayList<>();
  private final List<InputClassChanged> inputClassChanged = new ArrayList<>();
  private final List<InputActionPressed> inputActionPressed = new ArrayList<>();
  private final List<InputActionReleased> inputActionReleased = new ArrayList<>();
  private final List<Runnable> draw = new ArrayList<>();
  private final List<Runnable> close = new ArrayList<>();
  private final Window window;

  private double mouseX;
  private double mouseY;

  WindowEvents(final Window window) {
    this.window = window;
  }

  void onResize(final int width, final int height) {
    synchronized(LOCK) {
      this.resize.forEach(cb -> cb.resize(this.window, width, height));
    }
  }

  public Resize onResize(final Resize callback) {
    synchronized(LOCK) {
      this.resize.add(callback);
      return callback;
    }
  }

  public void removeOnResize(final Resize callback) {
    synchronized(LOCK) {
      this.resize.remove(callback);
    }
  }

  void onFocus(final boolean focus) {
    synchronized(LOCK) {
      if(focus) {
        this.gotFocus.forEach(cb -> cb.focus(this.window));
      } else {
        this.lostFocus.forEach(cb -> cb.focus(this.window));
      }
    }
  }

  public Focus onGotFocus(final Focus callback) {
    synchronized(LOCK) {
      this.gotFocus.add(callback);
      return callback;
    }
  }

  public Focus onLostFocus(final Focus callback) {
    synchronized(LOCK) {
      this.lostFocus.add(callback);
      return callback;
    }
  }

  public void removeOnGotFocus(final Focus callback) {
    synchronized(LOCK) {
      this.gotFocus.remove(callback);
    }
  }

  public void removeOnLostFocus(final Focus callback) {
    synchronized(LOCK) {
      this.lostFocus.remove(callback);
    }
  }

  void onKeyPress(final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat) {
    this.keyPress.forEach(cb -> cb.action(this.window, key, scancode, mods, repeat));
  }

  void onKeyRelease(final InputKey key, final InputKey scancode, final Set<InputMod> mods) {
    this.keyRelease.forEach(cb -> cb.action(this.window, key, scancode, mods));
  }

  void onButtonPress(final InputButton button, final boolean repeat) {
    this.buttonPress.forEach(cb -> cb.action(this.window, button, repeat));
  }

  void onButtonRelease(final InputButton button) {
    this.buttonRelease.forEach(cb -> cb.action(this.window, button));
  }

  void onChar(final int codepoint) {
    synchronized(LOCK) {
      this.charPress.forEach(cb -> cb.action(this.window, codepoint));
    }
  }

  void onMouseMove(final double x, final double y) {
    synchronized(LOCK) {
      this.mouseX = x;
      this.mouseY = y;
      this.mouseMove.forEach(cb -> cb.action(this.window, x, y));
    }
  }

  void onMousePress(final int button, final Set<InputMod> mods) {
    this.mousePress.forEach(cb -> cb.action(this.window, this.mouseX, this.mouseY, button, mods));
  }

  void onMouseRelease(final int button, final Set<InputMod> mods) {
    this.mouseRelease.forEach(cb -> cb.action(this.window, this.mouseX, this.mouseY, button, mods));
  }

  void onMouseScroll(final double deltaX, final double deltaY) {
    synchronized(LOCK) {
      this.mouseScroll.forEach(cb -> cb.action(this.window, deltaX, deltaY));
    }
  }

  void onInputClassChanged(final InputClass classification) {
    synchronized(LOCK) {
      this.inputClassChanged.forEach(cb -> cb.action(this.window, classification));
    }
  }

  void onInputActionPressed(final InputAction action, final boolean repeat) {
    synchronized(LOCK) {
      this.inputActionPressed.forEach(cb -> cb.action(this.window, action, repeat));
    }
  }

  void onInputActionReleased(final InputAction action) {
    synchronized(LOCK) {
      this.inputActionReleased.forEach(cb -> cb.action(this.window, action));
    }
  }

  void onControllerConnected(final int id) {
    synchronized(LOCK) {
      this.controllerConnected.forEach(cb -> cb.action(this.window, id));
    }
  }

  void onControllerDisconnected(final int id) {
    synchronized(LOCK) {
      this.controllerDisconnected.forEach(cb -> cb.action(this.window, id));
    }
  }

  public KeyPressed onKeyPress(final KeyPressed callback) {
    synchronized(LOCK) {
      this.keyPress.add(callback);
      return callback;
    }
  }

  public void removeKeyPress(final KeyPressed callback) {
    synchronized(LOCK) {
      this.keyPress.remove(callback);
    }
  }

  public KeyReleased onKeyRelease(final KeyReleased callback) {
    synchronized(LOCK) {
      this.keyRelease.add(callback);
      return callback;
    }
  }

  public void removeKeyRelease(final KeyReleased callback) {
    synchronized(LOCK) {
      this.keyRelease.remove(callback);
    }
  }

  public ButtonPressed onButtonPress(final ButtonPressed callback) {
    synchronized(LOCK) {
      this.buttonPress.add(callback);
      return callback;
    }
  }

  public void removeButtonPress(final ButtonPressed callback) {
    synchronized(LOCK) {
      this.buttonPress.remove(callback);
    }
  }

  public ButtonReleased onButtonRelease(final ButtonReleased callback) {
    synchronized(LOCK) {
      this.buttonRelease.add(callback);
      return callback;
    }
  }

  public void removeButtonRelease(final ButtonReleased callback) {
    synchronized(LOCK) {
      this.buttonRelease.remove(callback);
    }
  }

  public Char onCharPress(final Char callback) {
    synchronized(LOCK) {
      this.charPress.add(callback);
      return callback;
    }
  }

  public void removeCharPress(final Char callback) {
    synchronized(LOCK) {
      this.charPress.remove(callback);
    }
  }

  public Cursor onMouseMove(final Cursor callback) {
    synchronized(LOCK) {
      this.mouseMove.add(callback);
      return callback;
    }
  }

  public void removeMouseMove(final Cursor callback) {
    synchronized(LOCK) {
      this.mouseMove.remove(callback);
    }
  }

  public Click onMousePress(final Click callback) {
    synchronized(LOCK) {
      this.mousePress.add(callback);
      return callback;
    }
  }

  public void removeMousePress(final Click callback) {
    synchronized(LOCK) {
      this.mousePress.remove(callback);
    }
  }

  public Click onMouseRelease(final Click callback) {
    synchronized(LOCK) {
      this.mouseRelease.add(callback);
      return callback;
    }
  }

  public void removeMouseRelease(final Click callback) {
    synchronized(LOCK) {
      this.mouseRelease.remove(callback);
    }
  }

  public Scroll onMouseScroll(final Scroll callback) {
    synchronized(LOCK) {
      this.mouseScroll.add(callback);
      return callback;
    }
  }

  public void removeMouseScroll(final Scroll callback) {
    synchronized(LOCK) {
      this.mouseScroll.remove(callback);
    }
  }

  public InputClassChanged onInputClassChanged(final InputClassChanged callback) {
    synchronized(LOCK) {
      this.inputClassChanged.add(callback);
      return callback;
    }
  }

  public void removeInputClassChanged(final InputClassChanged callback) {
    synchronized(LOCK) {
      this.inputClassChanged.remove(callback);
    }
  }

  public InputActionPressed onInputActionPressed(final InputActionPressed callback) {
    synchronized(LOCK) {
      this.inputActionPressed.add(callback);
      return callback;
    }
  }

  public void removeInputActionPressed(final InputActionPressed callback) {
    synchronized(LOCK) {
      this.inputActionPressed.remove(callback);
    }
  }

  public InputActionReleased onInputActionReleased(final InputActionReleased callback) {
    synchronized(LOCK) {
      this.inputActionReleased.add(callback);
      return callback;
    }
  }

  public void removeInputActionReleased(final InputActionReleased callback) {
    synchronized(LOCK) {
      this.inputActionReleased.remove(callback);
    }
  }

  public ControllerState onControllerConnected(final ControllerState callback) {
    synchronized(LOCK) {
      this.controllerConnected.add(callback);
      return callback;
    }
  }

  public void removeControllerConnected(final ControllerState callback) {
    synchronized(LOCK) {
      this.controllerConnected.remove(callback);
    }
  }

  public ControllerState onControllerDisconnected(final ControllerState callback) {
    synchronized(LOCK) {
      this.controllerDisconnected.add(callback);
      return callback;
    }
  }

  public void removeControllerDisconnected(final ControllerState callback) {
    synchronized(LOCK) {
      this.controllerDisconnected.remove(callback);
    }
  }

  void onDraw() {
    synchronized(LOCK) {
      for(int i = 0; i < this.draw.size(); i++) {
        this.draw.get(i).run();
      }
    }
  }

  public Runnable onDraw(final Runnable callback) {
    synchronized(LOCK) {
      this.draw.add(callback);
      return callback;
    }
  }

  public void removeDraw(final Runnable callback) {
    synchronized(LOCK) {
      this.draw.remove(callback);
    }
  }

  void onClose() {
    synchronized(LOCK) {
      for(final Runnable close : this.close) {
        close.run();
      }
    }
  }

  public Runnable onClose(final Runnable callback) {
    synchronized(LOCK) {
      this.close.add(callback);
      return callback;
    }
  }

  public void removeClose(final Runnable callback) {
    synchronized(LOCK) {
      this.close.remove(callback);
    }
  }

  @FunctionalInterface
  public interface Resize {
    void resize(final Window window, final int width, final int height);
  }

  @FunctionalInterface
  public interface Focus {
    void focus(final Window window);
  }

  @FunctionalInterface
  public interface KeyPressed {
    void action(final Window window, final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat);
  }

  @FunctionalInterface
  public interface KeyReleased {
    void action(final Window window, final InputKey key, final InputKey scancode, final Set<InputMod> mods);
  }

  @FunctionalInterface
  public interface ButtonPressed {
    void action(final Window window, final InputButton button, final boolean repeat);
  }

  @FunctionalInterface
  public interface ButtonReleased {
    void action(final Window window, final InputButton button);
  }

  @FunctionalInterface
  public interface Char {
    void action(final Window window, final int codepoint);
  }

  @FunctionalInterface
  public interface Cursor {
    void action(final Window window, final double x, final double y);
  }

  @FunctionalInterface
  public interface Click {
    void action(final Window window, final double x, final double y, final int button, final Set<InputMod> mods);
  }

  @FunctionalInterface
  public interface Scroll {
    void action(final Window window, final double deltaX, final double deltaY);
  }

  @FunctionalInterface
  public interface ControllerState {
    void action(final Window window, final int id);
  }

  @FunctionalInterface
  public interface InputClassChanged {
    void action(final Window window, final InputClass classification);
  }

  @FunctionalInterface
  public interface InputActionPressed {
    void action(final Window window, final InputAction action, final boolean repeat);
  }

  @FunctionalInterface
  public interface InputActionReleased {
    void action(final Window window, final InputAction action);
  }
}
