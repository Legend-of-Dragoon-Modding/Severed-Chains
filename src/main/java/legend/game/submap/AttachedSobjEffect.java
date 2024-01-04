package legend.game.submap;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.MeshObj;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptStorageParam;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.flEq;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class AttachedSobjEffect {
  private final Vector3f[] footprintQuadVertices_800d6b7c = {
    new Vector3f(-10.0f, 0.0f, -22.0f),
    new Vector3f( 10.0f, 0.0f, -22.0f),
    new Vector3f(-10.0f, 0.0f,  22.0f),
    new Vector3f( 10.0f, 0.0f,  22.0f),
    new Vector3f(-12.0f, 0.0f, - 8.0f),
    new Vector3f(- 2.0f, 0.0f, - 8.0f),
    new Vector3f(-12.0f, 0.0f,   8.0f),
    new Vector3f(- 2.0f, 0.0f,   8.0f),
    new Vector3f(  2.0f, 0.0f, - 8.0f),
    new Vector3f( 12.0f, 0.0f, - 8.0f),
    new Vector3f(  2.0f, 0.0f,   8.0f),
    new Vector3f( 12.0f, 0.0f,   8.0f),
  };
  private final int[] orthoTrailUs_800d6bdc = {96, 112, 0, 64};
  private final int[] dustTextureWidths_800d6bec = {15, 15, 23, 31};
  private final int[] dustTextureHeights_800d6bfc = {31, 31, 23, 31};
  private final int[] particleFadeDelay_800d6c0c = {120, 0, 120};

  public CContainer dustTmd;
  public TmdAnimationFile dustAnimation;
  private final Model124 tmdDustModel_800d4d40 = new Model124("Dust");
  private final List<OrthoTrailParticle54> orthoQuadTrail_800d4e68 = new ArrayList<>();
  private final List<TmdTrailParticle20> tmdTrail_800d4ec0 = new ArrayList<>();
  private final List<LawPodTrailSegment34> lawPodTrail_800d4f90 = new ArrayList<>();
  private int lawPodTrailCount_800f9e78;
  private final LawPodTrailData18[] lawPodTrailsData_800f9e7c = new LawPodTrailData18[8];

  private final MV transforms = new MV();
  private Obj tmdDust;
  private MeshObj footprints;
  private MeshObj quadDust;

  @Method(0x800f0e60L)
  private void initLawPodTrail() {
    this.lawPodTrail_800d4f90.clear();
    this.lawPodTrailCount_800f9e78 = 0;
  }

  public void initLawPodTrail(final RunningScript<?> script) {
    final ScriptState<?> state = script.scriptState_04;

    script.params_20[9] = new ScriptStorageParam(state, 0);

    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[state.storage_44[0]].innerStruct_00;
    if(script.params_20[0].get() == 0 || this.lawPodTrailCount_800f9e78 >= 8) {
      //LAB_800f1698
      sobj.attachedEffectData_1d0.shouldRenderLawPodTrail_18 = false;
    } else {
      //LAB_800f16a4
      final LawPodTrailData18 trail = this.addLawPodTrail();
      trail.maxCountSegments_00 = script.params_20[1].get();
      trail.countSegments_01 = 0;
      trail.fadeDelay_02 = script.params_20[2].get();
      trail.countFadeSteps_04 = script.params_20[3].get();
      trail.maxTicks_06 = trail.fadeDelay_02 + trail.countFadeSteps_04;
      trail.width_08 = script.params_20[4].get();
      trail.translucency_0c = script.params_20[5].get();
      trail.colour_10.set(script.params_20[6].get(), script.params_20[7].get(), script.params_20[8].get()).div(255.0f);
      trail.currSegmentOriginVerts_14.zero();
      sobj.attachedEffectData_1d0.shouldRenderLawPodTrail_18 = script.params_20[0].get() == 1;
      sobj.attachedEffectData_1d0.trailData_3c = trail;
      this.lawPodTrailCount_800f9e78++;
    }
  }

  @Method(0x800f0370L)
  public void initAttachedSobjEffects() {
    initModel(this.tmdDustModel_800d4d40, this.dustTmd, this.dustAnimation);
    if(this.tmdDust == null) {
      this.tmdDust = TmdObjLoader.fromObjTable("DustTmd", this.dustTmd.tmdPtr_00.tmd.objTable[0]);
    }
    this.tmdTrail_800d4ec0.clear();
    if(this.footprints == null) {
      final PolyBuilder builder = new PolyBuilder("OrthoTrailParticles", GL_TRIANGLE_STRIP)
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_MINUS_F)
        .clut(992, 472)
        .vramPos(960, 256)
        .addVertex(this.footprintQuadVertices_800d6b7c[4])
        .uv(96, 0)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[5])
        .uv(112, 0)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[6])
        .uv(96, 32)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[7])
        .uv(112, 32)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[8])
        .uv(112, 0)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[9])
        .uv(128, 0)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[10])
        .uv(112, 32)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[11])
        .uv(128, 32)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[0])
        .clutOverride(960, 464)
        .uv(0, 64)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[1])
        .clutOverride(960, 464)
        .uv(24, 64)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[2])
        .clutOverride(960, 464)
        .uv(0, 88)
        .monochrome(1.0f)
        .addVertex(this.footprintQuadVertices_800d6b7c[3])
        .clutOverride(960, 464)
        .uv(24, 88)
        .monochrome(1.0f);
      this.footprints = builder.build();
    }
    this.orthoQuadTrail_800d4e68.clear();
    this.initLawPodTrail();
  }

  @Method(0x800f0440L)
  public void deallocateAttachedSobjEffects() {
    this.tmdTrail_800d4ec0.clear();
    this.orthoQuadTrail_800d4e68.clear();
    this.deallocateLawPodTrail();
  }

  @Method(0x800f047cL)
  public void renderAttachedSobjEffects(final int screenOffsetX, final int screenOffsetY) {
    if(!this.tmdTrail_800d4ec0.isEmpty()) {
      this.renderTmdTrail(screenOffsetX, screenOffsetY);
    }

    if(!this.orthoQuadTrail_800d4e68.isEmpty()) {
      this.renderOrthoQuadTrailEffects(screenOffsetX, screenOffsetY);
    }

    if(!this.lawPodTrail_800d4f90.isEmpty()) {
      this.renderLawPodTrail(screenOffsetX, screenOffsetY);
    }
  }

  private TmdTrailParticle20 addTmdTrailParticle() {
    TmdTrailParticle20 inst;
    for(int i = 0; i < this.tmdTrail_800d4ec0.size(); i++) {
      inst = this.tmdTrail_800d4ec0.get(i);
      if(!inst.used) {
        inst.used = true;
        return inst;
      }
    }

    inst = new TmdTrailParticle20();
    this.tmdTrail_800d4ec0.add(inst);
    return inst;
  }

  private OrthoTrailParticle54 addOrthoTrailParticle() {
    OrthoTrailParticle54 inst;
    for(int i = 0; i < this.orthoQuadTrail_800d4e68.size(); i++) {
      inst = this.orthoQuadTrail_800d4e68.get(i);
      if(!inst.used) {
        inst.used = true;
        return inst;
      }
    }

    inst = new OrthoTrailParticle54();
    this.orthoQuadTrail_800d4e68.add(inst);
    return inst;
  }

  private LawPodTrailSegment34 addLawPodTrailSegment() {
    LawPodTrailSegment34 inst;
    int i = 0;
    for(; i < this.lawPodTrail_800d4f90.size(); i++) {
      inst = this.lawPodTrail_800d4f90.get(i);
      if(!inst.used) {
        inst.used = true;
        return inst;
      }
    }

    inst = new LawPodTrailSegment34();
    this.lawPodTrail_800d4f90.add(inst);
    return inst;
  }

  @Method(0x800ef0f8L)
  public void tickAttachedSobjEffects(final Model124 model, final AttachedSobjEffectData40 data, final boolean unloadSubmapParticles, final int screenOffsetX, final int screenOffsetY) {
    if(!flEq(data.transfer_1e.x, model.coord2_14.coord.transfer.x) || !flEq(data.transfer_1e.y, model.coord2_14.coord.transfer.y) || !flEq(data.transfer_1e.z, model.coord2_14.coord.transfer.z)) {
      //LAB_800ef154
      if(data.shouldRenderTmdDust_04) {
        if(data.tick_00 % (data.instantiationIntervalDust_30 * (3 - vsyncMode_8007a3b8)) == 0) {
          final TmdTrailParticle20 inst = this.addTmdTrailParticle();

          inst.tick_00 = 0;
          inst.maxTicks_18 = data.maxTicks_38;

          final int size = data.size_28;
          if(size < 0) {
            inst.size_08 = -size;
            inst.stepSize_04 = -inst.stepSize_04 / 20.0f;
          } else if(size > 0) {
            //LAB_800ef1e0
            inst.size_08 = 0.0f;
            inst.stepSize_04 = size / 20.0f;
          } else {
            //LAB_800ef214
            inst.size_08 = 0.0f;
            inst.stepSize_04 = 0.0f;
          }

          //LAB_800ef21c
          inst.transfer.set(model.coord2_14.coord.transfer);
        }
      }

      //LAB_800ef240
      if(data.shouldRenderFootprints_08) {
        if(data.tick_00 % (data.instantiationIntervalFootprints_34 * (3 - vsyncMode_8007a3b8)) == 0) {
          //LAB_800ef394
          final OrthoTrailParticle54 inst = this.addOrthoTrailParticle();

          if(data.footprintMode_10 != 0) {
            //LAB_800ef3e8
            inst.renderMode_00 = 2;
            inst.textureIndex_02 = 2;

            //LAB_800ef3f8
          } else {
            inst.renderMode_00 = 0;
            inst.textureIndex_02 = data.textureIndexType1_1c;

            data.textureIndexType1_1c ^= 1;
          }

          //LAB_800ef3fc
          inst.x_18 = screenOffsetX;
          inst.y_1c = screenOffsetY;
          inst.tick_04 = 0;
          inst.maxTicks_06 = 150;

          final MV ls = new MV();
          GsGetLs(model.coord2_14, ls);
          GTE.setTransforms(ls);
          GsGetLw(model.coord2_14, inst.lw);

          final int textureIndex = inst.textureIndex_02;
          if(textureIndex == 0) {
            //LAB_800ef4b4
            //inst.lw.transfer.add(-12, 0, -8);
            inst.z_4c = RotTransPers4(this.footprintQuadVertices_800d6b7c[4], this.footprintQuadVertices_800d6b7c[5], this.footprintQuadVertices_800d6b7c[6], this.footprintQuadVertices_800d6b7c[7], inst.sxy0_20, inst.sxy1_28, inst.sxy2_30, inst.sxy3_38);
          } else if(textureIndex == 1) {
            //LAB_800ef484
            //LAB_800ef4b4
            //inst.lw.transfer.add(2, 0, -8);
            inst.z_4c = RotTransPers4(this.footprintQuadVertices_800d6b7c[8], this.footprintQuadVertices_800d6b7c[9], this.footprintQuadVertices_800d6b7c[10], this.footprintQuadVertices_800d6b7c[11], inst.sxy0_20, inst.sxy1_28, inst.sxy2_30, inst.sxy3_38);
          } else if(textureIndex == 3) {
            //LAB_800ef4a0
            //LAB_800ef4b4
            inst.z_4c = RotTransPers4(this.footprintQuadVertices_800d6b7c[0], this.footprintQuadVertices_800d6b7c[1], this.footprintQuadVertices_800d6b7c[2], this.footprintQuadVertices_800d6b7c[3], inst.sxy0_20, inst.sxy1_28, inst.sxy2_30, inst.sxy3_38);
          }

          //LAB_800ef4ec
          if(inst.z_4c < 41) {
            inst.z_4c = 41;
          }

          //LAB_800ef504
          inst.stepBrightness_40 = 0.5f / 30;
          inst.brightness_48 = 0.5f;
          data.transfer_1e.set(model.coord2_14.coord.transfer);
        }
      }

      //LAB_800ef520
      if(data.shouldRenderOrthoDust_0c) {
        if(data.tick_00 % (data.instantiationIntervalDust_30 * (3 - vsyncMode_8007a3b8)) == 0) {
          final OrthoTrailParticle54 inst = this.addOrthoTrailParticle();
          inst.renderMode_00 = 1;
          inst.textureIndex_02 = 3;
          inst.x_18 = screenOffsetX;
          inst.y_1c = screenOffsetY;

          final Vector3f vert0 = new Vector3f(-data.size_28, 0.0f, -data.size_28);
          final Vector3f vert1 = new Vector3f( data.size_28, 0.0f, -data.size_28);
          final Vector3f vert2 = new Vector3f(-data.size_28, 0.0f,  data.size_28);
          final Vector3f vert3 = new Vector3f( data.size_28, 0.0f,  data.size_28);

          inst.tick_04 = 0;
          inst.maxTicks_06 = data.maxTicks_38;

          final MV ls = new MV();
          GsGetLs(model.coord2_14, ls);
          GTE.setTransforms(ls);

          //TODO The real code actually passes the same reference for sxyz 1 and 2, is that a bug?
          inst.z_4c = RotTransPers4(vert0, vert1, vert2, vert3, inst.sxy0_20, inst.sxy1_28, inst.sxy2_30, inst.sxy3_38);

          if(inst.z_4c < 41) {
            inst.z_4c = 41;
          }

          //LAB_800ef6a0
          final float halfSize = (inst.sxy3_38.x - inst.sxy0_20.x) / 2.0f;
          inst.size_08 = halfSize;
          inst.sizeStep_0c = halfSize / data.maxTicks_38;

          inst.z0_26 = (inst.sxy3_38.x + inst.sxy0_20.x) / 2.0f;
          inst.z1_2e = (inst.sxy3_38.y + inst.sxy0_20.y) / 2.0f;

          inst.stepBrightness_40 = 0.5f / data.maxTicks_38;
          inst.brightness_48 = 0.5f;
        }
      }

      //LAB_800ef728
      if(data.shouldRenderLawPodTrail_18) {
        if(!unloadSubmapParticles) {
          this.tickLawPodTrail(model, data, screenOffsetX, screenOffsetY);
        }
      }
    }

    //LAB_800ef750
    data.tick_00++;
  }

  @Method(0x800f0644L)
  private void tickLawPodTrail(final Model124 model, final AttachedSobjEffectData40 data, final int screenOffsetX, final int screenOffsetY) {
    if((data.tick_00 % (3 - vsyncMode_8007a3b8)) == 0) {
      final LawPodTrailData18 trailData = data.trailData_3c;

      if(trailData.countSegments_01 < trailData.maxCountSegments_00) {
        final LawPodTrailSegment34 segment = this.addLawPodTrailSegment();
        TrailSegmentVertices14 newVerts = segment.endpointVerts23_28;

        final MV transforms = new MV();
        GsGetLs(model.coord2_14, transforms);

        PushMatrix();
        GTE.setTransforms(transforms);
        GTE.perspectiveTransform(-trailData.width_08, 0.0f, 0.0f);
        newVerts.vert0_00.x = GTE.getScreenX(2);
        newVerts.vert0_00.y = GTE.getScreenY(2);
        segment.z_20 = GTE.getScreenZ(3) / 4.0f;

        GTE.perspectiveTransform(trailData.width_08, 0.0f, 0.0f);
        newVerts.vert1_08.x = GTE.getScreenX(2);
        newVerts.vert1_08.y = GTE.getScreenY(2);
        segment.z_20 = GTE.getScreenZ(3) / 4.0f;
        PopMatrix();

        segment.tick_00 = 0;
        segment.tpage_04 = GetTPage(Bpp.BITS_4, Translucency.of(trailData.translucency_0c), 972, 320);
        this.initLawPodTrailSegmentColour(trailData, segment);
        newVerts.vert0_00.x -= screenOffsetX;
        newVerts.vert0_00.y -= screenOffsetY;
        newVerts.vert1_08.x -= screenOffsetX;
        newVerts.vert1_08.y -= screenOffsetY;

        if(trailData.countSegments_01 == 0) {
          trailData.currSegmentOriginVerts_14.set(newVerts);
          newVerts = new TrailSegmentVertices14();
          newVerts.set(trailData.currSegmentOriginVerts_14);
          segment.endpointVerts23_28.set(newVerts);
        }

        //LAB_800f0928
        segment.originVerts01_24.set(trailData.currSegmentOriginVerts_14);
        trailData.currSegmentOriginVerts_14.set(newVerts);
        segment.trailData_2c = trailData;
        trailData.countSegments_01++;
      }
    }
    //LAB_800f094c
  }

  @Method(0x800ef798L)
  private void renderTmdTrail(final int screenOffsetX, final int screenOffsetY) {
    //LAB_800ef7c8
    for(int i = this.tmdTrail_800d4ec0.size() - 1; i >= 0; i--) {
      final TmdTrailParticle20 inst = this.tmdTrail_800d4ec0.get(i);

      if(inst.tick_00 < inst.maxTicks_18 * (3 - vsyncMode_8007a3b8)) {
        //LAB_800ef804
        inst.transfer.y -= 1.0f / (3 - vsyncMode_8007a3b8);

        this.tmdDustModel_800d4d40.coord2_14.coord.transfer.set(inst.transfer);

        // In retail, the shrinking tail of the snow slide snow cloud animation occurs because size_08 is in .12
        // and RotMatrixXyz writes everything as shorts, which turns scale negative above 0x7fff (>=8.0f).
        // To implement this more smoothly, stepSize_04 is switched to negative once size_08 reaches 8.0f.
        if(inst.size_08 >= 8.0f) {
          inst.stepSize_04 = -inst.stepSize_04;
        }
        inst.size_08 += inst.stepSize_04 / (3 - vsyncMode_8007a3b8);

        this.tmdDustModel_800d4d40.coord2_14.transforms.scale.set(inst.size_08, inst.size_08, inst.size_08);

        applyModelRotationAndScale(this.tmdDustModel_800d4d40);
        if(this.tmdDust != null) {
          zOffset_1f8003e8 = this.tmdDustModel_800d4d40.zOffset_a0;
          tmdGp0Tpage_1f8003ec = this.tmdDustModel_800d4d40.tpage_108;
          GsGetLw(this.tmdDustModel_800d4d40.modelParts_00[0].coord2_04, this.transforms);

          RENDERER.queueModel(this.tmdDust, this.transforms)
            .screenspaceOffset(screenOffsetX + 8, -screenOffsetY)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }

        this.tmdDustModel_800d4d40.remainingFrames_9e = 0;
        this.tmdDustModel_800d4d40.interpolationFrameIndex = 0;

        this.tmdDustModel_800d4d40.modelParts_00[0].coord2_04.flg--;
        inst.tick_00++;
      } else {
        inst.used = false;
      }
      //LAB_800ef888
    }
    //LAB_800ef894
  }

  @Method(0x800ef8acL)
  private void renderOrthoQuadTrailEffects(final int screenOffsetX, final int screenOffsetY) {
    //LAB_800ef9cc
    for(int i = 0; i < this.orthoQuadTrail_800d4e68.size(); i++) {
      //LAB_800efa08
      final OrthoTrailParticle54 inst = this.orthoQuadTrail_800d4e68.get(i);
      if(inst.tick_04 < inst.maxTicks_06 * (3 - vsyncMode_8007a3b8)) {
        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        final int mode = inst.renderMode_00;
        if(mode == 0 || mode == 2) {
          //LAB_800efa44
          final int offsetX = screenOffsetX - inst.x_18;
          final int offsetY = screenOffsetY - inst.y_1c;

          cmd
            .pos(0, offsetX + inst.sxy0_20.x, offsetY + inst.sxy0_20.y)
            .pos(1, offsetX + inst.sxy1_28.x, offsetY + inst.sxy1_28.y)
            .pos(2, offsetX + inst.sxy2_30.x, offsetY + inst.sxy2_30.y)
            .pos(3, offsetX + inst.sxy3_38.x, offsetY + inst.sxy3_38.y);

          if(mode == 2) {
            cmd
              .clut(960, 464)
              .vramPos(960, 256)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_MINUS_F);
          } else {
            //LAB_800efb64
            cmd
              .clut(992, 472)
              .vramPos(960, 256)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_MINUS_F);
          }
        } else if(mode == 1) {
          //LAB_800efb7c
          inst.size_08 += inst.sizeStep_0c / (3 - vsyncMode_8007a3b8);
          inst.sxy0_20.x = inst.z0_26 - inst.size_08 / 2.0f;
          inst.sxy0_20.y = inst.z1_2e - inst.size_08 / 2.0f;
          final float x = screenOffsetX - inst.x_18 + inst.sxy0_20.x;
          final float y = screenOffsetY - inst.y_1c + inst.sxy0_20.y;

          cmd
            .pos(0, x, y)
            .pos(1, x + inst.size_08, y)
            .pos(2, x, y + inst.size_08)
            .pos(3, x + inst.size_08, y + inst.size_08);

          if((inst.tick_04 & 0x3) == 0) {
            inst.z1_2e -= 1.0f / (3 - vsyncMode_8007a3b8);
          }

          //LAB_800efc4c
          cmd
            .clut(960, 466)
            .vramPos(960, 256)
            .bpp(Bpp.BITS_4)
            .translucent(Translucency.B_PLUS_F);
        }

        //LAB_800efc64
        if(inst.tick_04 >= this.particleFadeDelay_800d6c0c[inst.renderMode_00] * (3 - vsyncMode_8007a3b8)) {
          inst.brightness_48 -= inst.stepBrightness_40 / (3 - vsyncMode_8007a3b8);

          if(inst.brightness_48 >= 1.0f || inst.brightness_48 < 0.0f) {
            inst.brightness_48 = 0.0f;
          }
        }

        //LAB_800efcb8
        final int v = inst.textureIndex_02 == 2 ? 64 : 0;
        cmd
          .monochrome(inst.brightness_48)
          .uv(0, this.orthoTrailUs_800d6bdc[inst.textureIndex_02], v)
          .uv(1, this.orthoTrailUs_800d6bdc[inst.textureIndex_02] + this.dustTextureWidths_800d6bec[inst.textureIndex_02], v)
          .uv(2, this.orthoTrailUs_800d6bdc[inst.textureIndex_02], v + this.dustTextureHeights_800d6bfc[inst.textureIndex_02])
          .uv(3, this.orthoTrailUs_800d6bdc[inst.textureIndex_02] + this.dustTextureWidths_800d6bec[inst.textureIndex_02], v + this.dustTextureHeights_800d6bfc[inst.textureIndex_02]);

        GPU.queueCommand(inst.z_4c, cmd);
        RENDERER.queueModel(this.footprints, inst.lw)
          .vertices(inst.textureIndex_02 * 4, 4)
          .monochrome(inst.brightness_48)
          .screenspaceOffset(screenOffsetX + 8, -screenOffsetY);
        inst.tick_04++;
      } else {
        inst.used = false;
      }
    }
    //LAB_800efe48
    //LAB_800efe54
  }

  @Method(0x800f0970L)
  private void renderLawPodTrail(final int screenOffsetX, final int screenOffsetY) {
    //LAB_800f09c0
    for(int i = 0; i < this.lawPodTrail_800d4f90.size(); i++) {
      final LawPodTrailSegment34 segment = this.lawPodTrail_800d4f90.get(i);
      final LawPodTrailData18 trailData = segment.trailData_2c;
      if(segment.tick_00 <= trailData.maxTicks_06) {
        final int tpage = segment.tpage_04;

        //LAB_800f0b04
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .translucent(Translucency.of(tpage >>> 5 & 0b11))
          .pos(0, screenOffsetX + segment.originVerts01_24.vert0_00.x, screenOffsetY + segment.originVerts01_24.vert0_00.y)
          .pos(1, screenOffsetX + segment.originVerts01_24.vert1_08.x, screenOffsetY + segment.originVerts01_24.vert1_08.y)
          .pos(2, screenOffsetX + segment.endpointVerts23_28.vert0_00.x, screenOffsetY + segment.endpointVerts23_28.vert0_00.y)
          .pos(3, screenOffsetX + segment.endpointVerts23_28.vert1_08.x, screenOffsetY + segment.endpointVerts23_28.vert1_08.y);

        if(segment.tick_00 > trailData.fadeDelay_02 - 1) {
          //LAB_800f0d0c
          segment.colour_14.sub(segment.colourStep_08);
          segment.colour_14.x = Math.max(0, segment.colour_14.x);
          segment.colour_14.y = Math.max(0, segment.colour_14.y);
          segment.colour_14.z = Math.max(0, segment.colour_14.z);
        }

        //LAB_800f0d18
        cmd.rgb((int)(segment.colour_14.x * 255), (int)(segment.colour_14.y * 255), (int)(segment.colour_14.z * 255));
        GPU.queueCommand(segment.z_20, cmd);

        segment.tick_00++;
      } else {
        segment.used = false;
        trailData.countSegments_01--;
      }
      //LAB_800f0d68
    }
    //LAB_800f0d74
    //LAB_800f0dc8
  }

  @Method(0x800f0df0L)
  private void initLawPodTrailSegmentColour(final LawPodTrailData18 trailData, final LawPodTrailSegment34 segment) {
    segment.colour_14.set(trailData.colour_10);
    segment.colourStep_08.set(segment.colour_14).div(trailData.countFadeSteps_04);
  }

  public void deallocateLawPodTrail(final RunningScript<?> script) {
    final ScriptState<?> state = script.scriptState_04;
    script.params_20[1] = new ScriptStorageParam(state, 0);

    if(script.params_20[0].get() == 1) {
      this.deallocateLawPodTrail();
      final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[state.storage_44[0]].innerStruct_00;
      sobj.attachedEffectData_1d0.shouldRenderLawPodTrail_18 = false;
    }
  }

  @Method(0x800f0e7cL)
  private void deallocateLawPodTrail() {
    this.lawPodTrail_800d4f90.clear();

    //LAB_800f0ec8
    //LAB_800f0edc
    //LAB_800f0efc
    this.clearLawPodTrailSegmentsList();
    this.lawPodTrailCount_800f9e78 = 0;
  }

  @Method(0x800f0f20L)
  private LawPodTrailData18 addLawPodTrail() {
    LawPodTrailData18 data = null;

    //LAB_800f0f3c
    for(int i = 0; i < 8; i++) {
      if(this.lawPodTrailsData_800f9e7c[i] == null) {
        data = new LawPodTrailData18();
        this.lawPodTrailsData_800f9e7c[i] = data;
        break;
      }
    }

    //LAB_800f0f78
    return data;
  }

  @Method(0x800f0fe8L)
  private void clearLawPodTrailSegmentsList() {
    for(int i = 0; i < 8; i++) {
      if(this.lawPodTrailsData_800f9e7c[i] != null) {
        this.lawPodTrailsData_800f9e7c[i] = null;
        this.lawPodTrailCount_800f9e78--;
      }
    }
  }
}
