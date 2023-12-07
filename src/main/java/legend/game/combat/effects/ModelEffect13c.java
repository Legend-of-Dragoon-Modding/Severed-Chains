package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;
import legend.game.types.CContainer;
import legend.game.types.Model124;

import static legend.game.combat.Bttl_800d.FUN_800dd89c;
import static legend.game.combat.Bttl_800d.applyAnimation;
import static legend.game.combat.Bttl_800e.FUN_800e60e0;
import static legend.game.combat.Bttl_800e.FUN_800e6170;
import static legend.game.combat.Bttl_800e.FUN_800e61e4;
import static legend.game.combat.Bttl_800e.FUN_800e62a8;
import static legend.game.combat.Bttl_800e.calculateEffectTransforms;

public class ModelEffect13c implements Effect {
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

  @Method(0x800ea3f8L)
  public void FUN_800ea3f8(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state, final EffectManagerData6c<EffectManagerParams.AnimType> manager) {
    final MV sp0x10 = new MV();
    calculateEffectTransforms(sp0x10, manager);

    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    final Model124 model = effect.model_134;
    model.coord2_14.transforms.rotate.set(manager.params_10.rot_10);
    model.coord2_14.transforms.scale.set(manager.params_10.scale_16);
    model.zOffset_a0 = manager.params_10.z_22;
    model.coord2_14.coord.set(sp0x10);
    model.coord2_14.flg = 0;

    if(effect.anim_0c != null) {
      applyAnimation(model, manager.params_10.ticks_24);
    }

    //LAB_800ea4fc
  }

  @Method(0x800ea510L)
  public void FUN_800ea510(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state, final EffectManagerData6c<EffectManagerParams.AnimType> manager) {
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    if(manager.params_10.flags_00 >= 0) {
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      } else {
        //LAB_800ea564
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_800ea574
      final Model124 model = effect.model_134;

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
}
