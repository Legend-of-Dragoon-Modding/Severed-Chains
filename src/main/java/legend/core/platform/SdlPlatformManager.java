package legend.core.platform;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import legend.core.MathHelper;
import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.FailedToLoadDeviceException;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActionState;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputAxisDirection;
import legend.core.platform.input.InputBinding;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.core.platform.input.SdlGamepadDevice;
import legend.game.modding.events.input.InputPressedEvent;
import legend.game.modding.events.input.InputReleasedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.lwjgl.sdl.SDL_Event;
import org.lwjgl.sdl.SDL_GamepadAxisEvent;
import org.lwjgl.sdl.SDL_GamepadButtonEvent;
import org.lwjgl.sdl.SDL_GamepadDeviceEvent;
import org.lwjgl.sdl.SDL_KeyboardEvent;
import org.lwjgl.sdl.SDL_MouseButtonEvent;
import org.lwjgl.sdl.SDL_MouseMotionEvent;
import org.lwjgl.sdl.SDL_MouseWheelEvent;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.EVENTS;
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
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_CLOSE_REQUESTED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_FOCUS_GAINED;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_FOCUS_LOST;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_RESIZED;
import static org.lwjgl.sdl.SDLEvents.SDL_GetWindowFromEvent;
import static org.lwjgl.sdl.SDLEvents.SDL_PollEvent;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_LEFTX;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_LEFTY;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_LEFT_TRIGGER;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTX;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTY;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_AXIS_RIGHT_TRIGGER;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_BACK;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_DPAD_DOWN;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_DPAD_LEFT;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_DPAD_RIGHT;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_DPAD_UP;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_EAST;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_LEFT_SHOULDER;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_LEFT_STICK;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_NORTH;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_RIGHT_SHOULDER;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_RIGHT_STICK;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_SOUTH;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_START;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_BUTTON_WEST;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepads;
import static org.lwjgl.sdl.SDLGamepad.SDL_OpenGamepad;
import static org.lwjgl.sdl.SDLInit.SDL_INIT_GAMEPAD;
import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;
import static org.lwjgl.sdl.SDLInit.SDL_Quit;
import static org.lwjgl.sdl.SDLKeycode.SDLK_0;
import static org.lwjgl.sdl.SDLKeycode.SDLK_1;
import static org.lwjgl.sdl.SDLKeycode.SDLK_2;
import static org.lwjgl.sdl.SDLKeycode.SDLK_3;
import static org.lwjgl.sdl.SDLKeycode.SDLK_4;
import static org.lwjgl.sdl.SDLKeycode.SDLK_5;
import static org.lwjgl.sdl.SDLKeycode.SDLK_6;
import static org.lwjgl.sdl.SDLKeycode.SDLK_7;
import static org.lwjgl.sdl.SDLKeycode.SDLK_8;
import static org.lwjgl.sdl.SDLKeycode.SDLK_9;
import static org.lwjgl.sdl.SDLKeycode.SDLK_A;
import static org.lwjgl.sdl.SDLKeycode.SDLK_AMPERSAND;
import static org.lwjgl.sdl.SDLKeycode.SDLK_APOSTROPHE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_ASTERISK;
import static org.lwjgl.sdl.SDLKeycode.SDLK_AT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_B;
import static org.lwjgl.sdl.SDLKeycode.SDLK_BACKSLASH;
import static org.lwjgl.sdl.SDLKeycode.SDLK_BACKSPACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_C;
import static org.lwjgl.sdl.SDLKeycode.SDLK_CARET;
import static org.lwjgl.sdl.SDLKeycode.SDLK_COLON;
import static org.lwjgl.sdl.SDLKeycode.SDLK_COMMA;
import static org.lwjgl.sdl.SDLKeycode.SDLK_D;
import static org.lwjgl.sdl.SDLKeycode.SDLK_DBLAPOSTROPHE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_DELETE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_DOLLAR;
import static org.lwjgl.sdl.SDLKeycode.SDLK_DOWN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_E;
import static org.lwjgl.sdl.SDLKeycode.SDLK_END;
import static org.lwjgl.sdl.SDLKeycode.SDLK_EQUALS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_ESCAPE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_EXCLAIM;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F1;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F10;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F11;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F12;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F13;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F14;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F15;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F16;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F17;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F18;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F19;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F2;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F20;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F21;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F22;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F23;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F24;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F3;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F4;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F5;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F6;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F7;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F8;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F9;
import static org.lwjgl.sdl.SDLKeycode.SDLK_G;
import static org.lwjgl.sdl.SDLKeycode.SDLK_GRAVE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_GREATER;
import static org.lwjgl.sdl.SDLKeycode.SDLK_H;
import static org.lwjgl.sdl.SDLKeycode.SDLK_HASH;
import static org.lwjgl.sdl.SDLKeycode.SDLK_HOME;
import static org.lwjgl.sdl.SDLKeycode.SDLK_I;
import static org.lwjgl.sdl.SDLKeycode.SDLK_INSERT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_J;
import static org.lwjgl.sdl.SDLKeycode.SDLK_K;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_0;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_1;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_2;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_3;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_4;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_5;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_6;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_7;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_8;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_9;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_DIVIDE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_ENTER;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_EQUALS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_MINUS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_MULTIPLY;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_PERIOD;
import static org.lwjgl.sdl.SDLKeycode.SDLK_KP_PLUS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_L;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LALT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LCTRL;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LEFT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LEFTBRACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LEFTBRACKET;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LEFTPAREN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LESS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LGUI;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LSHIFT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_M;
import static org.lwjgl.sdl.SDLKeycode.SDLK_MINUS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_N;
import static org.lwjgl.sdl.SDLKeycode.SDLK_O;
import static org.lwjgl.sdl.SDLKeycode.SDLK_P;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PAGEDOWN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PAGEUP;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PAUSE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PERCENT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PERIOD;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PIPE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PLUS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PLUSMINUS;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PRINTSCREEN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_Q;
import static org.lwjgl.sdl.SDLKeycode.SDLK_QUESTION;
import static org.lwjgl.sdl.SDLKeycode.SDLK_R;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RALT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RCTRL;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RETURN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RGUI;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RIGHT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RIGHTBRACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RIGHTBRACKET;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RIGHTPAREN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RSHIFT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_S;
import static org.lwjgl.sdl.SDLKeycode.SDLK_SCROLLLOCK;
import static org.lwjgl.sdl.SDLKeycode.SDLK_SEMICOLON;
import static org.lwjgl.sdl.SDLKeycode.SDLK_SLASH;
import static org.lwjgl.sdl.SDLKeycode.SDLK_SPACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_T;
import static org.lwjgl.sdl.SDLKeycode.SDLK_TAB;
import static org.lwjgl.sdl.SDLKeycode.SDLK_TILDE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_U;
import static org.lwjgl.sdl.SDLKeycode.SDLK_UNDERSCORE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_UP;
import static org.lwjgl.sdl.SDLKeycode.SDLK_V;
import static org.lwjgl.sdl.SDLKeycode.SDLK_W;
import static org.lwjgl.sdl.SDLKeycode.SDLK_X;
import static org.lwjgl.sdl.SDLKeycode.SDLK_Y;
import static org.lwjgl.sdl.SDLKeycode.SDLK_Z;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_ALT;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_CTRL;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_SHIFT;
import static org.lwjgl.sdl.SDLMouse.SDL_BUTTON_LEFT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_0;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_1;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_2;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_3;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_4;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_5;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_6;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_7;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_8;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_9;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_A;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_APOSTROPHE;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_B;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_BACKSLASH;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_BACKSPACE;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_C;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_COMMA;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_D;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_DOWN;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_E;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_EQUALS;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_ESCAPE;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F1;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F10;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F11;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F12;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F2;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F3;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F4;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F5;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F6;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F7;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F8;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_F9;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_G;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_GRAVE;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_H;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_I;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_J;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_K;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_L;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LALT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LCTRL;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LEFT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LEFTBRACKET;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LGUI;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_LSHIFT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_M;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_MINUS;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_N;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_O;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_P;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_PERIOD;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_Q;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_R;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RALT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RCTRL;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RETURN;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RGUI;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RIGHT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RIGHTBRACKET;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_RSHIFT;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_S;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_SEMICOLON;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_SLASH;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_SPACE;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_T;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_TAB;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_U;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_UP;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_V;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_W;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_X;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_Y;
import static org.lwjgl.sdl.SDLScancode.SDL_SCANCODE_Z;
import static org.lwjgl.sdl.SDLStdinc.SDL_free;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_GetCurrentContext;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplayName;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplays;

public class SdlPlatformManager extends PlatformManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SdlPlatformManager.class);

  private final Long2ObjectMap<SdlWindow> windows = new Long2ObjectOpenHashMap<>();
  private SdlWindow focus;
  private SDL_Event event;

  private final Map<InputAction, InputActionState> actionStates = new HashMap<>();

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
    if(!SDL_Init(SDL_INIT_VIDEO | SDL_INIT_GAMEPAD)) {
      throw new IllegalStateException("Unable to initialize SDL3");
    }

    this.event = SDL_Event.malloc();

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
    final IntBuffer ids = SDL_GetGamepads();

    if(ids == null) {
      this.logLastError();
      return List.of();
    }

    final List<SdlGamepadDevice> gamepads = new ArrayList<>();

    for(int i = 0; i < ids.limit(); i++) {
      try {
        final int id = ids.get(i);
        gamepads.add(new SdlGamepadDevice(id, SDL_OpenGamepad(id)));
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
  public void resetActionStates() {
    this.actionStates.clear();
  }

  @Override
  protected void tickInput() {
    while(SDL_PollEvent(this.event)) {
      switch(this.event.type()) {
        case SDL_EVENT_WINDOW_CLOSE_REQUESTED -> this.getWindow(this.event).close();
        case SDL_EVENT_WINDOW_FOCUS_GAINED -> {
          final SdlWindow window = this.getWindow(this.event);
          this.focus = window;
          window.hasFocus = true;
          window.events().onFocus(true);
        }

        case SDL_EVENT_WINDOW_FOCUS_LOST -> {
          final SdlWindow window = this.getWindow(this.event);
          this.focus = null;
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
          final SdlWindow window = this.getWindow(this.event);
          this.setWindowInputClass(window, InputClass.KEYBOARD);
          this.decodeMods(key, window.mods);

          if(key.down()) {
            window.events().onKeyPress(this.getInputFromKeycode(key.key()), this.getInputKeyFromScanCode(key.scancode()), window.mods, key.repeat());
          } else {
            window.events().onKeyRelease(this.getInputFromKeycode(key.key()), this.getInputKeyFromScanCode(key.scancode()), window.mods);
          }

          final List<InputBinding<KeyInputActivation>> keycodeBindings = InputBindings.getBindings(KeyInputActivation.class);

          for(int i = 0; i < keycodeBindings.size(); i++) {
            final InputBinding<KeyInputActivation> binding = keycodeBindings.get(i);

            if(this.getKeyCode(binding.activation.key) == key.key()) {
              if(key.down()) {
                if((binding.activation.mods.isEmpty() || window.mods.containsAll(binding.activation.mods)) && !key.repeat()) {
                  this.focus.events().onInputActionPressed(binding.action, false);
                  this.getInputActionState(binding.action).press();
                  EVENTS.postEvent(new InputPressedEvent(binding.action, false));
                }
              } else {
                this.focus.events().onInputActionReleased(binding.action);
                this.getInputActionState(binding.action).release();
                EVENTS.postEvent(new InputReleasedEvent(binding.action));
              }
            }
          }

          final List<InputBinding<ScancodeInputActivation>> scancodeBindings = InputBindings.getBindings(ScancodeInputActivation.class);

          for(int i = 0; i < scancodeBindings.size(); i++) {
            final InputBinding<ScancodeInputActivation> binding = scancodeBindings.get(i);

            if(this.getScanCode(binding.activation.key) == key.scancode()) {
              if(key.down()) {
                if((binding.activation.mods.isEmpty() || window.mods.containsAll(binding.activation.mods)) && !key.repeat()) {
                  this.focus.events().onInputActionPressed(binding.action, false);
                  this.getInputActionState(binding.action).press();
                  EVENTS.postEvent(new InputPressedEvent(binding.action, false));
                }
              } else {
                this.focus.events().onInputActionReleased(binding.action);
                this.getInputActionState(binding.action).release();
                EVENTS.postEvent(new InputReleasedEvent(binding.action));
              }
            }
          }
        }

        case SDL_EVENT_MOUSE_MOTION -> {
          final SDL_MouseMotionEvent mouse = this.event.motion();
          final SdlWindow window = this.getWindow(this.event);
          this.setWindowInputClass(window, InputClass.KEYBOARD);
          window.events().onMouseMove(mouse.x(), mouse.y());
        }

        case SDL_EVENT_MOUSE_BUTTON_DOWN, SDL_EVENT_MOUSE_BUTTON_UP -> {
          final SDL_MouseButtonEvent mouse = this.event.button();
          final SdlWindow window = this.getWindow(this.event);
          this.setWindowInputClass(window, InputClass.KEYBOARD);

          if(mouse.down()) {
            this.getWindow(this.event).events().onMousePress(mouse.button(), window.mods);
          } else {
            this.getWindow(this.event).events().onMouseRelease(mouse.button(), window.mods);
          }
        }

        case SDL_EVENT_MOUSE_WHEEL -> {
          final SDL_MouseWheelEvent wheel = this.event.wheel();
          final SdlWindow window = this.getWindow(this.event);
          this.setWindowInputClass(window, InputClass.KEYBOARD);
          window.events().onMouseScroll(wheel.x(), wheel.y());
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

          if(this.focus != null) {
            if(axis.value() > 0x2666) { // 30% inner deadzone
              this.setWindowInputClass(this.focus, InputClass.GAMEPAD);
              this.lastGamepad = this.gamepads.get(axis.which());
            }

            final List<InputBinding<AxisInputActivation>> axisBindings = InputBindings.getBindings(AxisInputActivation.class);

            for(int i = 0; i < axisBindings.size(); i++) {
              final InputBinding<AxisInputActivation> binding = axisBindings.get(i);

              if(this.getAxisCode(binding.activation.axis) == axis.axis()) {
                final InputAxisDirection direction = InputAxisDirection.getDirection(axis.value());

                if(binding.activation.direction == direction) {
                  final InputActionState state = this.getInputActionState(binding.action);
                  final float value = Math.min(1.0f, (Math.abs(axis.value()) - 0x7fff * binding.activation.threshold) / (0x7fff * Math.abs(binding.activation.threshold - binding.activation.deadzone)));

                  if(value >= 0.0f) {
                    if(!state.isHeld()) {
                      this.focus.events().onInputActionPressed(binding.action, false);
                      state.press();
                      EVENTS.postEvent(new InputPressedEvent(binding.action, false));
                    }

                    state.axis(value * Math.signum(axis.value()));
                  } else if(state.isHeld()) {
                    this.focus.events().onInputActionReleased(binding.action);
                    state.release();
                    EVENTS.postEvent(new InputReleasedEvent(binding.action));
                  }
                }
              }
            }
          }
        }

        case SDL_EVENT_GAMEPAD_BUTTON_DOWN -> {
          final SDL_GamepadButtonEvent button = this.event.gbutton();

          if(this.focus != null) {
            this.setWindowInputClass(this.focus, InputClass.GAMEPAD);
            this.lastGamepad = this.gamepads.get(button.which());
            this.focus.events().onButtonPress(this.getInputFromButton(button.button()), false);

            final List<InputBinding<ButtonInputActivation>> bindings = InputBindings.getBindings(ButtonInputActivation.class);

            for(int i = 0; i < bindings.size(); i++) {
              final InputBinding<ButtonInputActivation> binding = bindings.get(i);

              if(this.getButtonCode(binding.activation.button) == button.button()) {
                this.focus.events().onInputActionPressed(binding.action, false);
                this.getInputActionState(binding.action).press();
                EVENTS.postEvent(new InputPressedEvent(binding.action, false));
              }
            }
          }
        }

        case SDL_EVENT_GAMEPAD_BUTTON_UP -> {
          final SDL_GamepadButtonEvent button = this.event.gbutton();

          if(this.focus != null) {
            this.setWindowInputClass(this.focus, InputClass.GAMEPAD);
            this.lastGamepad = this.gamepads.get(button.which());
            this.focus.events().onButtonRelease(this.getInputFromButton(button.button()));

            final List<InputBinding<ButtonInputActivation>> bindings = InputBindings.getBindings(ButtonInputActivation.class);

            for(int i = 0; i < bindings.size(); i++) {
              final InputBinding<ButtonInputActivation> binding = bindings.get(i);

              if(this.getButtonCode(binding.activation.button) == button.button()) {
                this.focus.events().onInputActionReleased(binding.action);
                this.getInputActionState(binding.action).release();
                EVENTS.postEvent(new InputReleasedEvent(binding.action));
              }
            }
          }
        }
      }
    }

    if(this.focus != null) {
      for(final var entry : this.actionStates.entrySet()) {
        final InputAction action = entry.getKey();
        final InputActionState state = entry.getValue();

        if(state.repeat()) {
          this.focus.events().onInputActionPressed(action, true);
          EVENTS.postEvent(new InputPressedEvent(action, true));
        }
      }
    }

    if(this.rumbleLerpStart != 0) {
      final long time = System.nanoTime() - this.rumbleLerpStart;
      final float ratio = MathHelper.clamp(time / (float)this.rumbleLerpDuration, 0.0f, 1.0f);
      final float big = Math.lerp(this.rumbleBigStartingIntensity, this.rumbleBigEndingIntensity, ratio);
      final float small = Math.lerp(this.rumbleSmallStartingIntensity, this.rumbleSmallEndingIntensity, ratio);
      this.rumble(big, small, 0);

      if(time >= this.rumbleLerpDuration) {
        this.rumbleLerpStart = 0;
      }
    }

    super.tickInput();
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
    return this.getInputActionState(action).isPressed();
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

  private void decodeMods(final SDL_KeyboardEvent key, final Set<InputMod> mods) {
    mods.clear();

    if((key.mod() & SDL_KMOD_CTRL) != 0) {
      mods.add(InputMod.CTRL);
    }

    if((key.mod() & SDL_KMOD_ALT) != 0) {
      mods.add(InputMod.ALT);
    }

    if((key.mod() & SDL_KMOD_SHIFT) != 0) {
      mods.add(InputMod.SHIFT);
    }
  }

  private int getAxisCode(final InputAxis axis) {
    return switch(axis) {
      case LEFT_X -> SDL_GAMEPAD_AXIS_LEFTX;
      case LEFT_Y -> SDL_GAMEPAD_AXIS_LEFTY;
      case RIGHT_X -> SDL_GAMEPAD_AXIS_RIGHTX;
      case RIGHT_Y -> SDL_GAMEPAD_AXIS_RIGHTY;
      case LEFT_TRIGGER -> SDL_GAMEPAD_AXIS_LEFT_TRIGGER;
      case RIGHT_TRIGGER -> SDL_GAMEPAD_AXIS_RIGHT_TRIGGER;
    };
  }

  private int getButtonCode(final InputButton button) {
    return switch(button) {
      case DPAD_UP -> SDL_GAMEPAD_BUTTON_DPAD_UP;
      case DPAD_DOWN -> SDL_GAMEPAD_BUTTON_DPAD_DOWN;
      case DPAD_LEFT -> SDL_GAMEPAD_BUTTON_DPAD_LEFT;
      case DPAD_RIGHT -> SDL_GAMEPAD_BUTTON_DPAD_RIGHT;
      case A -> SDL_GAMEPAD_BUTTON_SOUTH;
      case B -> SDL_GAMEPAD_BUTTON_EAST;
      case X -> SDL_GAMEPAD_BUTTON_WEST;
      case Y -> SDL_GAMEPAD_BUTTON_NORTH;
      case START -> SDL_GAMEPAD_BUTTON_START;
      case SELECT -> SDL_GAMEPAD_BUTTON_BACK;
      case LEFT_BUMPER -> SDL_GAMEPAD_BUTTON_LEFT_SHOULDER;
      case RIGHT_BUMPER -> SDL_GAMEPAD_BUTTON_RIGHT_SHOULDER;
      case LEFT_STICK -> SDL_GAMEPAD_BUTTON_LEFT_STICK;
      case RIGHT_STICK -> SDL_GAMEPAD_BUTTON_RIGHT_STICK;
    };
  }

  private InputButton getInputFromButton(final int button) {
    return switch(button) {
      case SDL_GAMEPAD_BUTTON_DPAD_UP -> InputButton.DPAD_UP;
      case SDL_GAMEPAD_BUTTON_DPAD_DOWN -> InputButton.DPAD_DOWN;
      case SDL_GAMEPAD_BUTTON_DPAD_LEFT -> InputButton.DPAD_LEFT;
      case SDL_GAMEPAD_BUTTON_DPAD_RIGHT -> InputButton.DPAD_RIGHT;
      case SDL_GAMEPAD_BUTTON_SOUTH -> InputButton.A;
      case SDL_GAMEPAD_BUTTON_EAST -> InputButton.B;
      case SDL_GAMEPAD_BUTTON_WEST -> InputButton.X;
      case SDL_GAMEPAD_BUTTON_NORTH -> InputButton.Y;
      case SDL_GAMEPAD_BUTTON_START -> InputButton.START;
      case SDL_GAMEPAD_BUTTON_BACK -> InputButton.SELECT;
      case SDL_GAMEPAD_BUTTON_LEFT_SHOULDER -> InputButton.LEFT_BUMPER;
      case SDL_GAMEPAD_BUTTON_RIGHT_SHOULDER -> InputButton.RIGHT_BUMPER;
      case SDL_GAMEPAD_BUTTON_LEFT_STICK -> InputButton.LEFT_STICK;
      case SDL_GAMEPAD_BUTTON_RIGHT_STICK -> InputButton.RIGHT_STICK;
      default -> null;
    };
  }

  private int getKeyCode(final InputKey key) {
    return switch(key) {
      case RETURN -> SDLK_RETURN;
      case ESCAPE -> SDLK_ESCAPE;
      case BACKSPACE -> SDLK_BACKSPACE;
      case TAB -> SDLK_TAB;
      case SPACE -> SDLK_SPACE;
      case EXCLAMATION -> SDLK_EXCLAIM;
      case DOUBLE_QUOTE -> SDLK_DBLAPOSTROPHE;
      case HASH -> SDLK_HASH;
      case DOLLAR -> SDLK_DOLLAR;
      case PERCENT -> SDLK_PERCENT;
      case AMPERSAND -> SDLK_AMPERSAND;
      case APOSTROPHE -> SDLK_APOSTROPHE;
      case LEFT_PARENTHESIS -> SDLK_LEFTPAREN;
      case RIGHT_PARENTHESIS -> SDLK_RIGHTPAREN;
      case ASTERISK -> SDLK_ASTERISK;
      case PLUS -> SDLK_PLUS;
      case COMMA -> SDLK_COMMA;
      case MINUS -> SDLK_MINUS;
      case PERIOD -> SDLK_PERIOD;
      case SLASH -> SDLK_SLASH;
      case NUM_0 -> SDLK_0;
      case NUM_1 -> SDLK_1;
      case NUM_2 -> SDLK_2;
      case NUM_3 -> SDLK_3;
      case NUM_4 -> SDLK_4;
      case NUM_5 -> SDLK_5;
      case NUM_6 -> SDLK_6;
      case NUM_7 -> SDLK_7;
      case NUM_8 -> SDLK_8;
      case NUM_9 -> SDLK_9;
      case COLON -> SDLK_COLON;
      case SEMICOLON -> SDLK_SEMICOLON;
      case LESS -> SDLK_LESS;
      case EQUALS -> SDLK_EQUALS;
      case GREATER -> SDLK_GREATER;
      case QUESTION -> SDLK_QUESTION;
      case AT -> SDLK_AT;
      case LEFT_BRACKET -> SDLK_LEFTBRACKET;
      case BACKSLASH -> SDLK_BACKSLASH;
      case RIGHT_BRACKET -> SDLK_RIGHTBRACKET;
      case CARET -> SDLK_CARET;
      case UNDERSCORE -> SDLK_UNDERSCORE;
      case GRAVE -> SDLK_GRAVE;
      case A -> SDLK_A;
      case B -> SDLK_B;
      case C -> SDLK_C;
      case D -> SDLK_D;
      case E -> SDLK_E;
      case F -> SDLK_F;
      case G -> SDLK_G;
      case H -> SDLK_H;
      case I -> SDLK_I;
      case J -> SDLK_J;
      case K -> SDLK_K;
      case L -> SDLK_L;
      case M -> SDLK_M;
      case N -> SDLK_N;
      case O -> SDLK_O;
      case P -> SDLK_P;
      case Q -> SDLK_Q;
      case R -> SDLK_R;
      case S -> SDLK_S;
      case T -> SDLK_T;
      case U -> SDLK_U;
      case V -> SDLK_V;
      case W -> SDLK_W;
      case X -> SDLK_X;
      case Y -> SDLK_Y;
      case Z -> SDLK_Z;
      case LEFT_BRACE -> SDLK_LEFTBRACE;
      case PIPE -> SDLK_PIPE;
      case RIGHT_BRACE -> SDLK_RIGHTBRACE;
      case TILDE -> SDLK_TILDE;
      case DELETE -> SDLK_DELETE;
      case PLUS_MINUS -> SDLK_PLUSMINUS;

      case F1 -> SDLK_F1;
      case F2 -> SDLK_F2;
      case F3 -> SDLK_F3;
      case F4 -> SDLK_F4;
      case F5 -> SDLK_F5;
      case F6 -> SDLK_F6;
      case F7 -> SDLK_F7;
      case F8 -> SDLK_F8;
      case F9 -> SDLK_F9;
      case F10 -> SDLK_F10;
      case F11 -> SDLK_F11;
      case F12 -> SDLK_F12;
      case PRINT_SCREEN -> SDLK_PRINTSCREEN;
      case SCROLL_LOCK -> SDLK_SCROLLLOCK;
      case PAUSE -> SDLK_PAUSE;
      case INSERT -> SDLK_INSERT;
      case HOME -> SDLK_HOME;
      case PAGE_UP -> SDLK_PAGEUP;
      case END -> SDLK_END;
      case PAGE_DOWN -> SDLK_PAGEDOWN;
      case RIGHT -> SDLK_RIGHT;
      case LEFT -> SDLK_LEFT;
      case DOWN -> SDLK_DOWN;
      case UP -> SDLK_UP;

      case KP_DIVIDE -> SDLK_KP_DIVIDE;
      case KP_MULTIPLY -> SDLK_KP_MULTIPLY;
      case KP_MINUS -> SDLK_KP_MINUS;
      case KP_PLUS -> SDLK_KP_PLUS;
      case KP_ENTER -> SDLK_KP_ENTER;
      case KP_1 -> SDLK_KP_1;
      case KP_2 -> SDLK_KP_2;
      case KP_3 -> SDLK_KP_3;
      case KP_4 -> SDLK_KP_4;
      case KP_5 -> SDLK_KP_5;
      case KP_6 -> SDLK_KP_6;
      case KP_7 -> SDLK_KP_7;
      case KP_8 -> SDLK_KP_8;
      case KP_9 -> SDLK_KP_9;
      case KP_0 -> SDLK_KP_0;
      case KP_PERIOD -> SDLK_KP_PERIOD;
      case KP_EQUALS -> SDLK_KP_EQUALS;
      case F13 -> SDLK_F13;
      case F14 -> SDLK_F14;
      case F15 -> SDLK_F15;
      case F16 -> SDLK_F16;
      case F17 -> SDLK_F17;
      case F18 -> SDLK_F18;
      case F19 -> SDLK_F19;
      case F20 -> SDLK_F20;
      case F21 -> SDLK_F21;
      case F22 -> SDLK_F22;
      case F23 -> SDLK_F23;
      case F24 -> SDLK_F24;

      case LEFT_CTRL -> SDLK_LCTRL;
      case LEFT_SHIFT -> SDLK_LSHIFT;
      case LEFT_ALT -> SDLK_LALT;
      case LEFT_GUI -> SDLK_LGUI;
      case RIGHT_CTRL -> SDLK_RCTRL;
      case RIGHT_SHIFT -> SDLK_RSHIFT;
      case RIGHT_ALT -> SDLK_RALT;
      case RIGHT_GUI -> SDLK_RGUI;
    };
  }

  private InputKey getInputFromKeycode(final int key) {
    return switch(key) {
      case SDLK_RETURN -> InputKey.RETURN;
      case SDLK_ESCAPE -> InputKey.ESCAPE;
      case SDLK_BACKSPACE -> InputKey.BACKSPACE;
      case SDLK_TAB -> InputKey.TAB;
      case SDLK_SPACE -> InputKey.SPACE;
      case SDLK_EXCLAIM -> InputKey.EXCLAMATION;
      case SDLK_DBLAPOSTROPHE -> InputKey.DOUBLE_QUOTE;
      case SDLK_HASH -> InputKey.HASH;
      case SDLK_DOLLAR -> InputKey.DOLLAR;
      case SDLK_PERCENT -> InputKey.PERCENT;
      case SDLK_AMPERSAND -> InputKey.AMPERSAND;
      case SDLK_APOSTROPHE -> InputKey.APOSTROPHE;
      case SDLK_LEFTPAREN -> InputKey.LEFT_PARENTHESIS;
      case SDLK_RIGHTPAREN -> InputKey.RIGHT_PARENTHESIS;
      case SDLK_ASTERISK -> InputKey.ASTERISK;
      case SDLK_PLUS -> InputKey.PLUS;
      case SDLK_COMMA -> InputKey.COMMA;
      case SDLK_MINUS -> InputKey.MINUS;
      case SDLK_PERIOD -> InputKey.PERIOD;
      case SDLK_SLASH -> InputKey.SLASH;
      case SDLK_0 -> InputKey.NUM_0;
      case SDLK_1 -> InputKey.NUM_1;
      case SDLK_2 -> InputKey.NUM_2;
      case SDLK_3 -> InputKey.NUM_3;
      case SDLK_4 -> InputKey.NUM_4;
      case SDLK_5 -> InputKey.NUM_5;
      case SDLK_6 -> InputKey.NUM_6;
      case SDLK_7 -> InputKey.NUM_7;
      case SDLK_8 -> InputKey.NUM_8;
      case SDLK_9 -> InputKey.NUM_9;
      case SDLK_COLON -> InputKey.COLON;
      case SDLK_SEMICOLON -> InputKey.SEMICOLON;
      case SDLK_LESS -> InputKey.LESS;
      case SDLK_EQUALS -> InputKey.EQUALS;
      case SDLK_GREATER -> InputKey.GREATER;
      case SDLK_QUESTION -> InputKey.QUESTION;
      case SDLK_AT -> InputKey.AT;
      case SDLK_LEFTBRACKET -> InputKey.LEFT_BRACKET;
      case SDLK_BACKSLASH -> InputKey.BACKSLASH;
      case SDLK_RIGHTBRACKET -> InputKey.RIGHT_BRACKET;
      case SDLK_CARET -> InputKey.CARET;
      case SDLK_UNDERSCORE -> InputKey.UNDERSCORE;
      case SDLK_GRAVE -> InputKey.GRAVE;
      case SDLK_A -> InputKey.A;
      case SDLK_B -> InputKey.B;
      case SDLK_C -> InputKey.C;
      case SDLK_D -> InputKey.D;
      case SDLK_E -> InputKey.E;
      case SDLK_F -> InputKey.F;
      case SDLK_G -> InputKey.G;
      case SDLK_H -> InputKey.H;
      case SDLK_I -> InputKey.I;
      case SDLK_J -> InputKey.J;
      case SDLK_K -> InputKey.K;
      case SDLK_L -> InputKey.L;
      case SDLK_M -> InputKey.M;
      case SDLK_N -> InputKey.N;
      case SDLK_O -> InputKey.O;
      case SDLK_P -> InputKey.P;
      case SDLK_Q -> InputKey.Q;
      case SDLK_R -> InputKey.R;
      case SDLK_S -> InputKey.S;
      case SDLK_T -> InputKey.T;
      case SDLK_U -> InputKey.U;
      case SDLK_V -> InputKey.V;
      case SDLK_W -> InputKey.W;
      case SDLK_X -> InputKey.X;
      case SDLK_Y -> InputKey.Y;
      case SDLK_Z -> InputKey.Z;
      case SDLK_LEFTBRACE -> InputKey.LEFT_BRACE;
      case SDLK_PIPE -> InputKey.PIPE;
      case SDLK_RIGHTBRACE -> InputKey.RIGHT_BRACE;
      case SDLK_TILDE -> InputKey.TILDE;
      case SDLK_DELETE -> InputKey.DELETE;
      case SDLK_PLUSMINUS -> InputKey.PLUS_MINUS;
      case SDLK_F1 -> InputKey.F1;
      case SDLK_F2 -> InputKey.F2;
      case SDLK_F3 -> InputKey.F3;
      case SDLK_F4 -> InputKey.F4;
      case SDLK_F5 -> InputKey.F5;
      case SDLK_F6 -> InputKey.F6;
      case SDLK_F7 -> InputKey.F7;
      case SDLK_F8 -> InputKey.F8;
      case SDLK_F9 -> InputKey.F9;
      case SDLK_F10 -> InputKey.F10;
      case SDLK_F11 -> InputKey.F11;
      case SDLK_F12 -> InputKey.F12;
      case SDLK_PRINTSCREEN -> InputKey.PRINT_SCREEN;
      case SDLK_SCROLLLOCK -> InputKey.SCROLL_LOCK;
      case SDLK_PAUSE -> InputKey.PAUSE;
      case SDLK_INSERT -> InputKey.INSERT;
      case SDLK_HOME -> InputKey.HOME;
      case SDLK_PAGEUP -> InputKey.PAGE_UP;
      case SDLK_END -> InputKey.END;
      case SDLK_PAGEDOWN -> InputKey.PAGE_DOWN;
      case SDLK_RIGHT -> InputKey.RIGHT;
      case SDLK_LEFT -> InputKey.LEFT;
      case SDLK_DOWN -> InputKey.DOWN;
      case SDLK_UP -> InputKey.UP;
      case SDLK_KP_DIVIDE -> InputKey.KP_DIVIDE;
      case SDLK_KP_MULTIPLY -> InputKey.KP_MULTIPLY;
      case SDLK_KP_MINUS -> InputKey.KP_MINUS;
      case SDLK_KP_PLUS -> InputKey.KP_PLUS;
      case SDLK_KP_ENTER -> InputKey.KP_ENTER;
      case SDLK_KP_1 -> InputKey.KP_1;
      case SDLK_KP_2 -> InputKey.KP_2;
      case SDLK_KP_3 -> InputKey.KP_3;
      case SDLK_KP_4 -> InputKey.KP_4;
      case SDLK_KP_5 -> InputKey.KP_5;
      case SDLK_KP_6 -> InputKey.KP_6;
      case SDLK_KP_7 -> InputKey.KP_7;
      case SDLK_KP_8 -> InputKey.KP_8;
      case SDLK_KP_9 -> InputKey.KP_9;
      case SDLK_KP_0 -> InputKey.KP_0;
      case SDLK_KP_PERIOD -> InputKey.KP_PERIOD;
      case SDLK_KP_EQUALS -> InputKey.KP_EQUALS;
      case SDLK_F13 -> InputKey.F13;
      case SDLK_F14 -> InputKey.F14;
      case SDLK_F15 -> InputKey.F15;
      case SDLK_F16 -> InputKey.F16;
      case SDLK_F17 -> InputKey.F17;
      case SDLK_F18 -> InputKey.F18;
      case SDLK_F19 -> InputKey.F19;
      case SDLK_F20 -> InputKey.F20;
      case SDLK_F21 -> InputKey.F21;
      case SDLK_F22 -> InputKey.F22;
      case SDLK_F23 -> InputKey.F23;
      case SDLK_F24 -> InputKey.F24;

      case SDLK_LCTRL -> InputKey.LEFT_CTRL;
      case SDLK_LSHIFT -> InputKey.LEFT_SHIFT;
      case SDLK_LALT -> InputKey.LEFT_ALT;
      case SDLK_LGUI -> InputKey.LEFT_GUI;
      case SDLK_RCTRL -> InputKey.RIGHT_CTRL;
      case SDLK_RSHIFT -> InputKey.RIGHT_SHIFT;
      case SDLK_RALT -> InputKey.RIGHT_ALT;
      case SDLK_RGUI -> InputKey.RIGHT_GUI;

      default -> null;
    };
  }

  private int getScanCode(final InputKey key) {
    return switch(key) {
      case A -> SDL_SCANCODE_A;
      case B -> SDL_SCANCODE_B;
      case C -> SDL_SCANCODE_C;
      case D -> SDL_SCANCODE_D;
      case E -> SDL_SCANCODE_E;
      case F -> SDL_SCANCODE_F;
      case G -> SDL_SCANCODE_G;
      case H -> SDL_SCANCODE_H;
      case I -> SDL_SCANCODE_I;
      case J -> SDL_SCANCODE_J;
      case K -> SDL_SCANCODE_K;
      case L -> SDL_SCANCODE_L;
      case M -> SDL_SCANCODE_M;
      case N -> SDL_SCANCODE_N;
      case O -> SDL_SCANCODE_O;
      case P -> SDL_SCANCODE_P;
      case Q -> SDL_SCANCODE_Q;
      case R -> SDL_SCANCODE_R;
      case S -> SDL_SCANCODE_S;
      case T -> SDL_SCANCODE_T;
      case U -> SDL_SCANCODE_U;
      case V -> SDL_SCANCODE_V;
      case W -> SDL_SCANCODE_W;
      case X -> SDL_SCANCODE_X;
      case Y -> SDL_SCANCODE_Y;
      case Z -> SDL_SCANCODE_Z;
      case NUM_1 -> SDL_SCANCODE_1;
      case NUM_2 -> SDL_SCANCODE_2;
      case NUM_3 -> SDL_SCANCODE_3;
      case NUM_4 -> SDL_SCANCODE_4;
      case NUM_5 -> SDL_SCANCODE_5;
      case NUM_6 -> SDL_SCANCODE_6;
      case NUM_7 -> SDL_SCANCODE_7;
      case NUM_8 -> SDL_SCANCODE_8;
      case NUM_9 -> SDL_SCANCODE_9;
      case NUM_0 -> SDL_SCANCODE_0;

      case RETURN -> SDL_SCANCODE_RETURN;
      case ESCAPE -> SDL_SCANCODE_ESCAPE;
      case BACKSPACE -> SDL_SCANCODE_BACKSPACE;
      case TAB -> SDL_SCANCODE_TAB;
      case SPACE -> SDL_SCANCODE_SPACE;
      case MINUS -> SDL_SCANCODE_MINUS;
      case EQUALS -> SDL_SCANCODE_EQUALS;
      case LEFT_BRACKET -> SDL_SCANCODE_LEFTBRACKET;
      case RIGHT_BRACKET -> SDL_SCANCODE_RIGHTBRACKET;
      case BACKSLASH -> SDL_SCANCODE_BACKSLASH;
      case SEMICOLON -> SDL_SCANCODE_SEMICOLON;
      case APOSTROPHE -> SDL_SCANCODE_APOSTROPHE;
      case GRAVE -> SDL_SCANCODE_GRAVE;
      case COMMA -> SDL_SCANCODE_COMMA;
      case PERIOD -> SDL_SCANCODE_PERIOD;
      case SLASH -> SDL_SCANCODE_SLASH;
      case F1 -> SDL_SCANCODE_F1;
      case F2 -> SDL_SCANCODE_F2;
      case F3 -> SDL_SCANCODE_F3;
      case F4 -> SDL_SCANCODE_F4;
      case F5 -> SDL_SCANCODE_F5;
      case F6 -> SDL_SCANCODE_F6;
      case F7 -> SDL_SCANCODE_F7;
      case F8 -> SDL_SCANCODE_F8;
      case F9 -> SDL_SCANCODE_F9;
      case F10 -> SDL_SCANCODE_F10;
      case F11 -> SDL_SCANCODE_F11;
      case F12 -> SDL_SCANCODE_F12;

      case RIGHT -> SDL_SCANCODE_RIGHT;
      case LEFT -> SDL_SCANCODE_LEFT;
      case DOWN -> SDL_SCANCODE_DOWN;
      case UP -> SDL_SCANCODE_UP;

      case LEFT_CTRL -> SDL_SCANCODE_LCTRL;
      case LEFT_SHIFT -> SDL_SCANCODE_LSHIFT;
      case LEFT_ALT -> SDL_SCANCODE_LALT;
      case LEFT_GUI -> SDL_SCANCODE_LGUI;
      case RIGHT_CTRL -> SDL_SCANCODE_RCTRL;
      case RIGHT_SHIFT -> SDL_SCANCODE_RSHIFT;
      case RIGHT_ALT -> SDL_SCANCODE_RALT;
      case RIGHT_GUI -> SDL_SCANCODE_RGUI;

      default -> throw new IllegalStateException("Not yet implemented: " + key); //TODO
    };
  }

  private InputKey getInputKeyFromScanCode(final int key) {
    return switch(key) {
      case SDL_SCANCODE_A -> InputKey.A;
      case SDL_SCANCODE_B -> InputKey.B;
      case SDL_SCANCODE_C -> InputKey.C;
      case SDL_SCANCODE_D -> InputKey.D;
      case SDL_SCANCODE_E -> InputKey.E;
      case SDL_SCANCODE_F -> InputKey.F;
      case SDL_SCANCODE_G -> InputKey.G;
      case SDL_SCANCODE_H -> InputKey.H;
      case SDL_SCANCODE_I -> InputKey.I;
      case SDL_SCANCODE_J -> InputKey.J;
      case SDL_SCANCODE_K -> InputKey.K;
      case SDL_SCANCODE_L -> InputKey.L;
      case SDL_SCANCODE_M -> InputKey.M;
      case SDL_SCANCODE_N -> InputKey.N;
      case SDL_SCANCODE_O -> InputKey.O;
      case SDL_SCANCODE_P -> InputKey.P;
      case SDL_SCANCODE_Q -> InputKey.Q;
      case SDL_SCANCODE_R -> InputKey.R;
      case SDL_SCANCODE_S -> InputKey.S;
      case SDL_SCANCODE_T -> InputKey.T;
      case SDL_SCANCODE_U -> InputKey.U;
      case SDL_SCANCODE_V -> InputKey.V;
      case SDL_SCANCODE_W -> InputKey.W;
      case SDL_SCANCODE_X -> InputKey.X;
      case SDL_SCANCODE_Y -> InputKey.Y;
      case SDL_SCANCODE_Z -> InputKey.Z;
      case SDL_SCANCODE_1 -> InputKey.NUM_1;
      case SDL_SCANCODE_2 -> InputKey.NUM_2;
      case SDL_SCANCODE_3 -> InputKey.NUM_3;
      case SDL_SCANCODE_4 -> InputKey.NUM_4;
      case SDL_SCANCODE_5 -> InputKey.NUM_5;
      case SDL_SCANCODE_6 -> InputKey.NUM_6;
      case SDL_SCANCODE_7 -> InputKey.NUM_7;
      case SDL_SCANCODE_8 -> InputKey.NUM_8;
      case SDL_SCANCODE_9 -> InputKey.NUM_9;
      case SDL_SCANCODE_0 -> InputKey.NUM_0;

      case SDL_SCANCODE_RETURN -> InputKey.RETURN;
      case SDL_SCANCODE_ESCAPE -> InputKey.ESCAPE;
      case SDL_SCANCODE_BACKSPACE -> InputKey.BACKSPACE;
      case SDL_SCANCODE_TAB -> InputKey.TAB;
      case SDL_SCANCODE_SPACE -> InputKey.SPACE;
      case SDL_SCANCODE_MINUS -> InputKey.MINUS;
      case SDL_SCANCODE_EQUALS -> InputKey.EQUALS;
      case SDL_SCANCODE_LEFTBRACKET -> InputKey.LEFT_BRACKET;
      case SDL_SCANCODE_RIGHTBRACKET -> InputKey.RIGHT_BRACKET;
      case SDL_SCANCODE_BACKSLASH -> InputKey.BACKSLASH;
      case SDL_SCANCODE_SEMICOLON -> InputKey.SEMICOLON;
      case SDL_SCANCODE_APOSTROPHE -> InputKey.APOSTROPHE;
      case SDL_SCANCODE_GRAVE -> InputKey.GRAVE;
      case SDL_SCANCODE_COMMA -> InputKey.COMMA;
      case SDL_SCANCODE_PERIOD -> InputKey.PERIOD;
      case SDL_SCANCODE_SLASH -> InputKey.SLASH;
      case SDL_SCANCODE_F1 -> InputKey.F1;
      case SDL_SCANCODE_F2 -> InputKey.F2;
      case SDL_SCANCODE_F3 -> InputKey.F3;
      case SDL_SCANCODE_F4 -> InputKey.F4;
      case SDL_SCANCODE_F5 -> InputKey.F5;
      case SDL_SCANCODE_F6 -> InputKey.F6;
      case SDL_SCANCODE_F7 -> InputKey.F7;
      case SDL_SCANCODE_F8 -> InputKey.F8;
      case SDL_SCANCODE_F9 -> InputKey.F9;
      case SDL_SCANCODE_F10 -> InputKey.F10;
      case SDL_SCANCODE_F11 -> InputKey.F11;
      case SDL_SCANCODE_F12 -> InputKey.F12;

      case SDL_SCANCODE_RIGHT -> InputKey.RIGHT;
      case SDL_SCANCODE_LEFT -> InputKey.LEFT;
      case SDL_SCANCODE_DOWN -> InputKey.DOWN;
      case SDL_SCANCODE_UP -> InputKey.UP;

      case SDL_SCANCODE_LCTRL -> InputKey.LEFT_CTRL;
      case SDL_SCANCODE_LSHIFT -> InputKey.LEFT_SHIFT;
      case SDL_SCANCODE_LALT -> InputKey.LEFT_ALT;
      case SDL_SCANCODE_LGUI -> InputKey.LEFT_GUI;
      case SDL_SCANCODE_RCTRL -> InputKey.RIGHT_CTRL;
      case SDL_SCANCODE_RSHIFT -> InputKey.RIGHT_SHIFT;
      case SDL_SCANCODE_RALT -> InputKey.RIGHT_ALT;
      case SDL_SCANCODE_RGUI -> InputKey.RIGHT_GUI;

      default -> null;
    };
  }

  private void logLastError() {
    LOGGER.error(SDL_GetError());
  }
}
