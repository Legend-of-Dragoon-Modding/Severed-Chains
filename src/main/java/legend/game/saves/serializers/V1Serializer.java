package legend.game.saves.serializers;

import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SavedGame;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

import static legend.game.saves.serializers.RetailSerializer.deserializeRetailGameState;
import static legend.lodmod.LodMod.getLocationName;

public final class V1Serializer {
  private V1Serializer() { }

  public static final int MAGIC_V1 = 0x76615344; // DSav

  public static FileData fromV1Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V1) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV1(final Campaign campaign, final String name, final FileData data) {
    final RetailSavedGame savedGame = new RetailSavedGame(campaign, "V1", name, name, LodMod.RETAIL_CAMPAIGN_TYPE.getId(), new ConfigCollection());

    deserializeRetailGameState(savedGame, data.slice(0x30));

    final int locationType = data.readUByte(0x2d);
    final int locationIndex = data.readUByte(0x2c);
    savedGame.locationName = getLocationName(locationType, locationIndex);

    return savedGame;
  }
}
