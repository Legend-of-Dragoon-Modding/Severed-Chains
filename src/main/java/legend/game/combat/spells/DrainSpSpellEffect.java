package legend.game.combat.spells;
/**
 * Restores the caster's SP from the damage actually applied to the target.
 *
 * @param percent percent of applied damage restored to the caster
 */
public record DrainSpSpellEffect(int percent) implements SpellEffect { }
