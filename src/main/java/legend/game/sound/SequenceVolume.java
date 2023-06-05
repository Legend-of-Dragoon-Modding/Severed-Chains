package legend.game.sound;

public class SequenceVolume {
  public boolean used_00;
  public int newValue_02;
  public int remainingTime_04;
  public int totalTime_06;
  public int oldValue_08;
  public boolean used_0a;
  public int newValue_0c;
  public int remainingTime_0e;
  public int totalTime_10;
  public int oldValue_12;

  public void clear() {
    this.used_00 = false;
    this.newValue_02 = 0;
    this.remainingTime_04 = 0;
    this.totalTime_06 = 0;
    this.oldValue_08 = 0;
    this.used_0a = false;
    this.newValue_0c = 0;
    this.remainingTime_0e = 0;
    this.totalTime_10 = 0;
    this.oldValue_12 = 0;
  }
}
