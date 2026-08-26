package legend.game.combat.spells;
/**
 * Restores MP to each matched player target, up to that target's maximum MP.
 *
 * @param potency flat MP restored, or percent of maximum MP when {@code percentage} is true
 * @param percentage whether {@code potency} is a percentage of the target's maximum MP
 */
public record RestoreMpSpellEffect(int potency, boolean percentage) implements SpellEffect { }
