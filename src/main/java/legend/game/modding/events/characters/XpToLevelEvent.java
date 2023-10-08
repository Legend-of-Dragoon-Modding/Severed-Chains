package legend.game.modding.events.characters;

import org.legendofdragoon.modloader.events.Event;

public class XpToLevelEvent extends Event {
  public int charId;
  public int level;
  public int xp;

  public XpToLevelEvent(final int charId, final int level, final int xp) {
    this.charId = charId;
    this.level = level;
    this.xp = xp;
  }
}
