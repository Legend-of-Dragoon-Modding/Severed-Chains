package legend.game.modding.events.scripting;

import legend.game.scripting.ScriptFlagArrayEnum;
import legend.core.memory.types.IntRef;
import org.legendofdragoon.modloader.events.Event;

/**
 * Fired when scripts are accessing either scriptReadGlobalFlags1 or scriptReadGlobalFlags2.
 * You can edit `flagValue` to change what is returned to the scripts.
 * <p>The {@link ScriptFlagArrayEnum} stored in {@link #flagArray} is either:</p>
 * <ul>
 *   <li>{@link ScriptFlagArrayEnum#FLAGS1} event originated from scriptReadGlobalFlags1</li>
 *   <li>{@link ScriptFlagArrayEnum#FLAGS2} event originated from scriptReadGlobalFlags2</li>
 * </ul>
 */
public class ReadGlobalFlagsEvent extends Event {
  private final IntRef flagIndex;

  public final ScriptFlagArrayEnum flagArray;
  public boolean flagValue;

  public ReadGlobalFlagsEvent(final IntRef flagIndexRef, final ScriptFlagArrayEnum flagArray) {
    this.flagIndex = flagIndexRef;
    this.flagArray = flagArray;
  }

  public int getFlagIndex() {
    return this.flagIndex.get();
  }
}
