package legend.core.audio;

public interface MidiInstrumentLayer {
  int getRootKey();
  double getLockedVolume();
  double getVolume();
  int getPitchBendMultiplier();
  int getPan();
  int getCents();
  boolean isLockedPanAndVolume();
  boolean isPitchBendMultiplierFromInstrument();

  MidiSoundFontEntry getSoundFontEntry();
  AdsrEnvelope getAdsrEnvelope();
}
