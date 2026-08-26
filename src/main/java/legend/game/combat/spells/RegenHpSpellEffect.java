package legend.game.combat.spells;
/**
 * Replaces the target's current HP regeneration with a new turn-based regeneration effect.
 *
 * @param potency flat HP restored per turn, or percent of maximum HP when {@code percentage} is
 *                true
 * @param turns number of completed turns that apply the regeneration
 * @param percentage whether {@code potency} is a percentage of the target's maximum HP
 */
public record RegenHpSpellEffect(int potency, int turns, boolean percentage) implements SpellEffect { }
