package legend.lodmod;

import legend.core.GameEngine;
import legend.game.tim.Tim;
import legend.game.tmd.TmdWithId;
import legend.game.wmap.Continent;
import legend.game.wmap.registries.RegisterWorldMapRegionsEvent;
import legend.game.wmap.registries.WorldMapRegionEntry;
import legend.game.wmap.world.WorldMapCameraSettings;
import legend.game.wmap.world.WorldMapModelAssets;
import legend.game.wmap.world.WorldMapModelProvider;
import legend.game.wmap.world.WorldMapPresentationController;
import legend.game.wmap.world.WorldMapPortal;
import legend.game.wmap.world.WorldMapRegion;
import org.legendofdragoon.modloader.registries.Registrar;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.DrgnFiles.loadDrgnFile;

/** Retail map assets and camera defaults, replaceable through the region registry. */
public final class LodWorldMapRegions {
  private LodWorldMapRegions() { }

  private static final Registrar<WorldMapRegionEntry, RegisterWorldMapRegionsEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.worldMapRegions, LodMod.MOD_ID);

  static {
    for(final Continent continent : Continent.values()) {
      if(continent != Continent.NONE_8) {
        REGISTRAR.register(WorldMapRegion.legacyId(continent).entryId(), () -> new WorldMapRegionEntry(id -> new WorldMapRegion(continent, model(continent), WorldMapCameraSettings.legacy(null), () -> new WorldMapPresentationController() {
          @Override
          public boolean routeVisible(final WorldMapPortal portal, final boolean visible) {
            return visible && (continent != Continent.ENDINESS_7 || portal.legacyIndex() == 31 || portal.legacyIndex() == 78);
          }
        })));
      }
    }
  }

  public static WorldMapModelProvider model(final Continent continent) {
    return state -> loadDrgnDir(0, 5697 + continent.continentNum).thenCombine(loadDrgnFile(0, 5705 + continent.continentNum), (textures, model) -> new WorldMapModelAssets(new TmdWithId("Map model DRGN0/" + (5705 + continent.continentNum), model), textures.stream().filter(file -> file.size() != 0).map(Tim::new).toList(), null, true));
  }

  public static void register(final RegisterWorldMapRegionsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
