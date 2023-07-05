package legend.game.combat.effects;

import java.util.Arrays;

public class AdditionSparksEffect08 implements Effect {
  /** ubyte */
  public final int count_00;
  public final AdditionSparksEffectInstance4c[] instances_04;

  public AdditionSparksEffect08(final int count) {
    this.count_00 = count;
    this.instances_04 = new AdditionSparksEffectInstance4c[count];
    Arrays.setAll(this.instances_04, i -> new AdditionSparksEffectInstance4c());
  }
}
