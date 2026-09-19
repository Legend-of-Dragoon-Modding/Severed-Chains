package legend.game.wmap.world;

/** Defines how an attributed rule combines with the decision built before it. */
public enum WorldMapRuleComposition {
  REPLACE,
  REQUIRE_ALL,
  ALLOW_ANY,
  VETO,
}
