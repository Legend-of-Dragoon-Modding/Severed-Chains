package legend.lodmod;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

import static legend.lodmod.LodMod.id;

public final class Legacy {
  private Legacy() { }

  public static final RegistryId[] CHAR_IDS = {
    id("dart"),
    id("lavitz"),
    id("shana"),
    id("rose"),
    id("haschel"),
    id("albert"),
    id("meru"),
    id("kongol"),
    id("miranda"),
  };

  public static int indexForCharId(@Nullable final RegistryId id) {
    if(id != null) {
      for(int i = 0; i < CHAR_IDS.length; i++) {
        if(CHAR_IDS[i].equals(id)) {
          return i;
        }
      }
    }

    return -1;
  }
}
