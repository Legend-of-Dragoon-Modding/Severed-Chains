package legend.game.progression;

import legend.game.types.Flags;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Campaign-owned facts with retail flag banks retained as the script ABI. */
public final class CampaignProgression {
  private final Flags storyFlags;
  private final Flags locationFlags;
  private final Map<RegistryId, Boolean> facts = new LinkedHashMap<>();
  private long revision;

  public CampaignProgression(final Flags storyFlags, final Flags locationFlags) {
    this.storyFlags = Objects.requireNonNull(storyFlags, "storyFlags");
    this.locationFlags = Objects.requireNonNull(locationFlags, "locationFlags");
  }

  public boolean hasFact(final RegistryId id) {
    return this.facts.getOrDefault(Objects.requireNonNull(id, "id"), false);
  }

  public void setFact(final RegistryId id, final boolean value) {
    final RegistryId factId = Objects.requireNonNull(id, "id");
    final Boolean previous = this.facts.put(factId, value);
    if(previous == null || previous != value) this.revision++;
  }

  public void removeFact(final RegistryId id) {
    if(this.facts.remove(Objects.requireNonNull(id, "id")) != null) this.revision++;
  }

  public void setStoryFlag(final int index, final boolean value) {
    if(this.storyFlags.get(index) != value) this.revision++;
    this.storyFlags.set(index, value);
  }

  public boolean storyFlag(final int index) {
    return this.storyFlags.get(index);
  }

  public void setLocationEnabled(final int index, final boolean value) {
    if(this.locationFlags.get(index) != value) this.revision++;
    this.locationFlags.set(index, value);
  }

  public boolean locationEnabled(final int index) {
    return this.locationFlags.get(index);
  }

  public Map<RegistryId, Boolean> facts() {
    return Map.copyOf(this.facts);
  }

  public void setFacts(final Map<RegistryId, Boolean> facts) {
    final Map<RegistryId, Boolean> replacement = new LinkedHashMap<>(Objects.requireNonNull(facts, "facts"));
    if(this.facts.equals(replacement)) return;

    this.facts.clear();
    this.facts.putAll(replacement);
    this.revision++;
  }

  public long revision() {
    return this.revision;
  }
}
