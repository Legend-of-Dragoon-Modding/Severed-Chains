package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.BattleEvent;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SetBentStatEvent extends BattleEvent {
  public final BattleEntity27c bent;
  public final BattleEntityStat stat;
  public int value;
  public RegistryId registryValue;

  public SetBentStatEvent(final Battle battle, final BattleEntity27c bent, final BattleEntityStat stat, final int value) {
    super(battle);
    this.bent = bent;
    this.stat = stat;
    this.value = value;
  }

  public SetBentStatEvent(final Battle battle, final BattleEntity27c bent, final BattleEntityStat stat, final RegistryId registryValue) {
    super(battle);
    this.bent = bent;
    this.stat = stat;
    this.registryValue = registryValue;
  }
}
