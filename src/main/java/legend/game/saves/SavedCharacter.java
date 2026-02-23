package legend.game.saves;

import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;

public interface SavedCharacter {
  CharacterData2c make(final GameState52c gameState);
  void render(final SavedGame savedGame, final int x, final int y);
  boolean inParty();
}
