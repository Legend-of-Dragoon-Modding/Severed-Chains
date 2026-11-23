package legend.game;

import legend.core.memory.Method;
import legend.core.renderer.BufferUsage;
import legend.core.renderer.Obj;
import legend.core.renderer.PolyBuilder;
import legend.core.renderer.QueuedModelStandard;
import legend.core.renderer.Translucency;
import legend.core.renderer.VertexOrder;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.FullScreenEffect;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;

public final class FullScreenEffects {
  private FullScreenEffects() { }

  private static final int[] _8004dd48 = {0, 1, 2, 1, 2, 1, 2};

  public static final FullScreenEffect fullScreenEffect_800bb140 = new FullScreenEffect();

  /// @param effectType - 1: fade out (black)
  ///                   - 2: fade in (black)
  ///                   - 3: fade out (white)
  ///                   - 4: fade int (white)
  ///                   - 5: boxagon out
  ///                   - 6: boxagon in
  @Method(0x800136dcL)
  public static void startFadeEffect(final int effectType, final int frames) {
    //LAB_800136f4
    fullScreenEffect_800bb140.type_00 = effectType;
    fullScreenEffect_800bb140.totalFrames_08 = frames > 0 ? frames : 15;
    fullScreenEffect_800bb140.startTime_04 = RENDERER.getVsyncCount();

    if(_8004dd48[effectType] == 2) {
      fullScreenEffect_800bb140.blue1_0c = 0;
      fullScreenEffect_800bb140.green1_10 = 0;
      fullScreenEffect_800bb140.blue0_14 = 0;
      fullScreenEffect_800bb140.red1_18 = 0;
      fullScreenEffect_800bb140.green0_1c = 0;
      fullScreenEffect_800bb140.red0_20 = 0;
    }

    fullScreenEffect_800bb140._24 = _8004dd48[effectType];
    fullScreenEffect_800bb140.currentColour_28 = 1;

    //LAB_80013768
  }

  /**
   * This handles the lightning flashes/darkening, the scene fade-in, etc.
   */
  @Method(0x80013778L)
  public static void handleFullScreenEffects() {
    //LAB_80013994
    // This causes the bright flash of light from the lightning, etc.
    if(fullScreenEffect_800bb140.red0_20 != 0 || fullScreenEffect_800bb140.green0_1c != 0 || fullScreenEffect_800bb140.blue0_14 != 0) {
      // Make sure effect fills the whole screen
      final float fullWidth = Math.max(RENDERER.getNativeWidth(), (float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight() * displayHeight_1f8003e4 * 1.1f);
      fullScreenEffect_800bb140.transforms
        .scaling(fullWidth, displayHeight_1f8003e4, 1.0f)
        .translate(0.0f, 0.0f, 156.0f)
      ;

      //LAB_800139c4
      RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.B_PLUS_F), fullScreenEffect_800bb140.transforms, QueuedModelStandard.class)
        .colour(fullScreenEffect_800bb140.red0_20 / 255.0f, fullScreenEffect_800bb140.green0_1c / 255.0f, fullScreenEffect_800bb140.blue0_14 / 255.0f);
    }

    //LAB_80013adc

    // This causes the screen darkening from the lightning, etc.
    if(fullScreenEffect_800bb140.red1_18 != 0 || fullScreenEffect_800bb140.green1_10 != 0 || fullScreenEffect_800bb140.blue1_0c != 0) {
      // Make sure effect fills the whole screen
      final float fullWidth = Math.max(RENDERER.getNativeWidth(), (float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight() * displayHeight_1f8003e4 * 1.1f);
      fullScreenEffect_800bb140.transforms
        .scaling(fullWidth, displayHeight_1f8003e4, 1.0f)
        .translate(0.0f, 0.0f, 156.0f)
      ;

      //LAB_80013b10
      RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.B_MINUS_F), fullScreenEffect_800bb140.transforms, QueuedModelStandard.class)
        .colour(fullScreenEffect_800bb140.red1_18 / 255.0f, fullScreenEffect_800bb140.green1_10 / 255.0f, fullScreenEffect_800bb140.blue1_0c / 255.0f);
    }

    final int v1 = Math.min(fullScreenEffect_800bb140.totalFrames_08, (RENDERER.getVsyncCount() - fullScreenEffect_800bb140.startTime_04) / 2);

    //LAB_800137d0
    final int colour;
    if(fullScreenEffect_800bb140.totalFrames_08 == 0) {
      colour = 0;
    } else {
      final int a1 = fullScreenEffect_800bb140._24;
      if(a1 == 0) {
        colour = 0;
      } else if(a1 == 1) {
        //LAB_80013818
        colour = v1 * 255 / fullScreenEffect_800bb140.totalFrames_08;
        //LAB_80013808
      } else if(a1 == 2) {
        //LAB_8001383c
        colour = v1 * 255 / fullScreenEffect_800bb140.totalFrames_08 ^ 0xff;

        if(colour == 0) {
          //LAB_80013874
          fullScreenEffect_800bb140._24 = 0;
        }
      } else {
        fullScreenEffect_800bb140.type_00 = 0;
        fullScreenEffect_800bb140._24 = 0;
        colour = 0;
      }
    }

    //LAB_80013880
    //LAB_80013884
    fullScreenEffect_800bb140.currentColour_28 = colour;

    if(colour != 0) {
      //LAB_800138f0
      //LAB_80013948
      switch(fullScreenEffect_800bb140.type_00) {
        case 1, 2 -> drawFullScreenRect(colour, Translucency.B_MINUS_F);
        case 3, 4 -> drawFullScreenRect(colour, Translucency.B_PLUS_F);

        case 5 -> {
          final int maxX = (int)((Math.max(RENDERER.getNativeWidth(), (float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight() * displayHeight_1f8003e4) + 39) / 40);
          for(int x = 0; x < maxX; x++) {
            //LAB_800138f8
            for(int y = 0; y < 6; y++) {
              drawBoxagonEffect(colour - (12 - (x + y)) * 11, x, y);
            }
          }
        }

        case 6 -> {
          final int maxX = (int)((Math.max(RENDERER.getNativeWidth(), (float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight() * displayHeight_1f8003e4) + 39) / 40);
          for(int x = 0; x < maxX; x++) {
            //LAB_80013950
            for(int y = 0; y < 6; y++) {
              drawBoxagonEffect(colour - (x + y) * 11, x, y);
            }
          }
        }
      }
    }

    //LAB_80013c20
  }

  @Method(0x80013c3cL)
  public static void drawFullScreenRect(final int colour, final Translucency transMode) {
    // Make sure effect fills the whole screen
    final float fullWidth = Math.max(RENDERER.getNativeWidth(), (float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight() * displayHeight_1f8003e4 * 1.1f);
    fullScreenEffect_800bb140.transforms
      .scaling(fullWidth, displayHeight_1f8003e4, 1.0f)
      .translate(0.0f, 0.0f, 120.0f)
    ;

    RENDERER.queueOrthoModel(RENDERER.plainQuads.get(transMode), fullScreenEffect_800bb140.transforms, QueuedModelStandard.class)
      .monochrome(colour / 255.0f)
      .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());
  }

  @Method(0x80013d78L)
  public static void drawBoxagonEffect(final int angle, final int x, final int y) {
    if(angle > 0) {
      final int sin;
      final int cos;
      if(angle > 123) {
        sin = 20;
        cos = 0;
      } else {
        final int theta = angle * 0x400 / 123;
        sin = rsin(theta) * 20 / 0x1000;
        cos = rcos(theta) / 17;
      }

      //LAB_80013e18
      final int x0 = x * 40;
      final int y0 = y * 40;

      fullScreenEffect_800bb140.transforms.identity().setTranslation(x0, y0, 120.0f);

      final Obj obj = new PolyBuilder("Boxagon", VertexOrder.TRIANGLE_STRIP)
        .bufferUsage(BufferUsage.STREAMING)
        .addVertex(-sin + 20, -sin + 20, 0.0f)
        .monochrome(cos / 255.0f)
        .addVertex(40, 0, 0.0f)
        .monochrome(cos / 255.0f)
        .addVertex(0, 40, 0.0f)
        .monochrome(cos / 255.0f)
        .addVertex(sin + 20, sin + 20, 0.0f)
        .monochrome(cos / 255.0f)
        .build()
      ;

      obj.delete();
      RENDERER.queueOrthoModel(obj, fullScreenEffect_800bb140.transforms, QueuedModelStandard.class);
    }

    //LAB_80013f1c
  }

  @ScriptDescription("Starts a full-screen fade effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The type of effect to start")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames until the effect finishes")
  @Method(0x8001751cL)
  public static FlowControl scriptStartFadeEffect(final RunningScript<?> script) {
    startFadeEffect(script.params_20[0].get(), Math.max(1, script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }
}
