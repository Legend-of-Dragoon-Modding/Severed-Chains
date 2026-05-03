package legend.game.inventory;

import legend.game.characters.CharacterTemplate;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GatherCharacterEquipmentTypesEvent extends Event {
  private final Map<RegistryId, Set<String>> characterToEquipmentTypes;
  private final Map<String, Set<RegistryId>> equipmentTypeToCharacters;

  public GatherCharacterEquipmentTypesEvent(final Map<RegistryId, Set<String>> characterToEquipmentTypes, final Map<String, Set<RegistryId>> equipmentTypeToCharacters) {
    this.characterToEquipmentTypes = characterToEquipmentTypes;
    this.equipmentTypeToCharacters = equipmentTypeToCharacters;
  }

  public void add(final CharacterTemplate character, final String equipmentType) {
    this.add(character.getRegistryId(), equipmentType);
  }

  public void add(final RegistryId character, final String equipmentType) {
    this.characterToEquipmentTypes.computeIfAbsent(character, k -> new HashSet<>()).add(equipmentType);
    this.equipmentTypeToCharacters.computeIfAbsent(equipmentType, k -> new HashSet<>()).add(character);
  }

  public void remove(final CharacterTemplate character, final String equipmentType) {
    this.remove(character.getRegistryId(), equipmentType);
  }

  public void remove(final RegistryId character, final String equipmentType) {
    this.characterToEquipmentTypes.computeIfAbsent(character, k -> new HashSet<>()).remove(equipmentType);
    this.equipmentTypeToCharacters.computeIfAbsent(equipmentType, k -> new HashSet<>()).remove(character);
  }

  public void add(final CharacterTemplate character, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.add(character.getRegistryId(), equipmentType);
    }
  }

  public void add(final RegistryId character, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.add(character, equipmentType);
    }
  }

  public void remove(final CharacterTemplate character, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.remove(character.getRegistryId(), equipmentType);
    }
  }

  public void remove(final RegistryId character, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.remove(character, equipmentType);
    }
  }
}
