package legend.game.wmap.world;

import org.junit.jupiter.api.Test;

class WorldMapCompatibilityTest {
  @Test
  void preservesLegacyWorldMapAndModContracts() {
    WorldMapCompatibilityCheck.main(new String[0]);
  }
}
