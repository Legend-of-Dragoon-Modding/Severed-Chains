package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ActiveSpellEvent extends Event {
  public final BattleEntity27c bent;
  public final int spellId;
  public final RegistryId registryId;
  public SpellStats0c spell;

  public ActiveSpellEvent(final BattleEntity27c bent, final int spellId, final SpellStats0c spell) {
    this.bent = bent;
    this.spellId = spellId;
    this.spell = spell;
    this.registryId = null;
  }

  public ActiveSpellEvent(final BattleEntity27c bent, final RegistryId registryId, final SpellStats0c spell) {
    this.bent = bent;
    this.spellId = -1;
    this.spell = spell;
    this.registryId = registryId;
  }
}
