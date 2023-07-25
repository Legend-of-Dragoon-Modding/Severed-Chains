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
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

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
import static legend.game.Scus94491BpeSegment_8005.matrixStackIndex_80054a08;
import static legend.game.Scus94491BpeSegment_8005.matrixStack_80054a0c;
import static legend.game.Scus94491BpeSegment_8005.vectorStack_80054a0c;
import static legend.game.Scus94491BpeSegment_8005.vramHeight_800546c2;
import static legend.game.Scus94491BpeSegment_8005.vramWidth_800546c0;
import static legend.game.Scus94491BpeSegment_800c.PSDCNT_800c34d0;
import static legend.game.Scus94491BpeSegment_800c.coord2s_800c35a8;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.identityAspectMatrix_800c3588;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public final class Scus94491BpeSegment_8003 {
  private Scus94491BpeSegment_8003() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8003.class);

  @Method(0x80038190L)
  public static void ResetGraph() {
    GPU.resetCommandBuffer();
  }

  @Method(0x80038574L)
  private static void validateRect(final String text, final RECT rect) {
    if(rect.w.get() > vramWidth_800546c0 || rect.x.get() + rect.w.get() > vramWidth_800546c0 || rect.y.get() > vramHeight_800546c2 || rect.y.get() + rect.h.get() > vramHeight_800546c2 || rect.w.get() < 0 || rect.x.get() < 0 || rect.y.get() < 0 || rect.h.get() < 0) {
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

    rect.w.set(MathHelper.clamp(rect.w.get(), (short)0, (short)vramWidth_800546c0));
    rect.h.set(MathHelper.clamp(rect.h.get(), (short)0, (short)vramHeight_800546c2));

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

    final int aspect = (displayHeight << 14) / displayWidth / 3;

    identityAspectMatrix_800c3588.identity();
    identityAspectMatrix_800c3588.set(1, 1, (short)aspect);
    identityAspectMatrix_800c3588.transfer.set(0, 0, 0);

    lightDirectionMatrix_800c34e8.clear();
    lightColourMatrix_800c3508.zero();

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
    newCoord.coord.identity();
    newCoord.coord.transfer.set(0, 0, 0);
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
    final Matrix3f lightDirection = new Matrix3f();
    mp.mul(lightDirectionMatrix_800c34e8, lightDirection);
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

    if(id == 0) {
      //LAB_8003c7ec
      lightDirectionMatrix_800c34e8.set(0, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(1, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(2, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.m00 = r / 255.0f;
      lightColourMatrix_800c3508.m01 = g / 255.0f;
      lightColourMatrix_800c3508.m02 = b / 255.0f;
    } else if(id == 1) {
      //LAB_8003c904
      lightDirectionMatrix_800c34e8.set(3, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(4, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(5, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.m10 = r / 255.0f;
      lightColourMatrix_800c3508.m11 = g / 255.0f;
      lightColourMatrix_800c3508.m12 = b / 255.0f;
      //LAB_8003c7dc
    } else if(id == 2) {
      //LAB_8003ca20
      lightDirectionMatrix_800c34e8.set(6, (short)((-light.direction_00.getX() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(7, (short)((-light.direction_00.getY() << 12) / mag));
      lightDirectionMatrix_800c34e8.set(8, (short)((-light.direction_00.getZ() << 12) / mag));

      lightColourMatrix_800c3508.m20 = r / 255.0f;
      lightColourMatrix_800c3508.m21 = g / 255.0f;
      lightColourMatrix_800c3508.m22 = b / 255.0f;
    }

    //LAB_8003cb34
    SetColorMatrix(lightColourMatrix_800c3508);

    //LAB_8003cb88
    return 0;
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
  public static void FUN_8003cee0(final MATRIX matrix, final short sin, final short cos, final int type) {
    matrix.identity();
    matrix.transfer.set(0, 0, 0);

    //TODO this is weird... these are the positions for rotation matrices, but if they were transposed

    switch(type) {
      case 0 -> {
        matrix.set(1, 1, cos);
        matrix.set(1, 2, (short)-sin);
        matrix.set(2, 1, sin);
        matrix.set(2, 2, cos);
      }

      case 1 -> {
        matrix.set(0, 0, cos);
        matrix.set(0, 2, sin);
        matrix.set(2, 0, (short)-sin);
        matrix.set(2, 2, cos);
      }
    }
  }

  /**
   * GsMulCoord2 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m2 = m1 x m2
   */
  @Method(0x8003d550L)
  public static void GsMulCoord2(final MATRIX matrix1, final MATRIX matrix2) {
    matrix2.transfer.mul(matrix1);
    matrix2.mul(matrix1);
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

      rot.mul(matrix, matrix);
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
   * GsMulCoord3 multiplies the MATRIX m2 by the translation matrix m1 and stores the result in m1.
   * <p>
   * m1 = m1 x m2
   */
  @Method(0x8003d950L)
  public static void GsMulCoord3(final MATRIX m1, final MATRIX m2) {
    final VECTOR out = new VECTOR();
    m2.transfer.mul(m1, out);
    m2.mul(m1, m1);
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
  public static void GsSetRefView2L(final GsRVIEW2 s2) {
    worldToScreenMatrix_800c3548.set(identityAspectMatrix_800c3588);
    FUN_8003d5d0(worldToScreenMatrix_800c3548, -s2.viewpointTwist_18);

    final int deltaX = s2.refpoint_0c.getX() - s2.viewpoint_00.getX();
    final int deltaY = s2.refpoint_0c.getY() - s2.viewpoint_00.getY();
    final int deltaZ = s2.refpoint_0c.getZ() - s2.viewpoint_00.getZ();

    final int vectorLengthSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

    if(vectorLengthSquared == 0) {
      return;
    }

    final int vectorLength = Math.max(1, (int)Math.sqrt(vectorLengthSquared));

    final int normalizedY = deltaY * 0x1000 / vectorLength;

    final int horizontalLength = (int)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    final int normalizedHypotenuse = horizontalLength * 0x1000 / vectorLength;

    //LAB_8003e230
    final MATRIX sp0x30 = new MATRIX();
    FUN_8003cee0(sp0x30, (short)normalizedY, (short)normalizedHypotenuse, 0);
    sp0x30.mul(worldToScreenMatrix_800c3548, worldToScreenMatrix_800c3548);

    if(horizontalLength != 0) {
      final int normalizedX = deltaX * -0x1000 / horizontalLength;
      final int normalizedZ = deltaZ * 0x1000 / horizontalLength;

      FUN_8003cee0(sp0x30, (short)normalizedX, (short)normalizedZ, 1);
      sp0x30.mul(worldToScreenMatrix_800c3548, worldToScreenMatrix_800c3548);
    }

    //LAB_8003e474
    new VECTOR().set(s2.viewpoint_00).negate().mul(worldToScreenMatrix_800c3548, worldToScreenMatrix_800c3548.transfer);

    if(s2.super_1c != null) {
      final MATRIX lw = new MATRIX();
      GsGetLw(s2.super_1c, lw);

      final MATRIX transposedLw = new MATRIX();
      lw.transpose(transposedLw);
      lw.transfer.mul(transposedLw, transposedLw.transfer);
      transposedLw.transfer.negate();
      GsMulCoord2(worldToScreenMatrix_800c3548, transposedLw);
      worldToScreenMatrix_800c3548.set(transposedLw);
    }
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

  @Method(0x8003e958L)
  public static void InitGeom() {
    GTE.setProjectionPlaneDistance(1000);
    GTE.setScreenOffset(0, 0);
  }

  @Method(0x8003ef80L)
  public static void PushMatrix() {
    if(matrixStackIndex_80054a08 >= 20) {
      throw new RuntimeException("Error: Can't push matrix, stack(max 20) is full!");
    }

    //LAB_8003efc0
    GTE.getRotationMatrix(matrixStack_80054a0c[matrixStackIndex_80054a08]);
    GTE.getTranslationVector(vectorStack_80054a0c[matrixStackIndex_80054a08]);

    matrixStackIndex_80054a08++;
  }

  @Method(0x8003f024L)
  public static void PopMatrix() {
    if(matrixStackIndex_80054a08 == 0) {
      throw new RuntimeException("Error: Can't pop matrix, stack is empty!");
    }

    //LAB_8003f060
    matrixStackIndex_80054a08--;

    GTE.setRotationMatrix(matrixStack_80054a0c[matrixStackIndex_80054a08]);
    GTE.setTranslationVector(vectorStack_80054a0c[matrixStackIndex_80054a08]);
  }

  @Method(0x8003f8a0L)
  public static void getScreenOffset(final IntRef screenOffsetX, final IntRef screenOffsetY) {
    screenOffsetX.set(GTE.getScreenOffsetX() >> 16);
    screenOffsetY.set(GTE.getScreenOffsetY() >> 16);
  }

  @Method(0x8003f8c0L)
  public static int getProjectionPlaneDistance() {
    return GTE.getProjectionPlaneDistance();
  }

  @Method(0x8003f8f0L) //Also 0x8003c6d0
  public static void setProjectionPlaneDistance(final int distance) {
    GTE.setProjectionPlaneDistance(distance);
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

  @Method(0x8003faf0L)
  public static void RotMatrix_Xyz(final SVECTOR rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f().rotateXYZ(MathHelper.psxDegToRad(rotation.getX()), MathHelper.psxDegToRad(rotation.getY()), MathHelper.psxDegToRad(rotation.getZ())));
  }

  public static void RotMatrix_Xyz(final Vector3f rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f().rotateXYZ(rotation.x, rotation.y, rotation.z));
  }

  @Method(0x8003fd80L)
  public static void RotMatrix_Yxz(final Vector3f rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f().rotateYXZ(rotation.y, rotation.x, rotation.z));
  }
}
