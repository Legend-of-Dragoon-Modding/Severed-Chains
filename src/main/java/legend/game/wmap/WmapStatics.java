package legend.game.wmap;

import legend.game.tmd.UvAdjustmentMetrics14;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.Arrays;

import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_DOWN;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_LEFT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_RIGHT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_UP;

public final class WmapStatics {
  public static final Vector3f[] placePositionVectors_800c74b8 = new Vector3f[257];
  static {
    Arrays.setAll(placePositionVectors_800c74b8, i -> new Vector3f());
  }
  public static final int[] placeIndices_800c84c8 = new int[257];

  public static final UvAdjustmentMetrics14[] tmdUvAdjustmentMetrics_800eee48 = {
    UvAdjustmentMetrics14.NONE,
    new UvAdjustmentMetrics14( 1,  608, 256),
    new UvAdjustmentMetrics14( 2,  624, 256),
    new UvAdjustmentMetrics14( 3,  576, 384),
    new UvAdjustmentMetrics14( 4,  592, 384),
    new UvAdjustmentMetrics14( 5,  608, 384),
    new UvAdjustmentMetrics14( 6,  624, 384),
    new UvAdjustmentMetrics14( 7,  672, 256),
    new UvAdjustmentMetrics14( 8,  688, 256),
    new UvAdjustmentMetrics14( 9,  640, 384),
    new UvAdjustmentMetrics14(10,  656, 384),
    new UvAdjustmentMetrics14(11,  672, 384),
    new UvAdjustmentMetrics14(12,  688, 384),
    new UvAdjustmentMetrics14(13,  736, 256),
    new UvAdjustmentMetrics14(14,  752, 256),
    new UvAdjustmentMetrics14(15,  704, 256),
    new UvAdjustmentMetrics14(16,  720, 256),
    new UvAdjustmentMetrics14(17, 1008, 256),
    new UvAdjustmentMetrics14(18,  576, 256),
    new UvAdjustmentMetrics14(19,  640, 256),
    new UvAdjustmentMetrics14(20,  736, 256),
    new UvAdjustmentMetrics14(21,  704, 384),
  };

  /** These are where the 3D map disappears towards when you fully zoom out */
  public static final Vector3i[] mapPositions_800ef1a8 = {
    new Vector3i(-1550, - 8000,   900),
    new Vector3i(-2800, -20000, -1200),
    new Vector3i(- 750, -13000, - 450),
    new Vector3i(-1000, -24000, -2500),
    new Vector3i(  190, - 8600, -1640),
    new Vector3i( 1700, -10000, -1950),
    new Vector3i(  780, -10000, - 200),
    new Vector3i(   80, - 1700, -  80),
  };
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final CoolonWarpDestination20[] coolonWarpDest_800ef228 = legend.lodmod.LodWorldMapData.coolonWarpDest_800ef228;
  public static final int[] waterClutYs_800ef348 = {485, 486, 487, 488, 489, 490, 491, 492, 491, 490, 489, 488, 487, 486};
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final int[][] encounterIds_800ef364 = legend.lodmod.LodWorldMapData.encounterIds_800ef364;

  /**
   * <ol start="0">
   *   <li>Dart</li>
   *   <li>Queen Fury</li>
   *   <li>Coolon</li>
   *   <li>Teleporter</li>
   * </ol>
   */
  public static final int[] playerAvatarVramSlots_800ef694 = {15, 16, 20, 21};
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final int[][] teleportationEndpointIndices_800ef698 = legend.lodmod.LodWorldMapData.teleportationEndpointIndices_800ef698;
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final TeleportationLocation0c[] teleportationLocations_800ef6c8 = legend.lodmod.LodWorldMapData.teleportationLocations_800ef6c8;

  public static final String[] services_800f01cc = {
    "Save Point",
    "Hotel",
    "Clinic",
    "Weapon Shop",
    "Item Shop",
  };
  /** The regions you can travel to at Barrier Stations */
  public static final String[] regions_800f01ec = {"South of Serdio", "North of Serdio", "Tiberoa"};

  /** Each element is an input value mask, with values counter-clockwise from north */
  public static final RegistryDelegate[][] positiveDirectionMovementMask_800f0204 = {
    {INPUT_ACTION_GENERAL_MOVE_UP},
    {INPUT_ACTION_GENERAL_MOVE_UP, INPUT_ACTION_GENERAL_MOVE_LEFT},
    {INPUT_ACTION_GENERAL_MOVE_LEFT},
    {INPUT_ACTION_GENERAL_MOVE_LEFT, INPUT_ACTION_GENERAL_MOVE_DOWN},
    {INPUT_ACTION_GENERAL_MOVE_DOWN},
    {INPUT_ACTION_GENERAL_MOVE_DOWN, INPUT_ACTION_GENERAL_MOVE_RIGHT},
    {INPUT_ACTION_GENERAL_MOVE_RIGHT},
    {INPUT_ACTION_GENERAL_MOVE_RIGHT, INPUT_ACTION_GENERAL_MOVE_UP},
  };
  /** Each element is an input value mask, with values counter-clockwise from south */
  public static final RegistryDelegate[][] negativeDirectionMovementMask_800f0210 = {
    {INPUT_ACTION_GENERAL_MOVE_DOWN},
    {INPUT_ACTION_GENERAL_MOVE_DOWN, INPUT_ACTION_GENERAL_MOVE_RIGHT},
    {INPUT_ACTION_GENERAL_MOVE_RIGHT},
    {INPUT_ACTION_GENERAL_MOVE_RIGHT, INPUT_ACTION_GENERAL_MOVE_UP},
    {INPUT_ACTION_GENERAL_MOVE_UP},
    {INPUT_ACTION_GENERAL_MOVE_UP, INPUT_ACTION_GENERAL_MOVE_LEFT},
    {INPUT_ACTION_GENERAL_MOVE_LEFT},
    {INPUT_ACTION_GENERAL_MOVE_LEFT, INPUT_ACTION_GENERAL_MOVE_DOWN},
  };

  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final Place0c[] places_800f0234 = legend.lodmod.LodWorldMapData.places_800f0234;

  /** Valid entries seem to end at 158, though there seem to be some 0 entries scattered throughout as well */
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final Location14[] locations_800f0e34 = legend.lodmod.LodWorldMapData.locations_800f0e34;
  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final DirectionalPathSegmentData08[] directionalPathSegmentData_800f2248 = legend.lodmod.LodWorldMapData.directionalPathSegmentData_800f2248;

  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final int[] pathSegmentLengths_800f5810 = legend.lodmod.LodWorldMapData.pathSegmentLengths_800f5810;

  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static final WMapDestinationMarker2c[] wmapDestinationMarkers_800f5a6c = legend.lodmod.LodWorldMapData.wmapDestinationMarkers_800f5a6c;

  /** Legacy bootstrap alias. Active WMAP data comes from registries. */
  @Deprecated
  public static Vector3f[][] pathDotPosArr_800f591c = legend.lodmod.LodWorldMapData.pathDotPosArr_800f591c;

  /** This is a hack to "fix" a bug caused by the game loading too fast. Without this delay, Dart will automatically walk forward a bit when leaving a submap. */
  public static int loadWait = 60 / vsyncMode_8007a3b8;

  private WmapStatics() {
  }
}
