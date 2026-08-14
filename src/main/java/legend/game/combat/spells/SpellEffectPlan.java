package legend.game.combat.spells;

import java.util.List;

public record SpellEffectPlan(SpellTargetProfile target, List<SpellEffect> effects, ExecutionMode executionMode) {
  private static final SpellEffectPlan LEGACY = new SpellEffectPlan(new SpellTargetProfile(TargetSide.ANY, TargetScope.SINGLE, TargetLifeState.ANY), List.of(), ExecutionMode.LEGACY);

  public SpellEffectPlan {
    if(target == null) {
      throw new IllegalArgumentException("Spell effect target cannot be null");
    }

    if(effects == null) {
      throw new IllegalArgumentException("Spell effects cannot be null");
    }

    for(final SpellEffect effect : effects) {
      if(effect == null) {
        throw new IllegalArgumentException("Spell effects cannot contain null entries");
      }
    }

    if(executionMode == null) {
      throw new IllegalArgumentException("Spell execution mode cannot be null");
    }

    effects = List.copyOf(effects);
  }

  public static SpellEffectPlan legacy() {
    return LEGACY;
  }
}
