package legend.game;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandFillVram;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment_8005.matrixStackIndex_80054a08;
import static legend.game.Scus94491BpeSegment_8005.matrixStack_80054a0c;
import static legend.game.Scus94491BpeSegment_800c.PSDCNT_800c34d0;
import static legend.game.Scus94491BpeSegment_800c.coord2s_800c35a8;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.identityAspectMatrix_800c3588;
import static legend.game.Scus94491BpeSegment_800c.inverseWorldToScreenMatrix;
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
    displayWidth_1f8003e0 = displayWidth;
    displayHeight_1f8003e4 = displayHeight;

    final float aspect = (float)displayHeight / displayWidth * 4.0f / 3.0f;

    identityAspectMatrix_800c3588.identity();
    identityAspectMatrix_800c3588.set(1, 1, aspect);
    identityAspectMatrix_800c3588.transfer.zero();

    lightDirectionMatrix_800c34e8.zero();
    lightColourMatrix_800c3508.zero();

    centreScreenX_1f8003dc = 0;
    centreScreenY_1f8003de = 0;

    displayRect_800c34c8.set(0, 0, displayWidth, displayHeight);

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
    GPU.queueCommand(orderingTableSize_1f8003c8 - 1, new GpuCommandFillVram(r, g, b));
  }

  @Method(0x8003c1c0L)
  public static void GsSetDrawBuffOffset() {
    GPU.drawingOffset(centreScreenX_1f8003dc, centreScreenY_1f8003de);
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
    GPU.drawingArea(displayRect_800c34c8.x, displayRect_800c34c8.y, displayRect_800c34c8.w, displayRect_800c34c8.h);
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

  @Method(0x8003c4a0L)
  public static void GsSetLightMatrix(final Matrix3f mp) {
    final Matrix3f lightDirection = new Matrix3f();
    lightDirectionMatrix_800c34e8.mul(mp, lightDirection);
    GTE.setLightSourceMatrix(lightDirection);
  }

  @Method(0x8003c5e0L)
  public static void setDrawOffset() {
    centreScreenX_1f8003dc = displayWidth_1f8003e0 / 2;
    centreScreenY_1f8003de = displayHeight_1f8003e4 / 2;
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
    final float x = light.direction_00.x;
    final float y = light.direction_00.y;
    final float z = light.direction_00.z;
    final float r = light.r_0c;
    final float g = light.g_0d;
    final float b = light.b_0e;

    // Normalize vector - calculate magnitude
    final float mag = Math.sqrt(x * x + y * y + z * z);

    if(mag == 0) {
      return -1;
    }

    if(id == 0) {
      //LAB_8003c7ec
      lightDirectionMatrix_800c34e8.m00(-light.direction_00.x / mag);
      lightDirectionMatrix_800c34e8.m10(-light.direction_00.y / mag);
      lightDirectionMatrix_800c34e8.m20(-light.direction_00.z / mag);

      lightColourMatrix_800c3508.m00 = r;
      lightColourMatrix_800c3508.m01 = g;
      lightColourMatrix_800c3508.m02 = b;
    } else if(id == 1) {
      //LAB_8003c904
      lightDirectionMatrix_800c34e8.m01(-light.direction_00.x / mag);
      lightDirectionMatrix_800c34e8.m11(-light.direction_00.y / mag);
      lightDirectionMatrix_800c34e8.m21(-light.direction_00.z / mag);

      lightColourMatrix_800c3508.m10 = r;
      lightColourMatrix_800c3508.m11 = g;
      lightColourMatrix_800c3508.m12 = b;
      //LAB_8003c7dc
    } else if(id == 2) {
      //LAB_8003ca20
      lightDirectionMatrix_800c34e8.m02(-light.direction_00.x / mag);
      lightDirectionMatrix_800c34e8.m12(-light.direction_00.y / mag);
      lightDirectionMatrix_800c34e8.m22(-light.direction_00.z / mag);

      lightColourMatrix_800c3508.m20 = r;
      lightColourMatrix_800c3508.m21 = g;
      lightColourMatrix_800c3508.m22 = b;
    }

    //LAB_8003cb34
    GTE.setLightColourMatrix(lightColourMatrix_800c3508);

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
    GTE.setScreenOffset(0, 0);
  }

  @Method(0x8003cee0L)
  public static void FUN_8003cee0(final MV matrix, final float sin, final float cos, final int type) {
    matrix.identity();
    matrix.transfer.zero();

    switch(type) {
      case 0 -> { // x
        matrix.m11(cos);
        matrix.m12(sin);
        matrix.m21(-sin);
        matrix.m22(cos);
      }

      case 1 -> { // y
        matrix.m00(cos);
        matrix.m02(-sin);
        matrix.m20(sin);
        matrix.m22(cos);
      }
    }
  }

  /**
   * GsMulCoord2 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m2 = m1 x m2
   */
  @Method(0x8003d550L)
  public static void GsMulCoord2(final MV matrix1, final MV matrix2) {
    matrix2.transfer.mul(matrix1);
    matrix1.mul(matrix2, matrix2);
    matrix2.transfer.add(matrix1.transfer);
  }

  @Method(0x8003d5d0L)
  public static void FUN_8003d5d0(final MV matrix, final int angle) {
    if(angle != 0) {
      // Almost normal rotateZ except the sin is negated for some reason
      final float rads = MathHelper.psxDegToRad(angle / 360.0f);
      final float sin = -MathHelper.sin(rads);
      final float cos = MathHelper.cosFromSin(-sin, rads);

      final Matrix3f rot = new Matrix3f();
      rot.m00 = cos;
      rot.m10 = sin;
      rot.m20 = 0.0f;
      rot.m01 = -sin;
      rot.m11 = cos;
      rot.m21 = 0.0f;
      rot.m02 = 0.0f;
      rot.m12 = 0.0f;
      rot.m22 = 1.0f;

      matrix.mul(rot);
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
  public static void GsGetLw(final GsCOORDINATE2 coord, final MV matrix) {
    GsCOORDINATE2 current = coord;
    int count = 0;
    int a1_0 = 100;

    //LAB_8003d6c0
    do {
      coord2s_800c35a8[count] = current;

      if(current.super_ == null) {
        if(current.flg == 0 || current.flg == PSDCNT_800c34d0) { // Root coord2
          //LAB_8003d6fc
          current.workm.set(current.coord);
          matrix.set(current.workm);
          current.flg = PSDCNT_800c34d0;
          break;
        }

        //LAB_8003d78c
        if(a1_0 == 100) {
          matrix.set(coord2s_800c35a8[0].workm);
          count = 0;
          break;
        }

        //LAB_8003d7e8
        count = a1_0 + 1;
        matrix.set(coord2s_800c35a8[count].workm);
        break;
      }

      //LAB_8003d83c
      if(current.flg == PSDCNT_800c34d0) {
        matrix.set(current.workm);
        break;
      }

      //LAB_8003d898
      if(current.flg == 0) {
        a1_0 = count;
      }

      //LAB_8003d8a4
      current = current.super_;
      count++;
    } while(true);

    //LAB_8003d8ac
    //LAB_8003d8c0
    for(int i = count - 1; i >= 0; i--) {
      final GsCOORDINATE2 coord2 = coord2s_800c35a8[i];
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
  public static void GsMulCoord3(final MV m1, final MV m2) {
    final Vector3f out = new Vector3f();
    m2.transfer.mul(m1, out);
    m1.mul(m2);
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
  public static void GsGetLs(final GsCOORDINATE2 coord, final MV matrix) {
    GsCOORDINATE2 current = coord;
    int count = 0;
    int a1 = 100;

    //LAB_8003da00
    do {
      coord2s_800c35a8[count] = current;
      if(current.super_ == null) {
        if(current.flg == 0 || current.flg == PSDCNT_800c34d0) {
          //LAB_8003da3c
          current.workm.set(current.coord);
          matrix.set(current.workm);
          current.flg = PSDCNT_800c34d0;
          break;
        }

        //LAB_8003dacc
        count = a1 + 1;

        if(a1 == 100) {
          matrix.set(coord2s_800c35a8[0].workm);
          count = 0;
          break;
        }

        //LAB_8003db28
        matrix.set(coord2s_800c35a8[count].workm);
        break;
      }

      //LAB_8003db7c
      if(current.flg == PSDCNT_800c34d0) {
        matrix.set(current.workm);
        break;
      }

      //LAB_8003dbd8
      if(current.flg == 0) {
        a1 = count;
      }

      //LAB_8003dbe4
      current = current.super_;
      count++;
    } while(true);

    //LAB_8003dbec
    //LAB_8003dc00
    for(int i = count - 1; i >= 0; i--) {
      final GsCOORDINATE2 coord2 = coord2s_800c35a8[i];
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
  public static void GsGetLws(GsCOORDINATE2 coord, final MV lw, final MV ls) {
    int count = 0;
    int a = 100;

    //LAB_8003dcd8
    do {
      coord2s_800c35a8[count] = coord;

      if(coord.super_ == null) { // If top level coord2...
        if(coord.flg == PSDCNT_800c34d0 || coord.flg == 0) { // ...and not initialized (or initialized this frame - maybe prevents loops?)
          //LAB_8003dd14
          coord.flg = PSDCNT_800c34d0;
          coord.workm.set(coord.coord);
          lw.set(coord.workm);
          break;
        }

        //LAB_8003dda4
        if(a == 100) { // ...and this is the only coord2
          lw.set(coord2s_800c35a8[0].workm);
          count = 0;
          break;
        }

        //LAB_8003de00
        count = a + 1;
        lw.set(coord2s_800c35a8[count].workm);
        break;
      }

      //LAB_8003de54
      if(coord.flg == PSDCNT_800c34d0) {
        lw.set(coord.workm);
        break;
      }

      //LAB_8003deb0
      if(coord.flg == 0) {
        a = count;
      }

      //LAB_8003debc
      coord = coord.super_;
      count++;
    } while(true);

    //LAB_8003dec4
    //LAB_8003ded8
    for(int i = count - 1; i >= 0; i--) {
      final GsCOORDINATE2 c = coord2s_800c35a8[i];

      GsMulCoord3(lw, c.coord);

      c.flg = PSDCNT_800c34d0;
      c.workm.set(lw);
    }

    //LAB_8003df48
    ls.set(lw);
    GsMulCoord2(worldToScreenMatrix_800c3548, ls);
  }

  private static final MV cameraTemp = new MV();
  private static final MV cameraLw = new MV();
  private static final MV cameraTransposedLw = new MV();
  private static final Matrix4f cameraParent = new Matrix4f();

  /**
   * Calculates GsWSMATRIX using the viewpoint information in pv. GsWSMATRIX doesn’t change unless the
   * viewpoint is moved, so this function should be called every frame only if the viewpoint is moved, in order for
   * changes to be updated.
   *
   * It should also be called every frame if the GsRVIEW2 member super is set to anything other than WORLD,
   * because even if the other parameters are not changed, if the parameters of the superior coordinate system
   * are changed, the viewpoint will have moved.
   */
  @Method(0x8003dfc0L)
  public static void GsSetRefView2L(final GsRVIEW2 s2) {
    RENDERER.camera().lookAt(s2.viewpoint_00, s2.refpoint_0c);

    worldToScreenMatrix_800c3548.set(identityAspectMatrix_800c3588);

    FUN_8003d5d0(worldToScreenMatrix_800c3548, -s2.viewpointTwist_18);

    final float deltaX = s2.refpoint_0c.x - s2.viewpoint_00.x;
    final float deltaY = s2.refpoint_0c.y - s2.viewpoint_00.y;
    final float deltaZ = s2.refpoint_0c.z - s2.viewpoint_00.z;

    final float vectorLengthSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

    if(vectorLengthSquared == 0.0f) {
      return;
    }

    final float vectorLength = Math.sqrt(vectorLengthSquared);

    final float normalizedY = deltaY / vectorLength;

    final float horizontalLength = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    final float normalizedHypotenuse = horizontalLength / vectorLength;

    //LAB_8003e230
    FUN_8003cee0(cameraTemp, normalizedY, normalizedHypotenuse, 0);
    worldToScreenMatrix_800c3548.mul(cameraTemp);

    if(horizontalLength != 0) {
      final float normalizedX = -deltaX / horizontalLength;
      final float normalizedZ = deltaZ / horizontalLength;

      FUN_8003cee0(cameraTemp, normalizedX, normalizedZ, 1);
      worldToScreenMatrix_800c3548.mul(cameraTemp);
    }

    //LAB_8003e474
    worldToScreenMatrix_800c3548.transfer.set(s2.viewpoint_00).negate().mul(worldToScreenMatrix_800c3548);

    if(s2.super_1c != null) {
      GsGetLw(s2.super_1c, cameraLw);

      cameraLw.transpose(cameraTransposedLw);
      cameraLw.transfer.mul(cameraTransposedLw, cameraTransposedLw.transfer);
      cameraTransposedLw.transfer.negate();
      GsMulCoord2(worldToScreenMatrix_800c3548, cameraTransposedLw);
      worldToScreenMatrix_800c3548.set(cameraTransposedLw);

      cameraParent.set(cameraLw).translate(-cameraLw.transfer.x, -cameraLw.transfer.y, -cameraLw.transfer.z);
      RENDERER.camera().getView().mul(cameraParent);
    }

    inverseWorldToScreenMatrix.set(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer).invert();

    RENDERER.camera().getView().set(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer);
  }

  /**
   * This method is the same as GsSetRefView2L, except that internally the math is performed
   * on integers prior to normalization. This is done so that sobj and collision placements
   * match retail, as higher precision math was causing noticeable shifts.
   */
  public static void GsSetSmapRefView2L(final GsRVIEW2 s2) {
    worldToScreenMatrix_800c3548.set(identityAspectMatrix_800c3588);

    FUN_8003d5d0(worldToScreenMatrix_800c3548, -s2.viewpointTwist_18);

    final int deltaX = (int)(s2.refpoint_0c.x - s2.viewpoint_00.x);
    final int deltaY = (int)(s2.refpoint_0c.y - s2.viewpoint_00.y);
    final int deltaZ = (int)(s2.refpoint_0c.z - s2.viewpoint_00.z);

    final int vectorLengthSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

    if(vectorLengthSquared == 0) {
      return;
    }

    final int vectorLength = (int)Math.sqrt(vectorLengthSquared);

    final float normalizedY = (float)(deltaY * 0x1000 / vectorLength) / 0x1000;

    final int horizontalLength = (int)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    final float normalizedHypotenuse = (float)(horizontalLength * 0x1000 / vectorLength) / 0x1000;

    //LAB_8003e230
    final MV sp0x30 = new MV();
    FUN_8003cee0(sp0x30, normalizedY, normalizedHypotenuse, 0);
    worldToScreenMatrix_800c3548.mul(sp0x30);

    if(horizontalLength != 0) {
      final float normalizedX = (float)(-deltaX * 0x1000 / horizontalLength) / 0x1000;
      final float normalizedZ = (float)(deltaZ * 0x1000 / horizontalLength) / 0x1000;

      FUN_8003cee0(sp0x30, normalizedX, normalizedZ, 1);
      worldToScreenMatrix_800c3548.mul(sp0x30);
    }

    //LAB_8003e474
    worldToScreenMatrix_800c3548.transfer.set(s2.viewpoint_00).negate().mul(worldToScreenMatrix_800c3548);

    if(s2.super_1c != null) {
      final MV lw = new MV();
      GsGetLw(s2.super_1c, lw);

      final MV transposedLw = new MV();
      lw.transpose(transposedLw);
      lw.transfer.mul(transposedLw, transposedLw.transfer);
      transposedLw.transfer.negate();
      GsMulCoord2(worldToScreenMatrix_800c3548, transposedLw);
      worldToScreenMatrix_800c3548.set(transposedLw);
    }

    inverseWorldToScreenMatrix.set(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer).invert();

    RENDERER.camera().getView().set(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer);
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
    setProjectionPlaneDistance(1000);
    GTE.setScreenOffset(0, 0);
  }

  @Method(0x8003ef80L)
  public static void PushMatrix() {
    if(matrixStackIndex_80054a08 >= 20) {
      throw new RuntimeException("Error: Can't push matrix, stack(max 20) is full!");
    }

    //LAB_8003efc0
    GTE.getTransforms(matrixStack_80054a0c[matrixStackIndex_80054a08]);

    matrixStackIndex_80054a08++;
  }

  @Method(0x8003f024L)
  public static void PopMatrix() {
    if(matrixStackIndex_80054a08 == 0) {
      throw new RuntimeException("Error: Can't pop matrix, stack is empty!");
    }

    //LAB_8003f060
    matrixStackIndex_80054a08--;

    GTE.setTransforms(matrixStack_80054a0c[matrixStackIndex_80054a08]);
  }

  @Method(0x8003f8a0L)
  public static void getScreenOffset(final Vector2f offset) {
    GTE.getScreenOffset(offset);
  }

  @Method(0x8003f8c0L)
  public static float getProjectionPlaneDistance() {
    return GTE.getProjectionPlaneDistance();
  }

  @Method(0x8003f8f0L) //Also 0x8003c6d0
  public static void setProjectionPlaneDistance(final float distance) {
    RENDERER.setProjectionDepth(distance);
    GTE.setProjectionPlaneDistance(distance);
  }

  /** Returns Z */
  @Method(0x8003f900L)
  public static float perspectiveTransform(final Vector3f worldCoords, final Vector2f screenCoords) {
    GTE.perspectiveTransform(worldCoords);
    screenCoords.set(GTE.getScreenX(2), GTE.getScreenY(2));
    return GTE.getScreenZ(3) / 4.0f;
  }

  @Method(0x8003f930L)
  public static float perspectiveTransformTriple(final Vector3f world0, final Vector3f world1, final Vector3f world2, final Vector2f screen0, final Vector2f screen1, final Vector2f screen2) {
    GTE.perspectiveTransformTriangle(world0, world1, world2);

    screen0.set(GTE.getScreenX(0), GTE.getScreenY(0));
    screen1.set(GTE.getScreenX(1), GTE.getScreenY(1));
    screen2.set(GTE.getScreenX(2), GTE.getScreenY(2));

    return GTE.getScreenZ(3) / 4.0f;
  }

  /**
   * <p>Perform coordinate and perspective transformation for 4 vertices.</p>
   *
   * <p>After transforming the four coordinate vectors v0, v1, v2, and v3 using a rotation matrix, the function
   * performs perspective transformation, and returns four screen coordinates sxy0, sxy1, sxy2, and sxy3. It
   * also returns an interpolation value for depth queueing to p corresponding to v3.</p>
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
  public static float RotTransPers4(final Vector3f v0, final Vector3f v1, final Vector3f v2, final Vector3f v3, final Vector2f sxy0, final Vector2f sxy1, final Vector2f sxy2, final Vector2f sxy3) {
    GTE.perspectiveTransformTriangle(v0, v1, v2);
    sxy0.set(GTE.getScreenX(0), GTE.getScreenY(0));
    sxy1.set(GTE.getScreenX(1), GTE.getScreenY(1));
    sxy2.set(GTE.getScreenX(2), GTE.getScreenY(2));

    GTE.perspectiveTransform(v3);
    sxy3.set(GTE.getScreenX(2), GTE.getScreenY(2));

    return GTE.getScreenZ(3) / 4.0f;
  }
}
