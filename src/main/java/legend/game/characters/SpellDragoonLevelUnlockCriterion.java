package legend.game.characters;

public class SpellDragoonLevelUnlockCriterion implements SpellUnlockCriterion {
  private final int dragoonLevel;

  public SpellDragoonLevelUnlockCriterion(final int dragoonLevel) {
    this.dragoonLevel = dragoonLevel;
  }

  @Override
  public boolean isUnlocked(final CharacterData2c character, final CharacterSpellInfo spellInfo) {
    return character.dlevel_13 >= this.dragoonLevel;
  }
}
