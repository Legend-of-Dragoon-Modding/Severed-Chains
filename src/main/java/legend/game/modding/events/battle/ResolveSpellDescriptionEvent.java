package legend.game.modding.events.battle;

import legend.game.characters.CharacterData2c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ResolveSpellDescriptionEvent extends Event {
  public final CharacterData2c character;
  public final RegistryId spellId;
  public final SpellStats0c spell;
  public final String baseDescription;
  public String description;

  public ResolveSpellDescriptionEvent(final CharacterData2c character, final RegistryId spellId, final SpellStats0c spell, final String baseDescription) {
    this.character = character;
    this.spellId = spellId;
    this.spell = spell;
    this.baseDescription = baseDescription;
    this.description = baseDescription;
  }
}
