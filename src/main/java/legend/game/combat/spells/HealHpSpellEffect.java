package legend.game.combat.spells;
/**
 * Restores HP to each matched living target, up to that target's maximum HP.
 *
 * @param potency flat HP restored, or percent of maximum HP when {@code percentage} is true
 * @param percentage whether {@code potency} is a percentage of the target's maximum HP
 */
public record HealHpSpellEffect(int potency, boolean percentage) implements SpellEffect { }
