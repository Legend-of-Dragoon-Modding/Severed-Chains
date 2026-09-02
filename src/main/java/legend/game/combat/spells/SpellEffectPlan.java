package legend.game.combat.spells;

import java.util.List;

/**
 * Immutable targeting, effects, and execution policy for one part of a spell cast.
 *
 * <p>A spell may contain multiple plans so different effects can target different groups. The
 * constructor defensively copies {@code effects}; changing the source list afterward does not
 * change this plan.</p>
 *
 * @param target rules used to select battle entities for this plan
 * @param effects immutable effect declarations applied to every matched target
 * @param executionMode runtime responsible for interpreting this plan
 */
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

    /**
     * Returns the shared plan that delegates entirely to retail spell fields and scripts.
     *
     * @return an immutable legacy plan with no declarative effects
     */
    public static SpellEffectPlan legacy() {
    return LEGACY;
  }
}
