package legend.game;

import legend.core.memory.Method;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.lodmod.LodEngineStateTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Graphics.vsyncMode_8007a3b8;

public final class Rumble {
  private Rumble() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Rumble.class);

  public static float rumbleDampener_800bee80;

  @ScriptDescription("Start rumbling")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "joypadIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode (0: stop, 1: weak, 2: medium, 3: strong")
  @Method(0x80017584L)
  public static FlowControl scriptStartRumbleMode(final RunningScript<?> script) {
    startRumbleMode(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Start rumbling")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "joypadIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "intensity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames")
  @Method(0x80017688L)
  public static FlowControl scriptStartRumble(final RunningScript<?> script) {
    adjustRumbleOverTime(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the rumble intensity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "intensity")
  @Method(0x800176c0L)
  public static FlowControl scriptSetRumbleDampener(final RunningScript<?> script) {
    setRumbleDampener(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the rumble intensity param back to 0")
  @Method(0x800176ecL)
  public static FlowControl scriptResetRumbleDampener(final RunningScript<?> script) {
    resetRumbleDampener();
    return FlowControl.CONTINUE;
  }

  @Method(0x8002bb38L)
  public static void startRumbleMode(final int pad, final int mode) {
    LOGGER.debug("startRumbleMode %x %x", pad, mode);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    switch(mode) {
      case 0 -> stopRumble(pad);
      case 1 -> PLATFORM.rumble(0.25f, 0);
      case 2 -> {
        if(currentEngineState_8004dd04.is(LodEngineStateTypes.SUBMAP.get())) {
          PLATFORM.rumble(0.3f, 0);
        } else if(currentEngineState_8004dd04.is(LodEngineStateTypes.BATTLE.get())) {
          PLATFORM.rumble(0.75f - rumbleDampener_800bee80, 0);
        } else {
          PLATFORM.rumble(0.75f, 0);
        }
      }
      case 3 -> {
        if(currentEngineState_8004dd04.is(LodEngineStateTypes.SUBMAP.get())) {
          PLATFORM.rumble(0.4f, 0);
        } else if(currentEngineState_8004dd04.is(LodEngineStateTypes.BATTLE.get())) {
          PLATFORM.rumble(1.0f - rumbleDampener_800bee80, 0);
        } else {
          PLATFORM.rumble(1.0f, 0);
        }
      }
    }
  }

  @Method(0x8002bcc8L)
  public static void startRumbleIntensity(final int pad, int intensityIn) {
    LOGGER.debug("startRumbleIntensity %x %x", pad, intensityIn);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    if(intensityIn > 0x1ff) {
      intensityIn = 0x1ff;
    }

    float intensity = intensityIn / (float)0x1ff;

    if(intensity > 0.25f) {
      intensity -= rumbleDampener_800bee80;
    }

    PLATFORM.rumble(intensity, 0);
  }

  @Method(0x8002bda4L)
  public static void adjustRumbleOverTime(final int pad, final int intensity, final int frames) {
    final int divisor = vsyncMode_8007a3b8 * currentEngineState_8004dd04.tickMultiplier();
    adjustRumbleOverTime(pad, intensity, frames, divisor);
  }

  @Method(0x8002bda4L)
  public static void adjustRumbleOverTime(final int pad, int intensity, final int frames, final int framesDivisor) {
    LOGGER.debug("adjustRumbleOverTime %x %x %x", pad, intensity, frames);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    intensity = Math.clamp(intensity, 0, 0x1ff);

    if(frames == 0) {
      startRumbleIntensity(pad, intensity);
      return;
    }

    PLATFORM.adjustRumble(intensity / (float)0x1ff, frames / framesDivisor * 50);
  }

  @Method(0x8002c150L)
  public static void stopRumble(final int pad) {
    LOGGER.debug("stopRumble");
    PLATFORM.stopRumble();
  }

  @Method(0x8002c178L)
  public static void setRumbleDampener(final int intensity) {
    LOGGER.debug("setRumbleDampener %x", intensity);
    rumbleDampener_800bee80 = intensity / (float)0x1ff;
  }

  @Method(0x8002c184L)
  public static void resetRumbleDampener() {
    LOGGER.debug("resetRumbleDampener");
    rumbleDampener_800bee80 = 0.0f;
  }
}
