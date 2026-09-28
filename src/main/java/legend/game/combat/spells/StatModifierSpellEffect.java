package legend.game.combat.spells;

/**
 * Replaces the selected target's active modifier for one battle stat.
 *
 * @param stat battle stat whose active modifier is replaced
 * @param amount raw modifier amount consumed by the battle stat calculation
 * @param turns number of turns assigned to the modifier
 */
public record StatModifierSpellEffect(SpellStat stat, int amount, int turns) implements SpellEffect {
  public StatModifierSpellEffect {
    if(stat == null) {
      throw new IllegalArgumentException("Spell modifier stat cannot be null");
    }
  }
}
