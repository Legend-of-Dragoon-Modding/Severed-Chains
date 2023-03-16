package legend.game.sound;

import legend.game.unpacker.FileData;

public class SoundFileIndices {
  public static SoundFileIndices[] load(final FileData fileData) {
    final SoundFileIndices[] indices = new SoundFileIndices[fileData.size() / 2];

    for(int i = 0; i < indices.length; i++) {
      indices[i] = new SoundFileIndices();
      indices[i]._00 = fileData.readUByte(i * 2);
      indices[i].sequenceIndex_01 = fileData.readUByte(i * 2 + 1);
    }

    return indices;
  }

  public int _00;
  public int sequenceIndex_01;
}
