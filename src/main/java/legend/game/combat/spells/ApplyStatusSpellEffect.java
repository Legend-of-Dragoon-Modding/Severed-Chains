package legend.game.combat.spells;
/**
 * Attempts to apply one status represented by {@code statusMask} after damage and drain effects.
 *
 * @param statusMask status bits eligible for application; only the highest set bit in the low
 *                   eight bits is returned when the roll succeeds
 * @param chance percent chance to apply the status, normally from {@code 0} through {@code 100}
 */
public record ApplyStatusSpellEffect(int statusMask, int chance) implements SpellEffect { }
