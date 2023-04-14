package legend.game.soundFinal;

public class LayerData {
  public final Layer layer;
  public final SoundBankEntry soundBankEntry;
  public final AdsrEnvelope adsrEnvelope;
  public int sampleRate;
  public int note;
  public double velocity;

  public LayerData(final Layer layer, final int adsrLevel) {
    this.layer = layer;
    this.soundBankEntry = layer.getSoundBankEntry();
    this.adsrEnvelope = layer.getAdsrEnvelope(adsrLevel);
  }
}
