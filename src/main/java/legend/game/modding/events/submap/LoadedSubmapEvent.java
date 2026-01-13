package legend.game.modding.events.submap;

import legend.game.submap.Submap;

public interface LoadedSubmapEvent extends SubmapEvent {
  Submap getSubmap();
}
