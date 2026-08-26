package legend.game.combat.spells;
/**
 * Restores the caster's MP from the damage actually applied to the target.
 *
 * @param percent percent of applied damage restored to the caster
 */
public record DrainMpSpellEffect(int percent) implements SpellEffect { }
