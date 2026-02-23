package legend.game.saves;

import legend.core.GameEngine;
import legend.game.additions.CharacterAdditionStats;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.VitalsStat;
import legend.game.inventory.Equipment;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.types.Renderable58;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.SItem.renderNumber;
import static legend.game.types.CharacterData2c.IN_PARTY;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class SeveredSavedCharacterV1 implements SavedCharacter {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SeveredSavedCharacterV1.class);

  public final RegistryId templateId;

  public final int maxHp;
  public final int maxMp;

  public int xp;
  public int flags;
  public int hp;
  public int mp;
  public int sp;
  public int dlevelXp;
  public int status;
  public int level;
  public int dlevel;
  public final Map<EquipmentSlot, RegistryId> equipmentIds = new EnumMap<>(EquipmentSlot.class);
  public RegistryId selectedAddition;
  public final Map<RegistryId, CharacterAdditionStats> additionStats = new HashMap<>();

  public SeveredSavedCharacterV1(final RegistryId templateId, final int maxHp, final int maxMp) {
    this.templateId = templateId;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
  }

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(this.templateId).get();
    final CharacterData2c character = template.make(gameState);

    final VitalsStat hpStat = character.stats.getStat(HP_STAT.get());
    final VitalsStat mpStat = character.stats.getStat(MP_STAT.get());
    final VitalsStat spStat = character.stats.getStat(SP_STAT.get());

    character.xp_00 = this.xp;
    character.partyFlags_04 = this.flags;
    character.dlevelXp_0e = this.dlevelXp;
    character.status_10 = this.status;

    for(int i = 1; i < this.level; i++) {
      template.applyLevelUp(gameState, character, null);
    }

    for(int i = 1; i < this.dlevel; i++) {
      template.applyDragoonLevelUp(gameState, character, null);
    }

    hpStat.setCurrent(this.hp);
    hpStat.setMaxRaw(this.maxHp);
    mpStat.setCurrent(this.mp);
    mpStat.setMaxRaw(this.maxMp);
    spStat.setCurrent(this.sp);
    spStat.setMaxRaw(this.dlevel * 100);

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      if(this.equipmentIds.containsKey(slot)) {
        final RegistryDelegate<Equipment> delegate = GameEngine.REGISTRIES.equipment.getEntry(this.equipmentIds.get(slot));

        if(delegate.isValid()) {
          character.equip(slot, delegate.get());
        } else {
          LOGGER.warn("Skipping unknown equipment ID %s", delegate.getId());
        }
      }
    }

    character.selectedAddition_19 = this.selectedAddition;
    character.additionStats.putAll(this.additionStats);
    return character;
  }

  @Override
  public void render(final SavedGame savedGame, final int x, final int y) {
    renderNumber(224, y + 6, this.level, 0x2, 2);
    renderNumber(269, y + 6, this.dlevel, 0x2, 2);
    renderFourDigitHp(302, y + 6, this.hp, this.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER);
    renderNumber(332, y + 6, this.maxHp, 0x2, 4);
  }

  @Override
  public boolean inParty() {
    return (this.flags & IN_PARTY) != 0;
  }
}
