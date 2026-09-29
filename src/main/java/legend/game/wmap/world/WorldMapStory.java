package legend.game.wmap.world;

import legend.game.types.Flags;
import legend.game.wmap.WMapDestinationMarker2c;

/** Compatibility boundary for retail's ordered, full-mask story presets and objective marker. */
public final class WorldMapStory {
  private WorldMapStory() { }

  public static int select(final Flags story, final WMapDestinationMarker2c[] presets) {
    int selected = -1;
    for(int i = 0; i < Math.min(49, presets.length); i++) {
      if(story.get(presets[i].packedFlag_00)) {
        selected = i;
      }
    }
    return selected;
  }

  public static int apply(final Flags story, final Flags locations, final WMapDestinationMarker2c[] presets) {
    final int selected = select(story, presets);
    if(selected >= 0) {
      for(int i = 0; i < locations.count(); i++) {
        locations.setRaw(i, i < presets[selected].flags_04.length ? presets[selected].flags_04[i] : 0);
      }
    }
    return selected;
  }
}
