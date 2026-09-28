package legend.game.modding.events.battle;

import legend.game.characters.CharacterData2c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

/**
 * Fired when a player spell description is requested for display in battle.
 *
 * <p>This event may be fired repeatedly while the spell menu is displayed. Listeners may replace
 * {@link #description} with the text the menu should display. The other fields provide read-only
 * context for producing that text.</p>
 */
public class ResolveSpellDescriptionEvent extends Event {
    /** The player character whose spell description is being requested. */
    public final CharacterData2c character;

    /** The registry ID of the spell being described. */
    public final RegistryId spellId;

    /** The effective spell stats associated with the displayed spell. */
    public final SpellStats0c spell;

    /** The translated description before any listener-provided replacement. */
    public final String baseDescription;

    /**
     * The description returned to the menu after the event.
     *
     * <p>This initially contains {@link #baseDescription}. Listeners may assign a replacement.</p>
     */
    public String description;

    /**
     * Creates a spell-description request.
     *
     * @param character the player character whose spell description is being requested
     * @param spellId the registry ID of the spell being described
     * @param spell the effective spell stats associated with the displayed spell
     * @param baseDescription the translated description used as the initial result
     */
    public ResolveSpellDescriptionEvent(final CharacterData2c character, final RegistryId spellId, final SpellStats0c spell, final String baseDescription) {
        this.character = character;
        this.spellId = spellId;
    this.spell = spell;
    this.baseDescription = baseDescription;
    this.description = baseDescription;
  }
}
