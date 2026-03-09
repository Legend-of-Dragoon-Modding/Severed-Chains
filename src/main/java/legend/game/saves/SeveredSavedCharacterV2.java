package legend.game.saves;

import legend.core.GameEngine;
import legend.game.additions.UnlockState;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.characters.VitalsStat;
import legend.game.inventory.Equipment;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.renderHp;
import static legend.game.SItem.renderNumber;
import static legend.game.characters.CharacterData2c.IN_PARTY;

public class SeveredSavedCharacterV2 implements SavedCharacter {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SeveredSavedCharacterV2.class);

  public final RegistryId templateId;

  public int xp;
  public int flags;
  public StatCollection stats;
  public int dxp;
  public int status;
  public int level;
  public int dlevel;
  public final Map<EquipmentSlot, RegistryId> equipmentIds = new EnumMap<>(EquipmentSlot.class);
  public RegistryId selectedAddition;
  public final Map<RegistryId, AdditionInfo> additionInfo = new HashMap<>();
  public final Map<RegistryId, SpellInfo> spellInfo = new HashMap<>();

  public SeveredSavedCharacterV2(final RegistryId templateId) {
    this.templateId = templateId;
  }

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(this.templateId).get();
    final CharacterData2c character = template.make(gameState);

    character.xp_00 = this.xp;
    character.partyFlags_04 = this.flags;
    character.stats.set(this.stats);
    character.dlevelXp_0e = this.dxp;
    character.status_10 = this.status;
    character.level_12 = this.level;
    character.dlevel_13 = this.dlevel;

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

    for(final var entry : this.additionInfo.entrySet()) {
      final RegistryId additionId = entry.getKey();
      final CharacterAdditionInfo charAdditionInfo = character.getAdditionInfo(additionId);

      if(charAdditionInfo != null) {
        final AdditionInfo saveAdditionInfo = this.additionInfo.get(additionId);
        charAdditionInfo.setUnlockState(saveAdditionInfo.unlockState, saveAdditionInfo.unlockTimestamp);
        charAdditionInfo.level = saveAdditionInfo.level;
        charAdditionInfo.xp = saveAdditionInfo.xp;
      }
    }

    if(this.selectedAddition != null) {
      if(REGISTRIES.additions.hasEntry(this.selectedAddition)) {
        character.selectedAddition_19 = this.selectedAddition;
      } else {
        character.selectedAddition_19 = character.getUnlockedAdditions().getFirst();
      }
    }

    for(final var entry : this.spellInfo.entrySet()) {
      final RegistryId spellId = entry.getKey();
      final CharacterSpellInfo charSpellInfo = character.getSpellInfo(spellId);

      if(charSpellInfo != null) {
        final SpellInfo saveSpellInfo = this.spellInfo.get(spellId);
        charSpellInfo.setUnlockState(saveSpellInfo.unlockState, saveSpellInfo.unlockTimestamp);
      }
    }

    return character;
  }

  @Override
  public void render(final SavedGame savedGame, final int x, final int y) {
    final VitalsStat hp = this.stats.getStat(LodMod.HP_STAT.get());

    renderNumber(225, y + 6, this.level, 0x2, 2);
    renderNumber(270, y + 6, this.dlevel, 0x2, 2);
    renderHp(361, y + 6, hp.getCurrent(), hp.getMax());
  }

  @Override
  public boolean inParty() {
    return (this.flags & IN_PARTY) != 0;
  }

  public static class AdditionInfo {
    public final UnlockState unlockState;
    public final int unlockTimestamp;
    public final int level;
    public final int xp;

    public AdditionInfo(final UnlockState unlockState, final int unlockTimestamp, final int level, final int xp) {
      this.unlockState = unlockState;
      this.unlockTimestamp = unlockTimestamp;
      this.level = level;
      this.xp = xp;
    }
  }

  public static class SpellInfo {
    public final UnlockState unlockState;
    public final int unlockTimestamp;

    public SpellInfo(final UnlockState unlockState, final int unlockTimestamp) {
      this.unlockState = unlockState;
      this.unlockTimestamp = unlockTimestamp;
    }
  }
}
