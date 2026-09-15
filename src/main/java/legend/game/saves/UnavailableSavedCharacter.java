package legend.game.saves;

import legend.game.characters.CharacterData2c;
import legend.game.types.GameState52c;

/** Keeps save-card and original party indices intact until the game state is materialized. */
public final class UnavailableSavedCharacter implements SavedCharacter {
  @Override
  public CharacterData2c make(final GameState52c gameState) {
    throw new IllegalStateException("Unavailable characters must be remapped before materialization");
  }

  @Override
  public void render(final SavedGame savedGame, final int x, final int y) { }

  @Override
  public boolean inParty() {
    return false;
  }
}
