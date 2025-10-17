package legend.core.platform;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.FailedToLoadDeviceException;
import legend.core.platform.input.IgnoreSteamInputMode;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActionState;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputAxisDirection;
import legend.core.platform.input.InputBinding;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputGamepadType;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.core.platform.input.SdlGamepadDevice;
import legend.game.modding.events.input.InputPressedEvent;
import legend.game.modding.events.input.InputReleasedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.lwjgl.sdl.SDL_Event;
import org.lwjgl.sdl.SDL_GamepadAxisEvent;
import org.lwjgl.sdl.SDL_GamepadButtonEvent;
import org.lwjgl.sdl.SDL_GamepadDeviceEvent;
import org.lwjgl.sdl.SDL_KeyboardEvent;
import org.lwjgl.sdl.SDL_MouseButtonEvent;
import org.lwjgl.sdl.SDL_MouseMotionEvent;
import org.lwjgl.sdl.SDL_MouseWheelEvent;
import org.lwjgl.sdl.SDL_TextInputEvent;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.platform.SdlInput.decodeMods;
import static legend.core.platform.SdlInput.getAxisCode;
import static legend.core.platform.SdlInput.getButtonCode;
import static legend.core.platform.SdlInput.getInputFromAxisCode;
import static legend.core.platform.SdlInput.getInputFromButton;
import static legend.core.platform.SdlInput.getInputFromKeycode;
import static legend.core.platform.SdlInput.getInputKeyFromScanCode;
import static legend.core.platform.SdlInput.getKeyCode;
import static legend.core.platform.SdlInput.getScanCode;
import static legend.game.modding.coremod.CoreMod.IGNORE_STEAM_INPUT_MODE_CONFIG;
import static legend.game.modding.coremod.CoreMod.MENU_INNER_DEADZONE_CONFIG;
import static legend.game.modding.coremod.CoreMod.MENU_OUTER_DEADZONE_CONFIG;
import static legend.game.modding.coremod.CoreMod.MOVEMENT_INNER_DEADZONE_CONFIG;
import static legend.game.modding.coremod.CoreMod.MOVEMENT_OUTER_DEADZONE_CONFIG;
import static legend.game.modding.coremod.CoreMod.RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG;
import static org.lwjgl.sdl.SDLError.SDL_GetError;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_ADDED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_AXIS_MOTION;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_BUTTON_DOWN;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_BUTTON_UP;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_REMAPPED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_GAMEPAD_REMOVED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_KEY_DOWN;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_KEY_UP;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_MOUSE_BUTTON_DOWN;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_MOUSE_BUTTON_UP;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_MOUSE_MOTION;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_MOUSE_WHEEL;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_TEXT_INPUT;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_CLOSE_REQUESTED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_FOCUS_GAINED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_FOCUS_LOST;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_RESIZED;
import static org.lwjgl.sdl.SDLEvents.SDL_GetWindowFromEvent;
import static org.lwjgl.sdl.SDLEvents.SDL_PollEvent;
import static org.lwjgl.sdl.SDLGamepad.SDL_AddGamepadMappingsFromFile;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadStringForAxis;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadStringForButton;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepads;
import static org.lwjgl.sdl.SDLGamepad.SDL_OpenGamepad;
import static org.lwjgl.sdl.SDLHints.SDL_HINT_JOYSTICK_ALLOW_BACKGROUND_EVENTS;
import static org.lwjgl.sdl.SDLHints.SDL_HINT_WINDOWS_RAW_KEYBOARD;
import static org.lwjgl.sdl.SDLHints.SDL_SetHint;
import static org.lwjgl.sdl.SDLInit.SDL_INIT_GAMEPAD;
import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;
import static org.lwjgl.sdl.SDLInit.SDL_Quit;
import static org.lwjgl.sdl.SDLKeyboard.SDL_GetKeyName;
import static org.lwjgl.sdl.SDLKeyboard.SDL_GetScancodeName;
import static org.lwjgl.sdl.SDLMisc.SDL_OpenURL;
import static org.lwjgl.sdl.SDLMouse.SDL_BUTTON_LEFT;
import static org.lwjgl.sdl.SDLStdinc.SDL_free;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_GetCurrentContext;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplayName;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplays;

public class SdlPlatformManager extends PlatformManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SdlPlatformManager.class);
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");
  private static final Marker ACTIONS_MARKER = MarkerManager.getMarker("ACTIONS");

  private static final Object INPUT_LOCK = new Object();

  private final Long2ObjectMap<SdlWindow> windows = new Long2ObjectOpenHashMap<>();
  private SdlWindow lastActiveWindow;
  private SDL_Event event;

  private final Set<InputAction> pressed = new HashSet<>();
  private final Map<InputAction, InputActionState> actionStates = new HashMap<>();
  private boolean clearActionStates;

  private final Int2ObjectMap<SdlGamepadDevice> gamepads = new Int2ObjectOpenHashMap<>();
  private SdlGamepadDevice lastGamepad;
  private SdlGamepadDevice lastRumbleGamepad;

  private float rumbleBigCurrentIntensity;
  private float rumbleSmallCurrentIntensity;
  private float rumbleBigStartingIntensity;
  private float rumbleSmallStartingIntensity;
  private float rumbleBigEndingIntensity;
  private float rumbleSmallEndingIntensity;
  private long rumbleLerpStart;
  private long rumbleLerpDuration;

  @Override
  public void init() {
    SDL_SetHint(SDL_HINT_WINDOWS_RAW_KEYBOARD, "1");
    SDL_SetHint(SDL_HINT_JOYSTICK_ALLOW_BACKGROUND_EVENTS, "1");
//    SDL_SetHint("SDL_GAMECONTROLLER_ALLOW_STEAM_VIRTUAL_GAMEPAD", "1");

    if(!SDL_Init(SDL_INIT_VIDEO | SDL_INIT_GAMEPAD)) {
      this.logLastError();
      throw new IllegalStateException("Unable to initialize SDL3");
    }

    this.event = SDL_Event.malloc();

    SDL_AddGamepadMappingsFromFile("gamecontrollerdb.txt");

    this.gamepads.clear();
    for(final SdlGamepadDevice gamepad : this.loadGamepads()) {
      this.gamepads.put(gamepad.id, gamepad);
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    this.event.free();
    SDL_Quit();
  }

  @Override
  public boolean isContextCurrent() {
    return SDL_GL_GetCurrentContext() != 0;
  }

  @Override
  public boolean hasGamepad() {
    return !this.gamepads.isEmpty();
  }

  @Override
  public InputGamepadType getGamepadType() {
    return this.lastGamepad != null ? this.lastGamepad.type : InputGamepadType.STANDARD;
  }

  @Override
  public int getMouseButton(final int index) {
    return SDL_BUTTON_LEFT + index;
  }

  @Override
  public String[] listDisplays() {
    final IntBuffer ptrs = SDL_GetDisplays();
    final String[] displays = new String[ptrs.limit()];

    for(int i = 0; i < displays.length; i++) {
      displays[i] = SDL_GetDisplayName(ptrs.get(i));
    }

    SDL_free(ptrs);
    return displays;
  }

  public List<SdlGamepadDevice> loadGamepads() {
    LOGGER.info("Loading gamepads...");

    final IntBuffer ids = SDL_GetGamepads();

    if(ids == null) {
      this.logLastError();
      return List.of();
    }

    final List<SdlGamepadDevice> gamepads = new ArrayList<>();

    for(int i = 0; i < ids.limit(); i++) {
      try {
        final int id = ids.get(i);
        final SdlGamepadDevice gamepad = new SdlGamepadDevice(id, SDL_OpenGamepad(id));
        gamepads.add(gamepad);
        LOGGER.info("Found gamepad %s", gamepad.name);
      } catch(final FailedToLoadDeviceException e) {
        this.logLastError();
      }
    }

    SDL_free(ids);
    return gamepads;
  }

  @Override
  protected Window createWindow(final String title, final int width, final int height) {
    final SdlWindow window = new SdlWindow(this, title, width, height);
    this.windows.put(window.getWindowPtr(), window);
    return window;
  }

  @Override
  protected void removeWindows(final Collection<Window> windows) {
    this.windows.values().removeAll(windows);
  }

  private SdlWindow getWindow(final SDL_Event event) {
    return this.windows.get(SDL_GetWindowFromEvent(event));
  }

  @Override
  public SdlWindow getLastWindow() {
    return this.lastActiveWindow;
  }

  @Override
  public void resetActionStates() {
    this.clearActionStates = true;
  }

  // These are used to disable keyboard input while controller buttons are held. This is a dumb
  // workaround for dumb Steam's Steam Input repeating all controller input as keyboard input.
  private final IntSet ignoredKeys = new IntOpenHashSet();
  private final IntSet axesHeld = new IntOpenHashSet();
  private int buttonsHeld;
  //

  @Override
  protected void tickInput() {
    synchronized(INPUT_LOCK) {
      while(SDL_PollEvent(this.event)) {
        switch(this.event.type()) {
          case SDL_EVENT_WINDOW_CLOSE_REQUESTED -> this.getWindow(this.event).close();
          case SDL_EVENT_WINDOW_FOCUS_GAINED -> {
            final SdlWindow window = this.getWindow(this.event);
            this.lastActiveWindow = window;
            window.hasFocus = true;
            window.events().onFocus(true);
          }

          case SDL_EVENT_WINDOW_FOCUS_LOST -> {
            final SdlWindow window = this.getWindow(this.event);
            window.hasFocus = false;
            window.events().onFocus(false);
          }

          case SDL_EVENT_WINDOW_RESIZED -> {
            final SdlWindow window = this.getWindow(this.event);
            window.updateSize();
            window.events().onResize(window.getWidth(), window.getHeight());
          }

          case SDL_EVENT_KEY_DOWN, SDL_EVENT_KEY_UP -> {
            final SDL_KeyboardEvent key = this.event.key();

            if(LOGGER.isInfoEnabled(INPUT_MARKER)) {
              if(key.down()) {
                LOGGER.info(INPUT_MARKER, "Key down %s mods %#x repeat %b", SDL_GetKeyName(key.key()), key.mod(), key.repeat());
              } else {
                LOGGER.info(INPUT_MARKER, "Key up %s mods %#x repeat %b", SDL_GetKeyName(key.key()), key.mod(), key.repeat());
              }
            }

            final IgnoreSteamInputMode ignoreSteamInputMode = CONFIG.getConfig(IGNORE_STEAM_INPUT_MODE_CONFIG.get());

/*TODO          if(ignoreSteamInputMode == IgnoreSteamInputMode.VIRTUAL_KEYBOARD_ID) {
            if(key.which() == 0) {
              break;
            }
          } else */
            if(ignoreSteamInputMode == IgnoreSteamInputMode.IGNORE_WHEN_GAMEPAD_USED) {
              // Ignore keys if gamepad buttons are held. Workaround for Steam Input.
              if(this.buttonsHeld != 0 || !this.axesHeld.isEmpty()) {
                if(this.event.type() == SDL_EVENT_KEY_DOWN) {
                  if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                    LOGGER.info(ACTIONS_MARKER, "Ignoring key %s", SDL_GetKeyName(key.key()));
                  }

                  this.ignoredKeys.add(key.scancode());
                }

                break;
              }

              if(this.event.type() == SDL_EVENT_KEY_UP && this.ignoredKeys.contains(key.scancode())) {
                if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                  LOGGER.info(ACTIONS_MARKER, "Removing ignored key %s", SDL_GetKeyName(key.key()));
                }

                this.ignoredKeys.remove(key.scancode());
                break;
              }
            }

            final SdlWindow window = this.getWindow(this.event);

            if(window == null) {
              continue;
            }

            this.setWindowInputClass(window, InputClass.KEYBOARD);
            decodeMods(key, window.mods);

            final InputKey inputKey = getInputFromKeycode(key.key());

            if(inputKey == null) {
              LOGGER.warn("Unknown key %s", SDL_GetKeyName(key.key()));
              continue;
            }

            final InputKey inputScan = getInputKeyFromScanCode(key.scancode());

            if(key.down()) {
              if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                LOGGER.info(ACTIONS_MARKER, "Triggering press key %s -> %s", SDL_GetKeyName(key.key()), inputKey);
              }

              window.events().onKeyPress(inputKey, inputScan, window.mods, key.repeat());
            } else {
              if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                LOGGER.info(ACTIONS_MARKER, "Triggering release key %s -> %s", SDL_GetKeyName(key.key()), inputKey);
              }

              window.events().onKeyRelease(inputKey, inputScan, window.mods);
            }

            final List<InputBinding<KeyInputActivation>> keycodeBindings = InputBindings.getBindings(KeyInputActivation.class);

            for(int i = 0; i < keycodeBindings.size(); i++) {
              final InputBinding<KeyInputActivation> binding = keycodeBindings.get(i);

              if(getKeyCode(binding.activation.key) == key.key()) {
                if(key.down()) {
                  if((binding.activation.mods.isEmpty() || window.mods.containsAll(binding.activation.mods)) && !key.repeat()) {
                    if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                      LOGGER.info(ACTIONS_MARKER, "Triggering press input key %s -> %s -> %s", SDL_GetKeyName(key.key()), inputKey, binding.action);
                    }

                    this.pressed.add(binding.action);
                    this.triggerBindingPress(binding);
                  }
                } else {
                  if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                    LOGGER.info(ACTIONS_MARKER, "Triggering release input key %s -> %s -> %s", SDL_GetKeyName(key.key()), inputKey, binding.action);
                  }

                  this.triggerBindingRelease(binding);
                }
              }
            }

            final List<InputBinding<ScancodeInputActivation>> scancodeBindings = InputBindings.getBindings(ScancodeInputActivation.class);

            for(int i = 0; i < scancodeBindings.size(); i++) {
              final InputBinding<ScancodeInputActivation> binding = scancodeBindings.get(i);

              if(getScanCode(binding.activation.key) == key.scancode()) {
                if(key.down()) {
                  if((binding.activation.mods.isEmpty() || window.mods.containsAll(binding.activation.mods)) && !key.repeat()) {
                    if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                      LOGGER.info(ACTIONS_MARKER, "Triggering press input action scancode %s -> %s", SDL_GetScancodeName(key.scancode()), binding.action);
                    }

                    this.pressed.add(binding.action);
                    window.events().onInputActionPressed(binding.action, false);
                    this.getInputActionState(binding.action).press();
                    EVENTS.postEvent(new InputPressedEvent(binding.action, false));
                  }
                } else {
                  if(LOGGER.isInfoEnabled(ACTIONS_MARKER)) {
                    LOGGER.info(ACTIONS_MARKER, "Triggering release input action scancode %s -> %s", SDL_GetScancodeName(key.scancode()), binding.action);
                  }

                  window.events().onInputActionReleased(binding.action);
                  this.getInputActionState(binding.action).release();
                  EVENTS.postEvent(new InputReleasedEvent(binding.action));
                }
              }
            }
          }

          case SDL_EVENT_TEXT_INPUT -> {
            // Text event doesn't tell us which device it came from so we have to ignore input the less good way
            // Ignore keys if gamepad buttons are held. Workaround for Steam Input.
            if(this.buttonsHeld != 0 || !this.axesHeld.isEmpty()) {
              break;
            }

            final SDL_TextInputEvent text = this.event.text();
            final SdlWindow window = this.getWindow(this.event);
            this.setWindowInputClass(window, InputClass.KEYBOARD);
            final String string = text.textString();

            LOGGER.info(INPUT_MARKER, "Text %s", string);

            for(int i = 0; i < string.length(); i++) {
              window.events().onChar(string.charAt(i));
            }
          }

          case SDL_EVENT_MOUSE_MOTION -> {
            final SDL_MouseMotionEvent mouse = this.event.motion();
            final SdlWindow window = this.getWindow(this.event);

            if(window != null) {
              this.setWindowInputClass(window, InputClass.KEYBOARD);

              final boolean relative = mouse.which() != 0;
              window.events().onMouseMove(relative ? mouse.xrel() : mouse.x(), relative ? mouse.yrel() : mouse.y());
            }
          }

          case SDL_EVENT_MOUSE_BUTTON_DOWN, SDL_EVENT_MOUSE_BUTTON_UP -> {
            final SDL_MouseButtonEvent mouse = this.event.button();
            final SdlWindow window = this.getWindow(this.event);

            if(window != null) {
              this.setWindowInputClass(window, InputClass.KEYBOARD);

              if(mouse.down()) {
                this.getWindow(this.event).events().onMousePress(mouse.button(), window.mods);
              } else {
                this.getWindow(this.event).events().onMouseRelease(mouse.button(), window.mods);
              }
            }
          }

          case SDL_EVENT_MOUSE_WHEEL -> {
            final SDL_MouseWheelEvent wheel = this.event.wheel();
            final SdlWindow window = this.getWindow(this.event);

            if(window != null) {
              this.setWindowInputClass(window, InputClass.KEYBOARD);
              window.events().onMouseScroll(wheel.x(), wheel.y());
            }
          }

          case SDL_EVENT_GAMEPAD_ADDED -> {
            final SDL_GamepadDeviceEvent device = this.event.gdevice();

            try {
              final SdlGamepadDevice gamepad = new SdlGamepadDevice(device.which(), SDL_OpenGamepad(device.which()));
              this.gamepads.put(device.which(), gamepad);
              this.lastGamepad = gamepad;
              LOGGER.info("Gamepad %s connected", gamepad.name);
            } catch(final FailedToLoadDeviceException e) {
              this.logLastError();
            }
          }

          case SDL_EVENT_GAMEPAD_REMOVED -> {
            final SDL_GamepadDeviceEvent device = this.event.gdevice();
            final SdlGamepadDevice gamepad = this.gamepads.get(device.which());

            if(gamepad != null) {
              LOGGER.info("Gamepad %s disconnected", gamepad.name);

              if(this.lastGamepad == gamepad) {
                this.lastGamepad = null;
              }
            }

            this.gamepads.remove(device.which());
          }

          case SDL_EVENT_GAMEPAD_REMAPPED -> {
            //TODO wtf is this
            final SDL_GamepadDeviceEvent device = this.event.gdevice();
            System.out.println(device.which());
          }

          case SDL_EVENT_GAMEPAD_AXIS_MOTION -> {
            final SDL_GamepadAxisEvent axis = this.event.gaxis();

            if(LOGGER.isInfoEnabled(INPUT_MARKER) && axis.value() > 1000) {
              LOGGER.info(INPUT_MARKER, "Axis gamepad %d axis %d value %d", axis.which(), axis.axis(), axis.value());
            }

            if(this.lastActiveWindow != null && (this.lastActiveWindow.hasFocus || CONFIG.getConfig(RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get()))) {
              final float menuInnerDeadzone = CONFIG.getConfig(MENU_INNER_DEADZONE_CONFIG.get());
              final float menuOuterDeadzone = CONFIG.getConfig(MENU_OUTER_DEADZONE_CONFIG.get());
              final float movementInnerDeadzone = CONFIG.getConfig(MOVEMENT_INNER_DEADZONE_CONFIG.get());
              final float movementOuterDeadzone = CONFIG.getConfig(MOVEMENT_OUTER_DEADZONE_CONFIG.get());
              final float minInner = Math.min(menuInnerDeadzone, movementInnerDeadzone);
              final float maxOuter = Math.min(menuOuterDeadzone, movementOuterDeadzone);

              if(Math.abs(axis.value()) >= minInner * 0x7fff) {
                this.setWindowInputClass(this.lastActiveWindow, InputClass.GAMEPAD);
                this.lastGamepad = this.gamepads.get(axis.which());
                this.axesHeld.add(axis.axis());
                final float menuValue = Math.min(1.0f, (Math.abs(axis.value()) / (float)0x7fff - menuInnerDeadzone) / (Math.abs(menuOuterDeadzone - menuInnerDeadzone)));
                final float movementValue = Math.min(1.0f, (Math.abs(axis.value()) / (float)0x7fff - movementInnerDeadzone) / (Math.abs(movementOuterDeadzone - movementInnerDeadzone)));
                this.lastActiveWindow.events().onAxis(getInputFromAxisCode(axis.axis()), InputAxisDirection.getDirection(axis.value()), menuValue, movementValue);
              } else {
                this.axesHeld.remove(axis.axis());
              }

              final List<InputBinding<AxisInputActivation>> axisBindings = InputBindings.getBindings(AxisInputActivation.class);

              for(int i = 0; i < axisBindings.size(); i++) {
                final InputBinding<AxisInputActivation> binding = axisBindings.get(i);

                if(getAxisCode(binding.activation.axis) == axis.axis()) {
                  final InputAxisDirection direction = InputAxisDirection.getDirection(axis.value());
                  final InputActionState state = this.getInputActionState(binding.action);

                  if(binding.activation.direction == direction) {
                    final float inner;
                    final float outer;
                    if(binding.action.useMovementDeadzone) {
                      inner = movementInnerDeadzone;
                      outer = movementOuterDeadzone;
                    } else {
                      inner = menuInnerDeadzone;
                      outer = menuOuterDeadzone;
                    }

                    final float value = Math.min(1.0f, (Math.abs(axis.value()) / (float)0x7fff - inner) / (Math.abs(outer - inner)));

                    if(value >= 0.0f) {
                      if(!state.isHeld()) {
                        LOGGER.info(ACTIONS_MARKER, "Triggering press input action axis gamepad %d axis %d value %d -> %s", axis.which(), axis.axis(), axis.value(), binding.action);
                        this.pressed.add(binding.action);
                        this.lastActiveWindow.events().onInputActionPressed(binding.action, false);
                        state.press();
                        EVENTS.postEvent(new InputPressedEvent(binding.action, false));
                      }

                      state.axis(value * Math.signum(axis.value()));
                    } else if(state.isHeld() && state.getAxis() != 0.0f) {
                      LOGGER.info(ACTIONS_MARKER, "Triggering release A input action axis gamepad %d axis %d value %d -> %s", axis.which(), axis.axis(), axis.value(), binding.action);
                      this.lastActiveWindow.events().onInputActionReleased(binding.action);
                      state.release();
                      EVENTS.postEvent(new InputReleasedEvent(binding.action));
                    }
                  } else if(state.isHeld() && state.getAxis() != 0.0f) {
                    LOGGER.info(ACTIONS_MARKER, "Triggering release B input action axis gamepad %d axis %d value %d -> %s", axis.which(), axis.axis(), axis.value(), binding.action);
                    this.lastActiveWindow.events().onInputActionReleased(binding.action);
                    state.release();
                    EVENTS.postEvent(new InputReleasedEvent(binding.action));
                  }
                }
              }
            }
          }

          case SDL_EVENT_GAMEPAD_BUTTON_DOWN -> {
            this.buttonsHeld++;

            final SDL_GamepadButtonEvent button = this.event.gbutton();

            LOGGER.info(INPUT_MARKER, "Button down gamepad %d button %d", button.which(), button.button());

            if(this.lastActiveWindow != null && (this.lastActiveWindow.hasFocus || CONFIG.getConfig(RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get()))) {
              this.setWindowInputClass(this.lastActiveWindow, InputClass.GAMEPAD);
              this.lastGamepad = this.gamepads.get(button.which());
              this.lastActiveWindow.events().onButtonPress(getInputFromButton(button.button()), false);

              final List<InputBinding<ButtonInputActivation>> bindings = InputBindings.getBindings(ButtonInputActivation.class);

              for(int i = 0; i < bindings.size(); i++) {
                final InputBinding<ButtonInputActivation> binding = bindings.get(i);

                if(getButtonCode(binding.activation.button) == button.button()) {
                  LOGGER.info(ACTIONS_MARKER, "Triggering press input action button gamepad %d button %d -> %s", button.which(), button.button(), binding.action);
                  this.triggerBindingPress(binding);
                }
              }
            }
          }

          case SDL_EVENT_GAMEPAD_BUTTON_UP -> {
            this.buttonsHeld--;

            if(this.buttonsHeld < 0) {
              this.buttonsHeld = 0;
            }

            final SDL_GamepadButtonEvent button = this.event.gbutton();

            LOGGER.info(INPUT_MARKER, "Button up gamepad %d button %d", button.which(), button.button());

            if(this.lastActiveWindow != null && (this.lastActiveWindow.hasFocus || CONFIG.getConfig(RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get()))) {
              this.setWindowInputClass(this.lastActiveWindow, InputClass.GAMEPAD);
              this.lastGamepad = this.gamepads.get(button.which());
              this.lastActiveWindow.events().onButtonRelease(getInputFromButton(button.button()));

              final List<InputBinding<ButtonInputActivation>> bindings = InputBindings.getBindings(ButtonInputActivation.class);

              for(int i = 0; i < bindings.size(); i++) {
                final InputBinding<ButtonInputActivation> binding = bindings.get(i);

                if(getButtonCode(binding.activation.button) == button.button()) {
                  LOGGER.info(ACTIONS_MARKER, "Triggering release input action button gamepad %d button %d -> %s", button.which(), button.button(), binding.action);
                  this.triggerBindingRelease(binding);
                }
              }
            }
          }
        }
      }
    }

    if(this.lastActiveWindow != null) {
      if(this.lastActiveWindow.hasFocus || CONFIG.getConfig(RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get())) {
        for(final var entry : this.actionStates.entrySet()) {
          final InputAction action = entry.getKey();
          final InputActionState state = entry.getValue();

          if(state.repeat()) {
            LOGGER.info(ACTIONS_MARKER, "Triggering repeat input action %s", action);
            this.lastActiveWindow.events().onInputActionPressed(action, true);
            EVENTS.postEvent(new InputPressedEvent(action, true));
          }
        }
      } else {
        this.pressed.clear();
        this.ignoredKeys.clear();
        this.axesHeld.clear();
        this.buttonsHeld = 0;

        for(final var entry : this.actionStates.entrySet()) {
          final InputAction action = entry.getKey();
          final InputActionState state = entry.getValue();

          if(state.isHeld()) {
            LOGGER.info(ACTIONS_MARKER, "Triggering release input action %s", action);
            this.lastActiveWindow.events().onInputActionReleased(action);
            state.release();
            EVENTS.postEvent(new InputReleasedEvent(action));
          }
        }
      }
    }

    if(this.rumbleLerpStart != 0) {
      final long time = System.nanoTime() - this.rumbleLerpStart;
      final float ratio = java.lang.Math.clamp(time / (float)this.rumbleLerpDuration, 0.0f, 1.0f);
      final float big = Math.lerp(this.rumbleBigStartingIntensity, this.rumbleBigEndingIntensity, ratio);
      final float small = Math.lerp(this.rumbleSmallStartingIntensity, this.rumbleSmallEndingIntensity, ratio);
      this.rumble(big, small, 0);

      if(time >= this.rumbleLerpDuration) {
        this.rumbleLerpStart = 0;
      }
    }

    if(this.clearActionStates) {
      this.clearActionStates = false;
      this.actionStates.clear();
    }
  }

  public void triggerBindingPress(final InputBinding<?> binding) {
    this.pressed.add(binding.action);
    this.lastActiveWindow.events().onInputActionPressed(binding.action, false);
    this.getInputActionState(binding.action).press();
    EVENTS.postEvent(new InputPressedEvent(binding.action, false));
  }

  public void triggerBindingRelease(final InputBinding<?> binding) {
    this.lastActiveWindow.events().onInputActionReleased(binding.action);
    this.getInputActionState(binding.action).release();
    EVENTS.postEvent(new InputReleasedEvent(binding.action));
  }

  @Override
  public void clearPressed() {
    synchronized(INPUT_LOCK) {
      this.pressed.clear();
    }
  }

  @Override
  public void rumble(final float intensity, final int ms) {
    this.rumble(intensity, intensity, ms);
  }

  @Override
  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) {
    this.rumbleBigCurrentIntensity = bigIntensity;
    this.rumbleSmallCurrentIntensity = smallIntensity;

    if(this.lastRumbleGamepad != this.lastGamepad) {
      if(this.lastRumbleGamepad != null) {
        this.lastRumbleGamepad.stopRumble();
      }

      this.lastRumbleGamepad = this.lastGamepad;
    }

    if(this.lastRumbleGamepad != null) {
      this.lastRumbleGamepad.rumble(bigIntensity, smallIntensity, ms);
    }
  }

  @Override
  public void adjustRumble(final float intensity, final int ms) {
    this.adjustRumble(intensity, intensity, ms);
  }

  @Override
  public void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) {
    this.rumbleBigStartingIntensity = this.rumbleBigCurrentIntensity;
    this.rumbleSmallStartingIntensity = this.rumbleSmallCurrentIntensity;
    this.rumbleBigEndingIntensity = bigIntensity;
    this.rumbleSmallEndingIntensity = smallIntensity;
    this.rumbleLerpStart = System.nanoTime();
    this.rumbleLerpDuration = ms * 1_000_000L;
  }

  @Override
  public void stopRumble() {
    if(this.lastRumbleGamepad != null) {
      this.lastRumbleGamepad.stopRumble();
    }
  }

  private InputActionState getInputActionState(final InputAction action) {
    return this.actionStates.computeIfAbsent(action, key -> new InputActionState());
  }

  @Override
  public boolean isActionPressed(final InputAction action) {
    return this.pressed.contains(action);
  }

  @Override
  public boolean isActionRepeat(final InputAction action) {
    return this.getInputActionState(action).isRepeat();
  }

  @Override
  public boolean isActionHeld(final InputAction action) {
    return this.getInputActionState(action).isHeld();
  }

  @Override
  public float getAxis(final InputAction action) {
    return this.getInputActionState(action).getAxis();
  }

  private void setWindowInputClass(final SdlWindow window, final InputClass classification) {
    if(window.currentInputClass != classification) {
      window.currentInputClass = classification;
      window.events().onInputClassChanged(classification);
    }
  }

  @Override
  public String getKeyName(final InputKey key) {
    final int code = getKeyCode(key);
    return SDL_GetKeyName(code);
  }

  @Override
  public String getScancodeName(final InputKey key) {
    final int code = getScanCode(key);
    return SDL_GetScancodeName(code);
  }

  @Override
  public String getButtonName(final InputButton button) {
    final int code = getButtonCode(button);
    return SDL_GetGamepadStringForButton(code);
  }

  @Override
  public String getAxisName(final InputAxis axis) {
    final int code = getAxisCode(axis);
    return SDL_GetGamepadStringForAxis(code);
  }

  @Override
  public void openUrl(final String url) {
    if(!SDL_OpenURL(url)) {
      this.logLastError();
    }
  }

  private void logLastError() {
    LOGGER.error(SDL_GetError());
  }
}
