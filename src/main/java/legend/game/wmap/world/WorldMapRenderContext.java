package legend.game.wmap.world;

import legend.core.gte.MV;
import legend.game.wmap.WMap;
import legend.game.wmap.WMapModelAndAnimData258;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Frame-local values. Transform is a copy and may be changed when queuing a mod's own models. */
public record WorldMapRenderContext(WMap engineState, RegistryId region, WorldMapView view, WMapModelAndAnimData258.ZoomState zoom, MV transform, WorldMapPoint playerPosition, float frameScale, float brightness) { }
