package legend.game.characters;

@FunctionalInterface
public interface SpellUnlockCriterion {
  boolean isUnlocked(final CharacterData2c character, final CharacterSpellInfo spellInfo);
}
