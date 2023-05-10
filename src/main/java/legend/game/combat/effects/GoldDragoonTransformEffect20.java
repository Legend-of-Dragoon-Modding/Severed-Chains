package legend.game.combat.effects;

import java.util.Arrays;

public class GoldDragoonTransformEffect20 implements BttlScriptData6cSubBase1 {
//  public int count_00;
  public int _04;
  public final GoldDragoonTransformEffectInstance84[] parts_08;

  public GoldDragoonTransformEffect20(final int count) {
    this.parts_08 = new GoldDragoonTransformEffectInstance84[count];
    Arrays.setAll(this.parts_08, i -> new GoldDragoonTransformEffectInstance84());
  }
}
