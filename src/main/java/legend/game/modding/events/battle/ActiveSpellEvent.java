package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ActiveSpellEvent extends Event {
  public final BattleEntity27c bent;
  public final RegistryId registryId;
  public SpellStats0c spell;

  /**
   * Changes the active spell of a battle entity. Allowing the ability to change its properties. Typically used for MonsterBattleEntities.
   *
   * @param bent Battle Entity to replace the SpellStats0c spell of
   * @param registryId The registry ID of the SpellStats0c spell
   * @param spell The current SpellStats0c that was set by the script
   */
  public ActiveSpellEvent(final BattleEntity27c bent, final RegistryId registryId, final SpellStats0c spell) {
    this.bent = bent;
    this.spell = spell;
    this.registryId = registryId;
  }
}
