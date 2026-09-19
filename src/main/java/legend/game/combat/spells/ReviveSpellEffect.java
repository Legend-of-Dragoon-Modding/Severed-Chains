package legend.game.combat.spells;
/**
 * Revives each matched dead target before other restoration effects are applied.
 *
 * @param hpPercent percent of maximum HP restored on revival; revival always restores at least
 *                  one HP
 */
public record ReviveSpellEffect(int hpPercent) implements SpellEffect { }
