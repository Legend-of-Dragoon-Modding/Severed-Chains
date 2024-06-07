package legend.game.saves.types;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.inventory.screens.controls.EnhancedSaveCard;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.goodsItemNames_8011c008;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class EnhancedSaveType extends SaveType<EnhancedSaveDisplay> {
  private static final int[] dragoonSpiritGoodsBits_800fbabc = {0, 2, 5, 6, 4, 1, 3, 7};

  @Override
  public EnhancedSaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState) {
    final EnhancedSaveDisplay display = new EnhancedSaveDisplay(engineState.getLocationForSave(), gameState.gold_94, gameState.stardust_9c, gameState.timestamp_a0, SAVES.getSaveIcons());

    for(int i = 0; i < gameState.charData_32c.length; i++) {
      final CharacterData2c chr = gameState.charData_32c[i];

      if((chr.partyFlags_04 & 0x1) != 0) {
        for(int n = 0; n < gameState.charIds_88.length; n++) {
          if(gameState.charIds_88[n] == i) {
            display.party.add(display.chars.size());
            break;
          }
        }

        display.chars.add(new EnhancedSaveDisplay.Char(getCharacterName(i), i * 48, 0, 48, 48, chr.level_12, chr.xp_00, chr.dlevel_13, chr.dlevelXp_0e, chr.hp_08, chr.mp_0a, chr.sp_0c, activeStats[i].maxHp_66, activeStats[i].maxMp_6e, chr.dlevel_13 * 100));
      }
    }

    for(int spiritIndex = 0; spiritIndex < 8; spiritIndex++) {
      final int bit = dragoonSpiritGoodsBits_800fbabc[spiritIndex];
      if((gameState_800babc8.goods_19c[0] & 0x1 << bit) != 0) {
        display.dragoons.add(new EnhancedSaveDisplay.Dragoon(goodsItemNames_8011c008[bit], spiritIndex * 16, 48, 11, 9));
      } else {
        display.dragoons.add(new EnhancedSaveDisplay.Dragoon("Not discovered", 0, 0, 0, 0));
      }
    }

    return display;
  }

  @Override
  public void serialize(final FileData data, final EnhancedSaveDisplay display, final IntRef serializerOffset) {
    final IntRef offset = new IntRef();
    data.writeAscii(offset, display.location);
    data.writeVarInt(offset, display.gold);
    data.writeVarInt(offset, display.stardust);
    data.writeVarInt(offset, display.time);

    data.writeVarInt(offset, display.icons.size());
    data.write(0, display.icons, offset, display.icons.size());

    data.writeVarInt(offset, display.party.size());
    for(final int party : display.party) {
      data.writeVarInt(offset, party);
    }

    data.writeVarInt(offset, display.chars.size());
    for(final EnhancedSaveDisplay.Char chr : display.chars) {
      data.writeAscii(offset, chr.name);
      data.writeVarInt(offset, chr.iconU);
      data.writeVarInt(offset, chr.iconV);
      data.writeVarInt(offset, chr.iconW);
      data.writeVarInt(offset, chr.iconH);
      data.writeVarInt(offset, chr.lvl);
      data.writeVarInt(offset, chr.exp);
      data.writeVarInt(offset, chr.dlvl);
      data.writeVarInt(offset, chr.dexp);
      data.writeVarInt(offset, chr.hp);
      data.writeVarInt(offset, chr.mp);
      data.writeVarInt(offset, chr.sp);
      data.writeVarInt(offset, chr.maxHp);
      data.writeVarInt(offset, chr.maxMp);
      data.writeVarInt(offset, chr.maxSp);
    }

    data.writeVarInt(offset, display.dragoons.size());
    for(final EnhancedSaveDisplay.Dragoon dragoon : display.dragoons) {
      data.writeAscii(offset, dragoon.name);
      data.writeVarInt(offset, dragoon.iconU);
      data.writeVarInt(offset, dragoon.iconV);
      data.writeVarInt(offset, dragoon.iconW);
      data.writeVarInt(offset, dragoon.iconH);
    }

    serializerOffset.add(offset.get());
  }

  @Override
  public EnhancedSaveDisplay deserialize(final FileData data, final IntRef serializerOffset) {
    final IntRef offset = new IntRef();
    final String location = data.readAscii(offset);
    final int gold = data.readVarInt(offset);
    final int stardust = data.readVarInt(offset);
    final int time = data.readVarInt(offset);

    final int iconSizes = data.readVarInt(offset);
    final FileData icons = data.slice(offset.get(), iconSizes);
    offset.add(iconSizes);

    final EnhancedSaveDisplay display = new EnhancedSaveDisplay(location, gold, stardust, time, icons);

    final int partySize = data.readVarInt(offset);
    for(int i = 0; i < partySize; i++) {
      display.party.add(data.readVarInt(offset));
    }

    final int charsSize = data.readVarInt(offset);
    for(int i = 0; i < charsSize; i++) {
      final String name = data.readAscii(offset);
      final int iconU = data.readVarInt(offset);
      final int iconV = data.readVarInt(offset);
      final int iconW = data.readVarInt(offset);
      final int iconH = data.readVarInt(offset);
      final int lvl = data.readVarInt(offset);
      final int exp = data.readVarInt(offset);
      final int dlvl = data.readVarInt(offset);
      final int dexp = data.readVarInt(offset);
      final int hp = data.readVarInt(offset);
      final int mp = data.readVarInt(offset);
      final int sp = data.readVarInt(offset);
      final int maxHp = data.readVarInt(offset);
      final int maxMp = data.readVarInt(offset);
      final int maxSp = data.readVarInt(offset);

      display.chars.add(new EnhancedSaveDisplay.Char(name, iconU, iconV, iconW, iconH, lvl, exp, dlvl, dexp, hp, mp, sp, maxHp, maxMp, maxSp));
    }

    final int dragoonsSize = data.readVarInt(offset);
    for(int i = 0; i < dragoonsSize; i++) {
      final String name = data.readAscii(offset);
      final int iconU = data.readVarInt(offset);
      final int iconV = data.readVarInt(offset);
      final int iconW = data.readVarInt(offset);
      final int iconH = data.readVarInt(offset);

      display.dragoons.add(new EnhancedSaveDisplay.Dragoon(name, iconU, iconV, iconW, iconH));
    }

    serializerOffset.add(offset.get());

    return display;
  }

  @Override
  public SaveCard<EnhancedSaveDisplay> makeSaveCard() {
    return new EnhancedSaveCard();
  }
}
