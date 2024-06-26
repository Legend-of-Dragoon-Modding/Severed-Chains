package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GTE;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public class GoldDragoonTransformEffect20 implements Effect<EffectManagerParams.VoidType> {
//  public int count_00;
  public int _04;
  public final GoldDragoonTransformEffectInstance84[] parts_08;

  public GoldDragoonTransformEffect20(final int count) {
    this.parts_08 = new GoldDragoonTransformEffectInstance84[count];
    Arrays.setAll(this.parts_08, i -> new GoldDragoonTransformEffectInstance84());
  }

  @Override
  @Method(0x8010dcd0L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    int unusedCount = 0;

    //LAB_8010dd00
    for(final GoldDragoonTransformEffectInstance84 instance : this.parts_08) {
      if(!instance.used_00) {
        unusedCount++;
        continue;
      }

      //LAB_8010dd18
      instance._80++;

      if(instance._7e != -1) {
        if(instance._7e == instance._80) {
          instance._7e = -1;
        }
      }

      //LAB_8010dd40
      if(instance._7e == -1) {
        //LAB_8010dd50
        instance.counter_04++;
        final int counter = instance.counter_04;

        instance.trans_08.x += instance.transStep_28.x;
        instance.trans_08.y = counter * counter * 24 - instance.transStep_28.y * counter;
        instance.trans_08.z += instance.transStep_28.z;

        instance.rot_38.add(instance.rotStep_48);

        if(instance._7c <= 0) {
          if(instance.trans_08.y > 4.0f) {
            instance.used_00 = false;
          }
          //LAB_8010ddf8
        } else if(instance.trans_08.y >= 0.0f) {
          instance.counter_04 = 1;
          instance.transStep_28.y /= 2.0f;
          instance._7c--;
        }
      }
    }

    if(unusedCount >= this.parts_08.length) {
      //LAB_8010de5c
      state.deallocateWithChildren();
    }

    //LAB_8010de6c
  }

  @Override
  @Method(0x8010de7cL)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    this._04++;

    final ModelPart10 dobj2 = new ModelPart10();
    final Vector3f trans = new Vector3f();
    final MV transforms = new MV();
    final MV sp0x98 = new MV();

    //LAB_8010ded8
    for(final GoldDragoonTransformEffectInstance84 instance : this.parts_08) {
      if(instance.used_00) {
        trans.set(instance.trans_08).add(manager.params_10.trans_04);

        transforms.rotationXYZ(instance.rot_38);
        transforms.transfer.set(trans);
        transforms.scale(manager.params_10.scale_16);

        dobj2.tmd_08 = instance.tmd_70;

        transforms.compose(worldToScreenMatrix_800c3548, sp0x98);
        GTE.setTransforms(sp0x98);

        zOffset_1f8003e8 = 0;
        tmdGp0Tpage_1f8003ec = 2;

        Renderer.renderDobj2(dobj2, false, 0);
      }
    }

    //LAB_8010e020
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }
}
