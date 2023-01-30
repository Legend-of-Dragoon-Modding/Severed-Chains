package legend.game.sound;

public class SoundFileIndices {
  public static SoundFileIndices[] load(final byte[] fileData) {
    final SoundFileIndices[] indices = new SoundFileIndices[fileData.length / 2];

    for(int i = 0; i < indices.length; i++) {
      indices[i] = new SoundFileIndices();
      indices[i]._00 = fileData[i * 2] & 0xff;
      indices[i].sequenceIndex_01 = fileData[i * 2 + 1] & 0xff;
    }

    return indices;
  }

  public int _00;
  public int sequenceIndex_01;
}
