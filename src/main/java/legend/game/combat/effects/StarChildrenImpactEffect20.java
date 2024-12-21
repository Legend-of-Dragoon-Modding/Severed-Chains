package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelBattleTmd;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.TmdObjLoader;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.calculateEffectTransforms;

public class StarChildrenImpactEffect20 implements Effect<EffectManagerParams.VoidType> {
  // public int count_00;
  private int currentFrame_04;
  public final StarChildrenImpactEffectInstancea8[] impactArray_08;

  private final MV modelTransforms = new MV();
  private final MV w2sModelTransforms = new MV();
  private final Vector3f scale = new Vector3f();
  private final MV lightingTransforms = new MV();

  private Obj shockwaveObj;
  private Obj explosionObj;
  private Obj plumeObj;

  public StarChildrenImpactEffect20(final int count) {
    this.impactArray_08 = new StarChildrenImpactEffectInstancea8[count];
    Arrays.setAll(this.impactArray_08, StarChildrenImpactEffectInstancea8::new);
  }

  @Override
  @Method(0x8010f124L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    //LAB_8010f168
    for(int i = 0; i < this.impactArray_08.length; i++) {
      final StarChildrenImpactEffectInstancea8 impact = this.impactArray_08[i];

      if(this.currentFrame_04 > impact.startingFrame_04) {
        if(impact.explosionHeightAngle_a0 >= MathHelper.TWO_PI) {
          impact.renderImpact_00 = false;
        } else {
          //LAB_8010f19c
          final int currentAnimFrame = impact.animationFrame_a2;

          // Stage 0
          if(currentAnimFrame < 9) {
            //LAB_8010f240
            impact.renderImpact_00 = true;
            impact.renderShockwave_01 = true;
            impact.rotation_2c[0].y += MathHelper.TWO_PI / 32.0f;
            impact.scale_6c[0].x += 0.15f;
            impact.scale_6c[0].z += 0.15f;
            impact.explosionHeightAngle_a0 += MathHelper.TWO_PI / 20.0f;

            final float explosionHeight = MathHelper.sin(impact.explosionHeightAngle_a0) * 1.375f;
            impact.scale_6c[0].y = Math.max(explosionHeight, 0.0f);

            //LAB_8010f2a8
            impact.opacity_8c[0].x -= 23;
            impact.opacity_8c[0].y -= 23;
            impact.opacity_8c[0].z -= 23;
          } else { // Stage 1
            if(currentAnimFrame == 9) {
              impact.translation_0c[0].y = -0x800;
              impact.scale_6c[0].x = 6.5f;
              impact.scale_6c[0].z = 6.5f;
            }

            //LAB_8010f1dc
            // Start transforming plume
            if(currentAnimFrame >= 10) {
              if(currentAnimFrame == 10) {
                impact.renderShockwave_01 = false;
              }

              //LAB_8010f1f0
              impact.rotation_2c[1].y += MathHelper.TWO_PI / 32.0f;
              impact.explosionHeightAngle_a0 += MathHelper.TWO_PI / 8.0f;
              impact.scale_6c[1].x -= 0.109375f;
              impact.scale_6c[1].y += 0.375f;
              impact.scale_6c[1].z -= 0.109375f;
            }
            //LAB_8010f22c
          }
          impact.animationFrame_a2++;
        }
      }
    }

    //LAB_8010f2f4
    this.currentFrame_04++;

    if(this.impactArray_08.length == 0) {
      state.deallocateWithChildren();
    }

    //LAB_8010f31c
  }

  /** Used renderCtmd */
  @Override
  @Method(0x8010f340L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(manager.params_10.flags_00 >= 0) {
      final ModelPart10 dobj = new ModelPart10();

      //LAB_8010f3a4
      for(int i = 0; i < this.impactArray_08.length; i++) {
        final StarChildrenImpactEffectInstancea8 impact = this.impactArray_08[i];

        if(impact.renderImpact_00) {
          calculateEffectTransforms(this.lightingTransforms, manager);
          final int stageNum = impact.animationFrame_a2 >= 10 ? 1 : 0;
          final float scaleX = impact.scale_6c[stageNum].x * manager.params_10.scale_16.x;
          final float scaleY = impact.scale_6c[stageNum].y * manager.params_10.scale_16.y;
          final float scaleZ = impact.scale_6c[stageNum].z * manager.params_10.scale_16.z;
          final int r = impact.opacity_8c[stageNum].x * manager.params_10.colour_1c.x >> 8;
          final int g = impact.opacity_8c[stageNum].y * manager.params_10.colour_1c.y >> 8;
          final int b = impact.opacity_8c[stageNum].z * manager.params_10.colour_1c.z >> 8;

          if((manager.params_10.flags_00 & 0x40) == 0) {
            FUN_800e61e4(r / 128.0f, g / 128.0f, b / 128.0f);
          }

          //LAB_8010f50c
          GsSetLightMatrix(this.lightingTransforms);
          this.modelTransforms.rotationXYZ(impact.rotation_2c[stageNum]);
          this.modelTransforms.transfer.set(impact.translation_0c[stageNum]).add(manager.params_10.trans_04);
          this.scale.set(scaleX, scaleY, scaleZ);
          this.modelTransforms.scale(this.scale);
          dobj.attribute_00 = manager.params_10.flags_00;
          this.modelTransforms.compose(worldToScreenMatrix_800c3548, this.w2sModelTransforms);
          GTE.setTransforms(this.w2sModelTransforms);
          zOffset_1f8003e8 = 0;
          tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;

          final int oldZShift = zShift_1f8003c4;
          final int oldZMax = zMax_1f8003cc;
          final int oldZMin = zMin;
          zShift_1f8003c4 = 2;
          zMax_1f8003cc = 0xffe;
          zMin = 0xb;

          if(impact.renderShockwave_01) {
            dobj.tmd_08 = impact.shockwaveObjTable_98;
            Renderer.renderDobj2(dobj, false, 0x20);

            if(this.shockwaveObj == null) {
              this.shockwaveObj = TmdObjLoader.fromObjTable("Star Children shockwave" + i + ')', dobj.tmd_08);
            }
            RENDERER.queueModel(this.shockwaveObj, this.modelTransforms, QueuedModelBattleTmd.class);
          }

          //LAB_8010f5d0
          if(impact.animationFrame_a2 < 9) {
            dobj.tmd_08 = impact.explosionObjTable_94;
            Renderer.renderDobj2(dobj, false, 0x20);

            if(this.explosionObj == null) {
              this.explosionObj = TmdObjLoader.fromObjTable("Star Children explosion" + i + ')', dobj.tmd_08);
            }
            RENDERER.queueModel(this.explosionObj, this.modelTransforms, QueuedModelBattleTmd.class);
          } else if(impact.animationFrame_a2 >= 11) {
            dobj.tmd_08 = impact.plumeObjTable_9c;
            Renderer.renderDobj2(dobj, false, 0x20);

            if(this.plumeObj == null) {
              this.plumeObj = TmdObjLoader.fromObjTable("Star Children plume" + i + ')', dobj.tmd_08);
            }
            RENDERER.queueModel(this.plumeObj, this.modelTransforms, QueuedModelBattleTmd.class);
          }

          zShift_1f8003c4 = oldZShift;
          zMax_1f8003cc = oldZMax;
          zMin = oldZMin;

          //LAB_8010f608
          if((manager.params_10.flags_00 & 0x40) == 0) {
            FUN_800e62a8();
          }
        }
      }
    }
    //LAB_8010f640
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.shockwaveObj != null) {
      this.shockwaveObj.delete();
    }

    if(this.explosionObj != null) {
      this.explosionObj.delete();
    }

    if(this.plumeObj != null) {
      this.plumeObj.delete();
    }
  }
}
