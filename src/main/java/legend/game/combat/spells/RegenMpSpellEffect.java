package legend.game.combat.spells;
/**
 * Replaces the target's current MP regeneration with a new turn-based regeneration effect.
 *
 * <p>MP regeneration is applied only to player targets.</p>
 *
 * @param potency flat MP restored per turn, or percent of maximum MP when {@code percentage} is
 *                true
 * @param turns number of completed turns that apply the regeneration
 * @param percentage whether {@code potency} is a percentage of the target's maximum MP
 */
public record RegenMpSpellEffect(int potency, int turns, boolean percentage) implements SpellEffect { }
