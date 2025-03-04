package legend.core.platform.input;

import java.util.Objects;

import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadName;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadProperties;
import static org.lwjgl.sdl.SDLGamepad.SDL_PROP_GAMEPAD_CAP_RUMBLE_BOOLEAN;
import static org.lwjgl.sdl.SDLGamepad.SDL_RumbleGamepad;
import static org.lwjgl.sdl.SDLProperties.SDL_GetBooleanProperty;

public class SdlGamepadDevice extends InputDevice {
  public final int id;
  public final String name;
  public final boolean rumble;

  private final long ptr;

  public SdlGamepadDevice(final int id, final long ptr) throws FailedToLoadDeviceException {
    super(InputClass.GAMEPAD);
    this.id = id;
    this.ptr = ptr;

    if(ptr == 0) {
      throw new FailedToLoadDeviceException("Failed to open controller");
    }

    this.name = Objects.requireNonNullElse(SDL_GetGamepadName(ptr), "Unknown");

    final int props = SDL_GetGamepadProperties(ptr);

    if(props == 0) {
      throw new FailedToLoadDeviceException("Failed to open controller properties");
    }

    this.rumble = SDL_GetBooleanProperty(props, SDL_PROP_GAMEPAD_CAP_RUMBLE_BOOLEAN, false);
  }

  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) {
    SDL_RumbleGamepad(this.ptr, (short)(bigIntensity * 0xffff), (short)(smallIntensity * 0xffff), ms);
  }

  public void stopRumble() {
    this.rumble(0.0f, 0.0f, 0);
  }
}
