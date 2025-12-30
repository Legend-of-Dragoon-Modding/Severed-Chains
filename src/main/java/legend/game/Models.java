package legend.game;

import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.game.tmd.Tmd;
import legend.game.tmd.TmdObjTable1c;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.ClutAnimation;
import legend.game.types.ClutAnimations;
import legend.game.types.Model124;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Graphics.GsInitCoordinate2;

public final class Models {
  private Models() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Models.class);

  public static final UvAdjustmentMetrics14[] vramSlots_8005027c = {
    UvAdjustmentMetrics14.NONE,
    new UvAdjustmentMetrics14( 1, 320, 496, 320, 256),
    new UvAdjustmentMetrics14( 2, 384, 496, 384, 256),
    new UvAdjustmentMetrics14( 3, 448, 496, 448, 256),
    new UvAdjustmentMetrics14( 4, 512, 496, 512, 256),
    new UvAdjustmentMetrics14( 5, 576, 496, 576, 256),
    new UvAdjustmentMetrics14( 6, 640, 496, 640, 256),
    new UvAdjustmentMetrics14( 7, 576, 496, 576, 256),
    new UvAdjustmentMetrics14( 8, 640, 496, 640, 256),
    new UvAdjustmentMetrics14( 9, 704, 496, 704, 256),
    new UvAdjustmentMetrics14(10, 768, 240, 768,   0),
    new UvAdjustmentMetrics14(11, 832, 240, 832,   0),
    new UvAdjustmentMetrics14(12, 896, 240, 896,   0),
    new UvAdjustmentMetrics14(13, 960, 240, 960,   0),
    new UvAdjustmentMetrics14(14, 512, 240, 512,   0),
    new UvAdjustmentMetrics14(15, 576, 240, 576,   0),
    new UvAdjustmentMetrics14(16, 640, 240, 640,   0),
    new UvAdjustmentMetrics14(17, 704, 240, 704,   0),
  };

  public static final int[] shadowScale_8005039c = {0x1800, 0x1800, 0x1000, 0xe00, 0x1600, 0x1300, 0xe00, 0x2000, 0x1300, 0x1500};

  @Method(0x80020468L)
  public static void adjustPartUvs(final ModelPart10 dobj2, final UvAdjustmentMetrics14 metrics) {
    final TmdObjTable1c objTable = dobj2.tmd_08;

    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      final int command = primitive.header() & 0xff04_0000;

      if(command == 0x3400_0000 || command == 0x3500_0000 || command == 0x3600_0000 || command == 0x3700_0000 || command == 0x3c00_0000 || command == 0x3d00_0000 || command == 0x3e00_0000 || command == 0x3f00_0000) {
        metrics.apply(primitive);
      }
    }
  }

  @Method(0x80020718L)
  public static void loadModelAndAnimation(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    LOGGER.info("Loading scripted TMD %s (animation %s)", cContainer, tmdAnimFile);

    final float x = model.coord2_14.coord.transfer.x;
    final float y = model.coord2_14.coord.transfer.y;
    final float z = model.coord2_14.coord.transfer.z;

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    if(currentEngineState_8004dd04 != null) {
      currentEngineState_8004dd04.modelLoaded(model, cContainer);
    }

    //LAB_8002079c
    model.tpage_108 = (int)((cContainer.tmdPtr_00.id & 0xffff_0000L) >>> 11); // LOD uses the upper 16 bits of TMD IDs as tpage (sans VRAM X/Y)

    if(cContainer.ptr_08 != null) {
      model.ptr_a8 = cContainer.ptr_08;

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        model.animationMetrics_d0[i] = model.ptr_a8._00[i];
        FUN_8002246c(model, i);
      }
    } else {
      //LAB_80020818
      model.ptr_a8 = null;

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        model.animationMetrics_d0[i] = null;
      }
    }

    //LAB_80020838
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.modelParts_00, cContainer.tmdPtr_00.tmd, model.coord2_14);

    model.zOffset_a0 = 0;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    //LAB_800209b0
    model.coord2_14.coord.transfer.set(x, y, z);
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();
  }

  @Method(0x80020a00L)
  public static void initModel(final Model124 model, final CContainer CContainer, final TmdAnimationFile tmdAnimFile) {
    model.modelParts_00 = new ModelPart10[CContainer.tmdPtr_00.tmd.header.nobj];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    loadModelAndAnimation(model, CContainer, tmdAnimFile);
    adjustModelUvs(model);
    model.modelPartWithShadowIndex_cd = -2;
  }

  @Method(0x80020b98L)
  public static void animateModel(final Model124 model) {
    animateModel(model, 2);
  }

  public static void animateModel(final Model124 model, final int framesPerKeyframe) {
    //LAB_80020be8
    //LAB_80020bf0
    // Only apply texture animations for the keyframe of the middle interpolation frame
    final boolean tickAnimations = model.subFrameIndex == 0 || model.subFrameIndex == framesPerKeyframe / 2;

    if(model.clutAnimations_a4 != null) {
      //LAB_800da138
      for(int animIndex = 0; animIndex < 4; animIndex++) {
        if(model.clutAnimations_a4.used_04[animIndex]) {
          animateModelClut(model, animIndex, tickAnimations);
        }
      }
    }

    if(tickAnimations) {
      for(int i = 0; i < 7; i++) {
        if(model.animateTextures_ec[i]) {
          animateModelTextures(model, i);
        }
      }
    }

    if(model.animationState_9c == 2) {
      return;
    }

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    }

    //LAB_80020c3c
    if(model.animationState_9c == 0) {
      model.animationState_9c = 1;
      model.remainingFrames_9e = model.totalFrames_9a / 2;
      model.currentKeyframe_94 = 0;
      model.subFrameIndex = 0;
    }

    //LAB_80020c90
    if(model.subFrameIndex == framesPerKeyframe - 1 || model.disableInterpolation_a2 && model.subFrameIndex == framesPerKeyframe / 2 - 1) {
      applyKeyframe(model);
      model.currentKeyframe_94++;
      model.remainingFrames_9e--;
      model.subFrameIndex = 0;
    } else {
      applyInterpolationFrame(model, model.disableInterpolation_a2 ? framesPerKeyframe / 2 : framesPerKeyframe);
      model.subFrameIndex++;
    }
    //LAB_80020e98
    //LAB_80020ea8
  }

  /** (pulled from SMAP) Used in pre-Melbu submap cutscene, Prairie, new game Rose cutscene (animates the cloud flicker by changing CLUT, pretty sure this is CLUT animation) */
  @Method(0x800dde70L)
  private static void animateModelClut(final Model124 model, final int animIndex, final boolean tickAnimations) {
    final ClutAnimations anims = model.clutAnimations_a4;

    if(anims.clutAnimation_20[animIndex] == null) {
      anims.used_04[animIndex] = false;
    } else {
      //LAB_800ddeac
      final int x = model.uvAdjustments_9d.clutX;
      final int y = model.uvAdjustments_9d.clutY;

      final ClutAnimation anim = anims.clutAnimation_20[animIndex];
      int dataOffset = anims.dataIndex_08[animIndex] * 2;

      //LAB_800ddf08
      final int sourceYOffset = anim.dataStream_04[dataOffset];
      dataOffset++;

      if(tickAnimations) {
        anims.frameIndex_10[animIndex]++;

        if(anims.frameIndex_10[animIndex] == anim.dataStream_04[dataOffset]) {
          anims.frameIndex_10[animIndex] = 0;

          if(anim.dataStream_04[dataOffset + 1] == -1) {
            anims.dataIndex_08[animIndex] = 0;
          } else {
            //LAB_800ddf70
            anims.dataIndex_08[animIndex]++;
          }
        }
      }

      //LAB_800ddf8c
      RENDERER.addClutAnimation(x, y + anims.clutIndex_18[animIndex], x, y + sourceYOffset);
    }
    //LAB_800ddff4
  }

  @Method(0x800212d8L)
  public static void applyKeyframe(final Model124 model) {
    //LAB_80021320
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final Transforms params = coord2.transforms;

      params.quat.set(model.keyframes_90[model.currentKeyframe_94][i].quat);
      params.trans.set(model.keyframes_90[model.currentKeyframe_94][i].translate_06);

      coord2.coord.rotation(params.quat);
      coord2.coord.transfer.set(params.trans);
    }
  }

  @Method(0x800213c4L)
  public static void applyInterpolationFrame(final Model124 model, final int framesPerKeyframe) {
    //LAB_80021404
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final Transforms params = coord2.transforms;

      final float interpolationScale = (model.subFrameIndex + 1.0f) / framesPerKeyframe;
      params.trans.lerp(model.keyframes_90[model.currentKeyframe_94][i].translate_06, interpolationScale, coord2.coord.transfer);
      params.quat.nlerp(model.keyframes_90[model.currentKeyframe_94][i].quat, interpolationScale, params.quat);
      coord2.coord.rotation(params.quat);
    }
  }

  @Method(0x800214bcL)
  public static void applyModelRotationAndScale(final Model124 model) {
    model.coord2_14.coord.scaling(model.coord2_14.transforms.scale);
    model.coord2_14.coord.rotateXYZ(model.coord2_14.transforms.rotate);
    model.coord2_14.flg = 0;
  }

  @Method(0x80021520L)
  public static void loadPlayerModelAndAnimation(final Model124 model, final CContainer tmd, final TmdAnimationFile anim, final int shadowSizeIndex) {
    loadModelAndAnimation(model, tmd, anim);
    adjustModelUvs(model);
    model.modelPartWithShadowIndex_cd = -2;

    setShadowSize(model, shadowSizeIndex);
  }

  @Method(0x8002155cL)
  public static void setShadowSize(final Model124 model, final int shadowSizeIndex) {
    final float scale = shadowScale_8005039c[shadowSizeIndex] / (float)0x1000;
    model.shadowSize_10c.set(scale, scale, scale);
  }

  @Method(0x80021584L)
  public static void loadModelStandardAnimation(final Model124 model, final TmdAnimationFile tmdAnimFile) {
    model.anim_08 = model.new StandardAnim(tmdAnimFile);
    model.keyframes_90 = tmdAnimFile.partTransforms_10;
    model.currentKeyframe_94 = 0;
    model.partCount_98 = tmdAnimFile.modelPartCount_0c;
    model.totalFrames_9a = tmdAnimFile.totalFrames_0e;
    model.animationState_9c = 0;

    applyKeyframe(model);

    model.remainingFrames_9e = model.totalFrames_9a / 2;
    model.subFrameIndex = 0;
    model.animationState_9c = 1;
    model.currentKeyframe_94 = 0;
  }

  @Method(0x80021628L)
  public static void adjustModelUvs(final Model124 model) {
    for(final ModelPart10 dobj2 : model.modelParts_00) {
      adjustPartUvs(dobj2, model.uvAdjustments_9d);
    }
  }

  @Method(0x80021b08L)
  public static void initObjTable2(final ModelPart10[] dobj2s) {
    for(final ModelPart10 dobj2 : dobj2s) {
      dobj2.attribute_00 = 0x8000_0000;
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.tmd_08 = null;
    }
  }

  @Method(0x80021bacL)
  public static void addNewDobj2(final ModelPart10 dobj2) {
    dobj2.attribute_00 = 0;
    GsInitCoordinate2(null, dobj2.coord2_04);
    dobj2.tmd_08 = null;
  }

  @Method(0x80021ca0L)
  public static void prepareObjTable2(final ModelPart10[] table, final Tmd tmd, final GsCOORDINATE2 coord2) {
    //LAB_80021d08
    for(final ModelPart10 dobj2 : table) {
      addNewDobj2(dobj2);
    }

    //LAB_80021d3c
    //LAB_80021d64
    for(int i = 0; i < table.length; i++) {
      final ModelPart10 dobj2 = table[i];

      dobj2.coord2_04.flg = 0;

      if(dobj2.coord2_04.super_ == null) {
        dobj2.coord2_04.super_ = coord2;
      }

      dobj2.tmd_08 = tmd.objTable[i];

      dobj2.coord2_04.coord.identity();

      // Dunno why but models are initialized with a 1-degree rotation on all axes
      final float oneDegree = (float)Math.toRadians(1.0);
      dobj2.coord2_04.transforms.rotate.set(oneDegree, oneDegree, oneDegree);
      dobj2.coord2_04.transforms.scale.set(1.0f, 1.0f, 1.0f);
      dobj2.coord2_04.transforms.trans.set(1.0f, 1.0f, 1.0f);

      dobj2.coord2_04.coord.scaling(dobj2.coord2_04.transforms.scale);
      dobj2.coord2_04.coord.rotateXYZ(dobj2.coord2_04.transforms.rotate);
      dobj2.coord2_04.coord.transfer.set(dobj2.coord2_04.transforms.trans);
    }

    //LAB_80021db4
  }

  /**
   * This method animates the fog in the first cutscene with Rose/Feyrbrand
   */
  @Method(0x80022018L)
  public static void animateModelTextures(final Model124 model, final int index) {
    if(model.animationMetrics_d0[index] == null) {
      model.animateTextures_ec[index] = false;
      return;
    }

    //LAB_80022068
    //LAB_80022098
    if(model.uvAdjustments_9d == UvAdjustmentMetrics14.NONE) {
      return;
    }

    final int vramX = model.uvAdjustments_9d.tpageX;
    final int vramY = model.uvAdjustments_9d.tpageY;

    //LAB_800220c0
    if(model.usArr_ba[index] != 0x5678) {
      model.usArr_ba[index]--;
      if((short)model.usArr_ba[index] != 0) {
        return;
      }

      int metricsIndex = 0;
      model.usArr_ba[index] = model.animationMetrics_d0[index][metricsIndex++] & 0x7fff;
      final int destX = model.animationMetrics_d0[index][metricsIndex++] + vramX;
      final int destY = model.animationMetrics_d0[index][metricsIndex++] + vramY;
      final short w = (short)(model.animationMetrics_d0[index][metricsIndex++] / 4);
      final short h = model.animationMetrics_d0[index][metricsIndex++];

      //LAB_80022154
      for(int i = 0; i < model.usArr_ac[index]; i++) {
        metricsIndex += 2;
      }

      //LAB_80022164
      final short x2 = (short)(model.animationMetrics_d0[index][metricsIndex++] + vramX);
      final short y2 = (short)(model.animationMetrics_d0[index][metricsIndex++] + vramY);

      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x2, y2, destX & 0xffff, destY & 0xffff, w, h));

      model.usArr_ac[index]++;

      final int v1 = model.animationMetrics_d0[index][metricsIndex];
      if(v1 == -2) {
        model.animateTextures_ec[index] = false;
        model.usArr_ac[index] = 0;
      }

      //LAB_800221f8
      if(v1 == -1) {
        model.usArr_ac[index] = 0;
      }

      return;
    }

    //LAB_80022208
    int metricsIndex = 1;
    final int x = model.animationMetrics_d0[index][metricsIndex++] + vramX;
    final int y = model.animationMetrics_d0[index][metricsIndex++] + vramY;
    final int w = model.animationMetrics_d0[index][metricsIndex++] / 4;
    int h = model.animationMetrics_d0[index][metricsIndex++];
    final int copyMode = model.animationMetrics_d0[index][metricsIndex++];
    int secondaryYOffsetH = model.animationMetrics_d0[index][metricsIndex];

    if((model.usArr_ac[index] & 0xf) != 0) {
      model.usArr_ac[index]--;

      if(model.usArr_ac[index] == 0) {
        model.usArr_ac[index] = secondaryYOffsetH;
        secondaryYOffsetH = 16;
      } else {
        //LAB_80022278
        secondaryYOffsetH = 0;
      }
    }

    //LAB_8002227c
    if((short)secondaryYOffsetH == 0) {
      return;
    }

    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x & 0xffff, y & 0xffff, w, h));

    secondaryYOffsetH /= 16;
    h -= secondaryYOffsetH;

    if((short)copyMode == 0) {
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + h, 960, 256, w, secondaryYOffsetH));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, secondaryYOffsetH + 256, w, h));
    } else {
      //LAB_80022358
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, h + 256, w, secondaryYOffsetH));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + secondaryYOffsetH, 960, 256, w, h));
    }
    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final Model124 model, final int index) {
    if(model.animationMetrics_d0[index] == null) {
      model.animateTextures_ec[index] = false;
      return;
    }

    //LAB_80022490
    model.usArr_ac[index] = 0;
    model.usArr_ba[index] = model.animationMetrics_d0[index][0] & 0x3fff;

    //LAB_800224d0
    model.animateTextures_ec[index] = (model.animationMetrics_d0[index][0] & 0x8000) != 0;

    //LAB_800224d8
    if((model.animationMetrics_d0[index][0] & 0x4000) != 0) {
      model.usArr_ba[index] = 0x5678;
      model.usArr_ac[index] = model.animationMetrics_d0[index][6];
      model.animateTextures_ec[index] = true;
    }

    //LAB_80022510
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
}
