package legend.game.combat.particles;

import legend.core.RenderEngine;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.TmdObjLoader;
import legend.game.combat.Battle;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;
import static legend.game.combat.particles.ParticleManager.particleSubCounts_800fb794;

public class TmdParticle extends ParticleEffectData98 {
  private TmdObjTable1c tmd_30;

  /** ushort */
  private int tpage_56;

  public TmdParticle(final ParticleManager manager, @Nullable final BattleObject parentBobj, final ParticleEffectData98Inner24 effectInner, final int type, final int count) {
    super(manager, parentBobj, effectInner, type, count);
  }

  @Override
  protected void init(final int flags) {
    this.countParticleSub_54 = 0;

    if((flags & 0xf_ff00) == 0xf_ff00) {
      this.tmd_30 = deffManager_800c693c.tmds_2f8[flags & 0xff];
      this.tpage_56 = 0x20;
    } else {
      //LAB_80101d98
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(0x300_0000 | flags & 0xf_ffff);
      final CContainer extTmd = tmdType.tmd_0c;
      final TmdWithId tmd = extTmd.tmdPtr_00;
      this.tmd_30 = tmd.tmd.objTable[0];
      this.tpage_56 = (int)((tmd.id & 0xffff_0000L) >>> 11);
    }

    //LAB_80101dd8
    if((this.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
      this.countParticleSub_54 = particleSubCounts_800fb794[(this.effectInner_08.particleInnerStuff_1c & 0x6000_0000) >>> 28];

      //LAB_80101e3c
      for(int i = 0; i < this.countParticleInstance_50; i++) {
        this.particleArray_68[i].subParticlePositionsArray_44 = new Vector3f[this.countParticleSub_54];
        Arrays.setAll(this.particleArray_68[i].subParticlePositionsArray_44, n -> new Vector3f());
      }
    }

    //LAB_80101e6c
  }

  @Override
  protected void reinitInstanceType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst) {

  }

  @Method(0x800fc4bcL)
  private void FUN_800fc4bc(final MV out, final ParticleMetrics48 particleMetrics) {
    out.rotationXYZ(particleMetrics.rotation_38);
    out.transfer.set(particleMetrics.translation_18);
    out.scaleLocal(particleMetrics.scale_28);
  }

  /**
   * Used by particle renderer index 1 (renders ice chunk particles and ???). Used renderCtmd
   * Seems to involve lit TMDs.
   */
  @Method(0x800fcf20L)
  private void renderTmdParticle(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final TmdObjTable1c tmd, final Obj obj, final ParticleMetrics48 particleMetrics, final int tpage) {
    if(particleMetrics.flags_00 >= 0) {
      final MV transforms = new MV();
      this.FUN_800fc4bc(transforms, particleMetrics);
      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e61e4(particleMetrics.colour0_40.x, particleMetrics.colour0_40.y, particleMetrics.colour0_40.z);
      }

      //LAB_800fcf94
      GsSetLightMatrix(transforms);
      final MV transformMatrix = new MV();

      if(RenderEngine.legacyMode != 0) {
        transforms.compose(worldToScreenMatrix_800c3548, transformMatrix);
      } else {
        transformMatrix.set(transforms);
      }

      if((particleMetrics.flags_00 & 0x400_0000) == 0) {
        transformMatrix.rotationXYZ(manager.params_10.rot_10);
        transformMatrix.scaleLocal(manager.params_10.scale_16);
      }

      //LAB_800fcff8
      GTE.setTransforms(transformMatrix);
      zOffset_1f8003e8 = 0;
      if((manager.params_10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
      } else {
        //LAB_800fd038
        tmdGp0Tpage_1f8003ec = tpage;
      }

      //LAB_800fd040
      final ModelPart10 dobj = new ModelPart10();
      dobj.attribute_00 = particleMetrics.flags_00;
      dobj.tmd_08 = tmd;

      final int oldZShift = zShift_1f8003c4;
      final int oldZMax = zMax_1f8003cc;
      final int oldZMin = zMin;
      zShift_1f8003c4 = 2;
      zMax_1f8003cc = 0xffe;
      zMin = 0xb;
      Renderer.renderDobj2(dobj, false, 0x20);
      zShift_1f8003c4 = oldZShift;
      zMax_1f8003cc = oldZMax;
      zMin = oldZMin;

      RENDERER.queueModel(obj, transformMatrix)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour)
        .ctmdFlags((dobj.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0)
        .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
        .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);

      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }
    //LAB_800fd064
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    final EffectManagerData6c<EffectManagerParams.ParticleType> manager = state.innerStruct_00;
    this.countFramesRendered_52++;

    if(this.obj == null) {
      this.obj = TmdObjLoader.fromObjTable("Particle", this.tmd_30);
    }

    //LAB_800fd660
    for(int i = 0; i < this.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = this.particleArray_68[i];

      if(inst.tick(manager)) {
        inst.beforeRender(manager);
        final Vector3f colour = new Vector3f();
        inst.tickAttributes(manager, colour);

        final Vector3f rotatedAndTranslatedPosition = new Vector3f();
        rotateAndTranslateEffect(manager, null, inst.particlePosition_50, rotatedAndTranslatedPosition);

        final ParticleMetrics48 particleMetrics = new ParticleMetrics48();
        particleMetrics.flags_00 = manager.params_10.flags_00;
        particleMetrics.translation_18.set(inst.managerTranslation_2c).add(rotatedAndTranslatedPosition);

        particleMetrics.scale_28.set(
          manager.params_10.scale_16.x + inst.scaleHorizontal_06,
          manager.params_10.scale_16.y + inst.scaleVertical_08,
          manager.params_10.scale_16.x + inst.scaleHorizontal_06 // This is correct
        );

        particleMetrics.rotation_38.set(inst.spriteRotation_70).add(inst.managerRotation_68);
        particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
        particleMetrics.colour1_44.set(0, 0, 0);
        this.renderTmdParticle(manager, this.tmd_30, this.obj, particleMetrics, this.tpage_56);
      }
      //LAB_800fd7e0
    }

    //LAB_800fd7fc
    if(this.scaleOrUseEffectAcceleration_6c) {
      this.effectAcceleration_70.mul(this.scaleParticleAcceleration_80);
    }
    //LAB_800fd858
  }
}
