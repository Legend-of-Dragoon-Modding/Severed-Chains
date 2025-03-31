package legend.game.saves.serializers;

import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCollection;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.saves.serializers.RetailSerializer.deserializeRetailGameState;

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
    final GameState52c state = deserializeRetailGameState(data.slice(0x30));
    final CharacterData2c charData = state.charData_32c[state.charIds_88[0]];
    final int maxHp = CoreMod.CHARACTER_DATA[state.charIds_88[0]].statsTable[charData.level_12].hp_00;
    final int maxMp = CoreMod.CHARACTER_DATA[state.charIds_88[0]].dragoonStatsTable[charData.dlevel_13].mp_00;
    return new SavedGame(name, name, data.readUByte(0x2d), data.readUByte(0x2c), state, new ConfigCollection(), maxHp, maxMp);
  }
}
