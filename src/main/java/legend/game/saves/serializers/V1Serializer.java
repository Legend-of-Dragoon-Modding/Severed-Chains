package legend.game.saves.serializers;

import legend.game.saves.ConfigCollection;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
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

  public static SavedGame fromV1(final String name, final FileData data) {
    final GameState52c gameState = deserializeRetailGameState(data.slice(0x30));
    final CharacterData2c charData = gameState.charData_32c[gameState.charIds_88[0]];
    final RegistryId engineState = gameState.isOnWorldMap_4e4 ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();
    final int maxHp = levelStuff_80111cfc[gameState.charIds_88[0]][charData.level_12].hp_00;
    final int maxMp = magicStuff_80111d20[gameState.charIds_88[0]][charData.dlevel_13].mp_00;
    final int locationType = data.readUByte(0x2d);
    final int locationIndex = data.readUByte(0x2c);
    final String locationName = getLocationName(locationType, locationIndex);
    return new RetailSavedGame(name, name, locationName, engineState, new FileData(new byte[0]), gameState, new ConfigCollection(), maxHp, maxMp);
  }
}
