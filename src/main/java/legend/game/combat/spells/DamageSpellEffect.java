package legend.game.combat.spells;
/**
 * Contributes power to the spell damage calculated for each matched target.
 *
 * <p>Multiple damage effects in one plan have their power values added before damage is
 * calculated.</p>
 *
 * @param power power supplied to the battle damage calculation
 */
public record DamageSpellEffect(int power) implements SpellEffect { }
