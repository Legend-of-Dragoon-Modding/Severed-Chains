package legend.game.wmap.world;

import legend.game.types.Flags;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

/** Campaign state owned by world-map progression and its legacy script adapters. */
public final class WorldMapCampaignSnapshot implements AutoCloseable {
  private final GameState52c state;
  private final Flags story = new Flags(0);
  private final Flags script = new Flags(0);
  private final Flags enabled = new Flags(0);
  private final Flags visited = new Flags(0);
  private final WorldMapPortalState portals = new WorldMapPortalState();
  private final Map<RegistryId, Boolean> facts;
  private final int[] scriptData;
  private final String preset;
  private final long storyRevision;
  private final long scriptRevision;
  private final long enabledRevision;
  private final long visitedRevision;
  private final long portalRevision;

  public WorldMapCampaignSnapshot(final GameState52c state) {
    this.state = state;
    this.story.set(state.scriptFlags2_bc);
    this.script.set(state.scriptFlags1_13c);
    this.enabled.set(state.wmapFlags_15c);
    this.visited.set(state.visitedLocations_17c);
    this.portals.set(state.worldMapPortalState);
    this.facts = state.campaignProgression.facts();
    this.scriptData = state.scriptData_08.clone();
    this.preset = state.worldMapPreset;
    this.storyRevision = state.scriptFlags2_bc.revision();
    this.scriptRevision = state.scriptFlags1_13c.revision();
    this.enabledRevision = state.wmapFlags_15c.revision();
    this.visitedRevision = state.visitedLocations_17c.revision();
    this.portalRevision = state.worldMapPortalState.revision();
  }

  public void restore() {
    this.state.scriptFlags2_bc.set(this.story);
    this.state.scriptFlags1_13c.set(this.script);
    this.state.wmapFlags_15c.set(this.enabled);
    this.state.visitedLocations_17c.set(this.visited);
    this.state.worldMapPortalState.set(this.portals);
    this.state.campaignProgression.setFacts(this.facts);
    System.arraycopy(this.scriptData, 0, this.state.scriptData_08, 0, this.scriptData.length);
    this.state.worldMapPreset = this.preset;
  }

  /** Preparation must only describe a destination, never publish campaign mutations. */
  @Override
  public void close() {
    if(this.storyRevision == this.state.scriptFlags2_bc.revision()
      && this.scriptRevision == this.state.scriptFlags1_13c.revision()
      && this.enabledRevision == this.state.wmapFlags_15c.revision()
      && this.visitedRevision == this.state.visitedLocations_17c.revision()
      && this.portalRevision == this.state.worldMapPortalState.revision()
      && this.facts.equals(this.state.campaignProgression.facts())
      && java.util.Arrays.equals(this.scriptData, this.state.scriptData_08)
      && this.preset.equals(this.state.worldMapPreset)) return;
    this.restore();
    throw new IllegalStateException("World map preparation mutated campaign state; defer changes with WorldMapWarpEvent.onActivate");
  }
}
