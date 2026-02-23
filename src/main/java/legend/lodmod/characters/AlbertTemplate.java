package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

public class AlbertTemplate extends LavitzTemplate {
  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.additionStats.put(LodAdditions.ALBERT_HARPOON.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.ALBERT_SPINNING_CANE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.ALBERT_ROD_TYPHOON.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.ALBERT_GUST_OF_WIND_DANCE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.ALBERT_FLOWER_STORM.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.ALBERT_HARPOON.getId();

    return character;
  }

  @Override
  protected RegistryId getAdditionUnlock(final int level) {
    return switch(level) {
      case 1 -> LodAdditions.ALBERT_HARPOON.getId();
      case 5 -> LodAdditions.ALBERT_SPINNING_CANE.getId();
      case 7 -> LodAdditions.ALBERT_ROD_TYPHOON.getId();
      case 11 -> LodAdditions.ALBERT_GUST_OF_WIND_DANCE.getId();
      default -> null;
    };
  }
}
