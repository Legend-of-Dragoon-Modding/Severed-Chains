package legend.game.combat.effects;

import legend.core.QueuedModelBattleTmd;
import legend.core.RenderEngine;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.combat.Battle;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import org.joml.Matrix4f;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800c.inverseWorldToScreenMatrix;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.combat.SEffe.FUN_800e60e0;
import static legend.game.combat.SEffe.FUN_800e6170;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static legend.game.combat.SEffe.renderTmdSpriteEffect;

public class DeffTmdRenderer14 implements Effect<EffectManagerParams.AnimType> {
  public int _00;
  public DeffPart.TmdType tmdType_04;
  public TmdObjTable1c tmd_08;
  public Obj obj;

  /** ushort */
  public int tpage_10;

  final MV transforms = new MV();
  final Matrix4f glTransforms = new Matrix4f();

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {

  }

  /** TODO renders other effects too? Burnout, more? Uses CTMD render pipeline */
  @Override
  @Method(0x8011826cL)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;

    if(manager.params_10.flags_00 >= 0) {
      calculateEffectTransforms(this.transforms, manager);
      if((manager.params_10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
      } else {
        //LAB_801182bc
        tmdGp0Tpage_1f8003ec = this.tpage_10;
      }

      //LAB_801182c8
      zOffset_1f8003e8 = manager.params_10.z_22;
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4((manager.params_10.colour_1c.x << 5) / (float)0x1000, (manager.params_10.colour_1c.y << 5) / (float)0x1000, (manager.params_10.colour_1c.z << 5) / (float)0x1000);
      } else {
        //LAB_80118304
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_80118314
      if(manager.parentBobjIndex_0c < -2) {
        if(manager.parentBobjIndex_0c == -4) {
          this.transforms.transfer.z = projectionPlaneDistance_1f8003f8;
        }

        //LAB_8011833c
        GsSetLightMatrix(this.transforms);
        GTE.setTransforms(this.transforms);

        if(RenderEngine.legacyMode == 0) {
          this.glTransforms.set(this.transforms).setTranslation(this.transforms.transfer);
          this.glTransforms.mulLocal(inverseWorldToScreenMatrix);
        }

        final ModelPart10 dobj2 = new ModelPart10();
        dobj2.attribute_00 = manager.params_10.flags_00;
        dobj2.tmd_08 = this.tmd_08;

        final int oldZShift = zShift_1f8003c4;
        final int oldZMax = zMax_1f8003cc;
        final int oldZMin = zMin;
        zShift_1f8003c4 = 2;
        zMax_1f8003cc = 0xffe;
        zMin = 0xb;
        Renderer.renderDobj2(dobj2, false, 0x20);
        zShift_1f8003c4 = oldZShift;
        zMax_1f8003cc = oldZMax;
        zMin = oldZMin;

        final QueuedModelBattleTmd model = RENDERER.queueModel(this.obj, this.glTransforms, QueuedModelBattleTmd.class)
          .depthOffset(manager.params_10.z_22 * 4)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour)
          .ctmdFlags(0x20 | ((dobj2.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0))
          .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
          .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);

        if(this.tmd_08.vdf != null) {
          model.vdf(this.tmd_08.vdf);
        }
      } else {
        //LAB_80118370
        renderTmdSpriteEffect(this.tmd_08, this.obj, manager.params_10, this.transforms);
      }

      //LAB_80118380
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_801183a4
        FUN_800e6170();
      }
    }

    //LAB_801183ac
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }
}
