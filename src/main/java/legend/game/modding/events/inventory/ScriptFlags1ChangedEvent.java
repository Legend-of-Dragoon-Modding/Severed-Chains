package legend.game.modding.events.inventory;

/** Will be replaced when flags are overhauled, use at your own risk */
@Deprecated
public class ScriptFlags1ChangedEvent extends InventoryEvent {
  public final int index;
  public boolean set;

  public ScriptFlags1ChangedEvent(final int index, final boolean set) {
    this.index = index;
    this.set = set;
  }
}
