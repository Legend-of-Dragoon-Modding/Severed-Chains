package legend.game.combat.spells;
/**
 * Clears the selected status bits from each matched target before restoration and damage effects.
 *
 * @param statusMask status bits to clear
 */
public record CleanseSpellEffect(int statusMask) implements SpellEffect { }
