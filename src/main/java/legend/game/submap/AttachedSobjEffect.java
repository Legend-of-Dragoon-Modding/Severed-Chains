package legend.game.submap;

import legend.core.QueuedModelStandard;
import legend.core.QueuedModelTmd;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.PoolList;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptStorageParam;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class AttachedSobjEffect {
  public CContainer dustTmd;
  public TmdAnimationFile dustAnimation;
  private final Model124 tmdDustModel_800d4d40 = new Model124("Dust");
  private final PoolList<FootprintParticle54> footprintTrail = new PoolList<>(FootprintParticle54::new);
  private final PoolList<OrthoDustParticle54> orthoDustTrail_800d4e68 = new PoolList<>(OrthoDustParticle54::new);
  private final PoolList<TmdTrailParticle20> tmdTrail_800d4ec0 = new PoolList<>(TmdTrailParticle20::new);
  private final PoolList<LawPodTrailSegment34> lawPodTrail_800d4f90 = new PoolList<>(LawPodTrailSegment34::new);
  private int lawPodTrailCount_800f9e78;
  private final LawPodTrailData18[] lawPodTrailsData_800f9e7c = new LawPodTrailData18[8];

  private Obj tmdDust;
  private Obj footprints;
  private Obj quadDust;
  private final MV transforms = new MV();

  // TODO Still need to implement law pod trail in opengl, but requires custom shader

  @Method(0x800f0370L)
  public void initAttachedSobjEffects() {
    this.tmdTrail_800d4ec0.clear();
    initModel(this.tmdDustModel_800d4d40, this.dustTmd, this.dustAnimation);
    if(this.tmdDust == null) {
      this.tmdDust = TmdObjLoader.fromObjTable("DustTmd", this.dustTmd.tmdPtr_00.tmd.objTable[0]);
    }

    this.footprintTrail.clear();
    if(this.footprints == null) {
      this.footprints = new PolyBuilder("Footprints", GL_TRIANGLE_STRIP)
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_MINUS_F)
        .addVertex(-12.0f, 0.0f, - 8.0f)
        .clut(992, 472)
        .vramPos(960, 256)
        .uv(96, 0)
        .monochrome(1.0f)
        .addVertex(- 2.0f, 0.0f, - 8.0f)
        .uv(112, 0)
        .monochrome(1.0f)
        .addVertex(-12.0f, 0.0f,   8.0f)
        .uv(96, 32)
        .monochrome(1.0f)
        .addVertex(- 2.0f, 0.0f,   8.0f)
        .uv(112, 32)
        .monochrome(1.0f)
        .addVertex(  2.0f, 0.0f, - 8.0f)
        .uv(112, 0)
        .monochrome(1.0f)
        .addVertex( 12.0f, 0.0f, - 8.0f)
        .uv(128, 0)
        .monochrome(1.0f)
        .addVertex(  2.0f, 0.0f,   8.0f)
        .uv(112, 32)
        .monochrome(1.0f)
        .addVertex( 12.0f, 0.0f,   8.0f)
        .uv(128, 32)
        .monochrome(1.0f)
        .addVertex(-10.0f, 0.0f, -22.0f)
        .clut(960, 464)
        .uv(0, 64)
        .monochrome(1.0f)
        .addVertex( 10.0f, 0.0f, -22.0f)
        .uv(24, 64)
        .monochrome(1.0f)
        .addVertex(-10.0f, 0.0f,  22.0f)
        .uv(0, 88)
        .monochrome(1.0f)
        .addVertex( 10.0f, 0.0f,  22.0f)
        .uv(24, 88)
        .monochrome(1.0f)
        .build();
    }

    this.orthoDustTrail_800d4e68.clear();
    if(this.quadDust == null) {
      this.quadDust = new QuadBuilder("DustQuad")
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .clut(960, 465)
        .vramPos(960, 256)
        .monochrome(1.0f)
        .uv(64, 0)
        .uvSize(32, 32)
        .posSize(1.0f, 1.0f)
        .build();
    }

    this.lawPodTrail_800d4f90.clear();
    this.lawPodTrailCount_800f9e78 = 0;
  }

  /** Script method to initialize law pod trail. */
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

  @Method(0x800ef0f8L)
  public void tickAttachedSobjEffects(final Model124 model, final AttachedSobjEffectData40 data, final boolean unloadSubmapParticles, final float screenOffsetX, final float screenOffsetY) {
    if(!flEq(data.transfer_1e.x, model.coord2_14.coord.transfer.x) || !flEq(data.transfer_1e.y, model.coord2_14.coord.transfer.y) || !flEq(data.transfer_1e.z, model.coord2_14.coord.transfer.z)) {
      //LAB_800ef154
      if(data.shouldRenderTmdDust_04) {
        if(data.tick_00 % (data.instantiationIntervalDust_30 * (3 - vsyncMode_8007a3b8)) == 0) {
          final TmdTrailParticle20 inst = this.tmdTrail_800d4ec0.get();

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
          final FootprintParticle54 inst = this.footprintTrail.get();

          if(data.footprintMode_10 != 0) {
            //LAB_800ef3e8
            inst.textureIndex_02 = 2;

            //LAB_800ef3f8
          } else {
            inst.textureIndex_02 = data.textureIndexType1_1c;

            data.textureIndexType1_1c ^= 1;
          }

          //LAB_800ef3fc
          inst.tick_04 = 0;
          inst.maxTicks_06 = 150;
          GsGetLw(model.coord2_14, inst.transforms);

          //LAB_800ef504
          inst.stepBrightness_40 = 0.5f / 30;
          inst.brightness_48 = 0.5f;
        }
      }

      //LAB_800ef520
      if(data.shouldRenderOrthoDust_0c) {
        if(data.tick_00 % (data.instantiationIntervalDust_30 * (3 - vsyncMode_8007a3b8)) == 0) {
          final OrthoDustParticle54 inst = this.orthoDustTrail_800d4e68.get();
          inst.x_18 = screenOffsetX;
          inst.y_1c = screenOffsetY;

          inst.tick_04 = 0;
          inst.maxTicks_06 = data.maxTicks_38;

          GsGetLs(model.coord2_14, inst.transforms);
          GTE.setTransforms(inst.transforms);

          final Vector3f vert0 = new Vector3f(-data.size_28, 0.0f, -data.size_28);
          final Vector3f vert1 = new Vector3f( data.size_28, 0.0f, -data.size_28);
          final Vector3f vert2 = new Vector3f(-data.size_28, 0.0f,  data.size_28);
          final Vector3f vert3 = new Vector3f( data.size_28, 0.0f,  data.size_28);
          inst.z_4c = RotTransPers4(vert0, vert1, vert2, vert3, inst.sxy0_20, inst.sxy1_28, inst.sxy2_30, inst.sxy3_38);

          if(inst.z_4c < 41) {
            inst.z_4c = 41;
          }

          //LAB_800ef6a0
          final float halfSize = (inst.sxy3_38.x - inst.sxy0_20.x) / 2.0f;
          inst.size_08 = halfSize;
          inst.sizeStep_0c = halfSize / data.maxTicks_38;

          inst.centerX_26 = (inst.sxy3_38.x + inst.sxy0_20.x) / 2.0f;
          inst.centerY_2e = (inst.sxy3_38.y + inst.sxy0_20.y) / 2.0f;

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
    data.transfer_1e.set(model.coord2_14.coord.transfer);
    data.tick_00++;
  }

  @Method(0x800f0644L)
  private void tickLawPodTrail(final Model124 model, final AttachedSobjEffectData40 data, final float screenOffsetX, final float screenOffsetY) {
    if((data.tick_00 % (3 - vsyncMode_8007a3b8)) == 0) {
      final LawPodTrailData18 trailData = data.trailData_3c;

      if(trailData.countSegments_01 < trailData.maxCountSegments_00) {
        final LawPodTrailSegment34 segment = this.lawPodTrail_800d4f90.get();
        TrailSegmentVertices14 newVerts = segment.endpointVerts23_28;

        final MV transforms = new MV();
        GsGetLs(model.coord2_14, transforms);

        PushMatrix();
        GTE.setTransforms(transforms);
        GTE.perspectiveTransform(-trailData.width_08, 0.0f, 0.0f);
        newVerts.vert0_00.x = GTE.getScreenX(2);
        newVerts.vert0_00.y = GTE.getScreenY(2);
        segment.z_20 = GTE.getScreenZ(3);

        GTE.perspectiveTransform(trailData.width_08, 0.0f, 0.0f);
        newVerts.vert1_08.x = GTE.getScreenX(2);
        newVerts.vert1_08.y = GTE.getScreenY(2);
        segment.z_20 = GTE.getScreenZ(3);
        PopMatrix();

        segment.tick_00 = 0;
        segment.tpage_04 = GetTPage(Bpp.BITS_4, Translucency.of(trailData.translucency_0c), 972, 320);
        segment.colour_14.set(trailData.colour_10);
        segment.colourStep_08.set(segment.colour_14).div(trailData.countFadeSteps_04);
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

  @Method(0x800f047cL)
  public void renderAttachedSobjEffects(final float screenOffsetX, final float screenOffsetY) {
    if(!this.tmdTrail_800d4ec0.isEmpty()) {
      this.renderTmdTrail(screenOffsetX, screenOffsetY);
    }

    if(!this.footprintTrail.isEmpty()) {
      this.renderFootprints(screenOffsetX, screenOffsetY);
    }

    if(!this.orthoDustTrail_800d4e68.isEmpty()) {
      this.renderOrthoDustTrailEffect(screenOffsetX, screenOffsetY);
    }

    if(!this.lawPodTrail_800d4f90.isEmpty()) {
      this.renderLawPodTrail(screenOffsetX, screenOffsetY);
    }
  }

  @Method(0x800ef798L)
  private void renderTmdTrail(final float screenOffsetX, final float screenOffsetY) {
    //LAB_800ef7c8
    for(int i = 0; i < this.tmdTrail_800d4ec0.size(); i++) {
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
        zOffset_1f8003e8 = this.tmdDustModel_800d4d40.zOffset_a0;
        tmdGp0Tpage_1f8003ec = this.tmdDustModel_800d4d40.tpage_108;
        GsGetLw(this.tmdDustModel_800d4d40.modelParts_00[0].coord2_04, inst.transforms);

        RENDERER.queueModel(this.tmdDust, inst.transforms, QueuedModelTmd.class)
          .screenspaceOffset(GPU.getOffsetX() + GTE.getScreenOffsetX() - 184, GPU.getOffsetY() + GTE.getScreenOffsetY() - 120)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour)
          .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11);

        this.tmdDustModel_800d4d40.remainingFrames_9e = 0;

        this.tmdDustModel_800d4d40.modelParts_00[0].coord2_04.flg--;
        inst.tick_00++;
      } else {
        inst.free();
      }
      //LAB_800ef888
    }
    //LAB_800ef894
  }

  private void renderFootprints(final float screenOffsetX, final float screenOffsetY) {
    for(int i = 0; i < this.footprintTrail.size(); i++) {
      final FootprintParticle54 inst = this.footprintTrail.get(i);
      if(inst.tick_04 < inst.maxTicks_06 * (3 - vsyncMode_8007a3b8)) {
        if(inst.tick_04 >= 120 * (3 - vsyncMode_8007a3b8)) {
          inst.brightness_48 -= inst.stepBrightness_40 / (3 - vsyncMode_8007a3b8);

          if(inst.brightness_48 >= 1.0f || inst.brightness_48 < 0.0f) {
            inst.brightness_48 = 0.0f;
          }
        }

        RENDERER.queueModel(this.footprints, inst.transforms, QueuedModelStandard.class)
          .vertices(inst.textureIndex_02 * 4, 4)
          .monochrome(inst.brightness_48)
          .screenspaceOffset(GPU.getOffsetX() + GTE.getScreenOffsetX() - 184, GPU.getOffsetY() + GTE.getScreenOffsetY() - 120);
        inst.tick_04++;
      } else {
        inst.free();
      }
    }
  }

  private void renderOrthoDustTrailEffect(final float screenOffsetX, final float screenOffsetY) {
    //LAB_800ef9cc
    for(int i = 0; i < this.orthoDustTrail_800d4e68.size(); i++) {
      //LAB_800efa08
      final OrthoDustParticle54 inst = this.orthoDustTrail_800d4e68.get(i);
      if(inst.tick_04 < inst.maxTicks_06 * (3 - vsyncMode_8007a3b8)) {
        //LAB_800efc64
        inst.brightness_48 -= inst.stepBrightness_40 / (3 - vsyncMode_8007a3b8);

        if(inst.brightness_48 >= 1.0f || inst.brightness_48 < 0.0f) {
          inst.brightness_48 = 0.0f;
        }

        //LAB_800efb7c
        inst.size_08 += inst.sizeStep_0c / (3 - vsyncMode_8007a3b8);
        inst.sxy0_20.x = inst.centerX_26 - inst.size_08 / 2.0f;
        inst.sxy0_20.y = inst.centerY_2e - inst.size_08 / 2.0f;

        inst.centerY_2e -= 0.25f / (3 - vsyncMode_8007a3b8);

        inst.transforms.scaling(inst.size_08);
        inst.transforms.transfer.set(GPU.getOffsetX() + screenOffsetX - inst.x_18 + inst.sxy0_20.x, GPU.getOffsetY() + screenOffsetY - inst.y_1c + inst.sxy0_20.y, inst.z_4c * 4.0f);
        RENDERER.queueOrthoModel(this.quadDust, inst.transforms, QueuedModelStandard.class)
          .monochrome(inst.brightness_48);
        inst.tick_04++;
      } else {
        inst.free();
      }
    }
    //LAB_800efe48
    //LAB_800efe54
  }

  @Method(0x800f0970L)
  private void renderLawPodTrail(final float screenOffsetX, final float screenOffsetY) {
    float averageZ = 0.0f;
    int countZ = 0;
    for(int i = 0; i < this.lawPodTrail_800d4f90.size(); i++) {
      final LawPodTrailSegment34 segment = this.lawPodTrail_800d4f90.get(i);
      if(segment.tick_00 <= segment.trailData_2c.maxTicks_06) {
        averageZ += segment.z_20;
        countZ++;
      }
    }

    averageZ /= countZ;

    final PolyBuilder builder = new PolyBuilder("Law pod trail", GL_TRIANGLES);

    //LAB_800f09c0
    for(int i = 0; i < this.lawPodTrail_800d4f90.size(); i++) {
      final LawPodTrailSegment34 segment = this.lawPodTrail_800d4f90.get(i);
      final LawPodTrailData18 trailData = segment.trailData_2c;
      if(segment.tick_00 <= trailData.maxTicks_06) {
        //LAB_800f0b04
        if(segment.tick_00 > trailData.fadeDelay_02 - 1) {
          //LAB_800f0d0c
          segment.colour_14.sub(segment.colourStep_08);
          segment.colour_14.x = Math.max(0, segment.colour_14.x);
          segment.colour_14.y = Math.max(0, segment.colour_14.y);
          segment.colour_14.z = Math.max(0, segment.colour_14.z);
        }

        //LAB_800f0d18

        builder
          .translucency(Translucency.of(segment.tpage_04 >>> 5 & 0b11))
          .addVertex(segment.originVerts01_24.vert0_00.x, segment.originVerts01_24.vert0_00.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
          .addVertex(segment.originVerts01_24.vert1_08.x, segment.originVerts01_24.vert1_08.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
          .addVertex(segment.endpointVerts23_28.vert0_00.x, segment.endpointVerts23_28.vert0_00.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
          .addVertex(segment.originVerts01_24.vert1_08.x, segment.originVerts01_24.vert1_08.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
          .addVertex(segment.endpointVerts23_28.vert0_00.x, segment.endpointVerts23_28.vert0_00.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
          .addVertex(segment.endpointVerts23_28.vert1_08.x, segment.endpointVerts23_28.vert1_08.y, segment.z_20 - averageZ)
          .rgb(segment.colour_14)
        ;

        segment.tick_00++;
      } else {
        segment.free();
        trailData.countSegments_01--;
      }
    }

    final Obj obj = builder.build();
    obj.delete();

    this.transforms.transfer.set(GPU.getOffsetX() + screenOffsetX, GPU.getOffsetY() + screenOffsetY, averageZ);
    RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
  }

  @Method(0x800f0440L)
  public void deallocateAttachedSobjEffects() {
    this.tmdTrail_800d4ec0.clear();
    if(this.tmdDust != null) {
      this.tmdDust.delete();
      this.tmdDust = null;
    }

    this.footprintTrail.clear();
    if(this.footprints != null) {
      this.footprints.delete();
      this.footprints = null;
    }

    this.orthoDustTrail_800d4e68.clear();
    if(this.quadDust != null) {
      this.quadDust.delete();
      this.quadDust = null;
    }

    this.deallocateLawPodTrail();
  }

  /** Script method to deallocate law pod trail individually. */
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
    this.lawPodTrailCount_800f9e78 = 0;
    for(int i = 0; i < 8; i++) {
      if(this.lawPodTrailsData_800f9e7c[i] != null) {
        this.lawPodTrailsData_800f9e7c[i] = null;
        this.lawPodTrailCount_800f9e78--;
      }
    }
  }

  public static class AttachedSobjEffectData40 {
    public int tick_00;
    public boolean shouldRenderTmdDust_04;
    public boolean shouldRenderFootprints_08;
    public boolean shouldRenderOrthoDust_0c;
    public int footprintMode_10;

    public boolean shouldRenderLawPodTrail_18;
    public int textureIndexType1_1c;
    public final Vector3f transfer_1e = new Vector3f();

    public int size_28;
    public int oldFootprintInstantiationInterval_2c;
    public int instantiationIntervalDust_30;
    public int instantiationIntervalFootprints_34;
    /** short */
    public int maxTicks_38;

    public LawPodTrailData18 trailData_3c;

    public AttachedSobjEffectData40() {
      this.tick_00 = 0;
      this.shouldRenderTmdDust_04 = false;
      this.shouldRenderFootprints_08 = false;
      this.shouldRenderOrthoDust_0c = false;
      this.footprintMode_10 = 0;
      this.shouldRenderLawPodTrail_18 = false;
      this.textureIndexType1_1c = 0;
      this.transfer_1e.zero();
      this.size_28 = 0;
      this.oldFootprintInstantiationInterval_2c = 0;
      this.instantiationIntervalDust_30 = 0;
      this.instantiationIntervalFootprints_34 = 0;
      this.maxTicks_38 = 0;
      this.trailData_3c = null;
    }
  }

  /** Used for dust trail attached sobj effect particles that use TMD models. */
  public static class TmdTrailParticle20 implements PoolList.Usable {
    private boolean used;
    public MV transforms = new MV();

    public int tick_00;
    public float stepSize_04;
    public float size_08;
    public final Vector3f transfer = new Vector3f();
    public int maxTicks_18;
    //public TmdTrailParticle20 next_1c;

    public TmdTrailParticle20() {
      this.use();
    }

    @Override
    public boolean used() {
      return this.used;
    }

    @Override
    public void use() {
      this.used = true;
    }

    @Override
    public void free() {
      this.used = false;
    }
  }

  /** Used for ortho quad trail attached sobj effect particles (footprints and some kinds of dust). */
  public static class OrthoTrailParticle54 implements PoolList.Usable {
    private boolean used;
    public MV transforms = new MV();

    // public int renderMode_00;
    public int tick_04;
    public int maxTicks_06;
    // Appears to just be a second size attribute
    // public float _10;

    /** 16.16 fixed-point */
    public float stepBrightness_40;
    // /** 16.16 fixed-point */
    // public int brightnessAccumulator_44; // No longer necessary
    public float brightness_48;
    // public OrthoTrailParticle54 next_50;

    public OrthoTrailParticle54() {
      this.use();
    }

    @Override
    public boolean used() {
      return this.used;
    }

    @Override
    public void use() {
      this.used = true;
    }

    @Override
    public void free() {
      this.used = false;
    }
  }

  /** Split off from original OrthoTrailParticle54 */
  public static class FootprintParticle54 extends OrthoTrailParticle54 {
    public int textureIndex_02;
  }

  /** Split off from original OrthoTrailParticle54 */
  public static class OrthoDustParticle54 extends OrthoTrailParticle54 {
    public float size_08;
    public float sizeStep_0c;

    public float x_18;
    public float y_1c;
    public final Vector2f sxy0_20 = new Vector2f();
    public float centerX_26;
    public final Vector2f sxy1_28 = new Vector2f();
    public float centerY_2e;
    public final Vector2f sxy2_30 = new Vector2f();
    public final Vector2f sxy3_38 = new Vector2f();

    public float z_4c;
  }

  public static class LawPodTrailSegment34 implements PoolList.Usable {
    private boolean used;

    /** short */
    public int tick_00;

    public int tpage_04;
    public Vector3f colourStep_08 = new Vector3f();
    public Vector3f colour_14 = new Vector3f();
    public float z_20;
    public TrailSegmentVertices14 originVerts01_24 = new TrailSegmentVertices14();
    public TrailSegmentVertices14 endpointVerts23_28 = new TrailSegmentVertices14();
    public LawPodTrailData18 trailData_2c;
    // public LawPodTrailSegment34 next_30;

    public LawPodTrailSegment34() {
      this.use();
    }

    @Override
    public boolean used() {
      return this.used;
    }

    @Override
    public void use() {
      this.used = true;
    }

    @Override
    public void free() {
      this.used = false;
    }
  }

  public static class LawPodTrailData18 {
    public int maxCountSegments_00;
    public int countSegments_01;
    /** short */
    public int fadeDelay_02;
    /** short */
    public int countFadeSteps_04;
    /** short */
    public int maxTicks_06;
    /** short */
    public int width_08;

    public int translucency_0c;
    /** was 3 bytes */
    public Vector3f colour_10 = new Vector3f();

    public TrailSegmentVertices14 currSegmentOriginVerts_14 = new TrailSegmentVertices14();
  }

  public static class TrailSegmentVertices14 {
    public boolean used;

    public final Vector2f vert0_00 = new Vector2f();

    public final Vector2f vert1_08 = new Vector2f();

    // public TrailSegmentVertices14 next_10;

    public void set(final TrailSegmentVertices14 other) {
      this.vert0_00.set(other.vert0_00);
      this.vert1_08.set(other.vert1_08);
    }

    public void zero() {
      this.vert0_00.set(0.0f, 0.0f);
      this.vert1_08.set(0.0f, 0.0f);
    }
  }
}
