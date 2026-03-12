package legend.lodmod.characters;

import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.types.GameState52c;

import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;

public class DartCharacterData extends CharacterData2c {
  public DartCharacterData(final GameState52c gameState, final CharacterTemplate template, final StatCollection stats) {
    super(gameState, template, stats);
  }

  @Override
  public int getStatusAndFlags() {
    int flags = super.getStatusAndFlags();

    if(this.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
      flags |= 0x4000;
    }

    return flags;
  }

  @Override
  public boolean hasTransformed() {
    return !this.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT) && super.hasTransformed();
  }
}
