package legend.game.wmap.world;

/** Result of requesting travel, not completion of the queued fade/load operation. */
public enum WorldMapTravelRequestResult {
  ACCEPTED,
  /** The engine is not ready for travel, or another transition/request is active. */
  BUSY,
  /** Progression access denied the target, or no accessible portal binds the node/route. */
  DENIED,
  /** A WorldMapWarpEvent listener cancelled the request. */
  CANCELLED,
}
