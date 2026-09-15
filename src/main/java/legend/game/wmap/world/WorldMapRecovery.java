package legend.game.wmap.world;

import legend.core.tags.MapTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;
import legend.game.types.GameState52c;
import legend.game.wmap.preset.WorldMapPreset;
import legend.game.wmap.preset.WorldMapPresetManager;

import javax.annotation.Nullable;

/** Unresolved history is an explicit recovery option, never the next active save position. */
public final class WorldMapRecovery {
  private WorldMapRecovery() { }

  public static void retain(final GameState52c state, final Tag position, final String reason) {
    if(state.retainedSaveTags.has("worldMapRecovery")) {
      attachPackage(state);
      return;
    }
    final MapTag recovery = new MapTag();
    final Tag original = state.retainedSaveTags.has("worldMapUnresolvedPosition") ? state.retainedSaveTags.get("worldMapUnresolvedPosition") : position;
    recovery.set("position", original.clone());
    recovery.set("preset", new StringTag(state.worldMapPreset));
    recovery.set("reason", new StringTag(reason));
    if(state.worldMapPackage != null) recovery.set("package", state.worldMapPackage.clone());
    state.retainedSaveTags.set("worldMapRecovery", recovery);
    state.retainedSaveTags.set("worldMapUnresolvedPosition", original.clone());
  }

  /** Saves omit a duplicate package when recovery and active content have the same identity. */
  public static void attachPackage(final GameState52c state) {
    if(state.worldMapPackage == null || !state.retainedSaveTags.has("worldMapRecovery")) return;
    final MapTag recovery = state.retainedSaveTags.get("worldMapRecovery").asMap();
    if(!recovery.has("package") && recovery.get("preset").asString().get().equals(state.worldMapPreset)) {
      recovery.set("package", state.worldMapPackage.clone());
    }
  }

  @Nullable
  public static Tag position(final GameState52c state) {
    if(state.retainedSaveTags.has("worldMapRecovery")) return state.retainedSaveTags.get("worldMapRecovery").asMap().get("position").clone();
    return state.retainedSaveTags.has("worldMapUnresolvedPosition") ? state.retainedSaveTags.get("worldMapUnresolvedPosition").clone() : null;
  }

  @Nullable
  public static WorldMapPreset preset(final GameState52c state) {
    final GameState52c source = new GameState52c();
    source.campaign = state.campaign;
    source.worldMapPreset = state.worldMapPreset;
    source.worldMapPackage = state.worldMapPackage;
    if(state.retainedSaveTags.has("worldMapRecovery")) {
      final MapTag recovery = state.retainedSaveTags.get("worldMapRecovery").asMap();
      source.worldMapPreset = recovery.get("preset").asString().get();
      source.worldMapPackage = recovery.has("package") ? recovery.get("package").asMap() : source.worldMapPreset.equals(state.worldMapPreset) ? state.worldMapPackage : null;
    }
    final WorldMapPreset preset = WorldMapPresetManager.load(source);
    if(source.worldMapFallback) throw new WorldMapDependencyException("Original world map recovery content is still unavailable");
    return preset;
  }

  public static void discard(final GameState52c state) {
    state.retainedSaveTags.remove("worldMapRecovery");
    state.retainedSaveTags.remove("worldMapUnresolvedPosition");
  }
}
