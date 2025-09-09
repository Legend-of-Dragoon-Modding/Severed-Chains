package legend.core.platform.input;

import java.util.Objects;

import static legend.core.GameEngine.CONFIG;
import static legend.game.modding.coremod.CoreMod.RUMBLE_INTENSITY_CONFIG;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_NINTENDO_SWITCH_PRO;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_PS3;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_PS4;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_PS5;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_XBOX360;
import static org.lwjgl.sdl.SDLGamepad.SDL_GAMEPAD_TYPE_XBOXONE;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadName;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadProperties;
import static org.lwjgl.sdl.SDLGamepad.SDL_GetGamepadType;
import static org.lwjgl.sdl.SDLGamepad.SDL_PROP_GAMEPAD_CAP_RUMBLE_BOOLEAN;
import static org.lwjgl.sdl.SDLGamepad.SDL_RumbleGamepad;
import static org.lwjgl.sdl.SDLProperties.SDL_GetBooleanProperty;

public class SdlGamepadDevice extends InputDevice {
  public final int id;
  public final String name;
  public final boolean rumble;
  public final InputGamepadType type;

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

    this.type = switch(SDL_GetGamepadType(ptr)) {
      case SDL_GAMEPAD_TYPE_XBOXONE -> InputGamepadType.XBOX_ONE;
      case SDL_GAMEPAD_TYPE_XBOX360 -> InputGamepadType.XBOX_360;
      case SDL_GAMEPAD_TYPE_PS3, SDL_GAMEPAD_TYPE_PS4, SDL_GAMEPAD_TYPE_PS5 -> InputGamepadType.PLAYSTATION;
      case SDL_GAMEPAD_TYPE_NINTENDO_SWITCH_PRO -> InputGamepadType.SWITCH;
      default -> InputGamepadType.STANDARD;
    };
  }

  public void rumble(float bigIntensity, float smallIntensity, final int ms) {
    final float scale = CONFIG.getConfig(RUMBLE_INTENSITY_CONFIG.get());
    bigIntensity *= scale;
    smallIntensity *= scale;

    SDL_RumbleGamepad(this.ptr, (short)(Math.min(1.0f, bigIntensity) * 0xffff), (short)(Math.min(1.0f, smallIntensity) * 0xffff), ms);
  }

  public void stopRumble() {
    this.rumble(0.0f, 0.0f, 0);
  }
}
