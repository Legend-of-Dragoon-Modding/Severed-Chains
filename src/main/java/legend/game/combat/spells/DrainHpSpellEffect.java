package legend.game.combat.spells;
/**
 * Restores the caster's HP from the damage actually applied to the target.
 *
 * @param percent percent of applied damage restored to the caster
 */
public record DrainHpSpellEffect(int percent) implements SpellEffect { }
