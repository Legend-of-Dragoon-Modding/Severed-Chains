package legend.game.combat.spells;
/**
 * Restores SP to each matched player target, up to that target's maximum SP.
 *
 * @param potency flat SP restored, or percent of maximum SP when {@code percentage} is true
 * @param percentage whether {@code potency} is a percentage of the target's maximum SP
 */
public record RestoreSpSpellEffect(int potency, boolean percentage) implements SpellEffect { }
