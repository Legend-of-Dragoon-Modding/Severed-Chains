package legend.game.sound;

import legend.core.MathHelper;

public class PatchList implements Sshd.Subfile {
  public final int patchCount_00;
  public final SequenceList[] patches_02;

  public PatchList(final byte[] data, final int offset) {
    this.patchCount_00 = MathHelper.getUshort(data, offset);
    this.patches_02 = new SequenceList[this.patchCount_00 + 1];

    for(int i = 0; i < this.patches_02.length; i++) {
      final int patchOffset = MathHelper.getShort(data, offset + 2 + i * 2);

      if(patchOffset != -1) {
        this.patches_02[i] = new SequenceList(data, offset, offset + patchOffset);
      }
    }
  }

  public static class SequenceList {
    public final int sequenceCount_00;
    public final Sequence[] sequences_02;

    public SequenceList(final byte[] data, final int baseOffset, final int offset) {
      this.sequenceCount_00 = MathHelper.getUshort(data, offset);
      this.sequences_02 = new Sequence[this.sequenceCount_00 + 1];

      for(int i = 0; i < this.sequences_02.length; i++) {
        final int sequenceOffset = MathHelper.getShort(data, offset + 2 + i * 2);

        if(sequenceOffset != -1) {
          this.sequences_02[i] = new Sequence(data, baseOffset + sequenceOffset);
        }
      }
    }
  }
}
