package legend.game.combat.ui;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

import java.util.HashSet;
import java.util.Set;

public class GatherBattleActionsEvent extends BattleEvent {
  public final PlayerBattleEntity player;

  /** Map of action -> sorting value, higher values sort the icon further to the right in the list */
  public final Object2IntMap<BattleAction> actions = new Object2IntOpenHashMap<>();

  /** Any action in this set will be disabled if it is present in {@link #actions} */
  public final Set<BattleAction> disabledActions = new HashSet<>();

  public GatherBattleActionsEvent(final Battle battle, final PlayerBattleEntity player) {
    super(battle);
    this.player = player;
  }
}
