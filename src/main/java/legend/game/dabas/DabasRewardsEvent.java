package legend.game.dabas;

import legend.game.inventory.InventoryEntry;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;

public class DabasRewardsEvent extends Event {
  public int gold;
  public final List<InventoryEntry<?>> rewards = new ArrayList<>();
  public InventoryEntry<?> specialReward;
}
