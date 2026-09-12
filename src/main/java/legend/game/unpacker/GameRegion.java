package legend.game.unpacker;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

// Stuff that differs per retail build. Don't reuse offsets across regions, they won't line up.
public enum GameRegion {
  US(
    new String[] {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"},
    "SCUS_944.91", 0x3d0, 0x51c, 0x544, 0xb6744, null, false
  ),

  FR(
    new String[] {"SCES03044", "SCES13044", "SCES23044", "SCES33044"},
    "SCES_030.44", 0x3dc, 0x528, 0x550, 0xb6a3c,
    " !\"#$'()*+,-./0123456789:;?" +
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "\u00ab\u00bb_" +
    "abcdefghijklmnopqrstuvwxyz" +
    "\u00b7\u00b0\u00c7\u00c9" +
    "\u00e0\u00e1\u00e2\u00e7" +
    "\u00e8\u00e9\u00ea\u00eb" +
    "\u00ee\u00ef",
    true
  );

  public final String[] diskIds;
  public final String executable;
  public final int shadowCtmdOffset;
  public final int shadowAnimOffset;
  public final int shadowTimOffset;
  public final int fontTimOffset;
  // null = use the US table in LodString
  @Nullable
  public final String charset;
  public final boolean skipRetailFmvs;

  GameRegion(final String[] diskIds, final String executable, final int shadowCtmdOffset, final int shadowAnimOffset, final int shadowTimOffset, final int fontTimOffset, @Nullable final String charset, final boolean skipRetailFmvs) {
    this.diskIds = diskIds;
    this.executable = executable;
    this.shadowCtmdOffset = shadowCtmdOffset;
    this.shadowAnimOffset = shadowAnimOffset;
    this.shadowTimOffset = shadowTimOffset;
    this.fontTimOffset = fontTimOffset;
    this.charset = charset;
    this.skipRetailFmvs = skipRetailFmvs;
  }

  public Path filesDir() {
    return Path.of(".", "files", this.name().toLowerCase(Locale.ROOT));
  }

  public int diskIndex(final String volumeId) {
    return Arrays.asList(this.diskIds).indexOf(volumeId);
  }

  @Nullable
  public static GameRegion byName(@Nullable final String name) {
    return Arrays.stream(values()).filter(r -> r.name().equalsIgnoreCase(name)).findFirst().orElse(null);
  }
}
