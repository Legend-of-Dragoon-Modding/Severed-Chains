package legend.game.additions;

public class AdditionSound {
  /// These two values were extracted from the PCS. {@link #audioFile_06} was used as an index into a table to pull these from a packed value.
  public int soundIndex;
  public int initialDelay;

  public AdditionSound(final int soundIndex, final int initialDelay) {
    this.soundIndex = soundIndex;
    this.initialDelay = initialDelay;
  }
}
