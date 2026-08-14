package legend.game.combat.spells;

public record StatModifierSpellEffect(SpellStat stat, int amount, int turns) implements SpellEffect {
  public StatModifierSpellEffect {
    if(stat == null) {
      throw new IllegalArgumentException("Spell modifier stat cannot be null");
    }
  }
}
