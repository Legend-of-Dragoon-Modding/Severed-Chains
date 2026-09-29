package legend.game.saves;

import legend.game.characters.CharacterData2c;
import legend.game.types.GameState52c;

/** Retains an unavailable template payload at its original save and script index. */
public final class UnavailableSavedCharacter implements SavedCharacter {
  private final legend.core.tags.MapTag data;

  public UnavailableSavedCharacter(final legend.core.tags.MapTag data) {
    this.data = data.clone();
  }

  public legend.core.tags.MapTag data() {
    return this.data.clone();
  }

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    throw new IllegalStateException("Unavailable characters require an inert slot placeholder");
  }

  @Override
  public void render(final SavedGame savedGame, final int x, final int y) { }

  @Override
  public boolean inParty() {
    return false;
  }
}
