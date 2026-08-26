package legend.game.combat.spells;
/**
 * Replaces the target's current SP regeneration with a new turn-based regeneration effect.
 *
 * <p>SP regeneration is applied only to player targets.</p>
 *
 * @param potency flat SP restored per turn, or percent of maximum SP when {@code percentage} is
 *                true
 * @param turns number of completed turns that apply the regeneration
 * @param percentage whether {@code potency} is a percentage of the target's maximum SP
 */
public record RegenSpSpellEffect(int potency, int turns, boolean percentage) implements SpellEffect { }
