package legend.game.sound;

import legend.game.unpacker.FileData;

/** TODO patch is the wrong name */
public class PatchList implements Sshd.Subfile {
  public final String name;
  public final int patchCount_00;
  public final SequenceList[] patches_02;

  public PatchList(final String name, final FileData data, final int offset) {
    this.name = name;
    this.patchCount_00 = data.readUShort(offset);
    this.patches_02 = new SequenceList[this.patchCount_00 + 1];

    for(int i = 0; i < this.patches_02.length; i++) {
      final int patchOffset = data.readShort(offset + 2 + i * 2);

      if(patchOffset != -1) {
        this.patches_02[i] = new SequenceList(data, offset, offset + patchOffset);
      }
    }
  }

  public static class SequenceList {
    public final int sequenceCount_00;
    public final Sequence[] sequences_02;

    public SequenceList(final FileData data, final int baseOffset, final int offset) {
      this.sequenceCount_00 = data.readUShort(offset);
      this.sequences_02 = new Sequence[this.sequenceCount_00 + 1];

      for(int i = 0; i < this.sequences_02.length; i++) {
        final int sequenceOffset = data.readShort(offset + 2 + i * 2);

        if(sequenceOffset != -1) {
          this.sequences_02[i] = new Sequence(data, baseOffset + sequenceOffset);
        }
      }
    }
  }
}
