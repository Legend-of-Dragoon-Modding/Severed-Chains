package legend.game;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandFillVram;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.game.types.DR_TPAGE;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8002.SetColorMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetLightMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8005._800546c0;
import static legend.game.Scus94491BpeSegment_8005._800546c2;
import static legend.game.Scus94491BpeSegment_8005._80054870;
import static legend.game.Scus94491BpeSegment_8005.matrixStackIndex_80054a08;
import static legend.game.Scus94491BpeSegment_8005.matrixStack_80054a0c;
import static legend.game.Scus94491BpeSegment_800c.PSDCNT_800c34d0;
import static legend.game.Scus94491BpeSegment_800c.coord2s_800c35a8;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.identityAspectMatrix_800c3588;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3528;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public final class Scus94491BpeSegment_8003 {
  private Scus94491BpeSegment_8003() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8003.class);

  @Method(0x80038190L)
  public static void ResetGraph() {
    GPU.resetCommandBuffer();

    _800546c0.setu(1024);
    _800546c2.setu(512);
  }

  @Method(0x80038574L)
  private static void validateRect(final String text, final RECT rect) {
    if(rect.w.get() > _800546c0.get() || rect.x.get() + rect.w.get() > _800546c0.get() || rect.y.get() > _800546c2.get() || rect.y.get() + rect.h.get() > _800546c2.get() || rect.w.get() < 0 || rect.x.get() < 0 || rect.y.get() < 0 || rect.h.get() < 0) {
      LOGGER.warn("%s:bad RECT", text);
      LOGGER.warn("(%d,%d)-(%d,%d)", rect.x.get(), rect.y.get(), rect.w.get(), rect.h.get());
    }
  }

  @Method(0x800387b8L)
  public static void LoadImage(final RECT rect, final long address) {
    validateRect("LoadImage", rect);

    GPU.commandA0CopyRectFromCpuToVram(rect, address);
  }

  public static void LoadImage(final RECT rect, final FileData data) {
    validateRect("LoadImage", rect);

    GPU.uploadData(rect, data);
  }

  public static long StoreImage(final RECT rect, final FileData data) {
    validateRect("StoreImage", rect);

    rect.w.set(MathHelper.clamp(rect.w.get(), (short)0, (short)_800546c0.get()));
    rect.h.set(MathHelper.clamp(rect.h.get(), (short)0, (short)_800546c2.get()));

    if(rect.w.get() <= 0 || rect.h.get() <= 0) {
      throw new IllegalArgumentException("RECT width and height must be greater than 0");
    }

    GPU.commandC0CopyRectFromVramToCpu(rect, data);

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

  /**
   * <p>Initialize the graphics system.</p>
   *
   * <p>Resets libgpu and initializes the libgs graphic system.</p>
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
   */
  @Method(0x8003bc30L)
  public static void GsInitGraph(final int displayWidth, final int displayHeight) {
    GsInitGraph2(displayWidth, displayHeight);
    GsInit3D();

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
  public static void GsInitGraph2(final int displayWidth, final int displayHeight) {
    ResetGraph();

    GPU.drawingOffset(0, 0);
    GPU.displayMode(displayWidth, displayHeight);
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
   */
  @Method(0x8003c048L)
  public static void GsSortClear(final int r, final int g, final int b) {
    GPU.queueCommand(orderingTableSize_1f8003c8.get() - 1, new GpuCommandFillVram(r, g, b));
  }

  @Method(0x8003c1c0L)
  public static void GsSetDrawBuffOffset() {
    final short x = centreScreenX_1f8003dc.get();
    final short y = centreScreenY_1f8003de.get();

    GPU.drawingOffset(x, y);
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
    final int clipX = displayRect_800c34c8.x.get();
    final int clipY = displayRect_800c34c8.y.get();
    final int clipW = displayRect_800c34c8.w.get();
    final int clipH = displayRect_800c34c8.h.get();

    GPU.drawingArea(clipX, clipY, clipW, clipH);
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
   * <li>Sets libgpu offset</li>
   * <li>Sets libgs offset</li></ul></p>
   *
   * <p>This function does not execute correctly when GPU drawing is in progress, so it is necessary to call this
   * function after terminating drawing using ResetGraph (1).</p>
   */
  @Method(0x8003c350L)
  public static void GsSwapDispBuff() {
    // GsIncFrame macro
    PSDCNT_800c34d0++;
    if(PSDCNT_800c34d0 < 0) {
      PSDCNT_800c34d0 = 1;
    }
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

  @Method(0x8003c5e0L)
  public static void setDrawOffset() {
    centreScreenX_1f8003dc.set((short)(displayWidth_1f8003e0.get() / 2));
    centreScreenY_1f8003de.set((short)(displayHeight_1f8003e4.get() / 2));
    GsSetDrawBuffOffset();
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
  public static int GsSetFlatLight(final int id, final GsF_LIGHT light) {
    final int x = light.direction_00.getX();
    final int y = light.direction_00.getY();
    final int z = light.direction_00.getZ();
    final int r = light.r_0c;
    final int g = light.g_0d;
    final int b = light.b_0e;

    // Normalize vector - calculate magnitude
    final long mag = SquareRoot0(x * x + y * y + z * z);

    if(mag == 0) {
      return -1;
    }

    lightDirectionMatrix_800c34e8.set(lightDirectionMatrix_800c34e8);

    if(id == 0) {
      //LAB_8003c7ec
      lightDirectionMatrix_800c34e8.set(0, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(1, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(2, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.set(0, (short)((r << 12) / 0xff));
      lightColourMatrix_800c3508.set(3, (short)((g << 12) / 0xff));
      lightColourMatrix_800c3508.set(6, (short)((b << 12) / 0xff));
    } else if(id == 1) {
      //LAB_8003c904
      lightDirectionMatrix_800c34e8.set(3, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(4, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(5, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.set(1, (short)((r << 12) / 0xff));
      lightColourMatrix_800c3508.set(4, (short)((g << 12) / 0xff));
      lightColourMatrix_800c3508.set(7, (short)((b << 12) / 0xff));
      //LAB_8003c7dc
    } else if(id == 2) {
      //LAB_8003ca20
      lightDirectionMatrix_800c34e8.set(6, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(7, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(8, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.set(2, (short)((r << 12) / 0xff));
      lightColourMatrix_800c3508.set(5, (short)((g << 12) / 0xff));
      lightColourMatrix_800c3508.set(8, (short)((b << 12) / 0xff));
    }

    //LAB_8003cb34
    SetColorMatrix(lightColourMatrix_800c3508);

    //LAB_8003cb88
    return 0;
  }

  @Method(0x8003cce0L)
  public static void GsSetAmbient(final int r, final int g, final int b) {
    GTE.setBackgroundColour(r, g, b);
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
  }

  @Method(0x8003cdf0L)
  public static TimHeader parseTimHeader(final Value baseAddress) {
    final TimHeader header = new TimHeader();
    header.flags = (int)baseAddress.offset(4, 0x0L).get();

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
    ApplyMatrixLV(worldToScreenMatrix_800c3548, new VECTOR().set(struct.viewpoint_00).negate(), worldToScreenMatrix_800c3548.transfer);

    if(struct.super_1c != null) {
      final MATRIX lw = new MATRIX();
      GsGetLw(struct.super_1c, lw);

      final MATRIX transposedLw = new MATRIX();
      TransposeMatrix(lw, transposedLw);
      ApplyMatrixLV(transposedLw, lw.transfer, transposedLw.transfer).negate();
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
    ApplyMatrixLV(matrix1, matrix2.transfer, matrix2.transfer);
    MulMatrix2(matrix1, matrix2);
    matrix2.transfer.add(matrix1.transfer);
  }

  @Method(0x8003d5d0L)
  public static void FUN_8003d5d0(final MATRIX matrix, final int angle) {
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
    a1 = 12 - GTE.leadingZeroCount(s1);
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
    a1 = 12 - GTE.leadingZeroCount(s1);

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
      a1 = 12 - GTE.leadingZeroCount(s1);
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
      a1 = 12 - GTE.leadingZeroCount(s1);
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
    ApplyMatrixLV(worldToScreenMatrix_800c3548, new VECTOR().set(s2.viewpoint_00).negate(), worldToScreenMatrix_800c3548.transfer);

    if(s2.super_1c != null) {
      final MATRIX lw = new MATRIX();
      GsGetLw(s2.super_1c, lw);

      final MATRIX transposedLw = new MATRIX();
      TransposeMatrix(lw, transposedLw);
      ApplyMatrixLV(transposedLw, lw.transfer, transposedLw.transfer).negate();
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
  public static void updateTmdPacketIlen(final FileData primitives, final int count) {
    int primitivesSinceLastChange = 0;
    int mode = 0;
    int flag = 0;

    int packetIndex = 0;
    int packetStartIndex = 0;

    //LAB_8003e638
    for(int primitiveIndex = 0; primitiveIndex < count; primitiveIndex++) {
      final int previousMode = mode;
      final int previousFlag = flag;

      // Primitive: mode, flag, ilen, olen
      final int primitive = primitives.readInt(packetIndex);

      mode = primitive >>> 24 & 0xff;
      flag = primitive >>> 16 & 0xff;

      if(previousMode != 0) {
        if(mode != previousMode || flag != previousFlag) {
          //LAB_8003e668
          primitives.writeShort(packetStartIndex, primitivesSinceLastChange);
          primitivesSinceLastChange = 0;
          packetStartIndex = packetIndex;
        }
      }

      //LAB_8003e674
      //LAB_8003e678
      switch(mode & 0xfd) {
        case 0x20: // setPolyF3
          if((flag & 0x4) == 0) {
            packetIndex += 0x10;
            break;
          }

        case 0x31:
        case 0x24: // setPolyFT3
          packetIndex += 0x18;
          break;

        case 0x30: // setPolyG3
          if((flag & 0x4) == 0) {
            packetIndex += 0x14;
            break;
          }

        case 0x34: // setPolyGT3
        case 0x39:
        case 0x25:
          packetIndex += 0x1c;
          break;

        case 0x28: // setPolyF4
          if((flag & 0x4L) == 0) {
            packetIndex += 0x14;
            break;
          }

        case 0x2d:
        case 0x2c: // setPolyFT4
          packetIndex += 0x20;
          break;

        case 0x29:
        case 0x21:
          packetIndex += 0x10;
          break;

        case 0x3d:
          packetIndex += 0x2c;
          break;

        case 0x38: // setPolyG4
          if((flag & 0x4) == 0) {
            packetIndex += 0x18;
            break;
          }

        case 0x3c: // setPolyGT4
        case 0x35:
          packetIndex += 0x24;
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
      primitivesSinceLastChange++;
    }

    //LAB_8003e724
    primitives.writeShort(packetStartIndex, primitivesSinceLastChange);
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
    int v0 = 8 - GTE.leadingZeroCount(a0);
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
    // Fix retail bug where inputs can all be 0. Would result in negative array index.
    if(t0.get() == 0 && t1.get() == 0 && t2.get() == 0) {
      return;
    }

    GTE.setIr123(t0.get(), t1.get(), t2.get());
    CPU.COP2(0xa00428L); // Square of vector IR123, saturate IR123
    final int vectorLength = GTE.getMac1() + GTE.getMac2() + GTE.getMac3();
    final int lzc = GTE.leadingZeroCount(vectorLength) & 0xffff_fffe; // Leading zero count
    final int t6 = (31 - lzc) / 2;
    final int t4;
    if(lzc >= 24) {
      t4 = vectorLength << (lzc - 24);
    } else {
      //LAB_8003eb40
      t4 = vectorLength >> (24 - lzc);
    }

    //LAB_8003eb4c
    GTE.setIr0(_80054870.get(t4 - 0x40).get());
    GTE.setIr123(t0.get(), t1.get(), t2.get());
    CPU.COP2(0x190003dL); // General purpose interpolation (MAC123 = IR123 * IR0 >> 12)
    t0.set(GTE.getMac1() >> t6);
    t1.set(GTE.getMac2() >> t6);
    t2.set(GTE.getMac3() >> t6);
  }

  @Method(0x8003eba0L)
  public static void FUN_8003eba0(final MATRIX a0, final MATRIX a1) {
    final short oldRot11 = GTE.getRotationMatrixValue(0);
    final short oldRot22 = GTE.getRotationMatrixValue(4);
    final short oldRot33 = GTE.getRotationMatrixValue(8);

    GTE.setIr123(a0.get(3), a0.get(4), a0.get(5)); // transforms forward?

    GTE.setRotationMatrixValue(0, a0.get(0)); // r11 // transforms right?
    GTE.setRotationMatrixValue(4, a0.get(1)); // r22
    GTE.setRotationMatrixValue(8, a0.get(2)); // r33
    CPU.COP2(0x178000cL); // outer product of two vectors (sf 12), (IR3*R22-IR2*R33, IR1*R33-IR3*R11, IR2*R11-IR1*R22) >> 12
    final int productX0 = GTE.getMac1();
    final int productY0 = GTE.getMac2();
    final int productZ0 = GTE.getMac3();

    GTE.setRotationMatrixValue(0, a0.get(3)); // r11 // transforms forward?
    GTE.setRotationMatrixValue(4, a0.get(4)); // r22
    GTE.setRotationMatrixValue(8, a0.get(5)); // r33
    CPU.COP2(0x178000cL); // outer product of two vectors (sf 12), (IR3*R22-IR2*R33, IR1*R33-IR3*R11, IR2*R11-IR1*R22) >> 12
    final int productX1 = GTE.getMac1();
    final int productY1 = GTE.getMac2();
    final int productZ1 = GTE.getMac3();

    GTE.setRotationMatrixValue(0, oldRot11);
    GTE.setRotationMatrixValue(4, oldRot22);
    GTE.setRotationMatrixValue(8, oldRot33);

    final IntRef t0Ref = new IntRef().set(productX1);
    final IntRef t1Ref = new IntRef().set(productY1);
    final IntRef t2Ref = new IntRef().set(productZ1);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(0, (short)t0Ref.get());
    a1.set(1, (short)t1Ref.get());
    a1.set(2, (short)t2Ref.get());

    t0Ref.set(a0.get(3));
    t1Ref.set(a0.get(4));
    t2Ref.set(a0.get(5));
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(3, (short)t0Ref.get());
    a1.set(4, (short)t1Ref.get());
    a1.set(5, (short)t2Ref.get());

    t0Ref.set(productX0);
    t1Ref.set(productY0);
    t2Ref.set(productZ0);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(6, (short)t0Ref.get());
    a1.set(7, (short)t1Ref.get());
    a1.set(8, (short)t2Ref.get());
  }

  @Method(0x8003ec90L)
  public static MATRIX FUN_8003ec90(final MATRIX transformMatrix0, final MATRIX transformMatrix1, final MATRIX outMatrix) {
    GTE.setRotationMatrix(transformMatrix0);

    GTE.setVertex(0, transformMatrix1.get(0), transformMatrix1.get(3), transformMatrix1.get(6));
    CPU.COP2(0x486012L);
    outMatrix.set(0, GTE.getIr1());
    outMatrix.set(3, GTE.getIr2());
    outMatrix.set(6, GTE.getIr3());

    GTE.setVertex(0, transformMatrix1.get(1), transformMatrix1.get(4), transformMatrix1.get(7));
    CPU.COP2(0x486012L);
    outMatrix.set(1, GTE.getIr1());
    outMatrix.set(4, GTE.getIr2());
    outMatrix.set(7, GTE.getIr3());

    GTE.setVertex(0, transformMatrix1.get(2), transformMatrix1.get(5), transformMatrix1.get(8));
    CPU.COP2(0x486012L);
    outMatrix.set(2, GTE.getIr1());
    outMatrix.set(5, GTE.getIr2());
    outMatrix.set(8, GTE.getIr3());

    GTE.setVertex(0, transformMatrix1.transfer);
    CPU.COP2(0x486012L);

    outMatrix.transfer.set(transformMatrix0.transfer);
    outMatrix.transfer.x.add(GTE.getMac1());
    outMatrix.transfer.y.add(GTE.getMac2());
    outMatrix.transfer.z.add(GTE.getMac3());
    return outMatrix;
  }

  public static VECTOR ApplyMatrixLV(final MATRIX matrix, final VECTOR vector) {
    return ApplyMatrixLV(matrix, vector, null);
  }

  /**
   * Multiplies matrix by vector beginning from the rightmost end. The result is saved in vector v1. It is a 16
   * x 32 bit multiplier which uses the GTE. It destroys the constant rotation matrix
   */
  @Method(0x8003edf0L)
  public static VECTOR ApplyMatrixLV(final MATRIX matrix, final VECTOR vector, @Nullable VECTOR out) {
    GTE.setRotationMatrix(matrix);

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
    GTE.setIr123(wholeX, wholeY, wholeZ);
    CPU.COP2(0x41_e012L); // Multiply IR by rotation, no fraction)
    final int x1 = GTE.getMac1();
    final int y1 = GTE.getMac2();
    final int z1 = GTE.getMac3();

    GTE.setIr123(fractX, fractY, fractZ);
    CPU.COP2(0x49_e012L); // Multiply IR by rotation, 12-bit fraction
    final int x2 = GTE.getMac1();
    final int y2 = GTE.getMac2();
    final int z2 = GTE.getMac3();

    if(out == null) {
      out = new VECTOR();
    }

    return out.set(x2 + x1 * 8, y2 + y1 * 8, z2 + z1 * 8);
  }

  /** Transforms vec using the matrix already loaded into the GTE */
  @Method(0x8003ef50L)
  public static VECTOR ApplyRotMatrix(final SVECTOR vec, final VECTOR out) {
    GTE.setVertex(0, vec);
    CPU.COP2(0x48_6012L);
    out.setX(GTE.getIr1());
    out.setY(GTE.getIr2());
    out.setZ(GTE.getIr3());
    return out;
  }

  @Method(0x8003ef80L)
  public static void PushMatrix() {
    final int i = (int)matrixStackIndex_80054a08.get();

    if(i >= 640) {
      throw new RuntimeException("Error: Can't push matrix, stack(max 20) is full!");
    }

    //LAB_8003efc0
    final MATRIX matrix = matrixStack_80054a0c[i / 32];
    GTE.getRotationMatrix(matrix);
    GTE.getTranslationVector(matrix.transfer);

    matrixStackIndex_80054a08.addu(0x20L);
  }

  @Method(0x8003f024L)
  public static void PopMatrix() {
    int i = (int)matrixStackIndex_80054a08.get();

    if(i == 0) {
      throw new RuntimeException("Error: Can't pop matrix, stack is empty!");
    }

    //LAB_8003f060
    i -= 0x20;
    matrixStackIndex_80054a08.subu(0x20L);

    final MATRIX matrix = matrixStack_80054a0c[i / 32];
    GTE.setRotationMatrix(matrix);
    GTE.setTranslationVector(matrix.transfer);
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
    GTE.setRotationMatrix(a0);

    GTE.setVertex(0, a1.get(0), a1.get(3), a1.get(6));
    CPU.COP2(0x486012L);
    out.set(0, GTE.getIr1());
    out.set(3, GTE.getIr2());
    out.set(6, GTE.getIr3());

    GTE.setVertex(0, a1.get(1), a1.get(4), a1.get(7));
    CPU.COP2(0x486012L);
    out.set(1, GTE.getIr1());
    out.set(4, GTE.getIr2());
    out.set(7, GTE.getIr3());

    GTE.setVertex(0, a1.get(2), a1.get(5), a1.get(8));
    CPU.COP2(0x486012L);
    out.set(2, GTE.getIr1());
    out.set(5, GTE.getIr2());
    out.set(8, GTE.getIr3());

    final int transferX;
    final int t0;
    if(a1.transfer.getX() < 0) {
      transferX = -(-a1.transfer.getX() >> 15);
      t0 = -(-a1.transfer.getX() & 0x7fff);
    } else {
      //LAB_8003f33c
      transferX = a1.transfer.getX() >> 15;
      t0 = a1.transfer.getX() & 0x7fff;
    }

    //LAB_8003f344
    final int transferY;
    final int t1;
    if(a1.transfer.getY() < 0) {
      transferY = -(-a1.transfer.getY() >> 15);
      t1 = -(-a1.transfer.getY() & 0x7fff);
    } else {
      //LAB_8003f364
      transferY = a1.transfer.getY() >> 15;
      t1 = a1.transfer.getY() & 0x7fff;
    }

    //LAB_8003f36c
    final int transferZ;
    final int t2;
    if(a1.transfer.getZ() < 0) {
      transferZ = -(-a1.transfer.getZ() >> 15);
      t2 = -(-a1.transfer.getZ() & 0x7fff);
    } else {
      //LAB_8003f38c
      transferZ = a1.transfer.getZ() >> 15;
      t2 = a1.transfer.getZ() & 0x7fff;
    }

    //LAB_8003f394
    GTE.setIr123(transferX, transferY, transferZ);
    CPU.COP2(0x41e012L);
    final int t3 = GTE.getMac1();
    final int t4 = GTE.getMac2();
    final int t5 = GTE.getMac3();

    GTE.setIr123(t0, t1, t2);
    CPU.COP2(0x49e012L);

    //LAB_8003f3e0
    //LAB_8003f3e4
    //LAB_8003f3fc
    //LAB_8003f400
    //LAB_8003f418
    //LAB_8003f41c
    out.transfer.setX(GTE.getMac1() + t3 * 8 + a0.transfer.getX());
    out.transfer.setY(GTE.getMac2() + t4 * 8 + a0.transfer.getY());
    out.transfer.setZ(GTE.getMac3() + t5 * 8 + a0.transfer.getZ());
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
    GTE.setRotationMatrix(m0);

    GTE.setVertex(0, m1.get(0), m1.get(3), m1.get(6));
    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(0, GTE.getIr1());
    m0.set(3, GTE.getIr2());
    m0.set(6, GTE.getIr3());

    GTE.setVertex(0, m1.get(1), m1.get(4), m1.get(7));
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(1, GTE.getIr1());
    m0.set(4, GTE.getIr2());
    m0.set(7, GTE.getIr3());

    GTE.setVertex(0, m1.get(2), m1.get(5), m1.get(8));
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(2, GTE.getIr1());
    m0.set(5, GTE.getIr2());
    m0.set(8, GTE.getIr3());

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
    GTE.setRotationMatrix(m0);

    // First row
    GTE.setVertex(0, m1.get(0), m1.get(3), m1.get(6));
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(0, GTE.getIr1());
    m1.set(3, GTE.getIr2());
    m1.set(6, GTE.getIr3());

    // Second row
    GTE.setVertex(0, m1.get(1), m1.get(4), m1.get(7));
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(1, GTE.getIr1());
    m1.set(4, GTE.getIr2());
    m1.set(7, GTE.getIr3());

    // Third row
    GTE.setVertex(0, m1.get(2), m1.get(5), m1.get(8));
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(2, GTE.getIr1());
    m1.set(5, GTE.getIr2());
    m1.set(8, GTE.getIr3());

    return m1;
  }

  @Method(0x8003f680L)
  public static VECTOR ApplyMatrix(final MATRIX a0, final SVECTOR a1, final VECTOR out) {
    GTE.setRotationMatrix(a0);
    GTE.setVertex(0, a1);
    CPU.COP2(0x48_6012L); // Multiply V0 by rotation matrix, 12-bit fraction
    out.setX(GTE.getMac1());
    out.setY(GTE.getMac2());
    out.setZ(GTE.getMac3());
    return out;
  }

  @Method(0x8003f6d0L)
  public static SVECTOR ApplyMatrixSV(final MATRIX mat, final SVECTOR in, final SVECTOR out) {
    GTE.setRotationMatrix(mat);
    GTE.setVertex(0, in);

    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector

    out.setX(GTE.getIr1());
    out.setY(GTE.getIr2());
    out.setZ(GTE.getIr3());
    return out;
  }

  /**
   * Gives an amount of parallel transfer expressed by v to the matrix m.
   *
   * @param out Pointer to matrix (output)
   * @param vector Pointer to transfer vector (input)
   *
   * @return matrix
   */
  @Method(0x8003f730L)
  public static MATRIX TransMatrix(final MATRIX out, final VECTOR vector) {
    out.transfer.set(vector);
    return out;
  }

  @Method(0x8003f760L)
  public static MATRIX ScaleMatrixL(final MATRIX out, final VECTOR vector) {
    final int vx = vector.getX();
    final int vy = vector.getY();
    final int vz = vector.getZ();

    out.set(0, (short)(out.get(0) * vx >> 12));
    out.set(1, (short)(out.get(1) * vy >> 12));
    out.set(2, (short)(out.get(2) * vz >> 12));
    out.set(3, (short)(out.get(3) * vx >> 12));
    out.set(4, (short)(out.get(4) * vy >> 12));
    out.set(5, (short)(out.get(5) * vz >> 12));
    out.set(6, (short)(out.get(6) * vx >> 12));
    out.set(7, (short)(out.get(7) * vy >> 12));
    out.set(8, (short)(out.get(8) * vz >> 12));

    return out;
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
    CPU.CTC2(r << 4, 21);
    CPU.CTC2(g << 4, 22);
    CPU.CTC2(b << 4, 23);
  }

  @Method(0x8003f8f0L) //Also 0x8003c6d0
  public static void setProjectionPlaneDistance(final int distance) {
    CPU.CTC2(distance, 26);
  }

  /** Returns Z */
  @Method(0x8003f900L)
  public static int perspectiveTransform(final SVECTOR worldCoords, final DVECTOR screenCoords) {
    GTE.perspectiveTransform(worldCoords);
    screenCoords.set(GTE.getScreenX(2), GTE.getScreenY(2));
    return GTE.getScreenZ(3) >> 2;
  }

  @Method(0x8003f930L)
  public static int perspectiveTransformTriple(final SVECTOR world0, final SVECTOR world1, final SVECTOR world2, final DVECTOR screen0, final DVECTOR screen1, final DVECTOR screen2) {
    GTE.perspectiveTransformTriangle(world0, world1, world2);

    screen0.set(GTE.getScreenX(0), GTE.getScreenY(0));
    screen1.set(GTE.getScreenX(1), GTE.getScreenY(1));
    screen2.set(GTE.getScreenX(2), GTE.getScreenY(2));

    return GTE.getScreenZ(3) >> 2;
  }

  @Method(0x8003f990L)
  public static void RotTrans(final SVECTOR v0, final VECTOR out) {
    GTE.setVertex(0, v0);
    CPU.COP2(0x48_0012L); // MVMVA (translation=tr, mul vec=v0, mul mat=rot, 12-bit fraction)
    out.setX(GTE.getMac1());
    out.setY(GTE.getMac2());
    out.setZ(GTE.getMac3());
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
   * @param sxy0 Screen coords 0 (out)
   * @param sxy1 Screen coords 1 (out)
   * @param sxy2 Screen coords 2 (out)
   * @param sxy3 Screen coords 3 (out)
   *
   * @return 1/4 of the Z component sz of the screen coordinates corresponding to v3.
   */
  @Method(0x8003f9c0L)
  public static int RotTransPers4(final SVECTOR v0, final SVECTOR v1, final SVECTOR v2, final SVECTOR v3, final SVECTOR sxy0, final SVECTOR sxy1, final SVECTOR sxy2, final SVECTOR sxy3) {
    GTE.perspectiveTransformTriangle(v0, v1, v2);
    sxy0.set(GTE.getScreenX(0), GTE.getScreenY(0), (short)0);
    sxy1.set(GTE.getScreenX(1), GTE.getScreenY(1), (short)0);
    sxy2.set(GTE.getScreenX(2), GTE.getScreenY(2), (short)0);

    GTE.perspectiveTransform(v3);
    sxy3.set(GTE.getScreenX(2), GTE.getScreenY(2), (short)0);

    return GTE.getScreenZ(3) >> 2;
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

  @Method(0x8003faf0L)
  public static void RotMatrix_Xyz(final SVECTOR rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f().rotateXYZ(MathHelper.psxDegToRad(rotation.getX()), MathHelper.psxDegToRad(rotation.getY()), MathHelper.psxDegToRad(rotation.getZ())));
  }

  @Method(0x8003fd80L)
  public static void RotMatrix_Yxz(final SVECTOR rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f().rotateYXZ(MathHelper.psxDegToRad(rotation.getY()), MathHelper.psxDegToRad(rotation.getX()), MathHelper.psxDegToRad(rotation.getZ())));
  }
}
