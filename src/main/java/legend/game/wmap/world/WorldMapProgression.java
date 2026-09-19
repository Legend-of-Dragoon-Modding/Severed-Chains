package legend.game.wmap.world;

import legend.game.types.Flags;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Immutable input shared by all availability and travel decisions in a resolved view. */
public final class WorldMapProgression {
  private final int[] story;
  private final int[] locations;
  private final Map<RegistryId, Boolean> facts;
  @Nullable
  private final WorldMapObjective objective;

  private WorldMapProgression(final Builder builder) {
    this.story = builder.story.clone();
    this.locations = builder.locations.clone();
    this.facts = Map.copyOf(builder.facts);
    this.objective = builder.objective;
  }

  public boolean storyFlag(final int bit) {
    return bit >= 0 && bit >>> 5 < this.story.length && (this.story[bit >>> 5] & 1 << (bit & 31)) != 0;
  }

  public boolean locationEnabled(final int index) {
    return index >= 0 && index >>> 5 < this.locations.length && (this.locations[index >>> 5] & 1 << (index & 31)) != 0;
  }

  public boolean hasFact(final RegistryId id) {
    return this.facts.getOrDefault(id, false);
  }

  public Map<RegistryId, Boolean> facts() {
    return this.facts;
  }

  @Nullable
  public WorldMapObjective objective() {
    return this.objective;
  }

  public boolean matches(final Flags story, final Flags locations) {
    return matches(this.story, story) && matches(this.locations, locations);
  }

  private static boolean matches(final int[] saved, final Flags flags) {
    if(saved.length != flags.count()) {
      return false;
    }

    for(int i = 0; i < saved.length; i++) {
      if(saved[i] != flags.getRaw(i)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean equals(final Object other) {
    return other instanceof final WorldMapProgression value && Arrays.equals(this.story, value.story) && Arrays.equals(this.locations, value.locations) && this.facts.equals(value.facts) && Objects.equals(this.objective, value.objective);
  }

  @Override
  public int hashCode() {
    return 31 * (31 * (31 * Arrays.hashCode(this.story) + Arrays.hashCode(this.locations)) + this.facts.hashCode()) + Objects.hashCode(this.objective);
  }

  public static final class Builder {
    private final int[] story;
    private final int[] locations;
    private final Map<RegistryId, Boolean> facts = new LinkedHashMap<>();
    @Nullable
    private WorldMapObjective objective;

    public Builder(final Flags story, final Flags locations) {
      this.story = copy(story);
      this.locations = copy(locations);
    }

    public Builder fact(final RegistryId id, final boolean value) {
      this.facts.put(Objects.requireNonNull(id, "id"), value);
      return this;
    }

    public Builder objective(@Nullable final WorldMapObjective objective) {
      this.objective = objective;
      return this;
    }

    public WorldMapProgression build() {
      return new WorldMapProgression(this);
    }

    private static int[] copy(final Flags flags) {
      final int[] values = new int[flags.count()];
      for(int i = 0; i < values.length; i++) {
        values[i] = flags.getRaw(i);
      }
      return values;
    }
  }
}
