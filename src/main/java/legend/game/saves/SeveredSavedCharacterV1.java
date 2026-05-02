package legend.game.saves;

import legend.core.GameEngine;
import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.VitalsStat;
import legend.game.inventory.Equipment;
import legend.game.inventory.SpellStats0c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.renderHp;
import static legend.game.SItem.renderNumber;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.characters.CharacterData2c.IN_PARTY;
import static legend.lodmod.Legacy.CHARACTER_SPELLS;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class SeveredSavedCharacterV1 implements SavedCharacter {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SeveredSavedCharacterV1.class);

  public final RegistryId templateId;
  private final int index;

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
  public boolean hasTransformed;
  public final Map<EquipmentSlot, RegistryId> equipmentIds = new EnumMap<>(EquipmentSlot.class);
  public RegistryId selectedAddition;
  public final Map<RegistryId, AdditionInfo> additionInfo = new LinkedHashMap<>();

  public SeveredSavedCharacterV1(final RegistryId templateId, final int index, final int maxHp, final int maxMp) {
    this.templateId = templateId;
    this.index = index;
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
    character.hasTransformed = this.hasTransformed;

    for(int i = 1; i < this.level; i++) {
      template.applyLevelUp(character, null);
    }

    for(int i = 1; i < this.dlevel; i++) {
      template.applyDragoonLevelUp(character, null);
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

    final RegistryDelegate<Addition>[] additions = CHARACTER_ADDITIONS[this.index];
    if(additions.length != 0) {
      final Set<RegistryId> seen = new HashSet<>();

      // Check retail additions first so we get them in order
      int i = 0;
      for(final RegistryDelegate<Addition> addition : additions) {
        final RegistryId id = addition.getId();
        final AdditionInfo saveAdditionInfo = this.additionInfo.get(id);
        this.updateAddition(character, gameState.timestamp_a0 + i, id, saveAdditionInfo);
        seen.add(id);
        i++;
      }

      // Then add non-retail additions
      for(final var entry : this.additionInfo.entrySet()) {
        final RegistryId id = entry.getKey();

        if(!seen.contains(id)) {
          final AdditionInfo saveAdditionInfo = entry.getValue();
          this.updateAddition(character, gameState.timestamp_a0 + i, id, saveAdditionInfo);
          i++;
        }
      }

      if(REGISTRIES.additions.hasEntry(this.selectedAddition)) {
        character.selectedAddition_19 = this.selectedAddition;
      } else {
        character.selectedAddition_19 = character.getUnlockedAdditions().getFirst();
      }
    }

    final RegistryDelegate<SpellStats0c>[] spells = CHARACTER_SPELLS[this.index];
    if(spells.length != 0) {
      final Set<RegistryId> seen = new HashSet<>();

      // Check retail spells first so we get them in order
      int i = 0;
      for(final RegistryDelegate<SpellStats0c> spell : spells) {
        final RegistryId id = spell.getId();
        final CharacterSpellInfo info = character.getSpellInfo(id);

        if(info.checkUnlock(character)) {
          info.unlock(gameState.timestamp_a0 + i);
        }

        seen.add(id);
        i++;
      }

      // Then add non-retail spells
      for(final RegistryId id : character.getAllSpells()) {
        if(!seen.contains(id)) {
          final CharacterSpellInfo info = character.getSpellInfo(id);

          if(info.checkUnlock(character)) {
            info.unlock(gameState.timestamp_a0 + i);
          }

          i++;
        }
      }
    }

    return character;
  }

  private void updateAddition(final CharacterData2c character, final int timestamp, final RegistryId id, final AdditionInfo saveAdditionInfo) {
    final CharacterAdditionInfo charAdditionInfo = character.getAdditionInfo(id);

    if(charAdditionInfo != null) {
      charAdditionInfo.level = saveAdditionInfo.level;
      charAdditionInfo.xp = saveAdditionInfo.xp;

      if(charAdditionInfo.checkUnlock(character)) {
        charAdditionInfo.unlock(timestamp);
      }
    }
  }

  @Override
  public void render(final SavedGame savedGame, final int x, final int y) {
    renderNumber(225, y + 6, this.level, 0x2, 2);
    renderNumber(270, y + 6, this.dlevel, 0x2, 2);
    renderHp(361, y + 6, this.hp, this.maxHp);
  }

  @Override
  public boolean inParty() {
    return (this.flags & IN_PARTY) != 0;
  }

  public static class AdditionInfo {
    public final int level;
    public final int xp;

    public AdditionInfo(final int level, final int xp) {
      this.level = level;
      this.xp = xp;
    }
  }
}
