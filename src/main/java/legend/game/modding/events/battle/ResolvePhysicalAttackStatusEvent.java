package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

public class ResolvePhysicalAttackStatusEvent extends Event {
  public final PlayerBattleEntity attacker;
  public final BattleEntity27c defender;
  @Nullable
  public final RegistryId additionId;
  public final boolean additionCompletedSuccessfully;
  public final int baseChance;
  public final int baseStatusMask;
  public int chance;
  public int statusMask;

  public ResolvePhysicalAttackStatusEvent(final PlayerBattleEntity attacker, final BattleEntity27c defender, @Nullable final RegistryId additionId, final boolean additionCompletedSuccessfully, final int baseChance, final int baseStatusMask) {
    if(attacker == null) {
      throw new IllegalArgumentException("Physical attack status attacker cannot be null");
    }
    if(defender == null) {
      throw new IllegalArgumentException("Physical attack status defender cannot be null");
    }

    this.attacker = attacker;
    this.defender = defender;
    this.additionId = additionId;
    this.additionCompletedSuccessfully = additionCompletedSuccessfully;
    this.baseChance = baseChance;
    this.baseStatusMask = baseStatusMask;
    this.chance = baseChance;
    this.statusMask = baseStatusMask;
  }
}
