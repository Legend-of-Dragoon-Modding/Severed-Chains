package legend.game.modding.events.characters;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fired immediately before a character's selected addition and addition progression are saved.
 *
 * <p>The event starts with defensive copies of the character's current addition state. Listeners
 * may call {@link #resolve(RegistryId, Map)} to provide a different save projection without
 * mutating the live character. The projected selected addition and progression returned by this
 * event are written to the save.</p>
 */
public class ResolveCharacterAdditionSaveEvent extends Event {
  /** The character whose addition state is being saved. */
  public final CharacterData2c character;

  /** The character's selected addition before listener changes, or {@code null} when unselected. */
  @Nullable
  public final RegistryId baseSelectedAddition;

  private final Map<RegistryId, CharacterAdditionInfo> baseAdditions;
  @Nullable
  private RegistryId selectedAddition;
  private Map<RegistryId, CharacterAdditionInfo> additions;

  /**
   * Creates a character-addition save projection from the character's current state.
   *
   * @param character character whose addition state is being saved
   */
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

  /**
   * Returns the selected addition currently projected for the save.
   *
   * @return selected addition ID, or {@code null} when the save should contain no selection
   */
  @Nullable
  public RegistryId getSelectedAddition() {
    return this.selectedAddition;
  }

  /**
   * Returns a defensive snapshot of every assigned addition before listener changes.
   *
   * <p>The map is unmodifiable. Its {@link CharacterAdditionInfo} values are copies, so changing a
   * value does not modify the event or live character.</p>
   *
   * @return unmodifiable base addition-state snapshot keyed by assigned addition ID
   */
  public Map<RegistryId, CharacterAdditionInfo> getBaseAdditions() {
    return copyAdditions(this.baseAdditions);
  }

  /**
   * Returns a defensive snapshot of the addition state currently projected for the save.
   *
   * <p>The map is unmodifiable. To change the projection, copy this map and its values as needed,
   * then pass the completed map to {@link #resolve(RegistryId, Map)}.</p>
   *
   * @return unmodifiable projected addition state keyed by assigned addition ID
   */
  public Map<RegistryId, CharacterAdditionInfo> getAdditions() {
    return copyAdditions(this.additions);
  }

  /**
   * Replaces the selected addition and progression projected for the save.
   *
   * <p>The map must contain exactly the character's assigned addition IDs and no null keys or
   * values. A non-null selected addition must be one of those IDs. The accepted map and each
   * {@link CharacterAdditionInfo} value are defensively copied.</p>
   *
   * @param selectedAddition selected addition to write, or {@code null} to save no selection
   * @param additions complete projected progression keyed by assigned addition ID
   * @throws IllegalArgumentException if the map is null, contains different addition IDs, contains
   *                                  a null key or value, or omits a non-null selection
   */
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
