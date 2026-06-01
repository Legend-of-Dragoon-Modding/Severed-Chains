package legend.game.inventory;

import legend.game.characters.CharacterTemplate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.EVENTS;

public final class EquipmentTypes {
  private EquipmentTypes() { }

  public static final String LONGSWORD = "longsword";
  public static final String POLEARM = "polearm";
  public static final String BOW = "bow";
  public static final String SHORTSWORD = "shortsword";
  public static final String HAND = "hand";
  public static final String HAMMER = "hammer";
  public static final String AXE = "axe";
  public static final String DART = "dart";
  public static final String LAVITZ = "lavitz";
  public static final String SHANA = "shana";
  public static final String ROSE = "rose";
  public static final String HASCHEL = "haschel";
  public static final String ALBERT = "albert";
  public static final String MERU = "meru";
  public static final String KONGOL = "kongol";
  public static final String MIRANDA = "miranda";
  public static final String NEUTRAL = "neutral";
  public static final String MALE = "male";
  public static final String FEMALE = "female";
  public static final String ADDITIONS = "additions";
  public static final String HEAVY = "heavy";
  public static final String MEDIUM = "medium";
  public static final String LIGHT = "light";
  public static final String ARMOR_OF_YORE = "armor_of_yore";

  private static final Map<RegistryId, Set<String>> equipmentToEquipmentTypes = new HashMap<>();
  private static final Map<String, Set<RegistryId>> equipmentTypeToEquipment = new HashMap<>();

  private static final Map<RegistryId, Set<String>> characterToEquipmentTypes = new HashMap<>();
  private static final Map<String, Set<RegistryId>> equipmentTypeToCharacters = new HashMap<>();

  public static void loadEquipmentTypes() {
    equipmentToEquipmentTypes.clear();
    equipmentTypeToEquipment.clear();
    characterToEquipmentTypes.clear();
    equipmentTypeToCharacters.clear();

    EVENTS.postEvent(new GatherEquipmentTypesEvent(equipmentToEquipmentTypes, equipmentTypeToEquipment));
    EVENTS.postEvent(new GatherCharacterEquipmentTypesEvent(characterToEquipmentTypes, equipmentTypeToCharacters));
  }

  public static Set<String> getEquipmentTypesForEquipment(final Equipment equipment) {
    return equipmentToEquipmentTypes.getOrDefault(equipment.getRegistryId(), Set.of());
  }

  public static Set<RegistryId> getEquipmentForEquipmentType(final String equipmentType) {
    return equipmentTypeToEquipment.getOrDefault(equipmentType, Set.of());
  }

  public static Set<String> getEquipmentTypesForCharacter(final CharacterTemplate characterTemplate) {
    return characterToEquipmentTypes.getOrDefault(characterTemplate.getRegistryId(), Set.of());
  }

  public static Set<RegistryId> getCharactersForEquipmentType(final String equipmentType) {
    return equipmentTypeToCharacters.getOrDefault(equipmentType, Set.of());
  }
}
