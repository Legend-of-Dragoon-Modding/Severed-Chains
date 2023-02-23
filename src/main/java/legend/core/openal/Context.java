package legend.core.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.util.HashMap;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

public final class Context {

  private static XaFile xaAsset = null;
  static long audioContext;
  static long audioDevice;

  private Context() {
  }

  public static XaFile getXA(final int archive, final int file) {
    if (xaAsset != null) {
      xaAsset.delete();
    }

    xaAsset = new XaFile(System.getProperty("user.dir") + "\\files\\XA\\LODXA0" + archive + '\\' + file + ".ogg", false);

    return xaAsset;
  }

  public static void createContext() {
    final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
    audioDevice = alcOpenDevice(defaultDeviceName);

    final int[] attributes = {0};
    audioContext = alcCreateContext(audioDevice, attributes);
    alcMakeContextCurrent(audioContext);

    final ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
    final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

    if (!alCapabilities.OpenAL10) {
      assert false: "Audio library not supported";
    }
  }

  public static void destroyContext() {
    alcDestroyContext(audioContext);
    alcCloseDevice(audioDevice);
  }
}
