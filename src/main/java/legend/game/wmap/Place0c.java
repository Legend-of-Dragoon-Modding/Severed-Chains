package legend.game.wmap;

import javax.annotation.Nullable;

public class Place0c {
  public @Nullable String name_00;
  public final int fileIndex_04;
  /** Things a place offers like inn, shops, etc. */
  public final int servicesFlag_05;
  public final int[] soundIndices_06;

  public Place0c(final @Nullable String name, final int fileIndex, final int servicesFlag, final int[] soundIndices) {
    this.name_00 = name;
    this.fileIndex_04 = fileIndex;
    this.servicesFlag_05 = servicesFlag;
    this.soundIndices_06 = soundIndices;
  }

  @Override
  public String toString() {
    if(this.name_00 == null) {
      return "Place (Empty)";
    }

    return "Place (%s)".formatted(this.name_00);
  }
}
