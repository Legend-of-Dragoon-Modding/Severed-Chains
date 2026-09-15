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
    this.storyFlags.set(index, value);
  }

  public boolean storyFlag(final int index) {
    return this.storyFlags.get(index);
  }

  public void setLocationEnabled(final int index, final boolean value) {
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
    return this.revision + this.storyFlags.revision() + this.locationFlags.revision();
  }

  /** A detached view for consumers; later script writes cannot alter the snapshot. */
  public Snapshot snapshot() {
    final Flags story = new Flags(this.storyFlags.count());
    story.set(this.storyFlags);
    final Flags locations = new Flags(this.locationFlags.count());
    locations.set(this.locationFlags);
    return new Snapshot(this.revision(), this.facts(), story, locations);
  }

  public static final class Snapshot {
    public final long revision;
    public final Map<RegistryId, Boolean> facts;
    private final Flags story;
    private final Flags locations;

    private Snapshot(final long revision, final Map<RegistryId, Boolean> facts, final Flags story, final Flags locations) {
      this.revision = revision;
      this.facts = facts;
      this.story = story;
      this.locations = locations;
    }

    public boolean storyFlag(final int index) { return this.story.get(index); }
    public boolean locationEnabled(final int index) { return this.locations.get(index); }
  }
}
