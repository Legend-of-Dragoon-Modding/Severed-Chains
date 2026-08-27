package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

/**
 * Fired before the status chance roll for each player physical attack.
 *
 * <p>Listeners may replace {@link #chance} and {@link #statusMask}; those values are used for the
 * pending roll and status selection. The base values remain available for comparison.</p>
 */
public class ResolvePhysicalAttackStatusEvent extends Event {
  /** The player making the physical attack. */
  public final PlayerBattleEntity attacker;

  /** The battle entity receiving the physical attack. */
  public final BattleEntity27c defender;

  /** The selected addition ID, or {@code null} when no addition is active. */
  @Nullable
  public final RegistryId additionId;

  /** Whether this attack reached the selected non-Dragoon addition's final hit. */
  public final boolean additionCompletedSuccessfully;

  /** Status chance supplied by the attacker before listener changes. */
  public final int baseChance;

  /** Status bits supplied by the attacker before listener changes. */
  public final int baseStatusMask;

  /**
   * Percent chance used for the pending status roll.
   *
   * <p>This initially equals {@link #baseChance}. Listeners may assign a replacement.</p>
   */
  public int chance;

  /**
   * Status bits used when the chance roll succeeds.
   *
   * <p>This initially equals {@link #baseStatusMask}. Listeners may assign a replacement.</p>
   */
  public int statusMask;

  /**
   * Creates a physical-attack status-resolution request.
   *
   * @param attacker player making the physical attack
   * @param defender battle entity receiving the physical attack
   * @param additionId selected addition ID, or {@code null} when no addition is active
   * @param additionCompletedSuccessfully whether this attack reached the selected non-Dragoon
   *                                       addition's final hit
   * @param baseChance status chance used as the initial result
   * @param baseStatusMask status bits used as the initial result
   */
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
