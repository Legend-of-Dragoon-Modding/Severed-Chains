package legend.game.saves.types;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.inventory.screens.controls.EnhancedSaveCard;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.game.SItem.goodsItemNames_8011c008;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class EnhancedSaveType extends SaveType<EnhancedSaveDisplay> {
  private static final int[] dragoonSpiritGoodsBits_800fbabc = {0, 2, 5, 6, 4, 1, 3, 7};

  @Override
  public EnhancedSaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState) {
    final EnhancedSaveDisplay display = new EnhancedSaveDisplay(engineState.getLocationForSave(), gameState.gold_94, gameState.stardust_9c, gameState.timestamp_a0);

    for(int i = 0; i < gameState.charIds_88.length; i++) {
      if(gameState.charIds_88[i] != -1) {
        display.party.add(gameState.charIds_88[i]);
      }
    }

    for(int i = 0; i < gameState.charData_32c.length; i++) {
      final CharacterData2c chr = gameState.charData_32c[i];
      display.chars.add(new EnhancedSaveDisplay.Char(null, getCharacterName(i), chr.level_12, chr.xp_00, chr.dlevel_13, chr.dlevelXp_0e, chr.hp_08, chr.mp_0a, chr.sp_0c, activeStats[i].maxHp_66, activeStats[i].maxMp_6e, chr.dlevel_13 * 100));
    }

    for(int spiritIndex = 0; spiritIndex < 8; spiritIndex++) {
      final int bit = dragoonSpiritGoodsBits_800fbabc[spiritIndex];
      if((gameState_800babc8.goods_19c[0] & 0x1 << bit) != 0) {
        display.dragoons.add(new EnhancedSaveDisplay.Dragoon(null, goodsItemNames_8011c008[bit]));
      } else {
        display.dragoons.add(new EnhancedSaveDisplay.Dragoon(null, "Not discovered"));
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

    data.writeVarInt(offset, display.party.size());
    for(final int party : display.party) {
      data.writeVarInt(offset, party);
    }

    data.writeVarInt(offset, display.chars.size());
    for(final EnhancedSaveDisplay.Char chr : display.chars) {
      if(chr.icon != null) {
        data.writeVarInt(offset, chr.icon.size());
        data.write(0, chr.icon, offset, chr.icon.size());
      } else {
        data.writeVarInt(offset, 0);
      }

      data.writeAscii(offset, chr.name);
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
      if(dragoon.icon != null) {
        data.writeVarInt(offset, dragoon.icon.size());
        data.write(0, dragoon.icon, offset, dragoon.icon.size());
      } else {
        data.writeVarInt(offset, 0);
      }

      data.writeAscii(offset, dragoon.name);
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

    final EnhancedSaveDisplay display = new EnhancedSaveDisplay(location, gold, stardust, time);

    final int partySize = data.readVarInt(offset);
    for(int i = 0; i < partySize; i++) {
      display.party.add(data.readVarInt(offset));
    }

    final int charsSize = data.readVarInt(offset);
    for(int i = 0; i < charsSize; i++) {
      final int iconSize = data.readVarInt(offset);
      final FileData icon;

      if(iconSize != 0) {
        icon = data.slice(offset.get(), iconSize);
        offset.add(iconSize);
      } else {
        icon = null;
      }

      final String name = data.readAscii(offset);
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

      display.chars.add(new EnhancedSaveDisplay.Char(icon, name, lvl, exp, dlvl, dexp, hp, mp, sp, maxHp, maxMp, maxSp));
    }

    final int dragoonsSize = data.readVarInt(offset);
    for(int i = 0; i < dragoonsSize; i++) {
      final int iconSize = data.readVarInt(offset);
      final FileData icon;

      if(iconSize != 0) {
        icon = data.slice(offset.get(), iconSize);
        offset.add(iconSize);
      } else {
        icon = null;
      }

      final String name = data.readAscii(offset);

      display.dragoons.add(new EnhancedSaveDisplay.Dragoon(icon, name));
    }

    serializerOffset.add(offset.get());

    return display;
  }

  @Override
  public SaveCard<EnhancedSaveDisplay> makeSaveCard() {
    return new EnhancedSaveCard();
  }
}
