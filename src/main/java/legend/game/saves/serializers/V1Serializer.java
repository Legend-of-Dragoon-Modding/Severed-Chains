package legend.game.saves.serializers;

import legend.game.saves.ConfigCollection;
import legend.game.saves.SavedGame;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

import java.util.Map;

import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
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

  public static SavedGame<RetailSaveDisplay> fromV1(final String name, final FileData data) {
    final GameState52c state = deserializeRetailGameState(data.slice(0x30));
    final CharacterData2c charData = state.charData_32c[state.charIds_88[0]];

    final int locationIndex = data.readUByte(0x2c);
    final int locationType = data.readUByte(0x2d);
    final String[] locationNames;
    if(locationType == 1) {
      locationNames = worldMapNames_8011c1ec;
    } else if(locationType == 3) {
      locationNames = chapterNames_80114248;
    } else {
      locationNames = submapNames_8011c108;
    }

    final String locationName = locationNames[locationIndex];
    final int maxHp = levelStuff_80111cfc[state.charIds_88[0]][charData.level_12].hp_00;
    final int maxMp = magicStuff_80111d20[state.charIds_88[0]][charData.dlevel_13].mp_00;

    final RetailSaveDisplay display = new RetailSaveDisplay(locationName, maxHp, maxMp);

    return new SavedGame<>(name, name, LodMod.LEGEND_OF_DRAGOON_CAMPAIGN_TYPE, LodMod.RETAIL_SAVE_TYPE, display, state, new ConfigCollection(), Map.of());
  }
}
