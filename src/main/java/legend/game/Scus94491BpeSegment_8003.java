package legend.game;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommandFillVram;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.DR_TPAGE;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOffsetType;
import legend.game.types.GsRVIEW2;
import legend.game.types.Translucency;
import legend.game.types.WeirdTimHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.MathHelper.clamp;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8002.SetBackColor;
import static legend.game.Scus94491BpeSegment_8002.SetColorMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetLightMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8004.Lzc;
import static legend.game.Scus94491BpeSegment_8005.DISPENV_80054728;
import static legend.game.Scus94491BpeSegment_8005.DRAWENV_800546cc;
import static legend.game.Scus94491BpeSegment_8005.GsOUT_PACKET_P;
import static legend.game.Scus94491BpeSegment_8005._80054674;
import static legend.game.Scus94491BpeSegment_8005._800546bc;
import static legend.game.Scus94491BpeSegment_8005._800546bd;
import static legend.game.Scus94491BpeSegment_8005._800546c0;
import static legend.game.Scus94491BpeSegment_8005._800546c2;
import static legend.game.Scus94491BpeSegment_8005.array_8005473c;
import static legend.game.Scus94491BpeSegment_8005.array_80054748;
import static legend.game.Scus94491BpeSegment_8005.gpu_debug;
import static legend.game.Scus94491BpeSegment_8005.matrixStackIndex_80054a08;
import static legend.game.Scus94491BpeSegment_8005.matrixStack_80054a0c;
import static legend.game.Scus94491BpeSegment_8005.sin_cos_80054d0c;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.DRAWENV_800c3450;
import static legend.game.Scus94491BpeSegment_800c.PSDCNT_800c34d0;
import static legend.game.Scus94491BpeSegment_800c.PSDIDX_800c34d4;
import static legend.game.Scus94491BpeSegment_800c._800c3410;
import static legend.game.Scus94491BpeSegment_800c._800c34c4;
import static legend.game.Scus94491BpeSegment_800c._800c34c6;
import static legend.game.Scus94491BpeSegment_800c._800c34d8;
import static legend.game.Scus94491BpeSegment_800c._800c34e0;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3440;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3448;
import static legend.game.Scus94491BpeSegment_800c.coord2s_800c35a8;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.doubleBufferOffsetMode_800c34d6;
import static legend.game.Scus94491BpeSegment_800c.identityAspectMatrix_800c3588;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.lightMode_800c34dc;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3528;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public final class Scus94491BpeSegment_8003 {
  private Scus94491BpeSegment_8003() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8003.class);

  @Method(0x800309f0L)
  public static void bzero(final long address, final int size) {
    MEMORY.memfill(address, size, 0);
  }

  @Method(0x80035630L)
  public static void CdMix(final int cdLeftToSpuLeft, final int cdLeftToSpuRight, final int cdRightToSpuRight, final int cdRightToSpuLeft) {
    LOGGER.warn("SET CD MIX %d %d %d %d", cdLeftToSpuLeft, cdLeftToSpuRight, cdRightToSpuRight, cdRightToSpuLeft);
  }

  /**
   * Initialize drawing engine.
   *
   * Resets the graphic system according to mode
   *
   * 0 - Complete reset. The drawing environment and display environment are initialized.
   * 1 - Cancels the current drawing and flushes the command buffer.
   * 3 - Initializes the drawing engine while preserving the current display environment
   * 5 - ?
   *
   * Setting bit 4 seems to allow disabling textures (for debugging?)
   *
   * (i.e. the screen is not cleared or the screen mode changed).
   */
  @Method(0x80038190L)
  public static long ResetGraph(final long mode) {
    //LAB_800381dc
    LOGGER.trace("ResetGraph:jtb=%08x,env=%08x", _80054674.getAddress(), _800546bc.getAddress());

    //LAB_800381f8
    MEMORY.memfill(_800546bc.getAddress(), 0x80, 0);

    _800546bc.setu(FUN_8003a798(mode));
    _800546bd.setu(0x1L);
    _800546c0.setu(array_8005473c.offset(_800546bc.get() * 4));
    _800546c2.setu(array_80054748.offset(_800546bc.get() * 4));

    MEMORY.memfill(DRAWENV_800546cc.getAddress(), 0x5c, 0xff);
    MEMORY.memfill(DISPENV_80054728.getAddress(), 0x14, 0xff);

    return _800546bc.get();
  }

  /**
   * Set debugging level.
   *
   * Sets a debugging level for the graphics system. level can be one of the following:
   * Table 7-6
   * Level Operation
   * 0 No checks are performed. (Highest speed
   * mode)
   * 1 Checks coordinating registered and drawn
   * primitives.
   * 2 Registered and drawn primitives are
   * dumped.
   *
   * @param level Debugging level
   *
   * @return The previously set debug level.
   */
  @Method(0x80038304L)
  public static long SetGraphDebug(final int level) {
    final long old = gpu_debug.get();
    gpu_debug.setu(level);

    if(level != 0) {
      LOGGER.info("SetGraphDebug:level:%d,type:%d", gpu_debug.get(), _800546bc.get());
    }

    return old;
  }

  @Method(0x80038574L)
  private static void validateRect(final String text, final RECT rect) {
    if(rect.w.get() > _800546c0.get() || rect.x.get() + rect.w.get() > _800546c0.get() || rect.y.get() > _800546c2.get() || rect.y.get() + rect.h.get() > _800546c2.get() || rect.w.get() < 0 || rect.x.get() < 0 || rect.y.get() < 0 || rect.h.get() < 0) {
      LOGGER.warn("%s:bad RECT", text);
      LOGGER.warn("(%d,%d)-(%d,%d)", rect.x.get(), rect.y.get(), rect.w.get(), rect.h.get());
    }
  }

  @Method(0x80038690L)
  public static int ClearImage(final RECT rect, final byte r, final byte g, final byte b) {
    validateRect("ClearImage", rect);

    return (int)ClearImage_Impl(rect, (b & 0xffL) << 16 | (g & 0xffL) << 8 | r & 0xffL);
  }

  @Method(0x800387b8L)
  public static void LoadImage(final RECT rect, final long address) {
    validateRect("LoadImage", rect);

    GPU.commandA0CopyRectFromCpuToVram(rect, address);
  }

  @Method(0x80038818L)
  public static long StoreImage(final RECT rect, final long address) {
    validateRect("StoreImage", rect);

    rect.w.set(MathHelper.clamp(rect.w.get(), (short)0, (short)_800546c0.get()));
    rect.h.set(MathHelper.clamp(rect.h.get(), (short)0, (short)_800546c2.get()));

    if(rect.w.get() <= 0 || rect.h.get() <= 0) {
      throw new IllegalArgumentException("RECT width and height must be greater than 0");
    }

    GPU.commandC0CopyRectFromVramToCpu(rect, address);

    return 0;
  }

  @Method(0x80038878L)
  public static void MoveImage(final RECT rect, final int x, final int y) {
    validateRect("MoveImage", rect);

    if(rect.w.get() <= 0 || rect.h.get() <= 0) {
      throw new IllegalArgumentException("RECT width and height must be greater than 0");
    }

    //LAB_800388d0
    //LAB_80038918
    GPU.command80CopyRectFromVramToVram(rect.x.get(), rect.y.get(), x, y, rect.w.get(), rect.h.get());
  }

  /**
   * Set the drawing environment.
   *
   * @param env Pointer to drawing environment start address
   *
   * The basic drawing parameters (such as the drawing offset and the drawing clip area) are set according to
   * the values specified in env.
   *
   * The drawing environment is effective until the next time PutDrawEnv() is executed, or until the DR_ENV
   * primitive is executed.
   *
   * @return A pointer to the drawing environment set. On failure, returns 0.
   */
  @Method(0x80038b70L)
  public static DRAWENV PutDrawEnv(final DRAWENV env) {
    if(gpu_debug.get() > 1) {
      LOGGER.info("PutDrawEnv(%08x)...", env.getAddress());
    }

    GPU.drawingArea(env.clip.x.get(), env.clip.y.get(), env.clip.x.get() + env.clip.w.get(), env.clip.y.get() + env.clip.h.get());
    GPU.drawingOffset(env.ofs.get(0).get(), env.ofs.get(1).get());
    GPU.maskBit(false, Gpu.DRAW_PIXELS.ALWAYS);

    DRAWENV_800546cc.set(env);
    return env;
  }

  /**
   * Sets a display environment according to information specified by env.
   *
   * @param env Pointer to display environment start address
   *
   * @return A pointer to the display environment set; on failure, returns 0.
   */
  @Method(0x80038d3cL)
  public static DISPENV PutDispEnv(final DISPENV env) {
    if(gpu_debug.get() > 1) {
      LOGGER.info("PutDispEnv(%s)...", env);
    }

    //LAB_80038d8c
    GPU.displayStart(env.disp.x.get(), env.disp.y.get());

    long s0 = 0;

    if(DISPENV_80054728.disp.x.get() != env.disp.x.get() || DISPENV_80054728.disp.y.get() != env.disp.y.get() || DISPENV_80054728.disp.w.get() != env.disp.w.get() || DISPENV_80054728.disp.h.get() != env.disp.h.get()) {
      //LAB_80038e34
      //LAB_80038e94
      final int width = env.disp.w.get();

      final Gpu.HORIZONTAL_RESOLUTION hRes;
      final Gpu.VERTICAL_RESOLUTION vRes;

      if(width <= 280) {
        hRes = Gpu.HORIZONTAL_RESOLUTION._256;
      } else if(width <= 352) {
        hRes = Gpu.HORIZONTAL_RESOLUTION._320;
      } else if(width <= 400) {
        hRes = Gpu.HORIZONTAL_RESOLUTION._368;
      } else if(width <= 560) {
        hRes = Gpu.HORIZONTAL_RESOLUTION._512;
      } else {
        hRes = Gpu.HORIZONTAL_RESOLUTION._640;
      }

      //LAB_80038edc
      //LAB_80038ef0
      if(env.disp.h.get() <= 256) {
        vRes = Gpu.VERTICAL_RESOLUTION._240;
      } else {
        vRes = Gpu.VERTICAL_RESOLUTION._480;
      }

      //LAB_80038efc
      GPU.displayMode(hRes, vRes, Gpu.DISPLAY_AREA_COLOUR_DEPTH.BITS_15);

      env.pad0.set((byte)8);
    }

    //LAB_80038f20
    if(DISPENV_80054728.screen.x.get() != env.screen.x.get() || DISPENV_80054728.screen.y.get() != env.screen.y.get() || DISPENV_80054728.screen.w.get() != env.screen.w.get() || DISPENV_80054728.screen.h.get() != env.screen.h.get() || env.pad0.get() == 0x8L) {
      //LAB_80038f98
      env.pad0.set((byte)0);

      s0 = env.screen.y.get() + 16;

      //LAB_80038fb8
      final long s2;

      if(env.screen.h.get() == 0) {
        s2 = s0 + 0xf0L;
      } else {
        s2 = s0 + env.screen.h.get();
      }

      long a1;
      //LAB_80039164
      if(s0 < 16L) {
        //LAB_80039180
        a1 = 16L;
      } else if(s0 <= 257L) {
        a1 = s0;
      } else {
        a1 = 257L;
      }

      //LAB_80039184
      s0 = a1;

      //LAB_8003919c
      if(s2 < a1) {
        a1 += 2L;
      } else if(s2 <= 258L) {
        a1 = s2;
      } else {
        a1 = 258L;
      }

      //LAB_800391a8
      GPU.verticalDisplayRange((int)s0, (int)a1);
    }

    memcpy(DISPENV_80054728.getAddress(), env.getAddress(), 0x14);

    //LAB_80039204
    return env;
  }

  @Method(0x80039aa4L)
  public static long ClearImage_Impl(final RECT rect, final long colour) {
    rect.w.set(clamp(rect.w.get(), (short)0, (short)(_800546c0.get() - 1)));
    rect.h.set(clamp(rect.h.get(), (short)0, (short)(_800546c2.get() - 1)));

    LOGGER.info("Clearing screen %s %08x", rect, colour);

    GPU.queueCommand(orderingTableSize_1f8003c8.get() - 1, new GpuCommandFillVram(rect.x.get(), rect.y.get(), rect.w.get(), rect.h.get(), (int)colour));
    return 0;
  }

  @Method(0x8003a798L)
  public static long FUN_8003a798(final long mode) {
    if(mode == 1 || mode == 3) {
      //LAB_8003a854
      GPU.resetCommandBuffer();
    } else if(mode == 0 || mode == 5) {
      //LAB_8003a808
      GPU.reset();
    }

    //LAB_8003a8a0
    if(mode != 0) {
      return 0;
    }

    //LAB_8003a8c4
    return 1;
  }

  @Method(0x8003b0b4L)
  public static long GsGetWorkBase() {
    return GsOUT_PACKET_P.get();
  }

  /**
   * <p>Calculates the texture page ID, and returns it.</p>
   *
   * <p>The semitransparent rate is also effective for polygons on which texture mapping is not performed.
   * The texture page address is limited to a multiple of 64 in the X direction and a multiple of 256 in the Y
   * direction.</p>
   */
  @Method(0x8003b3f0L)
  public static int GetTPage(final Bpp bpp, final Translucency trans, final int x, final int y) {
    return
      (bpp.ordinal() << 7 |
      trans.ordinal() << 5 |
      (y & 0x200) << 2 |
      (y & 0x100) >> 4 |
      (x & 0x3ff) >> 6) & 0xffff;
  }

  /**
   * <p>Calculates and returns the texture CLUT ID.</p>
   *
   * <p>The CLUT address is limited to multiples of 16 in the x direction.</p>
   *
   * @param x X
   * @param y Y
   *
   * @return CLUT ID.
   */
  @Method(0x8003b430L)
  public static int GetClut(final int x, final int y) {
    return (y << 6 | x >> 4 & 0x3f) & 0xffff;
  }

  @Method(0x8003b490L)
  public static void gpuLinkedListSetCommandTransparency(final long entryAddress, final boolean transparency) {
    if(transparency) {
      MEMORY.ref(1, entryAddress).offset(0x7L).oru(0x2L);
    } else {
      MEMORY.ref(1, entryAddress).offset(0x7L).and(0xfdL);
    }
  }

  @Method(0x8003b4c0L)
  public static void gpuLinkedListSetCommandTextureUnshaded(final long entryAddress, final boolean unshaded) {
    if(unshaded) {
      MEMORY.ref(1, entryAddress).offset(0x7L).oru(0x1L);
    } else {
      MEMORY.ref(1, entryAddress).offset(0x7L).and(0xfeL);
    }
  }

  @Method(0x8003b4f0L)
  public static void setGp0_20(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x20L);
  }

  @Method(0x8003b510L)
  public static void setGp0_24(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x7L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x24L);
  }

  @Method(0x8003b530L)
  public static void setGp0_30(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x6L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x30L);
  }

  @Method(0x8003b550L)
  public static void setGp0_34(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x9L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x34L);
  }

  @Method(0x8003b570L)
  public static void setGp0_28(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x5L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x28L);
  }

  @Method(0x8003b590L)
  public static void setGp0_2c(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x9L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x2cL);
  }

  @Method(0x8003b5b0L)
  public static void setGp0_38(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x8L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x38L);
  }

  @Method(0x8003b5d0L)
  public static void setGp0_3c(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0xcL);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x3cL);
  }

  @Method(0x8003b5f0L)
  public static void setGp0_74(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x3L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x74L);
  }

  @Method(0x8003b610L)
  public static void setGp0_7c(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x3L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x7cL);
  }

  @Method(0x8003b630L)
  public static void setGp0_64(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x64L);
  }

  @Method(0x8003b650L)
  public static void setGp0_60(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x3L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x60L);
  }

  @Method(0x8003b670L)
  public static void setGp0_40(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x3L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x40L);
  }

  @Method(0x8003b690L)
  public static void setGp0_50(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x50L);
  }

  @Method(0x8003b6b0L)
  public static void setGp0_48(final long a0) {
    MEMORY.ref(1, a0).offset(0x03L).setu(0x5L);
    MEMORY.ref(1, a0).offset(0x07L).setu(0x48L);
    MEMORY.ref(4, a0).offset(0x14L).setu(0x5555_5555L);
  }

  @Method(0x8003b6d0L)
  public static void setGp0_58(final long a0) {
    MEMORY.ref(1, a0).offset(0x03L).setu(0x7L);
    MEMORY.ref(1, a0).offset(0x07L).setu(0x58L);
    MEMORY.ref(1, a0).offset(0x17L).setu(0x0L);
    MEMORY.ref(4, a0).offset(0x1cL).setu(0x5555_5555L);
  }

  @Method(0x8003b700L)
  public static void setGp0_4c(final long a0) {
    MEMORY.ref(1, a0).offset(0x03L).setu(0x6L);
    MEMORY.ref(1, a0).offset(0x07L).setu(0x4cL);
    MEMORY.ref(4, a0).offset(0x18L).setu(0x5555_5555L);
  }

  @Method(0x8003b720L)
  public static void setGp0_5c(final long a0) {
    MEMORY.ref(1, a0).offset(0x03L).setu(0x9L);
    MEMORY.ref(1, a0).offset(0x07L).setu(0x5cL);
    MEMORY.ref(1, a0).offset(0x17L).setu(0x0L);
    MEMORY.ref(1, a0).offset(0x1fL).setu(0x0L);
    MEMORY.ref(4, a0).offset(0x24L).setu(0x5555_5555L);
  }

  @Method(0x8003b750L)
  public static void SetDrawTPage(final DR_TPAGE p, final boolean allowDrawing, final boolean dither, final long tpage) {
    p.tag.and(0xff_ffffL).or(0x100_0000L);

    final long command = 0xe100_0000L; // Texpage/draw mode settings

    final long ditherBit = dither ? 0x200L : 0;
    final long drawingBit = allowDrawing ? 0x400L : 0;
    final long otherBits = tpage & 0x9ffL; // Texpage X/Y base; trans; texpage colours, tex disable

    //LAB_8003b770
    p.code.get(0).set(command | drawingBit | ditherBit | otherBits);
  }

  /**
   * <p>Links primitive p0 to primitive p1. The combined primitive size of p0 and p1 must be less than 15 words.
   * Within this size, any number of connections is possible.</p>
   *
   * <p>The resulting linked primitives can be added to an OT using AddPrim().
   * p0 and p1 describe continuous regions of memory. p1 must be the higher address.</p>
   *
   * @param packet1 First primitive
   * @param packet2 Second primitive
   *
   * @return 0 on success, -1 on failure.
   */
  @Method(0x8003b7e0L)
  public static long MargePrim(final long packet1, final long packet2) {
    final long size = MEMORY.ref(1, packet2).offset(0x3L).get() + MEMORY.ref(1, packet1).offset(0x3L).get() + 0x1L;

    if(size < 0x11L) {
      MEMORY.ref(1, packet1).offset(0x3L).setu(size);
      MEMORY.ref(4, packet2).setu(0);
      return 0;
    }

    //LAB_8003b80c
    //LAB_8003b810
    return -0x1L;
  }

  @Method(0x8003b8f0L)
  public static void FUN_8003b8f0(final long a0) {
    _800c3410.setu(a0);
  }

  @Method(0x8003b900L)
  public static WeirdTimHeader FUN_8003b900(final WeirdTimHeader timHeader) {
    final long ret = FUN_8003b964(_800c3410.get(), timHeader);

    if(ret == -0x1L) {
      //LAB_8003b950
      return null;
    }

    _800c3410.addu(ret * 0x4L);

    //LAB_8003b954
    return timHeader;
  }

  /** TODO figure out what this is doing - looks important... TIM loader? Why is this one different? */
  @Method(0x8003b964L)
  public static long FUN_8003b964(final long a0, final WeirdTimHeader timHeader) {
    if(MEMORY.ref(4, a0).get() != 0x10L) {
      return -0x1L;
    }

    //LAB_8003b998
    timHeader.flags.set(MEMORY.ref(4, a0).offset(0x4L).get());

    LOGGER.info("id  =%08x", 0x10L);
    LOGGER.info("mode=%08x", timHeader.flags.get());
    LOGGER.info("timaddr=%08x", a0 + 0x8L);

    //LAB_8003ba04
    final long a0_0;
    if((timHeader.flags.get() & 0b1000L) != 0) {
      timHeader.clutRect.set(MEMORY.ref(4, a0).offset(0xcL).cast(RECT::new));
      timHeader.clutAddress.set(a0 + 0x14L);
      a0_0 = MEMORY.ref(4, a0).offset(0x8L).get() / 0x4L;
    } else {
      //LAB_8003ba38
      timHeader.clutRect.clear();
      timHeader.clutAddress.set(0);
      a0_0 = 0;
    }

    final long s0 = a0 + a0_0 * 0x4L;

    //LAB_8003ba44
    timHeader.imageRect.set(MEMORY.ref(4, s0).offset(0xcL).cast(RECT::new));
    timHeader.imageAddress.set(s0 + 0x14L);

    //LAB_8003ba64
    return MEMORY.ref(4, s0).offset(0x8L).get() / 0x4L + 0x2L + a0_0; // +8 CLUT data pointer / 4 + 2 (plus CLUT data pointer if CLUT present) ???
  }

  /**
   * <p>Initialize the graphics system.</p>
   *
   * <p>Resets libgpu and initializes the libgs graphic system. libgpu settings are maintained by the global variables
   * GsDISPENV and GsDRAWENV. The programmer can verify and/or modify libgpu by referencing the settings.</p>
   *
   * <p>Vertical 480 line mode is effective only when a VGA monitor is connected. In 240-line mode,
   * the top and bottom 8 lines are almost invisible on home-use TV monitors. For PAL mode, the display
   * position should be shifted down by 24 lines.</p>
   *
   * <p>The double buffer offset mode is either GTE or GPU offset; when it is GPU, the packet does not include the
   * offset value and can therefore be handled easily.</p>
   *
   * <p>For 24-bit mode, only the memory image display is available and polygon drawing cannot be done.
   * Since initialization of the graphic system involves initialization of GsIDMATRIX and GsIDMATRIX2 as well,
   * GsInitGraph() must be called prior to all other libgs functions for correct operation.</p>
   *
   * @param displayWidth Horizontal resolution (256/320/384/512/640)
   * @param displayHeight Vertical resolution (240/480 NTSC or 256/512 PAL)
   * @param flags
   * </li>
   * <li>Double buffer offset mode (bit 2)<ul>
   * <li>0: GTE offset GsOFSGTE</li>
   * <li>1: GPU offset GsOFSGPU</li>
   * </ul></li>
   * <li>GPU Initialize Parameter (bits 4-5)<ul>
   * <li>0: ResetGraph(0) GsRESET0</li>
   * <li>3: ResetGraph(3) GsRESET3</li>
   * </ul></li>
   * </ul>
   */
  @Method(0x8003bc30L)
  public static void GsInitGraph(final short displayWidth, final short displayHeight, final int flags) {
    GsInitGraph2(displayWidth, displayHeight, flags);
    GsInit3D();

    PSDIDX_800c34d4.set(0);

    initDisplay(displayWidth, displayHeight);
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  /**
   * <p>GsInitGraph2() is different from {@link Scus94491BpeSegment_8003#GsInitGraph}() in that the GPU is not initialized COLD. This function is useful
   * for changing libgs resolution without affecting screen synchronization.</p>
   *
   * <p>Always use GsInitGraph() for the first initialization.</p>
   */
  @Method(0x8003bca4L)
  public static void GsInitGraph2(final short displayWidth, final short displayHeight, final int flags) {
    if((flags >> 4 & 0b11) == 0b11) {
      ResetGraph(3);
    } else {
      ResetGraph(0);
    }

    DRAWENV_800c3450.ofs.get(0).set((short)0);
    DRAWENV_800c3450.ofs.get(1).set((short)0);
    DRAWENV_800c3450.tw.set((short)0, (short)0, (short)0, (short)0);
    PutDrawEnv(DRAWENV_800c3450);

    DISPENV_800c34b0.disp.set((short)0, (short)0, displayWidth, displayHeight);
    DISPENV_800c34b0.screen.set((short)0, (short)0, (short)0, (short)0); // W/H of 0 are apparently treated as 256x240

    if(GsGetWorkBase() == 1) {
      DISPENV_800c34b0.screen.y.set((short)0x18);
      DISPENV_800c34b0.pad0.set((byte)1);
    }

    doubleBufferOffsetMode_800c34d6.set(GsOffsetType.fromValue(flags & 0b100));

    PutDispEnv(DISPENV_800c34b0);
  }

  @Method(0x8003be28L)
  public static void initDisplay(final int displayWidth, final int displayHeight) {
    displayWidth_1f8003e0.set(displayWidth);
    displayHeight_1f8003e4.set(displayHeight);

    final int a0 = (displayHeight << 14) / displayWidth / 3;

    identityMatrix_800c3568.set(0, 0, (short)0x1000);
    identityMatrix_800c3568.set(0, 1, (short)0);
    identityMatrix_800c3568.set(0, 2, (short)0);
    identityMatrix_800c3568.set(1, 0, (short)0);
    identityMatrix_800c3568.set(1, 1, (short)0x1000);
    identityMatrix_800c3568.set(1, 2, (short)0);
    identityMatrix_800c3568.set(2, 0, (short)0);
    identityMatrix_800c3568.set(2, 1, (short)0);
    identityMatrix_800c3568.set(2, 2, (short)0x1000);
    identityMatrix_800c3568.transfer.setX(0);
    identityMatrix_800c3568.transfer.setY(0);
    identityMatrix_800c3568.transfer.setZ(0);

    identityAspectMatrix_800c3588.set(0, 0, (short)0x1000);
    identityAspectMatrix_800c3588.set(0, 1, (short)0);
    identityAspectMatrix_800c3588.set(0, 2, (short)0);
    identityAspectMatrix_800c3588.set(1, 0, (short)0);
    identityAspectMatrix_800c3588.set(1, 1, (short)a0);
    identityAspectMatrix_800c3588.set(1, 2, (short)0);
    identityAspectMatrix_800c3588.set(2, 0, (short)0);
    identityAspectMatrix_800c3588.set(2, 1, (short)0);
    identityAspectMatrix_800c3588.set(2, 2, (short)0x1000);
    identityAspectMatrix_800c3588.transfer.setX(0);
    identityAspectMatrix_800c3588.transfer.setY(0);
    identityAspectMatrix_800c3588.transfer.setZ(0);

    lightDirectionMatrix_800c34e8.clear();
    lightColourMatrix_800c3508.clear();

    clip_800c3448.clear();

    centreScreenX_1f8003dc.set((short)0);
    centreScreenY_1f8003de.set((short)0);

    displayRect_800c34c8.set((short)0, (short)0, (short)displayWidth, (short)displayHeight);

    PSDCNT_800c34d0 = 1;
  }

  /**
   * <p>Register a screen clear command in the OT.</p>
   *
   * <p>Sets a screen clear command at the start of the OT indicated by otp. Should be called after
   * GsSwapDispBuff(). Note: Actual clearing isn’t executed until GsDrawOt() is used to start drawing.</p>
   *
   * @param r R
   * @param g G
   * @param b B
   */
  @Method(0x8003c048L)
  public static void GsSortClear(final int r, final int g, final int b) {
    final int x;
    final int y;
    if(PSDIDX_800c34d4.get() == 0) {
      x = clip_800c3440.x1.get();
      y = clip_800c3440.y1.get();
    } else {
      x = clip_800c3440.x2.get();
      y = clip_800c3440.y2.get();
    }

    final int h = displayHeight_1f8003e4.get();
    final int w = displayWidth_1f8003e0.get();

    GPU.queueCommand(orderingTableSize_1f8003c8.get() - 1, new GpuCommandFillVram(x, y, w, h, r, g, b));

    //LAB_8003c150
  }

  @Method(0x8003c1c0L)
  public static void GsSetDrawBuffOffset() {
    final short x = centreScreenX_1f8003dc.get();
    final short y = centreScreenY_1f8003de.get();

    final short clipX;
    final short clipY;
    if(PSDIDX_800c34d4.get() == 0) {
      clipX = clip_800c3440.x1.get();
      clipY = clip_800c3440.y1.get();
    } else {
      clipX = clip_800c3440.x2.get();
      clipY = clip_800c3440.y2.get();
    }

    if(doubleBufferOffsetMode_800c34d6.get() == GsOffsetType.GsOFSGTE) {
      SetGeomOffset(x + clipX, y + clipY);

      _800c34c4.setu(x + clipX);
      _800c34c6.setu(y + clipY);
    } else {
      _800c34c4.setu(0);
      _800c34c6.setu(0);

      DRAWENV_800c3450.ofs.get(0).set((short)(x + clipX));
      DRAWENV_800c3450.ofs.get(1).set((short)(y + clipY));

      PutDrawEnv(DRAWENV_800c3450);
    }
  }

  /**
   * Set drawing clipping area.<p>
   * Sets clipping for drawing. The clipping value set by GsSetClip2D() is set in libgs.<p>
   * This value is a relative one within the double buffers, so the clipping position does not change when buffers
   * are swapped.<p>
   * This function does not execute correctly if GPU drawing is in progress. Use ResetGraph(1) to terminate any
   * current drawing process or DrawSync() to wait until the process is completed.
   */
  @Method(0x8003c2d0L)
  public static void GsSetDrawBuffClip() {
    DRAWENV_800c3450.clip.set(
      (short)(displayRect_800c34c8.x.get() + (PSDIDX_800c34d4.get() == 0 ? clip_800c3440.x1 : clip_800c3440.x2).get()),
      (short)(displayRect_800c34c8.y.get() + (PSDIDX_800c34d4.get() == 0 ? clip_800c3440.y1 : clip_800c3440.y2).get()),
      displayRect_800c34c8.w.get(),
      displayRect_800c34c8.h.get()
    );

    PutDrawEnv(DRAWENV_800c3450);
  }

  /**
   * <p>Swaps double buffers.</p>
   *
   * <p>Exchanges the display buffer with the drawing buffer according to data set by GsDefDispBuff(). Normally,
   * swapping is done immediately after beginning vertical blanking. This function<ul>
   * <li>Sets display starting address</li>
   * <li>Cancels blanking</li>
   * <li>Sets double buffer index</li>
   * <li>Switches two-dimensional clipping</li>
   * <li>Sets libgte or libgpu offset</li>
   * <li>Sets libgs offset</li></ul></p>
   *
   * <p>Note: Using the GsOFSGPU or GsOFSGTE macro for the third argument of GsInitGraph() determines
   * whether the libgte or libgpu offset should be set.</p>
   *
   * <p>This function does not execute correctly when GPU drawing is in progress, so it is necessary to call this
   * function after terminating drawing using ResetGraph (1).</p>
   */
  @Method(0x8003c350L)
  public static void GsSwapDispBuff() {
    if(PSDIDX_800c34d4.get() == 0) {
      DISPENV_800c34b0.disp.x.set(clip_800c3440.x1.get());
      DISPENV_800c34b0.disp.y.set(clip_800c3440.y1.get());
    } else {
      DISPENV_800c34b0.disp.x.set(clip_800c3440.x2.get());
      DISPENV_800c34b0.disp.y.set(clip_800c3440.y2.get());
    }

    PutDispEnv(DISPENV_800c34b0);

    // GsIncFrame macro
    PSDCNT_800c34d0++;
    if(PSDCNT_800c34d0 < 0) {
      PSDCNT_800c34d0 = 1;
    }

    //LAB_8003c3bc
    PSDIDX_800c34d4.set(PSDIDX_800c34d4.get() == 0 ? 1 : 0);
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  @Method(0x8003c400L)
  public static void GsInitCoordinate2(@Nullable final GsCOORDINATE2 superCoord, final GsCOORDINATE2 newCoord) {
    newCoord.flg = 0;
    newCoord.coord.set(identityMatrix_800c3568);
    newCoord.super_ = superCoord;

    if(superCoord != null) {
      superCoord.sub = newCoord;
    }

    //LAB_8003c468
  }

  @Method(0x8003c470L)
  public static void setRotTransMatrix(final MATRIX matrix) {
    SetRotMatrix(matrix);
    SetTransMatrix(matrix);
  }

  @Method(0x8003c4a0L)
  public static void GsSetLightMatrix(final MATRIX mp) {
    final MATRIX lightDirection = new MATRIX().set(lightDirectionMatrix_800c34e8);

    PushMatrix();
    MulMatrix(lightDirection, mp);
    PopMatrix();

    SetLightMatrix(lightDirection);
  }

  /**
   * <p>Defines the display areas used for double-buffering.</p>
   *
   * <p>x0 and y0 specify the frame buffer coordinates for buffer #0. x1 and y1 specify the frame buffer coordinates
   * for buffer #0. Normally, buffer #0 is located at (0,0) and buffer #1 is located at (0, yres), where yres is the
   * vertical resolution specified using GsInitGraph().</p>
   *
   * <p>If x0, y0 and x1, y1 are specified as the same coordinates, the double buffers are released. However,
   * double-buffer swapping of even-numbered and odd-numbered fields is performed automatically when x0,
   * y0 and x1, y1 are specified as the same coordinates in interlace mode.</p>
   *
   * <p>GsSwapDispBuffer() is used to swap double buffers. The double buffer is implemented by the GPU or GTE
   * offset. Set the libgpu or libgte offset with GsInitGraph(). When using the libgpu offset, coordinate values
   * based on the coordinate system using the upper left point in the double buffer as the origin are created in
   * the packet (add the offset at the time of drawing, not at the time of packet preparation).</p>
   */
  @Method(0x8003c540L)
  public static void GsDefDispBuff(final short x1, final short y1, final short x2, final short y2) {
    clip_800c3440.set(x1, x2, y1, y2);

    if(doubleBufferOffsetMode_800c34d6.get() == GsOffsetType.GsOFSGTE) {
      //LAB_8003c598
      clip_800c3448.set(x1, x2, y1, y2);
    } else {
      clip_800c3448.set((short)0, (short)0, (short)0, (short)0);
    }

    //LAB_8003c5b8
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  @Method(0x8003c5e0L)
  public static void FUN_8003c5e0() {
    centreScreenX_1f8003dc.set((short)(displayWidth_1f8003e0.get() / 2));
    centreScreenY_1f8003de.set((short)(displayHeight_1f8003e4.get() / 2));
    GsSetDrawBuffOffset();
    _800c34d8.setu(0x3fff);
    lightMode_800c34dc.setu(0);
    _800c34e0.setu(10);
  }

  /**
   * TMDs can have either fixed pointers (fixp) or offset pointers. If it uses offsets, we need to add the base address to them.
   */
  @Method(0x8003c660L)
  public static void adjustTmdPointers(final Tmd tmd) {
    if((tmd.header.flags.get() & 0x1L) != 0) {
      return;
    }

    tmd.header.flags.or(0x1L);

    //LAB_8003c694
    for(int i = 0; i < tmd.header.nobj.get(); i++) {
      final TmdObjTable objTable = tmd.objTable.get(i);
      objTable.vert_top_00.add(tmd.objTable.getAddress());
      objTable.normal_top_08.add(tmd.objTable.getAddress());
      objTable.primitives_10.add(tmd.objTable.getAddress());
    }

    //LAB_8003c6c8
  }

  /**
   * Set a parallel light source.
   * <p>
   * Sets the values for one of up to three parallel light sources. Light source data is specified in the GsF_LIGHT structure.
   *
   * @param id Light source number (0, 1, 2)
   * @param light Pointer to light source data
   *
   * @return 0 on success, -1 on failure
   */
  @Method(0x8003c6f0L)
  public static long GsSetFlatLight(final long id, final GsF_LIGHT light) {
    final long x = light.direction_00.getX();
    final long y = light.direction_00.getY();
    final long z = light.direction_00.getZ();
    final long r = light.r_0c.get();
    final long g = light.g_0d.get();
    final long b = light.b_0e.get();

    final MATRIX directionMatrix = new MATRIX().set(lightDirectionMatrix_800c34e8);
    final MATRIX colourMatrix = new MATRIX();

    getLightColour(colourMatrix);

    // Normalize vector - calculate magnitude
    final long mag = SquareRoot0(x * x + y * y + z * z);

    if(mag == 0) {
      return -0x1L;
    }

    if(id == 0) {
      //LAB_8003c7ec
      directionMatrix.set(0, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(1, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(2, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(0, (short)((r << 12) / 0xff));
      colourMatrix.set(3, (short)((g << 12) / 0xff));
      colourMatrix.set(6, (short)((b << 12) / 0xff));
    } else if(id == 0x1L) {
      //LAB_8003c904
      directionMatrix.set(3, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(4, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(5, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(1, (short)((r << 12) / 0xff));
      colourMatrix.set(4, (short)((g << 12) / 0xff));
      colourMatrix.set(7, (short)((b << 12) / 0xff));
      //LAB_8003c7dc
    } else if(id == 0x2L) {
      //LAB_8003ca20
      directionMatrix.set(6, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(7, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(8, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(2, (short)((r << 12) / 0xff));
      colourMatrix.set(5, (short)((g << 12) / 0xff));
      colourMatrix.set(8, (short)((b << 12) / 0xff));
    }

    //LAB_8003cb34
    lightDirectionMatrix_800c34e8.set(directionMatrix);
    setLightColour(colourMatrix);

    //LAB_8003cb88
    return 0;
  }

  @Method(0x8003cba8L)
  public static void setLightColour(final MATRIX mat) {
    lightColourMatrix_800c3508.set(mat);
    SetColorMatrix(mat);
  }

  @Method(0x8003cc0cL)
  public static void getLightColour(final MATRIX mat) {
    mat.set(lightColourMatrix_800c3508);
  }

  @Method(0x8003cc60L)
  public static void setLightMode(final long a0) {
    if(a0 < 0 || a0 > 3) {
      throw new RuntimeException("Invalid lighting mode " + a0);
    }

    lightMode_800c34dc.setu(a0);
  }

  @Method(0x8003cce0L)
  public static void GsSetAmbient(final int r, final int g, final int b) {
    SetBackColor(r >> 4, g >> 4, b >> 4);
  }

  /**
   * Initializes the libgs three-dimensional graphics system. It must be called before using other 3D functions
   * such as GsSetRefView2(), GsInitCoordinate2(), and GsSortObject3(). It does the following:
   * <ul>
   *   <li>Brings the screen point of origin to the center of the screen.</li>
   *   <li>Sets the light source to the LIGHT_NORMAL default.</li>
   * </ul>
   */
  @Method(0x8003cda0L)
  public static void GsInit3D() {
    InitGeom();
    SetFarColour(0, 0, 0);
    SetGeomOffset(0, 0);
    _800c34c4.setu(0);
    _800c34c6.setu(0);
  }

  @Method(0x8003cdf0L)
  public static TimHeader parseTimHeader(final Value baseAddress) {
    final TimHeader header = new TimHeader();
    header.flags.set(baseAddress.offset(4, 0x0L).get());

    if(baseAddress.get(0b1000L) == 0) { // No CLUT
      //LAB_8003ce94
      final RECT imageRect = new RECT();
      imageRect.x.set((short)baseAddress.offset(2, 0x8L).get()); // Image X
      imageRect.y.set((short)baseAddress.offset(2, 0xaL).get()); // Image Y

      imageRect.w.set((short)baseAddress.offset(2, 0xcL).get()); // Image W
      imageRect.h.set((short)baseAddress.offset(2, 0xeL).get()); // Image H

      header.setImage(imageRect, baseAddress.offset(0x10L).getAddress()); // Pointer to image data
    } else { // Has CLUT
      final RECT clutRect = new RECT();
      clutRect.x.set((short)baseAddress.offset(2, 0x8L).get()); // CLUT X
      clutRect.y.set((short)baseAddress.offset(2, 0xaL).get()); // CLUT Y

      clutRect.w.set((short)baseAddress.offset(2, 0xcL).get()); // CLUT W
      clutRect.h.set((short)baseAddress.offset(2, 0xeL).get()); // CLUT H

      header.setClut(clutRect, baseAddress.offset(0x10L).getAddress()); // Pointer to CLUT table

      final Value imageData = baseAddress.offset(0x4L).offset(baseAddress.offset(2, 0x4L)); // Address to image data block

      final RECT imageRect = new RECT();
      imageRect.x.set((short)imageData.offset(2, 0x4L).get()); // Image X
      imageRect.y.set((short)imageData.offset(2, 0x6L).get()); // Image Y

      imageRect.w.set((short)imageData.offset(2, 0x8L).get()); // Image W
      imageRect.h.set((short)imageData.offset(2, 0xaL).get()); // Image H

      header.setImage(imageRect, imageData.offset(0xcL).getAddress()); // Pointer to image data
    }

    //LAB_8003cecc
    return header;
  }

  @Method(0x8003cee0L)
  public static void FUN_8003cee0(final MATRIX matrix, final long sin, final long cos, final long param_4) {
    matrix.set(identityMatrix_800c3568);

    //TODO this is weird... these are the positions for rotation matrices, but if they were transposed

    switch((byte)(param_4 - 0x58L)) {
      case 0x0, 0x20 -> {
        matrix.set(1, 1, (short)cos);
        matrix.set(1, 2, (short)-sin);
        matrix.set(2, 1, (short)sin);
        matrix.set(2, 2, (short)cos);
      }

      case 0x1, 0x21 -> {
        matrix.set(0, 0, (short)cos);
        matrix.set(0, 2, (short)sin);
        matrix.set(2, 0, (short)-sin);
        matrix.set(2, 2, (short)cos);
      }

      case 0x2, 0x22 -> {
        matrix.set(0, 0, (short)cos);
        matrix.set(0, 1, (short)-sin);
        matrix.set(1, 0, (short)sin);
        matrix.set(1, 1, (short)cos);
      }
    }
  }

  /**
   * <p>Calculates GsWSMATRIX using viewpoint information in pv. GsWSMATRIX doesn't change unless the
   * viewpoint is moved, so this function should be called every frame only if the viewpoint is moved, in order for
   * changes to be updated.</p>
   *
   * <p>It should also be called every frame if the GsRVIEW2 member super is set to anything other than WORLD,
   * because even if the other parameters are not changed, if the parameters of the superior coordinate system
   * are changed, the viewpoint will have moved.</p>
   *
   * @param struct Pointer to view information
   *
   * @return 0 on success; 2 on failure.
   */
  @Method(0x8003cfb0L)
  public static long GsSetRefView2(final GsRVIEW2 struct) {
    final long s0;
    final long s1;
    final long s2;
    final long a1;
    long a2;
    long v0;

    worldToScreenMatrix_800c3548.set(identityAspectMatrix_800c3588);

    FUN_8003d5d0(worldToScreenMatrix_800c3548, -struct.viewpointTwist_18);
    final VECTOR[] sp10 = {new VECTOR(), new VECTOR()};
    FUN_8003d380(struct, sp10);

    long a0;

    v0 = sp10[1].getX() - sp10[0].getX();
    a0 = v0 * v0;

    v0 = sp10[1].getY() - sp10[0].getY();
    a0 += v0 * v0;

    v0 = sp10[1].getZ() - sp10[0].getZ();
    a0 += v0 * v0;

    s2 = SquareRoot0(a0);
    if(s2 == 0) {
      return 0x1L;
    }

    s0 = (sp10[0].getY() - sp10[1].getY()) << 12;

    //LAB_8003d0dc
    v0 = sp10[1].getX() - sp10[0].getX();
    a0 = v0 * v0;

    v0 = sp10[1].getZ() - sp10[0].getZ();
    a0 += v0 * v0;

    s1 = SquareRoot0(a0);
    a2 = s1 << 12;

    //LAB_8003d134
    final MATRIX sp30 = new MATRIX();
    FUN_8003cee0(sp30, -(short)(s0 / s2), (short)(a2 / s2), 0x78L);
    MulMatrix(worldToScreenMatrix_800c3548, sp30);

    if(s1 != 0) {
      a1 = (sp10[1].getX() - sp10[0].getX()) << 12;

      //LAB_8003d1c0
      a2 = (sp10[1].getZ() - sp10[0].getZ()) << 12;

      //LAB_8003d200
      FUN_8003cee0(sp30, -(short)(a1 / s1), (short)(a2 / s1), 0x79L);
      MulMatrix(worldToScreenMatrix_800c3548, sp30);
    }

    //LAB_8003d230
    worldToScreenMatrix_800c3548.transfer.set(ApplyMatrixLV(worldToScreenMatrix_800c3548, new VECTOR().set(struct.viewpoint_00).negate()));

    if(struct.super_1c != null) {
      final MATRIX lw = new MATRIX();
      GsGetLw(struct.super_1c, lw);

      final MATRIX transposedLw = new MATRIX();
      TransposeMatrix(lw, transposedLw);
      transposedLw.transfer.set(ApplyMatrixLV(transposedLw, lw.transfer)).negate();
      GsMulCoord2(worldToScreenMatrix_800c3548, transposedLw);
      worldToScreenMatrix_800c3548.set(transposedLw);
    }

    //LAB_8003d310
    matrix_800c3528.set(worldToScreenMatrix_800c3548);

    //LAB_8003d35c
    return 0;
  }

  @Method(0x8003d380L)
  public static void FUN_8003d380(final GsRVIEW2 a0, final VECTOR[] a1) {
    int longestComponentBits = log2(getLongestComponent(a0));

    if(longestComponentBits < 16) {
      a1[0].set(a0.viewpoint_00);
      a1[1].set(a0.refpoint_0c);
    } else {
      longestComponentBits -= 15;

      a1[0].set(a0.viewpoint_00).shra(longestComponentBits);
      a1[1].set(a0.refpoint_0c).shra(longestComponentBits);
    }
  }

  @Method(0x8003d46cL)
  public static long getLongestComponent(final GsRVIEW2 a0) {
    long largest = Math.abs(a0.viewpoint_00.getX());
    long other = Math.abs(a0.viewpoint_00.getY());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.viewpoint_00.getZ());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getX());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getY());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getZ());
    if(largest < other) {
      largest = other;
    }

    return largest;
  }

  @Method(0x8003d534L)
  public static int log2(final long a0) {
    if(a0 == 0) {
      return 0;
    }

    return 64 - Long.numberOfLeadingZeros(a0);
  }

  /**
   * GsMulCoord2 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m2 = m1 x m2
   */
  @Method(0x8003d550L)
  public static void GsMulCoord2(final MATRIX matrix1, final MATRIX matrix2) {
    final VECTOR out = ApplyMatrixLV(matrix1, matrix2.transfer);
    MulMatrix2(matrix1, matrix2);
    matrix2.transfer.set(matrix1.transfer).add(out);
  }

  @Method(0x8003d5d0L)
  public static void FUN_8003d5d0(final MATRIX matrix, final long angle) {
    if(angle != 0) {
      final short cos = rcos(angle / 360);
      final short sin = rsin(angle / 360);

      final MATRIX rot = new MATRIX();
      rot.set(0, 0, cos);
      rot.set(0, 1, (short)-sin);
      rot.set(0, 2, (short)0);
      rot.set(1, 0, sin);
      rot.set(1, 1, cos);
      rot.set(1, 2, (short)0);
      rot.set(2, 0, (short)0);
      rot.set(2, 1, (short)0);
      rot.set(2, 2, (short)0x1000);
      MulMatrix(matrix, rot);
    }
  }

  /**
   * Calculates a local world perspective transformation matrix from the GsCOORDINATE2 structure pointed to
   * by the coord argument and stores the result in the MATRIX structure pointed to by the m argument.
   * <p>
   * For high speed operation, the function retains the result of calculation at each node of the hierarchical
   * coordinate system. When the next GsGetLw() function is called, calculation up to the node to which no
   * changes have been made is omitted. This is controlled by a GsCOORDINATE2 member flag (libgs replaces
   * 1 in flags already calculated by GsCOORDINATE2).
   * <p>
   * If the contents of a superior node are changed, the effect on a subordinate node is handled by libgs, so it is
   * not necessary to clear the flags of all subordinate nodes of the changed superior node.
   *
   * @param coord Pointer to local coordinate system
   * @param matrix Pointer to matrix
   */
  @Method(0x8003d690L)
  public static void GsGetLw(final GsCOORDINATE2 coord, final MATRIX matrix) {
    GsCOORDINATE2 a3 = coord;
    int s1 = 0;
    int a1_0 = 100;

    //LAB_8003d6c0
    do {
      coord2s_800c35a8[s1] = a3;

      if(a3.super_ == null) {
        if(a3.flg == 0 || a3.flg == PSDCNT_800c34d0) {
          //LAB_8003d6fc
          a3.workm.set(a3.coord);
          matrix.set(a3.workm);
          a3.flg = PSDCNT_800c34d0;
          break;
        }

        //LAB_8003d78c
        if(a1_0 == 100) {
          matrix.set(coord2s_800c35a8[0].workm);
          s1 = 0;
          break;
        }

        //LAB_8003d7e8
        s1 = a1_0 + 1;
        matrix.set(coord2s_800c35a8[s1].workm);
        break;
      }

      //LAB_8003d83c
      if(a3.flg == PSDCNT_800c34d0) {
        matrix.set(a3.workm);
        break;
      }

      //LAB_8003d898
      if(a3.flg == 0) {
        a1_0 = s1;
      }

      //LAB_8003d8a4
      a3 = a3.super_;
      s1++;
    } while(true);

    //LAB_8003d8ac
    //LAB_8003d8c0
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 coord2 = coord2s_800c35a8[s1];
      GsMulCoord3(matrix, coord2.coord);
      coord2.workm.set(matrix);
      coord2.flg = PSDCNT_800c34d0;
    }

    //LAB_8003d930
  }

  /**
   * GsMulCoord3 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m1 = m1 x m2
   */
  @Method(0x8003d950L)
  public static void GsMulCoord3(final MATRIX m1, final MATRIX m2) {
    final VECTOR out = ApplyMatrixLV(m1, m2.transfer);
    MulMatrix(m1, m2);
    m1.transfer.add(out);
  }

  /**
   * Calculates a local screen perspective transformation matrix from the GsCOORDINATE2 structure pointed
   * to by the coord argument and stores the result in the MATRIX structure pointed to by the m argument.
   * <p>
   * For high speed operation, the function retains the result of calculation at each node of the hierarchical
   * coordinate system. When the next GsGetLs() function is called, calculation up to the node to which no
   * changes have been made is omitted. This is controlled by a GsCOORDINATE2 member flag (libgs replaces
   * 1 in flags already calculated by GsCOORDINATE2).
   * <p>
   * If the contents of a superior node are changed, the effect on a subordinate node is handled by libgs, so it is
   * not necessary to clear the flags of all subordinate nodes of the changed superior node.
   *
   * @param coord Pointer to local coordinate system
   * @param matrix Pointer to matrix
   */
  @Method(0x8003d9d0L)
  public static void GsGetLs(final GsCOORDINATE2 coord, final MATRIX matrix) {
    GsCOORDINATE2 a3 = coord;
    int s1 = 0;
    int a1 = 100;

    //LAB_8003da00
    do {
      coord2s_800c35a8[s1] = a3;
      if(a3.super_ == null) {
        if(a3.flg == 0 || a3.flg == PSDCNT_800c34d0) {
          //LAB_8003da3c
          a3.workm.set(a3.coord);
          matrix.set(a3.workm);
          a3.flg = PSDCNT_800c34d0;
          break;
        }

        //LAB_8003dacc
        s1 = a1 + 1;

        if(a1 == 100) {
          matrix.set(coord2s_800c35a8[0].workm);
          s1 = 0;
          break;
        }

        //LAB_8003db28
        matrix.set(coord2s_800c35a8[s1].workm);
        break;
      }

      //LAB_8003db7c
      if(a3.flg == PSDCNT_800c34d0) {
        matrix.set(a3.workm);
        break;
      }

      //LAB_8003dbd8
      if(a3.flg == 0) {
        a1 = s1;
      }

      //LAB_8003dbe4
      a3 = a3.super_;
      s1++;
    } while(true);

    //LAB_8003dbec
    //LAB_8003dc00
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 coord2 = coord2s_800c35a8[s1];
      GsMulCoord3(matrix, coord2.coord);
      coord2.workm.set(matrix);
      coord2.flg = PSDCNT_800c34d0;
    }

    //LAB_8003dc70
    GsMulCoord2(worldToScreenMatrix_800c3548, matrix);
  }

  /**
   * Calculate local world and local screen matrices.
   * <p>
   * GsGetLws() calculates local world and local screen coordinates. It is faster than calling GsGetLw() followed
   * by calling GsGetLs(). When you use GsSetLightMatrix(), you pass it the lw matrix.
   *
   * @param coord Pointer to local coordinates
   * @param lw Pointer to matrix that stores the local world coordinates
   * @param ls Pointer to matrix that stores the local screen coordinates
   */
  @Method(0x8003dca0L)
  public static void GsGetLws(GsCOORDINATE2 coord, final MATRIX lw, final MATRIX ls) {
    int s1 = 0;
    int a = 100;

    //LAB_8003dcd8
    do {
      coord2s_800c35a8[s1] = coord;

      if(coord.super_ == null) {
        if(coord.flg == PSDCNT_800c34d0 || coord.flg == 0) {
          //LAB_8003dd14
          coord.flg = PSDCNT_800c34d0;
          coord.workm.set(coord.coord);
          lw.set(coord.workm);
          break;
        }

        //LAB_8003dda4
        s1 = a + 1;
        if(a == 100) {
          lw.set(coord2s_800c35a8[0].workm);
          s1 = 0;
          break;
        }

        //LAB_8003de00
        lw.set(coord2s_800c35a8[s1].workm);
        break;
      }

      //LAB_8003de54
      if(coord.flg == PSDCNT_800c34d0) {
        lw.set(coord.workm);
        break;
      }

      //LAB_8003deb0
      if(coord.flg == 0) {
        a = s1;
      }

      //LAB_8003debc
      coord = coord.super_;
      s1++;
    } while(true);

    //LAB_8003dec4
    //LAB_8003ded8
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 c = coord2s_800c35a8[s1];

      GsMulCoord3(lw, c.coord);

      c.flg = PSDCNT_800c34d0;
      c.workm.set(lw);
    }

    //LAB_8003df48
    ls.set(lw);
    GsMulCoord2(worldToScreenMatrix_800c3548, ls);
  }

  /**
   * Calculates GsWSMATRIX using the viewpoint information in pv. GsWSMATRIX doesn’t change unless the
   * viewpoint is moved, so this function should be called every frame only if the viewpoint is moved, in order for
   * changes to be updated.
   *
   * It should also be called every frame if the GsRVIEW2 member super is set to anything other than WORLD,
   * because even if the other parameters are not changed, if the parameters of the superior coordinate system
   * are changed, the viewpoint will have moved.
   *
   * Compared to GsSetRefView2(), GsSetRefView2L() has higher precision: viewpoint wobbling caused by
   * insufficient precision is improved. However, its execution time is doubled
   */
  @Method(0x8003dfc0L)
  public static long GsSetRefView2L(final GsRVIEW2 s2) {
    int v0;
    int v1;
    int a0;
    int a1;
    final int a3;
    int s0;
    int s1;
    int s3;

    worldToScreenMatrix_800c3548.set(identityAspectMatrix_800c3588);
    FUN_8003d5d0(worldToScreenMatrix_800c3548, -s2.viewpointTwist_18);

    v0 = s2.refpoint_0c.getX() - s2.viewpoint_00.getX();
    a3 = v0 * v0;

    v0 = s2.refpoint_0c.getY() - s2.viewpoint_00.getY();
    a0 = v0 * v0;

    v0 = s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ();
    v1 = v0 * v0;

    s0 = a3 + a0 + v1;
    if(s0 == 0) {
      return 0x1L;
    }

    v0 = s2.viewpoint_00.getY() - s2.refpoint_0c.getY();
    s1 = v0 * v0;
    a1 = 12 - Lzc(s1);
    if(a1 < 0) {
      //LAB_8003e0e4
      //LAB_8003e0fc
      s3 = (s2.viewpoint_00.getY() - s2.refpoint_0c.getY()) * -0x1000 / SquareRoot0(s0);
      //LAB_8003e108
    } else if(s2.viewpoint_00.getY() - s2.refpoint_0c.getY() >= 0) {
      v0 = s0 >>> a1;
      a0 = 12 - a1;
      a0 = s1 << a0;

      //LAB_8003e138
      s3 = -FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
    } else {
      v0 = s0 >>> a1;

      //LAB_8003e14c
      a0 = 12 - a1;
      a0 = s1 << a0;

      //LAB_8003e164
      s3 = FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
    }

    //LAB_8003e174
    v0 = s2.refpoint_0c.getX() - s2.viewpoint_00.getX();
    a0 = v0 * v0;

    v0 = s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ();
    v1 = v0 * v0;

    s1 = a0 + v1;
    a1 = 12 - Lzc(s1);

    if(a1 < 0) {
      //LAB_8003e1e8
      //LAB_8003e200
      v0 = SquareRoot0(s1) * 0x1000 / SquareRoot0(s0);
    } else {
      a0 = 12 - a1;

      //LAB_8003e20c
      a0 = s1 << a0;
      v0 = s0 >>> a1;

      //LAB_8003e224
      v0 = FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
    }

    //LAB_8003e230
    final MATRIX sp0x30 = new MATRIX();
    FUN_8003cee0(sp0x30, (short)s3, (short)v0, 0x78L);
    MulMatrix(worldToScreenMatrix_800c3548, sp0x30);

    if(s1 != 0) {
      s0 = s1;
      v0 = s2.refpoint_0c.getX() - s2.viewpoint_00.getX();
      s1 = v0 * v0;
      a1 = 12 - Lzc(s1);
      if(a1 < 0) {
        //LAB_8003e2c8
        //LAB_8003e2e0
        s3 = (s2.refpoint_0c.getX() - s2.viewpoint_00.getX()) * -0x1000 / SquareRoot0(s0);
        //LAB_8003e2ec
      } else if(s2.refpoint_0c.getX() - s2.viewpoint_00.getX() >= 0) {
        v0 = s0 >>> a1;
        a0 = 12 - a1;
        a0 = s1 << a0;

        //LAB_8003e31c
        s3 = -FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
      } else {
        v0 = s0 >>> a1;

        //LAB_8003e330
        a0 = 12 - a1;
        a0 = s1 << a0;

        //LAB_8003e348
        s3 = FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
      }

      //LAB_8003e358
      v0 = s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ();
      s1 = v0 * v0;
      a1 = 12 - Lzc(s1);
      if(a1 < 0) {
        //LAB_8003e3b4
        //LAB_8003e3cc
        v0 = (s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ()) * 0x1000 / SquareRoot0(s0);
        //LAB_8003e3d8
      } else if(s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ() >= 0) {
        v0 = s0 >>> a1;
        a0 = 12 - a1;
        a0 = s1 << a0;

        //LAB_8003e408
        v0 = FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
      } else {
        v0 = s0 >>> a1;
        //LAB_8003e41c
        a0 = 12 - a1;
        a0 = s1 << a0;

        //LAB_8003e434
        v0 = -FUN_8003e8b4((int)((a0 & 0xffff_ffffL) / v0));
      }

      //LAB_8003e444
      //LAB_8003e448
      FUN_8003cee0(sp0x30, (short)s3, (short)v0, 0x79L);
      MulMatrix(worldToScreenMatrix_800c3548, sp0x30);
    }

    //LAB_8003e474
    worldToScreenMatrix_800c3548.transfer.set(ApplyMatrixLV(worldToScreenMatrix_800c3548, new VECTOR().set(s2.viewpoint_00).negate()));

    if(s2.super_1c != null) {
      final MATRIX lw = new MATRIX();
      GsGetLw(s2.super_1c, lw);

      final MATRIX transposedLw = new MATRIX();
      TransposeMatrix(lw, transposedLw);
      transposedLw.transfer.set(ApplyMatrixLV(transposedLw, lw.transfer)).negate();
      GsMulCoord2(worldToScreenMatrix_800c3548, transposedLw);
      worldToScreenMatrix_800c3548.set(transposedLw);
    }

    //LAB_8003e55c
    matrix_800c3528.set(worldToScreenMatrix_800c3548);

    //LAB_8003e5a8
    return 0;
  }

  /**
   * I think this method reads through all the packets and sort of "combines" ones that have the same MODE and FLAG for efficiency
   */
  @Method(0x8003e5d0L)
  public static void updateTmdPacketIlen(final UnboundedArrayRef<TmdObjTable> objTables, final GsDOBJ2 dobj2, final int objIndex) {
    long bytesSinceModeOrFlagChange = 0;
    long mode = 0;
    long flag = 0;

    final TmdObjTable objTable = objTables.get(objIndex);
    dobj2.tmd_08 = objTable;

    int packetIndex = 0;
    int packetStartIndex = 0;

    //LAB_8003e638
    for(int primitiveIndex = 0; primitiveIndex < objTable.n_primitive_14.get(); primitiveIndex++) {
      final long previousMode = mode;
      final long previousFlag = flag;

      // Primitive: mode, flag, ilen, olen
      final long primitive = objTable.primitives_10.deref().get(packetIndex / 4).get();

      mode = primitive >>> 24 & 0xffL;
      flag = primitive >>> 16 & 0xffL;

      if(previousMode != 0) {
        if(mode != previousMode || flag != previousFlag) {
          //LAB_8003e668
          objTable.primitives_10.deref().get(packetStartIndex / 4).and(0xffff_0000L).or(bytesSinceModeOrFlagChange);
          bytesSinceModeOrFlagChange = 0;
          packetStartIndex = packetIndex;
        }
      }

      //LAB_8003e674
      //LAB_8003e678
      switch((int)(mode & 0xfdL)) {
        case 0x20: // setPolyF3
          if((flag & 0x4L) == 0) {
            packetIndex += 0x10L;
            break;
          }

        case 0x31:
        case 0x24: // setPolyFT3
          packetIndex += 0x18L;
          break;

        case 0x30: // setPolyG3
          if((flag & 0x4L) == 0) {
            packetIndex += 0x14L;
            break;
          }

        case 0x34: // setPolyGT3
        case 0x39:
        case 0x25:
          packetIndex += 0x1cL;
          break;

        case 0x28: // setPolyF4
          if((flag & 0x4L) == 0) {
            packetIndex += 0x14L;
            break;
          }

        case 0x2d:
        case 0x2c: // setPolyFT4
          packetIndex += 0x20L;
          break;

        case 0x29:
        case 0x21:
          packetIndex += 0x10L;
          break;

        case 0x3d:
          packetIndex += 0x2c;
          break;

        case 0x38: // setPolyG4
          if((flag & 0x4L) == 0) {
            packetIndex += 0x18L;
            break;
          }

        case 0x3c: // setPolyGT4
        case 0x35:
          packetIndex += 0x24L;
          break;

        case 0x23:
        case 0x26:
        case 0x27:
        case 0x2a:
        case 0x2b:
        case 0x2e:
        case 0x2f:
        case 0x32:
        case 0x33:
        case 0x36:
        case 0x37:
        case 0x3a:
        case 0x3b:
        case 0x22:
          LOGGER.error("GPU CODE %02xH not assigned.", mode);
          break;
      }

      //LAB_8003e714
      bytesSinceModeOrFlagChange++;
    }

    //LAB_8003e724
    objTable.primitives_10.deref().get(packetStartIndex / 4).and(0xffff_0000L).or(bytesSinceModeOrFlagChange);
  }

  @Method(0x8003e760L) //TODO using div instead of shifting means some of these values are slightly off, does this matter?
  public static int FUN_8003e760(final int a0) {
    final int[] sp0x04 = new int[7];
    final int[] sp0x24 = new int[7];
    sp0x04[0] = a0 + 0x5d_50ad;
    sp0x24[0] = a0 - 0x5d_50ad;

    //LAB_8003e790
    for(int a3 = 1; a3 < 7; a3++) {
      if(a3 != 4) {
        final int v0 = sp0x04[a3 - 1] >> a3;

        if(sp0x24[a3 - 1] < 0) {
          //LAB_8003e7d0
          sp0x04[a3] = sp0x04[a3 - 1] + (sp0x24[a3 - 1] >> a3);
          sp0x24[a3] = sp0x24[a3 - 1] + v0;
        } else {
          sp0x04[a3] = sp0x04[a3 - 1] - (sp0x24[a3 - 1] >> a3);
          sp0x24[a3] = sp0x24[a3 - 1] - v0;
        }
      } else {
        //LAB_8003e7fc
        //LAB_8003e87c
        if(sp0x24[3] >= 0) {
          sp0x24[3] -= sp0x04[3] >> 4;
          sp0x04[3] -= sp0x24[3] >> 4;
        } else {
          //LAB_8003e844
          sp0x24[3] += sp0x04[3] >> 4;
          sp0x04[3] += sp0x24[3] >> 4;
        }

        if(sp0x24[3] < 0) {
          //LAB_8003e87c
          sp0x04[4] = sp0x04[3] + (sp0x24[3] >> 4);
          sp0x24[4] = sp0x24[3] + (sp0x04[3] >> 4);
        } else {
          sp0x04[4] = sp0x04[3] - (sp0x24[3] >> 4);
          sp0x24[4] = sp0x24[3] - (sp0x04[3] >> 4);
        }

        //LAB_8003e890
      }

      //LAB_8003e894
    }

    return sp0x04[6];
  }

  @Method(0x8003e8b4L)
  public static int FUN_8003e8b4(int a0) {
    int s0;

    if(a0 == 0) {
      return 0;
    }

    //LAB_8003e8d4
    int v0 = 8 - Lzc(a0);
    if(v0 >= 0) {
      s0 = v0 >> 1;
      a0 = a0 >> s0 * 2;
    } else {
      //LAB_8003e8f8
      v0 = v0 >> 1;
      s0 = v0 + 1;
      a0 = a0 << -s0 * 2;
    }

    //LAB_8003e90c
    s0 = s0 - 6;
    if(s0 < 0) {
      v0 = FUN_8003e760(a0) >> -s0;
    } else {
      //LAB_8003e92c
      v0 = FUN_8003e760(a0) << s0;
    }

    //LAB_8003e938
    return v0;
  }

  @Method(0x8003e958L)
  public static void InitGeom() {
    // cop2r61 = 0x155, 61 = ZSF3 - Z3 avg scale factor (normally 1/3)
    CPU.CTC2(0x155, 29);
    // cop2r62 = 0x100, 62 = ZSF4 - Z4 avg scale factor (normally 1/4)
    CPU.CTC2(0x100, 30);
    // cop2r58 = 0x3e8, 58 = H - Projection plane distance
    CPU.CTC2(1000, 26);
    // cop2r59 = 0xffffef9e, 59 = DQA - Depth queueing param A (coefficient)
    CPU.CTC2(0xffffef9e, 27);
    // cop2r60 = 0x1400000, 60 = DQB - Depth queueing param B (offset)
    CPU.CTC2(0x1400000, 28);
    // cop2r56 = 0, 56 = OFX - Screen offset X
    CPU.CTC2(0, 24);
    // cop2r57 = 0, 57 = OFY - Screen offset Y
    CPU.CTC2(0, 25);
  }

  @Method(0x8003ea80L)
  public static void FUN_8003ea80(final VECTOR a0, final VECTOR a1) {
    a1.set(a0);
    FUN_8003eae0(a1.x, a1.y, a1.z);
  }

  @Method(0x8003eae0L)
  public static void FUN_8003eae0(final IntRef t0, final IntRef t1, final IntRef t2) {
    final long v0;
    final long v1;
    long t3;
    long t4;
    long t5;
    long t6;
    CPU.MTC2(t0.get(),  9);
    CPU.MTC2(t1.get(), 10);
    CPU.MTC2(t2.get(), 11);
    CPU.COP2(0xa00428L);
    v0 = CPU.MFC2(25) + CPU.MFC2(26) + CPU.MFC2(27);
    CPU.MTC2(v0, 30);
    v1 = CPU.MFC2(31) & 0xffff_fffeL;
    t6 = 31 - v1;
    t6 = (int)t6 >> 1;
    t3 = v1 - 24;
    if(t3 >= 0) {
      t4 = v0 << t3;
    } else {
      //LAB_8003eb40
      t3 = 24 - v1;
      t4 = (int)v0 >> t3;
    }

    //LAB_8003eb4c
    t4 = t4 - 0x80L;
    t4 = t4 << 1;
    t5 = 0x8005_0000L; //TODO
    t5 = t5 + t4;
    t5 = MEMORY.ref(2, t5).offset(0x4870L).getSigned();

    CPU.MTC2(t5, 8);
    CPU.MTC2(t0.get(), 9);
    CPU.MTC2(t1.get(), 10);
    CPU.MTC2(t2.get(), 11);
    CPU.COP2(0x190003dL);
    t0.set((int)(CPU.MFC2(25) >> t6));
    t1.set((int)(CPU.MFC2(26) >> t6));
    t2.set((int)(CPU.MFC2(27) >> t6));
  }

  @Method(0x8003eba0L)
  public static void FUN_8003eba0(final MATRIX a0, final MATRIX a1) {
    final int t0 = a0.get(0);
    final int t1 = a0.get(1);
    final int t2 = a0.get(2);
    final int t3 = a0.get(3);
    final int t4 = a0.get(4);
    final int t5 = a0.get(5);
    final long v0 = CPU.CFC2(0);
    final long v1 = CPU.CFC2(2);
    final long a2 = CPU.CFC2(4);
    CPU.CTC2(t0, 0);
    CPU.CTC2(t1, 2);
    CPU.CTC2(t2, 4);
    CPU.MTC2(t3, 9);
    CPU.MTC2(t4, 10);
    CPU.MTC2(t5, 11);
    CPU.COP2(0x178000cL);
    final long t7 = CPU.MFC2(25);
    final long t8 = CPU.MFC2(26);
    final long t9 = CPU.MFC2(27);
    CPU.CTC2(t3, 0);
    CPU.CTC2(t4, 2);
    CPU.CTC2(t5, 4);
    CPU.COP2(0x178000cL);
    CPU.MTC2(t3, 0);
    CPU.MTC2(t4, 1);
    CPU.MTC2(t5, 2);
    final IntRef t0Ref = new IntRef().set((int)CPU.MFC2(25));
    final IntRef t1Ref = new IntRef().set((int)CPU.MFC2(26));
    final IntRef t2Ref = new IntRef().set((int)CPU.MFC2(27));
    CPU.CTC2(v0, 0);
    CPU.CTC2(v1, 2);
    CPU.CTC2(a2, 4);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(0, (short)t0Ref.get());
    a1.set(1, (short)t1Ref.get());
    a1.set(2, (short)t2Ref.get());
    t0Ref.set((int)CPU.MFC2(0));
    t1Ref.set((int)CPU.MFC2(1));
    t2Ref.set((int)CPU.MFC2(2));
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(3, (short)t0Ref.get());
    a1.set(4, (short)t1Ref.get());
    a1.set(5, (short)t2Ref.get());
    t0Ref.set((int)t7);
    t1Ref.set((int)t8);
    t2Ref.set((int)t9);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(6, (short)t0Ref.get());
    a1.set(7, (short)t1Ref.get());
    a1.set(8, (short)t2Ref.get());
  }

  @Method(0x8003ec90L)
  public static MATRIX FUN_8003ec90(final MATRIX a0, final MATRIX a1, final MATRIX a2) {
    CPU.CTC2(a0.getPacked(0), 0); //
    CPU.CTC2(a0.getPacked(2), 1); //
    CPU.CTC2(a0.getPacked(4), 2); // Rotation
    CPU.CTC2(a0.getPacked(6), 3); //
    CPU.CTC2(a0.getPacked(8), 4); //

    CPU.MTC2((a1.get(3) & 0xffff) << 16 | a1.get(0) & 0xffff, 0); // VXY0
    CPU.MTC2(a1.get(6), 1); // VZ0
    CPU.COP2(0x486012L);
    a2.set(0, (short)CPU.MFC2( 9)); // IR1
    a2.set(3, (short)CPU.MFC2(10)); // IR2
    a2.set(6, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((a1.get(4) & 0xffff) << 16 | a1.get(1) & 0xffff, 0); // VXY0
    CPU.MTC2(a1.get(7), 1); // VZ0
    CPU.COP2(0x486012L);
    a2.set(1, (short)CPU.MFC2( 9)); // IR1
    a2.set(4, (short)CPU.MFC2(10)); // IR2
    a2.set(7, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((a1.get(5) & 0xffff) << 16 | a1.get(2) & 0xffff, 0); // VXY0
    CPU.MTC2(a1.get(8), 1); // VZ0
    CPU.COP2(0x486012L);
    a2.set(2, (short)CPU.MFC2( 9)); // IR1
    a2.set(5, (short)CPU.MFC2(10)); // IR2
    a2.set(8, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((a1.transfer.getY() & 0xffff) << 16 | a1.transfer.getX(), 0); // VXY0
    CPU.MTC2(a1.transfer.getZ(), 1); // VZ0
    CPU.COP2(0x486012L);

    a2.transfer.set(a0.transfer);
    a2.transfer.x.add((int)CPU.MFC2(25));
    a2.transfer.y.add((int)CPU.MFC2(26));
    a2.transfer.z.add((int)CPU.MFC2(27));
    return a2;
  }

  /**
   * Multiplies matrix by vector beginning from the rightmost end. The result is saved in vector v1. It is a 16
   * x 32 bit multiplier which uses the GTE. It destroys the constant rotation matrix
   *
   * NOTE: a2 moved to return
   */
  @Method(0x8003edf0L)
  public static VECTOR ApplyMatrixLV(final MATRIX matrix, final VECTOR vector) {
    CPU.CTC2(matrix.getPacked(0), 0); //
    CPU.CTC2(matrix.getPacked(2), 1); //
    CPU.CTC2(matrix.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(matrix.getPacked(6), 3); //
    CPU.CTC2(matrix.getPacked(8), 4); //

    final int wholeX;
    final int fractX;
    if(vector.getX() < 0) {
      wholeX = -(-vector.getX() / 8 >> 12);
      fractX = -(-vector.getX() & 0x7fff);
    } else {
      //LAB_8003ee44
      wholeX = vector.getX() / 8 >> 12;
      fractX = vector.getX() & 0x7fff;
    }

    //LAB_8003ee4c
    final int wholeY;
    final int fractY;
    if(vector.getY() < 0) {
      wholeY = -(-vector.getY() / 8 >> 12);
      fractY = -(-vector.getY() & 0x7fff);
    } else {
      //LAB_8003ee6c
      wholeY = vector.getY() / 8 >> 12;
      fractY = vector.getY() & 0x7fff;
    }

    //LAB_8003ee74
    final int wholeZ;
    final int fractZ;
    if(vector.getZ() < 0) {
      wholeZ = -(-vector.getZ() / 8 >> 12);
      fractZ = -(-vector.getZ() & 0x7fff);
    } else {
      //LAB_8003ee94
      wholeZ = vector.getZ() / 8 >> 12;
      fractZ = vector.getZ() & 0x7fff;
    }

    //LAB_8003ee9c
    CPU.MTC2(wholeX,  9); // IR1
    CPU.MTC2(wholeY, 10); // IR2
    CPU.MTC2(wholeZ, 11); // IR3
    CPU.COP2(0x41_e012L); // Multiply IR by rotation, no fraction)
    final int x1 = (int)CPU.MFC2(25); // MAC1
    final int y1 = (int)CPU.MFC2(26); // MAC2
    final int z1 = (int)CPU.MFC2(27); // MAC3

    CPU.MTC2(fractX,  9); // IR1
    CPU.MTC2(fractY, 10); // IR2
    CPU.MTC2(fractZ, 11); // IR3
    CPU.COP2(0x49_e012L); // Multiply IR by rotation, 12-bit fraction
    final int x2 = (int)CPU.MFC2(25); // MAC1
    final int y2 = (int)CPU.MFC2(26); // MAC2
    final int z2 = (int)CPU.MFC2(27); // MAC3

    return new VECTOR().set(x2 + x1 * 8, y2 + y1 * 8, z2 + z1 * 8);
  }

  /** Transforms vec using the matrix already loaded into the GTE */
  @Method(0x8003ef50L)
  public static VECTOR ApplyRotMatrix(final SVECTOR vec, final VECTOR out) {
    CPU.MTC2(vec.getXY(), 0); // VXY0
    CPU.MTC2(vec.getZ(),  1); // VZ0
    CPU.COP2(0x48_6012L);
    out.setX((int)CPU.MFC2( 9)); // IR0
    out.setY((int)CPU.MFC2(10)); // IR1
    out.setZ((int)CPU.MFC2(11)); // IR2
    return out;
  }

  @Method(0x8003ef80L)
  public static void PushMatrix() {
    final int i = (int)matrixStackIndex_80054a08.get();

    if(i >= 640) {
      throw new RuntimeException("Error: Can't push matrix, stack(max 20) is full!");
    }

    //LAB_8003efc0
    final MATRIX matrix = matrixStack_80054a0c.get(i / 32);
    matrix.setPacked(0, CPU.CFC2(0)); //
    matrix.setPacked(2, CPU.CFC2(1)); //
    matrix.setPacked(4, CPU.CFC2(2)); // Rotation matrix
    matrix.setPacked(6, CPU.CFC2(3)); //
    matrix.setPacked(8, CPU.CFC2(4)); //
    matrix.transfer.x.set((int)CPU.CFC2(5)); //
    matrix.transfer.y.set((int)CPU.CFC2(6)); // Translation vector
    matrix.transfer.z.set((int)CPU.CFC2(7)); //

    matrixStackIndex_80054a08.addu(0x20L);
  }

  @Method(0x8003f024L)
  public static void PopMatrix() {
    int i = (int)matrixStackIndex_80054a08.get();

    if(i == 0) {
      throw new RuntimeException("Error: Can't pop matrix, stack is empty!");
    }

    //LAB_8003f060
    i -= 0x20L;
    matrixStackIndex_80054a08.subu(0x20L);

    final MATRIX matrix = matrixStack_80054a0c.get(i / 32);
    CPU.CTC2(matrix.getPacked(0), 0); //
    CPU.CTC2(matrix.getPacked(2), 1); //
    CPU.CTC2(matrix.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(matrix.getPacked(6), 3); //
    CPU.CTC2(matrix.getPacked(8), 4); //

    CPU.CTC2(matrix.transfer.x.get(), 5); //
    CPU.CTC2(matrix.transfer.y.get(), 6); // Translation vector
    CPU.CTC2(matrix.transfer.z.get(), 7); //
  }

  @Method(0x8003f0d0L)
  public static MATRIX ScaleMatrix(final MATRIX mat, final VECTOR vector) {
    mat.set(0, (short)(mat.get(0) * vector.x.get() >> 12));
    mat.set(1, (short)(mat.get(1) * vector.x.get() >> 12));
    mat.set(2, (short)(mat.get(2) * vector.x.get() >> 12));
    mat.set(3, (short)(mat.get(3) * vector.y.get() >> 12));
    mat.set(4, (short)(mat.get(4) * vector.y.get() >> 12));
    mat.set(5, (short)(mat.get(5) * vector.y.get() >> 12));
    mat.set(6, (short)(mat.get(6) * vector.z.get() >> 12));
    mat.set(7, (short)(mat.get(7) * vector.z.get() >> 12));
    mat.set(8, (short)(mat.get(8) * vector.z.get() >> 12));
    return mat;
  }

  @Method(0x8003f210L)
  public static MATRIX MulMatrix0(final MATRIX a0, final MATRIX a1, final MATRIX out) {
    final long t0;
    final long t1;
    final long t2;
    CPU.CTC2(a0.getPacked(0), 0);
    CPU.CTC2(a0.getPacked(2), 1);
    CPU.CTC2(a0.getPacked(4), 2);
    CPU.CTC2(a0.getPacked(6), 3);
    CPU.CTC2(a0.getPacked(8), 4);

    CPU.MTC2((a1.get(3) & 0xffff) << 16 | a1.get(0) & 0xffff, 0);
    CPU.MTC2(                             a1.get(6) & 0xffff, 1);
    CPU.COP2(0x486012L);
    out.set(0, (short)CPU.MFC2( 9));
    out.set(3, (short)CPU.MFC2(10));
    out.set(6, (short)CPU.MFC2(11));

    CPU.MTC2((a1.get(4) & 0xffff) << 16 | a1.get(1) & 0xffff, 0);
    CPU.MTC2(                             a1.get(7) & 0xffff, 1);
    CPU.COP2(0x486012L);
    out.set(1, (short)CPU.MFC2( 9));
    out.set(4, (short)CPU.MFC2(10));
    out.set(7, (short)CPU.MFC2(11));

    CPU.MTC2((a1.get(5) & 0xffff) << 16 | a1.get(2) & 0xffff, 0);
    CPU.MTC2(                             a1.get(8) & 0xffff, 1);
    CPU.COP2(0x486012L);
    out.set(2, (short)CPU.MFC2( 9));
    out.set(5, (short)CPU.MFC2(10));
    out.set(8, (short)CPU.MFC2(11));

    if(a1.transfer.getX() < 0) {
      t0 = -(-a1.transfer.getX() & 0x7fffL);
    } else {
      //LAB_8003f33c
      t0 = a1.transfer.getX() & 0x7fffL;
    }

    //LAB_8003f344
    if(a1.transfer.getY() < 0) {
      t1 = -(-a1.transfer.getY() & 0x7fffL);
    } else {
      //LAB_8003f364
      t1 = a1.transfer.getY() & 0x7fffL;
    }

    //LAB_8003f36c
    if(a1.transfer.getZ() < 0) {
      t2 = -(-a1.transfer.getZ() & 0x7fffL);
    } else {
      //LAB_8003f38c
      t2 = a1.transfer.getZ() & 0x7fffL;
    }

    //LAB_8003f394
    CPU.MTC2(a1.transfer.getX() >> 15,  9);
    CPU.MTC2(a1.transfer.getY() >> 15, 10);
    CPU.MTC2(a1.transfer.getZ() >> 15, 11);
    CPU.COP2(0x41e012L);
    final long t3 = CPU.MFC2(25);
    final long t4 = CPU.MFC2(26);
    final long t5 = CPU.MFC2(27);

    CPU.MTC2(t0,  9);
    CPU.MTC2(t1, 10);
    CPU.MTC2(t2, 11);
    CPU.COP2(0x49e012L);

    //LAB_8003f3e0
    //LAB_8003f3e4
    //LAB_8003f3fc
    //LAB_8003f400
    //LAB_8003f418
    //LAB_8003f41c
    out.transfer.setX((int)(CPU.MFC2(25) + t3 * 8 + a0.transfer.getX()));
    out.transfer.setY((int)(CPU.MFC2(26) + t4 * 8 + a0.transfer.getY()));
    out.transfer.setZ((int)(CPU.MFC2(27) + t5 * 8 + a0.transfer.getZ()));
    return out;
  }

  /**
   * Multiply two matrices. Multiplies m1 by m0. The result is stored in m0 and returned.
   *
   * @param m0 Matrix 0 - both modified and returned
   * @param m1 Matrix 1
   *
   * @return m0
   */
  @Method(0x8003f460L)
  public static MATRIX MulMatrix(final MATRIX m0, final MATRIX m1) {
    CPU.CTC2(m0.getPacked(0), 0); //
    CPU.CTC2(m0.getPacked(2), 1); //
    CPU.CTC2(m0.getPacked(4), 2); // Rotation matrix (3x3)
    CPU.CTC2(m0.getPacked(6), 3); //
    CPU.CTC2(m0.getPacked(8), 4); //

    CPU.MTC2((m1.get(3) & 0xffffL) << 16 | m1.get(0) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(6) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(0, (short)CPU.MFC2( 9)); // IR1
    m0.set(3, (short)CPU.MFC2(10)); // IR2
    m0.set(6, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((m1.get(4) & 0xffffL) << 16 | m1.get(1) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(7) & 0xffffL, 1); // VZ0
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(1, (short)CPU.MFC2( 9)); // IR1
    m0.set(4, (short)CPU.MFC2(10)); // IR2
    m0.set(7, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((m1.get(5) & 0xffffL) << 16 | m1.get(2) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(8) & 0xffffL, 1); // VZ0
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(2, (short)CPU.MFC2( 9)); // IR1
    m0.set(5, (short)CPU.MFC2(10)); // IR2
    m0.set(8, (short)CPU.MFC2(11)); // IR3

    return m0;
  }

  /**
   * Multiply two matrices. Multiplies m1 by m0. m1 is both modified and returned.
   *
   * @param m0 Matrix 0
   * @param m1 Matrix 1 - both modified and returned
   *
   * @return m1
   */
  @Method(0x8003f570L)
  public static MATRIX MulMatrix2(final MATRIX m0, final MATRIX m1) {
    CPU.CTC2(m0.getPacked(0), 0); //
    CPU.CTC2(m0.getPacked(2), 1); //
    CPU.CTC2(m0.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(m0.getPacked(6), 3); //
    CPU.CTC2(m0.getPacked(8), 4); //

    // First row
    CPU.MTC2((m1.get(3) & 0xffffL) << 16 | m1.get(0) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(6) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(0, (short)CPU.MFC2( 9)); // IR1
    m1.set(3, (short)CPU.MFC2(10)); // IR2
    m1.set(6, (short)CPU.MFC2(11)); // IR3

    // Second row
    CPU.MTC2((m1.get(4) & 0xffffL) << 16 | m1.get(1) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(7) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(1, (short)CPU.MFC2( 9)); // IR1
    m1.set(4, (short)CPU.MFC2(10)); // IR2
    m1.set(7, (short)CPU.MFC2(11)); // IR3

    // Third row
    CPU.MTC2((m1.get(5) & 0xffffL) << 16 | m1.get(2) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(8) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(2, (short)CPU.MFC2( 9)); // IR1
    m1.set(5, (short)CPU.MFC2(10)); // IR2
    m1.set(8, (short)CPU.MFC2(11)); // IR3

    return m1;
  }

  @Method(0x8003f680L)
  public static VECTOR ApplyMatrix(final MATRIX a0, final SVECTOR a1, final VECTOR out) {
    CPU.CTC2(a0.getPacked(0), 0);
    CPU.CTC2(a0.getPacked(2), 1);
    CPU.CTC2(a0.getPacked(4), 2);
    CPU.CTC2(a0.getPacked(6), 3);
    CPU.CTC2(a0.getPacked(8), 4);
    CPU.MTC2(a1.getXY(), 0);
    CPU.MTC2(a1.getZ(),  1);
    CPU.COP2(0x48_6012L); // Multiply V0 by rotation matrix, 12-bit fraction
    out.setX((int)CPU.MFC2(25)); // MAC0
    out.setY((int)CPU.MFC2(26)); // MAC1
    out.setZ((int)CPU.MFC2(27)); // MAC2
    return out;
  }

  @Method(0x8003f6d0L)
  public static SVECTOR ApplyMatrixSV(final MATRIX mat, final SVECTOR in, final SVECTOR out) {
    CPU.CTC2(mat.getPacked(0), 0); //
    CPU.CTC2(mat.getPacked(2), 1); //
    CPU.CTC2(mat.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(mat.getPacked(6), 3); //
    CPU.CTC2(mat.getPacked(8), 4); //
    CPU.MTC2(in.getXY(), 0); // VXY0
    CPU.MTC2(in.getZ(),  1); // VZ0

    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector

    out.setX((short)CPU.MFC2( 9)); // IR1
    out.setY((short)CPU.MFC2(10)); // IR2
    out.setZ((short)CPU.MFC2(11)); // IR3
    return out;
  }

  /**
   * Gives an amount of parallel transfer expressed by v to the matrix m.
   *
   * @param matrix Pointer to matrix (output)
   * @param vector Pointer to transfer vector (input)
   *
   * @return matrix
   */
  @Method(0x8003f730L)
  public static MATRIX TransMatrix(final MATRIX matrix, final VECTOR vector) {
    matrix.transfer.set(vector);
    return matrix;
  }

  @Method(0x8003f760L)
  public static MATRIX ScaleMatrixL(final MATRIX matrix, final VECTOR vector) {
    final long vx = vector.getX();
    final long vy = vector.getY();
    final long vz = vector.getZ();

    matrix.set(0, (short)(matrix.get(0) * vx >> 12));
    matrix.set(1, (short)(matrix.get(1) * vy >> 12));
    matrix.set(2, (short)(matrix.get(2) * vz >> 12));
    matrix.set(3, (short)(matrix.get(3) * vx >> 12));
    matrix.set(4, (short)(matrix.get(4) * vy >> 12));
    matrix.set(5, (short)(matrix.get(5) * vz >> 12));
    matrix.set(6, (short)(matrix.get(6) * vx >> 12));
    matrix.set(7, (short)(matrix.get(7) * vy >> 12));
    matrix.set(8, (short)(matrix.get(8) * vz >> 12));

    return matrix;
  }

  @Method(0x8003f8a0L)
  public static void getScreenOffset(final IntRef screenOffsetX, final IntRef screenOffsetY) {
    screenOffsetX.set((int)CPU.CFC2(24) >> 16);
    screenOffsetY.set((int)CPU.CFC2(25) >> 16);
  }

  @Method(0x8003f8c0L)
  public static int getProjectionPlaneDistance() {
    return (int)CPU.CFC2(26);
  }

  @Method(0x8003f8d0L)
  public static void SetFarColour(final int r, final int g, final int b) {
    CPU.CTC2(r << 4, 0x15);
    CPU.CTC2(g << 4, 0x16);
    CPU.CTC2(b << 4, 0x17);
  }

  @Method(0x8003f8f0L) //Also 0x8003c6d0
  public static void setProjectionPlaneDistance(final int distance) {
    CPU.CTC2(distance, 26);
  }

  /** Returns Z */
  @Method(0x8003f900L)
  public static int perspectiveTransform(final SVECTOR worldCoords, final DVECTOR screenCoords, @Nullable final Ref<Long> ir0, @Nullable final Ref<Long> flags) {
    CPU.MTC2(worldCoords.getXY(), 0);
    CPU.MTC2(worldCoords.getZ(),  1);
    CPU.COP2(0x18_0001L); // Perspective transform single
    screenCoords.setXY(CPU.MFC2(14)); // SXY2

    if(ir0 != null) {
      ir0.set(CPU.MFC2(8)); // IR0
    }

    if(flags != null) {
      flags.set(CPU.CFC2(31)); // Flags
    }

    return (int)CPU.MFC2(19) >> 2; // SZ3
  }

  @Method(0x8003f930L)
  public static int perspectiveTransformTriple(final SVECTOR world0, final SVECTOR world1, final SVECTOR world2, final DVECTOR screen0, final DVECTOR screen1, final DVECTOR screen2, @Nullable final Ref<Long> ir0, @Nullable final Ref<Long> flags) {
    CPU.MTC2(world0.getXY(), 0);
    CPU.MTC2(world0.getZ(),  1);
    CPU.MTC2(world1.getXY(), 2);
    CPU.MTC2(world1.getZ(),  3);
    CPU.MTC2(world2.getXY(), 4);
    CPU.MTC2(world2.getZ(),  5);

    CPU.COP2(0x280030L);

    screen0.setXY(CPU.MFC2(12));
    screen1.setXY(CPU.MFC2(13));
    screen2.setXY(CPU.MFC2(14));

    if(ir0 != null) {
      ir0.set(CPU.MFC2(8));
    }

    if(flags != null) {
      flags.set(CPU.CFC2(31));
    }

    return (int)CPU.MFC2(19) >> 2;
  }

  @Method(0x8003f990L)
  public static void RotTrans(final SVECTOR v0, final VECTOR out, @Nullable final UnsignedIntRef flags) {
    CPU.MTC2(v0.getXY(), 0); // VXY0
    CPU.MTC2(v0.getZ(), 1); // VZ0
    CPU.COP2(0x48_0012L); // MVMVA (translation=tr, mul vec=v0, mul mat=rot, 12-bit fraction)
    out.setX((int)CPU.MFC2(25)); // MAC1
    out.setY((int)CPU.MFC2(26)); // MAC2
    out.setZ((int)CPU.MFC2(27)); // MAC3

    if(flags != null) {
      flags.set(CPU.CFC2(31)); // Flags
    }
  }

  /**
   * <p>Perform coordinate and perspective transformation for 4 vertices.</p>
   *
   * <p>After transforming the four coordinate vectors v0, v1, v2, and v3 using a rotation matrix, the function
   * performs perspective transformation, and returns four screen coordinates sxy0, sxy1, sxy2, and sxy3. It
   * also returns an interpolation value for depth cueing to p corresponding to v3.</p>
   *
   * @param v0 Vector 0
   * @param v1 Vector 1
   * @param v2 Vector 2
   * @param v3 Vector 3
   * @param sxyz0 Screen coords 0 (out)
   * @param sxyz1 Screen coords 1 (out)
   * @param sxyz2 Screen coords 2 (out)
   * @param sxyz3 Screen coords 3 (out)
   * @param ir0 Interpolated value for depth cueing (out)
   * @param flags Flags (out)
   *
   * @return 1/4 of the Z component sz of the screen coordinates corresponding to v3.
   */
  @Method(0x8003f9c0L)
  public static int RotTransPers4(final SVECTOR v0, final SVECTOR v1, final SVECTOR v2, final SVECTOR v3, final SVECTOR sxyz0, final SVECTOR sxyz1, final SVECTOR sxyz2, final SVECTOR sxyz3, @Nullable final Ref<Long> ir0, @Nullable final Ref<Long> flags) {
    CPU.MTC2(v0.getXY(), 0); // VXY0
    CPU.MTC2(v0.getZ(),  1); // VZ0
    CPU.MTC2(v1.getXY(), 2); // VXY1
    CPU.MTC2(v1.getZ(),  3); // VZ1
    CPU.MTC2(v2.getXY(), 4); // VXY2
    CPU.MTC2(v2.getZ(),  5); // VZ2
    CPU.COP2(0x28_0030L); // Perspective transformation triple
    sxyz0.setXY(CPU.MFC2(12)); // SXY0
    sxyz1.setXY(CPU.MFC2(13)); // SXY1
    sxyz2.setXY(CPU.MFC2(14)); // SXY2
    final long flags1 = CPU.CFC2(31); // Flags

    CPU.MTC2(v3.getXY(), 0); // SXY0
    CPU.MTC2(v3.getZ(),  1); // SZ0
    CPU.COP2(0x18_0001L); // Perspective transformation single
    sxyz3.setXY(CPU.MFC2(14)); // SXY2

    if(ir0 != null) {
      ir0.set(CPU.MFC2(8)); // IR0
    }

    if(flags != null) {
      final long flags2 = CPU.CFC2(31); // Flags
      flags.set(flags2 | flags1);
    }

    return (int)(CPU.MFC2(19) >> 2); // SZ3
  }

  /**
   * Perform coordinate and perspective transformation.
   * <p>
   * Executes RotTransPers() for the number of vertices specified by n.<br>
   * The arguments and internal data formats are as follows:<br>
   * <pre>v0 -> vx, vy, vz : (1, 15, 0)</pre>
   * <pre>v1 -> vx, vy : (1, 15, 0)</pre>
   * <pre>sz : (0, 16, 0)</pre>
   * <pre>flag : (0, 16, 0)</pre>
   * The flag must normally be set between bits 27 and 12 of the 32-bit flag.
   *
   * @param v0 Pointer to vertex coordinate vector (input)
   * @param v1 Pointer to vertex coordinate vector (output)
   * @param sz Pointer to SZ value (output)
   * @param interpolation Pointer to interpolation value (output)
   * @param flag Pointer to flag (output)
   * @param count Number of vertices (output)
   */
  @Method(0x8003fa40L)
  public static void RotTransPersN(final SVECTOR[] v0, final DVECTOR[] v1, final UnsignedShortRef[] sz, final UnsignedShortRef[] interpolation, final UnsignedShortRef[] flag, final int count) {
    //LAB_8003fa4c
    for(int i = 0; i < count; i++) {
      CPU.MTC2(v0[i].getXY(), 0); // VXY0
      CPU.MTC2(v0[i].getZ(), 1); // VZ0

      CPU.COP2(0x18_0001L); // Perspective transform single

      v1[i] = new DVECTOR().setXY(CPU.MFC2(14)); // SXY2
      sz[i] = new UnsignedShortRef().set((int)CPU.MFC2(19)); // SZ3
      interpolation[i] = new UnsignedShortRef().set((int)CPU.MFC2( 8)); // IR0
      flag[i] = new UnsignedShortRef().set((int)(CPU.CFC2(31) >>> 12) & 0xffff); // Flag (see no$ "GTE Saturation")
    }
  }

  /**
   * Transposes matrix m0 into m1.
   *
   * @param m0 Input
   * @param m1 Output
   * @return m1
   */
  @Method(0x8003fab0L)
  public static MATRIX TransposeMatrix(final MATRIX m0, final MATRIX m1) {
    m1.set(0, m0.get(0));
    m1.set(1, m0.get(3));
    m1.set(2, m0.get(6));
    m1.set(3, m0.get(1));
    m1.set(4, m0.get(4));
    m1.set(5, m0.get(7));
    m1.set(6, m0.get(2));
    m1.set(7, m0.get(5));
    m1.set(8, m0.get(8));
    return m1;
  }

  /** TODO one of the RotMatrix* methods */
  @Method(0x8003faf0L)
  public static void RotMatrix_8003faf0(final SVECTOR a0, final MATRIX a1) {
    int sinCos;

    final int x = a0.getX();
    final short sinX;
    if(x < 0) {
      //LAB_8003fb0c
      sinCos = (int)sin_cos_80054d0c.offset((-x & 0xfff) * 4).get();
      sinX = (short)-(short)sinCos;
    } else {
      //LAB_8003fb34
      sinCos = (int)sin_cos_80054d0c.offset((x & 0xfff) * 4).get();
      sinX = (short)sinCos;
    }

    final short cosX = (short)(sinCos >> 16);

    //LAB_8003fb54
    final int y = a0.getY();
    final short sinYN;
    final short sinYP;
    if(y < 0) {
      //LAB_8003fb70
      sinCos = (int)sin_cos_80054d0c.offset((-y & 0xfff) * 4).get();
      sinYN = (short)sinCos;
      sinYP = (short)-(short)sinCos;
    } else {
      //LAB_8003fb98
      sinCos = (int)sin_cos_80054d0c.offset((y & 0xfffL) * 4).get();
      sinYN = (short)-(short)sinCos;
      sinYP = (short)sinCos;
    }

    final short cosY = (short)(sinCos >> 16);

    //LAB_8003fbbc
    final int z = a0.getZ();
    final short sinZ;
    if(z < 0) {
      //LAB_8003fbfc
      sinCos = (int)sin_cos_80054d0c.offset((-z & 0xfff) * 4).get();
      sinZ = (short)-(short)sinCos;
    } else {
      //LAB_8003fc24
      sinCos = (int)sin_cos_80054d0c.offset((z & 0xfff) * 4).get();
      sinZ = (short)sinCos;
    }

    final short cosZ = (short)(sinCos >> 16);

    //LAB_8003fc50
    a1.set(0, (short)(cosZ * cosY >> 12));
    a1.set(1, (short)(-(sinZ * cosY) >> 12));
    a1.set(2, sinYP);
    a1.set(3, (short)((sinZ * cosX >> 12) - ((cosZ * sinYN >> 12) * sinX >> 12)));
    a1.set(4, (short)((cosZ * cosX >> 12) + ((sinZ * sinYN >> 12) * sinX >> 12)));
    a1.set(5, (short)(-(cosY * sinX) >> 12));
    a1.set(6, (short)((sinZ * sinX >> 12) + ((cosZ * sinYN >> 12) * cosX >> 12)));
    a1.set(7, (short)((cosZ * sinX >> 12) - ((sinZ * sinYN >> 12) * cosX >> 12)));
    a1.set(8, (short)(cosY * cosX >> 12));
  }

  /** TODO one of the RotMatrix* methods */
  @Method(0x8003fd80L)
  public static void RotMatrix_8003fd80(final SVECTOR svec, final MATRIX mat) {
    final int x = svec.getX();
    final int sinCosX;
    final int sinX;
    final int negSinX;
    if(x >= 0) {
      //LAB_8003fdc4
      sinCosX = (int)sin_cos_80054d0c.offset((x & 0xfff) * 0x4L).get();
      sinX = (short)sinCosX;
      negSinX = -sinX;
    } else {
      sinCosX = (int)sin_cos_80054d0c.offset((-x & 0xfff) * 0x4L).get();
      negSinX = (short)sinCosX;
      sinX = -negSinX;
    }
    final int cosX = sinCosX >> 16;

    //LAB_8003fde8
    final int y = svec.getY();
    final int sinCosY;
    final int sinY;
    if(y >= 0) {
      //LAB_8003fe2c
      sinCosY = (int)sin_cos_80054d0c.offset((y & 0xfff) * 0x4L).get();
      sinY = (short)sinCosY;
    } else {
      sinCosY = (int)sin_cos_80054d0c.offset((-y & 0xfff) * 0x4L).get();
      sinY = -(short)sinCosY;
    }
    final int cosY = sinCosY >> 16;

    //LAB_8003fe4c
    mat.set(5, (short)negSinX);
    mat.set(2, (short)(sinY * cosX >> 12));
    mat.set(8, (short)(cosY * cosX >> 12));

    final int z = svec.getZ();
    final int sinCosZ;
    final int sinZ;
    if(z >= 0) {
      //LAB_8003feb4
      sinCosZ = (int)sin_cos_80054d0c.offset((z & 0xfff) * 0x4L).get();
      sinZ = (short)sinCosZ;
    } else {
      sinCosZ = (int)sin_cos_80054d0c.offset((-z & 0xfff) * 0x4L).get();
      sinZ = -(short)sinCosZ;
    }
    final int cosZ = sinCosZ >> 16;

    //LAB_8003fee0
    mat.set(3, (short)(sinZ * cosX >> 12));
    mat.set(4, (short)(cosZ * cosX >> 12));
    mat.set(0, (short)((cosY * cosZ >> 12) + ((sinY * sinX >> 12) * sinZ >> 12)));
    mat.set(1, (short)(-(cosY * sinZ >> 12) + ((sinY * sinX >> 12) * cosZ >> 12)));
    mat.set(7, (short)((sinY * sinZ >> 12) + ((cosY * sinX >> 12) * cosZ >> 12)));
    mat.set(6, (short)(-(sinY * cosZ >> 12) + ((cosY * sinX >> 12) * sinZ >> 12)));
  }
}
