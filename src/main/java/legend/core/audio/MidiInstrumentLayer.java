package legend.core.audio;

public interface MidiInstrumentLayer {
  int getRootKey();
  double getVolume();
  int getPitchBendMultiplier();
  int getCents();
  MidiSoundFontEntry getSoundFontEntry();
  AdsrEnvelope getAdsrEnvelope();
}
