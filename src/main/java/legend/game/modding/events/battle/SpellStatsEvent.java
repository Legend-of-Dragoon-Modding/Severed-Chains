package legend.game.modding.events.battle;

import legend.game.characters.CharacterData2c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SpellStatsEvent extends Event {
  public final CharacterData2c character;
  public final RegistryId spellId;
  public final SpellStats0c baseSpell;
  public SpellStats0c spell;

  public SpellStatsEvent(final CharacterData2c character, final SpellStats0c spell) {
    this(character, spell.getRegistryId(), spell);
  }

  public SpellStatsEvent(final CharacterData2c character, final RegistryId spellId, final SpellStats0c baseSpell) {
    this.character = character;
    this.spellId = spellId;
    this.baseSpell = baseSpell;
    this.spell = baseSpell;
  }
}
