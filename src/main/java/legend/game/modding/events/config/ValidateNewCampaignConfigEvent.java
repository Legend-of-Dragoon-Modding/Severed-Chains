package legend.game.modding.events.config;

import legend.game.saves.ConfigCollection;

import java.util.ArrayList;
import java.util.List;

public class ValidateNewCampaignConfigEvent extends ConfigEvent {
  public final ConfigCollection configCollection;
  private final List<String> errors = new ArrayList<>();

  public ValidateNewCampaignConfigEvent(final ConfigCollection configCollection) {
    this.configCollection = configCollection;
  }

  public void addError(final String error) {
    if(error != null && !error.isBlank()) {
      this.errors.add(error);
    }
  }

  public boolean hasErrors() {
    return !this.errors.isEmpty();
  }

  public List<String> errors() {
    return List.copyOf(this.errors);
  }
}
