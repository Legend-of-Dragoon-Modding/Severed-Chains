package legend.game.combat.effects;

import legend.core.QueuedModelBattleTmd;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.game.combat.Battle;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import legend.game.types.Model124;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.combat.SEffe.FUN_800e60e0;
import static legend.game.combat.SEffe.FUN_800e6170;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static legend.game.combat.SEffe.renderBttlShadow;

public class ModelEffect13c implements Effect<EffectManagerParams.AnimType> {
  public int _00;
  /** Can be LMB or CMB subtype */
  public DeffPart.TmdType tmdType_04;
  public CContainer extTmd_08;
  public Anim anim_0c;
  public final Model124 model_10;
  public Model124 model_134;

  public ModelEffect13c(final String name) {
    this.model_10 = new Model124(name);
  }

  /**
   * used renderCtmd
   */
  @Method(0x800dd89cL)
  private void FUN_800dd89c(final Model124 model, final int newAttribute) {
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    final MV lw = new MV();
    final MV ls = new MV();

    //LAB_800dd928
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 part = model.modelParts_00[i];

      //LAB_800dd940
      if((model.partInvisible_f4 & 1L << i) == 0) {
        GsGetLws(part.coord2_04, lw, ls);

        //LAB_800dd9bc
        if((newAttribute & 0x8) != 0) {
          //TODO pretty sure this is not equivalent to MATRIX#normalize
          lw.normal();
        }

        //LAB_800dd9d8
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);

        final int oldAttrib = part.attribute_00;
        part.attribute_00 = newAttribute;

        final int oldZShift = zShift_1f8003c4;
        final int oldZMax = zMax_1f8003cc;
        final int oldZMin = zMin;
        zShift_1f8003c4 = 2;
        zMax_1f8003cc = 0xffe;
        zMin = 0xb;
        Renderer.renderDobj2(part, false, 0x20);
        zShift_1f8003c4 = oldZShift;
        zMax_1f8003cc = oldZMax;
        zMin = oldZMin;

        //TODO remove null check
        if(part.obj != null) {
          RENDERER.queueModel(part.obj, lw, QueuedModelBattleTmd.class)
            .depthOffset(model.zOffset_a0 * 4)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour)
            .ctmdFlags((part.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0)
            .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
            .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);
        }

        part.attribute_00 = oldAttrib;
      }
    }

    //LAB_800dda54
    //LAB_800dda58
    for(int i = 0; i < 7; i++) {
      if(model.animateTextures_ec[i]) {
        animateModelTextures(model, i);
      }

      //LAB_800dda70
    }

    if(model.shadowType_cc != 0) {
      renderBttlShadow(model);
    }

    //LAB_800dda98
  }

  @Override
  @Method(0x800ea3f8L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    final MV transformMatrix = new MV();
    calculateEffectTransforms(transformMatrix, manager);

    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    final Model124 model = effect.model_134;
    model.coord2_14.transforms.rotate.set(manager.params_10.rot_10);
    model.coord2_14.transforms.scale.set(manager.params_10.scale_16);
    model.zOffset_a0 = manager.params_10.z_22;
    model.coord2_14.coord.set(transformMatrix);
    model.coord2_14.flg = 0;

    if(effect.anim_0c != null) {
      model.anim_08.apply(manager.params_10.ticks_24);
    }

    //LAB_800ea4fc
  }

  @Override
  @Method(0x800ea510L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    if(manager.params_10.flags_00 >= 0) {
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      } else {
        //LAB_800ea564
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_800ea574
      final Model124 model = this.model_134;

      final int oldTpage = model.tpage_108;

      if((manager.params_10.flags_00 & 0x4000_0000L) != 0) {
        model.tpage_108 = manager.params_10.flags_00 >>> 23 & 0x60;
      }

      //LAB_800ea598
      FUN_800dd89c(model, manager.params_10.flags_00);

      model.tpage_108 = oldTpage;

      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_800ea5d4
        FUN_800e6170();
      }
    }

    //LAB_800ea5dc
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {

  }
}
