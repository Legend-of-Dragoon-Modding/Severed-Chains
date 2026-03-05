package legend.lodmod.characters;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

public class AlbertCharacterData extends CharacterData2c {
  public AlbertCharacterData(final GameState52c gameState, final CharacterTemplate template, final StatCollection stats) {
    super(gameState, template, stats);
  }

  @Override
  public void set(final CharacterData2c other) {
    super.set(other);

    this.swapAddition(LodAdditions.HARPOON.getId());
    this.swapAddition(LodAdditions.SPINNING_CANE.getId());
    this.swapAddition(LodAdditions.ROD_TYPHOON.getId());
    this.swapAddition(LodAdditions.GUST_OF_WIND_DANCE.getId());
    this.swapAddition(LodAdditions.FLOWER_STORM.getId());
  }

  private void swapAddition(final RegistryId additionId) {
    final CharacterAdditionInfo info = this.getAdditionInfo(additionId);
    final RegistryId newId = new RegistryId(additionId.modId(), "albert_" + additionId.entryId());

    if(info != null) {
      this.removeAddition(additionId);
      this.addAddition(newId, info);
    }

    if(additionId.equals(this.selectedAddition_19)) {
      this.selectedAddition_19 = newId;
    }
  }
}
