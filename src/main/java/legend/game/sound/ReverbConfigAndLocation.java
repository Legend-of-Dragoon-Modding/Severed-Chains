package legend.game.sound;

public class ReverbConfigAndLocation {
  public final int address_00;
  public final ReverbConfig config_02;

  public ReverbConfigAndLocation(final int address, final ReverbConfig config) {
    this.address_00 = address;
    this.config_02 = config;
  }
}
