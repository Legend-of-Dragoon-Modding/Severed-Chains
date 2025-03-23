package legend.game.sound;

import legend.game.unpacker.FileData;
import org.apache.commons.lang3.function.TriFunction;

public class Sshd {
  public static final long MAGIC = 0x6468_5353L; //SShd

  public final String name;
  private final FileData data;

  public int sshdSize_00;
  public int soundBankSize_04;

  private final int[] subfileOffsets = new int[28];
  /**
   * <ul>
   *   <li>0 - Same type as 4?</li>
   *   <li>1 - {@link VolumeRamp}</li>
   *   <li>3 - {@link PatchList} (only used if ptr 4 is also set?)</li>
   *   <li>4 - Embedded SSSQ file? Has 24 entries instead of 16. Most header information is 0.</li>
   * </ul>
   */
  private final Subfile[] subfiles = new Subfile[28];

  public Sshd(final String name, final FileData data) {
    this.name = name;
    if(data.readInt(0xc) != MAGIC) {
      throw new IllegalArgumentException("Invalid file magic");
    }

    this.sshdSize_00 = data.readInt(0x0);
    this.soundBankSize_04 = data.readInt(0x4);

    for(int i = 0; i < this.subfileOffsets.length; i++) {
      this.subfileOffsets[i] = data.readInt(0x10 + i * 0x4);
    }

    this.data = data;
  }

  public boolean hasSubfile(final int index) {
    return this.subfileOffsets[index] != -1;
  }

  public <T extends Subfile> T getSubfile(final int index, final TriFunction<String, FileData, Integer, T> constructor) {
    if(this.subfiles[index] == null && this.hasSubfile(index)) {
      this.subfiles[index] = constructor.apply(this.name + " subfile " + index, this.data, this.subfileOffsets[index]);
    }

    //noinspection unchecked
    return (T)this.subfiles[index];
  }

  public Subfile getSubfile(final int index) {
    return this.subfiles[index];
  }

  public int getSubfileSize(final int index) {
    final int offset = this.subfileOffsets[index];

    if(offset == -1) {
      return 0;
    }

    int nextOffset = this.data.size();
    for(final int subfileOffset : this.subfileOffsets) {
      if(subfileOffset > offset && subfileOffset < nextOffset) {
        nextOffset = subfileOffset;
      }
    }

    return nextOffset - offset;
  }

  public interface Subfile {

  }
}
