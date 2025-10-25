package legend.game.modding.events.inventory;

import org.legendofdragoon.modloader.events.Event;

public class DescriptionEvent extends Event {
  public final String translationKey;
  public String description;

  public DescriptionEvent(final String translationKey, final String description) {
    this.translationKey = translationKey;
    this.description = description;
  }
}
