package legend.game.modding.events.characters;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResolveCharacterAdditionSaveEvent extends Event {
  public final CharacterData2c character;
  @Nullable
  public final RegistryId baseSelectedAddition;

  private final Map<RegistryId, CharacterAdditionInfo> baseAdditions;
  @Nullable
  private RegistryId selectedAddition;
  private Map<RegistryId, CharacterAdditionInfo> additions;

  public ResolveCharacterAdditionSaveEvent(final CharacterData2c character) {
    if(character == null) {
      throw new IllegalArgumentException("Addition save character cannot be null");
    }

    this.character = character;
    this.baseSelectedAddition = character.selectedAddition_19;
    this.baseAdditions = copyAdditions(character);
    this.selectedAddition = this.baseSelectedAddition;
    this.additions = copyAdditions(this.baseAdditions);
  }

  @Nullable
  public RegistryId getSelectedAddition() {
    return this.selectedAddition;
  }

  public Map<RegistryId, CharacterAdditionInfo> getBaseAdditions() {
    return copyAdditions(this.baseAdditions);
  }

  public Map<RegistryId, CharacterAdditionInfo> getAdditions() {
    return copyAdditions(this.additions);
  }

  public void resolve(@Nullable final RegistryId selectedAddition, final Map<RegistryId, CharacterAdditionInfo> additions) {
    if(additions == null) {
      throw new IllegalArgumentException("Resolved addition save data cannot be null");
    }

    if(!additions.keySet().equals(this.baseAdditions.keySet())) {
      throw new IllegalArgumentException("Resolved addition save IDs must match the character's assigned additions");
    }

    for(final Map.Entry<RegistryId, CharacterAdditionInfo> entry : additions.entrySet()) {
      if(entry.getKey() == null || entry.getValue() == null) {
        throw new IllegalArgumentException("Resolved addition save IDs and info cannot be null");
      }
    }

    if(selectedAddition != null && !additions.containsKey(selectedAddition)) {
      throw new IllegalArgumentException("Resolved selected addition must be assigned to the character");
    }

    this.selectedAddition = selectedAddition;
    this.additions = copyAdditions(additions);
  }

  private static Map<RegistryId, CharacterAdditionInfo> copyAdditions(final CharacterData2c character) {
    final Map<RegistryId, CharacterAdditionInfo> additions = new LinkedHashMap<>();
    for(final RegistryId additionId : character.getAllAdditions()) {
      additions.put(additionId, new CharacterAdditionInfo(character.getAdditionInfo(additionId)));
    }
    return Collections.unmodifiableMap(additions);
  }

  private static Map<RegistryId, CharacterAdditionInfo> copyAdditions(final Map<RegistryId, CharacterAdditionInfo> source) {
    final Map<RegistryId, CharacterAdditionInfo> additions = new LinkedHashMap<>();
    for(final Map.Entry<RegistryId, CharacterAdditionInfo> entry : source.entrySet()) {
      additions.put(entry.getKey(), new CharacterAdditionInfo(entry.getValue()));
    }
    return Collections.unmodifiableMap(additions);
  }
}
