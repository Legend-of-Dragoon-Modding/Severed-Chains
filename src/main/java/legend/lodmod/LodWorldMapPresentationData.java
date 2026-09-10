package legend.lodmod;

import legend.core.GameEngine;
import legend.game.wmap.registries.RegisterWorldMapBehavioursEvent;
import legend.game.wmap.registries.RegisterWorldMapPresentationProfilesEvent;
import legend.game.wmap.registries.WorldMapPresentationProfileEntry;
import legend.game.wmap.world.WorldMapBehaviour;
import legend.game.wmap.world.WorldMapPresentationProfile;
import legend.game.wmap.world.WorldMapTravel;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodWorldMapPresentationData {
  private LodWorldMapPresentationData() { }

  public static final Registrar<WorldMapPresentationProfileEntry, RegisterWorldMapPresentationProfilesEvent> PRESENTATION_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.worldMapPresentationProfiles, LodMod.MOD_ID);
  public static final Registrar<WorldMapBehaviour, RegisterWorldMapBehavioursEvent> BEHAVIOUR_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.worldMapBehaviours, LodMod.MOD_ID);

  public static final RegistryDelegate<WorldMapPresentationProfileEntry> PRESENTATION = PRESENTATION_REGISTRAR.register("wmap_presentation", () -> {
    final WorldMapPresentationProfile profile = WorldMapPresentationProfile.legacy();
    return new WorldMapPresentationProfileEntry(id -> profile);
  });
  public static final RegistryDelegate<WorldMapBehaviour> BEHAVIOUR = BEHAVIOUR_REGISTRAR.register("wmap_behaviour", () -> new WorldMapBehaviour(0, (definition, rules) -> {
    rules.capability(WorldMapTravel.Capability.COOLON, progression -> progression.storyFlag(0x15a));
    rules.capability(WorldMapTravel.Capability.QUEEN_FURY_BOARDING, progression -> progression.storyFlag(0x97));
  }));

  public static void register(final RegisterWorldMapPresentationProfilesEvent event) {
    PRESENTATION_REGISTRAR.registryEvent(event);
  }

  public static void register(final RegisterWorldMapBehavioursEvent event) {
    BEHAVIOUR_REGISTRAR.registryEvent(event);
  }
}
