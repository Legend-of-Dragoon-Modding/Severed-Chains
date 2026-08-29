package legend.game.modding.events.battle;

import legend.game.characters.CharacterData2c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

/**
 * Fired when the effective stats for a player spell are requested.
 *
 * <p>This event may be fired during battle setup, when a spell becomes active, or when spell
 * stats are displayed in a menu. It may therefore be fired more than once for the same character
 * and spell.</p>
 *
 * <p>Listeners may replace {@link #spell} with the stats the requester should use. The other
 * fields provide read-only context. Prefer assigning a replacement to {@code spell} instead of
 * modifying {@link #baseSpell} so the registered base spell remains unchanged.</p>
 */
public class SpellStatsEvent extends Event {
    /** The player character whose spell stats are being requested. */
    public final CharacterData2c character;

    /** The registry ID of the requested spell. */
    public final RegistryId spellId;

    /** The registered spell stats before any listener-provided replacement. */
    public final SpellStats0c baseSpell;

    /**
     * The effective spell stats returned to the requester after the event.
     *
     * <p>This initially references {@link #baseSpell}. Listeners may assign a replacement.</p>
     */
    public SpellStats0c spell;

    /**
     * Creates a spell-stats request using the registered ID of {@code spell}.
     *
     * @param character the player character whose spell stats are being requested
     * @param spell the registered spell stats used as both the base value and initial result
     */
    public SpellStatsEvent(final CharacterData2c character, final SpellStats0c spell) {
        this(character, spell.getRegistryId(), spell);
    }

    /**
     * Creates a spell-stats request for a specific spell ID and base value.
     *
     * @param character the player character whose spell stats are being requested
     * @param spellId the registry ID of the requested spell
     * @param baseSpell the registered spell stats used as the initial result
     */
    public SpellStatsEvent(final CharacterData2c character, final RegistryId spellId, final SpellStats0c baseSpell) {
        this.character = character;
        this.spellId = spellId;
    this.baseSpell = baseSpell;
    this.spell = baseSpell;
  }
}
