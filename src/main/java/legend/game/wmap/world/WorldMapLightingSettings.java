package legend.game.wmap.world;

import java.util.List;
import java.util.Objects;

/**
 * Region lighting and overview dimming. Omitted preset settings retain retail lighting.
 * Retail dimming snaps to overviewBrightness below transitionBrightness; the return fade
 * starts at transitionBrightness. Keeping both values preserves the native transition.
 */
public record WorldMapLightingSettings(WorldMapPoint ambient, List<Light> lights, float overviewBrightness, float transitionStep, float transitionBrightness) {
  private static final Light RETAIL_LIGHT = new Light(new WorldMapPoint(0.24414062f, 0.024414062f, 0.0f), new WorldMapPoint(0.125f, 0.125f, 0.125f));
  public static final WorldMapLightingSettings DEFAULT = new WorldMapLightingSettings(new WorldMapPoint(0.375f, 0.375f, 0.375f), List.of(RETAIL_LIGHT, RETAIL_LIGHT, RETAIL_LIGHT), 0.125f, 0.140625f, 0.25f);

  public WorldMapLightingSettings(final WorldMapPoint ambient, final List<Light> lights, final float overviewBrightness, final float transitionStep) {
    this(ambient, lights, overviewBrightness, transitionStep, overviewBrightness);
  }

  public WorldMapLightingSettings {
    requireColour(ambient);
    lights = List.copyOf(lights);
    if(lights.size() != 3) throw new IllegalArgumentException("World map lighting requires exactly three directional lights");
    if(!Float.isFinite(overviewBrightness) || overviewBrightness < 0.0f || overviewBrightness > 1.0f) throw new IllegalArgumentException("World map overview brightness must be between 0 and 1");
    if(!Float.isFinite(transitionStep) || transitionStep <= 0.0f) throw new IllegalArgumentException("World map lighting transition step must be positive and finite");
    if(!Float.isFinite(transitionBrightness) || transitionBrightness < overviewBrightness || transitionBrightness > 1.0f) throw new IllegalArgumentException("World map lighting transition brightness must be between overview brightness and 1");
  }

  public record Light(WorldMapPoint direction, WorldMapPoint colour) {
    public Light {
      Objects.requireNonNull(direction, "direction");
      if(!Float.isFinite(direction.x()) || !Float.isFinite(direction.y()) || !Float.isFinite(direction.z()) || direction.x() == 0.0f && direction.y() == 0.0f && direction.z() == 0.0f) {
        throw new IllegalArgumentException("World map light direction must be finite and nonzero");
      }
      requireColour(colour);
    }
  }

  private static void requireColour(final WorldMapPoint colour) {
    Objects.requireNonNull(colour, "colour");
    if(!component(colour.x()) || !component(colour.y()) || !component(colour.z())) {
      throw new IllegalArgumentException("World map light colour components must be between 0 and 1");
    }
  }

  private static boolean component(final float value) {
    return Float.isFinite(value) && value >= 0.0f && value <= 1.0f;
  }
}
