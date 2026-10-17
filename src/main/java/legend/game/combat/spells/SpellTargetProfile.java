package legend.game.combat.spells;

/**
 * Immutable targeting rules for one {@link SpellEffectPlan}.
 *
 * @param side relationship to the caster that a target must have
 * @param scope whether one or every eligible target is selected
 * @param lifeState life state that a target must have
 */
public record SpellTargetProfile(TargetSide side, TargetScope scope, TargetLifeState lifeState) {
  public SpellTargetProfile {
    if(side == null) {
      throw new IllegalArgumentException("Spell target side cannot be null");
    }

    if(scope == null) {
      throw new IllegalArgumentException("Spell target scope cannot be null");
    }

    if(lifeState == null) {
      throw new IllegalArgumentException("Spell target life state cannot be null");
    }
  }
}
