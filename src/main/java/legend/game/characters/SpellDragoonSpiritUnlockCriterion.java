package legend.game.characters;

public class SpellDragoonSpiritUnlockCriterion implements SpellUnlockCriterion {
  @Override
  public boolean isUnlocked(final CharacterData2c character, final CharacterSpellInfo spellInfo) {
    return character.hasDragoon();
  }
}
