package legend.game.inventory;

import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GatherEquipmentTypesEvent extends Event {
  private final Map<RegistryId, Set<String>> equipmentToEquipmentTypes;
  private final Map<String, Set<RegistryId>> equipmentTypeToEquipment;

  public GatherEquipmentTypesEvent(final Map<RegistryId, Set<String>> equipmentToEquipmentTypes, final Map<String, Set<RegistryId>> equipmentTypeToEquipment) {
    this.equipmentToEquipmentTypes = equipmentToEquipmentTypes;
    this.equipmentTypeToEquipment = equipmentTypeToEquipment;
  }

  public void add(final Equipment equipment, final String equipmentType) {
    this.add(equipment.getRegistryId(), equipmentType);
  }

  public void add(final RegistryId equipment, final String equipmentType) {
    this.equipmentToEquipmentTypes.computeIfAbsent(equipment, k -> new HashSet<>()).add(equipmentType);
    this.equipmentTypeToEquipment.computeIfAbsent(equipmentType, k -> new HashSet<>()).add(equipment);
  }

  public void remove(final Equipment equipment, final String equipmentType) {
    this.remove(equipment.getRegistryId(), equipmentType);
  }

  public void remove(final RegistryId equipment, final String equipmentType) {
    this.equipmentToEquipmentTypes.computeIfAbsent(equipment, k -> new HashSet<>()).remove(equipmentType);
    this.equipmentTypeToEquipment.computeIfAbsent(equipmentType, k -> new HashSet<>()).remove(equipment);
  }

  public void add(final Equipment equipment, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.add(equipment.getRegistryId(), equipmentType);
    }
  }

  public void add(final RegistryId equipment, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.add(equipment, equipmentType);
    }
  }

  public void remove(final Equipment equipment, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.remove(equipment.getRegistryId(), equipmentType);
    }
  }

  public void remove(final RegistryId equipment, final String... equipmentTypes) {
    for(final String equipmentType : equipmentTypes) {
      this.remove(equipment, equipmentType);
    }
  }
}
