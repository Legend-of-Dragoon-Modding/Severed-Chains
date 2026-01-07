package legend.game;

import legend.core.GameEngine;
import legend.core.QueuedModelStandard;
import legend.core.font.Font;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputCodepoints;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.BackgroundType;
import legend.game.types.LodString;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxBorderMetrics0c;
import legend.game.types.TextboxChar08;
import legend.game.types.TextboxState;
import legend.game.types.TextboxText84;
import legend.game.types.TextboxTextState;
import legend.game.types.Translucency;
import legend.lodmod.LodEngineStateTypes;
import org.joml.Math;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Audio.playSound;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Graphics.centreScreenX_1f8003dc;
import static legend.game.Graphics.centreScreenY_1f8003de;
import static legend.game.Graphics.displayWidth_1f8003e0;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;

public final class Text {
  private Text() { }

  private static final TextboxBorderMetrics0c[] textboxBorderMetrics_800108b0 = {
    new TextboxBorderMetrics0c(0, 0, 0, 0, 6, 8),
    new TextboxBorderMetrics0c(1, 1, 48, 0, 6, 8),
    new TextboxBorderMetrics0c(2, 2, 0, 32, 6, 8),
    new TextboxBorderMetrics0c(3, 3, 48, 32, 6, 8),
    new TextboxBorderMetrics0c(0, 1, 16, 0, -4, 8),
    new TextboxBorderMetrics0c(0, 2, 0, 16, 6, -4),
    new TextboxBorderMetrics0c(1, 3, 48, 16, 6, -4),
    new TextboxBorderMetrics0c(2, 3, 16, 32, -4, 8),
  };

  private static final LodString[] digits_80052b40 = { new LodString("0"), new LodString("1"), new LodString("2"), new LodString("3"), new LodString("4"), new LodString("5"), new LodString("6"), new LodString("7"), new LodString("8"), new LodString("9") };
  public static final boolean[] renderBorder_80052b68 = {false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
  public static final int[] textboxMode_80052b88 = {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
  public static final int[] textboxTextType_80052ba8 = {0, 1, 2, 3, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

  private static final TextboxArrow0c[] textboxArrows_800bdea0 = new TextboxArrow0c[8];

  public static int textZ_800bdf00;

  private static final int[] textboxVariables_800bdf10 = new int[10];
  public static final TextboxText84[] textboxText_800bdf38 = new TextboxText84[8];
  public static final Textbox4c[] textboxes_800be358 = new Textbox4c[8];

  private static Obj textboxBackgroundObj;
  private static final Obj[] textboxBorderObjs = new Obj[8];

  /** One per animation frame */
  private static final Obj[] textboxArrowObjs = new Obj[7];
  private static final MV textboxArrowTransforms = new MV();

  private static Obj textboxSelectionObj;
  private static final MV textboxSelectionTransforms = new MV();

  private static long autoTextDelayStart;

  public static void initTextboxes() {
    //LAB_800250c0
    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      textboxes_800be358[i] = new Textbox4c();
      textboxes_800be358[i].state_00 = TextboxState.UNINITIALIZED_0;

      textboxText_800bdf38[i] = new TextboxText84();
      textboxText_800bdf38[i].state_00 = TextboxTextState.UNINITIALIZED_0;

      textboxArrows_800bdea0[i] = new TextboxArrow0c();

      setTextboxArrowPosition(i, false);
    }

    //LAB_80025118
    for(int i = 0; i < 10; i++) {
      textboxVariables_800bdf10[i] = 0;
    }
  }

  @ScriptDescription("Sets a textbox's text and type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The textbox type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x80025158L)
  public static FlowControl scriptSetTextboxContents(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    clearTextboxText(textboxIndex);

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    textboxText.type_04 = script.params_20[1].get();
    textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
    textboxText.str_24 = LodString.fromParam(script.params_20[2]);
    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
    calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    return FlowControl.CONTINUE;
  }

  /** Allocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @ScriptDescription("Adds a textbox to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Bit flags for textbox properties")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x800254bcL)
  public static FlowControl scriptAddTextbox(final RunningScript<?> script) {
    final int packed = script.params_20[1].get();

    if(packed != 0) {
      final int textboxIndex = script.params_20[0].get();
      final int textType = textboxTextType_80052ba8[packed >>> 8 & 0xf];
      clearTextbox(textboxIndex);

      final Textbox4c textbox = textboxes_800be358[textboxIndex];
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textbox.backgroundType_04 = BackgroundType.fromInt(textboxMode_80052b88[packed >>> 4 & 0xf]);
      textbox.renderBorder_06 = renderBorder_80052b68[packed & 0xf];
      textbox.x_14 = script.params_20[2].get();
      textbox.y_16 = script.params_20[3].get();
      textbox.chars_18 = script.params_20[4].get() + 1;
      textbox.lines_1a = script.params_20[5].get() + 1;
      textboxText.type_04 = textType;
      textboxText.str_24 = LodString.fromParam(script.params_20[6]);

      // This is a stupid hack to allow inns to display 99,999,999 gold without the G falling down to the next line (see GH#546)
      if("?Funds ?G".equals(textboxText.str_24.get())) {
        textbox.chars_18++;
      }

      clearTextboxText(textboxIndex);

      if(textType == 1 && (packed & 0x1000) != 0) {
        textboxText.flags_08 |= TextboxText84.NO_INPUT;
      }

      //LAB_8002562c
      //LAB_80025630
      if(textType == 3) {
        textboxText.selectionIndex_6c = -1;
      }

      //LAB_80025660
      if(textType == 4) {
        textboxText.flags_08 |= TextboxText84.HAS_NAME;
      }

      //LAB_80025690
      /* Not a retail flag. Used to remove arrows from overlapping textboxes for Phantom Ship's code-locked chest. */
      if((packed & TextboxText84.NO_ARROW) == 0) {
        textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
      }
      textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
      Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
      calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    }

    //LAB_800256f0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a textbox with selectable lines (like Yes/No, Enter/Don't Enter)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "minSelectionIndex", description = "The first selectable line index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxSelectionIndex", description = "The last selectable line index")
  @Method(0x80025718L)
  public static FlowControl scriptAddSelectionTextbox(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];

    textboxText.selectionIndex_6c = -1;
    textboxText.minSelectionLine_72 = script.params_20[1].get();
    textboxText.maxSelectionLine_70 = script.params_20[2].get();

    if(textboxText.state_00 == TextboxTextState._13) {
      textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
      textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
      textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
      playSound(0, 4, (short)0, (short)0);
    }

    //LAB_800257bc
    textboxText.flags_08 |= TextboxText84.SELECTION;
    return FlowControl.CONTINUE;
  }

  public static void initTextboxGeometry() {
    textboxBackgroundObj = new QuadBuilder("TextboxBackground")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .pos(-1.0f, -1.0f, 0.0f)
      .size(2.0f, 2.0f)
      .rgb(1.0f, 1.0f, 1.0f)
      .monochrome(0, 0.0f)
      .monochrome(3, 0.0f)
      .build();
    textboxBackgroundObj.persistent = true;

    for(int borderIndex = 0; borderIndex < 8; borderIndex++) {
      final TextboxBorderMetrics0c borderMetrics = textboxBorderMetrics_800108b0[borderIndex];
      final int u = borderMetrics.u_04;
      final int v = borderMetrics.v_06;

      textboxBorderObjs[borderIndex] = new QuadBuilder("TextboxBorder" + borderIndex)
        .bpp(Bpp.BITS_4)
        .clut(832, 484)
        .vramPos(896, 256)
        .size(16, 16)
        .uv(u, v)
        .build();
      textboxBorderObjs[borderIndex].persistent = true;
    }

    for(int i = 0; i < textboxArrowObjs.length; i++) {
      textboxArrowObjs[i] = new QuadBuilder("TextboxArrow" + i)
        .bpp(Bpp.BITS_4)
        .monochrome(1.0f)
        .clut(1008, 484)
        .vramPos(896, 256)
        .pos(-8.0f, -6.0f, 0.0f)
        .size(16.0f, 14.0f)
        .uv(64.0f + i * 16.0f, 0.0f)
        .build();
      textboxArrowObjs[i].persistent = true;
    }

    textboxSelectionObj = new QuadBuilder("TextboxSelection")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .rgb(0.5f, 0.19607843f, 0.39215687f)
      .size(1.0f, 12.0f)
      .build();
    textboxSelectionObj.persistent = true;
  }

  /** Deallocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @Method(0x800257e0L)
  public static void clearTextbox(final int textboxIndex) {
    if(textboxText_800bdf38[textboxIndex].state_00 != TextboxTextState.UNINITIALIZED_0) {
      textboxText_800bdf38[textboxIndex].delete();
    }

    //LAB_80025824
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox.clear();
  }

  @Method(0x800258a8L)
  public static void clearTextboxText(final int textboxIndex) {
    final TextboxText84 text = textboxText_800bdf38[textboxIndex];
    text.clear();

    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    text.x_14 = textbox.x_14;
    text.y_16 = textbox.y_16;
    text.chars_1c = textbox.chars_18 - 1;
    text.lines_1e = textbox.lines_1a - 1;
    text._18 = text.x_14 - text.chars_1c * 9 / 2;
    text._1a = text.y_16 - text.lines_1e * 6;
  }

  @Method(0x80025a04L)
  public static void handleTextbox(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    switch(textbox.state_00) {
      case _1 -> {
        switch(textbox.backgroundType_04) {
          case NO_BACKGROUND -> {
            //LAB_80025ab8
            textbox.state_00 = TextboxState._4;
            textbox.flags_08 ^= Textbox4c.RENDER_BACKGROUND;
          }

          case NORMAL -> {
            //LAB_80025b54
            //LAB_80025b5c
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
              textbox.state_00 = TextboxState._5;
            } else {
              textbox.state_00 = TextboxState._6;
            }

            //LAB_80025bc0
            textbox.flags_08 |= Textbox4c.RENDER_BACKGROUND;
          }

          case ANIMATE_IN_OUT -> {
            //LAB_80025ad4
            textbox.state_00 = TextboxState._2;
            textbox.flags_08 |= Textbox4c.ANIMATING;
            textbox.currentTicks_10 = 0;
            textbox.animationTicks_24 = 60 / vsyncMode_8007a3b8 / 4;

            if((textbox.flags_08 & 0x2) != 0) {
              textbox.stepX_30 = (textbox.currentX_28 - textbox._38) / textbox.animationTicks_24;
              textbox.stepY_34 = (textbox.currentY_2c - textbox._3c) / textbox.animationTicks_24;
            }
          }
        }
      }

      case _2 -> {
        textbox.flags_08 |= Textbox4c.RENDER_BACKGROUND;

        if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
          textbox.animationWidth_20 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.animationHeight_22 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox.animationWidth_20 >> 12;
          textbox.height_1e = textbox.animationHeight_22 * 6 * textbox.lines_1a >> 12;
          textbox.currentTicks_10++;

          if((textbox.flags_08 & 0x2) != 0) {
            textbox.currentX_28 -= textbox.stepX_30;
            textbox.currentY_2c -= textbox.stepY_34;
            textbox.x_14 = textbox.currentX_28;
            textbox.y_16 = textbox.currentY_2c;
          }

          //LAB_80025cf0
          if(textbox.currentTicks_10 >= textbox.animationTicks_24) {
            textbox.flags_08 ^= Textbox4c.ANIMATING;
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
              textbox.state_00 = TextboxState._5;
            } else {
              textbox.state_00 = TextboxState._6;
            }

            //LAB_80025d5c
            if((textbox.flags_08 & 0x2) != 0) {
              textbox.x_14 = textbox._38;
              textbox.y_16 = textbox._3c;
            }
          }

          break;
        }

        //LAB_80025d84
        if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
          textbox.state_00 = TextboxState._5;
        } else {
          textbox.state_00 = TextboxState._6;
        }
      }

      case ANIMATE_OUT_3 -> {
        if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
          textbox.animationWidth_20 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.animationHeight_22 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox.animationWidth_20 >> 12;
          textbox.height_1e = textbox.animationHeight_22 * 6 * textbox.lines_1a >> 12;
          textbox.currentTicks_10--;

          if(textbox.currentTicks_10 <= 0) {
            textbox.width_1c = 0;
            textbox.height_1e = 0;
            textbox.state_00 = TextboxState.UNINITIALIZED_0;
            textbox.flags_08 ^= Textbox4c.ANIMATING;
          }

          break;
        }

        //LAB_80025e94
        textbox.state_00 = TextboxState.UNINITIALIZED_0;
      }

      case _4, _5 -> {
        if(textboxText_800bdf38[textboxIndex].state_00 == TextboxTextState.UNINITIALIZED_0) {
          if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
            textbox.state_00 = TextboxState.ANIMATE_OUT_3;
            textbox.flags_08 |= Textbox4c.ANIMATING;

            final int ticks = 60 / vsyncMode_8007a3b8 / 4;
            textbox.currentTicks_10 = ticks;
            textbox.animationTicks_24 = ticks;
          } else {
            //LAB_80025f30
            textbox.state_00 = TextboxState.UNINITIALIZED_0;
          }

          //LAB_80025f34
          setTextboxArrowPosition(textboxIndex, false);
        }
      }
    }

    //LAB_80025f3c
  }

  @Method(0x80025f4cL)
  public static void renderTextboxBackground(final Textbox4c textbox) {
    //LAB_80025f7c
    if(textbox.backgroundType_04 != BackgroundType.NO_BACKGROUND) {
      if(textbox.state_00 != TextboxState._1) {
        if(textbox.x_14 != textbox.oldX || textbox.y_16 != textbox.oldY || textbox.width_1c != textbox.oldW || textbox.height_1e != textbox.oldH) {
          textbox.backgroundTransforms.transfer.set(textbox.x_14, textbox.y_16, textbox.z_0c * 4.0f + 1.0f);
          textbox.backgroundTransforms.scaling(textbox.width_1c, textbox.height_1e, 1.0f);

          textbox.oldX = textbox.x_14;
          textbox.oldY = textbox.y_16;
          textbox.oldW = textbox.width_1c;
          textbox.oldH = textbox.height_1e;
          textbox.updateBorder = true;
        }

        RENDERER.queueOrthoModel(textboxBackgroundObj, textbox.backgroundTransforms, QueuedModelStandard.class)
          .colour(textbox.colour)
          .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());

        if(textbox.renderBorder_06) {
          renderTextboxBorder(textbox, textbox.x_14 - textbox.width_1c, textbox.y_16 - textbox.height_1e, textbox.x_14 + textbox.width_1c, textbox.y_16 + textbox.height_1e);
        }
      }
    }

    //LAB_800261a0
  }

  @Method(0x800261c0L)
  public static void renderTextboxBorder(final Textbox4c textbox, final float boxLeft, final float boxTop, final float boxRight, final float boxBottom) {
    final float[] xs = {
      boxLeft + 4,
      boxRight - 4,
      boxLeft + 4,
      boxRight - 4,
    };

    final float[] ys = {
      boxTop + 5,
      boxTop + 5,
      boxBottom - 5,
      boxBottom - 5,
    };

    if(textbox.animationWidth_20 != textbox.oldScaleW || textbox.animationHeight_22 != textbox.oldScaleH) {
      textbox.updateBorder = true;
    }

    //LAB_800262e4
    for(int borderIndex = 0; borderIndex < 8; borderIndex++) {
      final TextboxBorderMetrics0c borderMetrics = textboxBorderMetrics_800108b0[borderIndex];

      int w = borderMetrics.w_08;
      int h = borderMetrics.h_0a;
      if((textbox.flags_08 & Textbox4c.ANIMATING) != 0) {
        w = w * textbox.animationWidth_20 >> 12;
        h = h * textbox.animationHeight_22 >> 12;
      }

      //LAB_8002637c
      final float left = xs[borderMetrics.topLeftVertexIndex_00] - w;
      final float right = xs[borderMetrics.bottomRightVertexIndex_02] + w;
      final float top = ys[borderMetrics.topLeftVertexIndex_00] - h;
      final float bottom = ys[borderMetrics.bottomRightVertexIndex_02] + h;

      if(textbox.updateBorder) {
        textbox.borderTransforms[borderIndex].transfer.set(left, top, textbox.z_0c * 4.0f);
        textbox.borderTransforms[borderIndex]
          .scaling((right - left) / 16.0f, (bottom - top) / 16.0f, 1.0f);
      }

      RENDERER.queueOrthoModel(textboxBorderObjs[borderIndex], textbox.borderTransforms[borderIndex], QueuedModelStandard.class)
        .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());
    }

    textbox.oldScaleW = textbox.animationWidth_20;
    textbox.oldScaleH = textbox.animationHeight_22;
    textbox.updateBorder = false;
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void handleTextboxText(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    switch(textboxText.state_00) {
      case _1 -> {
        //LAB_8002663c
        if((textbox.flags_08 & 0x1) == 0) {
          switch(textboxText.type_04) {
            case 0 -> textboxText.state_00 = TextboxTextState._12;

            case 2 -> {
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1.0f / currentEngineState_8004dd04.tickMultiplier();
              textboxText.charX_34 = 0;
              textboxText.charY_36 = textboxText.lines_1e;
            }

            case 3 -> {
              textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1.0f / currentEngineState_8004dd04.tickMultiplier();
              textboxText.charX_34 = 0;
              textboxText.charY_36 = 0;
              textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
              textboxText.stateAfterTransition_78 = TextboxTextState._17;
              playSound(0, 4, (short)0, (short)0);
            }

            case 4 -> {
              //LAB_80026780
              do {
                processTextboxLine(textboxIndex);
              } while((textboxText.flags_08 & TextboxText84.PROCESSED_NEW_LINE) == 0);

              textboxText.flags_08 ^= TextboxText84.PROCESSED_NEW_LINE;
            }

            default ->
              //LAB_800267a0
              textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
          }
        }
      }

      case _2 -> textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;

      //LAB_80026538
      case PROCESS_TEXT_4 ->
        //LAB_800267c4
        processTextboxLine(textboxIndex);

      case SCROLL_TEXT_5 -> {
        //LAB_800267d4
        if((textboxText.flags_08 & 0x1) != 0) {
          //LAB_800267f4
          if(textboxText.linesScrolled_3a < textboxText.lines_1e - ((textboxText.flags_08 & TextboxText84.HAS_NAME) == 0 ? 1 : 2)) {
            //LAB_80026828
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
            textboxText.linesScrolled_3a++;
            scrollTextboxDown(textboxIndex);
          } else {
            textboxText.flags_08 ^= 0x1;
            textboxText.linesScrolled_3a = 0;
            setTextboxArrowPosition(textboxIndex, true);
          }
          //LAB_8002684c
        } else if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
          textboxText.flags_08 |= 0x1;
        } else {
          if(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && autoTextDelayStart == 0) {
            autoTextDelayStart = System.nanoTime();
          }
          //LAB_8002686c
          if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || (CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && System.nanoTime() - autoTextDelayStart >= CONFIG.getConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get()) * 1_000_000_000)) {
            autoTextDelayStart = 0;
            setTextboxArrowPosition(textboxIndex, false);

            if(textboxText.type_04 == 1 || textboxText.type_04 == 4) {
              //LAB_800268b4
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
              textboxText.flags_08 |= 0x1;
            }

            if(textboxText.type_04 == 2) {
              //LAB_800268d0
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
            }
          }
        }
      }

      case _6 -> {
        if(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && autoTextDelayStart == 0) {
          autoTextDelayStart = System.nanoTime();
        }
        //LAB_800268dc
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || (CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && System.nanoTime() - autoTextDelayStart >= CONFIG.getConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get()) * 1_000_000_000)) {
          autoTextDelayStart = 0;
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }
      }

      case _7 -> {
        //LAB_800268fc
        textboxText._40++;
        if(textboxText._40 >= textboxText._3e) {
          textboxText._40 = 0;
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }

        //LAB_80026928
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) == 0) {
          if(PLATFORM.isActionHeld(INPUT_ACTION_MENU_CONFIRM.get()) || CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get())) {
            boolean found = false;

            //LAB_80026954
            for(int lineIndex = 0; lineIndex < 4; lineIndex++) {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState.SCROLL_TEXT_5 || textboxText.state_00 == TextboxTextState._6 || textboxText.state_00 == TextboxTextState._15 || textboxText.state_00 == TextboxTextState._11 || textboxText.state_00 == TextboxTextState._13) {
                //LAB_8002698c
                found = true;
                break;
              }

              //LAB_80026994
            }

            //LAB_800269a0
            if(!found) {
              textboxText._40 = 0;
              textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
            }
          }
        }
      }

      case WAIT_FOR_PAUSE_8 -> {
        //LAB_800269cc
        if(textboxText.pauseTimer_44 > 0) {
          //LAB_800269e8
          textboxText.pauseTimer_44--;
        } else {
          //LAB_800269e0
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }
      }

      //LAB_80026554
      case SCROLL_TEXT_DOWN_9 ->
        //LAB_800269f0
        scrollTextboxDown(textboxIndex);

      //LAB_80026580
      case SCROLL_TEXT_UP_10 -> {
        //LAB_80026a00
        scrollTextboxUp(textboxIndex);

        if((textboxText.flags_08 & 0x4) != 0) {
          textboxText.flags_08 ^= 0x4;

          if((textboxText.flags_08 & 0x2) == 0) {
            //LAB_80026a5c
            do {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState._15) {
                textboxText.linesScrolled_3a = 0;
                textboxText.flags_08 |= 0x2;
                break;
              }
            } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

            //LAB_80026a8c
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
          } else {
            textboxText.linesScrolled_3a++;

            if(textboxText.linesScrolled_3a >= textboxText.lines_1e + 1) {
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }
        }
      }

      case _11 -> {
        if(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && autoTextDelayStart == 0) {
          autoTextDelayStart = System.nanoTime();
        }
        //LAB_80026a98
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || (CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && System.nanoTime() - autoTextDelayStart >= CONFIG.getConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get()) * 1_000_000_000)) {
          autoTextDelayStart = 0;
          setTextboxArrowPosition(textboxIndex, false);
          clearTextboxChars(textboxIndex);

          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
          textboxText.flags_08 ^= 0x1;
          textboxText.charX_34 = 0;
          textboxText.charY_36 = 0;
          textboxText.linesScrolled_3a = 0;

          if((textboxText.flags_08 & 0x8) != 0) {
            textboxText.state_00 = TextboxTextState._13;
          }
        }
      }

      case _12 -> {
        //LAB_80026af0
        if(textbox.state_00 == TextboxState.UNINITIALIZED_0) {
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.inputActions.clear();
        }
      }

      case _13 -> {
        //LAB_80026b34
        textboxText.flags_08 |= 0x8;
        setTextboxArrowPosition(textboxIndex, true);

        //LAB_80026b4c
        do {
          processTextboxLine(textboxIndex);

          if(textboxText.state_00 == TextboxTextState.SCROLL_TEXT_5) {
            //LAB_80026b28
            textboxText.state_00 = TextboxTextState._11;
            break;
          }
        } while(textboxText.state_00 != TextboxTextState._15);

        //LAB_80026b6c
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          setTextboxArrowPosition(textboxIndex, false);
        }

        //LAB_80026ba0
        if(textboxText._3e != 0) {
          setTextboxArrowPosition(textboxIndex, false);
          textboxText._5c = textboxText.state_00;
          textboxText.state_00 = TextboxTextState._14;
        }

        //LAB_80026bc8
        if((textboxText.flags_08 & TextboxText84.SELECTION) != 0) {
          setTextboxArrowPosition(textboxIndex, false);
          textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
          textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
          textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
          playSound(0, 4, (short)0, (short)0);
        }
      }

      case _14 -> {
        //LAB_80026c18
        if((textboxText.flags_08 & 0x40) == 0) {
          textboxText._40--;

          if(textboxText._40 <= 0) {
            textboxText._40 = textboxText._3e;

            if(textboxText._5c == TextboxTextState._11) {
              //LAB_80026c70
              clearTextboxChars(textboxIndex);
              textboxText.charX_34 = 0;
              textboxText.charY_36 = 0;
              textboxText.linesScrolled_3a = 0;
              textboxText.state_00 = TextboxTextState._13;
              textboxText.flags_08 ^= 0x1;
            } else if(textboxText._5c == TextboxTextState._15) {
              //LAB_80026c98
              //LAB_80026c9c
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }
        }
      }

      case _15 -> {
        //LAB_80026cb0
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          textboxText.state_00 = TextboxTextState._16;
        } else {
          if(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && autoTextDelayStart == 0) {
            autoTextDelayStart = System.nanoTime();
          }
          //LAB_80026cd0
          if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || (CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()) && System.nanoTime() - autoTextDelayStart >= CONFIG.getConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get()) * 1_000_000_000)) {
            autoTextDelayStart = 0;
            textboxText.delete();
            textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            setTextboxArrowPosition(textboxIndex, false);
          }
        }
      }

      //LAB_800265d8
      case _16 -> {
        //LAB_80026cdc
        //LAB_80026ce8
        if((textboxText.flags_08 & 0x40) != 0) {
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          setTextboxArrowPosition(textboxIndex, false);
        }
      }

      case _17 -> {
        //LAB_80026d20
        textboxText.lines_1e++;

        //LAB_80026d30
        do {
          processTextboxLine(textboxIndex);

          if(textboxText.state_00 == TextboxTextState._15) {
            textboxText.linesScrolled_3a = 0;
            textboxText.flags_08 |= 0x102;
            break;
          }
        } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

        //LAB_80026d64
        textboxText.state_00 = TextboxTextState._18;
        textboxText.selectionIndex_6c = -1;
        textboxText.lines_1e--;
      }

      //LAB_8002659c
      case _18 -> {
        //LAB_80026d94
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          playSound(0, 2, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68;
        } else {
          //LAB_80026df0
          if(!PLATFORM.isActionHeld(INPUT_ACTION_MENU_DOWN.get())) {
            //LAB_80026ee8
            if(PLATFORM.isActionHeld(INPUT_ACTION_MENU_UP.get())) {
              if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                //LAB_80026f38
                playSound(0, 1, (short)0, (short)0);

                int extraLines = 3;
                if(textboxText.selectionLine_60 > 0) {
                  textboxText.state_00 = TextboxTextState._19;
                  textboxText.selectionLine_60--;
                  textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
                  textboxText.selectionLine_68--;
                } else {
                  //LAB_80026f88
                  if((textboxText.flags_08 & 0x2) != 0) {
                    // TODO not sure about this block of code
                    if(textboxText.linesScrolled_3a == 1) {
                      extraLines = 1;
                    } else {
                      if(textboxText.linesScrolled_3a == 0) {
                        //LAB_80026fbc
                        extraLines = 2;
                      }

                      //LAB_80026fc0
                      textboxText.linesScrolled_3a = 0;
                      textboxText.flags_08 ^= 0x2;
                    }

                    //LAB_80026fe8
                    textboxText.linesScrolled_3a--;
                  }

                  //LAB_80027014
                  textboxText.selectionLine_68--;

                  if(textboxText.selectionLine_68 < 0) {
                    textboxText.selectionLine_68 = 0;
                  } else {
                    //LAB_80027044
                    textboxText.scrollAmount_2c = 12.0f;
                    rewindTextbox(textboxIndex);

                    final LodString str = textboxText.str_24;

                    //LAB_80027068
                    int lineIndex = 0;
                    do {
                      if(str.charAt(textboxText.charIndex_30 - 1) >>> 8 == TextboxText84.LINE) {
                        lineIndex++;
                      }

                      //LAB_80027090
                      if(lineIndex == textboxText.lines_1e + extraLines) {
                        break;
                      }

                      textboxText.charIndex_30--;
                    } while(textboxText.charIndex_30 > 0);

                    //LAB_800270b0
                    textboxText.charX_34 = 0;
                    textboxText.charY_36 = 0;
                    textboxText.flags_08 |= 0x80;

                    //LAB_800270dc
                    do {
                      processTextboxLine(textboxIndex);
                    } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

                    //LAB_80027104
                    textboxText.state_00 = TextboxTextState._21;
                    textboxText.flags_08 ^= 0x80;
                  }
                }
              }
            }
          }

          textboxText.state_00 = TextboxTextState._19;
          textboxText.selectionLine_60++;
          textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.selectionLine_68++;

          if((textboxText.flags_08 & 0x100) == 0 || textboxText.charY_36 + 1 != textboxText.selectionLine_68) {
            //LAB_80026e68
            //LAB_80026e6c
            if(textboxText.selectionLine_60 < textboxText.lines_1e) {
              //LAB_80026ed0
              playSound(0, 1, (short)0, (short)0);

              //LAB_80026ee8
              if(PLATFORM.isActionHeld(INPUT_ACTION_MENU_UP.get())) {
                if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                  //LAB_80026f38
                  playSound(0, 1, (short)0, (short)0);

                  int extraLines = 3;
                  if(textboxText.selectionLine_60 > 0) {
                    textboxText.state_00 = TextboxTextState._19;
                    textboxText.selectionLine_60--;
                    textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
                    textboxText.selectionLine_68--;
                  } else {
                    //LAB_80026f88
                    if((textboxText.flags_08 & 0x2) != 0) {
                      // TODO not sure about this block of code
                      if(textboxText.linesScrolled_3a == 1) {
                        extraLines = 1;
                      } else {
                        if(textboxText.linesScrolled_3a == 0) {
                          //LAB_80026fbc
                          extraLines = 2;
                        }

                        //LAB_80026fc0
                        textboxText.linesScrolled_3a = 0;
                        textboxText.flags_08 ^= 0x2;
                      }

                      //LAB_80026fe8
                      textboxText.linesScrolled_3a--;
                    }

                    //LAB_80027014
                    textboxText.selectionLine_68--;

                    if(textboxText.selectionLine_68 < 0) {
                      textboxText.selectionLine_68 = 0;
                    } else {
                      //LAB_80027044
                      textboxText.scrollAmount_2c = 12.0f;
                      rewindTextbox(textboxIndex);

                      final LodString str = textboxText.str_24;

                      //LAB_80027068
                      int lineIndex = 0;
                      do {
                        if(str.charAt(textboxText.charIndex_30 - 1) >>> 8 == TextboxText84.LINE) {
                          lineIndex++;
                        }

                        //LAB_80027090
                        if(lineIndex == textboxText.lines_1e + extraLines) {
                          break;
                        }

                        textboxText.charIndex_30--;
                      } while(textboxText.charIndex_30 > 0);

                      //LAB_800270b0
                      textboxText.charX_34 = 0;
                      textboxText.charY_36 = 0;
                      textboxText.flags_08 |= 0x80;

                      //LAB_800270dc
                      do {
                        processTextboxLine(textboxIndex);
                      } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

                      //LAB_80027104
                      textboxText.state_00 = TextboxTextState._21;
                      textboxText.flags_08 ^= 0x80;
                    }
                  }
                }
              }
            } else {
              textboxText.selectionLine_60 = textboxText_800bdf38[textboxIndex].lines_1e - 1;
              textboxText.state_00 = TextboxTextState._20;
              textboxText.scrollAmount_2c = 0.0f;

              if(textboxText.linesScrolled_3a == 1) {
                textboxText.state_00 = TextboxTextState._18;
                textboxText.selectionLine_68--;
              }
            }
          } else {
            textboxText.state_00 = TextboxTextState._3;
            textboxText.selectionLine_60--;
            textboxText.selectionLine_68--;
          }
        }
      }

      case _19 -> {
        //LAB_8002711c
        textboxText.ticksUntilStateTransition_64--;

        if(textboxText.ticksUntilStateTransition_64 == 0) {
          textboxText.state_00 = TextboxTextState._18;

          if((textboxText.flags_08 & TextboxText84.SELECTION) != 0) {
            textboxText.state_00 = TextboxTextState.SELECTION_22;
          }
        }
      }

      case _20 -> {
        //LAB_8002715c
        textboxText.scrollAmount_2c += 4.0f / currentEngineState_8004dd04.tickMultiplier();

        if(textboxText.scrollAmount_2c >= 12.0f) {
          advanceTextbox(textboxIndex);
          textboxText.flags_08 |= 0x4;
          textboxText.scrollAmount_2c -= 12.0f;
          textboxText.charY_36 = textboxText.lines_1e;
        }

        //LAB_800271a8
        if((textboxText.flags_08 & 0x4) != 0) {
          textboxText.flags_08 ^= 0x4;

          if((textboxText.flags_08 & 0x2) == 0) {
            //LAB_8002720c
            //LAB_80027220
            do {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState._15) {
                textboxText.linesScrolled_3a = 0;
                textboxText.flags_08 |= 0x2;
                break;
              }
            } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);
          } else {
            textboxText.linesScrolled_3a++;

            if(textboxText.linesScrolled_3a >= textboxText.lines_1e + 1) {
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }

          //LAB_80027250
          //LAB_80027254
          textboxText.state_00 = TextboxTextState._18;
        }
      }

      //LAB_800265f4
      case _21 -> {
        //LAB_8002727c
        textboxText.scrollAmount_2c -= 4.0f / currentEngineState_8004dd04.tickMultiplier();
        if(textboxText.scrollAmount_2c <= 0.0f) {
          textboxText.charY_36 = 0;
          textboxText.scrollAmount_2c = 0.0f;
          textboxText.state_00 = TextboxTextState._18;
          textboxText.flags_08 |= 0x4;
        }

        //LAB_800272b0
        if((textboxText.flags_08 & 0x4) != 0) {
          final LodString str = textboxText.str_24;

          //LAB_800272dc
          int lineIndex = 0;
          do {
            final int control = str.charAt(textboxText.charIndex_30 + 1) >>> 8;
            if(control == TextboxText84.END) {
              //LAB_80027274
              textboxText.charIndex_30--;
              break;
            }

            if(control == TextboxText84.LINE) {
              lineIndex++;
            }

            //LAB_8002730c
            textboxText.charIndex_30++;
          } while(lineIndex != textboxText.lines_1e);

          //LAB_80027320
          textboxText.state_00 = TextboxTextState._18;
          textboxText.charIndex_30 += 2;
          textboxText.charX_34 = 0;
          textboxText.charY_36 = textboxText.lines_1e;
        }
      }

      case SELECTION_22 -> {
        //LAB_80027354
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          playSound(0, 2, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68 - textboxText.minSelectionLine_72;
        } else {
          //LAB_800273bc
          if(PLATFORM.isActionHeld(INPUT_ACTION_MENU_UP.get())) {
            textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
            textboxText.selectionLine_68--;

            if(textboxText.selectionLine_68 < textboxText.minSelectionLine_72) {
              textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
            } else {
              //LAB_80027404
              playSound(0, 1, (short)0, (short)0);
              textboxText.state_00 = TextboxTextState._19;
            }
          }

          //LAB_80027420
          if(PLATFORM.isActionHeld(INPUT_ACTION_MENU_DOWN.get())) {
            textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
            textboxText.selectionLine_68++;

            if(textboxText.selectionLine_68 > textboxText.maxSelectionLine_70) {
              textboxText.selectionLine_68 = textboxText.maxSelectionLine_70;
            } else {
              //LAB_80027480
              //LAB_80027490
              playSound(0, 1, (short)0, (short)0);
              textboxText.state_00 = TextboxTextState._19;
            }
          }
        }
      }

      //LAB_80026620
      case TRANSITION_AFTER_TIMEOUT_23 -> {  // Wait and then transition to another state
        //LAB_800274a4
        textboxText.ticksUntilStateTransition_64--;

        if(textboxText.ticksUntilStateTransition_64 == 0) {
          textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.state_00 = textboxText.stateAfterTransition_78;
        }
      }
    }

    //LAB_800274c8
    updateTextboxArrowSprite(textboxIndex);
  }

  @Method(0x800274f0L)
  public static void processTextboxLine(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    // This code would be really tricky to make work at 60 FPS, but there isn't any harm in just slowing it down
    if(textboxText.waitTicks != 0) {
      textboxText.waitTicks--;
      return;
    }

    textboxText.waitTicks = currentEngineState_8004dd04.tickMultiplier() - 1;

    final LodString str = textboxText.str_24;

    if((textboxText.flags_08 & TextboxText84.SHOW_VAR) != 0) {
      final int digitIndex = textboxText.digitIndex_80;
      appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, textboxText.digits_46[digitIndex]);

      textboxText.charX_34++;
      textboxText.digitIndex_80++;

      if(textboxText.charX_34 < textboxText.chars_1c) {
        //LAB_80027768
        if(textboxText.digits_46[digitIndex + 1] == -1) {
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
        }
      } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
        if(textboxText.digits_46[digitIndex + 1] != -1) {
          textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          setTextboxArrowPosition(textboxIndex, true);
          return;
        }

        //LAB_80027618
        final int control = str.charAt(textboxText.charIndex_30) >>> 8;

        if(control == TextboxText84.END) {
          //LAB_800276f4
          textboxText.state_00 = TextboxTextState._15;

          //LAB_80027704
          setTextboxArrowPosition(textboxIndex, true);

          //LAB_80027740
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
          return;
        }

        if(control == TextboxText84.LINE) {
          textboxText.charIndex_30++;
        }

        //LAB_8002764c
        textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        //LAB_80027704
        setTextboxArrowPosition(textboxIndex, true);
      } else {
        //LAB_80027688
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        if(textboxText.digits_46[digitIndex + 1] == -1) {
          final int control = str.charAt(textboxText.charIndex_30) >>> 8;

          if(control == TextboxText84.END) {
            //LAB_800276f4
            textboxText.state_00 = TextboxTextState._15;

            //LAB_80027704
            setTextboxArrowPosition(textboxIndex, true);
          } else {
            if(control == TextboxText84.LINE) {
              //LAB_80027714
              textboxText.charIndex_30++;
            }

            //LAB_80027724
            textboxText.state_00 = TextboxTextState._7;
          }

          //LAB_80027740
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
          return;
        }
      }

      //LAB_8002779c
      textboxText.state_00 = TextboxTextState._7;
      return;
    }

    //LAB_800277bc
    boolean parseMore = true;

    //LAB_800277cc
    do {
      final int chr = str.charAt(textboxText.charIndex_30);

      switch(chr >>> 8) {
        case TextboxText84.END -> {
          textboxText.state_00 = TextboxTextState._15;
          setTextboxArrowPosition(textboxIndex, true);
          parseMore = false;
        }

        case TextboxText84.LINE -> {
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;

          if(textboxText.charY_36 >= textboxText.lines_1e || (textboxText.flags_08 & 0x80) != 0) {
            //LAB_80027880
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;

            if((textboxText.flags_08 & 0x1) == 0) {
              setTextboxArrowPosition(textboxIndex, true);
            }

            parseMore = false;
          }
        }

        case TextboxText84.BUTTON -> {
          //LAB_80027d28
          textboxText.state_00 = TextboxTextState._6;

          //LAB_80027d2c
          parseMore = false;
        }

        case TextboxText84.MUTLIBOX -> {
          setTextboxArrowPosition(textboxIndex, true);
          textboxText.state_00 = TextboxTextState._11;

          if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
            textboxText.charIndex_30++;
          }

          parseMore = false;
        }

        case TextboxText84.SPEED -> {
          textboxText._3e = chr & 0xff;
          textboxText._40 = 0;
        }

        case TextboxText84.PAUSE -> {
          textboxText.state_00 = TextboxTextState.WAIT_FOR_PAUSE_8;
          textboxText.pauseTimer_44 = 60 / vsyncMode_8007a3b8 * (chr & 0xff);
          parseMore = false;
        }

        case TextboxText84.COLOUR ->
          //LAB_80027950
          textboxText.textColour_28 = TextColour.values()[chr & 0xf];

        case TextboxText84.VAR -> {
          textboxText.flags_08 |= TextboxText84.SHOW_VAR;

          //LAB_80027970
          Arrays.fill(textboxText.digits_46, -1);

          int variable = textboxVariables_800bdf10[chr & 0xff];
          int divisor = 1_000_000_000;
          final int[] varDigits = new int[10];
          //LAB_800279dc
          for(int i = 0; i < varDigits.length; i++) {
            varDigits[i] = digits_80052b40[variable / divisor].charAt(0);
            variable %= divisor;
            divisor /= 10;
          }

          //LAB_80027a34
          //LAB_80027a54
          int firstDigit;
          for(firstDigit = 0; firstDigit < 9; firstDigit++) {
            if(varDigits[firstDigit] != 0x15) { // 0
              break;
            }
          }

          //LAB_80027a84
          //LAB_80027a90
          for(int i = 0; i < textboxText.digits_46.length && firstDigit < varDigits.length; i++, firstDigit++) {
            textboxText.digits_46[i] = varDigits[firstDigit];
          }

          //LAB_80027ae4
          textboxText.digitIndex_80 = 0;

          //LAB_80027d2c
          parseMore = false;
        }

        case TextboxText84.X_OFFSET -> {
          final int v1_0 = chr & 0xff;

          if(v1_0 >= textboxText.chars_1c) {
            textboxText.charX_34 = textboxText.chars_1c - 1;
          } else {
            //LAB_80027b0c
            textboxText.charX_34 = v1_0;
          }
        }

        case TextboxText84.Y_OFFSET ->
          //LAB_80027b38
          textboxText.charY_36 = org.joml.Math.min(chr & 0xff, textboxText.lines_1e - 1);

        case TextboxText84.SAUTO -> {
          textboxText.state_00 = TextboxTextState._13;

          final int v0 = 60 / vsyncMode_8007a3b8 * (chr & 0xff);
          textboxText._3e = v0;
          textboxText._40 = v0;

          if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
            textboxText.charIndex_30++;
          }

          parseMore = false;
        }

        case TextboxText84.ELEMENT -> textboxText.element_7c = chr & 0xff;

        case TextboxText84.ARROW -> {
          if((chr & 0x1) != 0) {
            textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
          } else {
            //LAB_80027bd0
            textboxText.flags_08 ^= TextboxText84.SHOW_ARROW;
          }
        }

        default -> {
          if((chr >>> 8) != TextboxText84.ACTION) {
            //LAB_80027be4
            appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, chr);
          } else {
            final StringBuilder builder = new StringBuilder();
            final int length = str.charAt(textboxText.charIndex_30) & 0xff;

            for(int n = 0; n < length; n++) {
              builder.append((char)(str.charAt(textboxText.charIndex_30 + 1 + n / 2) >>> (n & 0x1) * 8 & 0xff));
            }

            textboxText.charIndex_30 += (length + 1) / 2;

            final RegistryId id = new RegistryId(builder.toString());
            final InputAction action = REGISTRIES.inputActions.getEntry(id).get();

            appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, InputCodepoints.TEXTBOX_INPUT_ACTION | textboxText.inputActions.size());
            textboxText.inputActions.add(action);
          }

          textboxText.charX_34++;

          if(textboxText.charX_34 < textboxText.chars_1c) {
            //LAB_80027d28
            textboxText.state_00 = TextboxTextState._7;
          } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
            final int control = str.charAt(textboxText.charIndex_30 + 1) >>> 8;

            if(control == TextboxText84.END) {
              //LAB_80027c7c
              textboxText.state_00 = TextboxTextState._15;
              setTextboxArrowPosition(textboxIndex, true);
            } else {
              if(control == TextboxText84.LINE) {
                //LAB_80027c98
                textboxText.charIndex_30++;
              }

              //LAB_80027c9c
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
              textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;
              textboxText.charX_34 = 0;
              textboxText.charY_36++;

              if((textboxText.flags_08 & 0x1) == 0) {
                setTextboxArrowPosition(textboxIndex, true);
              }
            }
          } else {
            //LAB_80027ce0
            textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;
            textboxText.charX_34 = 0;
            textboxText.charY_36++;

            if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
              textboxText.charIndex_30++;
            }

            //LAB_80027d28
            textboxText.state_00 = TextboxTextState._7;
          }

          //LAB_80027d2c
          parseMore = false;
        }
      }

      //LAB_80027d30
      textboxText.charIndex_30++;
    } while(parseMore);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void calculateAppropriateTextboxBounds(final int textboxIndex, final float x, final float y) {
    final int maxX;
    if(currentEngineState_8004dd04.is(LodEngineStateTypes.SUBMAP.get())) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80027d9c
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int width = textboxText.chars_1c * 9 / 2;
    final int height = textboxText.lines_1e * 6;

    float newX = x;
    if(x - width < 10) {
      newX = width + 10;
    }

    //LAB_80027dfc
    if(x + width > maxX) {
      newX = maxX - width;
    }

    //LAB_80027e14
    float newY = y;
    if(y - height < 18) {
      newY = height + 18;
    }

    //LAB_80027e28
    if(222 < y + height) {
      newY = 222 - height;
    }

    //LAB_80027e40
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox.x_14 = newX;
    textbox.y_16 = newY;
    textboxText.x_14 = newX;
    textboxText.y_16 = newY;
    textboxText._18 = newX - width;
    textboxText._1a = newY - height;
  }

  @Method(0x80027eb4L)
  public static void advanceTextbox(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_80027efc
    for(int lineIndex = (textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 ? 1 : 0; lineIndex < textboxText.lines_1e; lineIndex++) {
      //LAB_80027f18
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        final TextboxChar08 nextLine = textboxText.chars_58[(lineIndex + 1) * textboxText.chars_1c + charIndex];
        currentLine.set(nextLine);
        currentLine.y_02--;
      }
    }

    //LAB_8002804c
    //LAB_80028098
    for(int i = textboxText.chars_1c * textboxText.lines_1e; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      textboxText.chars_58[i].clear();
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void rewindTextbox(final int textboxIndex) {
    assert false : "This won't delete Objs from the removed line";

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002810c
    for(int lineIndex = textboxText.lines_1e; lineIndex > 0; lineIndex--) {
      //LAB_80028128
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 previousLine = textboxText.chars_58[(lineIndex - 1) * textboxText.chars_1c + charIndex];
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        currentLine.set(previousLine);
        currentLine.y_02++;
      }
    }

    //LAB_80028254
    //LAB_80028280
    for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
      textboxText.chars_58[charIndex].clear();
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void renderTextboxText(final TextboxText84 textboxText) {
    final int firstCharInLineIndex;
    final int lastCharInLineIndex;
    if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0) {
      firstCharInLineIndex = textboxText.chars_1c;
      lastCharInLineIndex = textboxText.chars_1c * 2;
    } else {
      firstCharInLineIndex = 0;
      lastCharInLineIndex = textboxText.chars_1c;
    }

    int nudgeX = 0;

    //LAB_80028328
    //LAB_80028348
    for(int i = 0; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      final TextboxChar08 chr = textboxText.chars_58[i];

      if(chr.x_00 == 0) {
        nudgeX = 0;
      }

      String str = String.valueOf(InputCodepoints.getCodepoint(PLATFORM.getGamepadType(), LodString.fromLodChar(chr.char_06)));

      if((str.charAt(0) & 0xff00) == InputCodepoints.TEXTBOX_INPUT_ACTION) {
        str = InputCodepoints.getActionName(textboxText.inputActions.get(str.charAt(0) & 0xff));
      }

      //LAB_8002835c
      for(int charIndex = 0; charIndex < str.length(); charIndex++) {
        final char c = str.charAt(charIndex);

        if(chr.char_06 != 0) {
          int scrollY = 0;
          int scrollH = 0;
          if((textboxText.flags_08 & 0x1) != 0) {
            if(i >= firstCharInLineIndex && i < lastCharInLineIndex) {
              final int scroll = org.joml.Math.round(textboxText.scrollAmount_2c);
              scrollY = -scroll;
              scrollH = scroll;
            }

            //LAB_800283c4
            if(i >= textboxText.chars_1c * textboxText.lines_1e && i < textboxText.chars_1c * (textboxText.lines_1e + 1)) {
              scrollY = 0;
              scrollH = 12 - Math.round(textboxText.scrollAmount_2c);
            }
          }

          //LAB_8002840c
          if(scrollH < 13) {
            final int x = (int)(textboxText._18 - centreScreenX_1f8003dc + nudgeX);
            final int y;

            // I adjusted the texture so that glyphs start 1 pixel lower to fix bleeding - subtract 1 here to compensate
            if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && i < textboxText.chars_1c) {
              y = (int)(textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de - scrollY) - 1;
            } else {
              y = (int)(textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de - scrollY - textboxText.scrollAmount_2c) - 1;
            }

            //LAB_80028544
            //LAB_80028564
            final int height = 12 - scrollH;

            textboxText.transforms.identity();
            textboxText.transforms.transfer.set(GPU.getOffsetX() + x + 1, GPU.getOffsetY() + y - scrollH + 1, (textboxText.z_0c + 1) * 4.0f);
            DEFAULT_FONT.queueChar(c, textboxText.transforms)
              .monochrome(0.0f)
              .scissor(GPU.getOffsetX() + x, GPU.getOffsetY() + y + 2, DEFAULT_FONT.charWidth(c) + 1, height)
              .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());

            textboxText.transforms.transfer.x--;
            textboxText.transforms.transfer.y--;
            textboxText.transforms.transfer.z -= 4.0f;

            DEFAULT_FONT.queueChar(c, textboxText.transforms)
              .colour(chr.colour_04.r / 255.0f, chr.colour_04.g / 255.0f, chr.colour_04.b / 255.0f)
              .scissor(GPU.getOffsetX() + x, GPU.getOffsetY() + y + 1, DEFAULT_FONT.charWidth(c) + 1, height)
              .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());
          }
        }

        nudgeX += DEFAULT_FONT.charWidth(c) + 1;
      }
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void scrollTextboxDown(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    textboxText.scrollAmount_2c += textboxText.scrollSpeed_2a;

    if(textboxText.scrollAmount_2c >= 12.0f) {
      advanceTextbox(textboxIndex);
      textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
      textboxText.scrollAmount_2c = 0.0f;
      textboxText.charY_36--;
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void scrollTextboxUp(final int textboxIndex) {
    if((tickCount_800bb0fc & 0x1) == 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textboxText.scrollAmount_2c += textboxText.scrollSpeed_2a;

      if(textboxText.scrollAmount_2c >= 12.0f) {
        advanceTextbox(textboxIndex);
        textboxText.flags_08 |= 0x4;
        textboxText.scrollAmount_2c -= 12.0f;
        textboxText.charY_36 = textboxText.lines_1e;
      }
    }

    //LAB_80028928
  }

  @Method(0x80028f20L)
  public static boolean textboxFits(final int textboxIndex, final float x, final float y) {
    final int maxX;
    if(currentEngineState_8004dd04.is(LodEngineStateTypes.SUBMAP.get())) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80028f40
    //LAB_80028fa8
    //LAB_80028fc0
    //LAB_80028fd4
    return
      x - textboxes_800be358[textboxIndex].chars_18 * 4.5f >= 10 &&
      x + textboxes_800be358[textboxIndex].chars_18 * 4.5f <= maxX &&
      y - textboxes_800be358[textboxIndex].lines_1a * 6.0f >= 18 &&
      y + textboxes_800be358[textboxIndex].lines_1a * 6.0f <= 222;

    //LAB_80028ff0
  }

  @ScriptDescription("Unknown, sets textbox 0 to hardcoded values (garbage text? unused?")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "ret", description = "Always set to 0")
  @Method(0x80028ff8L)
  public static FlowControl FUN_80028ff8(final RunningScript<?> script) {
    clearTextbox(0);

    final Textbox4c textbox = textboxes_800be358[0];
    textbox.backgroundType_04 = BackgroundType.fromInt(textboxMode_80052b88[2]);
    textbox.x_14 = 260;
    textbox.y_16 = 120;
    textbox.chars_18 = 6;
    textbox.lines_1a = 8;
    clearTextboxText(0);

    final TextboxText84 textboxText = textboxText_800bdf38[0];
    textboxText.type_04 = textboxTextType_80052ba8[1];
    textboxText.str_24 = new LodString("Garbage text"); // _80052c20
    textboxText.flags_08 |= 0x40;

    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());

    //LAB_80029100
    calculateAppropriateTextboxBounds(0, textboxText.x_14, textboxText.y_16);
    script.params_20[0].set(0);
    return FlowControl.CONTINUE;
  }

  /** The purple bar used in inn dialogs, etc. */
  @Method(0x80029140L)
  public static void renderTextboxSelection(final Textbox4c textbox, final int selectionLine) {
    final int width = (textbox.chars_18 - 1) * 9;
    final float x = textbox.x_14;
    final float y = textbox.y_16 + selectionLine * 12 - (textbox.lines_1a - 1) * 6;

    textboxSelectionTransforms.scaling(width, 1.0f, 1.0f);
    textboxSelectionTransforms.transfer.set(x - width / 2.0f, y, textbox.z_0c * 4.0f);
    RENDERER.queueOrthoModel(textboxSelectionObj, textboxSelectionTransforms, QueuedModelStandard.class);
  }

  private static final MV textTransforms = new MV();

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options) {
    renderText(text, originX, originY, options, null);
  }

  public interface QueueCallback {
    void run(final QueuedModelStandard model, final boolean shadow);
  }

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options, @Nullable final QueueCallback queueCallback) {
    renderText(GameEngine.DEFAULT_FONT, text, originX, originY, options, queueCallback);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options) {
    renderText(font, text, originX, originY, options, null);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options, @Nullable final QueueCallback queueCallback) {
    font.init();

    final float height = 12.0f * options.getSize();
    final float trim = java.lang.Math.clamp(options.getTrim() * options.getSize(), -height, height);

    textTransforms.scaling(options.getSize());

    for(int i = 0; i < (options.hasShadow() ? 4 : 1); i++) {
      float x = switch(options.getHorizontalAlign()) {
        case LEFT -> originX;
        case CENTRE -> originX - font.lineWidth(text) * options.getSize() / 2.0f;
        case RIGHT -> originX - font.lineWidth(text) * options.getSize();
      };

      // I adjusted the texture so that glyphs start 1 pixel lower to fix bleeding - subtract 1 here to compensate
      float y = originY - 1;
      float glyphNudge = 0.0f;

      for(int charIndex = 0; charIndex < text.length(); charIndex++) {
        final char c = text.charAt(charIndex);

        if(c != ' ') {
          if(c == '\n') {
            x = switch(options.getHorizontalAlign()) {
              case LEFT -> originX;
              case CENTRE -> originX - font.lineWidth(text, charIndex + 1) * options.getSize() / 2.0f;
              case RIGHT -> originX - font.lineWidth(text, charIndex + 1) * options.getSize();
            };

            glyphNudge = 0.0f;
            y += height;
          } else {
            final float offsetX = (i & 1) * options.getSize();
            final float offsetY = (i >>> 1) * options.getSize();

            textTransforms.transfer.set(x + glyphNudge + offsetX, y + offsetY, textZ_800bdf00 * 4.0f + i * 0.1f);

            if(trim < 0) {
              textTransforms.transfer.y += trim;
            }

            if(i == 0 || font.usesColour(c)) {
              final QueuedModelStandard model = font.queueChar(InputCodepoints.getCodepoint(PLATFORM.getGamepadType(), c), textTransforms);

              if(font.usesColour(c)) {
                if(i == 0) {
                  model.colour(options.getRed(), options.getGreen(), options.getBlue());
                } else if(font.usesColour(c)) {
                  model.colour(options.getShadowRed(), options.getShadowGreen(), options.getShadowBlue());
                }
              }

              if(trim != 0) {
                if(trim < 0) {
                  model.scissor(0, (int)y + 1, displayWidth_1f8003e0, (int)(height + trim));
                } else {
                  model.scissor(0, (int)(y + 1 - trim), displayWidth_1f8003e0, (int)height);
                }
              }

              if(queueCallback != null) {
                queueCallback.run(model, i != 0);
              }
            }
          }
        }

        if(c != '\n') {
          glyphNudge += font.charWidth(c) * options.getSize();
        }
      }
    }
  }

  @Method(0x80029920L)
  public static void setTextboxArrowPosition(final int textboxIndex, final boolean visible) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if(visible) {
      //LAB_80029948
      arrow.flags_00 |= TextboxArrow0c.ARROW_VISIBLE;
    } else {
      arrow.flags_00 = 0;
    }

    //LAB_80029970
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    arrow.x_04 = textbox.x_14;
    arrow.y_06 = textbox.y_16 + textbox.lines_1a * 6;
    arrow.spriteIndex_08 = 0;
  }

  @Method(0x800299d4L)
  public static void renderTextboxArrow(final TextboxText84 textboxText, final TextboxArrow0c arrow) {
    if((arrow.flags_00 & TextboxArrow0c.ARROW_VISIBLE) != 0) {
      if((textboxText.flags_08 & TextboxText84.SHOW_ARROW) != 0) {
        textboxArrowTransforms.scaling(1.0f, 0.875f, 1.0f);
        textboxArrowTransforms.transfer.set(arrow.x_04, arrow.y_06,  textboxText.z_0c * 4.0f);
        RENDERER.queueOrthoModel(textboxArrowObjs[arrow.spriteIndex_08], textboxArrowTransforms, QueuedModelStandard.class)
          .worldScissor().set(0, 0, RENDERER.getRenderWidth(), RENDERER.getRenderHeight());
      }
    }

    //LAB_80029b50
  }

  @ScriptDescription("Gets the first free textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "textboxIndex", description = "Textbox index, or -1 if none are free")
  @Method(0x80029b68L)
  public static FlowControl scriptGetFreeTextboxIndex(final RunningScript<?> script) {
    //LAB_80029b7c
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i].state_00 == TextboxState.UNINITIALIZED_0 && textboxText_800bdf38[i].state_00 == TextboxTextState.UNINITIALIZED_0) {
        script.params_20[0].set(i);
        return FlowControl.CONTINUE;
      }

      //LAB_80029bac
      //LAB_80029bb0
    }

    script.params_20[0].set(-1);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialize a textbox")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textboxIndex", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @Method(0x80029bd4L)
  public static FlowControl scriptInitTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    clearTextbox(textboxIndex);

    final Textbox4c struct4c = textboxes_800be358[textboxIndex];
    struct4c.backgroundType_04 = BackgroundType.fromInt(script.params_20[1].get());
    struct4c.x_14 = script.params_20[2].get();
    struct4c.y_16 = script.params_20[3].get();
    struct4c.chars_18 = script.params_20[4].get() + 1;
    struct4c.lines_1a = script.params_20[5].get() + 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns whether or not a textbox is initialized")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "initialized", description = "0 for false, non-zero for true")
  @Method(0x80029c98L)
  public static FlowControl scriptIsTextboxInitialized(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    script.params_20[1].set(textboxes_800be358[textboxIndex].state_00.ordinal() | textboxText_800bdf38[textboxIndex].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the textbox state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "state", description = "The textbox state")
  @Method(0x80029cf4L)
  public static FlowControl scriptGetTextboxState(final RunningScript<?> script) {
    script.params_20[1].set(textboxes_800be358[script.params_20[0].get()].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the textbox text state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "state", description = "The textbox text state")
  @Method(0x80029d34L)
  public static FlowControl scriptGetTextboxTextState(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a textbox")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029d6cL)
  public static FlowControl scriptDeallocateTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    if(textboxText.state_00 != TextboxTextState.UNINITIALIZED_0) {
      textboxText.delete();
    }

    //LAB_80029db8
    textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
    textbox.state_00 = TextboxState.UNINITIALIZED_0;

    setTextboxArrowPosition(textboxIndex, false);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates all textboxes")
  @Method(0x80029e04L)
  public static FlowControl scriptDeallocateAllTextboxes(final RunningScript<?> script) {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Textbox4c textbox = textboxes_800be358[i];
      final TextboxText84 textboxText = textboxText_800bdf38[i];

      if(textboxText.state_00 != TextboxTextState.UNINITIALIZED_0) {
        textboxText.delete();
      }

      //LAB_80029e48
      textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
      textbox.state_00 = TextboxState.UNINITIALIZED_0;

      setTextboxArrowPosition(i, false);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a variable used in a textbox's text (<var:n>)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The variable index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029e8cL)
  public static FlowControl scriptSetTextboxVariable(final RunningScript<?> script) {
    textboxVariables_800bdf10[Math.min(9, script.params_20[0].get())] = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029eccL)
  public static FlowControl FUN_80029ecc(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];
    if(textboxText.state_00 == TextboxTextState._16 && (textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
      textboxText.flags_08 ^= TextboxText84.NO_INPUT;
    }

    //LAB_80029f18
    //LAB_80029f1c
    textboxText.flags_08 |= 0x40;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the index of the currently-selected textbox line (i.e. do you want to stay at this inn? yes/no)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectionIndex", description = "The selected line index")
  @Method(0x80029f48L)
  public static FlowControl scriptGetTextboxSelectionIndex(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].selectionIndex_6c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Return's a textbox's element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029f80L)
  public static FlowControl scriptGetTextboxElement(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].element_7c);
    return FlowControl.CONTINUE;
  }

  @Method(0x8002a058L)
  public static void handleTextboxAndText() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i].state_00 != TextboxState.UNINITIALIZED_0) {
        handleTextbox(i);
      }

      //LAB_8002a098
      if(textboxText_800bdf38[i].state_00 != TextboxTextState.UNINITIALIZED_0) {
        handleTextboxText(i); // Animates the textbox arrow
      }
    }
  }

  @Method(0x8002a0e4L)
  public static void renderTextboxes() {
    for(int i = 0; i < 8; i++) {
      //LAB_8002a10c
      final Textbox4c textbox = textboxes_800be358[i];
      if(textbox.state_00 != TextboxState.UNINITIALIZED_0 && (textbox.flags_08 & Textbox4c.RENDER_BACKGROUND) != 0) {
        renderTextboxBackground(textbox);
      }

      if(!currentEngineState_8004dd04.renderTextOnTopOfAllBoxes()) {
        renderTextboxOverlays(textboxes_800be358[i], textboxText_800bdf38[i], textboxArrows_800bdea0[i]);
      }
    }

    if(currentEngineState_8004dd04.renderTextOnTopOfAllBoxes()) {
      for(int i = 0; i < 8; i++) {
        renderTextboxOverlays(textboxes_800be358[i], textboxText_800bdf38[i], textboxArrows_800bdea0[i]);
      }
    }
  }

  private static void renderTextboxOverlays(final Textbox4c textbox, final TextboxText84 text, final TextboxArrow0c arrow) {
    if(text.state_00 != TextboxTextState.UNINITIALIZED_0) {
      switch(text.state_00) {
        case _18 -> renderTextboxSelection(textbox, text.selectionLine_60);
        case _19, SELECTION_22 -> renderTextboxSelection(textbox, text.selectionLine_68);
      }

      renderTextboxText(text);
      renderTextboxArrow(text, arrow);
    }
  }

  @Method(0x8002a180L)
  public static void appendTextboxChar(final int textboxIndex, final int charX, final int charY, TextColour colour, final int lodChar) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int charIndex = textboxText.charY_36 * textboxText.chars_1c + textboxText.charX_34;
    final TextboxChar08 chr = textboxText.chars_58[charIndex];
    chr.x_00 = charX;
    chr.y_02 = charY;

    if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && charY == 0) {
      colour = TextColour.YELLOW;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    chr.colour_04 = colour;

    // Hellena Prison has a retail bug (textbox name says Warden?iate)
    if(lodChar == 0x900 || lodChar == -1) {
      chr.char_06 = 0;
    } else {
      chr.char_06 = lodChar;
    }
  }

  @Method(0x8002a2b4L)
  public static void clearTextboxChars(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002a2f0
    for(int charIndex = 0; charIndex < textboxText.chars_1c * (textboxText.lines_1e + 1); charIndex++) {
      textboxText.chars_58[charIndex].clear();
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void initTextbox(final Textbox4c textbox, final boolean animateInOut, final float x, final float y, final int chars, final int lines) {
    textbox.backgroundType_04 = animateInOut ? BackgroundType.ANIMATE_IN_OUT : BackgroundType.NORMAL;
    textbox.renderBorder_06 = true;
    textbox.flags_08 |= Textbox4c.NO_ANIMATE_OUT;

    textbox.x_14 = x;
    textbox.y_16 = y;
    textbox.chars_18 = chars + 1;
    textbox.lines_1a = lines + 1;
  }

  @Method(0x8002a3ecL)
  public static void setTextAndTextboxesToUninitialized(final int textboxIndex, final int mode) {
    if(mode == 0) {
      //LAB_8002a40c
      textboxText_800bdf38[textboxIndex].state_00 = TextboxTextState.UNINITIALIZED_0;
      textboxes_800be358[textboxIndex].state_00 = TextboxState.UNINITIALIZED_0;
    } else {
      //LAB_8002a458
      textboxes_800be358[textboxIndex].state_00 = TextboxState.ANIMATE_OUT_3;
    }
  }

  /** Too bad I don't know what state 6 is */
  @Method(0x8002a488L)
  public static boolean isTextboxInState6(final int textboxIndex) {
    return textboxes_800be358[textboxIndex].state_00 == TextboxState._6;
  }

  @Method(0x8002a4c4L)
  public static void updateTextboxArrowSprite(final int textboxIndex) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if((arrow.flags_00 & TextboxArrow0c.ARROW_VISIBLE) != 0) {
      if((textboxText_800bdf38[textboxIndex].flags_08 & TextboxText84.SHOW_ARROW) != 0) {
        if(tickCount_800bb0fc % (2 * currentEngineState_8004dd04.tickMultiplier()) == 0 && !CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
          arrow.spriteIndex_08++;
        }

        //LAB_8002a53c
        if(arrow.spriteIndex_08 > 6) {
          arrow.spriteIndex_08 = 0;
        }
      }
    }

    //LAB_8002a554
  }
}
