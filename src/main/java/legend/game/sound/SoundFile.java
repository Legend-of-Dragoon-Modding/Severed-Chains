package legend.game.sound;

public class SoundFile {
  public boolean used_00;
  /** short */
  public int charId_02;
  public SoundFileIndices[] indices_08;
  public byte[] ptr_0c;
  public PlayableSound0c playableSound_10;

  /** Only used if there are multiple soundbanks */
  public int spuRamOffset_14;
  /** (ubyte) */
  public int numberOfExtraSoundbanks_18;
}
