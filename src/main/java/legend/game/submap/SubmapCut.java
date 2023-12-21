package legend.game.submap;

import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SubmapCut extends RegistryEntry {
  public abstract void getSobjCount();
  public abstract void loadSobjTextures();
}
