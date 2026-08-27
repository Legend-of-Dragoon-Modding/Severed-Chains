package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

/**
 * Fired whenever an addition definition is resolved for a player character.
 *
 * <p>This includes battle setup, progression, animation loading, and menu display, so the event
 * may be fired repeatedly for the same character and addition. Listeners may replace
 * {@link #addition} with the definition the requester should use. The other fields provide
 * context and are not returned as event results.</p>
 */
public class ResolveAdditionEvent extends Event {
  /** The character for whom the addition is being resolved. */
  public final CharacterData2c character;

  /** The character's current progression and unlock state for this addition. */
  public final CharacterAdditionInfo additionInfo;

  /** The registry ID of the requested addition. */
  public final RegistryId additionId;

  /** The registered addition definition before any listener-provided replacement. */
  public final Addition baseAddition;

  /**
   * The addition definition returned to the requester after the event.
   *
   * <p>This initially references {@link #baseAddition}. Listeners may assign a replacement.</p>
   */
  public Addition addition;

  /**
   * Creates an addition-resolution request.
   *
   * @param character character for whom the addition is being resolved
   * @param additionInfo character progression and unlock state for the requested addition
   * @param additionId registry ID of the requested addition
   * @param baseAddition registered addition definition used as the initial result
   */
  public ResolveAdditionEvent(final CharacterData2c character, final CharacterAdditionInfo additionInfo, final RegistryId additionId, final Addition baseAddition) {
    if(character == null) {
      throw new IllegalArgumentException("Addition character cannot be null");
    }
    if(additionInfo == null) {
      throw new IllegalArgumentException("Addition info cannot be null");
    }
    if(additionId == null) {
      throw new IllegalArgumentException("Addition ID cannot be null");
    }
    if(baseAddition == null) {
      throw new IllegalArgumentException("Base addition cannot be null");
    }

    this.character = character;
    this.additionInfo = additionInfo;
    this.additionId = additionId;
    this.baseAddition = baseAddition;
    this.addition = baseAddition;
  }
}
