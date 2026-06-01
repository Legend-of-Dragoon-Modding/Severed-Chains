package legend.game;

import legend.core.platform.SdlInput;
import legend.core.platform.SdlWindow;
import legend.core.platform.input.InputKey;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.sdl.SDL_Event;
import org.lwjgl.sdl.SDL_KeyboardEvent;

import static legend.core.GameEngine.PLATFORM;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_KEY_DOWN;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_KEY_UP;
import static org.lwjgl.sdl.SDLEvents.SDL_PushEvent;
import static org.lwjgl.sdl.SDLTimer.SDL_GetTicksNS;

public final class Input {
  private Input() { }

  public static void sendKeyDown(final InputKey key) {
    final SDL_Event e = SDL_Event.create();

    e.type(SDL_EVENT_KEY_DOWN);

    final SDL_KeyboardEvent k = e.key();
    k.timestamp(SDL_GetTicksNS());
    k.windowID(SDLVideo.SDL_GetWindowID(((SdlWindow)PLATFORM.getLastWindow()).getWindowPtr()));
    k.repeat(false);
    k.key(SdlInput.getKeyCode(key));
    k.scancode(SdlInput.getScanCode(key));
    k.down(true);
    k.mod((short)0);

    SDL_PushEvent(e);
  }

  public static void sendKeyUp(final InputKey key) {
    final SDL_Event e = SDL_Event.create();

    e.type(SDL_EVENT_KEY_UP);

    final SDL_KeyboardEvent k = e.key();
    k.timestamp(SDL_GetTicksNS());
    k.windowID(SDLVideo.SDL_GetWindowID(((SdlWindow)PLATFORM.getLastWindow()).getWindowPtr()));
    k.repeat(false);
    k.key(SdlInput.getKeyCode(key));
    k.scancode(SdlInput.getScanCode(key));
    k.down(false);
    k.mod((short)0);

    SDL_PushEvent(e);
  }

  public static void sendKeyPress(final InputKey key) {
    sendKeyDown(key);
    sendKeyUp(key);
  }
}
